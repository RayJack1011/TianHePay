package com.tianhe.pay.ui.welcome;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.app.AppUpgrade;
import com.tianhe.pay.data.app.DownloadAppUseCase;
import com.tianhe.pay.common.StatesContainer;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class WelcomePresenter extends TianHePresenter<WelcomeContract.View> implements WelcomeContract.Presenter {
    private static final String TAG_SAVED_LAST_APP_INFO = "appUpgrade";

    private UseCase getAppUpgradeTask;
    private UseCase downloadTask;
    private AppUpgrade appUpgrade;
    private String currentVersionName;

    @Inject
    public WelcomePresenter(@Named("currentVersion") String versionName,
                            @Named("appLast") UseCase getAppUpgradeTask,
                            @Named("downloadApp") UseCase downloadTask) {
        this.getAppUpgradeTask = getAppUpgradeTask;
        this.downloadTask = downloadTask;
        this.currentVersionName = versionName;
    }

    @Override
    protected void onRestore(StatesContainer<String, Object> saved) {
        super.onRestore(saved);
        if (saved.contains(TAG_SAVED_LAST_APP_INFO)) {
            appUpgrade = (AppUpgrade) saved.remove(TAG_SAVED_LAST_APP_INFO);
        }
    }

    @Override
    protected void onSave(StatesContainer<String, Object> out) {
        super.onSave(out);
        out.save(TAG_SAVED_LAST_APP_INFO, appUpgrade);
    }

    @Override
    public void onDestroy() {
        getAppUpgradeTask.cancel();
        getAppUpgradeTask = null;
        super.onDestroy();
    }

    @Override
    public void checkAppUpdate() {
        getAppUpgradeTask.execute(new DefaultObserver<AppUpgrade>(){
            @Override
            public void onNext(@NonNull AppUpgrade appUpgrade) {
                if (needUpdate(appUpgrade)) {
                    updateToLastVersion(appUpgrade);
                } else {
                    ignoreLastVersion();
                }
            }
            @Override
            public void onError(@NonNull Throwable e) {
                ignoreLastVersion();
            }
        });
    }

    @Override
    public void downloadLastVersion() {
        downloadTask.setReqParam(appUpgrade.getUrl());
        downloadTask.execute(new DefaultObserver<DownloadStatus>(){
            @Override
            public void onNext(@NonNull DownloadStatus downloadStatus) {
                downloadProgress((int) downloadStatus.getPercentNumber());
            }
            @Override
            public void onError(@NonNull Throwable e) {
                downloadFail(e);
            }

            @Override
            public void onComplete() {
                downloadSuccess();
            }
        });
    }

    private boolean needUpdate(AppUpgrade appUpgrade) {
        return currentVersionName.compareTo(appUpgrade.getVersion()) < 0;
    }

    /** 需要更新新版本 */
    private void updateToLastVersion(AppUpgrade appUpgrade) {
        this.appUpgrade = appUpgrade;
        view.lastAppUpgrade(true, appUpgrade);
    }

    /** 忽略版本更新 */
    private void ignoreLastVersion() {
        view.lastAppUpgrade(false, null);
    }

    private void downloadProgress(int progress) {
        view.downloadingProgress(progress);
    }

    private void downloadSuccess() {
        File downloadFile = ((DownloadAppUseCase)downloadTask).getDownloadFile();
        if (downloadFile != null) {
            view.downloadSuccess(downloadFile);
        }
    }

    private void downloadFail(Throwable error) {
        view.downloadFail(error.getMessage());
    }

}
