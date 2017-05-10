package com.jfinal.weixin.util;

import com.jfinal.kit.PropKit;

public class Constant {
	// 域名
    public static String getHost = PropKit.get("host");
    
    // 花籽类型
    public static enum seedType{// TODO point作用？？
 	   sign("签到", 1),
 	   register("注册", 2),
 	   binding("绑定手机号", 2),
 	   invite("邀请好友", 2),
 	   buy("付款成功", 2);
 	   
        public String name;
        public int point;

        private seedType(String name, int point) {
            this.name = name;
            this.point = point;
        }
    }
    
    // 用户同意授权，获取code
    public static String getCode = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    // 通过code换取网页授权access_token
    public static String getAccess_Token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 拉取用户信息(需scope为 snsapi_userinfo)
    public static String getSnsapi_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
}
