package ls.main.activities.Running;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.netease.nim.demo.R;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

public class ReadyGoActivity extends Activity implements Animation.AnimationListener ,SwichLayoutInterFace {

    private ImageView iv_number1;
    private ImageView iv_number2;
    private ImageView iv_number3;

    private Animation animation1,animation2,animation3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_go);
        setEnterSwichLayout();
        iv_number1 = (ImageView) findViewById(R.id.iv_number);
        iv_number2 = (ImageView) findViewById(R.id.iv_number2);
        iv_number3 = (ImageView) findViewById(R.id.iv_number3);


        animation1 = AnimationUtils.loadAnimation(this,R.anim.scale_img);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.scale_img);
        animation3 = AnimationUtils.loadAnimation(this,R.anim.scale_img);

        animation1.setAnimationListener(this);
        animation2.setAnimationListener(this);
        animation3.setAnimationListener(this);

        iv_number3.startAnimation(animation3);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        index++;
        switch (index){
            case 1:
                iv_number3.clearAnimation();
                iv_number3.setVisibility(View.GONE);
                iv_number2.setVisibility(View.VISIBLE);
                iv_number2.startAnimation(animation2);
                break;
            case 2:
                iv_number2.clearAnimation();
                iv_number2.setVisibility(View.GONE);
                iv_number1.setVisibility(View.VISIBLE);
                iv_number1.startAnimation(animation1);
                break;
            case 3:
                iv_number1.clearAnimation();
                iv_number1.setVisibility(View.GONE);
                startActivity(new Intent(ReadyGoActivity.this,RunningActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private int index = 0;

    @Override
    public void setEnterSwichLayout() {
        SwitchLayout.ScaleBig(this, false, null);
    }

    @Override
    public void setExitSwichLayout() {
        SwitchLayout.ScaleBig(this, false, null);
    }
}
