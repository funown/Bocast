package per.funown.bocast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

  private ArrayList<Fragment> fragments;

  public void setFragments(ArrayList<Fragment> fragments) {
    this.fragments = fragments;
  }

  public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager,
      @NonNull Lifecycle lifecycle) {
    super(fragmentManager, lifecycle);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    return fragments.get(position);
  }

  @Override
  public int getItemCount() {
    return fragments.size();
  }
}
