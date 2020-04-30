package per.funown.bocast.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import per.funown.bocast.library.R;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

  private final ItemTouchHelperAdapter adapter;
  private Context context;
  Paint paint = new Paint();

  public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context context) {
    this.adapter = adapter;
    this.context = context;
  }

  @Override
  public boolean isLongPressDragEnabled() {
    return false;
  }

  @Override
  public boolean isItemViewSwipeEnabled() {
    return true;
  }

  @Override
  public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {
    final int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    final int swipeFlag = ItemTouchHelper.END;
    return makeMovementFlags(dragFlag, swipeFlag);
  }

  @Override
  public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType,
      float animateDx, float animateDy) {
    return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION
        : 350;
  }

  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder,
      @NonNull ViewHolder target) {
    if (viewHolder.getItemViewType() != target.getItemViewType()) {
      return false;
    }
    adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    return true;
  }

  @Override
  public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {
    adapter.onItemDismiss(viewHolder.getAdapterPosition());
  }

  @Override
  public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
      ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
  }

  @Override
  public int getBoundingBoxMargin() {
    return super.getBoundingBoxMargin();
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
      @NonNull ViewHolder viewHolder, float dX, float dY, int actionState,
      boolean isCurrentlyActive) {
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      View itemView = viewHolder.itemView;
      Bitmap icon;
      if (dX > 0) {
        Drawable d = context.getResources().getDrawable(R.drawable.ic_delete, null);
        d.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
        icon = drawableToBitmap(d);
        paint.setColor(ContextCompat.getColor(context, R.color.red400));
        c.drawRect((float) itemView.getLeft() + ScreenUtil.dpToPx(0), (float) itemView.getTop(),
            dX + ScreenUtil.dpToPx(0),
            (float) itemView.getBottom(), paint);
        c.drawBitmap(icon, (float) itemView.getLeft() + ScreenUtil.dpToPx(16),
            (float) itemView.getTop() +
                ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
            paint);
        icon.recycle();
      }
    }
  }

  @Override
  public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
      // Let the view holder know that this item is being moved or dragged
      ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
      itemViewHolder.onItemSelected(context);
    }

    super.onSelectedChanged(viewHolder, actionState);

       /* final boolean swiping = actionState == ItemTouchHelper.ACTION_STATE_SWIPE;
        swipeRefreshLayout.setEnabled(!swiping);*/
  }

  @Override
  public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);

    // Tell the view holder it's time to restore the idle state
    ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
    itemViewHolder.onItemClear(context);

  }

  @Override
  public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
    //  return super.getMoveThreshold(viewHolder);
    return 0.1f;
    //  return super.getMoveThreshold(0.5f);
  }

  @Override
  public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
    //if (viewHolder instanceof RecyclerView.ViewHolder) return 1f;
    //return super.getSwipeThreshold(viewHolder);
    return 0.9f;
  }

  public static Bitmap drawableToBitmap (Drawable drawable) {

    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable)drawable).getBitmap();
    }

    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }
}
