package ls.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nim.demo.R;

import java.util.ArrayList;
import java.util.List;

import ls.main.activities.LikeActivity;
import ls.main.activities.TestActivity;
import ls.main.adapter.NewsAdapter;
import ls.main.base.BaseFragment;
import ls.main.bean.NewsList;
import ls.main.utils.TitleBuilder;

public class HomeFragment extends BaseFragment {

    private NewsAdapter adapter;
    private View view;
    private TextView titlebar_tv;
    private PullToRefreshListView lv_content;
    List<NewsList.T1411113472760Bean> data = new ArrayList<NewsList.T1411113472760Bean>();
    int refresh_time = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_home, null);

        new TitleBuilder(view).setTitleText("首页").setLeftImage(R.drawable.ic_like)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, LikeActivity.class));
                    }
                });
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getJSONByVolley(0);
                refresh_time = 0;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getJSONByVolley(10 * (++refresh_time));
            }
        });

        ILoadingLayout end = lv_content.getLoadingLayoutProxy(false, true);
        end.setPullLabel("上拉刷新");
        end.setRefreshingLabel("玩命加载中");
        ILoadingLayout start = lv_content.getLoadingLayoutProxy(true, false);
        start.setPullLabel("下拉刷新");
        start.setRefreshingLabel("玩命加载中");


        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                NewsList.T1411113472760Bean bean = data.get(position - 1);
                System.out.println("postition-----" + position);
                Intent intent = new Intent(activity, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", bean.getUrl());
                bundle.putString("img_url", bean.getImgsrc());
                bundle.putString("title", bean.getTitle());
                bundle.putString("time", bean.getPtime());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        // 加载网络数据。使用volley

        // 0 10 20 30
        getJSONByVolley(0);
        return view;
    }

    private void getJSONByVolley(final int start) {

        // String url
        // ="http://c.m.163.com/nc/article/list/T1411113472760/0-10.html";
        String url = "http://c.m.163.com/nc/article/list/T1411113472760/"
                + start + "-10.html";
//        String url = "http://c.m.163.com/nc/article/list/T1411113472760/0-10.html";
        RequestQueue mQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("TAG", response);
                        Gson gson = new Gson();
                        NewsList newsList = gson.fromJson(response,
                                NewsList.class);
                        List<NewsList.T1411113472760Bean> result = (List<NewsList.T1411113472760Bean>) newsList
                                .getT1411113472760();

                        if (adapter != null ) {
                            if(start != 0){
                                data.addAll(result);
                                adapter.notifyDataSetChanged();
                                lv_content.onRefreshComplete();
                            }else{
                                data.clear();
                                data.addAll(result);
                                lv_content.onRefreshComplete();
                            }

                        } else {
                            data.addAll(result);
                            adapter = new NewsAdapter(activity, data);
                            lv_content.setAdapter(adapter);
                        }

                        // ToastUtils.showToast(activity, "if has", 0);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);

    }

}
