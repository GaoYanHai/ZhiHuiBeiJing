package com.example.zhbj.Base;

import android.app.Activity;
import android.view.View;

/*
*  菜单详情页的基类
* */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public final View mRootView;  //菜单详情页的根布局

    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView = initView();

    }

    //让子类去实现
    public abstract View initView();


    public void initData(){

    }
}
