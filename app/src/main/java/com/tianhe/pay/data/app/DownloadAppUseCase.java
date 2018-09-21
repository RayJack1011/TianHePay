package com.tianhe.pay.data.app;

import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class DownloadAppUseCase extends UseCase<String> {

    private RxDownload downloader;
    private String url;

    @Inject
    public DownloadAppUseCase(@MainThread Scheduler mainScheduler,
                              @RpcThread Scheduler rpcScheduler,
                              RxDownload downloader) {
        super(mainScheduler, rpcScheduler);
        this.downloader = downloader;
    }

    @Override
    public void setReqParam(String url) {
        this.url = url;
    }

    public File getDownloadFile() {
        File[] record = downloader.getRealFiles(url);
        if (record != null) {
            return record[0];
        }
        return null;
    }

    @Override
    protected Observable<DownloadStatus> buildUseCaseObservable() {
        return downloader.download(url);
    }

}
