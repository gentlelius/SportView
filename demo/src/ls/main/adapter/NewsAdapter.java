

package ls.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.netease.nim.demo.R;

import java.util.List;

import ls.main.bean.NewsList;
import ls.main.utils.MyTimeUtils;

public class NewsAdapter  extends BaseAdapter {

	private List<NewsList.T1411113472760Bean> data;
	private Context context;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	public NewsAdapter(Context context,List<NewsList.T1411113472760Bean> list){
		this.context = context;
		this.data = list;
		requestQueue = Volley.newRequestQueue(context);
		final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageCache imageCache = new ImageCache() {
            @Override  
            public void putBitmap(String key, Bitmap value) {  
                lruCache.put(key, value);  
            }  
  
            @Override  
            public Bitmap getBitmap(String key) {  
                return lruCache.get(key);  
            }  
        };  

		imageLoader = new ImageLoader(requestQueue,imageCache);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public NewsList.T1411113472760Bean getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.content_list,null);
			viewHolder = new ViewHolder();
			viewHolder.img = (NetworkImageView) convertView.findViewById(R.id.iv_img);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NewsList.T1411113472760Bean bean = data.get(position);
		String img_url = bean.getImgsrc();
		viewHolder.img.setTag("url");
		viewHolder.img.setImageUrl(img_url, imageLoader);
		String timeStr = bean.getPtime();
		long time = MyTimeUtils.getLongFormat(timeStr);
		viewHolder.tv_time.setText(MyTimeUtils.getModelTime(time));
		viewHolder.tv_title.setText(bean.getTitle());
		com.nostra13.universalimageloader.core.ImageLoader imageLoader;
		return convertView;
	}
	
	
	class ViewHolder{
		NetworkImageView img;
		TextView tv_title;
		TextView tv_time;
	}
	
	

}
