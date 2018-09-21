package com.tianhe.pay.data.crm.gift;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;
import com.tianhe.pay.data.crm.CrmRequest;

import java.util.List;

/**
 * Crm称为"赠品处理"请求.
 * 用来处理送卡、送券(纸质)
 */
public class ProcessGift extends CrmRequest {
    /** 单据类型: 1－销售单, 2-充值 */
    @CrmDataName("Type")
    String type;

    /** 门店编号 */
    @CrmDataName("ooef001")
    String shopNo;

    /** 单号 */
    @CrmDataName("rtiadocno")
    String saleNo;

    @CrmDataName("mmaq_t")
    List<GiftCard> giftCards;

    @CrmDataName("gcao_q")
    List<GiftCoupon> giftCoupons;

    boolean isTraining;

    public ProcessGift(String shopNo, boolean isTraining) {
        super(CrmConstants.GIFT_PROCESS, shopNo);
        this.isTraining = isTraining;
    }

    public String getType() {
        return type;
    }

//    public void setType(String type) {
//        this.type = type;
//    }

    public void setSaleType() {
        this.type = CrmConstants.TYPE_SALE;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public List<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(List<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }

    public List<GiftCoupon> getGiftCoupons() {
        return giftCoupons;
    }

    public void setGiftCoupons(List<GiftCoupon> giftCoupons) {
        this.giftCoupons = giftCoupons;
    }

    @Override
    public boolean isTraining() {
        return isTraining;
    }

    @Override
    protected boolean hasMode() {
        return true;
    }

    @Override
    protected boolean isProcess() {
        return true;
    }
}
