package per.funown.bocast.modules.login.ui.login;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.modules.login.R;
import per.funown.bocast.library.utils.ArouterUtil;
import per.funown.bocast.modules.login.databinding.FragmentSignuUpBinding;

@Route(path = ArouterConstant.FRAGMENT_SIGNUP)
public class SignuUpFragment extends Fragment {

  FragmentSignuUpBinding binding;
  LoginViewModel viewModel;

  public SignuUpFragment() {
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
    return inflater.inflate(R.layout.fragment_signu_up, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_signu_up);
    viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

    viewModel.getLoginFormState().observe(getActivity(), loginFormState -> {
      SignupFormState signupFormState = (SignupFormState) loginFormState;
      if (loginFormState == null) {
        return;
      }
      binding.SignUp.setEnabled(loginFormState.isDataValid());
      if (signupFormState.getPasswordError() != null) {
        binding.username.setError(getString(signupFormState.getUsernameError()));
      }
      if (signupFormState.getEmailError() != null) {
        binding.email.setError(getString(signupFormState.getEmailError()));
      }
      if (signupFormState.getPasswordError() != null) {
        binding.email.setError(getString(signupFormState.getPasswordError()));
      }
    });

    viewModel.getLoginResult().observe(getActivity(), loginResult -> {
      if (loginResult == null) {
        return;
      }
      binding.loading.setVisibility(View.GONE);
      if (loginResult.getError() != null) {
        showSignupFailed(loginResult.getError());
        return;
      }
      if (loginResult.getSuccess() != null) {
        updateUiWithUser(loginResult.getSuccess());
        SharedPreferences shp = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        Editor edit = shp.edit();
        edit.putBoolean("isLogin", true);
        edit.commit();

        // TODO
        ArouterUtil.navigation("/home");
      }
      getActivity().setResult(Activity.RESULT_OK);

      getActivity().finish();
    });

    TextWatcher watcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        viewModel.signupDataChange(binding.username.getText().toString(),
            binding.email.getText().toString(), binding.password.getText().toString());
      }
    };

    binding.username.addTextChangedListener(watcher);
    binding.email.addTextChangedListener(watcher);
    binding.password.addTextChangedListener(watcher);
    binding.password.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        viewModel.signup(binding.username.getText().toString(),
            binding.email.getText().toString(),
            binding.password.getText().toString());
      }
      return false;
    });

    binding.SignUp.setOnClickListener(v -> {
      binding.loading.setVisibility(View.VISIBLE);

      viewModel.signup(binding.username.getText().toString(),
          binding.email.getText().toString(),
          binding.password.getText().toString());
    });

    binding.ToLogin.setOnClickListener(
        v -> ARouter.getInstance().build("/bocast/loginActivity/login").navigation()
    );

  }

  private void updateUiWithUser(LoggedInUserView model) {
    String welcome = getString(R.string.welcome) + model.getDisplayName();
    // TODO : initiate successful logged in experience
    Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
  }

  private void showSignupFailed(@StringRes Integer errorString) {
    Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
  }
}
