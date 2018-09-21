package com.tianhe.pay.widget.padlock;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class AccessibilityUtils {
    public static void makeAnnouncement(View view, CharSequence var1) {
        if(view != null) {
            if(Build.VERSION.SDK_INT >= 16) {
                makeAnnouncementJellyBean(view, var1);
                return;
            }

            AccessibilityManager var2 = (AccessibilityManager)view.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
            if(var2.isEnabled()) {
                AccessibilityEvent var3 = AccessibilityEvent.obtain(64);
                var3.setSource(view);
                var3.setClassName(view.getClass().getName());
                var3.setPackageName(view.getContext().getPackageName());
                var3.setEnabled(view.isEnabled());
                var3.getText().add(var1);
                var2.sendAccessibilityEvent(var3);
                return;
            }
        }

    }

    @TargetApi(16)
    private static void makeAnnouncementJellyBean(View var0, CharSequence var1) {
        var0.announceForAccessibility(var1);
    }
}


