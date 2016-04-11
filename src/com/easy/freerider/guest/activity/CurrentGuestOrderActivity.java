package com.easy.freerider.guest.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.adapter.GuestRouteListAdapter;
import com.easy.freerider.common.activity.AppContext;
import com.easy.freerider.common.activity.AppManager;
import com.easy.freerider.model.GuestRoute;
import com.easy.freerider.model.User;
import com.easy.freerider.util.TaskRequest;
import com.easy.freerider.view.XListView;

public class CurrentGuestOrderActivity extends FragmentActivity implements XListView.IXListViewListener  {
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private TextView detail_title;
	private TextView detail_Back;
	private String d_title_s;
	private static final String TAG = CurrentGuestOrderActivity.class.getSimpleName();
	private XListView mListView;
	private List<GuestRoute> hostTaskList = new ArrayList<GuestRoute>();
	private GuestRouteListAdapter taskAdapter;
	private int mRefreshIndex = 0;   //记录刷新次数，0 代表完全更新，1 代表第一次载入更多，2代表第二次载入更多。。。
	private int position = 0; 
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if((msg.arg1 == 2) || (msg.arg1 == 1)) // means JSONObject array
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {     
							JSONArray array = object.getJSONArray("GuestWayInfo");
							List<GuestRoute> list = new ArrayList<GuestRoute>();
							for (int i = 0; i < array.length(); i++) {
								GuestRoute task = GuestRoute.parse(array.getJSONObject(i));
								list.add(task);
							}
							refreshList(list);
						} else if (object.getInt("Code") == 404) {
		    			    Toast.makeText(CurrentGuestOrderActivity.this, 
		        		    		object.getString("Reason"), 
		    			    		Toast.LENGTH_SHORT).show();	
		    			    refreshList(null);
						}else if (object.getInt("Code") == 500) {
		    			    Toast.makeText(CurrentGuestOrderActivity.this, 
		    			    		getResources().getString(R.string.internal_error), 
		    			    		Toast.LENGTH_SHORT).show();	
		    			    refreshList(null);
						}
					}catch (Exception e) {
						Log.e(TAG, "Handler:" + e.toString());
	    			    Toast.makeText(CurrentGuestOrderActivity.this, 
	    			    		getResources().getString(R.string.internal_error), 
	    			    		Toast.LENGTH_SHORT).show();	
	    			    refreshList(null);
					}
				}
				break;
			case RWSFAIL:
				refreshList(null);
				Toast.makeText(CurrentGuestOrderActivity.this, "数据更新出现错误，请稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			onLoad();
		}
	};
	
	private void refreshList(List<GuestRoute> list) {
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        AppManager.getAppManager().addActivity(this);
        AppManager.getAppManager().finishActivity(GuestPublishActivity.class);
		setContentView(R.layout.activity_guest_cu_order);
		detail_title = (TextView)findViewById(R.id.note_info);
		d_title_s = "当前订单";
		detail_title.setText(d_title_s);
		
        OnClickListener listener0 = null;
        listener0 = new OnClickListener() {
            public void onClick(View v) {
        		switch(v.getId()){
        		case R.id.b_lastpage:
                	finish();
        			break;
        		}
            }
        };
		detail_Back = (TextView)findViewById(R.id.b_lastpage);
		detail_Back.setOnClickListener(listener0);
		
		mListView = (XListView) findViewById(R.id.xlist_view_guest_order);
		if (taskAdapter == null) {
			Log.d(TAG, "taskAdapter is null");
			taskAdapter = new GuestRouteListAdapter(this,R.layout.guest_route_item, hostTaskList);
		}
		initView(mListView);
		
		//query the data and init
		hostRouteQueryTask(mRefreshIndex);;
		
	}
	
	private void initView(View v) {
        mListView = (XListView) v.findViewById(R.id.xlist_view_guest_order);
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
				Intent i = new Intent(CurrentGuestOrderActivity.this, GuestRouteDetailActivity.class);
				GuestRoute guestRoute = (GuestRoute) taskAdapter.getItem(arg2-1);
				i.putExtra("GuestWayId", guestRoute.getGuestWayId());
				startActivity(i);
			}
        	
        	
        });
        
        if(taskAdapter != null){
        	mListView.setAdapter(taskAdapter);
        }
    }
	
	
	public void hostRouteQueryTask(int refreshIndex){
		try{
			TaskRequest request = new TaskRequest(this,mHandler);
			User user=AppContext.getUser(getApplicationContext());
			request.getGuestRouteCurrentReq(user.getMobile(),refreshIndex);
		}catch (Exception e) {
			e.printStackTrace();
		}
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
//		                geneItems();
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
