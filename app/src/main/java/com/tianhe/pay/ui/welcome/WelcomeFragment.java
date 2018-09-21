package com.tianhe.pay.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianhe.pay.R;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.app.AppUpgrade;
import com.tianhe.pay.ui.MdDialog;
import com.tianhe.pay.ui.TianHeFragment;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

//@PerActivity
public class WelcomeFragment extends TianHeFragment implements WelcomeContract.View {

    private static final int ID_DIALOG_UPDATE = BaseDialog.getAutoId();
    private static final int ID_DIALOG_DOWNLOADING = BaseDialog.getAutoId();

    @Inject
    WelcomeContract.Presenter presenter;

    WelcomeAnimationControl animationControl;

    MdDialog.Builder updateBuilder;
    MdDialog.Builder downloadingBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateBuilder = new MdDialog.Builder(ID_DIALOG_UPDATE);
        downloadingBuilder = new MdDialog.Builder(ID_DIALOG_DOWNLOADING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        startAnimation(view);
        presenter.checkAppUpdate();
    }

    private void startAnimation(View view) {
        View animateView = view.findViewById(R.id.fragment_welcome_container);
        animationControl = new WelcomeAnimationControl(animateView);
        animationControl.startAnimation();
    }

    @Override
    public void onDestroy() {
        animationControl.cancelAnimation();
        super.onDestroy();
    }

    @Override
    protected WelcomeContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void lastAppUpgrade(boolean needUpdate, AppUpgrade upgrade) {
        animationControl.cancelAnimation();
        if (needUpdate) {
            showDownloadingDialog(0);
            presenter.downloadLastVersion();
//            showUpdateDialog(upgrade);
        } else {
            enterHome();
        }
    }

    @Override
    public void downloadingProgress(int progress) {
        showDownloadingDialog(progress);
    }

    @Override
    public void downloadFail(String reason) {
        dismissDialog(ID_DIALOG_DOWNLOADING);
        showMessage(reason);
        enterHome();
    }

    @Override
    public void downloadSuccess(File file) {
//        dismissDialog(ID_DIALOG_DOWNLOADING);
        installApp(file);
    }

    @Override
    public void onDialogOk(int dialogId) {
        if (dialogId == ID_DIALOG_UPDATE) {
            presenter.downloadLastVersion();
        }
    }

    @Override
    public void onDialogCancel(int dialogId) {
        dismissDialog(dialogId);
    }

    private void showUpdateDialog(AppUpgrade upgrade) {
        updateBuilder = new MdDialog.Builder(ID_DIALOG_UPDATE);
        updateBuilder.title(getString(R.string.welcome_update_title))
                .message(upgrade.getRemark());
        showDialog(updateBuilder);
    }

    private void showDownloadingDialog(int progress) {
        downloadingBuilder = new MdDialog.Builder(ID_DIALOG_DOWNLOADING);
        downloadingBuilder.title("下载新版本");
        downloadingBuilder.message(getString(R.string.welcome_downloading_message))
                .progress(progress);
        showDialog(downloadingBuilder);
    }

    private void enterHome() {
        nav.enterLogin(this);
        getActivity().finish();
    }

    public void installApp(File file) {
        if (file.exists()) {
            String[] command = {"chmod", "777", file.getPath()};
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        getActivity().finish();
    }
}
