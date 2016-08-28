package ls.main.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageGet{
	
	public static boolean isOver  ;
	
	private static Bitmap bm;
	
	public Bitmap getImg(){
		return bm;
	}
	

	
	public static void saveImg(final String path,final Context context){
		isOver = false;
		Thread t = new Thread(){
			

			@Override
			public void run() {
				
				try {
					//2.把网址封装成一个url对象
					URL url = new URL(path);
					//3.获取客户端和服务器的连接对象，此时还没有建立连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					//4.对连接对象进行初始化
					//设置请求方法，注意大写
					conn.setRequestMethod("GET");
					//设置连接超时
					conn.setConnectTimeout(5000);
					//设置读取超时
					conn.setReadTimeout(5000);
					//5.发送请求，与服务器建立连接
					conn.connect();
					//如果响应码为200，说明请求成功
					if(conn.getResponseCode() == 200){
						//获取服务器响应头中的流，流里的数据就是客户端请求的数据
						InputStream is = conn.getInputStream();
						
						//读取服务器返回的流里的数据，把数据写到本地文件，缓存起来
						//String name = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."));
						File file = new File(context.getCacheDir(), getFileName(path));
						FileOutputStream fos = new FileOutputStream(file);
						byte[] b = new byte[1024];
						int len = 0;
						while((len = is.read(b)) != -1){
							fos.write(b, 0, len);
						}
						fos.close();
						
						bm = BitmapFactory.decodeFile(file.getAbsolutePath());
						
						isOver = true;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	public static String getFileName(String path){
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}
}
