package com.tianhe.pay.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tianhe.pay.R;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.ui.TianHeFragment;

import javax.inject.Inject;

public class SettingFragment extends TianHeFragment {
    @Inject
    Settings settings;

    private ViewHolder viewHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);
        viewHolder.snTv.setText(getString(R.string.settings_current_sn, settings.getLocalSerialNumber()));
        viewHolder.skipSaleNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipSaleNo();
            }
        });
        viewHolder.trainingSwitch.setChecked(isTraining());
        viewHolder.trainingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("qqq",isChecked+":::::::::::hehe");
                setTraining(isChecked);
            }
        });

        viewHolder.lock.setOnClickListener(new View.OnClickListener() {//锁屏
            @Override
            public void onClick(View v) {
               login();
            }
        });
        viewHolder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
    }

    private void modify() {
        nav.enterModify(this);
    }

    public void login(){
        nav.enterLogin(this);
    }

    @Override
    public void onDestroy() {
        viewHolder = null;
        settings = null;
        super.onDestroy();
    }

    @Override
    protected <P extends SavablePresenter> P getPresenter() {
        return null;
    }

    private void skipSaleNo() {
        String lastSn = settings.skipLocalSerialNumber();
        viewHolder.snTv.setText(getString(R.string.settings_current_sn, lastSn));
    }

    private void setTraining(boolean isTraining) {
        settings.setTraining(isTraining);
    }

    private boolean isTraining() {
        return settings.isTraining();
    }

    private static class ViewHolder {
        Switch trainingSwitch;
        TextView snTv;
        Button skipSaleNoBtn;
        TextView lock;
        TextView modify;

        public ViewHolder(View view) {
            trainingSwitch = view.findViewById(R.id.fragment_settings_training_switch);
            skipSaleNoBtn = view.findViewById(R.id.fragment_settings_skip_sn_button);
            snTv = view.findViewById(R.id.fragment_settings_current_sn);
            lock = view.findViewById(R.id.locks);
            modify = view.findViewById(R.id.modify);
        }
    }
}
