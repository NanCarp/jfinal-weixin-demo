package com.jfinal.weixin.util;

import com.jfinal.kit.PropKit;

public class Constant {
	// 域名
    public static String getHost = PropKit.get("host");
    
    // 拉取用户信息(需scope为 snsapi_userinfo)
    public static String getSnsapi_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
}
