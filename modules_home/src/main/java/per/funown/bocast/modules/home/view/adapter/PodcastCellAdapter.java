package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
import android.util.TypedValue;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import java.util.List;
import java.util.ArrayList;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;

import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.modules.home.R;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.base.BaseRecyclerViewAdapter;
import per.funown.bocast.modules.home.callback.DiffCallBack;

/**
 * <pre>
 *     author : Funown
 *     time   : 2020/02/19
 *     desc   : podcast Cell Adapter
 *     version: 1.0
 * </pre>
 */
public class PodcastCellAdapter extends BaseRecyclerViewAdapter< PodcastCellViewHolder> {

  private static final String TAG = PodcastCellAdapter.class.getSimpleName();
  List<ItunesResponseEntity> entities = new ArrayList<>();

  FragmentManager manager;
  Context context;
  int containerId;

  public void setContext(Context context) {
    this.context = context;
  }

  public void setManager(FragmentManager manager) {
    this.manager = manager;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }


  public void setEntities(List<ItunesResponseEntity> entities) {
    this.entities = entities;
  }

  @NonNull
  @Override
  public PodcastCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return PodcastCellViewHolder.newInstance(parent);
  }

  @Override
  public void onBindViewHolder(@NonNull PodcastCellViewHolder holder, int position) {
    Log.i(TAG, "--> Binding podcast cell ...");
    holder.onBindViewHolder(entities.get(position), containerId, manager);
//    ItunesResponseEntity entity = entities.get(position);
//    holder.getPodcastLogo().setImageURI(Uri.parse(entity.getArtworkUrl60()));
//    holder.getPodcastTitle().setText(entity.getTrackName());
//    holder.getPodcastAuthor().setText(entity.getArtistName());
//    holder.getPodcastCard().setOnClickListener(v -> {
//      Fragment podcastFragment = (Fragment) ARouter.getInstance()
//          .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
//          .withSerializable("rssLink", entity.getFeedUrl()).withBoolean("isSubscribed", false)
//          .navigation();
//      FragmentTransaction transaction = manager.beginTransaction();
//      transaction.replace(containerId, podcastFragment);
//      transaction.addToBackStack(null);
//      transaction.commit();
//    });
//
//    if (holder.getPodcastTitle().getLineCount() >= 2) {
//      holder.getPodcastTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//    }

  }

  @Override
  public int getItemCount() {
    return entities.size();
  }
}