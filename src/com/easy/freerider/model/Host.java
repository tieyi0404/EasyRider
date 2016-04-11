package com.easy.freerider.model;


/*
 * Host personal information
 */

public class Host {
	private int hostId=0;
	private String hostPhone="";
	private String passwd="";
	private String userName = "未命名";
	private int gender = 0;
	private int driveAge = 0;
	private String carType="";
	private String carNumber="";
	private String carColor="";
	
	public int getHostId() {
		return hostId;
	}
	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	public String getHostPhone() {
		return hostPhone;
	}
	public void setHostPhone(String hostPhone) {
		this.hostPhone = hostPhone;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public String getNewPasswd() {
		return passwd;
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
	public int getDriveAge() {
		return driveAge;
	}
	public void setDriveAge(int driveAge) {
		this.driveAge = driveAge;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	
	public String getHostParamUrl()
	{
		StringBuilder strBuild = new StringBuilder();
		if(hostPhone != null)
		{
			strBuild.append("&HostPhone=").append(hostPhone);
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
		if(driveAge != 0)
		{
			strBuild.append("&DriveAge=").append(driveAge);
		}
		if(carType != null)
		{
			strBuild.append("&CarType=").append(carType);
		}
		if(carNumber != null)
		{
			strBuild.append("&CarNumber=").append(carNumber);
		}
		if(carColor != null)
		{
			strBuild.append("&CarColor=").append(carColor);
		}
		
		return strBuild.toString();
		
	}


}
