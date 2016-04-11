package com.easy.freerider.util;

import java.io.File;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.methods.multipart.Part;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpClientHelper {

	private static final int CONNECTION_TIMEOUT = 20000;
	private static final String Tag = "HttpClientHelper";
	private static final int RETRY_TIME=3;
	public HttpClientHelper() {

	}

	/**
	 * Get方式访问
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public static String httpGet(String url, String queryString) throws Exception {
		String responseData = null;

		if (queryString != null && !queryString.equals("")) {
			url += "?" + queryString;
		}
		responseData=httpGet(url);
		return responseData;
	}
	
	/**
	 * Get方式访问
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public static String httpGet(String url) throws Exception {
		String responseData = null;
		
		HttpClient httpClient = new HttpClient();
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		try {
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode != HttpStatus.SC_OK) {
				Log.d(Tag,"HttpGet [2] Method failed: "
						+ httpGet.getStatusLine());
			}
			// Read the response body.
			responseData = httpGet.getResponseBodyAsString();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

	/**
	 * Post方式访问
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public static String httpPost(String url, String queryString) throws Exception {
		String responseData = null;
		HttpClient httpClient = new HttpClient();

		Log.d(Tag,"QHttpClient httpPost [1] url = "+url);
		Log.d(Tag, "querystring:"+queryString);
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter("Content-Type",
				"application/json");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));
		if (queryString != null && !queryString.equals("")) {
			httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString
					.getBytes()));
		}

		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}
			responseData = httpPost.getResponseBodyAsString();
			
			//Log.d(Tag,"QHttpClient httpPost [2] responseData = "+responseData);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	/**
	 * post方式 多播
	 * @param url
	 * @param listParams
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url, List<QParameter> listParams) throws SocketTimeoutException, Exception {
		String responseData = null;
		HttpClient httpClient = new HttpClient();

		Log.d(Tag,"QHttpClient httpPost [1] url = "+url);
		
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter("Content-Type",
				"application/json");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));
		int length = listParams.size();
		Part[] parts=new Part[length];
		int i=0;
		for(QParameter parm:listParams){
			parts[i] = new StringPart(parm.getName(), parm.getValue(), "UTF-8");
			i++;
		}
		httpPost.setRequestEntity(new MultipartRequestEntity(parts,
				httpPost.getParams()));
		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}
			responseData = httpPost.getResponseBodyAsString();
		}catch(SocketTimeoutException e1){
			throw new SocketTimeoutException();
		}catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	
	/**
	 * post方式 多播
	 * @param url
	 * @param listParams
	 * @param 编码方式
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url, List<QParameter> listParams,String encodeType) throws Exception {
		String responseData = null;
		HttpClient httpClient = new HttpClient();

		Log.d(Tag,"QHttpClient httpPost [1] url = "+url);
		
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter("Content-Type",
				"application/json");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));
		int length = listParams.size();
		Part[] parts=new Part[length];
		int i=0;
		for(QParameter parm:listParams){
			parts[i] = new StringPart(parm.getName(), parm.getValue(), encodeType);
			i++;
		}
		httpPost.setRequestEntity(new MultipartRequestEntity(parts,
				httpPost.getParams()));
		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}
			responseData = httpPost.getResponseBodyAsString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	
	/**
	 * post 多播访问
	 * 
	 * @param url
	 *            The remote URL.
	 * @param listParams
	 *            常规的String型参数
	 * @param files
	 *            上传的文件参数 QParameter的value值为文件路径
	 * @return Response string.
	 * @throws Exception
	 */
	public static String httpPostWithFile(String url, List<QParameter> listParams,
			List<QParameter> files) throws Exception {

		String responseData = null;
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter("Content-Type",
		"application/json");
		httpPost.getParams().setParameter("http.socket.timeout",
		new Integer(CONNECTION_TIMEOUT));
		try {
			
			int length = listParams.size() + (files == null ? 0 : files.size());
			Part[] parts = new Part[length];
			int i = 0;
			for (QParameter param : listParams) {
				parts[i++] = new StringPart(param.getName(),
						param.getValue(), "UTF-8");
			}

			for (QParameter param : files) {
				String filePath = param.getValue();
				File file = new File(filePath);
				String name = param.getName();
//				String fileName = file.getName();
				String type = QHttpUtil.getContentType(file.getName());
				
				// image/jpeg - image/png
				FilePart filePart=new FilePart(name, file, type, "UTF-8");
				parts[i++] = filePart;

			}
			httpPost.setRequestEntity(new MultipartRequestEntity(parts,
					httpPost.getParams()));
			

			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				Log.e(Tag,"HttpPost Method failed: "
						+ httpPost.getStatusLine());
				throw new Exception(httpPost.getStatusLine().toString());
			}
			responseData = httpPost.getResponseBodyAsString();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());			
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	
	/**
	 * 获取网络图片
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) throws Exception {
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do{
			try 
			{
				httpClient = new HttpClient();
				httpGet = new GetMethod(url);

				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
				}
		        InputStream inStream = httpGet.getResponseBodyAsStream();
		        bitmap = BitmapFactory.decodeStream(inStream);
		        inStream.close();
		        break;
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				throw new HttpException();
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		return bitmap;
	}
	
	public static InputStream getInputStream(String url){
		InputStream is=null;
		try {
			/*
			 * use the URL to access the request
			 */
			URL version_url=new URL(url);
			URLConnection connection=version_url.openConnection();
			is = connection.getInputStream();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return is;
	}

}
