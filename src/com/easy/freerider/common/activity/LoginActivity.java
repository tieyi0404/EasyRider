package com.easy.freerider.common.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.Window;

import com.easy.freerider.R;
import com.easy.freerider.common.fragment.LoginFragment;

public class LoginActivity extends FragmentActivity {
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	public static FragmentManager fm; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_login);
		fm = getSupportFragmentManager();  
            initFragment(new LoginFragment());  //切换到注册页面
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*getMenuInflater().inflate(R.menu.jop, menu);*/
		return true;
	}
	
	private void initView() {
		// TODO Auto-generated method stub

	}

	// 切換Fragment  
    public  void changeFragment(Fragment f){  
        changeFragment(f, false);  
    }  
    // 初始化Fragment(FragmentActivity中呼叫)  
    public  void initFragment(Fragment f){  
        changeFragment(f, true);  
    }  
    public  void changeFragment(Fragment f, boolean init){  
        FragmentTransaction ft = fm.beginTransaction();  
        ft.replace(R.id.activity_login, f);  
        if(!init)  
            ft.addToBackStack(null);  
        ft.commit();  
    }  

}
