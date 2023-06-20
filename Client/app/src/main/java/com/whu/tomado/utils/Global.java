/**
 * @author: 杜忠璠
 * @date: 2023/6/18
 * @brief: 全局变量
 */

package com.whu.tomado.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Global {
    public static long userID; //用户ID
    public static String userName="未登录";  //用户名
    public static boolean isLogin=false;  //是否登录


    public static long userUpLimit=1000000;  //用户上限
}
