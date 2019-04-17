package com.example.zhbj.Base.Impl.menu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.Base.BaseMenuDetailPager;

/*
* 专题菜单详情页
* */
public class TopicMenuDetailPager extends BaseMenuDetailPager{
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("3");
        textView.setTextSize(25);
        return textView;
    }


}
