/**
 * Copyright (c) 2015-2016, Javen Zhou  (javenlife@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;

import com.dao.FCDao;
import com.interceptor.FrontInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author Javen
 * 2016年1月15日
 */
@Before(FrontInterceptor.class)
public class IndexController extends Controller {
	public void index(){
		setAttr("flower1", FCDao.getProductInfo(1));
		setAttr("flower2", FCDao.getProductInfo(2));
		Record account = getSessionAttr("account");
		int isbuy = account.getInt("isbuy");
		setAttr("aroundlist", FCDao.getAround(isbuy));	// 周边
		render("index.html");
	}
	
	public void product() {
		/*int pid = getParaToInt(0);
		int ptid = getParaToInt(1)==null?pid:getParaToInt(1);
		Record account = getSessionAttr("account");
		int isbuy = account.getInt("isbuy");
		setAttr("isbuy", isbuy);	// 判断是否第一次购物
		setAttr("activitylist", JsonKit.toJson(FCDao.getActivityList()));	//有效时间内的活动
		if(ptid==1 || ptid==2){
			Record pro = FCDao.getProductInfo(pid);
			String imgs = pro.getStr("imgurl");
			if(imgs.indexOf("-")!= -1){
				String[] ims = imgs.split("-");
				pro.set("imgurl1", ims[0]);
				pro.set("imgurl2", ims[1]);
				pro.set("imgurl3", ims[2]);
			}else{
				pro.set("imgurl1", imgs);
				pro.set("imgurl2", imgs);
				pro.set("imgurl3", imgs);
			}
			setAttr("flower", pro);	// 双品与多品
			
			setAttr("vaselist", FCDao.getProducts(4, isbuy));		// 花瓶
			setAttr("dmlj", FCDao.getDmlj());				// 多加一次，价格立减(双品)
			setAttr("dmlj2", FCDao.getDmlj2());				// 多加一次，价格立减(多品)
			render("product.html");
		}else if(ptid == 3){
			setAttr("givelist", FCDao.getProducts(3, isbuy));		// 送花系列
			setAttr("vaselist", FCDao.getProducts(4, isbuy));		// 花瓶
			setAttr("idoneitylist", Db.find("select id,title,imgurl from f_idoneity where state = 0"));	// 适赠对象
			setAttr("givetitle", Db.queryStr("SELECT code_value FROM f_dictionary WHERE code_key = 'give'"));
			render("product_give.html");
		}else{
			setAttr("flower", FCDao.getAroundInfo(pid));*/
			render("product_around.html");
	}
}
