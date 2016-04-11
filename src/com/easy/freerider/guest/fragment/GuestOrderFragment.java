package com.easy.freerider.guest.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.adapter.HostRouteListAdapter;
import com.easy.freerider.host.activity.HostRouteDetailActivity;
import com.easy.freerider.model.HostRoute;
import com.easy.freerider.util.TaskRequest;
import com.easy.freerider.view.XListView;

public class GuestOrderFragment extends Fragment implements XListView.IXListViewListener {
	
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private XListView mListView;
	private static final String TAG = GuestRouteQueryFragment.class.getSimpleName();
	private List<HostRoute> hostTaskList = new ArrayList<HostRoute>();
	private HostRouteListAdapter taskAdapter;
	private int mRefreshIndex = 0;   //记录刷新次数，0 代表完全更新，1 代表第一次载入更多，2代表第二次载入更多。。。
	private int position = 0; 
	
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if  ((msg.arg1 == 2) || (msg.arg1 == 1)) // means JSONObject array
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {     
							JSONArray array = object.getJSONArray("HostWayInfo");
							List<HostRoute> list = new ArrayList<HostRoute>();
							for (int i = 0; i < array.length(); i++) {
								HostRoute task = HostRoute.parse(array.getJSONObject(i));
								list.add(task);
							}
							refreshList(list);
						} else if (object.getInt("Code") == 404) {
		    			    Toast.makeText(getActivity(), 
		    			    		object.getString("Reason"), 
		    			    		Toast.LENGTH_SHORT).show();	
		    			    refreshList(null);
						}else if (object.getInt("Code") == 500) {
		    			    Toast.makeText(getActivity(), 
		    			    		getResources().getString(R.string.internal_error), 
		    			    		Toast.LENGTH_SHORT).show();	
		    			    refreshList(null);
						}
					}catch (Exception e) {
						Log.e(TAG, "Handler:" + e.toString());
	    			    Toast.makeText(getActivity(), 
	    			    		getResources().getString(R.string.internal_error), 
	    			    		Toast.LENGTH_SHORT).show();	
	    			    refreshList(null);
					}
				}
				break;
			case RWSFAIL:
				refreshList(null);
				Toast.makeText(getActivity(), "数据更新出现错误，请稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			onLoad();
		}
	};
	
	private void refreshList(List<HostRoute> list) {
		hostTaskList.clear();
		if(list!=null){
			hostTaskList.addAll(list);
		}
		if(mRefreshIndex != 0)
		{
			taskAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.myorder, null);
		Log.d(TAG, "onCreateView");	
		
		mListView = (XListView) view.findViewById(R.id.xlist_view_host_order);
		if (taskAdapter == null) {
			Log.d(TAG, "taskAdapter is null");
			taskAdapter = new HostRouteListAdapter(getActivity(),R.layout.host_route_item, hostTaskList);
		}
		initView(mListView);
		
		//query the data and init
		hostRouteQueryTask(mRefreshIndex);
        
		return view;
	}
	
	
	  private void initView(View v) {
		  	mListView = (XListView) v.findViewById(R.id.xlist_view_host_order);
	        mListView.setDividerHeight(0);
	        mListView.setDivider(null);
	        mListView.setPullRefreshEnable(true);
	        mListView.setPullLoadEnable(true);
	        mListView.setAutoLoadEnable(true);
	        mListView.setXListViewListener(this);
	        mListView.setRefreshTime(getTime());
	        mListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					/* fragment jump to activity */
					Intent i = new Intent(getActivity(), HostRouteDetailActivity.class);
					HostRoute hostRoute = (HostRoute) taskAdapter.getItem(arg2-1);
					i.putExtra("HostWayId", hostRoute.getHostWayId());
					startActivity(i);
				}
	        	
	        	
	        });
	        
	        if(taskAdapter != null){
	        	mListView.setAdapter(taskAdapter);
	        }
	    }
	
	
	public void hostRouteQueryTask(int refreshIndex){
		try{
			SharedPreferences prefs = getActivity().getSharedPreferences("user", 0);
			System.out.println("harry--"+prefs.getString("position", null));
			TaskRequest request = new TaskRequest(getActivity(),mHandler);
			request.hostRouteQueryReq(prefs.getString("position", null),null,getTime(), refreshIndex);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public GuestOrderFragment() {
		// TODO Auto-generated constructor stub
	}
	
	
	   @Override
		public void onStart() {
			// TODO Auto-generated method stub
	    	Log.i(TAG, "onStart");

			super.onStart();
			
			if(taskAdapter != null){
	        	mListView.setSelectionFromTop(position, 0);
	        }
		}

		@Override
	    public void onRefresh() {
	        mHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	mRefreshIndex = 0;
	            	hostRouteQueryTask(mRefreshIndex);
	                
	            }
	        }, 0);
	    }

	    @Override
	    public void onLoadMore() {
	        mHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
//	                geneItems();
	            	mRefreshIndex++;
	            	hostRouteQueryTask(mRefreshIndex);
	            }
	        }, 0);
	    }


	    private void onLoad() {
	        mListView.stopRefresh();
	        mListView.stopLoadMore();
	        mListView.setRefreshTime(getTime());
	    }

	    private String getTime() {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
	    }
}
