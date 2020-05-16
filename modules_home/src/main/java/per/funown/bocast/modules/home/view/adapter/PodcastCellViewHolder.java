package per.funown.bocast.modules.home.view.adapter;

import android.net.Uri;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.card.MaterialCardView;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.modules.home.R;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastCellViewHolder extends RecyclerView.ViewHolder {

  private MaterialCardView cardView;
  private SimpleDraweeView podcastLogo;
  private TextView podcastTitle, podcastAuthor;
  private ConstraintLayout podcastCard;

  public MaterialCardView getCardView() {
    return cardView;
  }

  public SimpleDraweeView getPodcastLogo() {
    return podcastLogo;
  }

  public TextView getPodcastTitle() {
    return podcastTitle;
  }

  public TextView getPodcastAuthor() {
    return podcastAuthor;
  }

  public ConstraintLayout getPodcastCard() {
    return podcastCard;
  }

  protected PodcastCellViewHolder(@NonNull View itemView) {
    super(itemView);
    cardView = itemView.findViewById(R.id.podcast_view);
    podcastLogo = itemView.findViewById(R.id.podcast_logo);
    podcastTitle = itemView.findViewById(R.id.podcast_title);
    podcastAuthor = itemView.findViewById(R.id.podcast_author);
    podcastCard = itemView.findViewById(R.id.podcast_card);
  }

  public static PodcastCellViewHolder newInstance(ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.cell_podcast, parent, false);
    PodcastCellViewHolder podcastCellViewHolder = new PodcastCellViewHolder(itemView);
    return podcastCellViewHolder;
  }

  public void   onBindViewHolder(ItunesResponseEntity entity, int containerId, FragmentManager manager) {
    podcastLogo.setImageURI(Uri.parse(entity.getArtworkUrl60()));
    podcastTitle.setText(entity.getTrackName());
    podcastAuthor.setText(Html.fromHtml(entity.getArtistName(), Html.FROM_HTML_MODE_LEGACY));
    podcastCard.setOnClickListener(v -> {
      Fragment podcastFragment = (Fragment) ARouter.getInstance()
          .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
          .withSerializable("rssLink", entity.getFeedUrl()).withBoolean("isSubscribed", false)
          .navigation();
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(containerId, podcastFragment);
      transaction.addToBackStack(null);
      transaction.commit();
    });

    if (podcastTitle.getLineCount() >= 2) {
      podcastTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    }
  }
}