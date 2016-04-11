package com.easy.freerider.common.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.RegisterActivity;
import com.easy.freerider.util.SendSMS;
import com.easy.freerider.util.SendSMS_old;

public class RegisterFragment1 extends Fragment {

	private static final String TAG = RegisterFragment1.class.getSimpleName();

	private Message handleMsg;
	private EditText phone = null;
	private String ActionType;
	public boolean isSubmitOk=false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater
				.inflate(R.layout.fragment_register1, container, false);
		ActionType = ((RegisterActivity) getActivity()).ActionType;
		TextView headTitle = (TextView) v.findViewById(R.id.userid);
		if ("backpassword".equals(ActionType)) {
			headTitle.setText("找回密码");
		}
	
		phone = (EditText) v.findViewById(R.id.register1_phone);
		Button registerbtn = (Button) v
				.findViewById(R.id.register1_registerbtn);
		registerbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (phone.getText().toString().trim().length() != 11) {
					Toast.makeText(getActivity(),
							getResources().getString(R.string.register1_tip),
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (((RegisterActivity) getActivity()).getTimes() == 90) {
					((RegisterActivity) getActivity()).startTimer(phone);
					getCodeRequest();
				}

				if (((RegisterActivity) getActivity()).getTimes() <= 0) {
					((RegisterActivity) getActivity()).setTimes(90);
					((RegisterActivity) getActivity()).startTimer(phone);
					getCodeRequest();
				}
				if (((RegisterActivity) getActivity()).getTimes() < 90
						&& ((RegisterActivity) getActivity()).getTimes() > 0) {
					Fragment f = new RegisterFragment2();
					((RegisterActivity) getActivity()).changeRegisterFragment2(
							f, false);
				}

			}

		});

		return v;
	}
	

	private void getCodeRequest() {
		new Thread(new Runnable() { 
			@Override
			public void run() {
		        try {
		        	int random = SendSMS_old.generateVerifykey();
		        	boolean sendResult = SendSMS.sendShortMsg(phone.getText().toString(), random); 
		        	((RegisterActivity) getActivity()).s_random = String.valueOf(random);
		    		((RegisterActivity) getActivity()).s_phone = phone.getText().toString();
		        	if(sendResult == true) {
		        		handleMsg = new Message();
		        		handleMsg.what = 6;
		        	} else {
						handleMsg = new Message();
		        		handleMsg.what = 2;
		        	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        getActivity().runOnUiThread(updateThread);
			}
		}).start();
	}

	Runnable updateThread = new Runnable() {

		@Override
		public void run() {
			// 更新UI
			if (handleMsg.what == 6) {
				isSubmitOk=true;
				Toast.makeText(getActivity(),
						getResources().getString(R.string.register1_tip1),
						Toast.LENGTH_SHORT).show();
				// 切换激活页面
				Fragment f = new RegisterFragment2();
				((RegisterActivity) getActivity()).changeRegisterFragment2(f,
						false);
			} else if (handleMsg.what == 2) {
				phone.setEnabled(true);
				Toast.makeText(getActivity(),
						getResources().getString(R.string.register1_tip2),
						Toast.LENGTH_SHORT).show();
			}
		}

	};
}
