package com.tianhe.pay.data.crm;

import com.tianhe.pay.data.Md5Sign;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.crm.member.QueryMember;
import com.tianhe.pay.data.crm.payment.ProcessPayment;
import com.tianhe.pay.data.crm.payment.ProcessPaymentResult;
import com.tianhe.pay.data.crm.payment.StoredValueCardPay;
import com.tianhe.pay.data.crm.storedvaluecard.QueryStoredValueCard;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;

import java.util.UUID;

public class MemberTest {

    public static void main(String[] args) {
        paymentProcess();
    }

    private static void queryMember() {
        QueryMember request = new QueryMember("1002");
        request.setQueryNo("120000080354");
        try {
            String result = executeRequest(request);
            CrmResponse<Member> response = CrmResponse.parseFromXml(result, Member.class);
            Member member = response.getSingleData();
            System.out.print(member.getMemeberName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void queryStoredValueCard() {
        QueryStoredValueCard request = new QueryStoredValueCard("1002");
//        request.setCardNo("120000080354");
        request.setCardNo("680000207760");
        try {
            String result = executeRequest(request);
            CrmResponse<StoredValueCard> response = CrmResponse.parseFromXml(result, StoredValueCard.class);
            StoredValueCard card = response.getSingleData();
            System.out.print(card.getCardNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void paymentProcess() {
        ProcessPayment processPayment = new ProcessPayment("1012", true);
        processPayment.setAmount("0.01");
        processPayment.setSaleNo("0071712221538440002");
        processPayment.setType("1");    // 销售单

        StoredValueCardPay cardPay = new StoredValueCardPay();
        cardPay.setVerifyCode("0400473");
        cardPay.setCardNo("680000207760");
        cardPay.setPassword("0400473");
        cardPay.setAmount("0.01");
        cardPay.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        processPayment.addStoredValueCardPay(cardPay);
        try {
            String result = executeRequest(processPayment);
            CrmResponse<ProcessPaymentResult> response = CrmResponse.parseFromXml(result, ProcessPaymentResult.class);
            ProcessPaymentResult paymentResult = response.getSingleData();
            System.out.print(paymentResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String executeRequest(CrmRequest request) throws Exception {
        Md5Sign md5Sign = new Md5Sign();

        CrmApiImpl crmApi = new CrmApiImpl(md5Sign);
        return crmApi.executeRequest(request);
    }
}
