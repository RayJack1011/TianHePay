package com.tianhe.pay.ui;

import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.common.DialogControl;
import com.tianhe.pay.common.FragmentManagersProvider;

public class DialogControlHelper {
    private DialogControl control;

    public DialogControlHelper(FragmentManagersProvider provider) {
        this.control = provider.provideFragmentManagers();
    }

    public void bindDialogListener(int dialogId, BaseDialog.Listener listener) {
        if (control != null) {
            control.bindDialogListener(dialogId, listener);
        }
    }

    public void unbindDialogListener(int dialogId) {
        if (control != null) {
            control.unbindDialogListener(dialogId);
        }
    }

    public void showDialog(MdDialog.Builder builder) {
        if (control != null && builder != null) {
            control.showDialog(builder);
        }
    }

    public void dismiss(int dialogId) {
        if (control != null) {
            control.dismissDialog(dialogId);
        }
    }
}
