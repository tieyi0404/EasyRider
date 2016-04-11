package com.easy.freerider.host.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.LoginActivity;
import com.easy.freerider.host.activity.HostMainActivity;
import com.easy.freerider.util.TaskRequest;

public class HostPropertyFragment extends Fragment {

	private static final String TAG = HostPropertyFragment.class.getSimpleName();
	protected static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 0;
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private Message handleMsg;
	private TextView username = null;
	private TextView phone = null;
	private TextView sex = null;
	private TextView age = null;
	private TextView type = null;
	private TextView color = null;
	private TextView number = null;
	private Button logout = null;


	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {  
							JSONObject object_userinfo =  object.getJSONObject("HostInfo");
//							System.out.println("harry ---" + URLDecoder.decode(object_userinfo.toString(), "UTF-8"));
/*							URLDecoder.decode(object_userinfo.toString(),"UTF-8");*/
							String nametmp = object_userinfo.getString("UserName");
							int gendertmp = object_userinfo.getInt("UserSex");
							int driveragetmp = object_userinfo.getInt("DriveAge");	
							String carTypetmp = object_userinfo.getString("CarType");
							String carNumbertmp = object_userinfo.getString("CarNumber");
							String carColortmp = object_userinfo.getString("CarColor");
							String phonetmp = object_userinfo.getString("HostPhone");
							SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("user", 0);
							Editor edit = prefs.edit();
							if ("null".equals(nametmp)) {
								nametmp = "";
							}
							edit.putString("name", nametmp);
							
							if (gendertmp == 0) {
								edit.putString("gender", "未知");	
							} else if (gendertmp == 1) {
								edit.putString("gender", "男");					
							} else if(gendertmp == 2) {
								edit.putString("gender", "女");
								
							}			
							if (driveragetmp == 0) {
								edit.putString("driveage","0 ~ 1" );
							} else {
								
	                    		if((driveragetmp > 0) && (driveragetmp < 1)){
	                        		edit.putString("driveage","0 ~ 1" );
	                    		} else if ((driveragetmp >=1) && (driveragetmp < 3)){
	                        		edit.putString("driveage","1 ~ 3" );
	                    		} else if ((driveragetmp >= 3) && (driveragetmp < 5)) {
	                        		edit.putString("driveage","3 ~ 5" );
	                    		}else if ((driveragetmp >= 5) && (driveragetmp < 10) ){
	                        		edit.putString("driveage","5 ~ 10" );
	                    		} else if (driveragetmp >= 10){
	                        		edit.putString("driveage","10 ~ " );
	                    		} else {
	                        		edit.putString("driveage","未知" );
	                    		}
							}
							
							if ("null".equals(carTypetmp)) {
								carTypetmp = "";
							}
							edit.putString("cartype", carTypetmp);
							
							if ("null".equals(carNumbertmp)) {
								carNumbertmp = "";
							}
							edit.putString("carnumber", carNumbertmp);
							
							if ("null".equals(carColortmp)) {
								carColortmp = "";
							}
							edit.putString("carcolor", carColortmp);

							edit.putString("mobile", phonetmp);
							edit.commit();

							username.setText(prefs.getString("name", null));
							phone.setText(prefs.getString("mobile", null));
							sex.setText(prefs.getString("gender", null));
							age.setText(prefs.getString("driveage", null));
							type.setText(prefs.getString("cartype", null));
							number.setText(prefs.getString("carnumber", null));
							color.setText(prefs.getString("carcolor", null));
						} else {
		        		    Toast.makeText(getActivity(), 
		        		    		object.getString("Reason"), 
		        		    		Toast.LENGTH_SHORT).show();
						}
					}catch(Exception e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
				}
				break;
			case RWSFAIL:
				break;
			}
		}
	};

					


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_host_property, null);		
		username = (TextView) view.findViewById(R.id.property_username);
		phone = (TextView) view.findViewById(R.id.property_phone);
		sex = (TextView) view.findViewById(R.id.property_sex);
		age = (TextView) view.findViewById(R.id.property_age);
		type = (TextView) view.findViewById(R.id.property_type);
		color = (TextView) view.findViewById(R.id.property_color);
		number = (TextView) view.findViewById(R.id.property_number);
		SharedPreferences prefs = getActivity().getApplicationContext()
				.getSharedPreferences("user", 0);
		
		
		String name = prefs.getString("name", null);
		if (name == null) {
			username.setText("");
		} else {
			username.setText(name);
		}
		String mobile = prefs.getString("mobile", null);
		if (mobile == null) {
			phone.setText("");
		} else {
			phone.setText(mobile);
		}
		String sextmp = prefs.getString("gender", null);
		if (sextmp == null) {
			sex.setText("");
		} else {
			sex.setText(sextmp);
		}
		String agetmp = prefs.getString("driveage", null);
		if (agetmp == null) {
			age.setText("");
		} else {
			age.setText(agetmp);
		}
		String typetmp = prefs.getString("cartype", null);
		if (typetmp == null) {
			type.setText("");
		} else {
			type.setText(typetmp);
		}
		String colortmp = prefs.getString("carcolor", null);
		if (colortmp == null) {
			color.setText("");
		} else {
			color.setText(colortmp);
		}
		String numbertmp = prefs.getString("carnumber", null);
		if (numbertmp == null) {
			number.setText("");
		} else {
			number.setText(numbertmp);
		}

		Button btn = (Button) view.findViewById(R.id.property_changebtn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences prefs = getActivity().getApplicationContext()
						.getSharedPreferences("user", 0);
				String u_mobile = prefs.getString("mobile", null);
				if (u_mobile == null) 
				{
					Intent intent = new Intent();
					intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
				} else {
					Fragment f = new HostFillPropertyFragment();
					((HostMainActivity) getActivity()).changeHostFillPropertyFragment(f,
							true);
				}

			}

		});

		logout = (Button) view.findViewById(R.id.property_logoutbtn);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username.setText("");
				phone.setText("");
				sex.setText("");
				age.setText("");
				type.setText("");
				color.setText("");
				number.setText("");
				handleMsg = new Message();
				handleMsg.what = 6;
				((HostMainActivity) getActivity()).logout();
				getActivity().runOnUiThread(updateThread);
				startActivity(new Intent(getActivity(),HostMainActivity.class));
			}

		});

		if (mobile != null && "".equalsIgnoreCase(mobile) == false)
		{
			getUserInfo();
		}

		if (mobile == null || mobile.equals(""))
		{ // 跳转登录
			logout.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.setClass(getActivity(), LoginActivity.class);
			startActivity(intent);
		} else {
			logout.setVisibility(View.VISIBLE);
		}

		return view;
	}

	Runnable updateThread = new Runnable() {

		@Override
		public void run() {
			// 更新UI
			if (handleMsg.what == 6) {
				logout.setVisibility(View.GONE);
				Toast.makeText(getActivity(),
						getResources().getString(R.string.register_tip4),
						Toast.LENGTH_SHORT).show();
			} else if (handleMsg.what == 17) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.property_tip1),
						Toast.LENGTH_SHORT).show();
			} else if (handleMsg.what == 3) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.property_tip),
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	void getUserInfo() {
		try {
			SharedPreferences prefs = getActivity().getApplicationContext()
					.getSharedPreferences("user", 0);
			String mobile = prefs.getString("mobile", null);
			TaskRequest request = new TaskRequest(getActivity(),mHandler);
			request.getHostInfoRequest(mobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
