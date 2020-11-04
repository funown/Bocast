package per.funown.bocast.library.utils;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/26
 *     desc   :
 *     version:
 * </pre>
 */
public interface ItemTouchHelperAdapter {

  void onItemMove(int adapterPosition, int adapterPosition1);
  void onItemDismiss(int adapterPosition);
}
