package com.tianhe.pay.ui.util;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    private GestureDetectorCompat gestureDetector;
    private RecyclerView touchView;
    private OnItemClickListener listener;

    public RecyclerItemClickListener(RecyclerView recyclerView) {
        this.touchView = recyclerView;
        this.gestureDetector = new GestureDetectorCompat(
                recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View child = touchView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null) {
                            performClick(touchView.getChildAdapterPosition(child) , child);
                            return true;
                        }
                        return super.onSingleTapUp(e);
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) {
            return true;
        }
        return false;
    }

    public void setItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void performClick(int position, View item) {
        if (listener != null) {
            listener.onItemClick(position, item);
        }
    }
}
