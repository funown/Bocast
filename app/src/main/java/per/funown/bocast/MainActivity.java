package per.funown.bocast;

import android.media.AudioManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import per.funown.bocast.databinding.ActivityMainBinding;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.utils.FragmentTransitionUtil;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();
  ActivityMainBinding binding;

  ViewPagerFragmentAdapter adapter;
  private static boolean PLAYERINITED = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e(TAG, "on creating...");
    ARouter.getInstance().inject(this);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    adapter = new ViewPagerFragmentAdapter(this.getSupportFragmentManager(), getLifecycle());
    adapter.setFragments(initFragment());
    binding.viewpager.setAdapter(adapter);
    FragmentTransitionUtil instance = FragmentTransitionUtil.getINSTANCE();
    instance.setContainerId(binding.getRoot().getId());

    new TabLayoutMediator(binding.TabHeader, binding.viewpager, true,
        true, ((tab, position) -> {
      switch (position) {
        case 0:
          tab.setText(R.string.title_home);
          break;
        case 1:
          tab.setText(R.string.title_discover);
          break;
        case 2:
          tab.setText(R.string.title_user);
          break;
      }
    })).attach();

    if (!PLAYERINITED) {
      initPlayer();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void initPlayer() {
    Log.i(TAG, "init Player -- " + PLAYERINITED);
    FragmentManager supportFragmentManager = getSupportFragmentManager();
    Fragment player = (Fragment) ARouter.getInstance().build(ArouterConstant.FRAGMENT_LISTENER)
        .navigation();
    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
    transaction.add(binding.getRoot().getId(), player);
    transaction.addToBackStack(null);
    transaction.commit();
    PLAYERINITED = true;
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.e(TAG, "destroy");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.e(TAG, "pause");
  }

  private ArrayList<Fragment> initFragment() {
    Fragment homeFragment = (Fragment) ARouter.getInstance().build(ArouterConstant.FRAGMENT_HOME)
        .withInt("containerId", binding.getRoot().getId())
        .navigation();
    Fragment discoverFragment = (Fragment) ARouter.getInstance()
        .build(ArouterConstant.FRAGMENT_DISCOVER).navigation();
    Fragment userCenterFragment = (Fragment) ARouter.getInstance()
        .build(ArouterConstant.FRAGMENT_USER_CENTER)
        .withInt("containerId", binding.getRoot().getId()).navigation();

    ArrayList<Fragment> fragments = new ArrayList<>(3);
    fragments.add(homeFragment);
    fragments.add(discoverFragment);
    fragments.add(userCenterFragment);

    return fragments;
  }
}
