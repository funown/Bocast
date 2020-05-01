package per.funown.bocast.modules.home.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.modules.home.databinding.FragmentHomePageBinding;
import per.funown.bocast.modules.home.model.PodcastViewModel;
import per.funown.bocast.modules.home.model.SubscribedPodcastViewModel;
import per.funown.bocast.modules.home.view.adapter.PodcastCellAdapter;
import per.funown.bocast.modules.home.view.adapter.RecommendationSectionAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ArouterConstant.FRAGMENT_HOME)
public class HomePageFragment extends Fragment {

  private static final String TAG = HomePageFragment.class.getSimpleName();
  FragmentHomePageBinding binding;

  PodcastCellAdapter podcastCellAdapter;
  RecommendationSectionAdapter recommendationSectionAdapter;

  PodcastViewModel podcastViewModel;
  SubscribedPodcastViewModel subscribedPodcastViewModel;

  @Autowired
  int containerId;

  public HomePageFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ARouter.getInstance().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    binding = FragmentHomePageBinding.inflate(getLayoutInflater());
    podcastViewModel = new ViewModelProvider(requireActivity()).get(PodcastViewModel.class);
    subscribedPodcastViewModel = new ViewModelProvider(requireActivity())
        .get(SubscribedPodcastViewModel.class);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    binding.SubscribedPodcastPanel.setLayoutManager(layoutManager);
    podcastCellAdapter = new PodcastCellAdapter();
    podcastCellAdapter.setManager(requireActivity().getSupportFragmentManager());
    podcastCellAdapter.setContainerId(containerId);
    binding.SubscribedPodcastPanel.setAdapter(podcastCellAdapter);

    recommendationSectionAdapter = new RecommendationSectionAdapter();
    recommendationSectionAdapter.setContainerId(containerId);
    recommendationSectionAdapter.setManager(requireActivity().getSupportFragmentManager());
    recommendationSectionAdapter.setOwner(getViewLifecycleOwner());
    recommendationSectionAdapter.setContext(requireContext());
    LinearLayoutManager manager = new LinearLayoutManager(requireContext());
    manager.setSmoothScrollbarEnabled(true);
    binding.RecommendationPodcastPanel.setLayoutManager(manager);
    binding.RecommendationPodcastPanel.setAdapter(recommendationSectionAdapter);


    if (subscribedPodcastViewModel.getAllPodcasts() != null) {
      subscribedPodcastViewModel.getAllPodcasts()
          .observe(getViewLifecycleOwner(), subscribedPodcasts -> {
            podcastCellAdapter.setSubscribedPodcasts(subscribedPodcasts);
            podcastCellAdapter.notifyDataSetChanged();
          });
    }

    podcastViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
      if (genres == null || genres.size() == 0) {
        genres = new ArrayList<>();
        genres.add(new Genre("Podcasts", 1, "26"));
        genres.add(new Genre("Society & Culture", 1, "1324"));
        genres.add(new Genre("News", 1, "1489"));
      }
      podcastViewModel.reset(genres);
      podcastViewModel.getTopPodcasts().observe(getViewLifecycleOwner(), topPodcasts -> {
        recommendationSectionAdapter.setTopPodcasts(topPodcasts);
        binding.RecommendationPodcastPanel.post(new Runnable() {
          @Override
          public void run() {
            recommendationSectionAdapter.notifyDataSetChanged();
          }
        });
//      if (podcastViewModel.isNeedToScrollToTop()) {
//        binding.RecommendationPodcastPanel.scrollToPosition(0);
//        podcastViewModel.setNeedToScrollToTop(false);
//      }
      });
    });

    // pull to get data
    binding.RecommendationPodcastPanel.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy < 0) {
          return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.RecommendationPodcastPanel
            .getLayoutManager();
        int itemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        if (itemPosition == recommendationSectionAdapter.getItemCount() - 1) {
          podcastViewModel.fetchTopPodcasts();
        }
      }
    });

    binding.RecommendationSwipelayoutManager.setOnRefreshListener(() -> {
      podcastViewModel.reset(podcastViewModel.getGenres().getValue());
    });

    // TODO
    podcastViewModel.getDataStatus().observe(getViewLifecycleOwner(), dataStatus -> {

    });

    // Inflate the layout for this fragment
    return binding.getRoot();
  }
}