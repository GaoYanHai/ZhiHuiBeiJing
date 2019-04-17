package com.example.zhbj.Base.Impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.Domain.NewTabBean;
import com.example.zhbj.Domain.NewsMenu;
import com.example.zhbj.Global.GlobalConstants;
import com.example.zhbj.NewsDetailActivity;
import com.example.zhbj.R;
import com.example.zhbj.Utils.CacheUtil;
import com.example.zhbj.Utils.sharePreference;
import com.example.zhbj.View.PullToRefreshListView;
import com.example.zhbj.View.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;  // 单个页签的网络数据
    //private TextView textView;

    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.cpi_indicator)
    private CirclePageIndicator mIdicator;

    @ViewInject(R.id.lv_list)
    private PullToRefreshListView lvList;

    private final String mUrl;  // 详情的链接
    private ArrayList<NewTabBean.TopNews> mTopNews;
    private ArrayList<NewTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;

        mUrl = GlobalConstants.SERVER_URL + newsTabData.url;
    }

    @Override
    public View initView() {
//        textView = new TextView(mActivity);
//
//        textView.setTextSize(25);

        View view = View.inflate(mActivity,R.layout.pager_tab_detail, null);
        ViewUtils.inject(this,view);

        //添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        ViewUtils.inject(this,mHeaderView);
        lvList.addHeaderView(mHeaderView);
        /*
        * 设置回调方法  前端界面设置回调
        * */
        lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //执行刷新列表的操作 再次从服务器拿数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl!=null){
                    getMoreDataFromServer();
                }else {
                    Toast.makeText(mActivity, "没有更多了", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(true);//没有数据也要隐藏起来
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int headerViewsCount = lvList.getHeaderViewsCount();
                i = i - headerViewsCount;   // 实际上被点击的item是减去头布局的数量
                NewTabBean.NewsData newsData = mNewsList.get(i);
                String read_ids = sharePreference.getString(mActivity, "read_ids", "");

                if (!read_ids.contains(newsData.id+"")){
                    read_ids = read_ids+newsData.id+",";
                    sharePreference.setString(mActivity,"read_ids",read_ids);
                }
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setTextColor(Color.GRAY);

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",newsData.url);
                mActivity.startActivity(intent);

            }
        });

        return view;
    }


    @Override
    public void initData() {
//        textView.setText(mTabData.title);
        String cache = CacheUtil.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)){
         processData(cache,false);
        }
        getDataFromServer();


    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtil.setCache(mUrl,result,mActivity);   //设置缓存数据
                processData(result,false);

                //收起控件
                lvList.onRefreshComplete(true);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "网络链接失败", Toast.LENGTH_SHORT).show();
                //收起控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    /*
     * 加载下一页数据
     * */
    private void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;

                processData(result,true);
//
//                //收起控件
                lvList.onRefreshComplete(true);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "加载下一页数据失败", Toast.LENGTH_SHORT).show();
                //收起控件
                lvList.onRefreshComplete(false);
            }
        });
    }


    private void processData(String result,boolean isMore) {
        Gson gson = new Gson();
        NewTabBean newTabBean = gson.fromJson(result, NewTabBean.class);
        String more = newTabBean.data.more;
        if (!TextUtils.isEmpty(more)){
            mMoreUrl = GlobalConstants.SERVER_URL + more;
        }else {
            mMoreUrl=null;
        }
        if (!isMore){
            //头条新闻填充数据
            mTopNews = newTabBean.data.topnews;
            if (mTopNews !=null){
                mViewPager.setAdapter(new TopNewsAdapter());

                mIdicator.setViewPager(mViewPager);
                mIdicator.setSnap(true);  //快照方式展示  不跟随手指移动

                //事件必须设置给mIdicator
                mIdicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        String title = mTopNews.get(position).title;
                        tvTitle.setText(title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //更新第一个标题
                tvTitle.setText(mTopNews.get(0).title);
                //默认第一个被选中 解决初始化时Idicator仍然保持上一次记录的bug
                mIdicator.onPageSelected(0);
            }

            mNewsList = newTabBean.data.news;
            if (mNewsList!=null){
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

            if (mHandler==null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem>mTopNews.size()-1){  //位置id大于总数量那么则强制让他归零从第一页开始跳
                            currentItem=0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        //循环发送消息
                        mHandler.sendEmptyMessageDelayed(0,3000);//延时3秒钟
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);//延时3秒钟
            }
//                mHandler.sendEmptyMessageDelayed(0,3000);//延时3秒钟

            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mHandler.removeCallbacksAndMessages(null);  //移除所有handler发送的的消息
                        break;
                        case MotionEvent.ACTION_POINTER_UP:
                            mHandler.sendEmptyMessageDelayed(0,3000);
                            break;
                    }
                    return false;
                }
            });



        }else {
            //加载更多数据

            ArrayList<NewTabBean.NewsData> moreNews = newTabBean.data.news;
            mNewsList.addAll(moreNews);//将数据追加在原来集合的后面
            mNewsAdapter.notifyDataSetChanged();//数据刷新
        }


    }


    //头条新闻的数据适配
    class TopNewsAdapter extends PagerAdapter{

        private final BitmapUtils mBitmapUtils;

        public TopNewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.topnews_item_default);//设置加载中的默认图片
        }


        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            //imageView.setImageResource(R.mipmap.topnews_item_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); //缩放模式 在不能设置背景时用缩放铺满父窗体

            String imageUrl = mTopNews.get(position).topimage;

            mBitmapUtils.display(imageView,imageUrl);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter{


        private final BitmapUtils bitmapUtils;

        public NewsAdapter(){
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);

        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int i) {
            return mNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHold viewHold;
            if (view == null){
                view = View.inflate(mActivity,R.layout.list_item_news,null);
                viewHold=new ViewHold();
                viewHold.ivIcon=view.findViewById(R.id.iv_icon);
                viewHold.tvTitle=view.findViewById(R.id.tv_title);
                viewHold.tvDate=view.findViewById(R.id.tv_date);
                view.setTag(viewHold);
            }else {
                viewHold = (ViewHold) view.getTag();
            }

            NewTabBean.NewsData news = (NewTabBean.NewsData) getItem(i);

            viewHold.tvTitle.setText(news.title);
            viewHold.tvDate.setText(news.pubdate);
            bitmapUtils.display(viewHold.ivIcon,news.listimage);
            String read_ids = sharePreference.getString(mActivity, "read_ids", "");
            if (read_ids.contains(news.id+"")){
                viewHold.tvTitle.setTextColor(Color.GRAY);
            }else {
                viewHold.tvTitle.setTextColor(Color.BLACK);
            }

            return view;
        }
    }

        static class ViewHold{
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;

    }

}
