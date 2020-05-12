package per.funown.bocast.modules.home.callback;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import per.funown.bocast.library.model.ItunesResponseEntity;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DiffCallBack extends DiffUtil.ItemCallback<ItunesResponseEntity> {

  private static final String TAG = DiffCallBack.class.getSimpleName();

  @Override
  public boolean areItemsTheSame(@NonNull ItunesResponseEntity oldItem, @NonNull ItunesResponseEntity newItem) {
    return oldItem == newItem;
  }

  @Override
  public boolean areContentsTheSame(@NonNull ItunesResponseEntity oldItem, @NonNull ItunesResponseEntity newItem) {
    return oldItem.getCollectionId().equals(newItem.getCollectionId());
  }
}
