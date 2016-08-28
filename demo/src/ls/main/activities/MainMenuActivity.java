package ls.main.activities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.netease.nim.demo.R;
import com.netease.nim.demo.main.activity.MainActivity;
import com.netease.nim.uikit.common.activity.UI;

import ls.main.activities.Running.MyRunningActivity;
import ls.main.fragments.FragmentController;
import ls.main.utils.ToastUtils;


public class MainMenuActivity extends UI implements
        OnCheckedChangeListener, OnClickListener {

    private RadioGroup rg_tab;
    private ImageView iv_add;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainmenu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        controller = FragmentController.getInstance(this, R.id.fl_content);
        controller.showFragment(0);

        initView();
    }

    private void initView() {
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        rg_tab.setOnCheckedChangeListener(this);
        iv_add.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_meassage:
                controller.showFragment(1);
                break;
            case R.id.rb_search:
                controller.showFragment(2);
                break;
            case R.id.rb_user:
                controller.showFragment(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
               startActivity(new Intent(MainMenuActivity.this, MyRunningActivity.class));
                break;

            default:
                break;
        }
    }

    public  void toMainaAtivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG","Destroy---------------------------------------");
        controller = null;

    }
    private boolean exit = false;//标识是否可以退出
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==1) {
                exit = false;
            }
        }
    };
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
            if(!exit) {
                exit = true;
                ToastUtils.showToast(this,"再按一次就退出应用",0);
//                Toast.makeText(this, "再按一次就退出应用", 0).show();
                //发消息延迟2s将exit=false
                handler.sendEmptyMessageDelayed(1, 2000);
                return true;//不退出
            }
        }
        System.exit(0);
        return super.onKeyUp(keyCode, event);
    }
}
