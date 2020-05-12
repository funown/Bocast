package per.funown.bocast.modules.home.view.fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.modules.home.callback.DiffCallBack;
import per.funown.bocast.modules.home.databinding.FragmentPodcastsBinding;
import per.funown.bocast.modules.home.model.GenrePodcastViewModel;
import per.funown.bocast.modules.home.view.adapter.PodcastPageListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ArouterConstant.FRAGMENT_PODCASTS)
public class PodcastsFragment extends Fragment {

  private static final String TAG = PodcastsFragment.class.getSimpleName();
  FragmentPodcastsBinding binding;
  GenrePodcastViewModel viewModel;
  PodcastPageListAdapter adapter;

  @Autowired
  String genreId;

  public PodcastsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ARouter.getInstance().inject(this);
    binding = FragmentPodcastsBinding.inflate(getLayoutInflater());
    viewModel = new ViewModelProvider(this).get(genreId, GenrePodcastViewModel.class);
    viewModel.setPodcastList(genreId);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    adapter = new PodcastPageListAdapter(viewModel);
    adapter.setContext(requireContext());
    adapter.setContainerId(binding.getRoot().getId());
    adapter.setManager(requireActivity().getSupportFragmentManager());

    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
        StaggeredGridLayoutManager.VERTICAL);
    binding.list.setLayoutManager(layoutManager);
    binding.list.setAdapter(adapter);

    viewModel.getPodcastList().observe(getViewLifecycleOwner(), itunesResponseEntities -> {
      adapter.submitList(itunesResponseEntities);
      adapter.notifyDataSetChanged();
    });

    viewModel.getNetworkStatus().observe(getViewLifecycleOwner(), networkState -> {
      adapter.updateNetworkState(networkState);
    });

    // Inflate the layout for this fragment
    return binding.getRoot();
  }
}
