package com.easy.freerider.net;



/*
 * 用在cloudbackuplistfragement中，下载apk，更新每个对应的 progressbar
 * */
import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.easy.freerider.R;
import com.easy.freerider.common.activity.AppContext;
import com.easy.freerider.download.DownloadUtils;
import com.easy.freerider.download.DownloadUtils.DownloadListener;


public class ApkDownloadService extends Service{
	private static final String TAG = ApkDownloadService.class.getSimpleName();

    public static final int APP_VERSION_LATEST = 0;
    public static final int APP_VERSION_OLDER = 1;

    private String mdownloadUrl = null;
    private int mIndex = 0;

    private File destDir = null;
    private File destFile = null;

    private static final int DOWNLOAD_FAIL = -1;
    private static final int DOWNLOAD_SUCCESS = 0;
    
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWNLOAD_SUCCESS:
                Toast.makeText(getApplicationContext(), R.string.app_upgrade_download_sucess, Toast.LENGTH_LONG).show();
                install(destFile);
                break;
            case DOWNLOAD_FAIL:
                Toast.makeText(getApplicationContext(), R.string.app_upgrade_download_fail, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
            }
        }

    };
    
    
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mdownloadUrl = intent.getStringExtra("downloadUrl");
        mIndex = intent.getIntExtra("index", 0);
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//        	System.out.println("onStartCommand apk 存储路径：" + Environment.getExternalStorageDirectory().getPath() + ApplicationData.mDownloadPath);
            File destdir = new File(Environment.getExternalStorageDirectory().getPath() + AppContext.ApkCacheDir);
            if (!destdir.exists()) {  
                //如果路径不存在， 创建之。
            	destdir.mkdirs();
            }
                   
            new ApkDownloadThread(mdownloadUrl, mIndex).start();
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
                
            
        }else {
        	
            return super.onStartCommand(intent, flags, startId);
        }

    }

    class ApkDownloadThread extends Thread{
    	private String dlUrl = null;
    	private int index = 0;
    	
    	
        public ApkDownloadThread(String dlUrl, int index) {
			super();
			this.dlUrl = dlUrl;
			this.index = index;
		}


		@Override
        public void run() {
            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
                if (destDir == null) {
                    destDir = new File(Environment.getExternalStorageDirectory().getPath() + AppContext.ApkCacheDir);
                }
//                System.out.println("run apk 存储路径：" + Environment.getExternalStorageDirectory().getPath() + ApplicationData.mDownloadPath);
                if (destDir.exists() || destDir.mkdirs()) {
                    destFile = new File(destDir.getPath() +File.separator+ getFileName(dlUrl));
                    if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                        if(destFile.delete() == false ){
//                        	System.out.println("删除文件失败");
                        }
                    }
                    try {
                        DownloadUtils.download(dlUrl, destFile, false, new DownloadListener() {
							
							@Override
							public void downloading(int progress) {
								// TODO Auto-generated method stub
								//Log.i(TAG, String.valueOf(progress));
								Message message = new Message(); 
//								Log.i(TAG, "send index :" + String.valueOf(index));
							    message.what = index;   // 哪一行数据需要修改
							    message.arg1 = AppContext.ProgressBarUpdate;
							    message.arg2 = progress;
							    //((ApplicationData)getApplicationContext()).getProgressBarHandler().sendMessage(message);
							}
							
							@Override
							public void downloaded() {
								// TODO Auto-generated method stub
								Log.i(TAG, "download complete");
								Message message = new Message(); 
							    message.what = index;   // 哪一行数据需要修改
							    message.arg1 = AppContext.ProgressBarComplete;
							    //((ApplicationData)getApplicationContext()).getProgressBarHandler().sendMessage(message);
							}
						});
                    } catch (Exception e) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = DOWNLOAD_FAIL;
                        mHandler.sendMessage(msg);
                        e.printStackTrace();
                    }
                }
            }
            stopSelf();
        }
    }

    public boolean checkApkFile(String apkFilePath) {
        boolean result = false;
        try{
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result =  false;
            } else {
                result =  true;
            }
        } catch(Exception e) {
            result =  false;
            e.printStackTrace();
        }
        return result;
    }

    public void install(File apkFile){
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopSelf();
	}
    
    private  String getFileName(String str){
        String name = str.substring(str.lastIndexOf(File.separator)+1, str.length());
        return name;
   	
   }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	

    
}
