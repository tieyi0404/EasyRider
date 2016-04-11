package com.easy.freerider.common.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;

import com.easy.freerider.R;
import com.easy.freerider.common.fragment.RegisterFragment1;
import com.easy.freerider.host.activity.HostMainActivity;
import com.easy.freerider.model.User;

public class RegisterActivity extends FragmentActivity {

	private static final String TAG = RegisterActivity.class.getSimpleName();
	public static FragmentManager fm;
	private Timer timer = null;
	private int times = 90;
	public String ActionType;
	public String s_random;
	public String s_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_register);
		ActionType = getIntent().getStringExtra("ActionType");
		fm = getSupportFragmentManager();
		timer = new Timer();
		// 第一次运行，初始化userid 为 null
		User user=AppContext.getUser(getApplicationContext());
		// 切换到主界面
		if (user.getMobile() == null) {
			// 只當容器，主要內容已Fragment呈現
			initFragment(new RegisterFragment1()); // 切换到注册页面
		} else {
			// 切换到主界面
			Intent intent = new Intent();

			intent.setClass(RegisterActivity.this, HostMainActivity.class);
			startActivity(intent);
		}

		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	private void initView() {
		// TODO Auto-generated method stub

	}

	// 切換Fragment
	public void changeFragment(Fragment f) {
		changeFragment(f, false);
	}

	// 初始化Fragment(FragmentActivity中呼叫)
	public void initFragment(Fragment f) {
		changeFragment(f, true);
	}

	public void changeFragment(Fragment f, boolean init) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.activity_register, f);
		if (!init)
			ft.addToBackStack(null);
		ft.commit();
	}

	public void changeRegisterFragment2(Fragment f, boolean init) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.resigterfragment1, f); // fragment replace
												// 只能替换relativelayout
		Log.d(TAG, "yyyy");
		if (!init)
			ft.addToBackStack(null);
		ft.commit();
	}

	public void changeRegisterFragment3(Fragment f, boolean init) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragment_resigter2, f); // fragment replace
												// 只能替换relativelayout
		Log.d(TAG, "yyyy");
		if (!init)
			ft.addToBackStack(null);
		ft.commit();
	}

	public void changeRegisterFragment1(Fragment f, boolean init) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragment_resigter2, f); // fragment replace
												// 只能替换relativelayout
		if (!init)
			ft.addToBackStack(null);
		ft.commit();
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void startTimer(final EditText e) {
		e.setEnabled(false);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {

				runOnUiThread(new Runnable() { // UI thread
					@Override
					public void run() {
						if (times <= 0) {
							times = 90;
							e.setEnabled(true);
							if (timer != null) {
								timer.cancel();
								timer=null;
							}
						} else {
							times--;
						}
					}
				});
			}
		};
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(task, 1000, 1000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	public void stopTimer() {
		this.timer.cancel();
		this.timer = null;
	}

	public void restartTimer(EditText e) {
		e.setEnabled(true);
		Timer timer = new Timer();
		this.timer = timer;
	}

}
