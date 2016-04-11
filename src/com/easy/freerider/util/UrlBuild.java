package com.easy.freerider.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


/*  
 * http://127.0.0.1/AndroidService/android/upload?title=aaa&timelength=90的形式  
 */  

public class UrlBuild {
	private HashMap<String, String> pairMap;
	private String initUrl;
	
	public UrlBuild(HashMap<String, String> pairParm, String initUrl)
	{
		this.pairMap = pairParm;
		this.initUrl = initUrl;
	}
	
	public String GetFullUrl()
	{
		StringBuilder strBuild = new StringBuilder();
		Set<Entry<String, String>> sets = pairMap.entrySet(); 
		
		if(initUrl == null)
		{
			return "";
		}
		strBuild.append(initUrl).append('?');
		for(Entry<String, String> entry : sets) {  
			strBuild.append(entry.getKey()).append('=').append(entry.getValue()).append('&');  
		}  		
		strBuild.deleteCharAt(strBuild.length()-1);
		
		return strBuild.toString();
	}
}
