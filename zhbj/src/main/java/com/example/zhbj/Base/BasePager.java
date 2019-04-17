package com.example.zhbj.Base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhbj.MainActivity;
import com.example.zhbj.R;


//五个标签类的基类
public class BasePager {
    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btMenu;
    public FrameLayout flContent;//空的帧布局 要动态添加
    public final View mRootView;//当前页面的布局文件对象
    public ImageButton btnPhotos;//组图切换按钮

    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    //初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = view.findViewById(R.id.tv_title);
        btMenu = view.findViewById(R.id.btn_menu);
        flContent = view.findViewById(R.id.fl_content);
        btnPhotos = view.findViewById(R.id.btn_photos);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        return view;
    }

    //打开或者关闭侧边栏
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        mainUI.SlidingMenuToggle();
    }


    public void initData() {

    }
}
