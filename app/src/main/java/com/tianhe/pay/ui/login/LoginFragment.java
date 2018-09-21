package com.tianhe.pay.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.R;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.di.PerActivity;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.ui.setting.Settings;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class LoginFragment extends TianHeFragment implements LoginContract.View {

    private static final int LOGIN_PROGRESS_ID = BaseDialog.getAutoId();

    @Inject
    public LoginContract.Presenter presenter;
    @Inject
    Settings settings;
    private ViewHolder viewHolder;
    @Inject
    @Named("currentVersion")
    String version;

    @Inject
    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        viewHolder = new ViewHolder(view);
        viewHolder.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });
        viewHolder.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        String secText = viewHolder.services.getText().toString() + version;
        viewHolder.services.setText(secText);
        settings.saveServerUrl(DataSource.HOST_AND_PORT);
        viewHolder.services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetServiceUrl();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewHolder.password.setText("");
    }

    @Override
    public void onDestroy() {
        viewHolder = null;
        super.onDestroy();
    }

    @Override
    protected LoginContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public CharSequence username() {
        return viewHolder.userName.getText();
    }

    @Override
    public CharSequence password() {
        return viewHolder.password.getText();
    }

    @Override
    public void loginSuccess() {
        dismissDialog(LOGIN_PROGRESS_ID);
        nav.enterHome(this);
    }

    @Override
    public void loginFailed(String reason) {
        dismissDialog(LOGIN_PROGRESS_ID);
        String msg = reason;
        if (msg == null || "".equals(msg)) {
            msg = getString(R.string.login_failed_tips);
        }
        showMessage(msg);
    }

    private void login() {
        if (validEmpty()) {
            showProgress(LOGIN_PROGRESS_ID, "登录中...");
            presenter.login();
        }
    }

    private boolean validEmpty() {
        if (username().length() == 0) {
            showMessage(R.string.error_login_account_empty);
            return false;
        }
        if (password().length() == 0) {
            showMessage(R.string.error_login_password_empty);
            return false;
        }
        return true;
    }

    private void showSetServiceUrl() {
        String hostAndPort = null;
        if (hostAndPort == null) {
            hostAndPort = DataSource.HOST_AND_PORT;
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.input("服务地址", hostAndPort, false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                settings.saveServerUrl(input.toString());
            }
        });
        builder.positiveText("确定");
        builder.negativeText("取消");
        builder.show();
    }

    private static class ViewHolder {
        EditText userName;
        EditText password;
        Button login;
        Button register;
        TextView services;

        public ViewHolder(View view) {
            userName = view.findViewById(R.id.fragment_login_username);
            password = view.findViewById(R.id.fragment_login_password);
            login = view.findViewById(R.id.fragment_login_login);
            register = view.findViewById(R.id.fragment_login_register);
            services = view.findViewById(R.id.login_service);
        }
    }
}
