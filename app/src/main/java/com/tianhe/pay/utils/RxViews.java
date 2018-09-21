package com.tianhe.pay.utils;

import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxViews {
    private static final int TAG_ATTACHED_SUBSCRIPTIONS = 0x7f100038;

    private static AttachedSubscriptions ensureAttachedSubscriptions(View view) {
        AttachedSubscriptions attachedSubs = (AttachedSubscriptions) view.getTag(TAG_ATTACHED_SUBSCRIPTIONS);
        if (attachedSubs == null) {
            attachedSubs = new AttachedSubscriptions();
            view.setTag(TAG_ATTACHED_SUBSCRIPTIONS, attachedSubs);
            view.addOnAttachStateChangeListener(attachedSubs);
        }
        return attachedSubs;
    }

    public static void unsubscribeOnDetach(View view, Disposable disposable) {
        AttachedSubscriptions attachedSubs = ensureAttachedSubscriptions(view);
        attachedSubs.subs.add(disposable);
    }

    private static class AttachedSubscriptions implements View.OnAttachStateChangeListener {
        CompositeDisposable subs = new CompositeDisposable();
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            subs.clear();
        }
    }
}
