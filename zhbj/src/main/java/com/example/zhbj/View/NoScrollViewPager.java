package com.example.zhbj.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


//让viwpager 不要滑动
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写方法 以子类为准 让他什么都不做
        return true;
    }


    //点击事件的拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;  // 不拦截子类的事件
    }
}
