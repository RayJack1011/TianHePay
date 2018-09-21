package com.tianhe.pay.data.crm.payment;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;

public class PointProcess {
    @CrmDataName("mmar004")
    String type;
    @CrmDataName("mmar001")
    String cardNo;          // 卡号
    @CrmDataName("mmar008")
    String amount;          // 异动金额(正数表示销售, 负数表示退货)
    @CrmDataName("mmar009")
    String point;           // 异动积点(正数表示销售, 负数表示退货)
    @CrmDataName("mmaq018")
    String remainingPoint;       // 剩余积点
    @CrmDataName("mmar100")
    String uuid;

    public String getType() {
        return type;
    }

    public void setSaleType() {
        this.type = CrmConstants.TYPE_POINT_FOR_SALE;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRemainingPoint() {
        return remainingPoint;
    }

    public void setRemainingPoint(String remainingPoint) {
        this.remainingPoint = remainingPoint;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
