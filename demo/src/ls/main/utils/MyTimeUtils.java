package ls.main.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTimeUtils {

	public static String getSimpleDate(long t){
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return format.format(new Date(t));
	}
	

	public static String getModelTime(long t){
		Date date = new Date();
		long delt = date.getTime()-t;
		int delt_minute = (int) (delt/(1000*60));
		if(delt_minute>60){
			int delt_hour = delt_minute/60;
			if(delt_hour>24){
				int days = delt_hour/24;
				if(days>3){
					return getSimpleDate(t);
				}else{
					return days+"天前";
				}
			}else{
				return delt_hour+"小时前";
			}
		}else{
			if(delt_minute>0){
				return delt_minute+"分钟前";
			}else{
				return "刚刚";
			}
		}
	}
	
	public static long getLongFormat(String time){
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		try {
			Date d2 = df.parse(time);
			return d2.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1L;
		}
		
	}
}
