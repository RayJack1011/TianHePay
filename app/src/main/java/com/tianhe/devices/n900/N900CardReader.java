package com.tianhe.devices.n900;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.newland.mtype.DeviceRTException;
import com.newland.mtype.ModuleType;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CardReader;
import com.newland.mtype.module.common.cardreader.CommonCardType;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.cardreader.K21CardReaderEvent;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.module.common.swiper.SwipResultType;
import com.newland.mtype.module.common.swiper.SwiperReadModel;

import java.util.concurrent.TimeUnit;

public class N900CardReader implements com.tianhe.devices.CardReader {
    private static final String TAG = "N900CardReader";
    private static final ModuleType[] TYPE_READ_CARD = new ModuleType[]{
            ModuleType.COMMON_SWIPER
    };

    private static final int NOT_OPEN = 1;
    private static final int OPENING = 1;
    private static final int OPENED = 2;
    private static final int WAIT_SWIPER_TIMEOUT = 3;
    private static final int CLOSING = 4;
    private static final int CLOSED = 5;

    private N900Device device;
    private K21CardReader cardReader;
    private Handler handler;
    private Thread readThread;
    private Callback callback;
    private CardReadListener listener;

    public N900CardReader(Context context) {
        this.device = N900Device.getInstance(context);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void openReadCard(final Callback callback) {
        this.callback = callback;
        if (readThread != null) {
            readThread = null;
        }
        readThread = new Thread() {
            @Override
            public void run() {
                startRead();
            }
        };
        readThread.start();
    }

    @Override
    public void closeReadCard() {
        if (cardReader != null) {
            this.callback = null;
            try {
                cardReader.cancelCardRead();
                Log.e(TAG, "close read");
//                cardReader.closeCardReader();
            } catch (Exception e) {
                // ignore
            }
            cardReader = null;
        }
    }

    private void startRead() {
        // 没有监听刷卡结果的回调, 不用开启读卡
        if (callback == null) {
            return;
        }
        device.connectDevice();
        cardReader = (K21CardReader) device.getCardReaderModuleType();
        if (cardReader == null) {
            notifyOpenCardReaderFailed("无法获取读卡设备");
            return;
        }
        try {
            cardReader.openCardReader("",
                    TYPE_READ_CARD,             // 读卡类型
                    true,                  // 是否读取磁道信息并监听按键
                    true,                   // 是否开启磁道校验
                    20, TimeUnit.SECONDS,  // 超时时间
                    new CardReadListener());
            notifyOpenCardReaderSuccess();
        } catch (Exception e) {
            notifyOpenCardReaderFailed("开启读卡设备失败");
            Log.e(TAG, e.getMessage() + device.isDeviceBusy());
        }
    }

    private void notifyOpenCardReaderSuccess() {
        if (callback == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onPrepared();
            }
        });
    }

    private void notifyOpenCardReaderFailed(final String reason) {
        if (callback == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.openFail(reason);
            }
        });
    }

    private class CardReadListener implements DeviceEventListener<K21CardReaderEvent> {
        @Override
        public void onEvent(K21CardReaderEvent event, Handler handler) {
            if (!event.isSuccess()) {
//                restartWhenTimeout(event);
                return;
            }
            CommonCardType[] commonCardTypes = event.getOpenCardReaderResult().getResponseCardTypes();
            if (commonCardTypes != null && commonCardTypes.length > 0) {
                for (CommonCardType type : commonCardTypes) {
                    if (CommonCardType.MSCARD == type) {
                        CardInfo cardInfo = readPlainTrackResult();
                        if (cardInfo != null) {
                            notifyReadResult(cardInfo);
                            break;
                        }
                    }
                }
            }
        }

        private void restartWhenTimeout(K21CardReaderEvent event) {
            Throwable error = event.getException();
            if (isWaitingSwipperTimeoutError(error)) {
                startRead();
            }
        }

        @Override
        public Handler getUIHandler() {
            return handler;
        }
    }

    private CardInfo readPlainTrackResult() {
        K21Swiper swiper = device.getK21Swiper();//获得磁卡读卡器模块接口
        // 获得明文磁道信息
        SwipResult swipResult = swiper
                .readPlainResult(new SwiperReadModel[]{SwiperReadModel.READ_FIRST_TRACK,
                        SwiperReadModel.READ_SECOND_TRACK,
                        SwiperReadModel.READ_THIRD_TRACK});
        if (swipResult.getRsltType() == SwipResultType.SUCCESS) {
            final CardInfo cardInfo = new CardInfo();
            if (swipResult.getFirstTrackData() != null) {
                cardInfo.trackOne = new String(swipResult.getSecondTrackData());
            }
            if (swipResult.getSecondTrackData() != null) {
                cardInfo.trackTwo = new String(swipResult.getSecondTrackData());
            }
            if (swipResult.getThirdTrackData() != null) {
                cardInfo.trackThree = new String(swipResult.getThirdTrackData());
            }
            return cardInfo;
        }
        return null;
    }

    private void notifyReadResult(final CardInfo cardInfo) {
        if (handler != null && this.callback != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onRead(cardInfo);
                }
            });
        }
    }

    private boolean isWaitingSwipperTimeoutError(Throwable error) {
        if (error != null && error instanceof DeviceRTException) {
            Throwable cause = error.getCause();
            if (cause instanceof ProcessTimeoutException) {
                return true;
            }
        }
        return false;
    }
}
