package ls.main.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ls.main.bean.TargetDay;

/**
 * Created by admin on 2016/8/27.
 */
public class SPUtil {
    public static void put(Context context,TargetDay day){
        SharedPreferences sharedPreferences = context.getSharedPreferences("target_day", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt("duration", day.getDuration());
        editor.putString("miles", String.valueOf(day.getMiles()));
        editor.commit();//提交

    }

    public static TargetDay get(Context context){
        SharedPreferences share=context.getSharedPreferences("target_day",Context.MODE_PRIVATE);
        String miles=share.getString("miles","0.1");
        TargetDay day = new TargetDay();
        day.setMiles(Double.parseDouble(miles));
        int duration = share.getInt("duration",1);
        day.setDuration(duration);
        return  day;
    }
}
