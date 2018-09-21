package com.tianhe.pay.data.print;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.common.Log;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.crm.gift.GiftCoupon;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.data.order.OrderHeader;
import com.tianhe.pay.data.order.OrderItem;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.data.order.OrderStatisticsPayMerged;
import com.tianhe.pay.data.payment.PaidInfo;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.model.PaymentSignpost;
import com.tianhe.pay.utils.Strings;
import com.tianhe.pay.utils.Times;
import com.tianhe.pay.utils.money.Money;
import com.tianhe.pay.utils.money.MoneyMath;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PrintUtils {

    public static PrintInfo[] wechatAliPrint(WechatAliOrder order) {
        StringBuilder sb = new StringBuilder();
        //要做null的判断。不然买单的时候，微信和支付宝会打成通莞金服，hujie
        if (CommomData.signName != null) {
            order.setPayName(CommomData.signName);
        }
        String payName = order.getPayName();
        if (payName == null) {
            payName = "通莞金服";
        }

        sb.append(payName + "签购单\n");
        if (order.isReprint()) {
            sb.append("- - - - - - - - 重打印 - - - - - - \n");
            if (!TextUtils.isEmpty(CommomData.authUser)) {
                sb.append("授权人: " + CommomData.authUser + "\n");
            }
        } else {
            sb.append("- - - - - - - - - - - - - - - - \n");
            if (!TextUtils.isEmpty(CommomData.authUser)) {
                sb.append("授权人: " + CommomData.authUser + "\n");
            }
        }
        sb.append("门店编号: " + order.getShopNo() + "\n");
        sb.append("操作人员: " + order.getUserNo() + "\n");
        sb.append("终端编号: " + order.getTerminalNo() + "\n");
        sb.append("交易类别:\n");
        sb.append("       " + order.getOrderState() + "\n");
        if (order.getDatetime() != null) {
            sb.append("日期/时间: " + order.getDatetime() + "\n");
        }
        sb.append("参考号:\n");
//        sb.append("    " + order.getUpOrderId() + "\n");
        sb.append("    " + order.getLowOrderId() + "\n");
        sb.append("金额: " + order.getPayMoney().toString() + "\n");

        ArrayList<PrintInfo> list = new ArrayList<>();
        PrintInfo printInfo = new PrintInfo.Default();
        printInfo.addLine(sb.toString() + "- - - - - - - - - - - - - - - - \n\n\n");
        if (!order.isReprint()) {
            PrintInfo printInfo2 = new PrintInfo.Default();
            printInfo2.addLine(sb.toString() + "- - - - - - - 存根联 - - - - - - \n\n\n\n");
            list.add(printInfo2);
        }
        android.util.Log.e("qqq", sb.toString() + "-------------------->签购单");
        printInfo.addLine("\n\n\n\n");
        list.add(printInfo);
        return list.toArray(new PrintInfo[list.size()]);
    }

    public static PrintInfo[] billOrder(Order order, boolean reprint, String supplierName) {
        boolean isTraining = "Y".equals(order.getOrderHeader().getIsPractice());
        StringBuilder sb = new StringBuilder();
        if (isTraining) {
            sb.append("- - - - - - - - - - - - - - - - \n");
            sb.append("             练习模式\n");
            sb.append("- - - - - - - - - - - - - - - - \n");
        }
//        for(global.)
        sb.append("\n\n            天和签购单\n\n");
        OrderHeader header = order.getOrderHeader();
        if (!TextUtils.isEmpty(CommomData.shopName)) {
            sb.append("门店名称:" + CommomData.shopName + "\n\n");
        }
        sb.append("操作人员:" + header.getUserNo() + "\n\n");
        sb.append("专柜名称:" + supplierName + "\n\n");
        sb.append("终端编号:" + header.getTerminalId() + "\n\n");
        sb.append("小票号:\t" + header.getSaleNo() + "\n\n");
        boolean isRefund = !Strings.isBlank(header.getSaleNoSource());
        if (isRefund) {
            sb.append("原单流水号:" + header.getSaleNoSource() + "\n\n");
        }
        sb.append("日期/时间:" + header.getTime() + "\n");
        if (reprint) {
            if (isRefund) {
                sb.append("- - - - - - 退货重打印 - - - - - \n");
            } else {
                sb.append("- - - - - - - 重打印 - - - - - - \n");
            }
        } else {
            if (isRefund) {
                sb.append("- - - - - - - 退货单 - - - - - - \n");
            } else {
                sb.append("- - - - - - - 销售单 - - - - - - \n");
            }
        }
        if (!TextUtils.isEmpty(CommomData.authUser)) {
            sb.append("授权人：" + CommomData.authUser + "\n");
        }
        sb.append("品名   数量  单价  折扣  小计\n");
        List<OrderItem> items = order.getOrderItems();
        Money total = Money.zeroMoney();
        Money saleTotal = Money.zeroMoney();

        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        for (OrderItem item : items) {
            Money itemTotal = item.getSubtotal();
            Money itemSaleTotal = item.getSaleAmount();
            BigDecimal itemDiscount = MoneyMath.divide(itemSaleTotal, itemTotal);
            total = total.add(itemTotal);
            saleTotal = saleTotal.add(itemSaleTotal);
            sb.append(item.getBarcode() + "\n");
            sb.append(String.format("%-7s", item.getName())
                    + String.format("%-4s", String.valueOf(item.getQuantity()))
                    + String.format("%-7s", item.getOldPrice().toString())
                    + String.format("%-4s ", percent.format(itemDiscount.doubleValue()))
                    + String.format("%-7s", itemSaleTotal.toString()) + "\n");
        }
        sb.append("- - - - - - - - - - - - - - - - \n");
        sb.append("应付: " + total.toString() + "     " +
                "实付:" + saleTotal.toString() + "\n");
        sb.append("折扣: " + saleTotal.subtract(total).toString() + "\n");
        sb.append("- - - - - - - - - - - - - - - - \n");
        sb.append("付款方式:\n");
        List<PaidInfo> paidInfos = order.getPaidInfos();
        for (PaidInfo info : paidInfos) {
            if (isRefund) {
                sb.append(info.getPaymentName() + "   -" +
                        info.getSaleAmount().toString() + "\n");
            } else {
                sb.append(info.getPaymentName() + "   " +
                        info.getSaleAmount().toString() + "\n");
            }
            if (info.getPaymentId().equals(PaymentSignpost.BANKCARD.getPaymentId())) {//银行卡
                if (!TextUtils.isEmpty(info.getBillNo())) {//银行卡
                    sb.append("银行卡： " +
                            info.getBillNo().toString() + "\n");
                }
                if (!TextUtils.isEmpty(info.getCardNo())) {//关联号
                    sb.append("\t\t关联号码:" + info.getCardNo() + "\n");
                }

            } else {
                if (info.getBillNo() != null) {
                    sb.append("\t\t关联号码:" + info.getBillNo() + "\n");
                }
            }
        }
        if (order.getOrderHeader().getVipNo() != null) {
            sb.append("会员: " + order.getOrderHeader().getVipNo() + "\n");
            if (isRefund) {
                sb.append("本期积分: -" + order.getPointTotal().toString() + "\n");
            } else {
                sb.append("本期积分: " + order.getPointTotal().toString() + "\n");
            }
        }
        ArrayList<PrintInfo> list = new ArrayList<>();
        PrintInfo printInfo = new PrintInfo.Default();
        if (isTraining) {
            sb.append( "\n- - - - - - - - - - - - - - - - \n             练习模式\n- - - - - - - - - - - - - - - - \n");
//            printInfo.addLine(sb.toString() + "\n- - - - - - - - - - - - - - - - \n             练习模式\n- - - - - - - - - - - - - - - - \n");
        } else {
            sb.append( "- - - - - - - - - - - - - - - - \n");
//            printInfo.addLine(sb.toString() + "- - - - - - - - - - - - - - - - \n");
        }

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (!TextUtils.isEmpty(CommomData.description)) {//打印描述
            String[] str = CommomData.description.split("br");
            for (int i = 0; i < str.length; i++) {
                sb1.append(str[i]+"\n");
//                printInfo.addLine(str[i] + "\n");
            }
        }
//        sb.append("\n\n\n\n");
        printInfo.addLine(sb.toString()+sb1.toString()+"\n\n\n\n");
        list.add(printInfo);
        if (!reprint) {
            PrintInfo printInfo2 = new PrintInfo.Default();
            if (isTraining) {
                sb.append("\n- - - - - - - - - - - - - - - - \n             练习模式存根联\n- - - - - - - - - - - - - - - - \n");
//                printInfo2.addLine(sb.toString() + "\n- - - - - - - - - - - - - - - - \n             练习模式存根联\n- - - - - - - - - - - - - - - - \n");
            } else {
                sb.append("- - - - - - - 存根联 - - - - - - \n");
//                printInfo2.addLine(sb.toString() + "- - - - - - - 存根联 - - - - - - \n");
            }
            if (!TextUtils.isEmpty(CommomData.description)) {//打印描述
                String[] str = CommomData.description.split("br");
                for (int i = 0; i < str.length; i++) {
                    sb.append(str[i]+"\n");
//                    printInfo2.addLine(str[i] + "\n");
                }
//                printInfo2.addLine(sb1.toString());
            }
            printInfo2.addLine(sb.toString()+" \n\n\n\n");
            list.add(printInfo2);
        }
        return list.toArray(new PrintInfo[list.size()]);
    }

    public static PrintInfo[] orderStatistics(Global global, OrderStatistics statistics) {
        StringBuilder sb = new StringBuilder();
        sb.append("            交易统计\n");
        sb.append("终端编号: " + global.getTerminalId() + "\n");
        sb.append("专柜名称: " + global.getSupplierName() + "\n");
        sb.append("统计日期: " + statistics.getCountDate() + "\n");
        sb.append("- - - - - - - - - - - - - - - - \n");
        sb.append("销售数量: " + statistics.getSaleCount() + "单\n");
        sb.append("销售总额: " + statistics.getSaleTotals().toString() + "\n");
        sb.append("退货数量: " + statistics.getRefundCount() + "单\n");
        sb.append("退货总额: " + statistics.getRefundTotal().negate().toString() + "\n");
        sb.append("- - - - - - - - - - - - - - - - \n");
        sb.append("总营业额: " + statistics.getStatisticsTotal().toString() + "\n");
        sb.append("类型         消费      退款\n");
        List<OrderStatisticsPayMerged> payList = statistics.mergePayById();
        for (OrderStatisticsPayMerged osp : payList) {
            sb.append(String.format("%-7s", osp.getPaymentName())
                    + String.format("%-11s", osp.getSaleTotals().toString())
                    + String.format("%-7s", osp.getRefundTotals().negate().toString()) + "\n");
        }
        ArrayList<PrintInfo> list = new ArrayList<>();
        PrintInfo printInfo = new PrintInfo.Default();
        printInfo.addLine(sb.toString() + "- - - - - - - - - - - - - - - - \n\n\n\n");
        list.add(printInfo);
        return list.toArray(new PrintInfo[list.size()]);
    }

    public static PrintInfo[] paperCoupon(Global global, List<GiftCoupon> giftCoupons) {
        PrintInfo printInfo = new PrintInfo.Default();
        for (GiftCoupon coupon : giftCoupons) {
//            StringBuilder sb = new StringBuilder();
            printInfo.addLine("\n            天和赠券\n");
            printInfo.addLine("终端编号: " + global.getTerminalId() + "\n");
            printInfo.addLine("专柜名称: " + global.getSupplierName() + "\n");
            printInfo.addLine("- - - - - - - - - - - - - - - - \n");
//            printInfo.addLine(sb.toString());
            Bitmap bitmap = EncodingUtils.createQRCode(coupon.getCouponNo(), 340, 340, null);
            printInfo.addLine(bitmap);
            printInfo.addLine("券号: " + coupon.getCouponNo());
            printInfo.addLine("金额: " + coupon.getDenomination());
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
            printInfo.addLine("\n");
        }
        ArrayList<PrintInfo> list = new ArrayList<>();
        list.add(printInfo);
        return list.toArray(new PrintInfo[list.size()]);
    }
}
