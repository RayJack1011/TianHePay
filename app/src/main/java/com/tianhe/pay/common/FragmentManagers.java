package com.tianhe.pay.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tianhe.pay.common.StatesContainer.ListStatesContainer;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class FragmentManagers implements BaseDialog.Listener, DialogControl {
    private static final String TAG = FragmentManagers.class.getName();
    private static final String TAG_STATE_MANAGER = "StateDataStore";
    private static final String TAG_DIALOG_MANAGER = "DialogActions";

    public static FragmentStackManagerFragment getFragmentStackManager(
            FragmentActivity activity, int contentId) {
        String stackTag = getStackTag(contentId);
        FragmentStackManagerFragment stackManager =
                (FragmentStackManagerFragment) activity.getSupportFragmentManager()
                        .findFragmentByTag(stackTag);
        if (stackManager == null) {
            stackManager = FragmentStackManagerFragment.newInstance(contentId);
        }
        activity.getSupportFragmentManager().beginTransaction().add(stackManager, stackTag).commit();
        return stackManager;
    }

    private static String getStackTag(int contentId) {
        return String.format(TAG + ":StateManager%d", contentId);
    }

    private StatesManager statesManager;
    private DialogActions dialogActions;
    private FragmentActivity bindActivity;

    public FragmentManagers(FragmentActivity bindActivity) {
        this.bindActivity = bindActivity;
    }

    public ListStatesContainer<String, Object> getRetainStates() {
        return statesManager.getRetainStates();
    }

    public void onActivityCreate(Bundle bundle) {
        this.dialogActions = (DialogActions) getFM().findFragmentByTag(TAG_DIALOG_MANAGER);
        if (this.dialogActions == null) {
            this.dialogActions = new DialogActions();
            getFM().beginTransaction().add(this.dialogActions, TAG_DIALOG_MANAGER).commit();
            getFM().executePendingTransactions();
        }
        statesManager = (StatesManager) (getFM().findFragmentByTag(TAG_STATE_MANAGER));
        if (statesManager == null) {
            statesManager = new StatesManager();
            getFM().beginTransaction().add(statesManager, TAG_STATE_MANAGER).commit();
            getFM().executePendingTransactions();
        }
    }

    public void onDestroy() {
        if (bindActivity.isFinishing()) {
            statesManager.getRetainStates().clearByKeys();
        }
    }

    @Override
    public void onNegative(BaseDialog dialog) {
        dialogActions.onNegative(dialog);
    }

    @Override
    public void onNeutral(BaseDialog dialog) {
        dialogActions.onNeutral(dialog);
    }

    @Override
    public void onPositive(BaseDialog dialog) {
        dialogActions.onPositive(dialog);
    }

    @Override
    public void onDismiss(BaseDialog dialog) {
        dialogActions.onDismiss(dialog);
    }

    @Override
    public void onCancel(BaseDialog dialog) {
        dialogActions.onCancel(dialog);
    }

    @Override
    public void bindDialogListener(int dialogId, BaseDialog.Listener listener) {
        dialogActions.bindListener(dialogId, listener);
    }

    @Override
    public void unbindDialogListener(int dialogId) {
        dialogActions.unbindListener(dialogId);
    }

    @Override
    public void dismissDialog(final int dialogId) {
        boolean dismissImmediate = dismissDialogInternal(dialogId);
        if (!dismissImmediate) {
            // dismissDialog()调用时, showDialog()方法的FragmentTransaction有可能还未被执行，
            // 通过提交一个新的FragmentTransaction, 确保Dialog能正常dismiss
            getFM().beginTransaction()
                    .runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            dismissDialogInternal(dialogId);
                        }
                    }).commitAllowingStateLoss();
        }
    }

    private boolean dismissDialogInternal(int dialogId) {
        BaseDialog dialog = findShowingDialog(dialogId);
        if (dialog != null) {
            dialog.dismissAllowingStateLoss();
            return true;
        }
        return false;
    }

    @Override
    public void showDialog(BaseDialog.Builder builder) {
        BaseDialog dialog = findShowingDialog(builder.getDialogId());
        if (dialog != null) {
            dialog.updateState(builder);
        } else {
            dialog = builder.build();
            FragmentTransaction fm = getFM().beginTransaction();
            String tag = getDialogTag(builder.getDialogId());
            fm.add(dialog, tag);
            fm.commitAllowingStateLoss();
        }
    }

    private BaseDialog findShowingDialog(int dialogId) {
        Fragment fragment = getFM().findFragmentByTag(getDialogTag(dialogId));
        if (fragment != null && fragment instanceof BaseDialog) {
            return (BaseDialog) fragment;
        }
        return null;
    }

    private String getDialogTag(int dialogId) {
        return "Dialog:" + dialogId;
    }

    private FragmentManager getFM() {
        return bindActivity.getSupportFragmentManager();
    }

}
