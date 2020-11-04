package per.funown.bocast.intercepter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import java.util.List;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.BmobUser;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Interceptor(priority = 1, name = "LoginInterceptor")
public class LoginInterceptor implements IInterceptor {

  private static final String TAG = LoginInterceptor.class.getSimpleName();
  private Context context;

  @Override
  public void process(Postcard postcard, InterceptorCallback callback) {
    Log.e(TAG, "login...");
    SharedPreferences token = context.getSharedPreferences("token", Context.MODE_PRIVATE);
    if (!token.getBoolean("isLogin", false)) {
      BmobQuery<BmobUser> query = new BmobQuery<>();
      String alias = token.getString("alias", "");
      query.addWhereEqualTo("alias", alias);
      query.setLimit(1);
      query.findObjects(new FindListener<BmobUser>() {
        @Override
        public void done(List<BmobUser> object, BmobException e) {
          if (e == null) {
            if (object.get(0).isLogined()) {
              callback.onContinue(postcard);
            }
            else {
              ARouter.getInstance().build(ArouterConstant.FRAGMENT_SIGNUP).navigation(context);
            }
          }
          else {
              ARouter.getInstance().build(ArouterConstant.FRAGMENT_LOGIN).navigation(context);
              callback.onInterrupt(new RuntimeException(e));
          }
        }
      });
    }
    else {
      callback.onContinue(postcard);
    }
  }

  @Override
  public void init(Context context) {
    this.context = context;
  }
}
