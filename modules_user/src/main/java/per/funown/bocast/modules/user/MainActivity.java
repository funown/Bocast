package per.funown.bocast.modules.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.user.databinding.ActivityMainBinding;
import per.funown.bocast.modules.user.fragment.UserCenterFragment;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ARouter.openLog();
    ARouter.openDebug();
    ARouter.init(this.getApplication());
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    FragmentTransitionUtil instance = FragmentTransitionUtil.getINSTANCE();
    instance.setManager(this.getSupportFragmentManager());
    instance.setContainerId(binding.getRoot().getId());
    UserCenterFragment userCenterFragment = (UserCenterFragment) ARouter.getInstance()
        .build(ArouterConstant.FRAGMENT_USER_CENTER)
        .withInt("containerId", binding.getRoot().getId()).navigation();
    instance.transit(userCenterFragment, binding.getRoot().getId());
  }
}
