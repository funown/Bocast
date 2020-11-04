package per.funown.bocast.modules.login.data;

import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import java.util.Date;
import java.util.List;
import per.funown.bocast.library.utils.DigestUtils;
import per.funown.bocast.library.utils.RegexpUtils;
import per.funown.bocast.library.model.BmobUser;
import per.funown.bocast.modules.login.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

  private static final String TAG = LoginDataSource.class.getSimpleName();

  private Result<LoggedInUser> result;

  public Result<LoggedInUser> login(String account, String password) {

    BmobQuery<BmobUser> query = new BmobQuery<>();

    if (RegexpUtils.checkEmail(account)) {
      query.addWhereEqualTo("email", account);
    } else {
      query.addWhereEqualTo("alias", account);
    }

    query.setLimit(1);
    query.findObjects(new FindListener<BmobUser>() {
      @Override
      public void done(List<BmobUser> object, BmobException e) {
        // query success
        if (e == null) {
          BmobUser bmobUser = object.get(0);
          if (DigestUtils.encode(password).equals(bmobUser.getPassword())) {
            bmobUser.setLogined(true);
            bmobUser.update(new UpdateListener() {
              @Override
              public void done(BmobException e) {
                if (e == null) {
                  Log.i(TAG,
                      "Data Update Succeed : " + bmobUser.getObjectId() + "- Updated at " + bmobUser
                          .getUpdatedAt());
                } else {
                  Log.i(TAG,
                      "Data Update Failed : " + bmobUser.getObjectId() + "- Error: " + e
                          .getErrorCode() + ":" + e.getMessage());
                }
              }
            });
            result = new Result.Success<LoggedInUser>(
                new LoggedInUser(bmobUser.getObjectId(), bmobUser.getAlias()));
            Log.i(TAG, "Login Succeed : " + bmobUser.getObjectId() + "- Login at" + new Date());
          } else {
            result = new Result.Error(new Exception("Incorrect password"));
            Log.i(TAG, "Login Failed : " + bmobUser.getObjectId() + "- Error： Incorrect password");
          }
        }
        // query fail
        else {
          result = new Result.Error(new Exception(e.getMessage(), e.getCause()));
          Log.i(TAG, "Login Failed : - Error： " + e.getErrorCode() + " " + e.getMessage());
        }
      }
    });
    return result;
  }

  public void logout() {
    // TODO: revoke authentication
  }

  public Result<LoggedInUser> signup(String username, String email, String password) {
    BmobUser bmobUser = new BmobUser();
    bmobUser.setAlias(username);
    bmobUser.setEmail(email);
    bmobUser.setPassword(DigestUtils.encode(password));
    bmobUser.setLogined(true);
    bmobUser.save(new SaveListener<String>() {
      @Override
      public void done(String s, BmobException e) {
        if (e == null) {
          Log.i(TAG, "Sign up Succeed: objectID-" + s);
          result = new Result.Success<LoggedInUser>(new LoggedInUser(s, username));
        } else {
          Log.i(TAG, "Sign up failed: Code-" + e.getErrorCode() + " " + e.getMessage());
          result = new Result.Error(e);
        }
      }
    });
    return result;
  }
}
