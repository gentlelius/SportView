package ls.main.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.netease.nim.demo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import ls.main.utils.DisplayUtils;

public class ImageBrowserAdapter extends PagerAdapter {

	private Activity context;
	private List<String> data;
	private ArrayList<View> picViews;
	
	private ImageLoader mImageLoader;

	public ImageBrowserAdapter(Activity context, List<String> data) {
		this.context = context;
		this.data = data;
		this.mImageLoader = ImageLoader.getInstance();
		initImgs();
	}

	private void initImgs() {
		picViews = new ArrayList<View>();
		
		for(int i=0; i<data.size(); i++) {
			// 填充显示图片的页面布局
			View view = View.inflate(context, R.layout.item_image_browser, null);
			picViews.add(view);
		}
	}

	@Override
	public int getCount() {
		if(data.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return data.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		int index = position % data.size();
		View view = picViews.get(index);
		final ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
		String url = data.get(index);
		
//		String url = picUrl.isShowOriImag() ? picUrl.getOriginal_pic() : picUrl.getBmiddle_pic();
		
		mImageLoader.loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				float scale = (float) loadedImage.getHeight() / loadedImage.getWidth();
				
				int screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);
				int screenHeightPixels = DisplayUtils.getScreenHeightPixels(context);
				int height = (int) (screenWidthPixels * scale); 
				
				if(height < screenHeightPixels) {
					height = screenHeightPixels;
				}
					
				LayoutParams params = iv_image_browser.getLayoutParams();
				params.height = height;
				params.width = screenWidthPixels;
				
				iv_image_browser.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View) object);
		int index = position % data.size();
		View view = picViews.get(index);
		container.removeView(view);
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public String getPic(int position) {
		return data.get(position % data.size());
	}
	
	public Bitmap getBitmap(int position) {
		Bitmap bitmap = null;
		View view = picViews.get(position % picViews.size());
		ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
		Drawable drawable = iv_image_browser.getDrawable();
		if(drawable != null && drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bitmap = bd.getBitmap();
		}
		
		return bitmap;
	}
}