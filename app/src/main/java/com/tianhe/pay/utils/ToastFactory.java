package com.tianhe.pay.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public interface ToastFactory {
    Toast makeText(@StringRes int msgRes, int duration);

    Toast makeText(CharSequence msg, int duration);

    class Default implements ToastFactory {
        Toast toast;
        Context context;

        public Default(Context context) {
            this.context = context;
        }

        @Override
        public Toast makeText(@StringRes int msgRes, int duration) {
            if (toast == null) {
                toast = Toast.makeText(context, msgRes, duration);
            } else {
                toast.setText(msgRes);
                toast.setDuration(duration);
            }
            return toast;
        }

        @Override
        public Toast makeText(CharSequence msg, int duration) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, duration);
            } else {
                toast.setText(msg);
                toast.setDuration(duration);
            }
            return toast;
        }
    }
}
