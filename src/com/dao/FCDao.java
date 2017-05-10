package com.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.util.Constant;
import com.jfinal.weixin.util.Constant.seedType;

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
	
	
	// 花边好物列表
	public static List<Record> getAround(int isbuy){
		String typeStr = "";
		if(isbuy == 0){
			typeStr = " and b.type in (0,1)";
		}else{
			typeStr = " and b.type in (0,2)";
		}
		return Db.find("SELECT b.id,b.name,b.imgurl,b.describe,b.price FROM f_dictionary a LEFT JOIN f_flower_pro b ON a.code_value=b.ptid "
				+ "where b.state=0 AND a.code_value>=4" + typeStr);
	}
	// 获取商品详情
	public static Record getProductInfo(int ptid){
		Record pro = Db.findFirst("SELECT b.id,b.name,b.imgurl,b.itinfo1,b.itinfo2,b.itinfo3,b.itinfo4,b.itinfo5,"
				+ "b.describe,b.price FROM f_dictionary a LEFT JOIN f_flower_pro b ON a.code_value=b.ptid where a.code_value=?", ptid);
		return pro;
	}



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
				// TODO ??
				account.set("tjid", typeId+"_"+eventUserId);
			}
			boolean result = Db.save("f_account", account);
			if(result){// 保存成功
				// TODO
				account.set("id", account.getLong("id").intValue());
				// 新注册获得花籽
				for(int i=0;i<seedType.register.point;i++){
					Record seed = new Record();
					seed.set("aid", account.getInt("id"));
					seed.set("send", 1);
					seed.set("type", seedType.register.name());
					seed.set("remarks", seedType.register.name);
					seed.set("ctime", new Date());
					Db.save("f_flowerseed", seed);
				}
				// 新注册获取花票
				List<Record> cashlist = Db.find("SELECT a.ltime,b.id FROM f_cashtheme a LEFT JOIN f_cashclassify b ON a.id=b.tid WHERE a.id=1 and a.ltime>0 and b.state=0");
				for(Record cash : cashlist){
					Record c = new Record();
					c.set("aid", account.getInt("id"));
					c.set("cid", cash.getInt("id"));
					c.set("code", "1111");
					Calendar now = Calendar.getInstance();
					c.set("time_a", now.getTime());
					now.add(Calendar.DAY_OF_MONTH, cash.getInt("ltime"));// 有效期一个月
					c.set("time_b", now.getTime());
					c.set("state", 1);
					c.set("origin", 1);
					Db.save("f_cash", c);
				}
			}
		}
		
		return account;
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
	
	// 获取openId
	public static Map<String, Object> getAccessToken(String code) {
		String rUrl = Constant.getAccess_Token
				.replace("APPID", PropKit.get("appId"))
				.replace("SECRET", PropKit.get("appSecret"))
				.replace("CODE", code);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			URL url = new URL(rUrl);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = br.readLine()) != null){
				builder.append(line);
			}
			
			br.close();
			isr.close();
			is.close();
			
			ApiResult ar = new ApiResult(builder.toString());
			map.put("access_token", ar.get("access_token"));
			map.put("openid", ar.get("openid"));
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}

























