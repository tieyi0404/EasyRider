package com.easy.freerider.service;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class InstallAPKBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String fileAbsPath=intent.getStringExtra("fileAbsPath");
		// TODO Auto-generated method stub
		Intent i2 = new Intent(Intent.ACTION_VIEW);
		i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i2.setDataAndType(Uri.fromFile(new File(fileAbsPath)),
				"application/vnd.android.package-archive");
		context.startActivity(i2);
	}

}
