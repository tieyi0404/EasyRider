package com.easy.freerider.util;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/* judge the reponse, if the dataString is null, then means failed. */
public class AsyncHttpReqInterface {

	private Context context;
	private static String rootUrl;
	private static int port;
	private static String rootUrlHead;
	private final int RESPSUCESS = 1;
	private final int RWSFAIL= 2;
	private Handler mHandler;

	public AsyncHttpReqInterface(Context context, Handler handler) {
		this.context = context;
		this.mHandler = handler;
		rootUrl = AppConfig.GetServerRootAddress(context);
		port = AppConfig.GetServerPort(context);
		rootUrlHead = rootUrl + ":" + String.valueOf(port);
	}

	/*
	 * reqName: e.g. guestregisterrequest hostwayreleaserequest parameters: from
	 * JSONObject.toString mHandler : the handler for the activity objName: e.g.
	 * for query result array: HostWayInfo for other insert or update operation:
	 * null
	 */
	public void PostReq(String reqName, String parameters) {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams requestParams = new RequestParams();
		StringEntity entity;
		try {
			StringBuilder urlBuilder = new StringBuilder(rootUrlHead);
			urlBuilder.append("/api/").append(reqName);
			entity = new StringEntity(parameters, "utf-8");
			entity.setContentType("application/json");
			client.post(context, urlBuilder.toString(), entity,
					"application/json", new JsonHttpResponseHandler() {
						// 返回JSONArray对象 | JSONObject对象
						@Override
						public void onSuccess(int statusCode,
								org.apache.http.Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							if (statusCode == 200) {
								Message msg = new Message();
								msg.obj = response;
								msg.arg1 = 1; // 1 means object
								msg.what = RESPSUCESS;
								mHandler.sendMessage(msg);
							} 
						}

						@Override
						public void onSuccess(int statusCode,
								org.apache.http.Header[] headers,
								JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							if (statusCode == 200) {
								// 存储数组变量
								// 获取具体的一个JSONObject对象
								Message msg = new Message();
								msg.obj = response;
								msg.arg1 = 2; // 2 means array
								msg.what = RESPSUCESS;
								mHandler.sendMessage(msg);
							}
						}

						/**
						 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
						 */
						@Override
						public void onFailure(int statusCode,
								org.apache.http.Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what = RWSFAIL;
							mHandler.sendMessage(msg);
						}

						/**
						 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
						 */
						@Override
						public void onFailure(int statusCode,
								org.apache.http.Header[] headers,
								java.lang.Throwable throwable,
								JSONObject response) {
							Message msg = new Message();
							msg.what = RWSFAIL;
							mHandler.sendMessage(msg);
						}

					});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/*
	 * reqName: e.g. guestregisterrequest hostwayreleaserequest parameters:
	 * xxx=xxx&xxxx=xxx&xxx=xxx JSONObject.toString mHandler : the handler for
	 * the activity objName: e.g. for query result array: HostWayInfo for other
	 * insert or update operation: null
	 */
	public void GetReq(String reqName, String Parameters) {
		AsyncHttpClient client = new AsyncHttpClient();
		StringBuilder urlBuilder = new StringBuilder(rootUrlHead);
		urlBuilder.append("/api/").append(reqName).append("?")
				.append(Parameters);
		RequestParams requestParams = new RequestParams();
		requestParams.add("Content-Type", "application/json");
		client.get(context, urlBuilder.toString(), requestParams,
				new JsonHttpResponseHandler() {
					// 返回JSONArray对象 | JSONObject对象
					@Override
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						if (statusCode == 200) {
							Message msg = new Message();
							msg.obj = response;
							msg.what = RESPSUCESS;
							msg.arg1 = 1;
							mHandler.sendMessage(msg);
						} 
					}

					@Override
					public void onSuccess(int statusCode,
							org.apache.http.Header[] headers, JSONArray response) {
						super.onSuccess(statusCode, headers, response);
						if (statusCode == 200) {
							// 存储数组变量
							// 获取具体的一个JSONObject对象
							Message msg = new Message();
							msg.obj = response;
							msg.arg1 = 2; // 2 means array
							msg.what = RESPSUCESS;
							mHandler.sendMessage(msg);
						} 
					}

					/**
					 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
					 */
					@Override
					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = RWSFAIL;
						mHandler.sendMessage(msg);
					}

					/**
					 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
					 */
					@Override
					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							java.lang.Throwable throwable, JSONObject response) {
						Message msg = new Message();
						msg.what = RWSFAIL;
						mHandler.sendMessage(msg);
					}

				});
	}
}
