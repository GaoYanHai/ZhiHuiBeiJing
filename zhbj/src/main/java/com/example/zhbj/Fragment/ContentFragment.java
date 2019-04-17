package com.example.zhbj.Fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.zhbj.Base.BasePager;
import com.example.zhbj.Base.Impl.GovAffairsPager;
import com.example.zhbj.Base.Impl.HomePager;
import com.example.zhbj.Base.Impl.NewsCenterPager;
import com.example.zhbj.Base.Impl.SettingPager;
import com.example.zhbj.Base.Impl.SmartServicePager;
import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.example.zhbj.View.NoScrollViewPager;

import java.util.ArrayList;

public class ContentFragment extends BaseFragment {

    private static final String TAG = "ContentFragment";
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers;
    private RadioGroup mRgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = view.findViewById(R.id.vp_content);
        mRgGroup = view.findViewById(R.id.rg_group);

        return view;

    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();

        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0);
                       // mViewPager.setCurrentItem(0,false); //参数2表示是否有滑动动画
                    break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4);
                        break;
                }
            }
        });
        //监听页面的切换 切换后在加载数据 不浪费性能
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                BasePager basePager = mPagers.get(position);
                basePager.initData();//加载对应页面的数据

                if (position==0 || position==mPagers.size()-1){
                    setSlidingMenuEnable(false);
                }else {
                    setSlidingMenuEnable(true); 
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //第一次要手动添加数据 因为只有切换才会调用方法 否则第一次是没有数据的
        mPagers.get(0).initData();
        setSlidingMenuEnable(false);
    }

    protected void setSlidingMenuEnable(boolean b) {
        MainActivity mainUI = (MainActivity) mActivity;
//        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (b){
//            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            mainUI.SlidingMenuFullscreen();
        }else {
//            Log.i(TAG, "setSlidingMenuEnable: 调用了");
//            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            mainUI.SlidingMenuNone();

        }

    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        //初始化界面
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);
            View view = basePager.mRootView;//获取当前页面的布局
            container.addView(view);
            basePager.initData();  //调用初始化数据
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            container.removeView((View) object);
        }
    }

    //获取新闻中心的页面
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager newsPager = (NewsCenterPager) mPagers.get(1);
        return newsPager;
    }
}
