package com.easy.freerider.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatTimeMethod {
	
	public static String formateTime(String pretime) {
		String formate_time ="";
		try {
			Calendar cld = Calendar.getInstance();
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ); 
			Date date = sdf.parse(pretime+":00:00"); 
			cld.setTime(date);
			formate_time = cld.get(Calendar.YEAR) + "年" + (cld.get(Calendar.MONTH)+1) + "月" + cld.get(Calendar.DAY_OF_MONTH) + "日";			
			if ((date.getHours() >= 1) && (date.getHours() <= 6)) {
				formate_time = formate_time + " " + "凌晨" + (date.getHours() - 1)+ "点";
			} else if ((date.getHours() >= 7) && (date.getHours() <= 12)) {
				formate_time = formate_time + " " + "上午" + (date.getHours() - 1)+ "点";
			} else if (date.getHours() == 13) {
				formate_time = formate_time + " " + "中午" + (date.getHours() - 1) + "点";
			} else if ((date.getHours() >= 14) && (date.getHours() <= 19))  {
				formate_time = formate_time +  " " +"下午" + (date.getHours() - 13) + "点";
			} else {
				formate_time = formate_time +  " " +"晚上" +  (date.getHours() - 13)  + "点";
			}
		}catch (Exception e) {	
			e.printStackTrace();
		}
		return formate_time;
	}
	
	
	public static String formatTime_full(String pretime) {
		String formate_time ="";
		try {
			Calendar cld = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ; 
			Date date = format.parse(pretime); 
			cld.setTime(date);
			formate_time = cld.get(Calendar.YEAR) + "年" + (cld.get(Calendar.MONTH)+1) + "月" + cld.get(Calendar.DAY_OF_MONTH) + "日";	
			formate_time = formate_time + " " +date.getHours() + "时" + date.getMinutes() + "分" + date.getSeconds() + "秒";
		}catch (Exception e) {	
			e.printStackTrace();
		}
		return formate_time;
	}
	
}
