package com.easy.freerider.util;

import android.content.Context;

import com.easy.freerider.model.Server;


public class AppConfig {
	
	public class MsgWhat{
		public final static int MSG_CODE_RESULT = 1;//首页
		public final static int MSG_CODE_ERROR = 100;
	}
	
	public class ListDataState{
		public final static int LISTVIEW_DATA_MORE = 0x01;
		public final static int LISTVIEW_DATA_LOADING = 0x02;
		public final static int LISTVIEW_DATA_FULL = 0x03;
		public final static int LISTVIEW_DATA_EMPTY = 0x04;
	}
	
	public final static int PAGE_SIZE=5;
	
	public final static String TEMP_HOUSE_IMAGE="temp_house_image";
	
	public final static String JSON_KEY="info";
	/**
	 * 返回服务器地址
	 * 
	 * @param context
	 * @return
	 */
	public static String GetServerRootAddress(Context context) {
		Server server = XmlUtils.getServerFromResXml(context);
		return server.getIp();
	}
	
	/**
	 * 获取版本说明文件的地址（server.xml）
	 * @param context
	 * @return
	 */
	public static String GetVersionIP(Context context){
		Server server = XmlUtils.getServerFromResXml(context);
		return server.getVersionIp();
	}
	
	/**
	 * 获取版本说明文件的地址（server.xml）
	 * @param context
	 * @return port
	 */
	public static int GetServerPort(Context context){
		Server server = XmlUtils.getServerFromResXml(context);
		return server.getPort();
	}
}
