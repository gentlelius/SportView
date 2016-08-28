package ls.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nim.demo.R;

import java.util.List;

import ls.main.adapter.LikeAdapter;
import ls.main.bean.Like;
import ls.main.utils.LikeDao;


public class LikeActivity extends Activity {


    private ListView lv_likes;
    private List<Like> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        lv_likes = (ListView) findViewById(R.id.lv_likes);
        LikeDao dao = new LikeDao(this);
        data = dao.getAllLikes();
        if(data==null || data.size()==0){
            TextView textView = (TextView) findViewById(R.id.tv_info);
            textView.setVisibility(View.VISIBLE);
            return;
        }
        LikeAdapter adapter = new LikeAdapter(this,data);
        lv_likes.setAdapter(adapter);
        lv_likes.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Like like = data.get(position);
                Intent intent = new Intent(LikeActivity.this,TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", like.getUrl());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    private int calculateDpToPx(int padding_in_dp){
        final float scale = getResources().getDisplayMetrics().density;
        return  (int) (padding_in_dp * scale + 0.5f);
    }


}
