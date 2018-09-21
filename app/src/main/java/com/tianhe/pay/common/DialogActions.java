package com.tianhe.pay.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

public class DialogActions extends Fragment implements BaseDialog.Listener {

    private SparseArray<BaseDialog.Listener> listenerSparseArray;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listenerSparseArray = new SparseArray<>();
        this.setRetainInstance(true);
    }

    @Override
    public void onNegative(BaseDialog dialog) {
        BaseDialog.Listener listener = getBoundListener(dialog.getDialogId());
        if (listener != null) {
            listener.onNegative(dialog);
        }
    }

    @Override
    public void onNeutral(BaseDialog dialog) {
        BaseDialog.Listener listener = getBoundListener(dialog.getDialogId());
        if (listener != null) {
            listener.onNeutral(dialog);
        }
    }

    @Override
    public void onPositive(BaseDialog dialog) {
        BaseDialog.Listener listener = getBoundListener(dialog.getDialogId());
        if (listener != null) {
            listener.onPositive(dialog);
        }
    }

    @Override
    public void onDismiss(BaseDialog dialog) {
        unbindListener(dialog.getDialogId());
    }

    @Override
    public void onCancel(BaseDialog dialog) {
        dialog.dismiss();
    }

    public void bindListener(int dialogId, BaseDialog.Listener listener) {
        listenerSparseArray.put(dialogId, listener);
    }

    public void unbindListener(int dialogId) {
        listenerSparseArray.remove(dialogId);
    }

    private BaseDialog.Listener getBoundListener(int dialogId) {
        return listenerSparseArray.get(dialogId);
    }

}
