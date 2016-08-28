package ls.main.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ls.main.activities.MainMenuActivity;


public class BaseFragment extends Fragment {
	
	protected MainMenuActivity activity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = (MainMenuActivity) getActivity();
	}
	
	protected void intent2Activity(Class<? extends Activity> tarActivity) {
		Intent intent = new Intent(activity, tarActivity);
		startActivity(intent);
	}
}
