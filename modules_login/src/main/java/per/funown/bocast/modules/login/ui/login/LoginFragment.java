package per.funown.bocast.modules.login.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.utils.ArouterUtil;
import per.funown.bocast.modules.login.R;
import per.funown.bocast.modules.login.databinding.FragmentLoginBinding;
import per.funown.bocast.modules.login.ui.login.LoginFormState;
import per.funown.bocast.modules.login.ui.login.LoginResult;
import per.funown.bocast.modules.login.ui.login.LoginViewModel;

@Route(path = "/bocast/login")
public class LoginFragment extends Fragment {

  FragmentLoginBinding binding;

  private LoginViewModel loginViewModel;

  public LoginFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_login);
    loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

    loginViewModel.getLoginFormState()
        .observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
          @Override
          public void onChanged(@Nullable LoginFormState loginFormState) {
            if (loginFormState == null) {
              return;
            }
            binding.login.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
              binding.username.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
              binding.password.setError(getString(loginFormState.getPasswordError()));
            }
          }
        });

    loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
      @Override
      public void onChanged(@Nullable LoginResult loginResult) {
        if (loginResult == null) {
          return;
        }
        binding.loading.setVisibility(View.GONE);
        if (loginResult.getError() != null) {
          showLoginFailed(loginResult.getError());
          return;
        }
        if (loginResult.getSuccess() != null) {
          updateUiWithUser(loginResult.getSuccess());

          SharedPreferences shp = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
          Editor edit = shp.edit();
          edit.putBoolean("isLogin", true);
          edit.putString("alias", loginResult.getSuccess().getDisplayName());
          edit.commit();

          // TODO
          ArouterUtil.navigation("/home");
        }
        getActivity().setResult(Activity.RESULT_OK);

        //Complete and destroy login activity once successful
        getActivity().finish();
      }
    });

    TextWatcher afterTextChangedListener = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // ignore
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // ignore
      }

      @Override
      public void afterTextChanged(Editable s) {
        loginViewModel.loginDataChanged(binding.username.getText().toString(),
            binding.password.getText().toString());
      }
    };
    binding.username.addTextChangedListener(afterTextChangedListener);
    binding.password.addTextChangedListener(afterTextChangedListener);
    binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          loginViewModel.login(binding.username.getText().toString(),
              binding.password.getText().toString());
        }
        return false;
      }
    });

    binding.login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        binding.loading.setVisibility(View.VISIBLE);
        loginViewModel.login(binding.username.getText().toString(),
            binding.password.getText().toString());
      }
    });

    binding.ToSignup.setOnClickListener(
        v -> ARouter.getInstance().build("/bocast/loginActivity/signup").navigation()
    );
  }

  private void updateUiWithUser(LoggedInUserView model) {
    String welcome = getString(R.string.welcome_back) + model.getDisplayName();
    // TODO : initiate successful logged in experience
    Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
  }

  private void showLoginFailed(@StringRes Integer errorString) {
    Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
  }
}
