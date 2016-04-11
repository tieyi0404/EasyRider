package com.easy.freerider.guest.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.AppContext;
import com.easy.freerider.common.activity.AppManager;
import com.easy.freerider.common.activity.LoginActivity;
import com.easy.freerider.common.fragment.MyFragmentTabManager;
import com.easy.freerider.guest.fragment.GuestOrderFragment;
import com.easy.freerider.guest.fragment.GuestPropertyFragment;
import com.easy.freerider.guest.fragment.GuestRouteQueryFragment;
import com.easy.freerider.host.activity.HostMainActivity;
import com.easy.freerider.host.activity.HostPublishActivity;
import com.easy.freerider.model.User;
import com.easy.freerider.net.AppUpgradeService;
import com.easy.freerider.util.MySlipSwitch;
import com.easy.freerider.util.TaskRequest;
import com.easy.freerider.util.MySlipSwitch.OnSwitchListener;


public class GuestMainActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnClickListener, OnTouchListener {

/*	private RadioGroup group;*/
	private static final int RESPSUCESS = 1;
	private static final int RWSFAIL = 2;
	private static final int DOWN_EXCEPTION = 3;
	private TabHost tabGuest;
	public static FragmentManager fm;
	private RelativeLayout layout;
	public static final String TAG="MainActivity";
	public static final String TAB_HOME="tabHome";
	public static final String TAB_QUERY="tab_query";
	public static final String TAB_PROPERTY="tab_property";
	public static final String TAB_MORE="tab_more";
	private static final int GO_HOST = 1;
	private static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 0;
	private static final int FLAG_ACTIVITY_NO_HISTORY = 0;
	public String[] tabs = new String[]{TAB_HOME,TAB_QUERY,TAB_PROPERTY};
	private MyFragmentTabManager tabManager;
	private RadioButton[] mRadioButtons;
	private TextView role;
	private TextView headsetting;
	private Button btnCurrentOrder;
	private Button btnHistoryOrder;
	private Button btnLogout;
	private MySlipSwitch slipswitch_MSL;
	
	// declare pop window
	private PopupWindow popupWindow; 
	private static boolean isExit = false;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Log.i("info", TAG +"==handleMessage()");
			switch (msg.what) {
			case DOWN_EXCEPTION:
				Toast.makeText(GuestMainActivity.this, 
    		    		"获取更新信息失败，请稍后再试", 
			    		Toast.LENGTH_SHORT).show();	
				break;
			case RESPSUCESS:
				if((msg.arg1 == 2) || (msg.arg1 == 1)) // means JSONObject array
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if(object.getInt("Code") == 200)
						{
							if (object.getString("Result").equals("NOK")) {     
								String versionname = object.getString("VersionName");
								String udpateinfo = object.getString("UpdateInfo");
								final String downloadUrl = object.getString("VersionUrl");
				        		LayoutInflater inflater = (LayoutInflater)getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				        	  	View v = inflater.inflate(R.layout.update_info, null);
				        	  	TextView packageName = (TextView) v.findViewById(R.id.packageName);
				        	  	TextView upInfoText = (TextView) v.findViewById(R.id.updateinfo);
				        	  	packageName.setText(versionname);
				        	  	upInfoText.setText(udpateinfo.replace(";","\n"));    	  	
								new android.app.AlertDialog.Builder(GuestMainActivity.this)
				                .setTitle(R.string.check_new_version)
				                .setView(v)
				                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				                    @Override
				                    public void onClick(DialogInterface dialog, int which) {
				                        Intent intent = new Intent(GuestMainActivity.this, AppUpgradeService.class);
				                        intent.putExtra("downloadUrl", downloadUrl);
				                        startService(intent);
				                    }
				                })
				                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				                    @Override
				                    public void onClick(DialogInterface dialog, int which) {

				                    }
				                })
				                .create()
				                .show();
							}
						}  else if (object.getInt("Code") == 404) {
						} else{
		    			    Toast.makeText(GuestMainActivity.this, 
		    			    		getResources().getString(R.string.internal_error), 
		    			    		Toast.LENGTH_SHORT).show();	
						}
					}catch (Exception e) {
						Log.e(TAG, "Handler:" + e.toString());
	    			    Toast.makeText(GuestMainActivity.this, 
	    			    		getResources().getString(R.string.internal_error), 
	    			    		Toast.LENGTH_SHORT).show();	
					}
				}
				break;
			case RWSFAIL:
				Toast.makeText(GuestMainActivity.this, "获取更新信息失败，请稍后再试", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
		AppManager.getAppManager().addActivity(this);
		fm = getSupportFragmentManager(); 
		tabGuest = (TabHost) findViewById(R.id.tabhost);
		tabGuest.setup();
		tabManager = new MyFragmentTabManager(this, tabGuest,
				android.R.id.tabcontent);
		tabManager.addTab(
				tabGuest.newTabSpec(TAB_HOME).setIndicator(TAB_HOME),
				GuestOrderFragment.class, null);
		tabManager.addTab(
				tabGuest.newTabSpec(TAB_QUERY).setIndicator(TAB_QUERY),
				GuestRouteQueryFragment.class, null);
		tabManager.addTab(
				tabGuest.newTabSpec(TAB_PROPERTY).setIndicator(TAB_PROPERTY),
				GuestPropertyFragment.class, null);
		
		initMainTabsBottomUI();
		User user=AppContext.getUser(getApplicationContext());
		role = (TextView)findViewById(R.id.head_user);
		if(user.getMobile() == null) {
			role.setText("登  陆");
		} else {
			role.setText("乘  客");
		}
		role.setOnClickListener(this);

		
		headsetting=(TextView)findViewById(R.id.head_setting);
		headsetting.setText("发布信息");
		headsetting.setOnClickListener(this);
		
        slipswitch_MSL = (MySlipSwitch)findViewById(R.id.main_myslipswitch);
        slipswitch_MSL.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
        slipswitch_MSL.setSwitchState(false);
        slipswitch_MSL.setOnSwitchListener(new OnSwitchListener() {
			
			@Override
			public void onSwitched(boolean isSwitchOn) {
				// TODO Auto-generated method stub
					AppContext.setRole(GO_HOST, getApplication());
					startActivity(new Intent(GuestMainActivity.this,HostMainActivity.class));
			}
		});
        
		//do the update here
		if(AppContext.ISCheckVersion == false){
			versionUpdateRequest();
			AppContext.ISCheckVersion = true;
		}
	}
	


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		User user=AppContext.getUser(getApplication());
		switch (v.getId()) {
		case R.id.head_user:
			if(user.getMobile() == null) {
                Intent intent = new Intent();
                //Intent传递参数
                intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setClass(GuestMainActivity.this, LoginActivity.class);
                GuestMainActivity.this.startActivity(intent);
			} else {
				getPopupWindow(); 
				// 这里是位置显示方式,在按钮的左下角 
				popupWindow.showAsDropDown(v); 
			}
			break;
		case R.id.head_setting:
			if(user.getMobile() == null) {
                Intent intent = new Intent();
                //Intent传递参数
                intent.setFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setClass(GuestMainActivity.this, LoginActivity.class);
                GuestMainActivity.this.startActivity(intent);
			} else {
                Intent intent = new Intent();
                //Intent传递参数
                intent.setFlags(FLAG_ACTIVITY_NO_HISTORY);
                intent.setClass(GuestMainActivity.this, GuestPublishActivity.class);
                GuestMainActivity.this.startActivity(intent);
			}
			break;
		}
	}
	
	private void initMainTabsBottomUI() {
		RadioGroup localRadioGroup = (RadioGroup) findViewById(R.id.main_radio);
		this.mRadioButtons = new RadioButton[3];
		int i = 0;
		while (i < 3) {
			String str = "radio_button" + i;
			this.mRadioButtons[i] = (RadioButton) localRadioGroup
					.findViewWithTag(str);
			this.mRadioButtons[i].setOnCheckedChangeListener(this);
			i += 1;
		}
		mRadioButtons[0].setChecked(true);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		for (int i = 0; i < mRadioButtons.length; i++) {
			if (buttonView == mRadioButtons[i]&&isChecked) {
				tabGuest.setCurrentTabByTag(tabs[i]);
			}
		}
	}
	
	/** 
	* 创建PopupWindow 
	*/ 
	protected void initPopuptWindow() { 
		
		DisplayMetrics dm = new DisplayMetrics();
		//获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		// TODO Auto-generated method stub 
		// 获取自定义布局文件pop.xml的视图 
		View popupWindow_view = getLayoutInflater().inflate(R.layout.pop_left_window, null, false); 
		// 创建PopupWindow实例,200,150分别是宽度和高度 
		popupWindow = new PopupWindow(popupWindow_view, (int) (screenWidth * 0.9) , 120, true);   
		
		// 设置SelectPicPopupWindow弹出窗体的宽  
		popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);  
		// 设置SelectPicPopupWindow弹出窗体的高  
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);  
		// 设置SelectPicPopupWindow弹出窗体可点击  
		popupWindow.setFocusable(true);  
		popupWindow.setOutsideTouchable(true);  
		// 刷新状态  
		popupWindow.update();
        // 设置SelectPicPopupWindow的View  
		popupWindow.setContentView(popupWindow_view);  
		// 实例化一个ColorDrawable颜色为半透明  
		ColorDrawable dw = new ColorDrawable(0000000000);  
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
		popupWindow.setBackgroundDrawable(dw);
		// 设置动画效果 
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		
		//点击其他地方消失 
		popupWindow_view.setOnTouchListener(this);
		
		btnCurrentOrder = (Button)popupWindow_view.findViewById(R.id.currentOrder);
		btnHistoryOrder = (Button)popupWindow_view.findViewById(R.id.historyOrder);
		btnLogout = (Button)popupWindow_view.findViewById(R.id.logout_sys);
		
        OnClickListener btnOnclickListener = null;
        btnOnclickListener = new OnClickListener() {
            public void onClick(View v) {
        		switch(v.getId()){
        		case R.id.currentOrder:
        			startActivity(new Intent(GuestMainActivity.this, CurrentGuestOrderActivity.class));
        			break;
        		case R.id.historyOrder:
        			startActivity(new Intent(GuestMainActivity.this, HistoryGuestOrderActivity.class));
        			break;
        		case R.id.logout_sys:
        			logout();
        			break;
        		}
            }
        };
        btnCurrentOrder.setOnClickListener(btnOnclickListener);
        btnHistoryOrder.setOnClickListener(btnOnclickListener);
        btnLogout.setOnClickListener(btnOnclickListener);
        //popupWindow_view.setOnClickListener(listener0);
	}
		
	/*** 
	* 获取PopupWindow实例 
	*/ 
	private void getPopupWindow() { 
		if (null != popupWindow) { 
			popupWindow.dismiss(); 
			return; 
		} else { 
			initPopuptWindow(); 
		} 
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (popupWindow != null && popupWindow.isShowing()) { 
			popupWindow.dismiss(); 
			popupWindow = null; 
			return false;
		} 
		else {  
			popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, 0, 0);  
			return true;  
		}  

	}  
	
    private void exit() {
        if(!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                
                @Override
                public void run() {
                    isExit = false;
                }
            },2000);
        } else {
        	AppManager.getAppManager().finishAllActivity();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    public  void changeGuestFillPropertyFragment(Fragment f, boolean init){  
        FragmentTransaction ft = fm.beginTransaction();  
        ft.replace(R.id.guest_propertyfragment, f);        //fragment replace 只能替换relativelayout 
        if(!init)  
            ft.addToBackStack(null);  
        ft.commit();  
    }
    
    public  void changeGuestProperyFragment(Fragment f, boolean init){  
        FragmentTransaction ft = fm.beginTransaction();  
        ft.replace(R.id.guest_fillinfragment, f);        //fragment replace 只能替换relativelayout 
        if(!init)  
            ft.addToBackStack(null);  
        ft.commit();  
    } 
    
    
    public void logout(){
    	/* clear all the user data except the role */
		SharedPreferences prefs = this.getApplicationContext()
				.getSharedPreferences("user", 0);
		prefs.edit().clear().commit();
		startActivity(new Intent(GuestMainActivity.this,GuestMainActivity.class));
    }
    
	private void versionUpdateRequest(){  
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
			int curVersionCode = info.versionCode;
			try {
				TaskRequest taskRequest = new TaskRequest(GuestMainActivity.this, mHandler);
				taskRequest.updateVersionReq(curVersionCode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message msg = new Message();
				msg.what = DOWN_EXCEPTION;
				mHandler.sendMessage(msg);
			}
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			Message msg = new Message();
			msg.what = RWSFAIL;
			mHandler.sendMessage(msg);
		}	
    }

}
