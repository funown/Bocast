package per.funown.bocast.modules.login.ui.login;

import android.os.Bundle;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import cn.bmob.v3.Bmob;

import com.alibaba.android.arouter.facade.annotation.Route;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.modules.login.R;

@Route(path = ArouterConstant.FRAGMENT_LOGIN)
public class LoginActivity extends AppCompatActivity {

  private final static String APPID_KEY = "Bmob_APP_KEY";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    try {
      Bmob.initialize(this, this.getPackageManager()
          .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA).metaData.getString(APPID_KEY));
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
  }

}
