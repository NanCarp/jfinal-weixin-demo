package com.jfinal.weixin.demo;

import com.jfinal.json.Jackson;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.MessageApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;

public class TestController extends ApiController {
	ApiResult apiResult = null;

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

	public void customService(){
		String openId = "oVOX00yKTd4dZAzVNbi7X4k3_Pdk";
		apiResult = CustomServiceApi.sendText(openId, "text");
		//apiResult = CustomServiceApi.sendImage(openId, "");
		renderText(apiResult.toString());
	}
	
	public void message() {
		String media_id = "3Rh54wxNnhrpM6aPIdv9cWKQmmJr_3TBmU-vUheA_mjf8_5cD2L-dLCl3op5ohgX";	// 素材ID  ABmIJqX6-ZFJ4BE4zjZ0H1cYbi5HiAk_YsxyAzVpkwY
		String jsonStr = new String();
		jsonStr = "{" +
 				"	\"filter\":{\"is_to_all\":true},"+
 				"	\"mpnews\":{\"media_id\":\"" + media_id + "\"},"+
 				"	\"msgtype\":\"mpnews\"}";
		apiResult = MessageApi.sendAll(jsonStr);
		renderText(apiResult.toString());
	}
}
