

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

import ls.main.bean.Like;
import ls.main.utils.MyTimeUtils;


public class LikeAdapter  extends BaseAdapter {

	private List<Like> data;
	private Context context;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	public LikeAdapter(Context context,List<Like> list){
		this.context = context;
		this.data = list;
		reverse(data);
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

	private void reverse(List<Like> data) {
		for (int i=0,j=data.size()-1;i<j;i++,j--){
			Like temp = data.get(i);
			data.set(i,data.get(j)) ;
			data.set(j,temp);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Like getItem(int position) {
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

		ViewHolder2 viewHolder = null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.content_list,null);
			viewHolder = new ViewHolder2();
			viewHolder.img = (NetworkImageView) convertView.findViewById(R.id.iv_img);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder2) convertView.getTag();
		}
		Like bean = data.get(position);
		System.out.println(bean.toString());
		String img_url = bean.getImg_url();
		viewHolder.img.setTag("url");
		viewHolder.img.setImageUrl(img_url, imageLoader);
		String timeStr = bean.getTime();
		long time = MyTimeUtils.getLongFormat(timeStr);
		viewHolder.tv_time.setText(MyTimeUtils.getModelTime(time)+"\t"+timeStr);
		viewHolder.tv_title.setText(bean.getTitle());
		return convertView;
	}
	
	
	class ViewHolder2{
		NetworkImageView img;
		TextView tv_title;
		TextView tv_time;
	}
	
	

}
