package com.tianhe.pay.data.login;

import com.google.gson.annotations.SerializedName;
import com.tianhe.pay.data.goods.Goods;
import com.tianhe.pay.data.payment.Payment;

import java.util.List;

public class LoginResp {
    private String supplierNo;              // 供应商编号
    private String supplierName;            // 供应商名称
    private String shopNo;                  // 天和门店编号
    private String terminalId;              // 终端编号
    @SerializedName("barcodeList")
    private List<Goods> goodsList;          // 可售卖商品
    private List<Payment> paymentList;      // 可用支付方式
    @SerializedName("padShopParmList")
    private List<ShopParam> shopParamList;  // 门店的参数

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<ShopParam> getShopParamList() {
        return shopParamList;
    }

    public void setShopParamList(List<ShopParam> shopParamList) {
        this.shopParamList = shopParamList;
    }
}
