package per.funown.bocast.modules.listener.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import java.util.Collections;
import java.util.List;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.utils.ItemTouchHelperAdapter;
import per.funown.bocast.library.utils.ItemTouchHelperCallback;
import per.funown.bocast.library.utils.ItemTouchHelperViewHolder;
import per.funown.bocast.library.utils.OnStartDragListener;
import per.funown.bocast.modules.listener.R;
import per.funown.bocast.modules.listener.databinding.FragmentEpisodeListDialogListDialogBinding;

public class EpisodeListDialogFragment extends BottomSheetDialogFragment {


  private static final String TAG = EpisodeListDialogFragment.class.getSimpleName();
  private final Context context;
  FragmentEpisodeListDialogListDialogBinding binding;
  episodeAdapter adapter;
  private ItemTouchHelper mItemTouchHelper;


  private OnStartDragListener onStartDragListener = new OnStartDragListener() {

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
      mItemTouchHelper.startDrag(viewHolder);
    }
  };

  // TODO: Customize parameters
  public static EpisodeListDialogFragment newInstance(Context context) {
    final EpisodeListDialogFragment fragment = new EpisodeListDialogFragment(context);
    return fragment;
  }

  public EpisodeListDialogFragment(Context context) {
    this.context = context;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentEpisodeListDialogListDialogBinding.inflate(getLayoutInflater());
    final RecyclerView recyclerView = binding.SongList;
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new episodeAdapter(onStartDragListener);
    recyclerView.setAdapter(adapter);

    ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter,
        this.getContext());
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(recyclerView);
    return binding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.i(TAG, "resume...");
    MusicService service = ARouter.getInstance().navigation(MusicService.class);
    Log.i(TAG, String.valueOf(service.getINSTANCE().getPlayList().size()));
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

  }


  private class PlaylistItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    TextView title, author, duration;
    SimpleDraweeView logo;
    ImageView btn_delete;
    ConstraintLayout song_panel;
    LinearLayout song_item;

    PlaylistItemViewHolder(@NonNull View itemView) {
      super(itemView);
      song_item = itemView.findViewById(R.id.song_item);
      song_panel = itemView.findViewById(R.id.song_panel);
      title = itemView.findViewById(R.id.title);
      author = itemView.findViewById(R.id.author);
      duration = itemView.findViewById(R.id.duration);
      logo = itemView.findViewById(R.id.logo);
      btn_delete = itemView.findViewById(R.id.btn_delete);
    }

    @Override
    public void onItemSelected(Context context) {

    }

    @Override
    public void onItemClear(Context context) {

    }
  }

  private class episodeAdapter extends RecyclerView.Adapter<PlaylistItemViewHolder> implements
      ItemTouchHelperAdapter {

    private final OnStartDragListener onStartDragListener;
    PlayerControl playerControl;
    MusicService service;

    public episodeAdapter(OnStartDragListener onStartDragListener) {
      service = ARouter.getInstance().navigation(MusicService.class);
      playerControl = service.getINSTANCE();
      this.onStartDragListener = onStartDragListener;
    }

    @NonNull
    @Override
    public PlaylistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View itemView = inflater
          .inflate(R.layout.fragment_episode_list_dialog_list_dialog_item, parent, false);
      return new PlaylistItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistItemViewHolder holder, int position) {
      SongInfo songInfo = playerControl.getPlayList().get(position);
      if (playerControl.isCurrMusicIsPlaying(songInfo.getSongId())) {
        holder.title.setTextColor(getResources().getColor(R.color.playing, null));
      } else {
        holder.title
            .setTextColor(getResources().getColor(R.color.grey, null));
      }
      holder.title.setText(songInfo.getSongName());
      holder.author.setText(songInfo.getAlbumName());
      long min = songInfo.getDuration() / 60;
      holder.duration.setText(min + " min");
      holder.logo.setImageURI(songInfo.getSongCover());
      holder.btn_delete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          List<SongInfo> playList = playerControl.getPlayList();
          playList.remove(position);
          playerControl.updatePlayList(playList);
          notifyDataSetChanged();
        }
      });
      if (playerControl.getPlayList().size() == 1) {
        holder.btn_delete.setClickable(false);
        holder.btn_delete
            .setColorFilter(getResources().getColor(R.color.material_on_primary_disabled, null));
      } else {
        holder.btn_delete.setClickable(true);
        holder.btn_delete.setColorFilter(
            getResources().getColor(R.color.material_on_primary_emphasis_medium, null));
      }
      holder.song_panel.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          playerControl.playMusicByIndex(position);
          notifyDataSetChanged();
        }
      });

      holder.song_item.setOnTouchListener((v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          onStartDragListener.onStartDrag(holder);
        }
        return false;
      });
    }

    @Override
    public int getItemCount() {
      return playerControl.getPlayList() == null ? 0 : playerControl.getPlayList().size();
    }

    public void addItem(int position, SongInfo item) {
      List<SongInfo> playList = playerControl.getPlayList();
      playList.add(item);
      playerControl.updatePlayList(playList);
      notifyItemInserted(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
      List<SongInfo> playList = playerControl.getPlayList();
      if (fromPosition < toPosition) {
        for (int i = fromPosition; i < toPosition; i++) {
          Collections.swap(playList, i, i + 1);
        }
      } else {
        for (int i = fromPosition; i > toPosition; i--) {
          Collections.swap(playList, i, i - 1);
        }
      }
      playerControl.updatePlayList(playList);
      notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int adapterPosition) {
      final SongInfo songInfo = new SongInfo();
      SongInfo info = playerControl.getPlayList().get(adapterPosition);
      songInfo.setSongId(info.getSongId());
      songInfo.setSongName(info.getSongName());
      songInfo.setSongCover(info.getSongCover());
      songInfo.setArtist(info.getArtist());
      songInfo.setPublishTime(info.getPublishTime());
      songInfo.setSongUrl(info.getSongUrl());
      songInfo.setAlbumName(info.getAlbumName());
      songInfo.setAlbumArtist(info.getAlbumArtist());
      songInfo.setAlbumCover(info.getAlbumCover());
      songInfo.setAlbumId(info.getAlbumId());
      songInfo.setDescription(info.getDescription());

      notifyItemRemoved(adapterPosition);
      Log.e(TAG,  playerControl.getPlayList().size()+ "-->" + adapterPosition);

      service.removeSong(info);
      notifyItemRangeChanged(0, getItemCount());
      notifyDataSetChanged();
    }
  }



}
