package com.easy.freerider.widget;


import com.easy.freerider.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class NormalProgressDialog extends Dialog {
	TextView messageTextView;
	public NormalProgressDialog(Context context) {
		super(context,R.style.MyDialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.progress_dialog);
		messageTextView = (TextView) findViewById(R.id.progress_dialog_message);
	}
	/**
	 * 设置提示信息.
	 * @param msg
	 */
	public void setMessage(CharSequence msg){
		messageTextView.setText(msg);
	}

}
