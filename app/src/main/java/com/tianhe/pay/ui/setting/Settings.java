package com.tianhe.pay.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.tianhe.pay.model.Global;
import com.tianhe.pay.utils.Times;

import java.util.Date;

public class Settings {

    private static final String FILE_NAME = "tianhe";
    private static final String SALE_NO_LOCAL = "saleNo";
    private static final String SERVER_URL = "serverUrl";
    private static final String TRAINING = "training";
    private static final String WECHAT_ALI = "wechatAli";

    private Context context;
    private Global global;

    public Settings(Context context, Global global) {
        this.context = context;
        this.global = global;
    }

    /**
     * @return 获取本地的最新流水号
     */
    public String getLocalSerialNumber() {
        String lastSaleNo = getTodayLastSaleNo();
        if (lastSaleNo != null) {
            long oldSn = Long.valueOf(lastSaleNo.substring(lastSaleNo.length() - 4));
            lastSaleNo = String.format("%04d", oldSn + 1);
        } else {
            lastSaleNo = "0001";
        }
        return lastSaleNo;
    }

    public String skipLocalSerialNumber() {
        String lastSaleNo = getTodayLastSaleNo();
        long oldSn = 0;
        if (lastSaleNo != null) {
            oldSn = Long.valueOf(lastSaleNo.substring(lastSaleNo.length() - 4));
        }
        if (lastSaleNo == null) {
            saveUsedSaleNo(global.getTerminalId() + Times.yyMMdd(new Date()) + String.format("%04d", oldSn + 1));
        } else {
            saveUsedSaleNo(lastSaleNo.substring(0, lastSaleNo.length() - 4).concat(String.format("%04d", oldSn + 1)));
        }
        return String.format("%04d", oldSn + 2);
    }

    public void saveUsedSaleNo(String saleNo) {
        setSetting(SALE_NO_LOCAL, saleNo);
    }

    public void saveServerUrl(String hostAndPort) {
        setSetting(SERVER_URL, hostAndPort);
    }

    public String getServerUrl() {
        return getSetting(SERVER_URL, null);
    }

    /** 当天最后被使用的SaleNo */
    @Nullable
    public String getTodayLastSaleNo() {
        String localSaleNo = getLastSaleNo();
        if (localSaleNo != null) {
            boolean isToday = localSaleNo.startsWith(global.getTerminalId() + Times.yyMMdd(new Date()));
            if (!isToday){
                localSaleNo = null;
            }
        }
        return localSaleNo;
    }

    @Nullable
    public String getLastSaleNo() {
        return getSp().getString(SALE_NO_LOCAL, null);
    }

    @Nullable
    public String getLastWechatAli() {
        return getSp().getString(WECHAT_ALI, null);
    }

    @Nullable
    public void saveWechatAli(String relNo) {
        getSp().edit().putString(WECHAT_ALI, relNo).commit();
    }

    public boolean isTraining() {
//        return getSp().getBoolean(TRAINING, false);
        return  false;
//        return getSp().getBoolean(TRAINING, false);
    }

    public void setTraining(boolean isTraining) {
        getSp().edit().putBoolean(TRAINING, isTraining).commit();
    }

    private SharedPreferences getSp(){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp;
    }

    private void setSetting(String key, String value){
        getSp().edit().putString(key, value).commit();
    }

    private String getSetting(String key, String value){
        return getSp().getString(key, value);
    }

    private void remove(String key) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.remove(key);
        editor.commit();
    }


    private void clear() {
        SharedPreferences.Editor editor = getSp().edit();
        editor.clear().commit();
    }
}
