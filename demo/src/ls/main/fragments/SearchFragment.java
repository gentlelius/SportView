package ls.main.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nim.demo.R;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.List;

import ls.main.adapter.InfoListAdapter;
import ls.main.base.BaseFragment;
import ls.main.bean.InfolistItem;
import ls.main.utils.TitleBuilder;
import ls.main.utils.ToastUtils;


public class SearchFragment extends BaseFragment {

	private int index = 0;
	private View view;
	private PullToRefreshListView lv_shuoshuo;
	public static DilatingDotsProgressBar mDilatingDotsProgressBar ;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = View.inflate(activity, R.layout.frag_search, null);
		new TitleBuilder(view).setTitleText("好友动态")
		.setRightImage(R.drawable.add_ss_selector)
		.setRightOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				startActivity(new Intent());
				ToastUtils.showToast(activity,"发啦",0);
			}
		});

		mDilatingDotsProgressBar = (DilatingDotsProgressBar) view.findViewById(R.id.pr2);
		mDilatingDotsProgressBar.show(500);
		mDilatingDotsProgressBar.setNumberOfDots(3);
		mDilatingDotsProgressBar.setDotColor(Color.GRAY);
		mDilatingDotsProgressBar.setDotSpacing(15);
		lv_shuoshuo = (PullToRefreshListView) view.findViewById(R.id.shuoshuo);
		lv_shuoshuo.setMode(PullToRefreshBase.Mode.BOTH);
		lv_shuoshuo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				lv_shuoshuo.setRefreshing();
				getJsonByVolley(0);
				index=0;
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				index++;
				getJsonByVolley(10*index);
			}
		});
		lv_shuoshuo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToastUtils.showToast(activity,"点了第"+position,0);
			}
		});
		ILoadingLayout end = (ILoadingLayout) lv_shuoshuo.getLoadingLayoutProxy(false, true);
		end.setPullLabel("上拉刷新");
		end.setRefreshingLabel("玩命加载中");
		ILoadingLayout start = (ILoadingLayout) lv_shuoshuo.getLoadingLayoutProxy(true, false);
		start.setPullLabel("下拉刷新");
		start.setRefreshingLabel("玩命加载中");
		getJsonByVolley(0);

		return view;
	}

	private void getJsonByVolley(final int start) {
		String url = "http://192.168.1.7:8080/ISport/servlet/GetInfoList?start="+start;
		RequestQueue mQueue = Volley.newRequestQueue(activity);
		StringRequest stringRequest = new StringRequest(
				url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("TAG",response);

						//  list = gson.fromJson(jsonString, new TypeToken<List<cls>>() {}.getType());
						List<InfolistItem> list = new Gson().fromJson(response, new TypeToken<List<InfolistItem>>(){}.getType());
						pullDataToUI(list,start);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				}
		);
		mQueue.add(stringRequest);
	}

	private void pullDataToUI(List<InfolistItem> infolist,int start) {
		if(infolist==null){
			ToastUtils.showToast(activity,"已经到底了",0);
			lv_shuoshuo.onRefreshComplete();
			return;
		}

		if (adapter != null ) {
			if(start == 0){
				data.clear();
			}
			data.addAll(infolist);
			adapter.notifyDataSetChanged();
			lv_shuoshuo.onRefreshComplete();
		} else {
			data.addAll(infolist);
			adapter = new InfoListAdapter(activity, data);
			lv_shuoshuo.setAdapter(adapter);
		}
	}
	private InfoListAdapter adapter;
	private List<InfolistItem> data = new ArrayList<>();
}
