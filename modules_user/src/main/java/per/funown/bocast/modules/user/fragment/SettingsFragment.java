package per.funown.bocast.modules.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import com.alibaba.android.arouter.facade.annotation.Route;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.modules.user.R;

@Route(path = ArouterConstant.FRAGMENT_USER_SETTINGS)
public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    view.setBackgroundColor(getContext().getColor(R.color.white));
    return view;
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.root_preferences, rootKey);
  }
}
