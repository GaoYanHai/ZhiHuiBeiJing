package com.example.zhbj.Base.Impl.menu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.Base.BaseMenuDetailPager;

/*
* 互动菜单详情页
* */
public class InteractMenuDetailPager extends BaseMenuDetailPager{
    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("1");
        textView.setTextSize(25);
        return textView;
    }


}
