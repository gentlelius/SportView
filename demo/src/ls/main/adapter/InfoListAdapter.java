package ls.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.netease.nim.demo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import ls.main.activities.ImageBrowserActivity;
import ls.main.bean.InfolistItem;
import ls.main.fragments.SearchFragment;
import ls.main.utils.ImageOptHelper;
import ls.main.utils.MyTimeUtils;

/**
 * Created by admin on 2016/8/23.
 */
public class InfoListAdapter extends BaseAdapter{

    private Context context;
    private List<InfolistItem> data;
    private boolean[] isLike;
    private ImageLoader imageLoader;
    public InfoListAdapter(Context context,List<InfolistItem> data){
        this.context = context;
        this.data = data;
        isLike = new boolean[2000];
        for(int i=0;i<isLike.length;i++){
            isLike[i] = false;
        }
        imageLoader = ImageLoader.getInstance();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public InfolistItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("TAG","       --------    "+position);
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_status,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tv_unick = (TextView) convertView.findViewById(R.id.tv_subhead);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_caption);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.include_status_image = (FrameLayout) convertView
                    .findViewById(R.id.include_status_image);
            viewHolder.include_bottombar = (LinearLayout) convertView.findViewById(R.id.ll_bottom_control);
            viewHolder.gv_images = (GridView) viewHolder.include_status_image
                    .findViewById(R.id.gv_images);
            viewHolder.iv_image = (ImageView) viewHolder.include_status_image
                    .findViewById(R.id.iv_image);
            viewHolder.ll_comment_bottom = (LinearLayout) convertView.findViewById(R.id.ll_comment_bottom);
            viewHolder.ll_share_bottom = (LinearLayout) convertView.findViewById(R.id.ll_share_bottom);
            viewHolder.ll_like_bottom = (LinearLayout) convertView.findViewById(R.id.ll_like_bottom);
            viewHolder.tv_comment_bottom = (TextView) convertView.findViewById(R.id.tv_comment_bottom);
            viewHolder.iv_comment_bottom = (ImageView) convertView.findViewById(R.id.iv_comment_bottom);
            viewHolder.tv_like_bottom = (TextView) convertView.findViewById(R.id.tv_likecount_bottom);
            viewHolder.iv_like_bottom = (ImageView) convertView.findViewById(R.id.iv_like_bottom);

            viewHolder.tv_share_bottom = (TextView) convertView.findViewById(R.id.tv_share_bottom);
            viewHolder.iv_share_bottom = (ImageView) convertView.findViewById(R.id.iv_share_bottom);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InfolistItem item = data.get(position);
        if(!isLike[position]){
            viewHolder.iv_like_bottom.setImageResource(R.drawable.timeline_icon_unlike);
        }else{
            viewHolder.iv_like_bottom.setImageResource(R.drawable.ic_selcted);
        }
        imageLoader.displayImage("",viewHolder.iv_icon,ImageOptHelper.getAvatarOptions());
        viewHolder.tv_unick.setText(item.getMUser().getUnick());
        viewHolder.tv_time.setText(MyTimeUtils.getModelTime(Long.parseLong(item.getTime())));
        viewHolder.tv_content.setText(item.getContent());
        setImages(item,viewHolder.include_status_image,viewHolder.gv_images,viewHolder.iv_image);
//        viewHolder.tv_share_bottom.setText("分享");
        viewHolder.tv_like_bottom.setText(String.valueOf(item.getLike_count()));
        viewHolder.tv_comment_bottom.setText(String.valueOf(item.getComment_count()));
        final ViewHolder vh = viewHolder;

        viewHolder.ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.ll_like_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_like = vh.tv_like_bottom;;
                if(!isLike[position]){
                    int new_count = Integer.parseInt(tv_like.getText().toString())+1;
                   tv_like.setText(String.valueOf(new_count));
                    item.setLike_count(new_count);
                   isLike[position] = true;
                    vh.iv_like_bottom.setImageResource(R.drawable.ic_selcted);
                    toServer(1,item.getId());
               }else {
                    int new_count = Integer.parseInt(tv_like.getText().toString())-1;
                    tv_like.setText(String.valueOf(new_count));
                    item.setLike_count(new_count);
                   isLike[position] = false;
                    vh.iv_like_bottom.setImageResource(R.drawable.timeline_icon_unlike);
                    toServer(0,item.getId());
               }
            }
        });
        viewHolder.ll_share_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
//     boolean isLike = false;
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ll_comment_bottom:
//                ToastUtils.showToast(context,"comment",0);
//                break;
//            case R.id.ll_like_bottom:
//                ToastUtils.showToast(context,"like",0);
//
//                break;
//            case R.id.ll_share_bottom:
//                ToastUtils.showToast(context,"share",0);
//                break;
//            default:
//                break;
//
//        }
//    }

    private void setImages(final InfolistItem item, FrameLayout imgContainer, GridView gv_imgs, ImageView iv_img){
        List<String> img_list = item.getImg_list();
        if(img_list!=null){
            if(img_list.size()>1){
                imgContainer.setVisibility(View.VISIBLE);
                gv_imgs.setVisibility(View.VISIBLE);
                iv_img.setVisibility(View.GONE);
                StatusGridImgsAdapter gvAdapter = new StatusGridImgsAdapter(context,img_list);
                gv_imgs.setAdapter(gvAdapter);
                gv_imgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, ImageBrowserActivity.class);
                        intent.putExtra("item",item);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                    }
                });
            }else{
                imgContainer.setVisibility(View.VISIBLE);
                gv_imgs.setVisibility(View.GONE);
                iv_img.setVisibility(View.VISIBLE);
                imageLoader.displayImage(img_list.get(0),iv_img, ImageOptHelper.getImgOptions());
                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,ImageBrowserActivity.class);
                        intent.putExtra("item",item);
                        context.startActivity(intent);
                    }
                });
            }
            imageLoader.setDefaultLoadingListener(new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    SearchFragment.mDilatingDotsProgressBar.hideNow();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }else {
            imgContainer.setVisibility(View.GONE);
        }

    }

    private void toServer(int op_type,int shuoshuo_id){
        String url = "http://192.168.1.7:8080/ISport/servlet/AddALike" + "?op_type="+op_type+"&shuoshuo_id="+shuoshuo_id;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(stringRequest);
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_unick;
        TextView tv_time;
        TextView tv_content;
        FrameLayout include_status_image;
        LinearLayout include_bottombar;
        GridView gv_images;
        ImageView iv_image;

        LinearLayout ll_share_bottom;
        ImageView iv_share_bottom;
        TextView tv_share_bottom;
        LinearLayout ll_comment_bottom;
        ImageView iv_comment_bottom;
        TextView tv_comment_bottom;
        LinearLayout ll_like_bottom;
        ImageView iv_like_bottom;
        TextView tv_like_bottom;
    }

}
