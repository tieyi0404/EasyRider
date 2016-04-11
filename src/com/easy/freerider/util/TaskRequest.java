package com.easy.freerider.util;

import java.net.URLDecoder;

import org.json.JSONObject;

import android.content.Context;
/*
 * rootUrl: http://xxx.xxx.xxx.xxx
 * port: xxxx
 * 
 */
import android.os.Handler;

import com.easy.freerider.model.Comments;
import com.easy.freerider.model.Guest;
import com.easy.freerider.model.GuestRoute;
import com.easy.freerider.model.Host;
import com.easy.freerider.model.HostRoute;


public class TaskRequest {
	@SuppressWarnings("unused")
	private Context context;
	private Handler mHandler;
	private JSONObject postinfo; 
	private static String rootUrl;
	private static int port;
	private static String rootUrlHead;
	private AsyncHttpReqInterface asyncHttpReqInterface;
	public TaskRequest(Context context, Handler handler){
		this.context = context;
		this.mHandler = handler;
		rootUrl= AppConfig.GetServerRootAddress(context);
		port = AppConfig.GetServerPort(context);
		rootUrlHead = rootUrl + ":" + String.valueOf(port);
		asyncHttpReqInterface = new AsyncHttpReqInterface(context, mHandler);
		postinfo = new JSONObject();
	}

	/**
	 * 判断是否需要升级
	 * @param versionCode
	 * @return
	 * @throws Exception
	 */
	public void updateVersionReq(int versionCode) throws Exception{
		String reqName = "versionupdate";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=VersionUpdate&VersionCode=" + String.valueOf(versionCode));
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;
	}
	
	/**
	 * 注册车主
	 * @param hostInfo
	 * @return
	 * @throws Exception
	 */
	public void hostRegisterReq(Host hostInfo) throws Exception{
		String reqName = "hostregisterrequest";			
		postinfo.put("HostPhone", hostInfo.getHostPhone());
		postinfo.put("Passwd", hostInfo.getPasswd());
		postinfo.put("UserSex", hostInfo.getGender());
		postinfo.put("DriveAge", hostInfo.getDriveAge());
		postinfo.put("CarType", hostInfo.getCarType());
		postinfo.put("CarNumber", hostInfo.getCarNumber());
		postinfo.put("CarColor", hostInfo.getCarColor());
		postinfo.put("UserName", hostInfo.getUserName());
		postinfo.put("CommandId", "HostRegisterRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 注册乘客
	 * @param guestInfo
	 * @return
	 * @throws Exception
	 */
	public void guestRegisterReq(Guest guestInfo) throws Exception{
		String reqName = "guestregisterrequest";			
		postinfo.put("GuestPhone", guestInfo.getGuestPhone());
		postinfo.put("Passwd", guestInfo.getPasswd());
		postinfo.put("UserSex", guestInfo.getGender());
		postinfo.put("UserName", guestInfo.getUserName());
		postinfo.put("CommandId", "GuestRegisterResponse");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 查询车主
	 * @param telePhone
	 * @return
	 * @throws Exception
	 */
	public void getHostInfoRequest(String telePhone) throws Exception{
		String reqName = "hostinforqueryrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=QueryHostInfoRequest&HostPhone=" + telePhone);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;
	}
	
	/**
	 * 查询乘客
	 * @param telePhone
	 * @return
	 * @throws Exception
	 */
	public void getGuestInfoRequest(String telePhone) throws Exception{
		String reqName = "guestinforqueryrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=QueryGuestInfoRequest&GuestPhone=" + telePhone);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;
	}
	
	
	/**
	 * 车主更新个人信息
	 * @param Host
	 * @return
	 * @throws Exception
	 */
	public void hostInfoUpdateReq(Host hostInfo) throws Exception{
		String reqName = "hostinfoupdaterequest";			
		postinfo.put("HostPhone", hostInfo.getHostPhone());
		postinfo.put("Passwd", hostInfo.getPasswd());
		postinfo.put("UserSex", hostInfo.getGender());
		postinfo.put("DriveAge", hostInfo.getDriveAge());
		postinfo.put("CarType", hostInfo.getCarType());
		postinfo.put("CarNumber", hostInfo.getCarNumber());
		postinfo.put("CarColor", hostInfo.getCarColor());
		postinfo.put("UserName", hostInfo.getUserName());
		postinfo.put("CommandId", "HostInfoUpdateRequest");
		asyncHttpReqInterface.PostReq(reqName, URLDecoder.decode(postinfo.toString(), "gb2312"));
		return;
	}
	
	
	/**
	 * 乘客更新个人信息
	 * @param Guest
	 * @return
	 * @throws Exception
	 */
	public void guestInfoUpdateReq(Guest guestInfo) throws Exception{
		String reqName = "guestinfoupdaterequest";			
		postinfo.put("GuestPhone", guestInfo.getGuestPhone());
		postinfo.put("Passwd", guestInfo.getPasswd());
		postinfo.put("UserSex", guestInfo.getGender());
		postinfo.put("UserName", guestInfo.getUserName());
		postinfo.put("CommandId", "GuestInfoUpdateRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	
	
	/**
	 * 车主发布路线
	 * @param HostRoute
	 * @return
	 * @throws Exception
	 */
	public void hostRouteRelaseReq(HostRoute hostRoute) throws Exception{
		String reqName = "hostwayreleaserequest";			
		postinfo.put("HostPhone",hostRoute.getHostPhone());
		postinfo.put("SourceAddr", hostRoute.getSourceAddr());
		postinfo.put("DestAddr", hostRoute.getDestAddr());
		postinfo.put("PassAddr", hostRoute.getPassAddr());
		postinfo.put("GoTime", hostRoute.getGoTime());
		postinfo.put("SeatNumber", hostRoute.getSeatNumber());
		postinfo.put("LinePrice", hostRoute.getLinePrice());
		postinfo.put("AdditionalInfo", hostRoute.getAdditionalInfo());
		postinfo.put("CommandId", "HostWayReleaseRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 乘客发布路线
	 * @param GuestRoute
	 * @return
	 * @throws Exception
	 */
	public void guestRouteRelaseReq(GuestRoute guestRoute) throws Exception{
		String reqName = "guestwayreleaserequest";			
		postinfo.put("SourceAddr", guestRoute.getSourceAddr());
		postinfo.put("GuestPhone", guestRoute.getGuestPhone());
		postinfo.put("DestAddr", guestRoute.getDestAddr());
		postinfo.put("ExpectGoTime", guestRoute.getExpectGoTime());
		postinfo.put("AdditionalInfo", guestRoute.getAdditionalInfo());
		postinfo.put("CommandId", "GuestWayReleaseRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 乘客查询车主发布的路线
	 * @param sourceAddr
	 * @param destAddr
	 * @param goTime
	 * @param refreshIndex
	 * @throws Exception
	 */
	public void hostRouteQueryReq(String sourceAddr, String destAddr, String goTime, int refreshIndex) throws Exception{
		String reqName = "hostwayqueryrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=HostWayQueryRequest");
		if(refreshIndex >= 0)
			strBuild.append("&RefreshIndex=").append(refreshIndex);
		if(sourceAddr != null)
			strBuild.append("&SourceAddr=").append(sourceAddr);
		if(destAddr != null)
			strBuild.append("&DestAddr=").append(destAddr);
		if(goTime != null)
			strBuild.append("&StartDate=").append(goTime);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;
	}
	
	/**
	 * 车主查询乘客发布的路线
	 * @param sourceAddr
	 * @param destAddr
	 * @param goTime
	 * @param refreshIndex
	 * @return
	 * @throws Exception
	 */
	public void guestRouteQueryReq(String sourceAddr, String destAddr, String goTime, int refreshIndex) throws Exception{
		String reqName = "guestwayqueryrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GuestWayQueryRequest");
		if(refreshIndex >= 0)
			strBuild.append("&RefreshIndex=").append(refreshIndex);
		if(sourceAddr != null)
			strBuild.append("&SourceAddr=").append(sourceAddr);
		if(destAddr != null)
			strBuild.append("&DestAddr=").append(destAddr);
		if(goTime != null)
			strBuild.append("&StartDate=").append(goTime);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;
	}
	
	/**
	 * 车主路线详细信息查询
	 * @param hostWayId
	 * @return
	 * @throws Exception
	 */
	public void getHostRouteDetailReq(String hostWayId) throws Exception{	
		String reqName = "gethostwayinforequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GetHostWayInfoRequest");
		if(!(hostWayId.equals("")))
			strBuild.append("&HostWayId=").append(hostWayId);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;	
	}
	
	/**
	 * 乘客路线详细信息查询
	 * @param hostWayId
	 * @return
	 * @throws Exception
	 */
	public void getGuestRouteDetailReq(String guestWayId) throws Exception{
		String reqName = "getguestwaydetailinforequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GetGuestWayInfoRequest");
		if(!(guestWayId.equals("")))
			strBuild.append("&GuestWayId=").append(guestWayId);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;	
	}
	
	
	/**
	 * 车主当前订单查询
	 * @param guestPhone
	 * @param refreshIndex
	 * @return
	 * @throws Exception
	 */
	public void getHostRouteCurrentReq(String HostPhone, int refreshIndex) throws Exception{		
		String reqName = "gethostcurrentrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GetHostCurrentOrderRequest");
		if(HostPhone != null)
		{
			strBuild.append("&HostPhone=").append(HostPhone);
		}
		if(refreshIndex >= 0)
			strBuild.append("&RefreshIndex=").append(refreshIndex);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;	
	}
	
	
	/**
	 * 乘客当前订单查询
	 * @param guestPhone
	 * @param refreshIndex
	 * @return
	 * @throws Exception
	 */
	public void getGuestRouteCurrentReq(String guestPhone, int refreshIndex) throws Exception{		
		String reqName = "getguestcurrentrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GetGuestCurrentOrderRequest");
		if(guestPhone != null)
		{
			strBuild.append("&GuestPhone=").append(guestPhone);
		}
		if(refreshIndex >= 0)
			strBuild.append("&RefreshIndex=").append(refreshIndex);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;	
	}
	
	
	
	/**
	 * 车主历史路查询
	 * @param guestPhone
	 * @param refreshIndex
	 * @return
	 * @throws Exception
	 */
	public void getHostRouteHistoryReq(String HostPhone, int refreshIndex) throws Exception{		
		String reqName = "gethosthistoryrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GetHostHistoryOrderRequest");
		if(HostPhone != null)
		{
			strBuild.append("&HostPhone=").append(HostPhone);
		}
		if(refreshIndex >= 0)
			strBuild.append("&RefreshIndex=").append(refreshIndex);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;	
	}
	
	
	/**
	 * 乘客历史路查询
	 * @param guestPhone
	 * @param refreshIndex
	 * @return
	 * @throws Exception
	 */
	public void getGuestRouteHistoryReq(String guestPhone, int refreshIndex) throws Exception{		
		String reqName = "getguesthistoryrequest";
		StringBuilder strBuild = new StringBuilder();
		strBuild.append("CommandId=GetGuestHistoryOrderRequest");
		if(guestPhone != null)
		{
			strBuild.append("&GuestPhone=").append(guestPhone);
		}
		if(refreshIndex >= 0)
			strBuild.append("&RefreshIndex=").append(refreshIndex);
		asyncHttpReqInterface.GetReq(reqName, strBuild.toString());
		return;	
	}
	
	
	/**
	 * 车主客满
	 * @param GuestRoute
	 * @return
	 * @throws Exception
	 */
	public void setGuestFullReq(String  HostWayId) throws Exception{
		String reqName = "setguestfull";			
		postinfo.put("HostWayId", HostWayId);
		postinfo.put("isFull", "1");
		postinfo.put("CommandId", "SetGuestFullRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	
	/**
	 * 车主修改密码
	 * @param hostPhone
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 * @throws Exception
	 */
	public void hostChangePasswdReq(String hostPhone, String newPasswd) throws Exception{
		String reqName = "hostchangepasswdrequest";			
		postinfo.put("HostPhone", hostPhone);
		postinfo.put("NewPasswd", newPasswd);
		postinfo.put("CommandId", "HostChangePasswdRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	
	/**
	 * 乘客修改密码
	 * @param guestPhone
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 * @throws Exception
	 */
	public void guestChangePasswdReq(String guestPhone, String newPasswd) throws Exception{		
		String reqName = "guestchangepasswdrequest";			
		postinfo.put("GuestPhone", guestPhone);
		postinfo.put("NewPasswd", newPasswd);
		postinfo.put("CommandId", "GuestChangePasswdRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	
	
	/**
	 * 提交建议
	 * @param userPhone
	 * @param proposal
	 * @return
	 * @throws Exception
	 */
	public void userProposalReq(String userPhone, String proposal) throws Exception{		
		String reqName = "proposalrequest";			
		postinfo.put("UserId", userPhone);
		postinfo.put("Proposal", proposal);
		postinfo.put("CommandId", "ProposalRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 车主登陆
	 * @param guestPhone
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 * @throws Exception
	 */
	public void hostLoginReq(String hostPhone, String passwd) throws Exception{		
		String reqName = "hostloginrequest";			
		postinfo.put("HostPhone", hostPhone);
		postinfo.put("Passwd", passwd);
		postinfo.put("CommandId", "HostLoginRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 乘客登陆
	 * @param guestPhone
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public void guestLoginReq(String guestPhone, String passwd) throws Exception{			
		String reqName = "guestloginrequest";			
		postinfo.put("GuestPhone", guestPhone);
		postinfo.put("Passwd", passwd);
		postinfo.put("CommandId", "GuestLoginRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 乘客评价车主
	 * @param comments
	 * @return
	 * @throws Exception
	 */
	public void commentToHostReq(Comments comments) throws Exception{	
		String reqName = "commentstohostrequest";			
		postinfo.put("HostPhone", comments.getHostPhone());
		postinfo.put("GuestPhone", comments.getGuestPhone());
		postinfo.put("StarRank", comments.getStarRank());
		postinfo.put("Contents", comments.getContents());
		postinfo.put("CommandId", "CommentsToHostRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
		
	}
	
	/**
	 * 车主评价乘客
	 * @param comments
	 * @return
	 * @throws Exception
	 */
	public void commentToGuestReq(Comments comments) throws Exception{
		String reqName = "commentstoguestrequest";			
		postinfo.put("HostPhone", comments.getHostPhone());
		postinfo.put("GuestPhone", comments.getGuestPhone());
		postinfo.put("StarRank", comments.getStarRank());
		postinfo.put("Contents", comments.getContents());
		postinfo.put("CommandId", "CommentsToGuestRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	/**
	 * 获取当前车主发布拼车信息次数
	 * @param comments
	 * @return
	 * @throws Exception
	 */
	public void getHostReleaseCountsReq(String phoneNumber) throws Exception{
		String reqName = "gethostreleasecountsrequest";			
		postinfo.put("HostPhone", phoneNumber);
		postinfo.put("CommandId", "GetHostReleaseCountsRequest");
		asyncHttpReqInterface.PostReq(reqName, postinfo.toString());
		return;
	}
	
	
}



































