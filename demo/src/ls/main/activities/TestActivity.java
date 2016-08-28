package ls.main.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.netease.nim.demo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import ls.main.bean.Like;
import ls.main.utils.LikeDao;
import ls.main.utils.ToastUtils;


public class TestActivity extends Activity implements OnClickListener {


    private Handler handler_send = new Handler(){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    //接收下载完成消息
                    parseToHtml((String)msg.obj);
                    break;

                default:
                    break;
            }
        }
    };

    private String targetString = "";
    private RequestQueue mQueue;
    private TextView tView;
    private int index = 0;
    //	private String img_urlString;
    private List<String> imgsList = new ArrayList<String>();
    private List<String> imgsFileSrc = new ArrayList<String>();

    private String urlString;

    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();

        bundle = getIntent().getBundleExtra("bundle");
        urlString = bundle.getString("url");
        mQueue = Volley.newRequestQueue(this);
        String url = urlString;

        // setContentView(R.layout.activity_test);

        // webView = new WebView(this);
        // WebSettings settings = webView.getSettings();
        // settings.setJavaScriptEnabled(true);
        // settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // webView.setWebViewClient(new WebViewClient());
        // //webView.loadUrl("http://sports.163.com/16/0728/09/BT27FLEO000509NH.html");
        // webView.loadUrl("file:///android_asset/load_content.html");
        getHtmlString(url);// 获得抓取
        // savePage();
        // getJSONByVolley();
    }

    private void initViews() {
        tView = (TextView) findViewById(R.id.show);

    }


    public void	showPopWindow(View v){

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindowlayout, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);


        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(TestActivity.this.findViewById(R.id.iv_bottom_menu),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        ImageView iv_like = (ImageView) view.findViewById(R.id.iv_like);
        ImageView iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
        ImageView iv_share = (ImageView) view.findViewById(R.id.iv_share);
        if(bundle.getString("time")==null){
            iv_like.setEnabled(false);
        }
        iv_like.setOnClickListener(this);
        iv_comment.setOnClickListener(this);
        iv_share.setOnClickListener(this);


        //popWindow消失监听方法
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

    }

    private void savePage() {
        try {
            FileOutputStream out = openFileOutput("page.html", MODE_PRIVATE);
            out.write(targetString.getBytes());
            out.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    private void getJSONByVolley() {
//        String url = "http://op.juhe.cn/yi18/news/list?key=75abf624ab0553e18360287a455f4e76";
//        StringRequest stringRequest = new StringRequest(url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("TAG", response);
//                        Gson gson = new Gson();
//                        ContentList contentList = gson.fromJson(response,
//                                ContentList.class);
//                        // data = (ArrayList<ListBean>)
//                        // contentList.getResult().getList();
//                        ToastUtils.showToast(getBaseContext(), "aaa", 0);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        });
//        mQueue.add(stringRequest);
//
//    }

    @SuppressLint("JavascriptInterface")
    public void getHtmlString(final String url) {

        StringRequest htmlstrRequest = new StringRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        String putString = response;
                        if (putString.contains("查看全文")) {
                            getHtmlString(url.substring(0, url.lastIndexOf("."))
                                    + "_0.html");
                        } else {
                            int start = putString
                                    .indexOf("<div class=\"content\">");
                            String putString2 = putString.substring(start);
                            int end = putString2.indexOf("</div>");
                            targetString = putString2.substring(
                                    putString2.indexOf(">") + 1, end);

                            System.out.println(targetString);
                            // savePage();
                            System.out.println("---------------");
                            final String temp = targetString.replace("查看大图", "");
                            //保存图片至本地
                            saveImgs(temp);



                        }

                    }




                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(htmlstrRequest);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    public String getFileName(String path){
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }


    private void saveImgs(final String temp){
        // 下载图片
        tView.setText(Html.fromHtml(temp,
                new Html.ImageGetter() {

                    Drawable drawable;
                    @Override
                    public Drawable getDrawable (String source) {

                        System.out.println(source+"-------1");
                        imgsList.add(source) ;
                        return drawable;
                    }
                }, null));

        HttpUtils http = new HttpUtils();
        if(imgsList==null){
            return;
        }
        for(int i=0;i<imgsList.size();i++){

            String img_urlString = imgsList.get(i);
            final String targetPath = getCacheDir()+"/"+"pic"+i+img_urlString.substring(img_urlString.lastIndexOf("."));
            HttpHandler handler = http.download(
                    img_urlString,
                    targetPath,
                    true,
                    true,
                    new RequestCallBack<File>() {

                        @Override
                        public void onFailure(HttpException arg0,String arg1) {
                            Log.e("TAG", "sb");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            imgsFileSrc.add(arg0.result.getAbsolutePath());
                            if(imgsFileSrc.size()==imgsList.size()){
                                //发消息
                                Message message = new Message();
                                message.obj = temp;
                                message.what = 1;
//									handler_send.sendEmptyMessage(1);
                                handler_send.sendMessage(message);
                            }
                        }


                    });

        }
    }

    private void parseToHtml(String temp) {
        tView.setText(Html.fromHtml(temp,
                new Html.ImageGetter() {

                    Drawable drawable;
                    @Override
                    public Drawable getDrawable (String source) {
                        System.out.println(source+"-------2");
                        drawable=Drawable.createFromPath(imgsFileSrc.get(index++));
                        //设置图片边界
                        int width = tView.getWidth();
                        drawable.setBounds(0, 0, width,(int)(width*0.7));
                        return drawable;
                    }
                }, null));
    }

    /*
     * 分享
     */
    private void shareToOther(String url){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_like:
                saveLike();
                break;
            case R.id.iv_share:
                shareToOther(urlString);
                break;
            case R.id.iv_comment:
                break;
            default:
                break;
        }
    }

    private void saveLike() {
        Like like = new Like();
        like.setImg_url(bundle.getString("img_url"));
        like.setTitle(bundle.getString("title"));
        like.setTime(bundle.getString("time"));
        like.setUrl(urlString);
        LikeDao dao = new LikeDao(this);
        dao.add(like);
        ToastUtils.showToast(TestActivity.this,"收藏成功",0);
    }
}
