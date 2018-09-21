package com.tianhe.pay.ui.wechatalireprint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.common.Util;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.data.TianheSign;
import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.print.WechatAliOrder;
import com.tianhe.pay.model.Global;
import com.tianhe.pay.ui.TianHeFragment;
import com.tianhe.pay.utils.Strings;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import javax.inject.Inject;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;
import static com.tianhe.pay.data.DataSource.ALI_REFUND;
import static com.tianhe.pay.data.DataSource.SCHEME;
import static com.tianhe.pay.data.DataSource.WX_REFUND;

public class WechatAliReprintFragment extends TianHeFragment
        implements WechatAliReprintContract.View {

    private static final int ID_DIALOG_REPRINTING = BaseDialog.getAutoId();
    private static final int REQUEST_WECHAT_FOR_REFUND = 1001;
    private static final int REQUEST_ALI_FOR_REFUND = 1002;
    private static final int REQUEST_FOR_REPRINT = 1003;

    public static WechatAliReprintFragment newInstance(WechatAliOrder order) {
        WechatAliReprintFragment fragment = new WechatAliReprintFragment();
        fragment.getArguments().putSerializable("order", order);
        return fragment;
    }

    ViewHolder viewHolder;
    WechatAliOrder wechatAliOrder;
    @Inject
    WechatAliReprintPresenter presenter;
    @Inject
    Global global;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wechatali_detail, container, false);
    }

    String type = "";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wechatAliOrder = (WechatAliOrder) getArguments().getSerializable("order");
        type = wechatAliOrder.getPayName();
        viewHolder = new ViewHolder(view);
        viewHolder.shopNoTv.setText(wechatAliOrder.getShopNo());
        viewHolder.terminalNoTv.setText(wechatAliOrder.getTerminalNo());
        String terminalNo = wechatAliOrder.getTerminalNo();
        if (Strings.isBlank(terminalNo)) {
            terminalNo = global.getTerminalId();
        }
        viewHolder.terminalNoTv.setText(terminalNo);
        viewHolder.userNoTv.setText(wechatAliOrder.getUserNo());
        viewHolder.orderStateTv.setText(wechatAliOrder.getOrderState());
        viewHolder.timeTv.setText(wechatAliOrder.getDatetime());
//        viewHolder.relNoTv.setText(wechatAliOrder.getUpOrderId());
        viewHolder.relNoTv.setText(wechatAliOrder.getLowOrderId());
        viewHolder.amountTv.setText(wechatAliOrder.getPayMoney().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reprint, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reprint) {
            requestAuth(REQUEST_FOR_REPRINT);
//            reprint();
            return true;
        } else if (id == R.id.refund) {//退款
            if (CommomData.signName.equals("微信")) {
                requestAuth(REQUEST_WECHAT_FOR_REFUND);
            } else if (CommomData.signName.equals("支付宝")) {
                requestAuth(REQUEST_ALI_FOR_REFUND);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestAuth(int requestCode) {
        nav.enterQueryAuthForResult(this, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_WECHAT_FOR_REFUND:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasRefundAuth()) {//退货
                        queryByWechat();
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            case REQUEST_ALI_FOR_REFUND:
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasReprintAuth()) {
                        queryByAli();
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            case REQUEST_FOR_REPRINT://重打印权限
                if (resultCode == RESULT_OK) {
                    Auth auth = (Auth) data.getSerializableExtra("data");
                    if (auth.hasReprintAuth()) {
                        reprint();
//                        queryByAli();
                    } else {
                        showMessage("没有退货权限");
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 支付宝退款
     */
    private void queryByAli() {
        String saleNo = viewHolder.relNoTv.getText().toString();//流水号
        String moneyes = viewHolder.amountTv.getText().toString();

        /**
         * ，方法名：wxRefund，参数为： * * @param out_trade_no 退款单的流水号
         *
         * @param out_refund_no
         *            原单流水号
         * @param total_fee
         *            原单支付总金额
         */
        SharedPreferences sp = getActivity().getSharedPreferences("tianhe",
                Context.MODE_PRIVATE);
        String urls = sp.getString("serverUrl", null);
        JSONObject data = new JSONObject();
        try {
            data.put("out_trade_no",saleNo);
//            data.put("out_refund_no",saleNo);
            data.put("total_fee",moneyes);
            data.put("shop_no",wechatAliOrder.getShopNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("qqq",data.toString()+"-------------------->支付宝退款参数");
        OkHttpUtils
                .post()
                .url(SCHEME + urls+ ALI_REFUND)
                .addParams("data", data.toString())
                .addParams("sign", new TianheSign().sign(data.toString()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("qqq", e.getMessage() + "---------------->支付宝退款接口回参(单边)");
                        Toast.makeText(getContext(), "退款失败", Toast.LENGTH_LONG).show();
                        //弹出退款成功提示框

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("qqq", response + "--------------------->支付宝退款成功（单边）");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("code").equals("0")){
                                Toast.makeText(getContext(),"退款成功",Toast.LENGTH_LONG).show();
                                showProgress(ID_DIALOG_REPRINTING, "正在打印...");
                                wechatAliOrder.setOrderState("退款");
                                wechatAliOrder.setReprint(false);
                                wechatAliOrder.setTerminalNo(global.getTerminalId());
                                presenter.reprint(wechatAliOrder);
                            }else{
                                Toast.makeText(getContext(),"退款失败！",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 微信退款
     */
    private void queryByWechat() {
//        int refundTotal = (int) refundDataManager.getOriSaleAmountTotal().getAmount();//金额
        String saleNo = viewHolder.relNoTv.getText().toString();//流水号
        String moneyes = viewHolder.amountTv.getText().toString();
        int moneys = new BigDecimal(moneyes).multiply(new BigDecimal(100)).intValue();
        RefundReqData.Builder builder = RefundReqData.newBuilder();
        builder.setBizParams("", saleNo + "01",
                saleNo, moneys, moneys);
        RefundReqData request = builder.build();

        Log.e("qqq", new Gson().toJson(request) + "----------------->微信退款接口入参");

        String requestxml = Util.objectToXml(request);
//        OkHttpClient.Builder builders = new OkHttpClient.Builder();
//        builders.sslSocketFactory(SSLContextFactory.getInstance());
        SharedPreferences sp = getActivity().getSharedPreferences("tianhe",
                Context.MODE_PRIVATE);
        /**
         * ，方法名：wxRefund，参数为： * * @param out_trade_no 退款单的流水号
         *
         * @param out_refund_no
         *            原单流水号
         * @param total_fee
         *            原单支付总金额
         * @param refund_fee
         *            本次退款金额

         */
        String urls = sp.getString("serverUrl", null);
        JSONObject data = new JSONObject();
        try {
            data.put("out_trade_no",saleNo);
            data.put("out_refund_no",saleNo);
            data.put("total_fee",moneys+"");
            data.put("refund_fee",moneys+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("qqq",data.toString()+"-------------------->微信退款借口");
        OkHttpUtils
                .post()
                .url(SCHEME + urls+ WX_REFUND)
                .addParams("data", data.toString())
                .addParams("sign", new TianheSign().sign(data.toString()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("qqq", e.getMessage() + "---------------->微信退款接口回参(单边)");
                        Toast.makeText(getContext(), "退款失败", Toast.LENGTH_LONG).show();
                        //弹出退款成功提示框

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("qqq", response + "--------------------->微信退款成功（单边）");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("code").equals("0")){
                                Toast.makeText(getContext(),"退款成功",Toast.LENGTH_LONG).show();
                                showProgress(ID_DIALOG_REPRINTING, "正在打印...");
                                wechatAliOrder.setOrderState("退款");
                                wechatAliOrder.setReprint(false);
                                wechatAliOrder.setTerminalNo(global.getTerminalId());
                                presenter.reprint(wechatAliOrder);
                            }else{
                                Toast.makeText(getContext(),"退款失败！",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected WechatAliReprintContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void reprintSuccess() {
        dismissDialog(ID_DIALOG_REPRINTING);
        getActivity().finish();
    }

    @Override
    public void reprintFail(String reason) {
        dismissDialog(ID_DIALOG_REPRINTING);
        showMessage("重打印失败:" + reason);
    }

    private void reprint() {
        showProgress(ID_DIALOG_REPRINTING, "正在重打印...");
        presenter.reprint(wechatAliOrder);
    }

    private static class ViewHolder {
        TextView shopNoTv;
        TextView terminalNoTv;
        TextView userNoTv;
        TextView orderStateTv;
        TextView timeTv;
        TextView relNoTv;
        TextView amountTv;

        public ViewHolder(View view) {
            shopNoTv = view.findViewById(R.id.print_store_name);
            terminalNoTv = view.findViewById(R.id.print_pos_no);
            userNoTv = view.findViewById(R.id.print_user_no);
            orderStateTv = view.findViewById(R.id.print_trade_type);
            timeTv = view.findViewById(R.id.print_datetime);
            relNoTv = view.findViewById(R.id.print_thirdparty_no);
            amountTv = view.findViewById(R.id.print_amount);
        }
    }
}
