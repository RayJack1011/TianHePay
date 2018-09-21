package com.tianhe.pay.model;

import android.util.Log;

import com.google.gson.Gson;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.login.LoginResp;
import com.tianhe.pay.data.goods.Goods;
import com.tianhe.pay.data.login.ShopParam;
import com.tianhe.pay.data.payment.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Global {
    private List<Goods> goodsList;
    private List<Payment> usablePayments;
    private List<ShopParam> shopParams;
    private String shopNo;
    private String supplierName;            // 供应商名称
    private String terminalId;
    private String userNo;

    public void onLoginSuccess(String userNo, LoginResp resp) {
        this.userNo = userNo;
        this.goodsList = resp.getGoodsList();
        this.shopNo = resp.getShopNo();
        this.terminalId = resp.getTerminalId();
        this.usablePayments = resp.getPaymentList();
        this.shopParams = resp.getShopParamList();

        Log.e("qqq","登陆接口回参----->"+new Gson().toJson(resp));
        CommomData.shopNos = resp.getShopNo();
        CommomData.terminalID =resp.getTerminalId();
        for(ShopParam param:shopParams){
            if("03".equals(param.getParamNo())){
                CommomData.shopName = param.getParamValue();

            }else if("04".equals(param.getParamNo())){//描述
                CommomData.description= param.getParamValue();
            }
        }
        this.supplierName = resp.getSupplierName();
    }

    public List<Goods> getSellingGoods() {
        if (goodsList == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(goodsList);
    }

    public Goods findGoodsByBarcode(String barcode) {
        for (Goods goods : goodsList) {
            if (goods.getBarcode().equals(barcode)) {
                return goods;
            }
        }
        return null;
    }

    public List<Payment> getUsablePayments() {
        List<Payment> payments = new ArrayList<>(usablePayments.size());
        payments.addAll(usablePayments);
        return payments;
    }

    public String getShopNo() {
        return shopNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public String getUserNo() {
        return userNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getWechatAliAccount() {
        if (shopParams == null) {
            return null;
        }
        for (ShopParam param : shopParams) {
            if (param.isWechatAliAccount()) {
                return param.getParamValue();
            }
        }
        return null;
    }

    public String getWechatAliKey() {
        if (shopParams == null) {
            return null;
        }
        for (ShopParam param : shopParams) {
            if (param.isWechatAliKey()) {
                return param.getParamValue();
            }
        }
        return null;
    }
}
