package com.easy.freerider.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.easy.freerider.R;
import com.easy.freerider.guest.activity.GuestMainActivity;
import com.easy.freerider.host.activity.HostMainActivity;
import com.easy.freerider.util.SharePreferenceUtil;

public class WelcomeActivity extends Activity {
	
	private final String TAG = "Activity_welcome";
	private static final int GO_USER_FARST_PAGE = 1;
	private static final int GO_ROLE = 2;
	private static final long DELAY_MILLIS = 2000;
	private boolean isFirstIn = false;
	private SharePreferenceUtil sp;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GO_USER_FARST_PAGE:
				/*Toast.makeText(FirstActivity.this, "receive message", Toast.LENGTH_SHORT).show();*/
				goUserFirstPage();
				break;
			case GO_ROLE:
				goRolePage();
				sp.setIsFirst(false);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 设置为无标题栏并且为全屏模式 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_welcome);
		sp = new SharePreferenceUtil(this, SharePreferenceUtil.IS_FIRST_FILE);
		isFirstIn = sp.getIsFirst();
		if (isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_ROLE, 0);
		} else {
		//set the message exit 2 second
			mHandler.sendEmptyMessageDelayed(GO_USER_FARST_PAGE, DELAY_MILLIS);
		}
	}
	
	protected void goUserFirstPage() {
		//根据角色创建对应的activity
		Intent intent;
		if(AppContext.getRole(this) == 1)
		{
			//车主
			intent = new Intent(this, HostMainActivity.class);
			startActivity(intent);
		}
		else if(AppContext.getRole(this) == 2) {	
			//乘客
			intent = new Intent(this, GuestMainActivity.class);
			startActivity(intent);
		} else {
			intent = new Intent(this, GuestMainActivity.class);
			startActivity(intent);
		}
		this.finish();
	}
	
	protected void goRolePage() {
		Intent intent = new Intent();
		intent.setClass(this, RoleChooseActivity.class);
		startActivity(intent);

	}
	
}
