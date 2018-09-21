package com.tianhe.devices.n900;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;

import java.util.concurrent.TimeUnit;

public class N900Printer implements com.tianhe.devices.Printer {

    private static final int PRINT_MAX_LENGTH = 1024;
    // 默认的字体浓度(1-15)
    private static final int TEXT_DENSITY_DEFAULT = 7;
    // 默认的行间距
    private static final int TEXT_LINE_SPACE = 1;
    private static final int OFF_LEFT = 20;

    private N900Device device;

    public N900Printer(Context context) {
        device = N900Device.getInstance(context);
    }

    @WorkerThread
    public com.tianhe.devices.Printer.State printText(String text) {
        if (text == null) {
            return match(PrinterResult.SUCCESS);
        }
        if (text.length() > PRINT_MAX_LENGTH) {
            return State.TOO_LONG_DATA;
        }
        boolean deviceBusy = device.isDeviceBusy();
        if (deviceBusy) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
        }
        Log.e("N900Printer", "device is busy = " + deviceBusy);
        device.disconnect();
        device.connectDevice();
        Printer printer = device.getPrinter();
        // printer.setLineSpace(TEXT_LINE_SPACE); // 设置行间距
        printer.setDensity(TEXT_DENSITY_DEFAULT); // 设置浓度 1-15
//        printer.init();
        try {
            PrinterResult result = printer.print(text, 30, TimeUnit.SECONDS);
            if (result == PrinterResult.BUSY) {
                Thread.sleep(500);
                result = printer.print(text, 30, TimeUnit.SECONDS);
            }
            return match(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return match(PrinterResult.SUCCESS);
    }

    @Override
    public State printBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return match(PrinterResult.SUCCESS);
        }
        if (!device.isDeviceAlive()) {
            device.connectDevice();
        }
        Printer printer = device.getPrinter();
        printer.autoSetThreshold(true);
        PrinterResult result = printer.print(OFF_LEFT, bitmap, 30, TimeUnit.SECONDS);
        return match(result);
    }

    private com.tianhe.devices.Printer.State match(PrinterResult result) {
        switch (result) {
            case SUCCESS:
                return State.NORMAL;
            case OUTOF_PAPER:
                return State.OUT_OF_PAPER;
            case HEAT_LIMITED:
                return State.TOO_HOT;
            case BUSY:
                return State.BUSYING;
            default:
                return State.DATA_ERROR;
        }
    }
}
