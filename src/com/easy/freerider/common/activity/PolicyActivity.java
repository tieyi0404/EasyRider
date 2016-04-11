package com.easy.freerider.common.activity;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.easy.freerider.R;

public class PolicyActivity extends FragmentActivity {
	
	private TextView confirm_t;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);        
        try {  
        	//Return an AssetManager instance for your application's package  
            InputStream is = getAssets().open("sfProtocol.txt");  
            int size = is.available();  
            // Read the entire asset into a local byte buffer.  
            byte[] buffer = new byte[size];  
            is.read(buffer);  
            is.close();  
            // Convert the buffer into a string.  
            String text = new String(buffer, "GB2312");  
            // Finally stick the string into the text view.  
            TextView tv = (TextView) findViewById(R.id.text);  
            tv.setText(text);  
            confirm_t = (TextView) findViewById(R.id.confirm_t);  
            
            confirm_t.setOnClickListener(new OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub);
    				finish();
    			}
    			});
        } catch (IOException e) {  
            // Should never happen!  
            throw new RuntimeException(e);  
        }  

    }

}
