package com.tianhe.pay.ui.modify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tianhe.pay.CommomData;
import com.tianhe.pay.R;
import com.tianhe.pay.common.StatesContainer;
import com.tianhe.pay.data.Md5Sign;
import com.tianhe.pay.data.TianheSign;
import com.tianhe.pay.ui.MdDialog;
import com.tianhe.pay.ui.login.LoginActivity;
import com.tianhe.pay.ui.login.LoginContract;
import com.tianhe.pay.ui.payment.PayActivity;
import com.tianhe.pay.ui.setting.Settings;
import com.tianhe.pay.widget.MyDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.tianhe.pay.data.DataSource.LOGIN;
import static com.tianhe.pay.data.DataSource.MODIFY;
import static com.tianhe.pay.data.DataSource.SCHEME;

/**
 * 修改密码
 */
public class ModifyActivity extends AppCompatActivity {
    private ImageView icBack;
    private TextView userName;
    private TextView oldPwd;
    private TextView newPwd, newPwd1;

    Settings settings;

    @Inject
    public ModifyContract.Presenter presenter;
    String url = "";

    MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        SharedPreferences sp = this.getSharedPreferences("tianhe",
                Context.MODE_PRIVATE);
        url = sp.getString("serverUrl", null);

        initView();
        initData();
    }

    private void initData() {
        findViewById(R.id.modify_btn).setOnClickListener(new View.OnClickListener() {//修改密码
            @Override
            public void onClick(View v) {
                if (!newPwd.getText().toString().equals(newPwd1.getText().toString())) {
                    Toast.makeText(ModifyActivity.this, "两次输入新密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog =MyDialog.showDialog(ModifyActivity.this);
                dialog.show();
                initNet();
            }
        });
    }

    /***
     *
     */
    private void initNet() {
        String name = userName.getText().toString();
        String oldPwds = oldPwd.getText().toString();
        String newPwds = newPwd.getText().toString();
        Log.e("qqq", oldPwds + ":::::::::::原密码");
        Md5Sign md5Sign = new Md5Sign();
        try {
//            JSONObject jsonObject = new JSONObject();
            final JSONObject js = new JSONObject();
            js.put("userNo", name);
            js.put("oldPasswd", md5Sign.sign(name + oldPwds));
            js.put("newPasswd", md5Sign.sign(name + newPwds));
            js.put("shopNo", CommomData.shopNos);
//            jsonObject.put("data", js);
//            jsonObject.put("sign", new TianheSign().sign(jsonObject.toString()));
            Log.e("qqq", js.toString() + "::::::::::hehda");
            OkHttpUtils
                    .post()
                    .url(SCHEME + url + MODIFY)
                    .addParams("sign", new TianheSign().sign(js.toString()))
                    .addParams("data", js.toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("qqq", e.getMessage() + "::::::::::");
                            dialog.dismiss();
                            Toast.makeText(ModifyActivity.this,"连接服务器失败",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("qqq", response + ":::::;f返回陈宫");
                            dialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if("0".equals(jsonObject.getString("code"))){
                                    Toast.makeText(ModifyActivity.this,"密码修改成功",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ModifyActivity.this, LoginActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(ModifyActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        icBack = findViewById(R.id.back);
        userName = findViewById(R.id.username);
        oldPwd = findViewById(R.id.old_pwd);
        newPwd = findViewById(R.id.new_pwd);
        newPwd1 = findViewById(R.id.new_pwd1);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
