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

public class IM81Device extends AbstractDevice {
	private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";
	private static Context activity;
	private static IM81Device im81Device=null;
	private static DeviceManager deviceManager = ConnUtils.getDeviceManager();

	private IM81Device(Context baseactivity) {
		IM81Device.activity = baseactivity;
	}

	public static IM81Device getInstance(Context baseactivity) {
		if (im81Device == null) {
			synchronized (IM81Device.class) {
				if (im81Device == null) {
					im81Device = new IM81Device(baseactivity);
				}	
			}
		}
		IM81Device.activity = baseactivity;
		return im81Device; 
	}

	@Override
	public void connectDevice() {
		
		Log.v("gamemes","设备连接中...");
//		baseActivity.showMessage("设备连接中..", MessageTag.TIP);
		try {
			deviceManager = ConnUtils.getDeviceManager();
			//NSConnV100ConnParams
			deviceManager.init(activity, K21_DRIVER_NAME, new NSConnV100ConnParams(), new DeviceEventListener<ConnectionCloseEvent>() {
				@Override
				public void onEvent(ConnectionCloseEvent event, Handler handler) {
					if (event.isSuccess()) {
//						baseActivity.showMessage("设备被客户主动断开！", MessageTag.NORMAL);
						Log.v("gamemes","设备被客户主动断开！");
					}
					if (event.isFailed()) {
//						baseActivity.showMessage("设备链接异常断开！", MessageTag.ERROR);
						Log.v("gamemes","设备链接异常断开！");
					}	
				}

				@Override
				public Handler getUIHandler() {
					return null;
				}
			});

			Log.v("gamemes","设备控制器已初始化!！");
			
//			baseActivity.showMessage("设备控制器已初始化!", MessageTag.TIP);
			deviceManager.connect();
			deviceManager.getDevice().setBundle(new NSConnV100ConnParams());
//			baseActivity.showMessage("设备连接成功.", MessageTag.TIP);
			Log.v("gamemes","设备连接成功!！");
//			baseActivity.btnStateToWaitingConn();
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.v("gamemes","链接异常,请检查设备或重新连接..."+e1);
//			baseActivity.showMessage("链接异常,请检查设备或重新连接..."+e1, MessageTag.ERROR);
		}
	}

	@Override
	public void disconnect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (deviceManager != null) {
						deviceManager.disconnect();				
						deviceManager = null;
//						baseActivity.showMessage("设备断开成功...", MessageTag.TIP);
//						baseActivity.btnStateToWaitingInit();
						Log.v("gamemes","设备断开成功...");
						
					}
				} catch (Exception e) {
//					baseActivity.showMessage("设备断开异常:" + e, MessageTag.TIP);
					Log.v("gamemes","设备断开异常...");
				}
			}
		}).start();
	}

	@Override
	public boolean isDeviceAlive() {
		boolean ifConnected = (deviceManager == null ? false : deviceManager.getDevice().isAlive());
        return ifConnected;
	}

	@Override
	public CardReader getCardReaderModuleType() {
		
		Log.v("gamemes","(deviceManager==null)"+(deviceManager==null)+"--"+(deviceManager.getDevice() == null));
		
		CardReader cardReader=(CardReader) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_CARDREADER);
		return cardReader;
	}

	@Override
	public EmvModule getEmvModuleType() {
		EmvModule emvModule=(EmvModule) deviceManager.getDevice().getExModule(ExModuleType.EMVINNERLEVEL2);
		return emvModule;
	}

	@Override
	public ICCardModule getICCardModule() {
		ICCardModule iCCardModule=(ICCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_ICCARDREADER);
		return iCCardModule;
	}

	@Override
	public IndicatorLight getIndicatorLight() {
		IndicatorLight indicatorLight=(IndicatorLight) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_INDICATOR_LIGHT);
		return indicatorLight;
	}

	@Override
	public K21Pininput getK21Pininput() {
		K21Pininput k21Pininput=(K21Pininput) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PININPUT);
		return k21Pininput;
	}

	@Override
	public Printer getPrinter() {
		Printer printer=(Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
		printer.init();
		return printer;
	}

	@Override
	public RFCardModule getRFCardModule() {
		RFCardModule rFCardModule=(RFCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_RFCARDREADER);
		return rFCardModule;
	}

	@Override
	public BarcodeScanner getBarcodeScanner() {
		BarcodeScannerManager barcodeScannerManager=(BarcodeScannerManager) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_BARCODESCANNER);
		BarcodeScanner scanner = barcodeScannerManager.getDefault();
		return scanner;
	}

	@Override
	public SecurityModule getSecurityModule() {
		SecurityModule securityModule=(SecurityModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SECURITY);
		return securityModule;
	}

	@Override
	public Storage getStorage() {
		Storage storage=(Storage) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_STORAGE);
		return storage;
	}

	@Override
	public K21Swiper getK21Swiper() {
		K21Swiper k21Swiper=(K21Swiper) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SWIPER);
		return k21Swiper;
	}

	@Override
	public Device getDevice() {
		return deviceManager.getDevice();
	}

	@Override
	public Buzzer getBuzzer() {
		return (Buzzer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_BUZZER);
	}

	@Override
	public ExternalPinInput getExternalPinInput() {
		// TODO Auto-generated method stub
		return  (ExternalPinInput) deviceManager.getDevice().getStandardModule(ModuleType.EXTERNAL_PININPUT);


	}

	@Override
	public KeyBoard getKeyBoard() {
		return (KeyBoard) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_KEYBOARD);
	}

//	@Override
//	public NlCashBoxManager getNlCashBoxManager(Context context) {
//		return (NlCashBoxManager) context.getSystemService("cashbox_service");
//	}
	@Override
	public CashBoxModule getCashBox() {
		CashBoxModule k21CashBox=(CashBoxModule) deviceManager.getDevice().getExModule(ExModuleType.CASHBOX);
		return k21CashBox;
	}

	@Override
	public SerialModule getUsbSerial() {
		SerialModule k21USBSerial=(SerialModule) deviceManager.getDevice().getExModule(ExModuleType.USBSERIAL);
		return k21USBSerial;
	}
}
