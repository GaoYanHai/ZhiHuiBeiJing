package com.example.zhbj.Domain;

import java.util.ArrayList;


/*
* 分类信息的封装
* 使用Gson解析时 对象书写技巧
* 1 遇到{} 大括号 创建对象
* 2遇到【】中括号 创建集合ArrayList
* 3所有的字段名称要和json返回的名称高度一致 类名可以自己起
*
* */

public class NewsMenu {
    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsMenuData> data;


    //侧边栏菜单的对象
    public class NewsMenuData{
        public int id;
        public String title;
        public int type;

        public ArrayList<NewsTabData> children;
    }

    //页签的对象
    public class NewsTabData{
        public int id;
        public String title;
        public int type;
        public String url;
    }
}
