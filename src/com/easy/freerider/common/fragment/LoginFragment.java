package com.easy.freerider.common.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.AppContext;
import com.easy.freerider.common.activity.RegisterActivity;
import com.easy.freerider.guest.activity.GuestMainActivity;
import com.easy.freerider.host.activity.HostMainActivity;
import com.easy.freerider.util.TaskRequest;

public class LoginFragment extends Fragment {

	private static final String TAG = LoginFragment.class.getSimpleName();
	private EditText phone;
	private EditText passwd;
	private TextView backPassword;
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {    
			                if((object.getString("CommandId").equals("QueryHostInfoResponse")) || 
			        				(object.getString("CommandId").equals("QueryGuestInfoResponse"))) {
			                	
									Toast.makeText(getActivity(), 
											getResources().getString(R.string.register_tip6),
											Toast.LENGTH_SHORT).show();
	                    			SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("user", 0); 
	                        		Editor edit = prefs.edit();
	                    			if(AppContext.getRole(getActivity()) == 1)
	                    			{
										JSONObject object_userinfo =  object.getJSONObject("HostInfo");
										//支持用户更换账号登陆
	                    				edit.putString("mobile", phone.getText().toString());
		                        		edit.putString("passwd", passwd.getText().toString());
		                        		edit.putString("name", object_userinfo.getString("UserName"));
		                        		
		                        		if(object_userinfo.getInt("UserSex") == 1) {
		                            		edit.putString("gender", "男");
		                        		} else if(object_userinfo.getInt("UserSex") == 2){
		                        			edit.putString("gender", "女");
		                        		} else {
		                        			edit.putString("gender", "未知");
		                        		}
		                        		
		                        		if((object_userinfo.getInt("DriveAge") > 0) && (object_userinfo.getInt("DriveAge") < 1)){
		                            		edit.putString("driveage","0 ~ 1" );
		                        		} else if ((object_userinfo.getInt("DriveAge") >=1) && (object_userinfo.getInt("DriveAge")< 3)){
		                            		edit.putString("driveage","1 ~ 3" );
		                        		} else if((object_userinfo.getInt("DriveAge") >=3) && (object_userinfo.getInt("DriveAge") < 5)) {
		                            		edit.putString("driveage","3 ~ 5" );
		                        		}else if ((object_userinfo.getInt("DriveAge") >=5) && (object_userinfo.getInt("DriveAge") < 10) ){
		                            		edit.putString("driveage","5 ~ 10" );
		                        		} else if ((object_userinfo.getInt("DriveAge") >=10)) {
		                            		edit.putString("driveage","10 ~ " );
		                        		} else {
		                            		edit.putString("driveage","未知" );
		                        		}
		                        		
		                        		if(object_userinfo.getString("CarType").equals("")) {
			                        		edit.putString("cartype", "");
		                        		} else {
			                        		edit.putString("cartype", object_userinfo.getString("CarType"));
		                        		}
		                        		
		                        		if(object_userinfo.getString("CarColor").equals("")) {
			                        		edit.putString("carcolor", "");
		                        		} else {
			                        		edit.putString("carcolor", object_userinfo.getString("CarColor"));
		                        		}
		                        		
		                        		if(object_userinfo.getString("CarNumber").equals("")) {
			                        		edit.putString("carnumber", "");
		                        		} else {
			                        		edit.putString("carnumber", object_userinfo.getString("CarNumber"));
		                        		}
		                        		edit.commit();
	                    			} else {
										JSONObject object_guestinfo =  object.getJSONObject("GuestInfo");
										//支持用户更换账号登陆
	                    				edit.putString("mobile", phone.getText().toString());
		                        		edit.putString("passwd", passwd.getText().toString());
		                        		edit.putString("name", object_guestinfo.getString("UserName"));
		                        		
		                        		if(object_guestinfo.getInt("UserSex") == 1) {
		                            		edit.putString("gender", "男");
		                        		} else if(object_guestinfo.getInt("UserSex") == 2){
		                        			edit.putString("gender", "女");
		                        		} else {
		                        			edit.putString("gender", "未知");
		                        		}
		                        		edit.commit();
	                    			}
	    							//登陆成功后，切换到主界面
					        		Intent intent = new Intent();  
									if(AppContext.getRole(getActivity()) == 1)
									{
										intent.setClass(getActivity(), HostMainActivity.class); 
										startActivity(intent); 
									} else {
										intent.setClass(getActivity(), GuestMainActivity.class); 
										startActivity(intent); 
									}
									getActivity().finish();
			                	} else {
				        			if(AppContext.getRole(getActivity()) == 1)
				        			{
						        		try {
						        			TaskRequest request = new TaskRequest(getActivity(), mHandler);
							        		request.getHostInfoRequest( phone.getText().toString());
							        		
						        		} catch(Exception e) {
						        			// TODO Auto-generated catch block
						        			e.printStackTrace();
						        			Message msg_r = new Message();
						        			msg_r.what = RWSFAIL;
						        			mHandler.sendMessage(msg_r);
						        		}
				        			} else {
						        		try {
						        			TaskRequest request = new TaskRequest(getActivity(), mHandler);
							        		request.getGuestInfoRequest( phone.getText().toString());	
						        		} catch(Exception e) {
						        			// TODO Auto-generated catch block
						        			e.printStackTrace();
						        			Message msg_r = new Message();
						        			msg_r.what = RWSFAIL;
						        			mHandler.sendMessage(msg_r);
						        		}
				        			} 
			                	}
	
			        		} else  if (object.getInt("Code") == 407) {
			        		    Toast.makeText(getActivity(), 
			        		    		object.getString("Reason"), 
			        		    		Toast.LENGTH_SHORT).show();
			        		}  else if (object.getInt("Code") == 500) {
			    			    Toast.makeText(getActivity(), 
			    			    		getResources().getString(R.string.internal_error), 
			    			    		Toast.LENGTH_SHORT).show();	
			        		}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
	    			    Toast.makeText(getActivity(), 
	    			    		getResources().getString(R.string.internal_error), 
	    			    		Toast.LENGTH_SHORT).show();	
						break;
					}
				}
				break;
		  case RWSFAIL:
				Toast.makeText(getActivity(), 
						getResources().getString(R.string.register_tip15), 
						Toast.LENGTH_SHORT).show();
			default:
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
		// TODO Auto-generated method stub
		View v = inflater.inflate( R.layout.fragment_login, container, false);
		phone = (EditText)v.findViewById(R.id.login_phone);
		passwd = (EditText)v.findViewById(R.id.login_passwd);
		Button btn = (Button)v.findViewById(R.id.login_btn);
		btn.setText("登  陆");
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(phone.getText().toString().trim().length() != 11){
					Toast.makeText(getActivity(), getResources().getString(R.string.register1_tip), Toast.LENGTH_SHORT).show();
					return;
				}
				if(passwd.getText().toString().trim().length() == 0){
					Toast.makeText(getActivity(), getResources().getString(R.string.login_passwd_tip), Toast.LENGTH_SHORT).show();
					return;
				}
				userloginRequest();
			}
			
		});
		TextView register = (TextView)v.findViewById(R.id.login_resigter);
		register.setText("注  册");
		register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();  

                intent.setClass(getActivity(), RegisterActivity.class); 
                startActivity(intent); 
			}
			
		});
		
		backPassword=(TextView)v.findViewById(R.id.login_backpassword);
		backPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		backPassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), RegisterActivity.class);  
				intent.putExtra("ActionType", "backpassword");
                startActivity(intent); 
			}
		});
		
		return v;
	}
	
	public void userloginRequest() {
		try {
			TaskRequest request = new TaskRequest(getActivity(), mHandler);
			if(AppContext.getRole(getActivity()) == 1)
			{
				request.hostLoginReq(phone.getText().toString(), passwd.getText().toString());
			} else {
				request.guestLoginReq(phone.getText().toString(), passwd.getText().toString());
			}
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = new Message();
			msg.what = RWSFAIL;
			mHandler.sendMessage(msg);
		}
	}		
}
