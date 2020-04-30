package per.funown.bocast.modules.login.ui.login;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SignupFormState extends LoginFormState {

  @Nullable
  private Integer emailError;

  SignupFormState(boolean isDataValid) {
    super(isDataValid);
  }

  SignupFormState(@Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer passwordError) {
    super(usernameError, passwordError);
    this.emailError = emailError;
  }

  @Nullable
  public Integer getEmailError() {
    return emailError;
  }

  @Nullable
  @Override
  Integer getPasswordError() {
    return super.getPasswordError();
  }

  @Nullable
  @Override
  Integer getUsernameError() {
    return super.getUsernameError();
  }
}
