package com.example.zhbj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.example.zhbj.Fragment.ContentFragment;
import com.example.zhbj.Fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity {

    private final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private final String TAG_MAIN = "TAG_MAIN";
    private SlidingMenu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉标题栏  在setContentView之前调用
        setContentView(R.layout.activity_main);


        menu = new SlidingMenu(this);

        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setMode(SlidingMenu.LEFT);

        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setShadowDrawable(R.color.colorAccent);
        //为侧滑菜单设置布局

        menu.setBehindWidth(400);//设置SlidingMenu菜单的宽度   要进行屏幕的适配否则可能会出现问题
        menu.setMenu(R.layout.activity_main_leftmenu);
//
//        WindowManager windowManager = getWindowManager();
//        int width = windowManager.getDefaultDisplay().getWidth();
//

        initFragment();
    }

    private void initFragment(){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();//开始事务
        beginTransaction.replace(R.id.fl_left_menu,new LeftMenuFragment(), TAG_LEFT_MENU);//用Fragment替换帧布局 参数1帧布局容器的id 参数2是要替换的Fragment 参数3 标记作用
        beginTransaction.replace(R.id.fl_main,new ContentFragment(),TAG_MAIN);

        beginTransaction.commit();//提交事务
    }

    //获取侧边栏对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragmentByTag = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragmentByTag;

    }

    //获取主页对象
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragmentByTag = (ContentFragment) fm.findFragmentByTag(TAG_MAIN);
        return fragmentByTag;

    }




    public void SlidingMenuNone(){
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    public void SlidingMenuFullscreen(){
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    public void SlidingMenuToggle(){
        menu.toggle();//侧边栏是打开状态则关闭 否则相反
    }



}
