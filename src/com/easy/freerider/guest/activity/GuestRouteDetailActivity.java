package com.easy.freerider.guest.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.easy.freerider.guest.fragment.GuestRouteQueryFragment;
import com.easy.freerider.util.FormatTimeMethod;
import com.easy.freerider.util.TaskRequest;

public class GuestRouteDetailActivity extends FragmentActivity {
	
	
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	
	
	private static final String TAG = GuestRouteQueryFragment.class.getSimpleName();
	
	private TextView detail_title;
	private TextView detail_Back;
	private TextView detail_call_t;
	private TextView detail_call_m;
	private String d_title_s;
	
	private TextView fromTo;
	private TextView sourceAddr;
	private TextView destAddr;
	private TextView expectGoTime;
	private TextView pubTime;
	private TextView addiInfo;
	private TextView guestPhone;
	private TextView guestName;
	
	private String phoneNumber;
	private String guestWayId;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object_userinfo = (JSONObject) msg.obj;
					try {
						if (object_userinfo.getInt("Code") == 200) {   
							JSONObject object =  object_userinfo.getJSONObject("GuestWayInfo");
							fromTo.setText(object.getString("SourceAddr") + "------->" + object.getString("DestAddr"));
							sourceAddr.setText(object.getString("SourceAddr"));
							destAddr.setText(object.getString("DestAddr"));
							pubTime.setText(FormatTimeMethod.formatTime_full(object.getString("PubTime")));
							expectGoTime.setText(FormatTimeMethod.formateTime(object.getString("ExpectGoTime")));
							if(object.getString("AdditionalInfo").equals(""))
							{
								addiInfo.setText("无");
							} else {
								addiInfo.setText(object.getString("AdditionalInfo"));
							}
							phoneNumber = object.getString("GuestPhone");
							guestPhone.setText(object.getString("GuestPhone"));
							if(object.getString("UserSex").equals("1"))
							{
								guestName.setText(object.getString("GuestName") + "先生");
							}
							else {
								guestName.setText(object.getString("GuestName") + "女士");
							}
						}
						}catch (Exception e) {	
							e.printStackTrace();
						}
				}	
				break;
			case RWSFAIL:
				Toast.makeText(GuestRouteDetailActivity.this, "数据更新出现错误，请稍后重试", Toast.LENGTH_SHORT)
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
		setContentView(R.layout.guest_route_detail);
		detail_title = (TextView) findViewById(R.id.note_info);
		d_title_s = "乘客";
		detail_title.setText(d_title_s);
		
		Intent i = getIntent();	
		
		fromTo = (TextView)findViewById(R.id.g_ft_fromto);
		sourceAddr = (TextView) findViewById(R.id.gr_sourceAddr);
		destAddr   = (TextView) findViewById(R.id.gr_destAddr);
		expectGoTime  =   (TextView) findViewById(R.id.gr_gotime);
		pubTime =   (TextView) findViewById(R.id.hr_pub_time);
		addiInfo =  (TextView) findViewById(R.id.gr_addiInfo);
		guestPhone = (TextView) findViewById(R.id.gt_phonenumber);
		guestName  = (TextView) findViewById(R.id.gt_gtperson);
		
		
/*		fromTo.setText(i.getStringExtra("SourceAddr") + "------->" +  i.getStringExtra("DestAddr"));
		sourceAddr.setText(i.getStringExtra("SourceAddr") );
		destAddr.setText( i.getStringExtra("DestAddr"));
		expectGoTime.setText( i.getStringExtra("ExpectGoTime"));
		guestPhone.setText(i.getStringExtra("GuestPhone"));
		phoneNumber = i.getStringExtra("GuestPhone");*/
		
		guestWayId =  i.getStringExtra("GuestWayId");
		guestInfoQueryTask(guestWayId);
		
        OnClickListener listener0 = null;
        listener0 = new OnClickListener() {
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
		detail_Back.setOnClickListener(listener0);
		detail_call_t = (TextView)findViewById(R.id.t_phone);
		detail_call_t.setOnClickListener(listener0);
		detail_call_m = (TextView)findViewById(R.id.t_mail);
		detail_call_m.setOnClickListener(listener0);
	}
	

	public void guestInfoQueryTask(String guestWayId){
		try{
			TaskRequest request = new TaskRequest(GuestRouteDetailActivity.this, mHandler);
			request.getGuestRouteDetailReq(guestWayId);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
