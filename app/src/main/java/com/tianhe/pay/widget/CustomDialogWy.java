package com.tianhe.pay.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianhe.pay.R;

/**
 * Created by wangya3 on 2018/7/30.
 */

public class CustomDialogWy {



    Dialog bottomDialog;
    private static CustomDialogWy instance = new CustomDialogWy();

    private CustomDialogWy() {
    }

    public static CustomDialogWy getInstance() {
        return instance;
    }

    /**
     * 通过文本的方式显示的dialog
     *
     * @param context
     * @param msg             提示的信息
     * @param sure            确认按钮显示的内容
     * @param cancle          取消按钮显示的内容
     * @param onClickListener 确认按钮点击事件
     */
    public void showTextDialog(Context context, String msg, String sure, String cancle, View.OnClickListener onClickListener) {
        TextView content;
        TextView sureBtn;
        TextView cancleBtn;
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_deletes, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        content = contentView.findViewById(R.id.number);
        sureBtn = contentView.findViewById(R.id.sure);
        cancleBtn = contentView.findViewById(R.id.cancle);
        if (!TextUtils.isEmpty(msg)) {
            content.setText(msg);
        }
        if (!TextUtils.isEmpty(sure))
            sureBtn.setText(sure);
        if (!TextUtils.isEmpty(cancle))
            cancleBtn.setText(cancle);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
//        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        contentView.findViewById(R.id.sure).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });
    }

    /**
     * 通过文本的方式显示的dialog
     *
     * @param context
     * @param msg             提示的信息
     * @param sure            确认按钮显示的内容
     * @param cancle          取消按钮显示的内容
     * @param onClickListener 确认按钮点击事件
     */
    public void showTextDialog(Context context, String msg, String sure, String cancle, boolean isShow, View.OnClickListener onClickListener, View.OnClickListener cancleClick) {
        TextView content;
        TextView sureBtn;
        TextView cancleBtn;
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_deletes, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        content = contentView.findViewById(R.id.number);
        sureBtn = contentView.findViewById(R.id.sure);
        cancleBtn = contentView.findViewById(R.id.cancle);
        if (!TextUtils.isEmpty(msg)) {
            content.setText(msg);
        }
        if (!TextUtils.isEmpty(sure))
            sureBtn.setText(sure);
        if (!TextUtils.isEmpty(cancle))
            cancleBtn.setText(cancle);
        if (isShow) {
            cancleBtn.setVisibility(View.VISIBLE);
        } else {
            cancleBtn.setVisibility(View.GONE);
        }
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
//        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        contentView.findViewById(R.id.sure).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.cancle).setOnClickListener(cancleClick);
    }

    public void dismiss() {
        if (bottomDialog != null) {
            bottomDialog.dismiss();
        }
    }

}
