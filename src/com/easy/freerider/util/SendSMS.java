package com.easy.freerider.util;

import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author xiaoyuEr
 *
 */
public class SendSMS
{	
	//中国短信
	//private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	
	//淘宝  https://item.taobao.com/item.htm?id=523002170612&mt=&wwlight=cntaobaotb%E4%BF%9D%E5%81%A5-%7B523002170612%7D
	private static String sOpenUrl = "http://115.238.169.140:8888/sms.aspx?action=send";

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
		
		String shortMsgContent = "【顺路】验证码为  " + verifyKey + ", 请您于2分钟内正确输入";
		
		StringBuilder fullUrlString1 = new StringBuilder(sOpenUrl);
		fullUrlString1.append("&userid=").append("284");
		fullUrlString1.append("&account=").append("yufei");
		fullUrlString1.append("&password=").append("a222222");
		fullUrlString1.append("&content=").append(java.net.URLEncoder.encode(shortMsgContent));
		fullUrlString1.append("&mobile=").append(mobile).append("&sendTime=&extno=");

		
		String reponseData = HttpClientHelper.httpPost(fullUrlString1.toString(),"");
		
		//handle reponse
        SAXBuilder builder=new SAXBuilder(false);       
        Reader in= new StringReader(reponseData);
        Document doc=builder.build(in);

        Element root = doc.getRootElement();//获得根节点
        Element result = root.getChild("returnstatus");
        
        if(result.getText().equals("Success"))
        {
        	System.out.println("sccess");
        	return true;
        }
        else {
			return false;
		}
	}
}
