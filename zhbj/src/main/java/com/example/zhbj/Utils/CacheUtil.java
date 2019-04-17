package com.example.zhbj.Utils;

import android.content.Context;

/*
* 网络缓存工具类
* 以Url为key json为value 保存在本地 使用sharePreference存储
* */
public class CacheUtil {

    public static void setCache(String url,String json,Context ctx){
        sharePreference.setString(ctx,url,json);
    }

    public static String getCache(Context ctx,String url){
        String string = sharePreference.getString(ctx, url, null);
        return string;
    }

}
