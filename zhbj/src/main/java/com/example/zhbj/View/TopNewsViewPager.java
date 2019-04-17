package com.example.zhbj.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
*新闻头条自定义ViewPager
*
* */

public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;

    public TopNewsViewPager(@NonNull Context context) {
        super(context);
    }

    public TopNewsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    //请求父控件不要拦截子控件的事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dy)<Math.abs(dx)){
                    int currentItem = getCurrentItem();
                    //左右滑动
                    if (dx>0){
                        //向右滑动
                        if (currentItem == 0 ){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //向左滑动
                        int count = getAdapter().getCount();
                        if (currentItem == count-1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                    //上下滑动  需要父控件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;


        }

        return super.dispatchTouchEvent(ev);
    }
}
