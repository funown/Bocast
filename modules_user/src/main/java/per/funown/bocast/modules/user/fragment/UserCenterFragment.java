package per.funown.bocast.modules.user.fragment;

import android.os.Bundle;
import android.view.View.OnClickListener;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.user.MainActivity;
import per.funown.bocast.modules.user.R;
import per.funown.bocast.modules.user.databinding.FragmentUserCenterBinding;

@Route(path = ArouterConstant.FRAGMENT_USER_CENTER)
public class UserCenterFragment extends Fragment {

  FragmentUserCenterBinding binding;

  @Autowired
  int containerId;

  public UserCenterFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ARouter.getInstance().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentUserCenterBinding.inflate(getLayoutInflater());
    FragmentManager manager = getActivity().getSupportFragmentManager();

    binding.subscribed.setOnClickListener(
        v -> {
          FragmentTransaction transaction = manager.beginTransaction();
          Fragment subscribed = (Fragment) ARouter.getInstance()
              .build(ArouterConstant.FRAGMENT_USER_SUBSCRIBED)
              .navigation();
          transaction.replace(containerId, subscribed);
          transaction.addToBackStack(null);
          transaction.commit();
        }
    );
    binding.download.setOnClickListener(
        v -> {
          FragmentTransaction transaction = manager.beginTransaction();
          Fragment download = (Fragment) ARouter.getInstance()
              .build(ArouterConstant.FRAGMENT_USER_DOWNLOAD)
              .navigation();
          transaction.replace(containerId, download);
          transaction.addToBackStack(null);
          transaction.commit();
        }
    );
    binding.history.setOnClickListener(
        v -> {
          FragmentTransaction transaction = manager.beginTransaction();
          Fragment history = (Fragment) ARouter.getInstance()
              .build(ArouterConstant.FRAGMENT_USER_HISTORY)
              .navigation();
          transaction.replace(containerId, history);
          transaction.addToBackStack(null);
          transaction.commit();
        }
    );
    binding.settings.setOnClickListener(
        v -> {
          FragmentTransaction transaction = manager.beginTransaction();
          Fragment settings = (Fragment) ARouter.getInstance()
              .build(ArouterConstant.FRAGMENT_USER_SETTINGS)
              .navigation();
          transaction.replace(containerId, settings);
          transaction.addToBackStack(null);
          transaction.commit();
        });

    // Inflate the layout for this fragment
    return binding.getRoot();
  }
}
