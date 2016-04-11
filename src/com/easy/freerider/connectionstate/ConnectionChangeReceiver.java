package com.easy.freerider.connectionstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	//0 means not ok  1 means ok
	private static int netStatus = 0;
	
	@Override
	public void onReceive( Context context, Intent intent ) {
		// TODO Auto-generated method stub
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );   
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
		NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
        	netStatus = 0;
        }else {
        	netStatus = 1;
        }
	}

}
