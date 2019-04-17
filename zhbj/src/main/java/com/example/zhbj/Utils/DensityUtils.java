package com.example.zhbj.Utils;

import android.content.Context;

public class DensityUtils {
    public static int dip2px(float dip, Context ctx){

        float density = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dip*density+0.5f);
        return px;
    }


    public static int px2dip(int px, Context ctx){

        float density = ctx.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return px;
    }
}
