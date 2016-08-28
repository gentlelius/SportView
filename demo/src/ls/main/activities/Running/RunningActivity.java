package ls.main.activities.Running;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.netease.nim.demo.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ls.main.adapter.DbAdapter;
import ls.main.bean.PathRecord;
import ls.main.utils.ToastUtils;

import static android.content.DialogInterface.OnClickListener;


/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class RunningActivity extends Activity implements LocationSource,
        AMapLocationListener, View.OnClickListener {
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolyoptions;
    private PathRecord record;
    private long starttime;
    private long endtime;
    private ToggleButton btn;
    private DbAdapter DbHepler;
    private ImageView iv_lock,iv_settings;
    private ImageView iv_pause, iv_continue, iv_end;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView tv_sd,tv_time,tv_souce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basicmap_activity);
        initViews();
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
        // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
//		if(!isOpen()){
//			openGps();
//		}
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
        initpolyline();
        if (record != null) {
            record = null;
        }
        record = new PathRecord();
        starttime = System.currentTimeMillis();
        record.setDate(getcueDate(starttime));
        myTimer.start();
    }

    private void initViews() {
        iv_pause = (ImageView) findViewById(R.id.iv_pause);
        iv_continue = (ImageView) findViewById(R.id.iv_continue);
        iv_end = (ImageView) findViewById(R.id.iv_end);
        iv_lock = (ImageView) findViewById(R.id.iv_lock);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        tv_sd = (TextView) findViewById(R.id.tv_sd);
        tv_souce = (TextView) findViewById(R.id.tv_souce);
        tv_time = (TextView) findViewById(R.id.tv_time);

        iv_pause.setOnClickListener(this);
        iv_continue.setOnClickListener(this);
        iv_end.setOnClickListener(this);
        iv_lock.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
//        btn = (ToggleButton) findViewById(R.id.locationbtn);
//        btn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btn.isChecked()) {
//                    Log.i("MY", "isChecked");
//
//                    aMap.clear(true);
//                    if (record != null) {
//                        record = null;
//                    }
//                    record = new PathRecord();
//                    starttime = System.currentTimeMillis();
//                    record.setDate(getcueDate(starttime));
//                } else {
//                    endtime = System.currentTimeMillis();
//                    saverecord(record);
//                }
//            }
//        });

    }

    protected void saverecord(PathRecord record) {
        if (record != null && record.getPathline().size() > 0) {
            DbHepler = new DbAdapter(this);
            DbHepler.open();
            record.setDuration(String.valueOf((endtime - starttime) / 1000f));
            float distance = 0;
            String pathline = "";
            for (int i = 0; i < record.getPathline().size(); i++) {
                if (i < record.getPathline().size() - 1) {
                    LatLng firstpoint = record.getPathline().get(i);
                    LatLng secoundpoint = record.getPathline().get(i + 1);
                    distance = distance
                            + AMapUtils.calculateLineDistance(firstpoint,
                            secoundpoint);
                }
                LatLng point = record.getPathline().get(i);
                pathline = pathline + point.latitude + "," + point.longitude
                        + ";";
            }
            record.setDistance(String.valueOf(distance));
            record.setStartpoint(record.getPathline().get(0));
            record.setAveragespeed(String.valueOf(distance
                    / (float) (endtime - starttime)));
            record.setEndpoint(record.getPathline().get(
                    record.getPathline().size() - 1));

            String stratpoint = record.getStartpoint().latitude + ","
                    + record.getStartpoint().longitude;
            String endpoint = record.getEndpoint().latitude + ","
                    + record.getEndpoint().longitude;
            DbHepler.createrecord(record.getDistance(), record.getDuration(),
                    record.getAveragespeed(), pathline, stratpoint, endpoint,
                    record.getDate());
            DbHepler.close();
        } else {
            Toast.makeText(this, "没有记录到路径", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.BLUE);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.animateCamera(CameraUpdateFactory.zoomTo(16f));

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
//        mlocationClient.stopLocation();
//        mlocationClient = null;
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        Log.e("TAG", "active start location");
        startlocation();


    }
    private int second;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    second++;
                    tv_time.setText(RunningDataUtil.getTime(second));
                    break;
            }
        }
    };


    private boolean isStop = false,isPause=false;
    Thread myTimer = new Thread(new Runnable() {
        @Override
        public void run() {
          while(!isStop){
              if(!isPause){
                  try{
                      Thread.sleep(1000);
                  }catch (Exception e){
                      Log.e("TAG","计时器线程出现错误");
                  }
                  handler.sendEmptyMessage(1);
              }
          }
        }
    });

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();

        }
        mlocationClient = null;
    }

    private int index = 0;
    private double distance = 0.0;
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                LatLng mylocation = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                //更新distance并且显示到textview
                int size = record.getPathline().size();
                if(size>1){
                    LatLng firstpoint = record.getPathline().get(size-2);
                    LatLng secoundpoint = record.getPathline().get(size-1);
                    if(!isPause && !isStop){
                        distance = distance
                                + AMapUtils.calculateLineDistance(firstpoint,
                                secoundpoint);
                    }
                    //显示
                    DecimalFormat df = new DecimalFormat("0.000");
                    tv_souce.setText(df.format(distance/1000));
                    if(distance < Double.MIN_VALUE || second/distance>1200){
                        tv_sd.setText("00'00''");
                    }else{
                        tv_sd.setText(RunningDataUtil.getSd(second, distance));
                    }
                }

//                if (btn.isChecked()) {
                    record.addpoint(mylocation);
                    mPolyoptions.add(mylocation);

                    //
                    redrawline();
                    index++;
//                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void startlocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(200);
//            mLocationOption.setGpsFirst(true);
            mLocationOption.setWifiActiveScan(true);


            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();


        }
    }

    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 0) {
            aMap.clear(true);
            aMap.addPolyline(mPolyoptions);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    public void record(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause:
                //开启动画,停止计时，停止画线
                isPause = true;
                startAnimator();
                break;
            case R.id.iv_continue:
                //关闭动画，继续计时，继续划线
                isPause = false;
                endAnimator();
                break;
            case R.id.iv_end:
                isPause = true;
                endtime = System.currentTimeMillis();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确认完成此次锻炼？");
                builder.setPositiveButton("完成", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isStop = true;
                        ToastUtils.showToast(getBaseContext(),tv_souce+"里程",0);

                        saverecord(record);

                       PathRecord today_record = getTodayData();
                       Intent intent = new Intent(getBaseContext(),RecordShowActivity.class);
                        intent.putExtra("recorditem",today_record);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("继续锻炼", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isPause = false;
                            }
                        });
                builder.create().show();
                break;
            case R.id.iv_settings:
                break;
            case R.id.iv_lock:
                startActivity(new Intent(this,LockActivity.class));
                break;
            default:
                break;
        }
    }

    private PathRecord getTodayData() {
        PathRecord path = new PathRecord();
        DbHepler.open();
        Cursor mCursor = DbHepler.getallrecord();
        mCursor.moveToLast();
        record.setDistance(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
        record.setDuration(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURATION)));
        record.setDate(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DATE)));
        String lines = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_LINE));
        record.setPathline(RecordActivity.parseLocations(lines));
        record.setStartpoint(RecordActivity.parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_STRAT))));
        record.setEndpoint(RecordActivity.parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_END))));
        return  record;
    }

    ObjectAnimator animator1;
    ObjectAnimator animator2;
    private void startAnimator() {
        iv_pause.setVisibility(View.GONE);
        iv_continue.setVisibility(View.VISIBLE);
        iv_end.setVisibility(View.VISIBLE);
        int x1 = -150,x2 = 150;
        PropertyValuesHolder px = PropertyValuesHolder.ofFloat("translationX", (float)x1);
        animator1 = ObjectAnimator.ofPropertyValuesHolder(iv_continue, px);
        animator1.setDuration(200).start();

        PropertyValuesHolder px2 = PropertyValuesHolder.ofFloat("translationX", (float)x2);
        animator2 = ObjectAnimator.ofPropertyValuesHolder(iv_end, px2);
        animator2.setDuration(200).start();
    }
    ObjectAnimator animator3;
    ObjectAnimator animator4;
    private void endAnimator() {
        int x1 = 0,x2 = 0;
        PropertyValuesHolder px = PropertyValuesHolder.ofFloat("translationX", (float)x1);
        animator3 = ObjectAnimator.ofPropertyValuesHolder(iv_continue, px);
        animator3.setDuration(300).start();

        PropertyValuesHolder px2 = PropertyValuesHolder.ofFloat("translationX", (float)x2);
        animator4 = ObjectAnimator.ofPropertyValuesHolder(iv_end, px2);
        animator4.setDuration(300).start();
        animator4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                iv_continue.setVisibility(View.GONE);
                iv_end.setVisibility(View.GONE);
                iv_pause.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
