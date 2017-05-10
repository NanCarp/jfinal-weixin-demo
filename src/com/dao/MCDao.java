package com.dao;

import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class MCDao {
	//日期编码
		static String dateCode;
		//返回信息
		static Map<String, Object> responseMap;
		//反馈信息
		static String Msg;
		
		static int Id;
		
		static int typeId;
		
		static String fList;
		
		static String fName;
		
		static String sName;
		
		static Integer iId;
		
		static Integer fPid;
		
		static Map<String, Object> map;
		
		static String Copycode;
		
		static int Type;
		
		static String OrderCode;
		static String Aid;
		static int Pid;
		static Integer Vase;
		static String Area;
		static String Address;
		static int Reach;
		static int Cycle;
		static String Zhufu;
		static String Songhua;
		static String jh_List;
		static String jhColor_List;
		static int TType;
		static String Recommend;
		static Integer Szdx;
		static Integer Cash;
		static Integer Activity;
		static Double Yh;
		static Record Account;
		static Record pro_Flower;
		static int isBuy;
		static Record Order;
		static String Name;
		static String Tel;
		
		// 获取常见问题
		public static Page<Record> getQuestion(int pageno, int pagesize){
			return Db.paginate(pageno, pagesize, "select id,title,info", "from f_question order by id desc");
		}
}
