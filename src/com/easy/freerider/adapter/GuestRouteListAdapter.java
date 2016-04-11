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
import com.easy.freerider.model.GuestRoute;

public class GuestRouteListAdapter extends ArrayAdapter<GuestRoute> {
	private List<GuestRoute> items = null;
	private Context mContext = null;
	private DisplayMetrics metric;
	
	private LayoutInflater inflater;// 视图容器
	
	public GuestRouteListAdapter(Context context, int resource, List<GuestRoute> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = objects;
		metric = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
	}
	
	static class ViewHolder { // 自定义控件集合
		public TextView sourceAddr;
		public TextView destAddr;
		public TextView go_time;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder holder = null;
		 if (convertView == null) {                   
			 holder=new ViewHolder();                  
		     convertView = LayoutInflater.from(mContext).inflate(R.layout.guest_route_item, null);
		     holder.sourceAddr = (TextView)convertView.findViewById(R.id.sourceAddr);
		     holder.destAddr = (TextView)convertView.findViewById(R.id.destAddr);
		     holder.go_time = (TextView)convertView.findViewById(R.id.gotime);
	         convertView.setTag(holder);    
		 }else {                  
		     holder = (ViewHolder)convertView.getTag();
		 }   
		 
		 holder.sourceAddr.setText(items.get(position).getSourceAddr());
		 holder.destAddr.setText(items.get(position).getDestAddr());
		 holder.go_time.setText(items.get(position).getExpectGoTime());
		 return convertView;
	}
	

}

