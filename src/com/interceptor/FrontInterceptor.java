package com.interceptor;

import java.util.Map;

import com.dao.FCDao;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.util.Constant;

public class FrontInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		Record account = controller.getSessionAttr("account");
		if (account == null){
			String state = controller.getPara("state");
			if("openid".equalsIgnoreCase(state)){
				String code = controller.getPara("code");
				Map<String, Object> map = FCDao.getAccessToken(code);
				// 根据openId获取用户
				account = FCDao.setAccount(map.get("access_token").toString(), map.get("openid").toString(), null, null);
				controller.setSessionAttr("account", account);
				int state_user = Db.queryInt("select state from f_account where id=?", account.getInt("id"));
				if(state_user == 1) {
					controller.redirect("/freeze");
				}else{
					inv.invoke();
				}
			} else {// openid 不同，重新授权
				String code = Constant.getCode
						.replace("APPID", PropKit.get("appId"))
						.replace("REDIRECT_URI", Constant.getHost + controller.getRequest().getRequestURI())
						.replace("SCOPE", "snsapi_userinfo")
						.replace("STATE", "openid");
				controller.redirect(code);
			}
		}else{
			int state_user = Db.queryInt("select state from f_account where id=?", account.getInt("id"));
			if(state_user == 1){
				controller.redirect("/freeze");
			}else{
				inv.invoke();
			}
		}
	}
	

}
