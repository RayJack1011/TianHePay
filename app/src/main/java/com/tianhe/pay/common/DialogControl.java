package com.tianhe.pay.common;

public interface DialogControl {
    void dismissDialog(int dialogId);

    void showDialog(BaseDialog.Builder builder);

    void bindDialogListener(int dialogId, BaseDialog.Listener listener);

    void unbindDialogListener(int dialogId);
}
