package ls.main.activities.Running;

/**
 * Created by admin on 2016/8/18.
 */
public class RunningDataUtil {

    private static String format(int i){
        if(i<10 && i>=0){
            return "0".concat(String.valueOf(i));
        }else if(i>=10 && i<100){
            return String.valueOf(i);
        }else{
            return "XX";
        }
    }

    public static String getTime(int second) {
        if(second>60){
            if(second>3600){
                int h = second/3600;
                int ms = second%3600;
                int m = ms/60;
                int s = ms%60;
                return format(h)+":"+format(m)+":"+format(s);
            }else if(second<3600){
                int m = second/60;
                int s = second%60;
                return "00:"+format(m)+":"+format(s);
            }else{
                return "01:00:00";
            }

        } else if(second<60){
            return "00:00:"+format(second);
        } else {
            return "00:01:00";
        }
    }

    /**
     *
     * @param second 时间：秒
     * @param souce 路程：米
     * @return 速度（分钟/公里） XX'YY''形式的
     */
    public  static String getSd(int second,double souce){
        double sce = souce/1000.0;
        int sd = (int)(second/sce);
        int m = sd / 60;
        int s = sd % 60;
        return m+"'"+s+"''";
    }
}
