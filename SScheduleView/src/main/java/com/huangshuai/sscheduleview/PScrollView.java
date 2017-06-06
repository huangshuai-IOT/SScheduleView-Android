package com.huangshuai.sscheduleview;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by tangyijian on 2017/5/9.
 */

public class PScrollView extends ScrollView {
    int oX;
    private ScrollViewListener scrollViewListener = null;

    public PScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                oX= (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if(oX==0){
                    oX= (int) ev.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (scrollViewListener != null && oX!=0) {
                    int x= (int) ev.getX();
                    scrollViewListener.onScrollChanged(x, 0, oX, 0);
                }
                oX=0;
                break;
            default:oX=0;
        }
        return super.onTouchEvent(ev);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface ScrollViewListener {
        void onScrollChanged(int x, int y, int oldx, int oldy);

    }

}
