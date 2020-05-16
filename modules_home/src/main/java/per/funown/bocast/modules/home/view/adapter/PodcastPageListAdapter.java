package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.modules.home.R;
import per.funown.bocast.modules.home.model.GenrePodcastViewModel;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastPageListAdapter extends
    PagedListAdapter<ItunesResponseEntity, ViewHolder> {

  public static final ItemCallback diffCallBack = new ItemCallback<ItunesResponseEntity>() {
    @Override
    public boolean areItemsTheSame(@NonNull ItunesResponseEntity oldItem,
        @NonNull ItunesResponseEntity newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull ItunesResponseEntity oldItem,
        @NonNull ItunesResponseEntity newItem) {
      Log.e(TAG, "-->" + (oldItem.equals(newItem)));
      return oldItem.equals(newItem);
    }
  };
  private static final String TAG = PodcastPageListAdapter.class.getSimpleName();

  FragmentManager manager;
  Context context;
  int containerId;

  NetworkState networkState = null;
  GenrePodcastViewModel viewModel;

  private boolean hasFooter = false;

  public void updateNetworkState(NetworkState networkState) {
    this.networkState = networkState;
    if (networkState.equals(NetworkState.INITIAL_LOADING)) {
      hideFooter();
    }
    else {
      showFooter();
    }
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void setManager(FragmentManager manager) {
    this.manager = manager;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public PodcastPageListAdapter(GenrePodcastViewModel viewModel) {
    super(diffCallBack);
    this.viewModel = viewModel;
  }

  protected PodcastPageListAdapter(
      @NonNull DiffUtil.ItemCallback diffCallback) {
    super(diffCallback);
  }

  private void hideFooter() {
    if (hasFooter) {
      notifyItemRemoved(getItemCount() - 1);
    }
    hasFooter = false;
  }

  private void showFooter() {
    if (hasFooter) {
      notifyItemChanged(getItemCount() - 1);
    }
    else {
      hasFooter = true;
      notifyItemInserted(getItemCount() - 1);
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == R.layout.cell_podcast) {
      return PodcastCellViewHolder.newInstance(parent);
    }
    else {
      LoadingFooterViewHolder footerViewHolder = LoadingFooterViewHolder.newInstance(parent);
      footerViewHolder.itemView.setOnClickListener(v -> {
        viewModel.retry();
      });
      return footerViewHolder;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if (holder.getItemViewType() == R.layout.loading_footer) {
      LoadingFooterViewHolder footerViewHolder = (LoadingFooterViewHolder) holder;
      ((LoadingFooterViewHolder) holder).onBindViewHolder(networkState);
    }
    else {
      if (getItem(position) == null) {
        return;
      }
      ((PodcastCellViewHolder) holder).onBindViewHolder(getItem(position), containerId, manager);
    }
  }

  @Override
  public int getItemCount() {
    return super.getItemCount() + (hasFooter ? 1 : 0);
  }

  @Override
  public int getItemViewType(int position) {
    return hasFooter && position == getItemCount() - 1 ? R.layout.loading_footer
        : R.layout.cell_podcast;
  }
}
