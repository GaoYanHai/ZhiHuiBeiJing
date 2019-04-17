package com.example.zhbj.Base.Impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.Domain.PhotosBean;
import com.example.zhbj.Global.GlobalConstants;
import com.example.zhbj.R;
import com.example.zhbj.Utils.CacheUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/*
 * 组图菜单详情页
 * */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{

    private ArrayList<PhotosBean.PhotoNews> mNewsList;
    private BitmapUtils bitmapUtils;

    public ImageButton btnPhotos;

    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhotos) {
        super(activity);
        btnPhotos.setOnClickListener(this);
        this.btnPhotos = btnPhotos;
    }

    @ViewInject(R.id.lv_photos)
    private ListView lvPhotos;
    @ViewInject(R.id.gv_photos)
    private GridView gvPhotos;

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    public void initData() {
        String cache = CacheUtil.getCache(mActivity, GlobalConstants.PHOTO_URL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                CacheUtil.setCache(GlobalConstants.PHOTO_URL, result, mActivity);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "组图网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

        mNewsList = photosBean.data.news;

        lvPhotos.setAdapter(new PhotoAdapter());
        gvPhotos.setAdapter(new PhotoAdapter());
    }

    private boolean isListView=true;  //标记当前是否是listview

    @Override
    public void onClick(View view) {
        if (isListView){
            lvPhotos.setVisibility(View.GONE);
            gvPhotos.setVisibility(View.VISIBLE);
            btnPhotos.setImageResource(R.mipmap.icon_pic_list_type);
            isListView=false;
        }else {
            lvPhotos.setVisibility(View.VISIBLE);
            gvPhotos.setVisibility(View.GONE);
            btnPhotos.setImageResource(R.mipmap.icon_pic_grid_type);
            isListView=true;
        }
    }

    class PhotoAdapter extends BaseAdapter {

        public PhotoAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int i) {
            return mNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder;
            if (view == null) {
                view = View.inflate(mActivity, R.layout.list_item_photos, null);
                holder = new ViewHolder();
                holder.ivPic = view.findViewById(R.id.iv_pic);
                holder.tvTitle = view.findViewById(R.id.tv_photos_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            PhotosBean.PhotoNews item = getItem(i);
            holder.tvTitle.setText(item.title);
            bitmapUtils.display(holder.ivPic, item.listimage);
            return view;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
    }


}
