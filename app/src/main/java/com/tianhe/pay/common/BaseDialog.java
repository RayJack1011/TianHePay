package com.tianhe.pay.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public abstract class BaseDialog extends DialogFragment {

    public interface ListenerProvider {
        Listener provideDialogListener();
    }

    public interface Listener {

        void onNegative(BaseDialog dialog);

        void onNeutral(BaseDialog dialog);

        void onPositive(BaseDialog dialog);

        void onDismiss(BaseDialog dialog);

        void onCancel(BaseDialog dialog);

    }

    public static class EmptyListener implements Listener {
        @Override
        public void onNegative(BaseDialog dialog) {
        }

        @Override
        public void onNeutral(BaseDialog dialog) {
        }

        @Override
        public void onPositive(BaseDialog dialog) {
        }

        @Override
        public void onDismiss(BaseDialog dialog) {
        }

        @Override
        public void onCancel(BaseDialog dialog) {
        }
    }

    public interface Builder {
        int getDialogId();

        BaseDialog build();
    }

    private static int idSequence = Integer.MIN_VALUE;

    public static int getAutoId() {
        int autoId = idSequence;
        idSequence += 1;
        return autoId;
    }

    private Listener listener;

    public BaseDialog() {
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof ListenerProvider) {
            listener = ((ListenerProvider) getActivity()).provideDialogListener();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (listener != null) {
            listener.onCancel(this);
        }
    }

    @Override
    public void dismiss() {
        if(listener != null) {
            listener.onDismiss(this);
        }
        super.dismiss();
//        super.dismissAllowingStateLoss();
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(cancelable);
        }
    }

    public Listener getListener() {
        return listener;
    }

    public abstract void updateState(Builder builder);

    public abstract int getDialogId();
}
