package ls.main.activities.Running;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.netease.nim.demo.R;

import java.util.ArrayList;
import java.util.List;

import ls.main.adapter.OptionAdapter;
import ls.main.bean.OptionBean;
import ls.main.utils.CircularRingPercentageView;

public class MyRunningActivity extends AppCompatActivity {

    private FloatingActionButton btn_go;
    public static final String EXTRA_NAME = "cheese_name";
    private ListView lv_op ;
    private List<OptionBean> data;
    private String[] titles = {"成就","历史数据","目标里程","体重与身高"};
    private String[] contents = {"查看获得的成就以及称谓","获取最近的运动数据，很友好的数据展示"
            ,"自己定制一个运动目标","免费计算亲的身体质量指数 (BMI)"};
    private int[] icon_res = {R.drawable.op_got,R.drawable.op_history,R.drawable.op_goal,R.drawable.op_weight_height};
    private int[] title_color = {0xffF9924C,0xffC381FC,0xff68AAF4,0xff5BB035};//F9924C   C381FC  68AAF4  5BB035
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_running);
        btn_go = (FloatingActionButton) findViewById(R.id.go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyRunningActivity.this,ReadyGoActivity.class));
            }
        });

//        Intent intent = getIntent();
//        final String cheeseName = intent.getStringExtra(EXTRA_NAME);
        initGPS();
        lv_op = (ListView) findViewById(R.id.lv_option);
        getData();
        lv_op.setAdapter(new OptionAdapter(this,data));
        lv_op.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        startActivity(new Intent(MyRunningActivity.this,ChartActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        startActivity(new Intent(MyRunningActivity.this,BmiActivity.class));
                        break;
                }
            }
        });

        getWH();
        CircularRingPercentageView progressCircle = (CircularRingPercentageView) findViewById(R.id.progress);
        progressCircle.setMaxColorNumber(200);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)progressCircle.getLayoutParams();
        int progress = (int) (screenWidth * 0.8);
        layoutParams.width = progress;
        layoutParams.height = progress;
        progressCircle.setLayoutParams(layoutParams);
        progressCircle.setCircleWidth(progress);
        progressCircle.setProgress(100);
    }

    private void getData() {
        data = new ArrayList<>();
        for (int i=0;i<titles.length;i++){
            OptionBean bean = new OptionBean();
            bean.setTitle(titles[i]);
            bean.setContent(contents[i]);
            bean.setTitle_color(title_color[i]);
            bean.setIcon_url(icon_res[i]);
            data.add(bean);
        }
    }

    private int screenWidth,screenHeight;
    private void getWH(){
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
    }

    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "请打开GPS",
//                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("为了获得更准确的运动记录，请打开GPS");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            // 弹出Toast
//			Toast.makeText(TrainDetailsActivity.this, "GPS is ready",
//					Toast.LENGTH_LONG).show();
//			// 弹出对话框
//			new AlertDialog.Builder(this).setMessage("GPS is ready")
//					.setPositiveButton("OK", null).show();
        }
    }
}
