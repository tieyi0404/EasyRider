package com.easy.freerider.model;


/*
 * guest personal information
 */

public class Guest {
	private int guestId = 0;
	private String guestPhone= "";
	private String passwd = "";
	private String userName = "未命名";
	private int gender=0;
	
	
	public int getGuestId() {
		return guestId;
	}
	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}
	public String getGuestPhone() {
		return guestPhone;
	}
	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public String getGuestParamUrl()
	{
		StringBuilder strBuild = new StringBuilder();
		if(guestPhone != null)
		{
			strBuild.append("&GuestPhone=").append(guestPhone);
		}
		if(passwd != null)
		{
			strBuild.append("&Passwd=").append(passwd);
		}
		if(userName != null)
		{
			strBuild.append("&UserName=").append(userName);
		}
		if(gender != 0)
		{
			strBuild.append("&UserSex=").append(gender);
		}
		
		return strBuild.toString();
		
	}
			
}
