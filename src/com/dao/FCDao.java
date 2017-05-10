package com.dao;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;

import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.util.Constant;

/**
 * @Desc 后台公共数据接口
 * */
public class FCDao {
	static Map<String , Object> resultMap;	//创建订单结果
	static int Pid;
	static Integer Vase;
	static int addressId;
	static int Reach;
	static int Cycle;
	static String Zhufu;
	static String Songhua;
	static String jh_List;
	static String jhColor_List;
	static int Type;
	static String Recommend;
	static Integer Szdx;
	static Integer Cash;
	static Integer Activity;
	static Double Yh;
	static Record Account;
	static Record pro_Flower;
	static int isBuy;
	static Record Order;
	
	// 根据openId注册新用户
	public static Record setAccount(String access_token, String openId, String typeId, String eventUserId){
		Record account = Db.findFirst("select id,openid,nick,headimg,tel,recommend,isbuy,qrurl,state from f_account where openid = ?", openId);
		if(account == null) {
			ApiResult ar;
			account = new Record();
			account.set("openid", openId);
			if (access_token == null) {
				ar = UserApi.getUserInfo(openId);
			} else {
				String userinfo = HttpKit.get(Constant.getSnsapi_userinfo
												.replace("ACCESS_TOKEN", access_token)
												.replace("OPENID", openId));
				ar = new ApiResult(userinfo);
				account.set("isfans", 1);
			}
			try {
				account.set("nick", filterOffUtf8Mb4(ar.get("nickname").toString()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			account.set("headimg", ar.get("headimgurl"));
			account.set("isbuy", 0);
			account.set("ctime", new Date());
			account.set("state", 0);
			if(typeId!=null && eventUserId!=null){
				// TODO
			}
			boolean result = Db.save("f_account", account);
			if(result){
				// TODO
			}
		}
		
		return Account;
	}
	
	public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            }
            else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            }
            else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }
}

























