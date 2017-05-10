/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.jfinal.weixin.demo;

import com.jfinal.kit.HashKit;
import com.jfinal.weixin.sdk.api.*;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.jfinal.weixin.util.Constant;
import com.jfinal.weixin.util.WeixinUtil;


public class WeixinApiController extends ApiController {
	
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		return WeixinUtil.getApiConfig();
	}

	/**
	 * 获取公众号菜单
	 */
	public void getMenu() {
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}

	/**
	 * 创建菜单
	 */
	public void createmenu() {
    	String path = Constant.getHost;
    	String jsonstr = "{" +
				"    \"button\": [" +
				"        {" +
				"            \"name\": \"鲜花订阅\"," +
				"            \"sub_button\": [" +
				"			 	{\"name\": \"多品鲜花订阅 | 59.99元\",\"type\": \"view\",\"url\": \"" + path + "/product/2\"}," +
				"			 	{\"name\": \"双品鲜花订阅 | 39.99元\",\"type\": \"view\",\"url\": \"" + path + "/product/1\"}," +
				"				{\"name\": \"我要送花| 69.99元\",\"type\": \"view\",\"url\": \"" + path + "/product/3\"}," +
				"				{\"name\": \"花边好物 | 花瓶 花剪\",\"type\": \"view\",\"url\": \"" + path + "/around\"}," +
				"				{\"name\": \"轻松赚花票\",\"type\": \"view\",\"url\": \"" + path + "/account/invitefri\"}]" +
				"        }," +
				"        {" +
                "            \"name\": \"小美秘密\"," +
				"            \"sub_button\": [" +
                "            	{\"name\": \"生活美学\",\"type\": \"view\",\"url\": \"" + path + "/esthetics\"}," +
                "				{\"name\": \"养护 | 搭配\",\"type\": \"view\",\"url\": \"" + path + "/knowledge\"}," +
                "				{\"name\": \"晒 晒 晒\",\"type\": \"view\",\"url\": \"http://www.webei.cn/1b434a4e7d\"}," +
				"           	{\"name\": \"我要带颜\",\"type\": \"view\",\"url\": \""+ path + "/account/invitefri\"}]" +
				"        }," +
				"        {" +
				"    		 \"name\": \"为您服务\"," +
				"    		 \"sub_button\": ["+
				"			 	{\"name\": \"会员中心\",\"type\": \"view\",\"url\": \"" + path + "/account/center\"}," +
				"			 	{\"name\": \"在线客服\",\"type\": \"click\",\"key\": \"32\"}," +
				"           	{\"name\": \"联系我们\",\"type\": \"view\",\"url\": \""+ path + "/contactus\"},"+
				"			 	{\"name\": \"物流查询\",\"type\": \"view\",\"url\": \"" + path + "/logistics_query\"}," +
				"			 	{\"name\": \"常见问题\",\"type\": \"view\",\"url\": \"" + path + "/question\"}]" +
				"        }" +
				"    ]" +
				"}";
    	ApiResult apiResult = MenuApi.createMenu(jsonstr);
    	renderText(apiResult.getJson());
    }

	/**
	 * 获取公众号关注用户
	 */
	public void getFollowers()
	{
		ApiResult apiResult = UserApi.getFollows();
		renderText(apiResult.getJson());
	}

	/**
	 * 获取用户信息
	 */
	public void getUserInfo()
	{
		ApiResult apiResult = UserApi.getUserInfo("ohbweuNYB_heu_buiBWZtwgi4xzU");
		renderText(apiResult.getJson());
	}

	/**
	 * 发送模板消息
	 */
	public void sendMsg()
	{
		String str = " {\n" +
				"           \"touser\":\"ohbweuNYB_heu_buiBWZtwgi4xzU\",\n" +
				"           \"template_id\":\"9SIa8ph1403NEM3qk3z9-go-p4kBMeh-HGepQZVdA7w\",\n" +
				"           \"url\":\"http://www.sina.com\",\n" +
				"           \"topcolor\":\"#FF0000\",\n" +
				"           \"data\":{\n" +
				"                   \"first\": {\n" +
				"                       \"value\":\"恭喜你购买成功！\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   },\n" +
				"                   \"keyword1\":{\n" +
				"                       \"value\":\"去哪儿网发的酒店红包（1个）\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   },\n" +
				"                   \"keyword2\":{\n" +
				"                       \"value\":\"1元\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   },\n" +
				"                   \"remark\":{\n" +
				"                       \"value\":\"欢迎再次购买！\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   }\n" +
				"           }\n" +
				"       }";
		ApiResult apiResult = TemplateMsgApi.send(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取参数二维码
	 */
	public void getQrcode()
	{
		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
		ApiResult apiResult = QrcodeApi.create(str);
		renderText(apiResult.getJson());

//        String str = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"123\"}}}";
//        ApiResult apiResult = QrcodeApi.create(str);
//        renderText(apiResult.getJson());
		
	}
	
	public void createTemporary(){
		/**
		 * {"ticket":"gQGq8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2xVTnBzUHJtRzlWaXFZeHJBRzI2AAIEnhWaVgMEIBwAAA==","expire_seconds":7200,"url":"http:\/\/weixin.qq.com\/q\/lUNpsPrmG9ViqYxrAG26"}
		 */
		ApiResult createTemporary = QrcodeApi.createTemporary(7200, 123);
		renderText(createTemporary.getJson());
	}
	
	public void getShowQrcodeUrl(){
		String url = QrcodeApi.getShowQrcodeUrl("gQGq8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2xVTnBzUHJtRzlWaXFZeHJBRzI2AAIEnhWaVgMEIBwAAA==");
		renderText(url);
	}

	/**
	 * 长链接转成短链接
	 */
	public void getShorturl()
	{
		String str = "http://wap.koudaitong.com/v2/showcase/goods?alias=128wi9shh&spm=h56083&redirect_count=1";
		ApiResult apiResult = ShorturlApi.getShortUrl(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取客服聊天记录
	 */
	public void getRecord()
	{
		String str = "{\n" +
				"    \"endtime\" : 987654321,\n" +
				"    \"pageindex\" : 1,\n" +
				"    \"pagesize\" : 10,\n" +
				"    \"starttime\" : 123456789\n" +
				" }";
		ApiResult apiResult = CustomServiceApi.getRecord(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取微信服务器IP地址
	 */
	public void getCallbackIp()
	{
		ApiResult apiResult = CallbackIpApi.getCallbackIp();
		renderText(apiResult.getJson());
	}
	/**
	 * 添加多客服
	 */
	public void addKfAccount(){
		ApiResult apiResult = CustomServiceApi.addKfAccount("javen@gh_8746b7fa293e", "javen", HashKit.md5("javen123"));
		renderText(apiResult.getJson());
	}
}

