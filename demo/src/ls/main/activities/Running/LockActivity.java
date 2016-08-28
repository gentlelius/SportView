package ls.main.activities.Running;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.netease.nim.demo.R;

public class LockActivity extends Activity {

    int x;
    private ImageView iv_right_touchmv;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        iv_right_touchmv = (ImageView) findViewById(R.id.iv_touchmv);
        animationDrawable = (AnimationDrawable) iv_right_touchmv.getDrawable();
        animationDrawable.start();
//        iv_right_touchmv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        x = (int) event.getX();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if(event.getX()-x > 30){
//                            Log.e("TAG","x-->"+x+","+event.getX());
//                            animationDrawable.stop();
//                            finish();
//                            return  true;
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });


    }


    private boolean isOnSlidingUnlock(float x, float y) {
        double deltX =  iv_right_touchmv.getX()+ iv_right_touchmv.getWidth();
        double deltY = iv_right_touchmv.getY() + iv_right_touchmv.getHeight();
        if(x>iv_right_touchmv.getX() && x<deltX && y>iv_right_touchmv.getY() && y<deltY){
            return true;
        }
        Log.e("TAG","deltX:"+deltX+",deltY:"+deltY);
        Log.e("TAG","X:"+iv_right_touchmv.getX()+",Y:"+iv_right_touchmv.getY());

        return  false;
    }

    boolean flag = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                flag = isOnSlidingUnlock(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if(event.getX()-x > 200){
                    Log.e("TAG","x-->"+x+","+event.getX());

                    if(flag) {
                        animationDrawable.stop();
                        finish();
                        return  true;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


}
