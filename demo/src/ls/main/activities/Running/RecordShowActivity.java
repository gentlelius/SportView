package ls.main.activities.Running;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.netease.nim.demo.R;

import ls.main.bean.PathRecord;


public class RecordShowActivity extends Activity implements AMap.OnMapLoadedListener {
	private MapView mapView;
	private AMap aMap;
	private PathRecord mrecord;
//	private ToggleButton displaybtn;
	private Marker mMarker;
	private final static int UPDATE_MARKER = 0;
	private final static int MOVE_FINISH = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorddisplay_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		mrecord = (PathRecord)getIntent().getParcelableExtra("recorditem");

		init();
//        displaybtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (displaybtn.isChecked()) {
//					startMove();
//				}
//			}
//		});
	}
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMapLoadedListener(this);
		}
//		displaybtn = (ToggleButton)findViewById(R.id.displaybtn);
		
	}


	public void toMyRunningActivity(View view){
		startActivity(new Intent(this,MyRunningActivity.class));
	}
//	private void startMove(){
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				for (int i = 0; i < mrecord.getPathline().size(); i++) {
//					if (displaybtn.isChecked()) {
//						//为marker准备它的下一个位置
//						LatLng latLng = mrecord.getPathline().get(i);
//						//刷新界面的东西弄到主线程中
//						Message message = new Message();
//						message.what = UPDATE_MARKER;
//						message.obj = latLng;
//						handler.sendMessage(message);
//
//						try {
//							Thread.sleep(100);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//				Message msg = handler.obtainMessage(MOVE_FINISH);
//				msg.sendToTarget();
//			}
//		}).start();
//	}
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_MARKER:
				//更新marker的位置				
				LatLng latLng = (LatLng) msg.obj;
				mMarker.setPosition(latLng);
				break;
			case MOVE_FINISH:
//				displaybtn.setChecked(false);
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		startActivity(new Intent(this,MyRunningActivity.class));
	}

	private LatLngBounds getBounds(){
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < mrecord.getPathline().size(); i++) {
			b.include(mrecord.getPathline().get(i));
		}
		return b.build();
		
	}
	@Override
	public void onMapLoaded() {
		if (mrecord != null && mrecord.getPathline().size()>0) {
			aMap.addPolyline(new PolylineOptions().color(Color.BLUE)
					.addAll(mrecord.getPathline()));
			aMap.addMarker(new MarkerOptions().position(mrecord.getStartpoint())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
			aMap.addMarker(new MarkerOptions().position(mrecord.getEndpoint())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
			
			
//			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mrecord.getStartpoint(), 15));
			try {
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(), 50));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mMarker = aMap.addMarker(new MarkerOptions().position(mrecord.getStartpoint())
					.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.walk)))
					);
			
		}else {
			Log.i("MY", "mrecord == null");
		}
	}
}
