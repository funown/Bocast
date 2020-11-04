package per.funown.bocast.library.model;

import cn.bmob.v3.BmobObject;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BmobUser extends BmobObject {

  private String alias;
  private String email;
  private String password;
  private boolean isLogined;

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isLogined() {
    return isLogined;
  }

  public void setLogined(boolean logined) {
    isLogined = logined;
  }
}
