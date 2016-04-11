package com.easy.freerider.adapter;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.host.activity.CurrentHostOrderActivity;
import com.easy.freerider.model.HostRoute;
import com.easy.freerider.util.TaskRequest;

public class HostCurrentListAdapter extends ArrayAdapter<HostRoute>  {
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private List<HostRoute> items = null;
	private Context mContext = null;
	private DisplayMetrics metric;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if((msg.arg1 == 2) || (msg.arg1 == 1)) // means JSONObject array
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {
							//very important, need to clear the list
							((CurrentHostOrderActivity)mContext).refreshList(null);
							((CurrentHostOrderActivity)mContext).getmListView().autoRefresh();
						} else {
							Toast.makeText(mContext, "设置客满失败", Toast.LENGTH_SHORT)
							.show();
						}
					}catch (Exception e) {
	    			    Toast.makeText(mContext, 
	    			    		mContext.getResources().getString(R.string.internal_error), 
	    			    		Toast.LENGTH_SHORT).show();	
					}
				}
				break;
			case RWSFAIL:
				Toast.makeText(mContext, "数据更新出现错误，请稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
		}
		}
	};
	
	
	public HostCurrentListAdapter(Context context, int resource, List<HostRoute> objects){
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = objects;
		metric = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
	}
	
	static class ListItemView { // 自定义控件集合
		public TextView sourceAddr;
		public TextView destAddr;
		public TextView bypassAddr;
		public TextView go_time;
		public TextView pub_time;
		public TextView site_full;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView holder = null;
		 if (convertView == null) {                   
			 holder = new ListItemView();                  
		     convertView = LayoutInflater.from(mContext).inflate(R.layout.host_current_item, null);
		     holder.sourceAddr = (TextView)convertView.findViewById(R.id.sourceAddr);
		     holder.destAddr = (TextView)convertView.findViewById(R.id.destAddr);
		     holder.go_time = (TextView)convertView.findViewById(R.id.gotime);
		     holder.bypassAddr = (TextView)convertView.findViewById(R.id.bypassAddr);
		     holder.site_full = (TextView)convertView.findViewById(R.id.guestfull);
		     
/*		     holder.pub_time = (TextView)convertView.findViewById(R.id.g_pubtime);*/
	         convertView.setTag(holder);    
		 }else {                  
		     holder = (ListItemView)convertView.getTag();
		 }   
		 
		 HostRoute hostRoute = (HostRoute) this.getItem(position);
		 String hostwayid = hostRoute.getHostWayId().toString();	 
	     
	     holder.site_full.setOnClickListener(new sfButtonListener(hostwayid));	     
		 
		 holder.sourceAddr.setText(items.get(position).getSourceAddr());
		 holder.destAddr.setText(items.get(position).getDestAddr());
		 holder.go_time.setText(items.get(position).getGoTime());
		 holder.bypassAddr.setText(items.get(position).getPassAddr());
/*		 holder.pub_time.setText(items.get(position).getPubTime());*/
		 return convertView;
	}
	
	public void setGuestFullReqTask(String hostWayId) {
		try{
			TaskRequest request = new TaskRequest(mContext,mHandler);
			request.setGuestFullReq(hostWayId);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//主要的部分是以下代码，
    class sfButtonListener implements OnClickListener {
        private String hostwayId;
        //获取所点击项的id
        sfButtonListener(String hostWayId) {
        	hostwayId = hostWayId;
        }
        
        //获取当前项的id并判断是否与所点击项的id相同
        public void onClick(View v) {

			// TODO Auto-generated method stub
			
			View diaView=View.inflate(mContext, R.layout.dialog_bu, null);
			final Dialog dialog=new Dialog(mContext,R.style.dialog);
			dialog.setContentView(diaView);
			dialog.show(); 
			
			Button full_sure = (Button)diaView.findViewById(R.id.full_sure);
			Button full_cancel = (Button)diaView.findViewById(R.id.full_cancel);
			
			full_sure.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
                	setGuestFullReqTask(hostwayId);
                	dialog.dismiss();
				}
			});
			
			full_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
                	dialog.dismiss();	
				}
				
			});				
		
        }
    }
}

