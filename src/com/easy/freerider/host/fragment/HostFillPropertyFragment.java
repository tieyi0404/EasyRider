package com.easy.freerider.host.fragment;

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
import com.easy.freerider.host.activity.HostMainActivity;
import com.easy.freerider.model.Host;
import com.easy.freerider.model.User;
import com.easy.freerider.util.TaskRequest;

public class HostFillPropertyFragment extends Fragment {

	private static final String TAG = HostFillPropertyFragment.class.getSimpleName();
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private Spinner gender = null;
	private Spinner driverage = null;
	private Spinner color = null;
	private Host houtInfo;
	
	private ArrayAdapter driverageAdapter = null;
	private ArrayAdapter genderAdapter = null;
	private ArrayAdapter colorAdapter = null;
	
	
	private int postgender;
	private int postdriverage = 0;
	private String postcolor = null;
	private EditText username = null;
	private EditText cartype = null;
	private EditText carnumber = null;
	private Message handleMsg;
	
	
	
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
                    		edit.putString("mobile", houtInfo.getHostPhone());
                    		edit.putString("name", houtInfo.getUserName());
                    		if(houtInfo.getGender() == 1) {
                        		edit.putString("gender", "男");
                    		} else {
                    			edit.putString("gender", "女");
                    		}
                    		if((houtInfo.getDriveAge() >= 0) && (houtInfo.getDriveAge() < 1)){
                        		edit.putString("driveage","0 ~ 1" );
                    		} else if ((houtInfo.getDriveAge() > 1) && (houtInfo.getDriveAge() < 3)){
                        		edit.putString("driveage","1 ~ 3" );
                    		} else if ((houtInfo.getDriveAge() > 3) && (houtInfo.getDriveAge() < 5)) {
                        		edit.putString("driveage","3 ~ 5" );
                    		}else if ((houtInfo.getDriveAge() > 5) && (houtInfo.getDriveAge() < 10) ){
                        		edit.putString("driveage","5 ~ 10" );
                    		} else {
                        		edit.putString("driveage","10 ~ " );
                    		}

                    		edit.putString("cartype", houtInfo.getCarType());
                    		edit.putString("carnumber", houtInfo.getCarNumber());
                    		edit.putString("carcolor", houtInfo.getCarColor());
                    		edit.commit();
				        	//修改成功切换propertyfragment
				        	Fragment f = new HostPropertyFragment();
				        	((HostMainActivity) getActivity()).changeHostProperyFragment(f, false);
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
		View view = inflater.inflate(R.layout.fragment_host_fillin, null);
		username = (EditText)view.findViewById(R.id.fillin_username);
		cartype     = (EditText)view.findViewById(R.id.fillin_type);
		carnumber = (EditText)view.findViewById(R.id.fillin_number);
		gender = (Spinner)view.findViewById(R.id.fillin_sex);
		driverage = (Spinner)view.findViewById(R.id.fillin_driverage);
		color = (Spinner)view.findViewById(R.id.spinner_color);
		
		genderAdapter =  ArrayAdapter.createFromResource(getActivity(), R.array.fillin_sex, android.R.layout.simple_spinner_item);
		genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(genderAdapter);
		//添加事件Spinner事件监听 
		gender.setOnItemSelectedListener(new SpinnerSexSelectedListener());
		gender.setVisibility(View.VISIBLE);
		
		driverageAdapter =  ArrayAdapter.createFromResource(getActivity(), R.array.fillin_age, android.R.layout.simple_spinner_item);
		driverageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		driverage.setAdapter(driverageAdapter);
		//添加事件Spinner事件监听 
		driverage.setOnItemSelectedListener(new SpinnerDriverageSelectedListener());
        //设置默认值
		driverage.setVisibility(View.VISIBLE);
		
		colorAdapter =  ArrayAdapter.createFromResource(getActivity(), R.array.fillin_color, android.R.layout.simple_spinner_item);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		color.setAdapter(colorAdapter);
		//添加事件Spinner事件监听 
		color.setOnItemSelectedListener(new SpinnerColorSelectedListener());
        //设置默认值
		color.setVisibility(View.VISIBLE);
		
		User user=AppContext.getUser(getActivity());
		if(user.getMobile()!=null){
			username.setText(user.getName());
			String[] sexs = getActivity().getResources().getStringArray(R.array.fillin_sex);
			String[] ages = getActivity().getResources().getStringArray(R.array.fillin_age);
			String[] colors = getActivity().getResources().getStringArray(R.array.fillin_color);
			for(int i=0;i<sexs.length;i++){
				if(user.getGender()!=null&&user.getGender().equals(sexs[i])){
					gender.setSelection(i);
					break;
				}
			}
			for(int i=0;i<ages.length;i++){
				if(user.getDriverage()!=null&&user.getDriverage().equals(ages[i])){
					driverage.setSelection(i);
					break;
				}
			}
			for(int i=0;i<colors.length;i++){
				if(user.getCarcolor()!=null&&user.getCarcolor().equals(colors[i])){
					color.setSelection(i);
					break;
				}
			}
		}
		Button btn = (Button)view.findViewById(R.id.fillin_changebtn);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nName=username.getText().toString();
				if(nName.equals("")||nName.length()>6){
					Toast.makeText(getActivity(), "姓名长度不能超过6个字符", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String nCartype=cartype.getText().toString();
				if(nCartype.equals("")||nCartype.length()>10){
					Toast.makeText(getActivity(), "汽车品牌不能超过10个字符", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String nCarnumber=carnumber.getText().toString();
				if(nCarnumber.equals("")||nCarnumber.length()>15){
					Toast.makeText(getActivity(), "汽车牌号不能超过15个字符", Toast.LENGTH_SHORT).show();
					return;
				}
				SharedPreferences prefs = getActivity().getApplicationContext()
						.getSharedPreferences("user", 0);
				String mobile = prefs.getString("mobile", null);
				
				TaskRequest request = new TaskRequest(getActivity(),mHandler);
				try {
					//重新修改车主个人信息
					houtInfo = new Host();			
					houtInfo.setHostPhone(mobile);
					houtInfo.setUserName(nName);
					houtInfo.setGender(postgender);
					houtInfo.setDriveAge(postdriverage);
					houtInfo.setCarType(nCartype);
					houtInfo.setCarNumber(nCarnumber);
					houtInfo.setCarColor(postcolor);
					request.hostInfoUpdateReq(houtInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();	
				}
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
	
	//使用XML形式操作
    class SpinnerDriverageSelectedListener implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
           // sex.setText(ageAdapter.getItem(arg2));

        	   postdriverage = arg2+1;
        	   if(postdriverage == 1) {
        		   postdriverage = 1;
        	   } else if (postdriverage == 2) {
        		   postdriverage = 2;
        	   } else if (postdriverage == 3) {
        		   postdriverage = 4;
        	   }else if (postdriverage == 4) {
        		   postdriverage = 8;
        	   } else {
        		   postdriverage = 11;
        	   }
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
             
        }
         
    }
    class SpinnerColorSelectedListener implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
           // sex.setText(ageAdapter.getItem(arg2));        
        	postcolor = getResources().getStringArray(R.array.fillin_color)[arg2];
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
		        	Fragment f = new HostPropertyFragment();
		        	((HostMainActivity) getActivity()).changeHostProperyFragment(f, false);
				}else if(handleMsg.what == 2){
					Log.d(TAG, "22222");
					Toast.makeText(getActivity(), getResources().getString(R.string.fillin_tip2), Toast.LENGTH_SHORT).show();
				}
	       }   
	           
	   };

}
