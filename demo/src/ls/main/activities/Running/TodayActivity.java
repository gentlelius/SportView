package ls.main.activities.Running;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nim.demo.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ls.main.adapter.DbAdapter;
import ls.main.bean.PathRecord;
import ls.main.bean.TargetDay;
import ls.main.utils.SPUtil;

public class TodayActivity extends AppCompatActivity {

    ListView lv_today;
    private DbAdapter DbHepler;
    private ArrayAdapter<String> adapter;
    private List<String> data;
    private TextView tv_shownull;
    double mile_total_today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        mile_total_today = getIntent().getDoubleExtra("mile_total_today",0.0);
        lv_today = (ListView) findViewById(R.id.lv_today);
        tv_shownull = (TextView) findViewById(R.id.tv_shownull);
        getData();
        if(data==null){
            tv_shownull.setVisibility(View.VISIBLE);
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,data);
        lv_today.setAdapter(adapter);
    }

    private List<PathRecord> listdata;
    private void getData() {
        DecimalFormat format = new DecimalFormat("0.0");
        getListByDate();
        data = new ArrayList<>();
        TargetDay day = SPUtil.get(this);
        double targetmiles = day.getMiles();
        double delt = targetmiles - mile_total_today;
        data.add("今日总里程： "+(int)(mile_total_today*1000)+"米\n"+(delt<0?"超目标里程":"差目标里程")+(int)(Math.abs(delt*1000))+"米");
        for(int i=0;i<listdata.size();i++){
            PathRecord pr = listdata.get(i);
            String temp = pr.getDate()+"\n"+"里程："+ (int)Double.parseDouble(pr.getDistance())+"米\t"+"时长："+(int)Double.parseDouble(pr.getDuration())+"秒";
            data.add(temp);
        }
    }

    private List<PathRecord> getListByDate(){
        listdata = new ArrayList<>();
        DbHepler=new DbAdapter(this);
        DbHepler.open();
        DbHepler.delete();
        Cursor mCursor = DbHepler.getRecordByDate();
        while (mCursor.moveToNext()){
            PathRecord record = new PathRecord();
            record.setDistance(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DATE)));
            listdata.add(record);
            Log.e("TAG",record.getDate());
        }
        return  listdata;
    }
}
