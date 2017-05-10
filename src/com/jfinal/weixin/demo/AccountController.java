package com.jfinal.weixin.demo;

import com.dao.FCDao;
import com.interceptor.FrontInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.util.Constant.seedType;
import com.jfinal.weixin.util.SendMsgUtil;

@Before(FrontInterceptor.class)
public class AccountController extends Controller{
	// 会员中心
	public void center() {
		Record account_1 = getSessionAttr("account");
		Record account = Db.findFirst("select * from f_account where id=?",account_1.getInt("id"));
		setAttr("account", account);
		// 检测今日是否签到
		Number num = Db.queryNumber("select count(1) from f_flowerseed where aid=? and type=? and ctime=curdate()", account.getInt("id"), seedType.sign.name());
		setAttr("sign", num.intValue());// 是否签到
		setAttr("ordercount", FCDao.orderCount(account.getInt("id")));// 订单状态统计
		setAttr("yqhy", FCDao.getInviteFriend());// 获得邀请好友内容
		setAttr("cashcount", Db.queryLong("SELECT COUNT(id) FROM f_cash WHERE state=1 AND aid=? and CURDATE()>=time_a AND CURDATE()<=time_b", account.getInt("id")));
		render("center.html");
	}
	// 签到
	public void signin(){
		Record account = getSessionAttr("account");
		renderJson(FCDao.signin(account.getInt("id")));
	}
	// 绑定手机号
	public void binding(){
		render("binding.html");
	}
	// 获取验证码
	public void getBindingCode() throws Exception{
		String number = getPara("number");
		int result = SendMsgUtil.sendMsg(number, getSession());
		renderJson(result);
	}
	// 保存绑定号码
	public void saveBinding(){
		String number = getPara("number");
		String msgcode = getPara("msgcode");
		String bindingcode = getSessionAttr("bindingcode");
		renderJson(FCDao.saveBinding(number, msgcode, bindingcode, getSession()));
	}
}

























