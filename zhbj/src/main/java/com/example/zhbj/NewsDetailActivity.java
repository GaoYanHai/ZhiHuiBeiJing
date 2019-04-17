package com.example.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;

import java.lang.annotation.Annotation;

//import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.ll_controll)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_textSize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.btn_share)
    private ImageButton btnShare;
    @ViewInject(R.id.btn_menu)
    private ImageButton btnMenu;
    @ViewInject(R.id.wv_news_detail)
    private WebView mWenView;
    @ViewInject(R.id.pb_loding)
    private ProgressBar pbLoding;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);


        ViewUtils.inject(this);
        llControl.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.INVISIBLE);

        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        mUrl = getIntent().getStringExtra("url");

        mWenView.loadUrl(mUrl);
        WebSettings settings = mWenView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWenView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoding.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoding.setVisibility(View.GONE);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载连接时强制在webview中进行
                return true;
            }
        });

//        mWenView.goBack();   拦截返回键  跳到上一个页面
//        mWenView.goForward();  跳到下一个页面

        pbLoding.setMax(100);
        mWenView.setWebChromeClient(new WebChromeClient() {
            //进度发生变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pbLoding.setProgress(newProgress);
            }

            //显示标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                //showShare();
                break;
            case R.id.btn_textSize:
                //修改字体大小方法
                showChooseDialog();
                break;
        }

    }

    private String TAG = "aaaaa";
    private int mTempWhich;
    private int mCurrentSize = 2;

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        String[] items = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

        builder.setSingleChoiceItems(items, mCurrentSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTempWhich = i;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WebSettings settings = mWenView.getSettings();
                switch (mTempWhich) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentSize = mTempWhich;
                Log.i(TAG, "onClick: " + i);
            }
        });

//        builder.setPositiveButton("取消",null);
        builder.show();
    }


//    private void showShare() {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // title标题，微信、QQ和QQ空间等平台使用
//        oks.setTitle("分享");
//        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//       // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
//        // 启动分享GUI
//        oks.show(this);
//    }
}
