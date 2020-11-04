package per.funown.bocast.library.utils;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface ItemTouchHelperViewHolder {

  void onItemSelected(Context context);
  void onItemClear(Context context);

}
