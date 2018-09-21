package com.tianhe.pay.data.crm.storedvaluecard;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;
import com.tianhe.pay.data.crm.CrmRequest;

public class QueryStoredValueCard extends CrmRequest {
    @CrmDataName("type")
    String type = "3";      // 操作类型: 储值卡付款前查询
//    String type = "1";      // 操作类型: 储值卡充值前查询
    @CrmDataName("ooef001")
    String shopNo;          // 门店编号
    @CrmDataName("mmaq001")
    String cardNo;          // 卡号
    @CrmDataName("mmaq004")
    String password;
    @CrmDataName("mmaqua001")
    String cardValidCode;   // 验证码

    public QueryStoredValueCard(String shopNo) {
        super(CrmConstants.QUERY_SINGLE_CARD, shopNo);
        this.shopNo = shopNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCardValidCode() {
        return cardValidCode;
    }

    public void setCardValidCode(String cardValidCode) {
        this.cardValidCode = cardValidCode;
    }
}
