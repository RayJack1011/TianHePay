package com.tianhe.pay.data.bankcard;

import android.content.Intent;

import com.tianhe.pay.data.bankcard.refund.BankcardRefundRequest;
import com.tianhe.pay.data.bankcard.refund.BankcardRefundResponse;
import com.tianhe.pay.data.bankcard.revoke.BankcardRevokeRequest;
import com.tianhe.pay.data.bankcard.revoke.BankcardRevokeResponse;
import com.tianhe.pay.data.bankcard.sale.BankcardSaleRequest;
import com.tianhe.pay.data.bankcard.sale.BankcardSaleResponse;

import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.TRANS_TYPE;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.OPERATOR_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.OUT_ORDER_ID;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.AMOUNT;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.TRANS_AMOUNT;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.TRANS_TIME;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.ORI_TRANS_TIME;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.CARD_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.VOUCHER_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.ORI_VOUCHER_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.BATCH_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.REFERENCE_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.ORI_REFERENCE_NO;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.ORI_DATE;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.CHANNEL_ID;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.OPEN_ADMIN_VERIFY;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.RESPONSE_CODE;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.MESSAGE;
import static com.tianhe.pay.data.bankcard.BankcardConstant.DataKey.PAY_TYPE;
import static com.tianhe.pay.data.bankcard.BankcardConstant.ACTION;

public class DataUtils {

    public static Intent getStartMisPayIntent(BankcardSaleRequest request) {
        Intent intent = new Intent(ACTION);
        setSaleRequest(intent, request);
        return intent;
    }

    public static Intent getStartMisRevokeIntent(BankcardRevokeRequest request) {
        Intent intent = new Intent(ACTION);
        setRevokeRequest(intent, request);
        return intent;
    }

    public static Intent getStartMisRefundIntent(BankcardRefundRequest request) {
        Intent intent = new Intent(ACTION);
        setRefundRequest(intent, request);
        return intent;
    }

    public static void setSaleRequest(Intent intent, BankcardSaleRequest request) {
        intent.putExtra(TRANS_TYPE, request.getTransType());
        intent.putExtra(OPERATOR_NO, request.getOperatorNo());
        intent.putExtra(OUT_ORDER_ID, request.getOutOrderNo());
        intent.putExtra(AMOUNT, request.getAmount());
    }

    public static BankcardSaleResponse getSaleResponse(Intent intent) {
        BankcardSaleResponse response = new BankcardSaleResponse();
        response.setTransType(intent.getIntExtra(TRANS_TYPE, 0));
        response.setResponseCode(intent.getStringExtra(RESPONSE_CODE));
        response.setMessage(intent.getStringExtra(MESSAGE));
        response.setPayType(intent.getIntExtra(PAY_TYPE, 0));
        response.setAmount(intent.getLongExtra(AMOUNT, 0));
        response.setTransAmount(intent.getLongExtra(TRANS_AMOUNT, 0));
        response.setTransTime(intent.getStringExtra(TRANS_TIME));
        response.setCardNo(intent.getStringExtra(CARD_NO));
        response.setVoucherNo(intent.getStringExtra(VOUCHER_NO));
        response.setBatchNo(intent.getStringExtra(BATCH_NO));
        response.setReferenceNo(intent.getStringExtra(REFERENCE_NO));
        response.setChannelId(intent.getStringExtra(CHANNEL_ID));
        return response;
    }

    public static void setRevokeRequest(Intent intent, BankcardRevokeRequest request) {
        intent.putExtra(TRANS_TYPE, request.getTransType());
        intent.putExtra(OPERATOR_NO, request.getOperatorNo());
        intent.putExtra(OUT_ORDER_ID, request.getOutOrderNo());
        intent.putExtra(ORI_VOUCHER_NO, request.getOriVoucherNo());
        intent.putExtra(ORI_TRANS_TIME, request.getOriTransTime());
        intent.putExtra(OPEN_ADMIN_VERIFY, request.isOpenAdminVerify());
    }

    public static BankcardRevokeResponse getRevokeRequest(Intent intent) {
        BankcardRevokeResponse response = new BankcardRevokeResponse();
        response.setTransType(intent.getIntExtra(TRANS_TYPE, 0));
        response.setResponseCode(intent.getStringExtra(RESPONSE_CODE));
        response.setMessage(intent.getStringExtra(MESSAGE));
        response.setTransAmount(intent.getLongExtra(TRANS_AMOUNT, 0));
        response.setTransTime(intent.getStringExtra(TRANS_TIME));
        response.setCardNo(intent.getStringExtra(CARD_NO));
        response.setVoucherNo(intent.getStringExtra(VOUCHER_NO));
        response.setBatchNo(intent.getStringExtra(BATCH_NO));
        response.setReferenceNo(intent.getStringExtra(REFERENCE_NO));
        response.setOriVoucherNo(intent.getStringExtra(ORI_VOUCHER_NO));
        response.setChannelId(intent.getStringExtra(CHANNEL_ID));
        return response;
    }

    public static void setRefundRequest(Intent intent, BankcardRefundRequest request) {
        intent.putExtra(TRANS_TYPE, request.getTransType());
        intent.putExtra(OPERATOR_NO, request.getOperatorNo());
        intent.putExtra(OUT_ORDER_ID, request.getOutOrderNo());
        intent.putExtra(AMOUNT, request.getAmount());
        intent.putExtra(ORI_REFERENCE_NO, request.getOriReferenceNo());
        intent.putExtra(ORI_DATE, request.getOriDate());
        intent.putExtra(OPEN_ADMIN_VERIFY, request.isOpenAdminVerify());
    }

    public static BankcardRefundResponse getRefundResponse(Intent intent) {
        BankcardRefundResponse response = new BankcardRefundResponse();
        response.setTransType(intent.getIntExtra(TRANS_TYPE, 0));
        response.setResponseCode(intent.getStringExtra(RESPONSE_CODE));
        response.setMessage(intent.getStringExtra(MESSAGE));
        response.setAmount(intent.getLongExtra(AMOUNT, 0));
        response.setTransAmount(intent.getLongExtra(TRANS_AMOUNT, 0));
        response.setTransTime(intent.getStringExtra(TRANS_TIME));
        response.setCardNo(intent.getStringExtra(CARD_NO));
        response.setVoucherNo(intent.getStringExtra(VOUCHER_NO));
        response.setBatchNo(intent.getStringExtra(BATCH_NO));
        response.setReferenceNo(intent.getStringExtra(REFERENCE_NO));
        response.setOriReferenceNo(intent.getStringExtra(ORI_REFERENCE_NO));
        response.setChannelId(intent.getStringExtra(CHANNEL_ID));
        return response;
    }

}
