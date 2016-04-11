package com.easy.freerider.util;

/*
 * 从 R.xml.server 中读取gateserver ip/port
 * */

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.easy.freerider.R;
import com.easy.freerider.model.Server;

public class XmlUtils {
	
	public static Server getServerFromResXml(Context context){
		Server s = new Server();
		XmlPullParser xpp = context.getResources().getXml(R.xml.server);
	    try {  
	        //开始解析事件  
	        int eventType = xpp.getEventType();  
	      
	        //处理事件，不碰到文档结束就一直处理  
	        while (eventType != XmlPullParser.END_DOCUMENT) {   
	            //因为定义了一堆静态常量，所以这里可以用switch  
	            switch (eventType) {  
	                case XmlPullParser.START_DOCUMENT:  
	                    // 不做任何操作或初开始化数据  
	                    break;  
	      
	                case XmlPullParser.START_TAG:  
	                    // 解析XML节点数据  
	                    // 获取当前标签名字  
	                    String tagName = xpp.getName();  
	                    if(tagName.equals("item")){  
	      
	                        // 通过getAttributeValue 和 netxText解析节点的属性值和节点值  
	                    	s.setIp(xpp.nextText());
	                    	xpp.next();
	                    	s.setPort(Integer.parseInt(xpp.nextText()));
	                    	xpp.next();
	                    	s.setVersionIp(xpp.nextText());
	      
	                    }  
	                    break;  
	      
	                case XmlPullParser.END_TAG:  
	                    // 单节点完成，可往集合里边添加新的数据  
	                    break;  
	                case XmlPullParser.END_DOCUMENT:  
	      
	                    break;  
	            }  
	      
	            // 别忘了用next方法处理下一个事件，不然就会死循环  
	            eventType = xpp.next();  
	        }  
	    } catch (XmlPullParserException e) {  
	        e.printStackTrace();  
	    }catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    
	    return s;
	}
	
//	public static String getHomepageFromResXml(Context context){
//		String homepage = null;
//		XmlPullParser xpp = context.getResources().getXml(R.xml.homepage);
//	    try {  
//	        //开始解析事件  
//	        int eventType = xpp.getEventType();  
//	      
//	        //处理事件，不碰到文档结束就一直处理  
//	        while (eventType != XmlPullParser.END_DOCUMENT) {   
//	            //因为定义了一堆静态常量，所以这里可以用switch  
//	            switch (eventType) {  
//	                case XmlPullParser.START_DOCUMENT:  
//	                    // 不做任何操作或初开始化数据  
//	                    break;  
//	      
//	                case XmlPullParser.START_TAG:  
//	                    // 解析XML节点数据  
//	                    // 获取当前标签名字  
//	                    String tagName = xpp.getName();  
//	                    if(tagName.equals("item")){  
//	      
//	                        // 通过getAttributeValue 和 netxText解析节点的属性值和节点值  
//	                    	homepage = xpp.nextText();
//	      
//	                    }  
//	                    break;  
//	      
//	                case XmlPullParser.END_TAG:  
//	                    // 单节点完成，可往集合里边添加新的数据  
//	                    break;  
//	                case XmlPullParser.END_DOCUMENT:  
//	      
//	                    break;  
//	            }  
//	      
//	            // 别忘了用next方法处理下一个事件，不然就会死循环  
//	            eventType = xpp.next();  
//	        }  
//	    } catch (XmlPullParserException e) {  
//	        e.printStackTrace();  
//	    }catch (IOException e) {  
//	        e.printStackTrace();  
//	    }  
//	    
//	    return homepage;
//	}

}
