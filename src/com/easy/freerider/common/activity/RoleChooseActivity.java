package com.easy.freerider.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.easy.freerider.R;
import com.easy.freerider.guest.activity.GuestMainActivity;
import com.easy.freerider.host.activity.HostMainActivity;

public class RoleChooseActivity extends Activity implements OnClickListener {

	private Button btnChooseHost;
	private Button btnChooseGuest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/* 设置为无标题栏并且为全屏模式 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().finishActivity(WelcomeActivity.class);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_role_choose);
		
		
		btnChooseHost = (Button) findViewById(R.id.choose_host);
		btnChooseGuest = (Button) findViewById(R.id.choose_guest);
		
		btnChooseGuest.setOnClickListener(this);
		btnChooseHost.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId())
		{
		case R.id.choose_host:
			AppContext.setRole(1, this);
			intent = new Intent(this, HostMainActivity.class);
			startActivity(intent);
			break;
		case R.id.choose_guest:
			AppContext.setRole(2, this);
			intent = new Intent(this, GuestMainActivity.class);
			startActivity(intent);
			break;
		}
		this.finish();
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
        	finish();
        }
        return false;
    }
	
}
