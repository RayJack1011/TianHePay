package com.tianhe.pay.data.print;

import android.graphics.Bitmap;

import com.tianhe.devices.Printer;
import com.tianhe.pay.data.MainThread;
import com.tianhe.pay.data.RpcThread;
import com.tianhe.pay.data.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

public class PrintUseCase extends UseCase<PrintInfo[]> {

    Printer printer;
    PrintInfo[] printInfos;

    @Inject
    public PrintUseCase(@MainThread Scheduler mainScheduler,
                        @RpcThread Scheduler rpcScheduler,
                        Printer printer) {
        super(mainScheduler, rpcScheduler);
        this.printer = printer;
    }

    @Override
    protected Observable<Printer.State> buildUseCaseObservable() {
        return Observable.create(new ObservableOnSubscribe<Printer.State>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Printer.State> e) throws Exception {
                Printer.State state = Printer.State.NORMAL;
                for (PrintInfo info : printInfos) {
                    state = printOnce(info);
                    Thread.sleep(300);
                }
                e.onNext(state);
            }
        });
    }

    @Override
    public void setReqParam(PrintInfo[] reqParam) {
        this.printInfos = reqParam;
    }

    private Printer.State printOnce(PrintInfo printInfo) {
        Printer.State state = Printer.State.NORMAL;
        for (PrintInfo.Line line : printInfo.getContents()) {
            if (line.content instanceof String) {
                state = printer.printText((String) line.content);
                if (state != Printer.State.NORMAL) {
                    break;
                }
            } else if (line.content instanceof Bitmap) {
                state = printer.printBitmap((Bitmap) line.content);
                if (state != Printer.State.NORMAL) {
                    break;
                }
            }
        }
        return state;
    }
}
