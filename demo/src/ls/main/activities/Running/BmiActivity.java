package ls.main.activities.Running;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.demo.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import ls.main.adapter.DbAdapter;
import ls.main.bean.BMIBean;
import ls.main.utils.MyTimeUtils;
import ls.main.utils.TitleBuilder;

public class BmiActivity extends AppCompatActivity implements View.OnClickListener {
//51A9E1  88C703  FFB402  D41E02
    private int[] bmi_level_color = {0xff51A9E1,0xff88C703,0xffFFB402,0xffD41E02};

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 10;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;


    private ImageView iv_bmi_level,iv_weight_how_change;
    private TextView tv_height, tv_weight, tv_bmi_level, tv_bmi_value
           , tv_delt_weight,tv_update_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        db = new DbAdapter(this);
        db.open();
//        getLatesBmi
        initViews();


    }



    private void initViews() {

        new TitleBuilder(this)
                .setTitleText("身体质量")
                .setRightImage(R.drawable.edit_data_selector)
        .setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        }).setTextColor(Color.GRAY);

        chart = (LineChartView) findViewById(R.id.linechart_weight);
        generateValues();

//       List<Integer> dataList = getTop10List();
//        generateData(dataList);
        chart.setViewportCalculationEnabled(false);
        resetViewport();


        iv_bmi_level = (ImageView) findViewById(R.id.iv_bmi_level);
        iv_weight_how_change = (ImageView) findViewById(R.id.iv_weight_how_change);
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        tv_bmi_level = (TextView) findViewById(R.id.tv_bmi_level);
        tv_bmi_value = (TextView) findViewById(R.id.tv_bmi_value);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_delt_weight = (TextView) findViewById(R.id.tv_delt_weight);
        lastWeight = tv_weight.getText().toString();


        initListener();
        resetUI();
    }

    private List<Integer> getTop10List() {
        Cursor cursor = db.getTop10Bmi();
        List<Integer> dataList = new ArrayList<>();
        while(cursor.moveToNext()){
            Integer i = new Integer(cursor.getString(cursor.getColumnIndex("weight")));
            dataList.add(i);
        }
        Collections.reverse(dataList);
        return dataList;
    }

    private void initListener() {
        iv_bmi_level.setOnClickListener(this);

    }



    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 200;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    private void generateData(List<Integer> dataList) {

        List<Line> lines = new ArrayList<Line>();
        if (dataList==null){
            return;
        }
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < dataList.size(); ++j) {
                values.add(new PointValue(j, dataList.get(j)));
                Log.e("TAG",dataList.get(j)+"dataList  ---");
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
//            axisY.setMaxLabelChars(200);
            if (hasAxesNames) {
                axisX.setName("最近"+(dataList!=null?dataList.size():0)+"天");
                axisY.setName("体重(单位：斤)");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    private void resetUI(){
        Cursor cursor = db.getTop10Bmi();
        if(cursor.moveToNext()){
            BMIBean bmiBean = new BMIBean();
            bmiBean.setBmi(cursor.getString(cursor.getColumnIndex("bmi")));
            bmiBean.setHeight(cursor.getString(cursor.getColumnIndex("height")));
            bmiBean.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
            bmiBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            double lastWeight = 0;
            if(cursor.moveToNext()){
                lastWeight = Double.parseDouble(cursor.getString(cursor.getColumnIndex("weight")));
            }
            double myWeight = Double.parseDouble(bmiBean.getWeight());
            double myHeight = Double.parseDouble(bmiBean.getHeight());
            double delt_weight = myWeight - lastWeight;
            Log.e("TAG","======"+bmiBean.getTime());
            long time = Long.parseLong(bmiBean.getTime());
            setParameters(delt_weight,myWeight,myHeight,time);
            setBMI("男",myHeight,myWeight/2);
        }
        List<Integer> dataList = getTop10List();
        generateData(dataList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_bmi_level:
                resetData();
                break;

        }
    }
    private DbAdapter db;
    private String lastWeight;
    private void resetData() {
        final AlertDialog.Builder buider = new AlertDialog.Builder(this);
        final View dialog_view = LayoutInflater.from(this).inflate(R.layout.dialog_resetdata,null);
        buider.setView(dialog_view);
        buider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               EditText et_height = (EditText) dialog_view.findViewById(R.id.et_height);
                EditText et_weight = (EditText) dialog_view.findViewById(R.id.et_weight);
                if(!TextUtils.isEmpty(et_height.getText().toString()) &&  !TextUtils.isEmpty(et_weight.getText().toString())){
                    double myHeight = Integer.parseInt(et_height.getText().toString());
                    double myWeight = Integer.parseInt(et_weight.getText().toString());
                    double delt_weight = myWeight-Double.parseDouble(lastWeight);
                    // 先存后取
                    BMIBean bean = new BMIBean();
                    bean.setTime(String.valueOf(System.currentTimeMillis()));
                    bean.setWeight(String.valueOf(myWeight));
                    bean.setHeight(String.valueOf(myHeight));
                    double sg = myHeight/100.0;
                    double bmi = myWeight / (sg*sg);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String str_bmi = df.format(bmi);
                    bean.setBmi(str_bmi);
                    db.updateBmi(bean);
//                    setParameters(delt_weight,myWeight,myHeight);
//                    setBMI("男",myHeight,myWeight/2);
                    resetUI();
                }
            }
        });
        buider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        buider.show();

    }


    private void setParameters(double delt_weight,double myWeight,double myHeight,long time){
        iv_weight_how_change.setImageResource(delt_weight>0?R.drawable.icon_arrow_up:R.drawable.icon_arrow_down);
        tv_height.setText(String.valueOf(myHeight));
        tv_weight.setText(String.valueOf(myWeight));
        tv_update_time.setText(MyTimeUtils.getSimpleDate(time));
        tv_delt_weight.setText(String.valueOf(Math.abs(delt_weight)));
    }

    private void setBMI(String sex,double height,double weight){
        double sg = height/100.0;
        double bmi = weight / (sg*sg);
        DecimalFormat df = new DecimalFormat("0.00");
        String str_bmi = df.format(bmi);
        tv_bmi_value.setText(str_bmi);
        if(sex.equals("男")){
            if(bmi<18){
                iv_bmi_level.setImageResource(R.drawable.bmi_1);
                tv_bmi_level.setText("偏瘦");
                setTextColor(bmi_level_color[0]);
            }else if(bmi>=18 & bmi<22){
                iv_bmi_level.setImageResource(R.drawable.bmi_2);
                tv_bmi_level.setText("正常");
                setTextColor(bmi_level_color[1]);
            }else if(bmi>=22 && bmi<25){
                iv_bmi_level.setImageResource(R.drawable.bmi_3);
                tv_bmi_level.setText("偏胖");
                setTextColor(bmi_level_color[2]);
            }else{
                iv_bmi_level.setImageResource(R.drawable.bmi_4);
                tv_bmi_level.setText("重度偏胖");
                setTextColor(bmi_level_color[3]);
            }
        }else{
            if(bmi<16){
                iv_bmi_level.setImageResource(R.drawable.bmi_1);
                tv_bmi_level.setText("偏瘦");
                setTextColor(bmi_level_color[0]);
//				tv_bim.setTextColor(0xffffff);
            }else if(bmi>=16 & bmi<20){
                iv_bmi_level.setImageResource(R.drawable.bmi_2);
                tv_bmi_level.setText("正常");
                setTextColor(bmi_level_color[1]);
            }else if(bmi>=20 && bmi<23){
                iv_bmi_level.setImageResource(R.drawable.bmi_3);
                tv_bmi_level.setText("偏胖");
                setTextColor(bmi_level_color[2]);
            }else{
                iv_bmi_level.setImageResource(R.drawable.bmi_4);
                tv_bmi_level.setText("重度偏胖");
                setTextColor(bmi_level_color[3]);
            }
        }
    }
    private void setTextColor(int color){
        tv_bmi_value.setTextColor(color);
        tv_bmi_level.setTextColor(color);
    }
}
