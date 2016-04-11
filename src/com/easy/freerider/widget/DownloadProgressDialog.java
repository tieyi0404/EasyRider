package com.easy.freerider.widget;

import com.easy.freerider.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadProgressDialog extends AlertDialog {
	TextView messageTextView,rateTextView;
	ProgressBar bar;
	static final String TAG=DownloadProgressDialog.class.getSimpleName();
	public DownloadProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_progressbar, null);
		messageTextView = (TextView) contentView.findViewById(R.id.dialog_progressbar_message);
		rateTextView=(TextView) contentView.findViewById(R.id.dialog_progressbar_rate);
		bar=(ProgressBar) contentView.findViewById(R.id.dialog_progressbar_progress);
		setView(contentView);
	}

	public void SetAlertMessage(CharSequence message){
		messageTextView.setText(message);
	}

	public void SetProgress(int rate){
		bar.setProgress(rate);
	}

	public void SetRate(CharSequence message){
		rateTextView.setText(message);
	}
}
