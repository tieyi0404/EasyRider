package com.easy.freerider.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	
	public  static final String IS_FIRST_FILE = "first";
	private static final String IS_FIRST_IN = "isFirstIn";
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	//isFirst
	public void setIsFirst(boolean flag) {
		// TODO Auto-generated method stub
		editor.putBoolean(IS_FIRST_IN, flag);
		editor.commit();
	}

	public boolean getIsFirst() {
		return sp.getBoolean(IS_FIRST_IN, true);
	}
}
