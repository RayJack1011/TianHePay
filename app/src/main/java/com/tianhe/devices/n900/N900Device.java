package com.tianhe.devices.n900;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.Device;
import com.newland.mtype.ExModuleType;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.buzzer.Buzzer;
import com.newland.mtype.module.common.cardreader.CardReader;
import com.newland.mtype.module.common.cashbox.CashBoxModule;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.externalPin.ExternalPinInput;
import com.newland.mtype.module.common.iccard.ICCardModule;
import com.newland.mtype.module.common.keyboard.KeyBoard;
import com.newland.mtype.module.common.light.IndicatorLight;
import com.newland.mtype.module.common.pin.K21Pininput;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.rfcard.RFCardModule;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.BarcodeScannerManager;
import com.newland.mtype.module.common.security.SecurityModule;
import com.newland.mtype.module.common.serialport.SerialModule;
import com.newland.mtype.module.common.storage.Storage;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtypex.nseries.NSConnV100ConnParams;

import static com.newland.me.DeviceManager.DeviceConnState.CONNECTED;
import static com.newland.me.DeviceManager.DeviceConnState.CONNECTING;
import static com.newland.me.DeviceManager.DeviceConnState.DISCONNCECTED;
import static com.newland.me.DeviceManager.DeviceConnState.DISCONNECTING;
import static com.newland.me.DeviceManager.DeviceConnState.NOT_INIT;

public class N900Device extends AbstractDevice {
    private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";
    private static Context context;
    private static N900Device n900Device = null;
    private static DeviceManager deviceManager = ConnUtils.getDeviceManager();
    private Object lock = new Object();

    private N900Device() {
    }

    public static N900Device getInstance(Context context) {
        if (n900Device == null) {
            synchronized (N900Device.class) {
                if (n900Device == null) {
                    n900Device = new N900Device();
                }
            }
        }
        N900Device.context = context.getApplicationContext();
        return n900Device;
    }

    @Override
    public void connectDevice() {
        try {
            deviceManager = ConnUtils.getDeviceManager();
            DeviceManager.DeviceConnState connState = deviceManager.getDeviceConnState();
            Log.e("N900Device state=", connState.name());
            if (connState == NOT_INIT) {
                deviceManager.init(context, K21_DRIVER_NAME, new NSConnV100ConnParams(), new
                        DeviceEventListener<ConnectionCloseEvent>() {
                            @Override
                            public void onEvent(ConnectionCloseEvent event, Handler handler) {
                                if (event.isSuccess()) {
                                    // "设备被客户主动断开!"
                                    Log.e("N900Device", "connect close success");
                                }
                                if (event.isFailed()) {
                                    // "设备链接异常断开!"
                                    Log.e("N900Device", "connect close failed");
                                }
                            }
                            @Override
                            public Handler getUIHandler() {
                                return null;
                            }
                        });
                Thread.sleep(500);
                deviceManager.connect();
            } else if (connState == DISCONNCECTED) {
                deviceManager.connect();
            } else if (connState == DISCONNECTING) {
                while (true) {
                    Thread.sleep(500);
                    if (deviceManager.getDeviceConnState() == DISCONNCECTED) {
                        break;
                    }
                }
                deviceManager.connect();
            } else if (connState == CONNECTING) {
                while (true) {
                    Thread.sleep(500);
                    if (deviceManager.getDeviceConnState() == CONNECTED) {
                        break;
                    }
                }
            }
//            deviceManager.getDevice().setBundle(new NSConnV100ConnParams());
        } catch (Exception e) {
            Log.e("N900Device", e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            if (deviceManager != null) {
                deviceManager.disconnect();
                deviceManager = null;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean isDeviceAlive() {
        return (deviceManager != null) && (deviceManager.getDeviceConnState() == CONNECTED);
    }

    @Override
    public CardReader getCardReaderModuleType() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        CardReader cardReader = (CardReader) device.getStandardModule(ModuleType.COMMON_CARDREADER);
        return cardReader;
    }

    @Override
    public EmvModule getEmvModuleType() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        EmvModule emvModule = (EmvModule) device.getExModule(ExModuleType.EMVINNERLEVEL2);
        return emvModule;
    }

    @Override
    public ICCardModule getICCardModule() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        ICCardModule iCCardModule = (ICCardModule) device.getStandardModule(ModuleType
                .COMMON_ICCARDREADER);
        return iCCardModule;
    }

    @Override
    public IndicatorLight getIndicatorLight() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        IndicatorLight indicatorLight = (IndicatorLight) device.getStandardModule(ModuleType
                .COMMON_INDICATOR_LIGHT);
        return indicatorLight;
    }

    @Override
    public K21Pininput getK21Pininput() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        K21Pininput k21Pininput = (K21Pininput) device.getStandardModule(ModuleType.COMMON_PININPUT);
        return k21Pininput;
    }

    @Override
    public Printer getPrinter() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        Printer printer = (Printer) device.getStandardModule(ModuleType.COMMON_PRINTER);
        printer.init();
        return printer;
    }

    @Override
    public RFCardModule getRFCardModule() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        RFCardModule rFCardModule = (RFCardModule) device.getStandardModule(ModuleType
                .COMMON_RFCARDREADER);
        return rFCardModule;
    }

    @Override
    public BarcodeScanner getBarcodeScanner() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        BarcodeScannerManager barcodeScannerManager = (BarcodeScannerManager) device.getStandardModule(ModuleType
                .COMMON_BARCODESCANNER);
        BarcodeScanner scanner = barcodeScannerManager.getDefault();
        return scanner;
    }

    @Override
    public SecurityModule getSecurityModule() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        SecurityModule securityModule = (SecurityModule) device.getStandardModule(ModuleType
                .COMMON_SECURITY);
        return securityModule;
    }

    @Override
    public Storage getStorage() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        Storage storage = (Storage) device.getStandardModule(ModuleType.COMMON_STORAGE);
        return storage;
    }

    @Override
    public K21Swiper getK21Swiper() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        K21Swiper k21Swiper = (K21Swiper) device.getStandardModule(ModuleType.COMMON_SWIPER);
        return k21Swiper;
    }

    @Override
    public Device getDevice() {
        return deviceManager.getDevice();
    }

    @Override
    public Buzzer getBuzzer() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        return (Buzzer) device.getStandardModule(ModuleType.COMMON_BUZZER);
    }

    @Override
    public ExternalPinInput getExternalPinInput() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        return (ExternalPinInput) device.getStandardModule(ModuleType.EXTERNAL_PININPUT);
    }

    @Override
    public KeyBoard getKeyBoard() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        return (KeyBoard) device.getStandardModule(ModuleType.COMMON_KEYBOARD);
    }

    //	@Override
//	public NlCashBoxManager getNlCashBoxManager(Context context) {
//		return (NlCashBoxManager) context.getSystemService("cashbox_service");
//	}
    @Override
    public CashBoxModule getCashBox() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        CashBoxModule k21CashBox = (CashBoxModule) device.getExModule(ExModuleType.CASHBOX);
        return k21CashBox;
    }

    @Override
    public SerialModule getUsbSerial() {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        SerialModule k21USBSerial = (SerialModule) device.getExModule(ExModuleType.USBSERIAL);
        return k21USBSerial;
    }

    public boolean isDeviceBusy() {
        Device device = getDevice();
        if (device == null) {
            return false;
        }
        return device.isBusy();
    }
}
