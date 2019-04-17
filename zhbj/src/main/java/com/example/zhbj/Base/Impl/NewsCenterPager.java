package com.example.zhbj.Base.Impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.Base.BasePager;
import com.example.zhbj.Base.Impl.menu.InteractMenuDetailPager;
import com.example.zhbj.Base.Impl.menu.NewsMenuDetailPager;
import com.example.zhbj.Base.Impl.menu.PhotosMenuDetailPager;
import com.example.zhbj.Base.Impl.menu.TopicMenuDetailPager;
import com.example.zhbj.Domain.NewsMenu;
import com.example.zhbj.Fragment.LeftMenuFragment;
import com.example.zhbj.Global.GlobalConstants;
import com.example.zhbj.MainActivity;
import com.example.zhbj.Utils.CacheUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;


public class NewsCenterPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> menuDetailPagers;//菜单详情页的集合
    private NewsMenu mNewsData;  //网络数据  分组名称

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    public void initData() {
//        TextView view = new TextView(mActivity);
//        view.setText("新闻");
//        view.setTextSize(22);
//        view.setTextColor(Color.RED);
//        view.setGravity(Gravity.CENTER);
        //修改标题
        tvTitle.setText("新闻中心");

//        flContent.addView(view);
        //显示按钮
        btMenu.setVisibility(View.VISIBLE);

        //判断有没有缓存
        String cache = CacheUtil.getCache(mActivity, GlobalConstants.CATEGORY_URL);
        if (!TextUtils.isEmpty(cache)) {
            //有缓存数据  不用请求数据直接解析
            processData(cache);
        } else {
            //请求数据
            getDataFromServer();
        }


    }

    public void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                //请求成功 写入缓存
                CacheUtil.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "网络链接失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //解析数据
    private void processData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsMenu.class);

        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        //给侧边栏设置数据
        leftMenuFragment.setMenuData(mNewsData.data);

        //初始化四个页面
        menuDetailPagers = new ArrayList<>();
        menuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        menuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        menuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhotos));
        menuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentDetailPager(0);


    }


    //设置菜单详情页
    public void setCurrentDetailPager(int i) {
        //重新给fragmentlayout添加内容
        BaseMenuDetailPager pager = menuDetailPagers.get(i);//获取当前应该显示的布局
        View view = pager.mRootView;//当前页面的布局
        //清除旧布局
        flContent.removeAllViews();
        flContent.addView(view);//给帧布局添加布局
        //初始化数据
        pager.initData();

        //更新标题
        tvTitle.setText(mNewsData.data.get(i).title);

        if (pager instanceof PhotosMenuDetailPager) {
            //显示切换按钮
            btnPhotos.setVisibility(View.VISIBLE);
        } else {
            //隐藏切换按钮
            btnPhotos.setVisibility(View.GONE);
        }

    }
}
