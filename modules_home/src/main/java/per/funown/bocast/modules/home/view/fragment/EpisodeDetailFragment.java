package per.funown.bocast.modules.home.view.fragment;

import android.net.Uri;
import android.os.Bundle;

import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import com.alibaba.android.arouter.launcher.ARouter;
import java.util.Date;
import java.util.Locale;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.utils.DateUtils;
import per.funown.bocast.library.utils.DateUtils.DatePattern;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.home.model.CurrentPodcastViewModel;
import per.funown.bocast.modules.home.databinding.FragmentEpisodeDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ArouterConstant.FRAGMENT_PODCAST_EPISODE_DETAIL)
public class EpisodeDetailFragment extends Fragment {

  private static final String TAG = EpisodeDetailFragment.class.getSimpleName();
  FragmentEpisodeDetailBinding binding;

  @Autowired(name = "item", required = false)
  RssItem item;

  @Autowired(name = "guid")
  String guid;

  @Autowired(name = "feed")
  String feed;

  public EpisodeDetailFragment() {
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
    if (item == null) {
      Toast.makeText(getContext(), "Fetch Data Error", Toast.LENGTH_LONG);
    }
    binding = FragmentEpisodeDetailBinding.inflate(getLayoutInflater());
    binding.EpisodeTitle.setText(item.getTitle());
    binding.PodcastAuthor.setText(item.getAuthor());
    binding.imageView.setImageURI(Uri.parse(item.getImage().getHref()));
    binding.ShowNotes
        .loadData(item.getSummary() == null ? item.getDescription() : item.getSummary(),
            "text/html", "UTF-8");

    Date date = DateUtils
        .stringToDate(item.getPubDate().trim(), DatePattern.RSS_DATE, Locale.ENGLISH);
    String pubDate = DateUtils
        .dateToString(date, DatePattern.ONLY_MINUTE);
    if (item.getDuration().trim().contains(":")) {
      binding.pubDate.setText(pubDate + " " + item.getDuration());
    } else {
      int duration = Integer.parseInt(item.getDuration().trim());
      int mins = duration / 60;
      int seconds = duration % 60;
      binding.pubDate.setText(item.getPubDate() + " " + mins + ":" + seconds);
    }

    binding.PodcastAuthor.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
            .withString("rssLink", feed).navigation();
        FragmentTransitionUtil.getINSTANCE().transit(fragment);
      }
    });

    return binding.getRoot();
  }
}
