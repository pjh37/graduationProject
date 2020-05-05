package com.example.graduationproject.service;

import android.view.View;
import android.content.Context;
import android.view.MotionEvent;

public class OnClickWithOnTouchListener implements View.OnTouchListener {
    public interface OnClickListener{
        void onClick();
    }
    /**
     * Max allowed duration for a "click", in milliseconds.
     */
    static final int MAX_CLICK_DURATION = 1000;
    /**
     * Max allowed distance to move during a "click", in DP.
     */
    static final int MAX_CLICK_DISTANCE = 15;

    private Context mContext;
    private OnClickListener mClickListener;

    private long pressStartTime;
    private float pressedX;
    private float pressedY;
    private boolean stayedWithinClickDistance;

    public OnClickWithOnTouchListener(Context context, OnClickListener clickListener) {
        this.mContext = context;
        this.mClickListener = clickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(mClickListener == null) {
            return false;
        }

        int eventAction = event.getAction();
        if(eventAction == MotionEvent.ACTION_DOWN) {
            pressStartTime = System.currentTimeMillis();
            pressedX = event.getX();
            pressedY = event.getY();
            stayedWithinClickDistance = true;
        } else if(eventAction == MotionEvent.ACTION_MOVE) {
            float distance = distance(pressedX, pressedY, event.getX(), event.getY());
            if (stayedWithinClickDistance && distance > MAX_CLICK_DISTANCE) {
                stayedWithinClickDistance = false;
            }
        } else if(eventAction == MotionEvent.ACTION_UP) {
            long pressDuration = System.currentTimeMillis() - pressStartTime;
            if (pressDuration < MAX_CLICK_DURATION && stayedWithinClickDistance) {
                mClickListener.onClick();
            }
        }

        return false;
    }

    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(mContext, distanceInPx);
    }

    public static float pxToDp(Context context, float px) {
        if(context == null) {
            return 0f;
        }

        return px / context.getResources().getDisplayMetrics().density;
    }

}
