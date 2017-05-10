package com.jfinal.weixin.util;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SendMsgUtil {
	public static int sendMsg(String number, HttpSession session) throws Exception {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
		
		NameValuePair Uid = new NameValuePair("Uid", "jsdod2");
		NameValuePair Key = new NameValuePair("Key", "7b6bb251a63aa5134982");
		NameValuePair smsMob = new NameValuePair("smsMob", number);
		String code = (String)session.getAttribute("bingcode");
		if(code == null){
			code = getCode();
		}
		NameValuePair smsText = new NameValuePair("smsText", "花美美用户绑定手机验证码：" + code);
		NameValuePair[] data ={ Uid, Key, smsMob, smsText };
		
		post.setRequestBody(data);
		client.executeMethod(post);
		
		String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
		if(Integer.parseInt(result) == 1){
			session.setAttribute("bindingcode", code);
			session.setMaxInactiveInterval(60 * 3);// 当前会话维持 60 * 3 秒
		}
		
		post.releaseConnection();
		
		return Integer.parseInt(result);
	}
	
	//生成6位随机数
	public static String getCode() {
		Random r = new Random();
		StringBuffer vc = new StringBuffer();
		for (int i = 0; i < 6; i++){
			String ch = String.valueOf(r.nextInt(10));
			vc.append(ch);
		}
		return vc.toString();
	};
}
