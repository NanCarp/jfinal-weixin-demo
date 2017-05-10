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
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.GroupsApi;
import com.jfinal.weixin.sdk.api.MessageApi;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;

public class TestController extends ApiController {
	ApiResult apiResult = null;
	String openId = "oVOX00yKTd4dZAzVNbi7X4k3_Pdk";

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

	public void index() {
		render("test.html");
	}
	
	public void customService(){
		String openId = "oVOX00yKTd4dZAzVNbi7X4k3_Pdk";
		apiResult = CustomServiceApi.sendText(openId, "text1");
		apiResult = CustomServiceApi.sendText(openId, "text2");
		apiResult = CustomServiceApi.sendImage(openId, "eIg_oqlG-qqRpE3XaRncgh32C59aHfhZcESUNPsX03AWSiVzuGsGEUBXtKO3A-7l");
		//apiResult = CustomServiceApi.sendVideo(openId, "eIg_oqlG-qqRpE3XaRncgh32C59aHfhZcESUNPsX03AWSiVzuGsGEUBXtKO3A-7l","title","description");
		renderText(apiResult.toString());
	}
	
	public void message() {
		String media_id = "eIg_oqlG-qqRpE3XaRncgh32C59aHfhZcESUNPsX03AWSiVzuGsGEUBXtKO3A-7l";	// 素材ID  ABmIJqX6-ZFJ4BE4zjZ0H1cYbi5HiAk_YsxyAzVpkwY
		String jsonStr = new String();
		jsonStr = "{" +
 				"	\"filter\":{\"is_to_all\":true},"+
 				"	\"mpnews\":{\"media_id\":\"" + media_id + "\"},"+
 				"	\"msgtype\":\"mpnews\"}";
		apiResult = MessageApi.sendAll(jsonStr);
		renderText(apiResult.toString());
	}
	
	public void templateMsg() {
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
			renderText(apiResult.toString());
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
		Date date = new Date();
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
		apiResult = GroupsApi.getId(openId);
		renderText(GroupsApi.membersUpdate(openId, 1).toString());
	}
}
