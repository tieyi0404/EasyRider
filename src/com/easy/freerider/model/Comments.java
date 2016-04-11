package com.easy.freerider.model;

public class Comments {
	private String hostPhone;
	private String guestPhone;
	private float starRank;
	private String contents;
	public String getHostPhone() {
		return hostPhone;
	}
	public void setHostPhone(String hostPhone) {
		this.hostPhone = hostPhone;
	}
	public String getGuestPhone() {
		return guestPhone;
	}
	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}
	public float getStarRank() {
		return starRank;
	}
	public void setStarRank(float starRank) {
		this.starRank = starRank;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getCommentsToHostParamUrl()
	{
		StringBuilder strBuild = new StringBuilder();
		if(hostPhone != null)
		{
			strBuild.append("&HostPhone=").append(hostPhone);
		}
		if(guestPhone != null)
		{
			strBuild.append("&GuestPhone=").append(guestPhone);
		}
		if(starRank > 0)
		{
			strBuild.append("&StarRank=").append(starRank);
		}
		if(contents != null)
		{
			strBuild.append("&Contents=").append(contents);
		}
		
		return strBuild.toString();
		
	}
}
