package per.funown.bocast.library.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener;
import androidx.fragment.app.FragmentTransaction;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FragmentTransitionUtil {

  private static FragmentTransitionUtil INSTANCE;

  int containerId;
  FragmentManager manager;

  public static FragmentTransitionUtil getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new FragmentTransitionUtil();
    }
    return INSTANCE;
  }

  public void setManager(FragmentManager manager) {
    this.manager = manager;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public void transit(Fragment fragment, int containerId) {
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(containerId, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  public void transit(Fragment fragment) {
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(containerId, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }


}
