package com.easy.freerider.common.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.RegisterActivity;

public class RegisterFragment2 extends Fragment {

	private static final String TAG = RegisterFragment2.class.getSimpleName();
	private Message handleMsg;
	private EditText code = null;
	private Button countdown = null;
	private Timer timer = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		timer = new Timer();
		timer.schedule(task, 0, 1000);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_register2, container, false);
		code = (EditText)v.findViewById(R.id.register2_code);
        Button registerbtn = (Button)v.findViewById(R.id.register2_registerbtn);
        registerbtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if(code.getText().toString().trim().length() == 0){
						Toast.makeText(getActivity(), getResources().getString(R.string.register2_tip1), Toast.LENGTH_SHORT) 
		                .show();
						return;
					}
					postCodeRequest();
				}
        		
        	});
        countdown = (Button)v.findViewById(R.id.register2_countdownbtn);
		return v;
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
		}
	}
	
	
	private void postCodeRequest(){	
		String random = ((RegisterActivity) getActivity()).s_random;
		if(code.getText().toString().equalsIgnoreCase(random))
		{
		  handleMsg = new Message();
		  handleMsg.what = 6;
	    } else {
	       handleMsg = new Message();
		   handleMsg.what = 2;  						
	    }
	    getActivity().runOnUiThread(updateThread);  
    }
	
	
	Runnable updateThread = new Runnable()    
	   {   
	  
	        @Override  
	        public void run()   
	        {   
	            //更新UI   
	        	if (handleMsg.what == 6) {
	        		Toast.makeText(getActivity(), getResources().getString(R.string.activation_tip1), Toast.LENGTH_SHORT).show();
	        		//切换激活页面
	        		Fragment f = new RegisterFragment3();
	        		((RegisterActivity)getActivity()).changeRegisterFragment3(f, true);
				} else if(handleMsg.what == 2){
					Toast.makeText(getActivity(), getResources().getString(R.string.activation_tip2), Toast.LENGTH_SHORT).show();
				}
	       }   
	           
	   };
	   

		   TimerTask task = new TimerTask() {  
		        @Override  
		        public void run() {  
		  
		            getActivity().runOnUiThread(new Runnable() {      // UI thread  
		                @Override  
		                public void run() {
		                	try{
			                	countdown.setText(String.valueOf(((RegisterActivity)getActivity()).getTimes()));
				        		if(((RegisterActivity)getActivity()).getTimes()<0){
				        			((RegisterActivity)getActivity()).stopTimer();
				        			((RegisterActivity)getActivity()).setTimes(90);
				        		}
		                	}catch (Exception e) {
								// TODO: handle exception
							}

		                }  
		            });  
		        }  
		    };
}
