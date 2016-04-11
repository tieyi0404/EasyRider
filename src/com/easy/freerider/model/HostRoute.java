package com.easy.freerider.model;

import org.json.JSONObject;

import com.easy.freerider.util.FormatTimeMethod;


/*
 * Host Release route 
 * hostPhone or hostId?
 * 
 */

public class HostRoute {
	private String hostWayId;
	private String hostPhone;
	private String sourceAddr;
	private String destAddr;
	private String passAddr;
	private String goTime;
	private String pubTime;
	private int seatNumber;
	private String linePrice;
	private String additionalInfo;
	
	public String getHostWayId() {
		return hostWayId;
	}
	public void setHostWayId(String hostWayId) {
		this.hostWayId = hostWayId;
	}
	public String getHostPhone() {
		return hostPhone;
	}
	public void setHostPhone(String hostPhone) {
		this.hostPhone = hostPhone;
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
	public String getPassAddr() {
		return passAddr;
	}
	public void setPassAddr(String passAddr) {
		this.passAddr = passAddr;
	}
	public String getGoTime() {
		return goTime;
	}
	
	public String getPubTime() {
		return pubTime;
	}
	
	public void setGoTime(String goTime) {
		this.goTime = goTime;
	}
	
	public void SetPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	
	public int getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	public String getLinePrice() {
		return linePrice;
	}
	public void setLinePrice(String linePrice) {
		this.linePrice = linePrice;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public static HostRoute parsedetail(JSONObject obj){
		HostRoute hostRoute=null;
		try{
			if(obj!=null){
				hostRoute = new HostRoute();
				if(!(obj.getString("HostWayId").equals("")))
					hostRoute.setHostWayId(obj.getString("HostWayId"));
				if(obj.getString("SourceAddr") != null)
					hostRoute.setSourceAddr(obj.getString("SourceAddr"));
				if(obj.getString("HostPhone") != null)
					hostRoute.setHostPhone(obj.getString("HostPhone"));
				if(obj.getString("DestAddr") != null)
					hostRoute.setDestAddr(obj.getString("DestAddr"));
				if(obj.getString("PassAddr") != null)
					hostRoute.setPassAddr(obj.getString("PassAddr"));
				if(obj.getString("GoTime") != null)
					hostRoute.setGoTime(obj.getString("GoTime"));
				if(obj.getString("PubTime") != null)
					hostRoute.SetPubTime(obj.getString("PubTime"));
				if(obj.getInt("SeatNumber") != 0)
					hostRoute.setSeatNumber(obj.getInt("SeatNumber"));
				if(obj.getString("LinePrice") != null)
					hostRoute.setLinePrice(obj.getString("LinePrice"));
				if(obj.getString("AdditionalInfo") != null)
					hostRoute.setAdditionalInfo(obj.getString("AdditionalInfo"));	
			}			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return hostRoute;
	}
	
	public static HostRoute parse(JSONObject obj){
		HostRoute hostRoute=null;
		try{
			if(obj!=null){
				hostRoute = new HostRoute();
				if(!(obj.getString("HostWayId").equals(""))) {
					hostRoute.setHostWayId(obj.getString("HostWayId"));
				}
				if(obj.getString("SourceAddr") != null) {
					hostRoute.setSourceAddr(obj.getString("SourceAddr"));
				}
				
				if(obj.getString("DestAddr") != null) {
					hostRoute.setDestAddr(obj.getString("DestAddr"));
				}
				if(obj.getString("GoTime") != null) {
					hostRoute.setGoTime(FormatTimeMethod.formateTime(obj.getString("GoTime")));
				}
				if(obj.getString("PassAddr") != null) {
					hostRoute.setPassAddr(obj.getString("PassAddr"));
				}
			}			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return hostRoute;
	}
	

}
