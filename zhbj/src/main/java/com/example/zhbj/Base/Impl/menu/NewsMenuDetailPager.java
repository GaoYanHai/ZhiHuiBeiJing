package com.example.zhbj.Base.Impl.menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.Domain.NewsMenu;
import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/*
 * 新闻菜单详情页
 * */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ArrayList<NewsMenu.NewsTabData> mTabData;//页签网络数据

    private ArrayList<TabDetailPager> mPager;//页面标签页的集合


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData = children;
    }

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.tpi_indicator)
    private TabPageIndicator mIndicator;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        mPager = new ArrayList<>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
            mPager.add(pager);
        }

        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);   // 设置完数据以后再绑定 否则会报错

        //  mViewPager.setOnPageChangeListener(this);
        mIndicator.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUI = (MainActivity) this.mActivity;

        if (position == 0) {
            mainUI.SlidingMenuFullscreen();
        } else {
            mainUI.SlidingMenuNone();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        //设置指示器的标题
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData data = mTabData.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mPager.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager pager = mPager.get(position);
            View view = pager.mRootView;
            container.addView(view);

            pager.initData();

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    @OnClick(R.id.btn_next)
    public void nextPage(View view) {
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }


}
