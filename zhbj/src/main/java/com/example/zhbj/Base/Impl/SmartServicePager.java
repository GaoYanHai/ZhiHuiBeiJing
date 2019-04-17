package com.example.zhbj.Base.Impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.Base.BasePager;

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    public void initData(){
        TextView view = new TextView(mActivity);
        view.setText("智慧北京");
        view.setTextSize(22);
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        tvTitle.setText("智慧服务");

        flContent.addView(view);

        btMenu.setVisibility(View.VISIBLE);
    }
}
