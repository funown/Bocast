package per.funown.bocast.modules.home.view.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import per.funown.bocast.library.model.ItunesSearchResultList;
import per.funown.bocast.library.model.RssChannel;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.utils.DateUtils;
import per.funown.bocast.library.utils.RssFetchUtils;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.iTunesCategory;
import per.funown.bocast.library.model.MediaBrowserProvider;
import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.modules.home.model.EpisodeViewModel;
import per.funown.bocast.modules.home.model.PodcastViewModel;
import per.funown.bocast.modules.home.model.SubscribedPodcastViewModel;
import per.funown.bocast.modules.home.databinding.FragmentPodcastDetailBinding;
import per.funown.bocast.modules.home.view.adapter.PodcastRssEpisodeCellAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
@Route(path = ArouterConstant.FRAGMENT_PODCAST_DETAIL)
public class PodcastDetailFragment extends Fragment {

  private static final String PODCAST_MEDIA_ID = "Podcast_media_id";
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
  private EpisodeViewModel episodeViewModel;
  private SubscribedPodcastViewModel subscribedPodcastViewModel;

  private boolean isFirstLoad = true;
  private boolean isReverse = false;
  private boolean isSubscribrd = false;

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
    episodeViewModel = new ViewModelProvider(requireActivity()).get(EpisodeViewModel.class);
    subscribedPodcastViewModel = new ViewModelProvider(getActivity())
        .get(SubscribedPodcastViewModel.class);
    binding = FragmentPodcastDetailBinding.inflate(getLayoutInflater());
    getLifecycle().addObserver(episodeViewModel);
    initData();
    initEvent();
  }

  @Override
  public void onResume() {
    super.onResume();
//    if (isFirstLoad) {
//      initData();
//      initEvent();
//      isFirstLoad = false;
//    }

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
    episodeViewModel.setCurrentPodcast((RssFeed) null);
    super.onDestroy();
  }

  private void initView() {
    episodeCellAdapter = new PodcastRssEpisodeCellAdapter(getContext());
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

    episodeViewModel.getCurrentPodcast().observe(this.getViewLifecycleOwner(), currentPodcast -> {
      if (currentPodcast != null) {
        isSubscribed = subscribedPodcastViewModel.isSubscribed(currentPodcast);
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

        for (SubscribedPodcast podcast : subscribedPodcastViewModel.getAllPodcasts().getValue()) {
          if (podcast.getRssLink() == channel.getLink()) {
            binding.SubscribeCheck.setChecked(true);
          }
        }
        binding.SubscribeCheck.setOnCheckedChangeListener(((buttonView, isChecked) -> {
          List<iTunesCategory> categorys = channel.getCategorys();
          Log.e(TAG, "-->" + categorys.size());
          if (isChecked) {
            SubscribedPodcast podcast = new SubscribedPodcast(
                channel.getTitle(),
                channel.getOwner() == null ? channel.getAuthor() : channel.getOwner().getName(),
                channel.getItems().size(),
                channel.getAtomLink() == null ? channel.getLink() : channel.getAtomLink().getHref(),
                channel.getImage().getHref(),
                DateUtils.stringToDate(channel.getItems().get(0).getPubDate()),
                new Date());
            NetManager.getInstance().getItunesApiService().SearchPodcast(podcast.getTitle())
                .enqueue(
                    new Callback<ItunesSearchResultList>() {
                      @Override
                      public void onResponse(Call<ItunesSearchResultList> call,
                          Response<ItunesSearchResultList> response) {
                        if (response.isSuccessful() && response.body().getResultCount() > 0) {
                          for (ItunesResponseEntity entity : response.body().getResults()) {
                            if (entity.getFeedUrl().trim().toLowerCase()
                                .equals(podcast.getRssLink().trim().toLowerCase())) {
                              Genre[] genres = new Genre[entity.getGenres().length];
                              for (int i = 0; i < entity.getGenres().length; i++) {
                                genres[i] = new Genre(entity.getGenres()[i], 1,
                                    entity.getGenreIds()[i]);
                              }
                              addGenre(genres);
                              return;
                            }
                          }
                        }
                      }

                      @Override
                      public void onFailure(Call<ItunesSearchResultList> call, Throwable t) {
                      }
                    });
            subscribedPodcastViewModel.subscribe(podcast);
          } else {
            Log.e(TAG, "unsubscribed");
            Iterator<SubscribedPodcast> iterator = subscribedPodcastViewModel.getAllPodcasts()
                .getValue().iterator();
            while (iterator.hasNext()) {
              SubscribedPodcast subscribedPodcast = iterator.next();
              if (channel.getAtomLink() != null) {
                if (subscribedPodcast.getRssLink().equals(channel.getAtomLink().getHref())) {

                  NetManager.getInstance().getItunesApiService()
                      .SearchPodcast(subscribedPodcast.getTitle()).enqueue(
                      new Callback<ItunesSearchResultList>() {
                        @Override
                        public void onResponse(Call<ItunesSearchResultList> call,
                            Response<ItunesSearchResultList> response) {
                          if (response.isSuccessful() && response.body().getResultCount() > 0) {
                            for (ItunesResponseEntity entity : response.body().getResults()) {
                              if (entity.getFeedUrl().trim().toLowerCase()
                                  .equals(subscribedPodcast.getRssLink().trim().toLowerCase())) {
                                Genre[] genres = new Genre[entity.getGenres().length];
                                for (int i = 0; i < entity.getGenres().length; i++) {
                                  genres[i] = new Genre(entity.getGenres()[i], 1,
                                      entity.getGenreIds()[i]);
                                }
                                cutGenre(genres);
                                return;
                              }
                            }
                          }
                        }

                        @Override
                        public void onFailure(Call<ItunesSearchResultList> call, Throwable t) {
                        }
                      });

                  subscribedPodcastViewModel.unsubscribe(subscribedPodcast);
                  return;
                }
              }
            }
          }
        }));
      }
    });
  }

  private void initPlayer() {
    FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
    Fragment player = (Fragment) ARouter.getInstance().build(ArouterConstant.FRAGMENT_LISTENER)
        .navigation(getContext());
    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
    transaction.replace(binding.getRoot().getId(), player);
    transaction.addToBackStack(null);
    transaction.commit();
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
      Observable.timer(3, TimeUnit.SECONDS)
          .subscribeOn(Schedulers.io())
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(aLong -> {
            feed = RssFetchUtils.getFeed(rssLink);
            episodeViewModel.setCurrentPodcast(feed);
          }, throwable -> Log.e(TAG, "Throwable " + throwable.getMessage()));
    } else {
      episodeViewModel.setCurrentPodcast(feed);
    }
  }

  private interface EpisodeListener extends MediaBrowserProvider {

    void onMediaItemSelected(MediaBrowserCompat.MediaItem item);

    void setToolbarTitle(CharSequence title);
  }
}
