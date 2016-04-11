package com.easy.freerider.host.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.host.fragment.HostRouteQueryFragment;
import com.easy.freerider.util.FormatTimeMethod;
import com.easy.freerider.util.TaskRequest;

public class HostRouteDetailActivity extends FragmentActivity {
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	
	private static final String TAG = HostRouteQueryFragment.class.getSimpleName();
	
	private TextView detail_title;
	private TextView detail_Back;
	private TextView detail_call_t;
	private TextView detail_call_m;
	private String d_title_s;
	
	private TextView fromTo;
	private TextView sourceAddr;
	private TextView destAddr;
	private TextView bypassAddr;
	private TextView seatNumber;
	private TextView linePrice;
	private TextView goTime;
	private TextView pubTime;
	private TextView addiInfo;
	private TextView hostPhone;
	private TextView hostName;
	private TextView carType;
	private TextView carNumber;
	
	private String phoneNumber;
	private String hostWayId;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object_userinfo = (JSONObject) msg.obj;
					try {
						if (object_userinfo.getInt("Code") == 200) {   
							JSONObject object =  object_userinfo.getJSONObject("HostWayInfo");
							fromTo.setText(object.getString("SourceAddr") + " ---> " + object.getString("DestAddr"));
							sourceAddr.setText(object.getString("SourceAddr"));
							destAddr.setText(object.getString("DestAddr"));
							goTime.setText(FormatTimeMethod.formateTime(object.getString("StartTime")));
							pubTime.setText(FormatTimeMethod.formatTime_full(object.getString("PubTime")));
							bypassAddr.setText(object.getString("PassAddr"));
							linePrice.setText(object.getString("LinePrice"));
							seatNumber.setText(object.getString("SeatNumber"));
							if(object.getString("AdditionalInfo").equals(""))
							{
								addiInfo.setText("无");
							} else {
								addiInfo.setText(object.getString("AdditionalInfo"));
							}
							hostPhone.setText(object.getString("HostPhone"));
							phoneNumber = object.getString("HostPhone");
							carType.setText(object.getString("CarType"));
							carNumber.setText(object.getString("CarNumber"));
							if(object.getString("UserSex").equals("1"))
							{
								hostName.setText(object.getString("UserName") + "先生");
							}
							else {
								hostName.setText(object.getString("UserName") + "女士");
							}
						}
						}catch (Exception e) {	
							e.printStackTrace();
						}
				}	
				break;
			case RWSFAIL:
				Toast.makeText(HostRouteDetailActivity.this, "数据更新出现错误，请稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);
		detail_title = (TextView) findViewById(R.id.note_info);
		d_title_s = "个人车主";
		detail_title.setText(d_title_s);
	
		Intent i = getIntent();	
		
		sourceAddr = (TextView) findViewById(R.id.dt_textview1);
		destAddr   = (TextView) findViewById(R.id.dt_textview2);
		bypassAddr = (TextView) findViewById(R.id.dt_textview3);
		seatNumber = (TextView) findViewById(R.id.dt_textview4);
		linePrice = (TextView)findViewById(R.id.dt_textview6);
		goTime  =   (TextView) findViewById(R.id.dt_textview5);
		pubTime =   (TextView) findViewById(R.id.ft_textview1);
		addiInfo =  (TextView) findViewById(R.id.at_info);
		hostPhone = (TextView) findViewById(R.id.ct_textview2);
		hostName  = (TextView) findViewById(R.id.ct_textview1);
		carType   = (TextView) findViewById(R.id.ct_textview4);
		carNumber = (TextView) findViewById(R.id.ct_textview3);
		fromTo = (TextView)findViewById(R.id.ft_fromto);
		
		hostWayId = i.getStringExtra("HostWayId");
		routeInfoDetailQueryTask(hostWayId);
				
        OnClickListener clickListener = null;
        clickListener = new OnClickListener() {
            public void onClick(View v) {
        		switch(v.getId()){
        		case R.id.b_lastpage:
                	finish();
        			break;
        		case R.id.t_phone:
        			Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
        			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			startActivity(intent);
        			break;
        		case R.id.t_mail:
        			Uri uri = Uri.parse("smsto:" + phoneNumber);
        			Intent intent_mail = new Intent(Intent.ACTION_SENDTO,uri);
        			String messageBody = "您好，我对您在顺路上发布的“"  + fromTo.getText().toString() + 
        					"”很感兴趣，我希望获得能获得更多的信息。";
        			intent_mail.putExtra("sms_body", messageBody);
        			startActivity(intent_mail);
        			break;
        		}
            }
        };
		
		detail_Back = (TextView)findViewById(R.id.b_lastpage);
		detail_Back.setOnClickListener(clickListener);
		detail_call_t = (TextView)findViewById(R.id.t_phone);
		detail_call_t.setOnClickListener(clickListener);
		detail_call_m = (TextView)findViewById(R.id.t_mail);
		detail_call_m.setOnClickListener(clickListener);

	}
	
	
	public void routeInfoDetailQueryTask(final String hostWayId){
		try{
			TaskRequest request = new TaskRequest(HostRouteDetailActivity.this,mHandler);
			request.getHostRouteDetailReq(hostWayId);
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
