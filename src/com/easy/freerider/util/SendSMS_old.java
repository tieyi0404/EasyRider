package com.easy.freerider.util;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

/**
 * @author xiaoyuEr
 *
 */
public class SendSMS_old
{	
	private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";

	public static int generateVerifykey() {
		int verifyKey = (int) (Math.random()*9000+1000);
		return verifyKey;
	}
	
	//转换为unicode
	public static String encodeUnicode(final String gbString) {   
	        char[] utfBytes = gbString.toCharArray();   
	        String unicodeBytes = "";   
	        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {   
	            String hexB = Integer.toHexString(utfBytes[byteIndex]);   
	            if (hexB.length() <= 2) {   
	                hexB = "00" + hexB;   
	            }   
	            unicodeBytes = unicodeBytes + "\\u" + hexB;   
	        }   
	        return unicodeBytes;
	    }   

	public static boolean sendShortMsg(String mobile, int verifyKey) throws Exception
	{
		
		String shortMsgContent = "顺风车注册验证码 " + verifyKey + " 请于2分钟内正确输入";
		HashMap<String, String> smsAccount = new HashMap<String, String>();
		//smsAccount.put("action", "getBalance");
		smsAccount.put("action", "sendOnce");
		smsAccount.put("ac", "1001@501228840001");
		smsAccount.put("authkey", "006757004488604A6776C5E3746AFAF4");
		smsAccount.put("cgid", "52");
		smsAccount.put("c", java.net.URLEncoder.encode(shortMsgContent));
		smsAccount.put("m", mobile);
		String fullUrlString = new UrlBuild(smsAccount, sOpenUrl).GetFullUrl();
		
		System.out.println(fullUrlString);

		String reponseData = HttpClientHelper.httpPost(fullUrlString,"");
		
		//handle reponse
        SAXBuilder builder=new SAXBuilder(false);       
        Reader in= new StringReader(reponseData);
        Document doc=builder.build(in);
        int result = doc.getRootElement().getAttribute("result").getIntValue();
        System.out.println("result" + result);
        if(result == 1)
        {
        	return true;
        }
        else {
			return false;
		}
	}
}
