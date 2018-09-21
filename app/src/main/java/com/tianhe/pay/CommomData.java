package com.tianhe.pay;

import com.tianhe.pay.data.order.calculate.PaymentLimit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangya3 on 2018/3/21.
 */

public class CommomData {
    public static String shopName;
    public static String shopNos;
    public static String terminalID;
    public static String description;
    public static String uuid;
    public static List<PaymentLimit> limitCoupon=new ArrayList<PaymentLimit>();//需要清空，券的接券情况
    public static Map<String, BigDecimal> coupopLimitMap;//接券限制的map

    public static String signName;//重打印的时候，支付宝和微信的判断
    public static String money;//金额
    public static String tracHost;
    public static String authUser;//授权人

    public static ArrayList<String> aliWechatList = new ArrayList<String>();
    public static boolean isCouponReturn;

    /**
     * 单边方便专柜人员（免输入流水号）
     * @param saleNo
     */
    public static void addSaleNo(String saleNo){
        if (CommomData.aliWechatList.size() < 20) {
            CommomData.aliWechatList.add(CommomData.shopNos + saleNo);//支付宝微信单边
        } else {
            CommomData.aliWechatList.remove(0);
            CommomData.aliWechatList.add(CommomData.shopNos + saleNo);//支付宝微信单边
        }
    }
}
