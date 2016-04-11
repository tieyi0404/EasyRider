package com.easy.freerider.guest.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.AppContext;
import com.easy.freerider.guest.activity.GuestMainActivity;
import com.easy.freerider.host.fragment.HostFillPropertyFragment;
import com.easy.freerider.host.fragment.HostPropertyFragment;
import com.easy.freerider.model.Guest;
import com.easy.freerider.model.User;
import com.easy.freerider.util.TaskRequest;

public class GuestFillPropertyFragment extends Fragment {
	
	private static final String TAG = HostFillPropertyFragment.class.getSimpleName();
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private Spinner gender = null;
	private ArrayAdapter genderAdapter = null;
	private int postgender;
	private EditText username = null;
	private Message handleMsg;
	private Guest guestInfo;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {    
							Toast.makeText(getActivity(), 
									getResources().getString(R.string.modify_success), 
									Toast.LENGTH_SHORT).show();
							
                			SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("user", 0); 
                    		Editor edit = prefs.edit();
                    		edit.putString("mobile", guestInfo.getGuestPhone());
                    		edit.putString("name", guestInfo.getUserName());
                    		if(guestInfo.getGender() == 1) {
                        		edit.putString("gender", "男");
                    		} else {
                    			edit.putString("gender", "女");
                    		}
                    		edit.commit();
				        	//修改成功切换propertyfragment
				        	Fragment f = new GuestPropertyFragment();
				        	((GuestMainActivity) getActivity()).changeGuestProperyFragment(f, false);
						}
					}catch (JSONException e) {
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
	       		Toast.makeText(getActivity(), getResources().getString(R.string.register_tip15), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_guest_fillin, null);
		username = (EditText)view.findViewById(R.id.guest_fillin_username);
		gender = (Spinner)view.findViewById(R.id.guest_fillin_sex);
		genderAdapter =  ArrayAdapter.createFromResource(getActivity(), R.array.fillin_sex, android.R.layout.simple_spinner_item);
		genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(genderAdapter);
		//添加事件Spinner事件监听 
		gender.setOnItemSelectedListener(new SpinnerSexSelectedListener());
		gender.setVisibility(View.VISIBLE);
		
		User user=AppContext.getUser(getActivity());
		if(user.getMobile()!=null){
			username.setText(user.getName());
			String[] sexs = getActivity().getResources().getStringArray(R.array.fillin_sex);
			for(int i=0;i<sexs.length;i++){
				if(user.getGender()!=null&&user.getGender().equals(sexs[i])){
					gender.setSelection(i);
					break;
				}
			}
		}
		
		Button btn = (Button)view.findViewById(R.id.guest_fillin_changebtn);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nName=username.getText().toString();
				if(nName==null||nName.length()>6){
					Toast.makeText(getActivity(), "姓名长度不能超过6个字符", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				String taskFillPropertyStr;
				String code;
				JSONObject obj;
				SharedPreferences prefs = getActivity().getApplicationContext()
						.getSharedPreferences("user", 0);
				String mobile = prefs.getString("mobile", null);
				TaskRequest request = new TaskRequest(getActivity(),mHandler);
				try {
					//重新修改车主个人信息
					guestInfo = new Guest();			
					guestInfo.setGuestPhone(mobile);
					guestInfo.setUserName(nName);
					guestInfo.setGender(postgender);
					request.guestInfoUpdateReq(guestInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();	
				}
				
/*				TaskRequest request = new TaskRequest(getActivity());
				try {
					Guest guestInfo = new Guest();
					guestInfo.setGuestPhone(mobile);
					guestInfo.setGender(postgender);
					guestInfo.setUserName(nName);
					taskFillPropertyStr = request.guestRegisterReq(guestInfo);
					obj = new JSONObject(taskFillPropertyStr);
					code = obj.getString("Code");
					if ("200".equals(code)) {
                    	Editor edit = prefs.edit();
                    	edit.putString("name", username.getText().toString());
                    	if(postgender == 1 ){
                    		edit.putString("gender", "男");
                    	}else{
                    		edit.putString("gender", "女");
                    	}
                    	edit.commit();
						handleMsg = new Message();
						handleMsg.what = 6;
					} else if ("404".equals(code)) {
						handleMsg = new Message();
						handleMsg.what = 2;
					}
					getActivity().runOnUiThread(updateThread); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();	
				}*/
			}
			
		});
        
		return view;
	}
		
	    //使用XML形式操作
	    class SpinnerSexSelectedListener implements OnItemSelectedListener{
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	           // sex.setText(ageAdapter.getItem(arg2));
	           if(arg2 == 0){
	        	   postgender = 1;
	           }else if(arg2 == 1){
	        	   postgender = 2;
	           }
	        }
	 
	        public void onNothingSelected(AdapterView<?> arg0) {
	             
	        }
	         
	    }
    
    Runnable updateThread = new Runnable()    
	   {   
	  
	        @Override  
	        public void run()   
	        {   
	            //更新UI   
	        	if (handleMsg.what == 6) {
	        		
	        		Toast.makeText(getActivity(), getResources().getString(R.string.fillin_tip1), Toast.LENGTH_SHORT).show();
	        	//修改成功切换propertyfragment
		        	Fragment f = new GuestPropertyFragment();
		        	((GuestMainActivity) getActivity()).changeGuestProperyFragment(f, false);
				}else if(handleMsg.what == 2){
					Log.d(TAG, "22222");
					Toast.makeText(getActivity(), getResources().getString(R.string.fillin_tip2), Toast.LENGTH_SHORT).show();
				}
	       }   
	           
	   };


}
