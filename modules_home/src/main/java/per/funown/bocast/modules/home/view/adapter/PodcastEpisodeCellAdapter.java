package per.funown.bocast.modules.home.view.adapter;

import android.app.Activity;
import android.support.v4.media.session.MediaControllerCompat;
import com.alibaba.android.arouter.launcher.ARouter;
import java.util.List;
import java.util.ArrayList;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import per.funown.bocast.modules.home.R;
import per.funown.bocast.library.model.EpisodeSource;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastEpisodeCellAdapter extends
    RecyclerView.Adapter<PodcastEpisodeCellAdapter.PodcastEpisodeCellViewHolder> {

  private List<MediaItem> episodeList = new ArrayList<>();
  private Activity activity;

  public void setEpisodeList(List<MediaItem> episodeList) {
    this.episodeList = episodeList;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  @NonNull
  @Override
  public PodcastEpisodeCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.cell_podcast_episode, parent, false);
    return new PodcastEpisodeCellViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PodcastEpisodeCellViewHolder holder, int position) {
    MediaItem episode = episodeList.get(position);

    MediaDescriptionCompat description = episode.getDescription();
    Bundle extras = description.getExtras();
    holder.episodeTitle.setText(description.getTitle());
    holder.updateTime_durationTime.setText(
        String.format("%s · %i", extras.getString(EpisodeSource.CUSTOM_METADATA_TRACK_PUBDATE),
            Integer.valueOf(extras.getString(MediaMetadataCompat.METADATA_KEY_DURATION)) / 60));
    holder.btn_play.setOnClickListener(v -> {
      MediaControllerCompat.getMediaController(activity).getTransportControls()
          .playFromUri(episode.getDescription().getMediaUri(), episode.getDescription().getExtras());
    });
    Bundle bundle = new Bundle();
    bundle.putInt("Episode", position);
    holder.itemView.setOnClickListener(v -> {
      ARouter.getInstance().build("/bocast/home/podcast/episode/detail").navigation();
    });
  }

  @Override
  public int getItemCount() {
    return episodeList.size();
  }

  /**
   *
   */
  class PodcastEpisodeCellViewHolder extends RecyclerView.ViewHolder {

    private ImageView btn_play;
    private TextView episodeTitle, updateTime_durationTime;

    public PodcastEpisodeCellViewHolder(@NonNull View itemView) {
      super(itemView);
      btn_play = itemView.findViewById(R.id.btn_play);
      episodeTitle = itemView.findViewById(R.id.Episode_Title);
      updateTime_durationTime = itemView.findViewById(R.id.Update_time);
    }
  }

}
