package com.easy.freerider.common.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.AppContext;
import com.easy.freerider.common.activity.LoginActivity;
import com.easy.freerider.common.activity.PolicyActivity;
import com.easy.freerider.common.activity.RegisterActivity;
import com.easy.freerider.model.Guest;
import com.easy.freerider.model.Host;
import com.easy.freerider.model.User;
import com.easy.freerider.util.TaskRequest;

public class RegisterFragment3 extends Fragment {

	private static final String TAG = RegisterFragment3.class.getSimpleName();
	protected static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 0;
	private String ActionType;
	private EditText passwd = null;
	private EditText confirmpasswd = null;
	private TextView read_p;
	private String u_phone;
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
						if( (object.getInt("Code") == 200) || (object.getString("Result") == "OK") ){
							try {	
								TaskRequest request = new TaskRequest(getActivity(), mHandler);
								if((object.getString("CommandId").equals("HostRegisterResponse")) && 
										AppContext.getRole(getActivity()) == 1) {
									Guest guestInfo = new Guest();
									guestInfo.setGuestPhone(u_phone);
									guestInfo.setPasswd(passwd.getText().toString());
									request.guestRegisterReq(guestInfo);
								} else if ((object.getString("CommandId").equals("GuestRegisterResponse")) && 
										AppContext.getRole(getActivity()) == 2) {
									Host hostInfo = new Host();
									hostInfo.setHostPhone(u_phone);
									hostInfo.setPasswd(passwd.getText().toString());
									request.hostRegisterReq(hostInfo);	
								}	else {
					        		Toast.makeText(getActivity(), 
					        				getResources().getString(R.string.activation_tip1), 
					        				Toast.LENGTH_SHORT).show();
					        		Intent intent = new Intent();  
									intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					                intent.setClass(getActivity(), LoginActivity.class); 
					                startActivity(intent); 
								}
							} catch(Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
			        		} else if (object.getInt("Code") == 406) {
			        		    Toast.makeText(getActivity(), 
			        		    		getResources().getString(R.string.register_tip11), 
			        		    		Toast.LENGTH_SHORT).show();
			        		}else if (object.getInt("Code") == 404) {
			        		    Toast.makeText(getActivity(), 
			        		    		getResources().getString(R.string.register_tip12), 
			        		    		Toast.LENGTH_SHORT).show();
			        		} else if (object.getInt("Code") == 500) {
			    			    Toast.makeText(getActivity(), 
			    			    		getResources().getString(R.string.internal_error), 
			    			    		Toast.LENGTH_SHORT).show();
			        		}else {
			    			    Toast.makeText(getActivity(), 
			    			    		object.getString("Reason"), 
			    			    		Toast.LENGTH_SHORT).show();
			        		}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
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
		ActionType = ((RegisterActivity) getActivity()).ActionType;
		View v = inflater.inflate(R.layout.fragment_register3, container, false);
		CheckBox check = (CheckBox)v.findViewById(R.id.checkBox1);
		check.setChecked(true);
		passwd = (EditText)v.findViewById(R.id.register3_passwd);
		confirmpasswd = (EditText)v.findViewById(R.id.register3_confirmpasswd);
		u_phone = ((RegisterActivity) getActivity()).s_phone;
        read_p = (TextView)v.findViewById(R.id.read_policy);
        read_p.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub);
				startActivity(new Intent(getActivity(),PolicyActivity.class));
			}
			});
        Button registerbtn = (Button)v.findViewById(R.id.register3_registerbtn);
        registerbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					if(passwd.getText().toString().trim().length() < 6){
						Toast.makeText(getActivity(), getResources().getString(R.string.register_tip2), Toast.LENGTH_SHORT) 
		                .show();
						return;
					}
					if(!passwd.getText().toString().trim().equalsIgnoreCase(confirmpasswd.getText().toString().trim())){
						Toast.makeText(getActivity(), getResources().getString(R.string.register_tip3), Toast.LENGTH_SHORT) 
		                .show();
						return;
					}
					if ("backpassword".equals(ActionType)) {
						modifyRequest();
					} else {
						registerRequest();
					}
				}
        	});
		return v;
	}
	
	private void registerRequest(){
		// 访问服务器端 获取json数据  
        // 创建客户端对象  
		try {
			TaskRequest request = new TaskRequest(getActivity(), mHandler);
			if(AppContext.getRole(getActivity()) == 1)
			{
				Host hostInfo = new Host();
				hostInfo.setHostPhone(u_phone);
				hostInfo.setPasswd(passwd.getText().toString());
				request.hostRegisterReq(hostInfo);
			} else {
				Guest guestInfo = new Guest();
				guestInfo.setGuestPhone(u_phone);
				guestInfo.setPasswd(passwd.getText().toString());
				request.guestRegisterReq(guestInfo);
			}
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = new Message();
			msg.what = RWSFAIL;
			mHandler.sendMessage(msg);
		}
	}
	
	private void modifyRequest(){
		// 访问服务器端 获取json数据  
        // 创建客户端对象  
		try {
			TaskRequest request = new TaskRequest(getActivity(), mHandler);
			if(AppContext.getRole(getActivity()) == 1)
			{
				User user=AppContext.getUser(getActivity());
				request.hostChangePasswdReq(u_phone,passwd.getText().toString());
			} else {
				User user=AppContext.getUser(getActivity());
				request.guestChangePasswdReq(u_phone,passwd.getText().toString());
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
