package com.easy.freerider.guest.activity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.AppManager;
import com.easy.freerider.model.GuestRoute;
import com.easy.freerider.util.TaskRequest;

public class GuestPublishActivity extends FragmentActivity {
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	public static FragmentManager fm;
	private TextView detail_title;
	private TextView pub_bt_text;
	private String d_title_s;
	
	private EditText sourceadd = null;
	private EditText decadd = null;
	private EditText addinfo = null;
	private TextView contact_name;
	private TextView contact_phone;
	
	private Spinner p_site_time = null;
	private ArrayAdapter timeAdapter = null;
	private int posttime = 0;
	
	private EditText p_date;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {    
							Toast.makeText(GuestPublishActivity.this, 
									getResources().getString(R.string.public_success), 
									Toast.LENGTH_SHORT).show();
			       		 	startActivity(new Intent(GuestPublishActivity.this,CurrentGuestOrderActivity.class));
						} else {
							Toast.makeText(GuestPublishActivity.this, 
									object.getString("Reason"), 
									Toast.LENGTH_SHORT).show();
						}
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
	    			    Toast.makeText(GuestPublishActivity.this, 
	    			    		getResources().getString(R.string.internal_error), 
	    			    		Toast.LENGTH_SHORT).show();	
						break;
				}
				}
				break;
			case RWSFAIL:
	       		Toast.makeText(GuestPublishActivity.this, getResources().getString(R.string.register_tip15), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest_publish);
		AppManager.getAppManager().addActivity(this);
		detail_title = (TextView) findViewById(R.id.note_info);
		d_title_s = "填写信息";
		detail_title.setText(d_title_s);
		
		SharedPreferences prefs = this.getApplicationContext()
				.getSharedPreferences("user", 0);
		String mobile = prefs.getString("mobile", null);
		String username = prefs.getString("name", null);
		
		contact_name = (TextView)findViewById(R.id.pub_ct_name_show);
		contact_phone = (TextView)findViewById(R.id.pub_ct_phone_show);
		if(username != null) 
		{
			contact_name.setText(username);
			contact_phone.setText(mobile);
		}
		sourceadd = (EditText)findViewById(R.id.dt_from_show);
		decadd = (EditText)findViewById(R.id.dt_to_show);
		addinfo = (EditText)findViewById(R.id.pub_edit);
		p_date = (EditText)findViewById(R.id.tt_date_show);
		
		final Calendar c_date = Calendar.getInstance();
		p_date.setOnTouchListener(new OnTouchListener() {
			//按住和松开的标识
			int touch_flag=0;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				touch_flag++;
				if(touch_flag==2){
			           DatePickerDialog dialog = new DatePickerDialog(GuestPublishActivity.this, new DatePickerDialog.OnDateSetListener() {
		                    @Override
		                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		                    	c_date.set(year, monthOfYear, dayOfMonth);
		                    	p_date.setText(DateFormat.format("yyyy-MM-dd", c_date));
		                    	p_date.setSelection(p_date.getText().toString().length());
		                    	touch_flag = 0;
		                    }
		                }, c_date.get(Calendar.YEAR), c_date.get(Calendar.MONTH), c_date.get(Calendar.DAY_OF_MONTH));
		                dialog.show();
					

				}
				return false;
			}
		});
		
		
		p_site_time = (Spinner)findViewById(R.id.dt_time_show);
		timeAdapter =  ArrayAdapter.createFromResource(this, R.array.fillin_time, android.R.layout.simple_spinner_item);
		timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		p_site_time.setAdapter(timeAdapter);
		//添加事件Spinner事件监听 
		p_site_time.setOnItemSelectedListener(new SpinnerTimeSelectedListener());
		p_site_time.setVisibility(View.VISIBLE);
		
        OnClickListener listener0 = null;
        listener0 = new OnClickListener() {
            public void onClick(View v) {
        		switch(v.getId()){
        		case R.id.b_lastpage:
                	finish();
        			break;
        		case R.id.pub_bt:
        			pubGuestRoute();
        			break;
        		}
            }
        };
		
        pub_bt_text = (TextView) findViewById(R.id.b_lastpage);
        pub_bt_text.setOnClickListener(listener0);
        
        pub_bt_text = (TextView) findViewById(R.id.pub_bt);
        pub_bt_text.setOnClickListener(listener0);

	}
	
	public void pubGuestRoute() {
		SharedPreferences prefs = this.getApplicationContext()
				.getSharedPreferences("user", 0);
		String mobile = prefs.getString("mobile", null);
		
		String sourceAddr = sourceadd.getText().toString();
		if(sourceAddr.length() == 0||sourceAddr.length()>10) {
			Toast.makeText(this, "起始地址不能为空 或者超过10个字符", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String destAddr = decadd.getText().toString();
		if(destAddr.length() == 0||destAddr.length()>10){
			Toast.makeText(this, "目的地址不能为空 或者超过10个字符", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String date = p_date.getText().toString();
		if( date.length() == 0){
			Toast.makeText(this, "日期不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String additionalInfo = addinfo.getText().toString();
		if(additionalInfo.length()>50){
			Toast.makeText(this, "附加信息不能超过50个字符", Toast.LENGTH_SHORT).show();
			return;
		}
		TaskRequest request = new TaskRequest(this,mHandler);
		try {
			GuestRoute g_route = new GuestRoute();			
			g_route.setGuestPhone(mobile);
			g_route.setSourceAddr(sourceAddr);
			g_route.setDestAddr(destAddr);
			g_route.setExpectGoTime(date + " " + String.valueOf(posttime));
			g_route.setAdditionalInfo(additionalInfo);
			request.guestRouteRelaseReq(g_route);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
    
	//解析XML
    class SpinnerTimeSelectedListener implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
           // sex.setText(ageAdapter.getItem(arg2));

        	posttime = arg2 + 1;
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}
