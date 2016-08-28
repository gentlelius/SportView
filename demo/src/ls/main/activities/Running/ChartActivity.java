package ls.main.activities.Running;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.netease.nim.demo.R;
import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import ls.main.adapter.DbAdapter;
import ls.main.bean.TargetDay;
import ls.main.utils.SPUtil;
import ls.main.utils.TitleBuilder;

public class ChartActivity extends AppCompatActivity implements SwichLayoutInterFace {




    private ColumnChartView chart ;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesName = true;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = false;

    private SimpleAdapter simpleAdapter;
    private ListView list_choice;
    private List<HashMap<String,Object>> data_choice;
    String[] choice_str = {"今日已达到目标里程，查看详情","查看更多历史纪录","重新设置每日目标"};
    private int index = 1;
    private long last;
    private double mile_total_today;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        new TitleBuilder(this)
                .setTitleText("历史数据")
               .setTextColor(Color.GRAY);
        setEnterSwichLayout();
        chart = (ColumnChartView) findViewById(R.id.chart);
        list_choice = (ListView) findViewById(R.id.list_choice);
        setListenerForLv();
        mile_total_today = initTodayData();
        getData();
        simpleAdapter = new SimpleAdapter(this,data_choice,R.layout.choice_item,new String[]{"choice_content","choice_icon"},
                new int[]{R.id.choice_text,R.id.choice_icon});
        list_choice.setAdapter(simpleAdapter);
        chart.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                long cur = System.currentTimeMillis();
                if(index!=1){
                    long delt = cur - last;
                    Log.e("TAG"," delt/1000-->"+delt/1000);

                    if(delt/1000<=1){
                        Toast.makeText(getBaseContext(),"没有1秒",Toast.LENGTH_SHORT).show();
                    }
                }
                last = cur;
                index++;
//                    Toast.makeText(getBaseContext(),subcolumnValue.getValue()+""+subcolumnValue.getLabelAsChars()+i+"i1:"+i1,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onValueDeselected() {

            }
        });
        chart.setBackgroundColor(Color.WHITE);
//        chart.setHorizontalScrollBarEnabled(true);
//设置行为属性，支持缩放、滑动以及平移
        chart.setInteractive(true);
        chart.setZoomEnabled(false);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        generateData();


    }


    private double initTodayData() {
        DbAdapter DbHepler=new DbAdapter(this);
        DbHepler.open();
       double sum = DbHepler.getSumToday();
        return sum/1000;
    }

    private void setListenerForLv() {
        list_choice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.showToast(getBaseContext(),"id:"+id+"  position:"+position,0);
                switch (position){
                    case 0:
                        Intent intent = new Intent(ChartActivity.this,TodayActivity.class);
                        intent.putExtra("mile_total_today",mile_total_today);
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(new Intent(ChartActivity.this,RecordActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(ChartActivity.this,TargetOfDayActivity.class));
                        break;
                }
            }
        });
    }

    private void getData() {
        data_choice = new ArrayList<>();
        TargetDay today = SPUtil.get(this);

        double wannaS = today.getMiles();
        double curS = mile_total_today;
        double deltS =  wannaS-curS;
        DecimalFormat decimalformat = new DecimalFormat("0.###");
        if(deltS>0){
            choice_str[0] = "今日未达到目标里程，查看详情";
        }

        for(int i=0;i<choice_str.length;i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put("choice_content",choice_str[i]);
            map.put("choice_icon",R.drawable.next_normal);
            data_choice.add(map);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_MOVE:
//                Log.e("TAG",event.getX()+"=======================");
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    private void generateData() {
        int numSubcolumns = 1;
        int numColmns = 15;
        List<Column> colums = new ArrayList<>();
        List<SubcolumnValue> values ;
        for(int i=0;i<numColmns;i++){
            values = new ArrayList<>();
            for(int j=0;j<numSubcolumns;j++){
                values.add(new SubcolumnValue(new Random().nextInt(5000), 0xffBBFFFF));
            }
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            colums.add(column);
        }
        data = new ColumnChartData(colums);
        if(hasAxes){
            Axis axisX = new Axis();
            Axis axisY = new Axis();

            List<AxisValue> xValues = new ArrayList<>();
            for(int i=0;i<numColmns-1;i++){
                xValues.add(new AxisValue(i).setLabel((numColmns-1-i)+"天前"));
            }
            xValues.add(new AxisValue(numColmns-1).setLabel("今天"));
//            for(int i=0;i<numColmns;i++){
//                xValues.add(new AxisValue((float) i).setLabel(String.valueOf(i)));
//            }
            axisX.setValues(xValues);
            axisX.setTextColor(0xff8B8682);
            axisY.setTextColor(0xff8B8682);
            if(hasAxesName){
                axisX.setName("最近半个月");
                axisY.setName("里程数");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        }else{
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);

        }
        chart.setColumnChartData(data);
        Viewport viewport = new Viewport();
        viewport.left = numColmns-7;
        viewport.right = numColmns;
        viewport.top =  chart.getMaximumViewport().height();
        viewport.bottom = 0;
        chart.setCurrentViewport(viewport);
//        chart.resetViewports();
    }

//    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
//
//        @Override
//        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
//            Toast.makeText(getBaseContext(), "Selected: " + value, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onValueDeselected() {
//            // TODO Auto-generated method stub
//
//        }
//
//    }


    @Override
    public void setEnterSwichLayout() {
        SwitchLayout.getSlideFromBottom(this, false,
                BaseEffects.getMoreSlowEffect());
    }

    @Override
    public void setExitSwichLayout() {
        SwitchLayout.getSlideFromBottom(this, false,
                BaseEffects.getMoreSlowEffect());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setExitSwichLayout();
    }
}
