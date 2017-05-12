package com.jfinal.weixin.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.AccessToken;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CallbackIpApi;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.GroupsApi;
import com.jfinal.weixin.sdk.api.MessageApi;
import com.jfinal.weixin.sdk.api.QrcodeApi;
import com.jfinal.weixin.sdk.api.ShorturlApi;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import com.jfinal.weixin.sdk.api.TemplateData;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.jfinal.weixin.util.Constant;

public class TestController extends ApiController {
	private static ApiResult apiResult = null;
	//String openId = "oVOX00yKTd4dZAzVNbi7X4k3_Pdk";
	private static String openId = "oVOX00_xo-tSjfPE5ySJuUe7OywI";
	private Date date = new Date();
	private static String appId = PropKit.get("appId");
	private static String appSecret = PropKit.get("appSecret");
	private String redirect_uri = Constant.getHost + "/test/getAuthorizeURL";
	private static boolean snsapiBase = false;
	private String outMessage = "";
	
	@Override
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
        // 配置微信 API 相关常量
        ac.setToken(PropKit.get("token"));
        ac.setAppId(PropKit.get("appId"));
        ac.setAppSecret(PropKit.get("appSecret"));
        ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
        ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
        return ac;
	}

	// 测试页面
	public void index() {
		render("test.html");
	}
	
	// 获取接口调用凭据
	// 利用AccessTokenApi获取access token
	public void getAccessTokenStr(){
		String accessTokenStr = AccessTokenApi.getAccessTokenStr();
		renderText(accessTokenStr);
	}
	
	// 利用 CallbackIpApi 获取微信服务器 IP 地址
	public void getCallbackIp(){
		apiResult = CallbackIpApi.getCallbackIp();
		renderText(apiResult.toString());
	}
	
	// 客服接口-发消息CustomServiceApi
	public void customService(){
		apiResult = CustomServiceApi.sendText(openId, "text1");
		apiResult = CustomServiceApi.sendText(openId, "text2");
		apiResult = CustomServiceApi.sendImage(openId, "eIg_oqlG-qqRpE3XaRncgh32C59aHfhZcESUNPsX03AWSiVzuGsGEUBXtKO3A-7l");
		//apiResult = CustomServiceApi.sendVideo(openId, "eIg_oqlG-qqRpE3XaRncgh32C59aHfhZcESUNPsX03AWSiVzuGsGEUBXtKO3A-7l","title","description");
		renderText(apiResult.toString());
	}
	
	// 高级群发接口 MessageApi
	// 根据分组进行群发
	public void message() {
		// String media_id = "K5ah2PbnSDqu64aNrQhTJ1ONvqDvdhALqfp56jtEoJZ9VoFUbqnQ7MBJpyWFmudl";	// 素材ID  K5ah2PbnSDqu64aNrQhTJ1ONvqDvdhALqfp56jtEoJZ9VoFUbqnQ7MBJpyWFmudl
		String text = "群发消息 " + date.toString();
		String jsonStr = new String();
		jsonStr = "{" +
 				"	\"filter\":{\"is_to_all\":true},"+
 				"	\"text\":{\"content\":\"" + text + "\"},"+
 				"	\"msgtype\":\"text\"}";
		apiResult = MessageApi.sendAll(jsonStr);
		renderText(apiResult.toString());
	}
	
	// 根据OpenID列表群发
	public void message_send() {
		String text = "根据OpenID列表群发" + date.toString();
		String jsonStr = new String();
		
		renderText(apiResult.toString());
	}
	
	// 模板消息操作页面
	public void templateMsgUI(){
		
		
		render("templage_massage.html");
	}
	
	// 发送模板消息
	public void templateMsg() {
		apiResult = TemplateMsgApi.send(TemplateData.New()
				// 消息接收者
				.setTouser(openId)
				// 模板id
				.setTemplate_id("wpkda7JXBQJWi89RthfH8-x2IhBy8yWGMnaA-kmLytI")
				.setTopcolor("#743A3A")
				.setUrl("http://img2.3lian.com/2014/f5/158/d/86.jpg")

				// 模板参数
				.add("first", " 您好,欢迎使用模版消息!!\n", "#999")
				.add("keyword1", " 微信公众平台测试", "#999")
				.add("keyword2", " 39.8元", "#999")
				.add("keyword3", " yyyy年MM月dd日 HH时mm分ss秒", "#999")
				.add("remark", "\n您的订单已提交，我们将尽快发货，祝生活愉快! 点击可以查看详细信息。", "#999")
				.build());

		 System.out.println(apiResult.getJson());
		 renderText(apiResult.getJson());
	}
	
	public void groups () {
		
		apiResult = GroupsApi.get();
		JSONArray jsonArray = JSON.parseObject(apiResult.toString()).getJSONArray("groups");
		JSONObject jsonObject = null;
		List<Record> groups = new ArrayList<Record>();
		Record group = new Record();
		for(int i = 0; i < jsonArray.size(); i++){
			//System.out.println(jsonArray.get(i));
			jsonObject = (JSONObject) jsonArray.get(i);
			group.set("id", jsonObject.getInteger("id"));
			group.set("name", jsonObject.getString("name"));
			group.set("count", jsonObject.getInteger("count"));
			groups.add(group);
		}
		System.out.println(groups);
		setAttr("groups", groups);
		
		renderText(apiResult.toString());
	}
	
	// 创建分组
	public void groups_create () {
		//GroupsApi.create("new");
		GroupsApi.update(100, "新建分组");
		apiResult = GroupsApi.get();
		renderText(apiResult.toString());
	}
	
	// 删除分组
	public void groups_delete () {
		
		apiResult = GroupsApi.delete(100);
		renderText(apiResult.toString());
	}
	
	// 修改分组名
	public void groups_update(){
		
		GroupsApi.update(109, date.toString());
		apiResult = GroupsApi.get();
		renderText(apiResult.toString());
	}
	
	// 获取用户GroupID
	public void getGroupId(){
		GroupsApi.membersUpdate(openId, 0);
		renderText(GroupsApi.getId(openId).toString());
	}
	
	// 移动用户分组
	public void members_update(){
		//Random groupid = new Random();
		//GroupsApi.membersUpdate(openId, 1);
		GroupsApi.membersUpdate(openId, 109);
		apiResult = GroupsApi.getId(openId);
		renderText(apiResult.toString());
	}
	
	// 设置用户备注名
	public void updateRemark(){
		String remark = date.toString();
		apiResult = UserApi.updateRemark(openId, remark);
		renderText(apiResult.toString());
	}
	
	// 获取用户基本信息(UnionID机制)
	public void getUserInfo(){
		apiResult = UserApi.getUserInfo(openId);
		renderText(apiResult.toString());
	}
	
	// 获取用户列表
	public void getFollows(){
		apiResult = UserApi.getFollows();
		renderText(apiResult.toString());
	}
	
	// 网页授权获取用户基本信息
	public void getAuthorizeURL(){
		// 首先，构造跳转到微信授权的地址
		String code = SnsAccessTokenApi.getAuthorizeURL(appId, redirect_uri, snsapiBase);
		System.out.println(code);
		// 通过回调地址参数code获取access_token
		SnsAccessToken accessToken =SnsAccessTokenApi.getSnsAccessToken(appId, appSecret, code);
		String token = accessToken.getAccessToken();
		// 拉取用户信息(需scope为 snsapi_userinfo)
		apiResult =SnsApi.getUserInfo(token, openId);
		renderText(code.toString());
	}
	
	// 生成带参数的二维码QrcodeApi
	// 创建临时二维码
	public void createTemporary(){
		/*String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
        apiResult = QrcodeApi.create(str);*/
		int expireSeconds = 604800;
		int sceneId = 111;
		apiResult = QrcodeApi.createTemporary(expireSeconds, sceneId);
        JSONObject jsonObjec = JSONObject.parseObject(apiResult.getJson());
        String ticket = jsonObjec.getString("ticket");
        System.out.println(QrcodeApi.getShowQrcodeUrl(ticket));
        outMessage = "apiResult: " + apiResult.getJson() +"\n"
        		+ "QrcodeUrl: " + QrcodeApi.getShowQrcodeUrl(ticket);
        renderText(outMessage);
	}
	
	// 长链接转短链接接口ShorturlApi
	public void getShortUrl(){
		String longUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHJ8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyQ0JhdVJVa0JmUWoxMGVtdGhwMUgAAgSO2xNZAwSAOgkA";
        apiResult = ShorturlApi.getShortUrl(longUrl);
        JSONObject jsonObjec = JSONObject.parseObject(apiResult.getJson());
        String shorUrl = jsonObjec.getString("short_url");
		outMessage = "apiResult: " + apiResult.getJson() +"\n"
				+ "longUrl: " + longUrl + "\n\n"
        		+ "shorUrl: " + shorUrl;
        renderText(outMessage);
	}
}
