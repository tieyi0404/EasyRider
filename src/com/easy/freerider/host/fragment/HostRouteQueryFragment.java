package com.easy.freerider.host.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.adapter.GuestRouteListAdapter;
import com.easy.freerider.guest.activity.GuestRouteDetailActivity;
import com.easy.freerider.model.GuestRoute;
import com.easy.freerider.util.TaskRequest;
import com.easy.freerider.view.XListView;


public class HostRouteQueryFragment extends Fragment implements OnTouchListener, XListView.IXListViewListener{
    private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private XListView mListView;
	private static final String TAG = HostRouteQueryFragment.class.getSimpleName();
	private GuestRouteListAdapter taskAdapter;
	private List<GuestRoute> hostTaskList = new ArrayList<GuestRoute>();
	private int mRefreshIndex = 0;   //记录刷新次数，0 代表完全更新，1 代表第一次载入更多，2代表第二次载入更多。。。
	private int position = 0; 
	
	private EditText sourceAdd;
	private EditText decAdd;
	private EditText goTime;
	private Button  query_btn;

	private PopupWindow popupWindow; 
	private TextView queryInfo;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RESPSUCESS:
				if (msg.arg1 == 1) // means JSONObject
				{
					JSONObject object = (JSONObject) msg.obj;
					try {
						if (object.getInt("Code") == 200) {   
							JSONArray array = object.getJSONArray("GuestWayInfo");
							List<GuestRoute> list = new ArrayList<GuestRoute>();
							for (int i = 0; i < array.length(); i++) {
								GuestRoute task = new GuestRoute();
								task = GuestRoute.parse(array.getJSONObject(i));
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
				Toast.makeText(getActivity(), "数据更新出现错误，请稍后重试", Toast.LENGTH_SHORT)
						.show();
				 refreshList(null);
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
        taskAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_query, null);	
		queryInfo = (TextView)view.findViewById(R.id.query_iv);
		
		queryInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPopupWindow();
		        popupWindow.showAsDropDown(v.findViewById(R.id.query_iv), 0,5);
			}
		});
		
	    /* PullToRefreshListView is for refresh when pulling the screen */
		mListView = (XListView) view.findViewById(R.id.g_q_xlist_view);
		if (taskAdapter == null) {
			Log.d(TAG, "taskAdapter is null");
			taskAdapter = new GuestRouteListAdapter(getActivity(),R.layout.guest_route_item, hostTaskList);
		}
		initView(mListView);
		
		return view;
	}
	
    private void initView(View v) {
        mListView = (XListView) v.findViewById(R.id.g_q_xlist_view);
        mListView.setDividerHeight(0);
        mListView.setDivider(null);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				/* fragment jump to activity */
				Intent i = new Intent(getActivity(), GuestRouteDetailActivity.class);
				GuestRoute guestRoute = (GuestRoute) taskAdapter.getItem(arg2-1);
				i.putExtra("GuestWayId", guestRoute.getGuestWayId());
				i.putExtra("GuestPhone", guestRoute.getGuestPhone());
				i.putExtra("SourceAddr", guestRoute.getSourceAddr());
				i.putExtra("DestAddr", guestRoute.getDestAddr());
				i.putExtra("ExpectGoTime", guestRoute.getExpectGoTime());
				startActivity(i);
			}
        	
        	
        });
        
        if(taskAdapter != null){
        	mListView.setAdapter(taskAdapter);
        }
    }

	public HostRouteQueryFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public void hostRouteQueryTask(int refreshIndex){
		
		if(goTime.length() == 0) {
			Toast.makeText(getActivity(), "查询日期不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try{
			TaskRequest request = new TaskRequest(getActivity(),mHandler);
			request.guestRouteQueryReq(sourceAdd.getText().toString(), 
					decAdd.getText().toString(), goTime.getText().toString(), refreshIndex);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /** 
	* 创建PopupWindow 
	*/ 
	protected void initPopuptWindow() { 
		DisplayMetrics dm = new DisplayMetrics();
		//获取屏幕信息
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		// TODO Auto-generated method stub 
		// 获取自定义布局文件pop.xml的视图 
		View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.fragment_query_pop, null, false); 
		// 创建PopupWindow实例,200,150分别是宽度和高度 
		popupWindow = new PopupWindow(popupWindow_view, (int) (screenWidth * 0.9) , 120, true);   
		
		// 设置SelectPicPopupWindow弹出窗体的宽  
		popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);  
		// 设置SelectPicPopupWindow弹出窗体的高  
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);  
		// 设置SelectPicPopupWindow弹出窗体可点击  
		popupWindow.setFocusable(true);  
		popupWindow.setOutsideTouchable(true);  
		// 刷新状态  
		popupWindow.update();
        // 设置SelectPicPopupWindow的View  
		popupWindow.setContentView(popupWindow_view);  
		// 实例化一个ColorDrawable颜色为半透明  
		ColorDrawable dw = new ColorDrawable(0000000000);  
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
		popupWindow.setBackgroundDrawable(dw);
		// 设置动画效果 
		popupWindow.setAnimationStyle(R.style.AnimationShow);
		//popupWindow.showAtLocation(popupWindow_view,Gravity.CENTER,0,0);		
		//点击其他地方消失 
		popupWindow_view.setOnTouchListener(this);
		
		sourceAdd = (EditText)popupWindow_view.findViewById(R.id.query_from);
		decAdd = (EditText)popupWindow_view.findViewById(R.id.query_to);
		goTime = (EditText)popupWindow_view.findViewById(R.id.query_date);	
		query_btn = (Button)popupWindow_view.findViewById(R.id.query_btn);
		
		SimpleDateFormat currentData=new SimpleDateFormat("yyyy-MM-dd");  
		String date = currentData.format(new java.util.Date());  
		goTime.setText(date);
		
		final Calendar c_date = Calendar.getInstance();
		goTime.setOnTouchListener(new OnTouchListener() {
			//按住和松开的标识
			int touch_flag=0;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				touch_flag++;
				if(touch_flag==2){
			           DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
		                    @Override
		                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		                    	c_date.set(year, monthOfYear, dayOfMonth);
		                    	goTime.setText(DateFormat.format("yyy-MM-dd", c_date));
		                    	goTime.setSelection(goTime.getText().toString().length());
		                    	touch_flag = 0;
		                    }
		                }, c_date.get(Calendar.YEAR), c_date.get(Calendar.MONTH), c_date.get(Calendar.DAY_OF_MONTH));
		                dialog.show();
					

				}
				return false;
			}
		});
		
		query_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				System.out.println(sourceAdd.getText().toString() + decAdd.getText().toString() + goTime.getText().toString());
				if(goTime.getText().toString().equals(""))
				{
					Toast.makeText(getActivity(), "查询日期不能为空", Toast.LENGTH_SHORT).show();
				}
				else {
					mRefreshIndex = 0;
					hostRouteQueryTask(mRefreshIndex);
					getPopupWindow();
				}
				
			}
		});
		
		
	}
		
	/*** 
	* 获取PopupWindow实例 
	*/ 
	private void getPopupWindow() { 
		if (null != popupWindow) { 
			popupWindow.dismiss(); 
			return; 
		} else { 
			initPopuptWindow(); 
		} 
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (popupWindow != null && popupWindow.isShowing()) { 
			popupWindow.dismiss(); 
			popupWindow = null; 
			return false;
		} 
		else {  
			popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, 0, 0);  
			return true;  
		}  

	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
    	Log.i(TAG, "onStart");

		super.onStart();
		
		//trigger the click once
		queryInfo.performClick();
		
		//默认查询一次数据
		try{
			TaskRequest request = new TaskRequest(getActivity(),mHandler);
			request.guestRouteQueryReq(sourceAdd.getText().toString(), 
					decAdd.getText().toString(), goTime.getText().toString(), mRefreshIndex);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
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
        mListView.setRefreshTime(goTime.getText().toString());
    }

	    private String getTime() {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
	    }

}
