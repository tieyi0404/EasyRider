package com.easy.freerider.model;

import org.json.JSONObject;

import com.easy.freerider.util.FormatTimeMethod;

/*
 * Guest release way information
 * 
 * expectGoTime should be formatted as "yyyy-MM-dd HH"
 * 
 */
public class GuestRoute {
	private String guestWayId;
	private String guestPhone;
	private String sourceAddr;
	private String destAddr;
	private String expectGoTime;
	private String additionalInfo;
	private String pubTime;
	
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public String getGuestWayId() {
		return guestWayId;
	}
	public void setGuestWayId(String guestWayId) {
		this.guestWayId = guestWayId;
	}
	public String getGuestPhone() {
		return guestPhone;
	}
	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}
	public String getSourceAddr() {
		return sourceAddr;
	}
	public void setSourceAddr(String sourceAddr) {
		this.sourceAddr = sourceAddr;
	}
	public String getDestAddr() {
		return destAddr;
	}
	public void setDestAddr(String destAddr) {
		this.destAddr = destAddr;
	}
	public String getExpectGoTime() {
		return expectGoTime;
	}
	public void setExpectGoTime(String expectGoTime) {
		this.expectGoTime = expectGoTime;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public static GuestRoute parse(JSONObject obj){
		GuestRoute guestRoute =null;
		try{
			if(obj!=null){
				guestRoute = new GuestRoute();
				if(!(obj.getString("GuestWayId").equals(""))){
					guestRoute.setGuestWayId(obj.getString("GuestWayId"));
				}
				if(!(obj.getString("GuestPhone").equals(""))) {
					guestRoute.setGuestPhone(obj.getString("GuestPhone"));
				}

				if(!(obj.getString("SourceAddr").equals(""))) {
					guestRoute.setSourceAddr(obj.getString("SourceAddr"));
				}

				if(!(obj.getString("DestAddr").equals(""))) {
					guestRoute.setDestAddr(obj.getString("DestAddr"));	
				}

				if(!(obj.getString("ExpectGoTime").equals(""))) {
					guestRoute.setExpectGoTime(FormatTimeMethod.formateTime(obj.getString("ExpectGoTime")));
				}

				if(!(obj.getString("PubTime").equals(""))) {
					guestRoute.setPubTime(obj.getString("PubTime"));	
				}

				if(!(obj.getString("AdditionalInfo").equals(""))) {
					guestRoute.setAdditionalInfo(obj.getString("AdditionalInfo"));	
				}
			}			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return guestRoute;
	}

}
