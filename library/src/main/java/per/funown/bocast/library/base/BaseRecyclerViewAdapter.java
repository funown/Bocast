package per.funown.bocast.library.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class BaseRecyclerViewAdapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener<T> {
    void onItemClick(T o);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    onItemClickListener = listener;
  }

}
