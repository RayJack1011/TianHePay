package com.tianhe.pay.ui.wechatalireprint;

import com.tianhe.devices.Printer;
import com.tianhe.pay.data.print.PrintUseCase;
import com.tianhe.pay.data.print.PrintUtils;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.data.DefaultObserver;
import com.tianhe.pay.ui.TianHePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class WechatAliReprintPresenter extends TianHePresenter<WechatAliReprintContract.View>
    implements WechatAliReprintContract.Presenter {

    PrintUseCase printTask;

    @Inject
    public WechatAliReprintPresenter(PrintUseCase printTask) {
        this.printTask = printTask;
    }

    @Override
    public void reprint(WechatAliOrder order) {
        printTask.setReqParam(PrintUtils.wechatAliPrint(order));
        printTask.execute(new DefaultObserver<Printer.State>(){
            @Override
            public void onNext(@NonNull Printer.State state) {
                if (state != Printer.State.NORMAL) {
                    view.reprintFail(state.getMessage());
                } else {
                    view.reprintSuccess();
                };
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.reprintFail(e.getMessage());
            }
        });
    }
}
