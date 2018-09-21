package com.tianhe.pay.data.order.calculate;

import com.google.gson.annotations.SerializedName;

/**
 * 赠送(卡／券)
 */
public class Gift {
    @SerializedName("couponType")
    String type;        // (卡/券)种类
    @SerializedName("couponNum")
    int quantity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
