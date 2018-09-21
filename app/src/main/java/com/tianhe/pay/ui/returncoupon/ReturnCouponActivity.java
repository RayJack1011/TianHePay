package com.tianhe.pay.ui.returncoupon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.TianheSign;
import com.tianhe.pay.utils.Times;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 退券
 */
public class ReturnCouponActivity extends AppCompatActivity {
//    @Inject
//    Settings settings;

    EditText coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_coupon);
//        String lastSn = settings.skipLocalSerialNumber();
//        Log.e("qqq",lastSn+"::::::::::");

        initView();
    }

    public String skipLocalSerialNumber() {
        String lastSaleNo = getTodayLastSaleNo();
        long oldSn = 0;
        if (lastSaleNo != null) {
            oldSn = Long.valueOf(lastSaleNo.substring(lastSaleNo.length() - 4));
        }
        if (lastSaleNo == null) {
            saveUsedSaleNo(CommomData.terminalID + Times.yyMMdd(new Date()) + String.format("%04d", oldSn + 1));
        } else {
            saveUsedSaleNo(lastSaleNo.substring(0, lastSaleNo.length() - 4).concat(String.format("%04d", oldSn + 1)));
        }
        return String.format("%04d", oldSn + 2);
    }


    /**
     * @return 获取本地的最新流水号
     */
    public String getLocalSerialNumber() {
        String lastSaleNo = getTodayLastSaleNo();
        if (lastSaleNo != null) {
            long oldSn = Long.valueOf(lastSaleNo.substring(lastSaleNo.length() - 4));
            lastSaleNo = String.format("%04d", oldSn + 1);
        } else {
            lastSaleNo = "0001";
        }
        return lastSaleNo;
    }

    public void saveUsedSaleNo(String saleNo) {
        setSetting(SALE_NO_LOCAL, saleNo);
    }

    private void setSetting(String key, String value) {
        getSp().edit().putString(key, value).commit();
    }

    public String getLastSaleNo() {
        return getSp().getString(SALE_NO_LOCAL, null);
    }

    private SharedPreferences getSp() {
        SharedPreferences sp = getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp;
    }

    private static final String FILE_NAME = "tianhe";
    private static final String SALE_NO_LOCAL = "saleNo";


    /**
     * 当天最后被使用的SaleNo
     */
    public String getTodayLastSaleNo() {
        String localSaleNo = getLastSaleNo();
        if (localSaleNo != null) {
            boolean isToday = localSaleNo.startsWith(CommomData.terminalID + Times.yyMMdd(new Date()));
            if (!isToday) {
                localSaleNo = null;
            }
        }
        return localSaleNo;
    }

    private void initView() {
        coupon = findViewById(R.id.coupon_edt);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(coupon.getText().toString())) {
                    Toast.makeText(ReturnCouponActivity.this, "请输入券号", Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("couponNo", coupon.getText().toString());
                map.put("shopNo", CommomData.shopNos);
                map.put("saleNo", CommomData.terminalID + Times.yyMMddHHmmss(new Date())+getLocalSerialNumber());
                String s = new Gson().toJson(map);
                Log.e("qqq", "券参数入参--->" + s);
                OkHttpUtils.post()
                        .url("http://"+DataSource.HOST_AND_PORT+"/tianHePayService/service/createCoupon")
                        .addParams("data", s.toString())
                        .addParams("sign", new TianheSign().sign(s.toString()))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("qqq", e.getMessage() + "---------------->退券接口回参(单边)");
                                Toast.makeText(ReturnCouponActivity.this, "退券失败", Toast.LENGTH_LONG).show();
                                //弹出退款成功提示框

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("qqq", response + "--------------------->退券成功（单边）");
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if("0".equals(jsonObject.getString("code"))){
                                        Toast.makeText(ReturnCouponActivity.this, "退券成功", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(ReturnCouponActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        });
    }
}
