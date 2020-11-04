package per.funown.bocast.modules.home.view.fragment;

import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.annotation.SuppressLint;
import android.view.View.OnKeyListener;
import android.view.View.OnClickListener;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.annotation.Autowired;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import per.funown.bocast.library.utils.RssFetchUtils;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.library.net.service.iTunesSearchService;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssChannel;
import per.funown.bocast.library.model.iTunesCategory;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.modules.home.R;
import per.funown.bocast.modules.home.model.CurrentPodcastViewModel;
import per.funown.bocast.modules.home.model.EpisodesViewModel;
import per.funown.bocast.modules.home.model.PodcastViewModel;
import per.funown.bocast.modules.home.model.SubscribedPodcastViewModel;
import per.funown.bocast.modules.home.databinding.FragmentPodcastDetailBinding;
import per.funown.bocast.modules.home.view.adapter.PodcastRssEpisodeCellAdapter;

/**
 *
 */
@Route(path = ArouterConstant.FRAGMENT_PODCAST_DETAIL)
public class PodcastDetailFragment extends Fragment {
  private static final String TAG = PodcastDetailFragment.class.getSimpleName();

  @Autowired
  boolean isSubscribed;

  @Autowired
  String rssLink;

  @Autowired
  RssFeed feed;

  private FragmentPodcastDetailBinding binding;
  private PodcastRssEpisodeCellAdapter episodeCellAdapter;

  private PodcastViewModel podcastViewModel;
  private EpisodesViewModel episodesViewModel;
  private CurrentPodcastViewModel currentPodcastViewModel;
  private SubscribedPodcastViewModel subscribedPodcastViewModel;

  private boolean isReverse = false;

  @Override
  public void onStart() {
    super.onStart();
    Log.e(TAG, "on start...");
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e(TAG, "on creating...");
    ARouter.getInstance().inject(this);
    podcastViewModel = new ViewModelProvider(requireActivity()).get(PodcastViewModel.class);
    currentPodcastViewModel = new ViewModelProvider(requireActivity())
        .get(CurrentPodcastViewModel.class);
    subscribedPodcastViewModel = new ViewModelProvider(getActivity())
        .get(SubscribedPodcastViewModel.class);
    episodesViewModel = new ViewModelProvider(requireActivity()).get(EpisodesViewModel.class);
    binding = FragmentPodcastDetailBinding.inflate(getLayoutInflater());
//    getLifecycle().addObserver(currentPodcastViewModel);
    currentPodcastViewModel.setContext(requireContext());
    initData();
    initEvent();
  }

  @Override
  public void onResume() {
    super.onResume();

    getView().setFocusableInTouchMode(true);
    getView().requestFocus();
    getView().setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
          getActivity().getSupportFragmentManager().popBackStack();
          return true;
        } else {
          return false;
        }
      }
    });
  }

  private void initEvent() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Log.e(TAG, "Showing Podcast's detail...");
    binding.getRoot().setClickable(true);
    initView();
    return binding.getRoot();
  }

  @Override
  public void onDestroy() {
    currentPodcastViewModel.setCurrentPodcast((RssFeed) null);
    super.onDestroy();
  }

  private void initView() {
    episodeCellAdapter = new PodcastRssEpisodeCellAdapter(getContext(), episodesViewModel,
        subscribedPodcastViewModel);
    episodeCellAdapter.setActivity(requireActivity());
    episodeCellAdapter.setItems(new ArrayList<>());
    episodeCellAdapter.setContainerId(binding.getRoot().getId());
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    linearLayoutManager.setItemPrefetchEnabled(true);
    binding.PodcastEpisodes.setLayoutManager(linearLayoutManager);
    binding.PodcastEpisodes.setAdapter(episodeCellAdapter);
    binding.PodcastEpisodes.setNestedScrollingEnabled(false);

    binding.SortBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        isReverse = !isReverse;
        binding.SortBtn.setRotation(isReverse ? 180 : 0);
        LinearLayoutManager manager = (LinearLayoutManager) binding.PodcastEpisodes
            .getLayoutManager();
        manager.setReverseLayout(isReverse);
        binding.PodcastEpisodes.setLayoutManager(manager);
      }
    });

    currentPodcastViewModel.getCurrentPodcast()
        .observe(this.getViewLifecycleOwner(), currentPodcast -> {
          if (currentPodcast != null && feed != null && currentPodcast.getChannel().getTitle()
              .equals(feed.getChannel().getTitle())) {
            episodeCellAdapter.setFeed(currentPodcast);
            RssChannel channel = currentPodcast.getChannel();
            binding.PodcastAuthor.setText(
                channel.getOwner() == null ? channel.getAuthor() : channel.getOwner().getName());
            binding.PodcastTitle.setText(channel.getTitle());
            binding.PodcastIntro.setText(channel.getDescription());
            binding.PodcastLogo.setImageURI(Uri.parse(channel.getImage().getHref()));
            binding.SubscribeCheck.setChecked(isSubscribed);
            episodeCellAdapter.setItems(channel.getItems());
            episodeCellAdapter.notifyDataSetChanged();
            long podcastId = subscribedPodcastViewModel.isSubscribed(currentPodcast);
            if (podcastId == 0) {
              binding.SubscribeCheck.setChecked(false);
              isSubscribed = false;
            } else {
              episodeCellAdapter.setPodcastId(podcastId);
            }
            binding.SubscribeCheck.setOnCheckedChangeListener(((buttonView, isChecked) -> {
              List<iTunesCategory> categorys = channel.getCategorys();
              // subscribe op
              if (isChecked) {
                Podcast podcast = new Podcast(
                    channel.getTitle(),
                    channel.getOwner() == null ? channel.getAuthor() : channel.getOwner().getName(),
                    channel.getItems().size(),
                    channel.getAtomLink() == null ? channel.getLink()
                        : channel.getAtomLink().getHref(),
                    channel.getImage().getHref());
                List<ItunesResponseEntity> itunesResponseEntities = iTunesSearchService
                    .SearchTerms(podcast.getTitle());
                if (itunesResponseEntities != null && itunesResponseEntities.size() > 0) {
                  for (ItunesResponseEntity entity : itunesResponseEntities) {
                    if (entity.getFeedUrl().trim().toLowerCase()
                        .equals(podcast.getRssLink().trim().toLowerCase())) {
                      Genre[] genres = new Genre[entity.getGenres().length];
                      for (int i = 0; i < entity.getGenres().length; i++) {
                        genres[i] = new Genre(entity.getGenres()[i], 1,
                            entity.getGenreIds()[i]);
                      }
                      addGenre(genres);
                      break;
                    }
                  }
                }
                subscribedPodcastViewModel.subscribe(podcast);
              }
              // unsubscribe op
              else {
                Log.e(TAG, "unsubscribed");
                SubscribedPodcast subscribedPodcast = subscribedPodcastViewModel
                    .getSubscribedPodcast(podcastId);
                if (subscribedPodcast != null) {
                  List<ItunesResponseEntity> itunesResponseEntities = iTunesSearchService
                      .SearchTerms(currentPodcast.getChannel().getTitle());
                  itunesResponseEntities.forEach(entity -> {
                    if (entity.getFeedUrl()
                        .equals(currentPodcast.getChannel().getAtomLink().getHref())) {
                      Genre[] genres = new Genre[entity.getGenres().length];
                      for (int i = 0; i < entity.getGenres().length; i++) {
                        genres[i] = new Genre(entity.getGenres()[i], 1,
                            entity.getGenreIds()[i]);
                      }
                      cutGenre(genres);
                      return;
                    }
                  });
                  subscribedPodcastViewModel.unsubscribe(subscribedPodcast);
                }
              }
            }));
          }
        });

    currentPodcastViewModel.getNetworkStatus().observe(getViewLifecycleOwner(),
        networkState -> {
          if (networkState.equals(NetworkState.LOADING)) {
            binding.loadingPage.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.VISIBLE);
            binding.mainContent.setVisibility(View.GONE);
            binding.loadingPage.setClickable(false);
            binding.LoadingMessage.setText(getText(R.string.Loaing_message));
          } else if (networkState.equals(NetworkState.LOADED)) {
            binding.loadingPage.setVisibility(View.GONE);
            binding.mainContent.setVisibility(View.VISIBLE);
          } else if (networkState.equals(NetworkState.FAILED)) {
            Toast.makeText(requireContext(), "Fetch Data Error. Please retry", Toast.LENGTH_LONG);
            binding.LoadingMessage.setText(getText(R.string.retryLoading));
            binding.loading.setVisibility(View.GONE);
            binding.loadingPage.setClickable(true);
            binding.loadingPage.setOnClickListener(v -> initData());
          }
        });

  }

  private void addGenre(Genre... genres) {
    Map<String, Genre> mapGenres = podcastViewModel.getMapGenres();
    for (int i = 0; i < genres.length; i++) {
      if (mapGenres.containsKey(genres[i].getGenre())) {
        podcastViewModel.addWeight(mapGenres.get(genres[i].getGenre()));
      } else {
        podcastViewModel.addGenres(genres[i]);
      }
    }
  }

  private void cutGenre(Genre... genres) {
    Map<String, Genre> mapGenres = podcastViewModel.getMapGenres();
    for (int i = 0; i < genres.length; i++) {
      if (mapGenres.containsKey(genres[i].getGenre())) {
        podcastViewModel.cutWeight(mapGenres.get(genres[i].getGenre()));
      }
    }
  }

  @SuppressLint("CheckResult")
  private void initData() {
    if (feed == null) {
      currentPodcastViewModel.setNetworkStatus(NetworkState.LOADING);
      Observable.timer(1, TimeUnit.SECONDS)
          .subscribeOn(Schedulers.io())
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(aLong -> {
            feed = RssFetchUtils.getFeed(rssLink);
            if (feed != null) {
              currentPodcastViewModel.setCurrentPodcast(feed);
              currentPodcastViewModel.setNetworkStatus(NetworkState.LOADED);
            } else {
              currentPodcastViewModel.setNetworkStatus(NetworkState.FAILED);
            }
          }, throwable -> {
            Log.e(TAG, "Throwable " + throwable.getMessage());
            currentPodcastViewModel.setNetworkStatus(NetworkState.FAILED);
          });
    } else {
      currentPodcastViewModel.setCurrentPodcast(feed);
    }
  }
}
