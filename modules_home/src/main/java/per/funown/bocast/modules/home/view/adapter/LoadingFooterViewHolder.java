package per.funown.bocast.modules.home.view.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.modules.home.R;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LoadingFooterViewHolder extends ViewHolder {

  private static final String TAG = LoadingFooterViewHolder.class.getSimpleName();
  private ProgressBar loadingProgress;
  private TextView loadingMessage;

  private LoadingFooterViewHolder(@NonNull View itemView) {
    super(itemView);
    loadingMessage = itemView.findViewById(R.id.loadingMessage);
    loadingProgress = itemView.findViewById(R.id.loadingProgress);
  }

  public static LoadingFooterViewHolder newInstance(ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.loading_footer, parent, false);
    LoadingFooterViewHolder loadingFooterViewHolder = new LoadingFooterViewHolder(itemView);
    return loadingFooterViewHolder;
  }

  public void onBindViewHolder(NetworkState status) {
    Log.e(TAG, status.getStatus().name());
    if (status.equals(NetworkState.FAILED)) {
      loadingMessage.setText("Click to retry");
      itemView.setClickable(true);
      loadingProgress.setVisibility(View.GONE);
    }
    else if (status.equals(NetworkState.LOADING)) {
      loadingMessage.setText("loading");
      itemView.setClickable(false);
      loadingProgress.setVisibility(View.VISIBLE);
    }
    else if (status.equals(NetworkState.LOADED)) {
      loadingMessage.setText("Loading completed");
      itemView.setClickable(false);
      loadingProgress.setVisibility(View.GONE);
    }
  }

}
