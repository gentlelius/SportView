package ls.main.activities.Running;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.R;

import java.text.DecimalFormat;

import ls.main.bean.TargetDay;
import ls.main.utils.SPUtil;
import ls.main.utils.TitleBuilder;

public class TargetOfDayActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_mile_plus,iv_mile_sub,iv_duration_plus,iv_duration_sub;
    private TextView tv_mile,tv_duration;
    private Button btn_target_ok ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_of_day);
        initViews();
        TargetDay day = SPUtil.get(this);
        tv_duration.setText(String.valueOf(day.getDuration()));
        tv_mile.setText(String.valueOf(day.getMiles()));
        miles = day.getMiles();
        duration =  day.getDuration();
    }


    private void initViews() {
        new TitleBuilder(this).setTitleText("每日运动目标").setTextColor(Color.GRAY);
        tv_mile = (TextView) findViewById(R.id.tv_target_mile);
        tv_duration = (TextView) findViewById(R.id.tv_target_duration);
        iv_duration_plus = (ImageView) findViewById(R.id.iv_duration_plus);
        iv_duration_sub = (ImageView) findViewById(R.id.iv_duration_sub);
        iv_mile_plus = (ImageView) findViewById(R.id.iv_mile_plus);
        iv_mile_sub = (ImageView) findViewById(R.id.iv_mile_sub);
        btn_target_ok = (Button) findViewById(R.id.btn_target_ok);
        btn_target_ok.setOnClickListener(this);
        iv_duration_sub.setOnClickListener(this);
        iv_duration_plus.setOnClickListener(this);
        iv_mile_plus.setOnClickListener(this);
        iv_mile_sub.setOnClickListener(this);
    }

    private DecimalFormat format = new DecimalFormat("0.0");
    private double miles ;
    private int duration;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_duration_plus:
                duration = Integer.parseInt(tv_duration.getText().toString());
                duration+=1;
                tv_duration.setText(String.valueOf(duration));
                break;
            case R.id.iv_duration_sub:

                duration = Integer.parseInt(tv_duration.getText().toString());
                duration-=1;
                if(duration<1){
                    break;
                }
                tv_duration.setText(String.valueOf(duration));
                break;
            case R.id.iv_mile_plus:
                miles = Double.parseDouble(tv_mile.getText().toString());
                miles += 0.1;
                tv_mile.setText(format.format(miles));
                break;
            case R.id.iv_mile_sub:

                miles = Double.parseDouble(tv_mile.getText().toString());
                miles -= 0.1;
                if(miles<0.1){
                    break;
                }
                tv_mile.setText(format.format(miles));
                break;
            case R.id.btn_target_ok:
//                Toast.makeText(this,"duration:"+duration+",miles:"+miles,Toast.LENGTH_SHORT).show();
                TargetDay day = new TargetDay();
                day.setDuration(duration);
                day.setMiles(miles);
                SPUtil.put(this,day);
                Toast.makeText(this,"修改成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
