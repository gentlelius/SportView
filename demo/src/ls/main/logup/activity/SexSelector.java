package ls.main.logup.activity;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.demo.R;

/**
 * Created by admin on 2016/8/9.
 */
public class SexSelector  implements View.OnClickListener{

    private LinearLayout linearLayout;
    private TextView tv_man,tv_woman;
    private String sex = "";
    private LogupActivity context;

    public SexSelector(LinearLayout linearLayout, LogupActivity context){
        this.linearLayout = linearLayout;
        this.context = context;
        tv_man = (TextView) linearLayout.findViewById(R.id.tv_man);
        tv_woman = (TextView) linearLayout.findViewById(R.id.tv_woman);
        tv_man.setOnClickListener(this);
        tv_woman.setOnClickListener(this);

    }

    public String getSelctedSex(){
        return this.sex;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_man:
                tv_man.setBackgroundColor(0x88b0b0b0);
                tv_woman.setBackgroundColor(0xffffffff);
                this.sex = "男";
                Log.e("TAG","nan ---------------------===================");
                break;
            case R.id.tv_woman:
                tv_woman.setBackgroundColor(0x88b0b0b0);
                tv_man.setBackgroundColor(0xffffffff);
                this.sex = "女";
                break;
            default:
                break;
        }
        context.checkBySexSelector();

    }
}


