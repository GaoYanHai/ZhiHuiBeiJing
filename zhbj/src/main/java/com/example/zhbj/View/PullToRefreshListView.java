package com.example.zhbj.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private static final int STATA_PULL_TO_REFRESH = 1;
    private static final int STATA_RELEASE_TO_REFRESH = 2;
    private static final int STATA_REFRESHING = 3;
    private int mCurrentState = STATA_PULL_TO_REFRESH;   //当前状态  默认为刷新状态

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int startY = -1;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private RotateAnimation animUP;
    private RotateAnimation animDOWN;
    private ProgressBar pbProgress;
    private View mFooterView;
    private int mFooterHeight;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFootView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFootView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFootView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initHeaderView();
        initFootView();
    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        addHeaderView(mHeaderView);

        //隐藏头布局

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        tvTitle = mHeaderView.findViewById(R.id.tv_title);
        tvTime = mHeaderView.findViewById(R.id.tv_time);
        ivArrow = mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = mHeaderView.findViewById(R.id.pb_loding);

        initAnim();
        //设置时间
        setCurrentTime();

    }

    //初始化脚布局
    private void  initFootView(){
        mFooterView = View.inflate(getContext(),R.layout.pull_to_refresh_footer, null);

        addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0,-mFooterHeight,0,0);

        this.setOnScrollListener(this);  //滑动监听
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentState == STATA_REFRESHING) {
                    //正在刷新  就跳过
                    break;
                }

                if (startY == -1) {  //防止头布局吧事件消耗提升稳定性
                    startY = (int) ev.getY();
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;

                int firstVisiblePosition = getFirstVisiblePosition(); //获取当前iten的值
                if (dy > 0 && firstVisiblePosition == 0) {//下拉 并且是第一个
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0 && mCurrentState != STATA_RELEASE_TO_REFRESH) {
                        //松开刷新
                        mCurrentState = STATA_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATA_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = STATA_PULL_TO_REFRESH;
                        refreshState();
                    }


                    return true; //表示事件的消耗 不需要其他再处理
                }
                break;
            case MotionEvent.ACTION_UP:

                startY = -1;  //初始化值
                if (mCurrentState == STATA_RELEASE_TO_REFRESH) {
                    mCurrentState = STATA_REFRESHING;
                    refreshState();
                    mHeaderView.setPadding(0, 0, 0, 0);  //完整展示其头布局
                } else if (mCurrentState == STATA_PULL_TO_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏
                }


                break;

        }
        return super.onTouchEvent(ev);
    }

    /*
     * 初始化箭头动画
     * */
    private void initAnim() {
        animUP = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUP.setDuration(200);
        animUP.setFillAfter(true);


        animDOWN = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDOWN.setDuration(200);
        animDOWN.setFillAfter(true);


    }


    /*
     *
     * 根据当前状态刷新界面
     * */
    private void refreshState() {
        switch (mCurrentState) {
            case STATA_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.startAnimation(animDOWN);
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                break;
            case STATA_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.startAnimation(animUP);
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();  // 回调方法  刷新列表
                }

                break;
            case STATA_REFRESHING:
                tvTitle.setText("正在刷新");
                ivArrow.clearAnimation();  // 清除箭头的动画  否则无法隐藏
                pbProgress.setVisibility(VISIBLE);
                ivArrow.setVisibility(INVISIBLE);
                break;
        }

    }

    /*
     * 刷新结束 收起控件
     * */
    public void onRefreshComplete(boolean success) {
        if (!isLoadMore){
            if (success){
                //设置时间
                setCurrentTime();
            }
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATA_PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            pbProgress.setVisibility(INVISIBLE);
            ivArrow.setVisibility(VISIBLE);
        }else {
            //加载更多
            mFooterView.setPadding(0,-mFooterHeight,0,0);
            isLoadMore=false;
        }


    }

    private void setCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        tvTime.setText(time);

    }


    /*
     * 3 定义成员变量，接受监听对象
     * */
    private OnRefreshListener mListener;


    /*
     * 2 暴露其接口，设置监听
     * */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }
    /*
     * 1 回调方法的接口
     * */
    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();
    }

    private boolean isLoadMore;  //加载更多的标记  如果当前正在加载中那么不要再一次触发

    //滑动状态发生变化
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i==SCROLL_STATE_IDLE){//空闲状态
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition==getCount()-1&&!isLoadMore ){//滑动到列表的最后一个
                isLoadMore=true;
                mFooterView.setPadding(0,0,0,0);
                setSelection(getCount()-1);//将listview显示在最后一个item上  加载更多会直接显示出来

                //通知主界面加载下一页面
                if (mListener!=null){
                    mListener.onLoadMore();
                }
            }

        }
    }

    //滑动过程中
    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }


}
