package com.tianhe.pay.data.crm.payment;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;
import com.tianhe.pay.data.crm.CrmRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Crm称为"支付处理"请求.
 * 在销售单结算完成后的crm相关数据(储值卡、券、积分)变更
 */
public class ProcessPayment extends CrmRequest {
    @CrmDataName("Type")
    String type = CrmConstants.TYPE_SALE; // 单据类型
    @CrmDataName("ooef001")
    String shopNo;              // 门店编号
    @CrmDataName("rtiadocno")
    String saleNo;              // 单号
    @CrmDataName("rtia049")
    String amount;              // 应收金额
    @CrmDataName("mmau_t_8")
    List<StoredValueCardPay>  storedValueCardPays;  // 储值卡支付列表
    @CrmDataName("mmar_t")
    List<PointProcess> pointProcesses;        // 积分处理(新增积分/积分抵现)
    @CrmDataName("gcao_t")
    List<CouponProcess> coupons;    // 券的使用信息

    boolean isTraining;

    public ProcessPayment(String shopNo, boolean isTraining) {
        super(CrmConstants.PROCESS_PAYMENT, shopNo);
        this.shopNo = shopNo;
        this.isTraining = isTraining;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<StoredValueCardPay> getStoredValueCardPays() {
        return storedValueCardPays;
    }

    public void setStoredValueCardPays(List<StoredValueCardPay> storedValueCardPays) {
        this.storedValueCardPays = storedValueCardPays;
    }

    public void addStoredValueCardPay(StoredValueCardPay pay) {
        if (this.storedValueCardPays == null) {
            this.storedValueCardPays = new ArrayList<>();
        }
        this.storedValueCardPays.add(pay);
    }

    public List<PointProcess> getPointProcesses() {
        return pointProcesses;
    }

    public void setPointProcesses(List<PointProcess> pointProcesses) {
        this.pointProcesses = pointProcesses;
    }

    public void setPoint(PointProcess pointProcess) {
        if (pointProcesses == null) {
            pointProcesses = new ArrayList<>();
        }
        this.pointProcesses.add(pointProcess);
    }

    public List<CouponProcess> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponProcess> coupons) {
        this.coupons = coupons;
    }

    @Override
    protected boolean hasMode() {
        return true;
    }

    @Override
    protected boolean isTraining() {
        return isTraining;
    }

    @Override
    protected boolean isProcess() {
        return true;
    }

}
