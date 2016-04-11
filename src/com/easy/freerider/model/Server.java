
/*
 * 代表 gate server ip/port
 * */


package com.easy.freerider.model;


public class Server {
	
	
	private String ip = null;
	private int port = 0;
	private String versionIp=null;
	public String getVersionIp() {
		return versionIp;
	}
	public void setVersionIp(String versionIp) {
		this.versionIp = versionIp;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	

}
