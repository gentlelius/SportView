package ls.main.utils;

import android.util.Log;


public class Logger {

	/**
	 * 显示LOG(默认info级别)
	 * 
	 * @param TAG
	 * @param msg
	 */
	public static void show(String TAG, String msg) {
		
	}

	/**
	 * 显示LOG
	 * 
	 * @param TAG
	 * @param msg
	 * @param level
	 *            1-info; 2-debug; 3-verbose
	 */
	public static void show(String TAG, String msg, int level) {
		
		switch (level) {
		case Log.VERBOSE:
			Log.v(TAG, msg);
			break;
		case Log.DEBUG:
			Log.d(TAG, msg);
			break;
		case Log.INFO:
			Log.i(TAG, msg);
			break;
		case Log.WARN:
			Log.w(TAG, msg);
			break;
		case Log.ERROR:
			Log.e(TAG, msg);
			break;
		default:
			Log.i(TAG, msg);
			break;
		}
	}

}
