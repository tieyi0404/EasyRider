package com.easy.freerider.common.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.easy.freerider.location.LocationService;
import com.easy.freerider.location.WriteLog;
import com.easy.freerider.model.User;

public class AppContext extends Application {
	public LocationService locationService;
	final static String EDIT_KEY = "user";
	
	public static boolean ISCheckVersion = false;
	
	public static String ApkCacheDir = "/freerider/apks";   //apk 存储路径
	public static int ProgressBarUpdate = 0;       //donwload thred 向CloudBacupListFragment
	public static int ProgressBarComplete = 1;    //报告下载进度
	
	public static String whichCity;
	
	public static User getUser(Context context) {
		User user = new User();
		SharedPreferences prefs = context.getSharedPreferences(EDIT_KEY, 0);

		user.setPasswd(prefs.getString("passwd", null));
		user.setMobile(prefs.getString("mobile", null));
		user.setName(prefs.getString("name", null));
		user.setDriverage(prefs.getString("driveage", null));
		user.setGender(prefs.getString("gender", null));
		user.setCartype(prefs.getString("cartype", null));
		user.setCarnumber(prefs.getString("carnumber", null));
		user.setCarcolor(prefs.getString("carcolor", null));
		return user;
	}
	
	public static void setRole(int roleId, Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(EDIT_KEY, 0);
		SharedPreferences.Editor editor = prefs.edit(); 
		//用putString的方法保存数据 
		editor.putInt("role", roleId);//role = 1: host role = 2: guest
		//提交当前数据 
		editor.commit(); 		
	}
	
	public static int getRole(Context context)
	{
	   SharedPreferences sharedPreferences= context.getSharedPreferences(EDIT_KEY, 0); 
	   
	   int roleId = 0;
	   
	   roleId = sharedPreferences.getInt("role", 0);
	   return roleId;
	}

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext()); 
        
		//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		//注册监听
		locationService.setLocationOption(locationService.getDefaultLocationClientOption());
       	
		// 定位SDK
		// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
		locationService.start();
    }
    
	/*****
	 * @see copy funtion to you project
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				StringBuffer sb = new StringBuffer(256);
				/**
				 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
				 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
				 */
				sb.append(location.getCity());
				
				if(location.getCity() != null) {
					String state = sb.toString();
					state =  state.substring(0,state.length()-1);
					if(!state.equals("")) {
						SharedPreferences prefs = getSharedPreferences(EDIT_KEY, 0); 
						SharedPreferences.Editor edit = prefs.edit();
        				edit.putString("position", state);
        				edit.commit();
        				
        				System.out.println(location.getCity());
        				//only location one time if success
        				stopLocation();  				
					}
				}

			}
		}

	};
	
	private void stopLocation()
	{
	    locationService.unregisterListener(mListener); //注销掉监听
	    locationService.stop(); //停止定位服务
	}

}