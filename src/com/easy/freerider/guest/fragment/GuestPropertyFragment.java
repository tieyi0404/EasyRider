package com.easy.freerider.guest.fragment;

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
import com.easy.freerider.guest.activity.GuestMainActivity;
import com.easy.freerider.util.TaskRequest;

public class GuestPropertyFragment extends Fragment {
	
	private static final String TAG = GuestPropertyFragment.class.getSimpleName();
	protected static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 0;
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private Message handleMsg;
	private TextView username = null;
	private TextView phone = null;
	private TextView sex = null;
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
							JSONObject object_userinfo =  object.getJSONObject("GuestInfo");
							
							String nametmp = object_userinfo.getString("UserName");
							int gendertmp = object_userinfo.getInt("UserSex");
							String phonetmp = object_userinfo.getString("HostPhone");
							
							
							SharedPreferences prefs = getActivity().getApplicationContext()
									.getSharedPreferences("user", 0);
							Editor edit = prefs.edit();
							if ("null".equals(nametmp)) {
								nametmp = "";
							}
							edit.putString("name", nametmp);
							
							if (gendertmp == 0) {
								
							} else if (gendertmp == 1) {
								edit.putString("gender", "男");
								
							} else if(gendertmp == 2) {
								edit.putString("gender", "女");
								
							}

							edit.putString("mobile", phonetmp);
							edit.commit();
							username.setText(prefs.getString("name", null));
							phone.setText(prefs.getString("mobile", null));
							sex.setText(prefs.getString("gender", null));
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
		View view = inflater.inflate(R.layout.fragment_guest_property, null);
		username = (TextView) view.findViewById(R.id.guest_property_username);
		phone = (TextView) view.findViewById(R.id.guest_property_phone);
		sex = (TextView) view.findViewById(R.id.guest_property_sex);
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

		Button btn = (Button) view.findViewById(R.id.guest_property_changebtn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences prefs = getActivity().getApplicationContext()
						.getSharedPreferences("user", 0);
				String u_mobile = prefs.getString("mobile", null);
				/*if (u_mobile == null) */
				if (u_mobile == null) 
				{
					Intent intent = new Intent();
					intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
				} else {
					Fragment f = new GuestFillPropertyFragment();
					((GuestMainActivity) getActivity()).changeGuestFillPropertyFragment(f,
							true);
				}

			}

		});

		logout = (Button) view.findViewById(R.id.guest_property_logoutbtn);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username.setText("");
				phone.setText("");
				sex.setText("");
				handleMsg = new Message();
				handleMsg.what = 6;
				((GuestMainActivity) getActivity()).logout();
				getActivity().runOnUiThread(updateThread);
				startActivity(new Intent(getActivity(),GuestMainActivity.class));
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
		SharedPreferences prefs = getActivity().getApplicationContext()
				.getSharedPreferences("user", 0);
		String mobile = prefs.getString("mobile", null);
		try {
			TaskRequest request = new TaskRequest(getActivity(),mHandler);
			request.getGuestInfoRequest(mobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
