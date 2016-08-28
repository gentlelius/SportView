package ls.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.demo.R;

import java.util.List;

import ls.main.bean.OptionBean;

//F9924C   C381FC  68AAF4  5BB035

/**
 * Created by admin on 2016/8/26.
 */
public class OptionAdapter extends BaseAdapter {

    private Context context;
    private List<OptionBean> data;

    public OptionAdapter(Context context, List<OptionBean> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.option_item,null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_item_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_item_content);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OptionBean bean = (OptionBean) getItem(position);
        viewHolder.title.setText(bean.getTitle());
        viewHolder.title.setTextColor(bean.getTitle_color());
        viewHolder.content.setText(bean.getContent());
        viewHolder.icon.setImageResource(bean.getIcon_url());
        return convertView;
    }

    static class ViewHolder{
        TextView title;
        TextView content;
        ImageView icon;
    }

}
