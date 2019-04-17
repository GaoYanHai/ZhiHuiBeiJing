package com.example.zhbj.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.zhbj.Base.Impl.NewsCenterPager;
import com.example.zhbj.Domain.NewsMenu;
import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


//侧边栏
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lv_list;

    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;

    private int mCurrentPos;  //当前被选中的item对象
    private LeftMenuAdapter leftMenuAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);

        ViewUtils.inject(this,view);//注入view和事件

        return view;
    }

    @Override
    public void initData() {

    }

    //设置数据
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        mCurrentPos = 0;//当前选中的位置归零
        mNewsMenuData = data;

        leftMenuAdapter = new LeftMenuAdapter();
        lv_list.setAdapter(leftMenuAdapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentPos=i;
                leftMenuAdapter.notifyDataSetChanged();//刷新列表
                toggle();
                //点击侧边栏后 修改新闻中心的Fragmentlayout
                setCurrentDetailPager(i);


            }
        });
    }

    private void setCurrentDetailPager(int i) {
        MainActivity mainUI = (MainActivity) this.mActivity;
        ContentFragment contentFragment = mainUI.getContentFragment();
        //获取newsCenterPager
        NewsCenterPager newsCenterPager =  contentFragment.getNewsCenterPager();
        //修改FragmentLayout布局
        newsCenterPager.setCurrentDetailPager(i);
    }

    //打开或者关闭侧边栏
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        mainUI.SlidingMenuToggle();
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int i) {
            return mNewsMenuData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView tvMenu = view1.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(i);
            tvMenu.setText(item.title);

            if (i==mCurrentPos){
                tvMenu.setEnabled(true);   //选中文字变成红色
            }else {
                tvMenu.setEnabled(false);
            }

            return view1;
        }
    }
}
