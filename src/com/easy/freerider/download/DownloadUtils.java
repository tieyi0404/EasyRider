package com.easy.freerider.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.easy.freerider.common.activity.AppContext;

import android.os.Environment;

public class DownloadUtils {
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int DATA_TIMEOUT = 80000;
    private final static int DATA_BUFFER = 8192;

    public interface DownloadListener {
        public void downloading(int progress);
        public void downloaded();
    }

    public static long download(String urlStr, File dest, boolean append, DownloadListener downloadListener) throws Exception {
        int downloadProgress = 0;
        long remoteSize = 0;
        int currentSize = 0;
        long totalSize = -1;

        if(!append && dest.exists() && dest.isFile()) {
            dest.delete();
        }

        if(append && dest.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dest);
                currentSize = fis.available();
            } catch(IOException e) {
                throw e;
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
        }

        HttpGet request = new HttpGet(urlStr);

        if(currentSize > 0) {
            request.addHeader("RANGE", "bytes=" + currentSize + "-");
        }

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, DATA_TIMEOUT);
        HttpClient httpClient = new DefaultHttpClient(params);

        InputStream is = null;
        FileOutputStream os = null;
        try {
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
                remoteSize = response.getEntity().getContentLength();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if(contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    is = new GZIPInputStream(is);
                }
//                System.out.println("dest is:" + dest);
                //dest 与 Environment.getExternalStorageDirectory().getPath() + File.separator, ApplicationData.mDownloadfile
                //相同
              //将内容写道SD卡中，使用 File（path , name）形式， openFileOutput 不能包含路径，因此不能用，它只能写道data/data/packagname
				File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator+ AppContext.ApkCacheDir+File.separator,  getFileName(urlStr));
				//openFileOutput()方法的第一参数用于指定文件名称，不能包含路径分隔符“/” ，如果文件不存在，Android 会自动创建它。
//				fos = context.openFileOutput(dir+md5 + FILE_EXT,
//						Context.MODE_WORLD_READABLE);
				if(!file.exists()){
					file.createNewFile();
				}
                os = new FileOutputStream(file, append);
                byte buffer[] = new byte[DATA_BUFFER];
                int readSize = 0;
                int i =0;
                while((readSize = is.read(buffer)) > 0){
                	i++;
                    os.write(buffer, 0, readSize);
                    os.flush();
                    totalSize += readSize;
                    if(i%10 == 0 || totalSize == remoteSize){ //减少10倍的进度条更新频率， 否则卡
	                    if(downloadListener!= null){
	                        downloadProgress = (int) (totalSize*100/remoteSize);
	                        downloadListener.downloading(downloadProgress);
	                    }
                    }
                }
                if(totalSize < 0) {
                    totalSize = 0;
                }
            }
        } finally {
            if(os != null) {
                os.close();
            }
            if(is != null) {
                is.close();
            }
        }

        if(totalSize < 0) {
            throw new Exception("Download file fail: " + urlStr);
        }

        if(downloadListener!= null){
            downloadListener.downloaded();
        }

        return totalSize;
    }
    
    private static String getFileName(String str){
         String name = str.substring(str.lastIndexOf(File.separator)+1, str.length());
         return name;
    	
    }
}
