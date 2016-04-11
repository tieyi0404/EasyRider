package com.easy.freerider.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easy.freerider.R;
import com.easy.freerider.model.HostRoute;

public class HostRouteListAdapter extends ArrayAdapter<HostRoute>  {
	private List<HostRoute> items = null;
	private Context mContext = null;
	private DisplayMetrics metric;
	
	private LayoutInflater inflater;// 视图容器
	
	public HostRouteListAdapter(Context context, int resource, List<HostRoute> objects){
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
			 holder=new ListItemView();                  
		     convertView = LayoutInflater.from(mContext).inflate(R.layout.host_route_item, null);
		     holder.sourceAddr = (TextView)convertView.findViewById(R.id.g_sourceAddr);
		     holder.destAddr = (TextView)convertView.findViewById(R.id.g_destAddr);
		     holder.go_time = (TextView)convertView.findViewById(R.id.g_gotime);
		     holder.bypassAddr = (TextView)convertView.findViewById(R.id.g_bypassAddr);
		     holder.pub_time = (TextView)convertView.findViewById(R.id.g_pubtime);
	         convertView.setTag(holder);    
		 }else {                  
		     holder = (ListItemView)convertView.getTag();
		 }   
		 
		 holder.sourceAddr.setText(items.get(position).getSourceAddr());
		 holder.destAddr.setText(items.get(position).getDestAddr());
		 holder.go_time.setText(items.get(position).getGoTime());
		 holder.bypassAddr.setText(items.get(position).getPassAddr());
		 holder.pub_time.setText(items.get(position).getPubTime());
		 return convertView;
	}
}

