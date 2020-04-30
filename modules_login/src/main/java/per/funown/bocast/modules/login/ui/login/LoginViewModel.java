package per.funown.bocast.modules.login.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import java.util.regex.Pattern;
import per.funown.bocast.modules.login.data.LoginRepository;
import per.funown.bocast.modules.login.data.Result;
import per.funown.bocast.modules.login.data.model.LoggedInUser;
import per.funown.bocast.modules.login.R;

public class LoginViewModel extends ViewModel {

  private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
  private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
  private MutableLiveData<LoginResult> signupResult = new MutableLiveData<>();
  private LoginRepository loginRepository;

  LoginViewModel(LoginRepository loginRepository) {
    this.loginRepository = loginRepository;
  }

  LiveData<LoginFormState> getLoginFormState() {
    return loginFormState;
  }

  public LiveData<LoginResult> getSignupResult() {
    return signupResult;
  }

  LiveData<LoginResult> getLoginResult() {
    return loginResult;
  }

  public void login(String username, String password) {
    // can be launched in a separate asynchronous job
    Result<LoggedInUser> result = loginRepository.login(username, password);

    if (result instanceof Result.Success) {
      LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
      loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
    } else {
      loginResult.setValue(new LoginResult(R.string.login_failed));
    }
  }

  public void loginDataChanged(String username, String password) {
    if (!isUserNameOrEmailValid(username)) {
      loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
    } else if (!isPasswordValid(password)) {
      loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
    } else {
      loginFormState.setValue(new LoginFormState(true));
    }
  }

  public void signup(String username, String email, String password) {
    Result<LoggedInUser> result = loginRepository.signup(username, email, password);

    if (result instanceof  Result.Success) {
      LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
      signupResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
    } else {
      signupResult.setValue(new LoginResult(R.string.signup_failed));
    }
  }

  public void signupDataChange(String username, String email, String password) {
    if (!isUserNameOrEmailValid(username)) {
      loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
    } else if (!isEmailValid(email)) {
      loginFormState.setValue(new SignupFormState(null, null, R.string.invalid_email));
    } else if (!isPasswordValid(password)) {
      loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
    } else {
      loginFormState.setValue(new LoginFormState(true));
    }
  }

  // A placeholder username validation check
  private boolean isUserNameOrEmailValid(String username) {
    if (username == null || username.trim().isEmpty()) {
      return false;
    }
    if (username.contains("@")) {
      return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    } else {
      return Pattern.matches("^[a-zA-Z0-9_\u4e00-\u9fa5]+$", username.trim());
    }
  }

  private boolean isUsernameValid(String username) {
    if (username == null || username.trim().isEmpty()) {
      return false;
    } else {
      return Pattern.matches("^[a-zA-Z0-9_\u4e00-\u9fa5]+$", username.trim());
    }
  }

  private boolean isEmailValid(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    } else {
      return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
  }

  // A placeholder password validation check
  private boolean isPasswordValid(String password) {
    return password != null && password.trim().length() > 5;
  }
}
