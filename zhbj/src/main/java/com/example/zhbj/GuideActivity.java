package com.example.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zhbj.Utils.DensityUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llContainer;
    private ImageView redPoint;
    private int mPointDis;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);  去掉标题栏
        setContentView(R.layout.activity_guide);

        ViewPager mViewPager = findViewById(R.id.vp_guide);
        llContainer = findViewById(R.id.ll_container);
        redPoint = findViewById(R.id.iv_red_point);
        btn_start = findViewById(R.id.btn_start);
        initData();
        mViewPager.setAdapter(new GuideAdapter());

        //设置viewpager 的滑动监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动过程中

                //小红点要移动的距离
                int leftMargin = (int) (mPointDis * positionOffset) + position * mPointDis;

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
                params.leftMargin = leftMargin;
                //重新设置红点的布局参数
                redPoint.setLayoutParams(params);


            }

            @Override
            public void onPageSelected(int position) {
                //页面被选中
                if (position == mImageViewList.size() - 1) {
                    btn_start.setVisibility(View.VISIBLE);//显示按钮
                } else {
                    btn_start.setVisibility(View.INVISIBLE);//隐藏按钮
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态发生改变
            }
        });


        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //layout 方法执行完的回调方法

                //移除监听  防止重复调用
                redPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
        });


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击按钮 跳转到主页面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }

    private int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    private void initData() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);

            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = DensityUtils.dip2px(10, this);
            }

            llContainer.addView(point);

        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        //初始化item的布局
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);  //强制转换成view对象
        }
    }
}
