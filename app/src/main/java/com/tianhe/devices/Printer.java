package com.tianhe.devices;


import android.graphics.Bitmap;
import android.support.annotation.WorkerThread;

public interface Printer {

    public static enum State {
        NORMAL("打印机准备就绪"),
        OUT_OF_PAPER("打印机缺纸"),
        TOO_HOT("打印机温度过高"),
        TOO_LONG_DATA("打印数据过长"),
        DATA_ERROR("无法识别的打印数据"),
        BUSYING("打印机正忙");

        String message;

        State(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @WorkerThread
    State printText(String text);

    @WorkerThread
    State printBitmap(Bitmap bitmap);
}
