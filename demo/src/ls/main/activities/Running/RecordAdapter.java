package ls.main.activities.Running;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netease.nim.demo.R;

import java.util.List;

import ls.main.bean.PathRecord;


public class RecordAdapter extends BaseAdapter {

	private Context ctx;
    private List<PathRecord> list;
    
    public RecordAdapter(Context context, List<PathRecord> list) {
        this.ctx = context;
        this.list = list;
    }
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		 return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.recorditem, null);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.record = (TextView) convertView
                   .findViewById(R.id.record);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PathRecord item = list.get(position);
        holder.date.setText(item.getDate());
        holder.record.setText(item.toString());
        return convertView;
	}

	private class ViewHolder {
		TextView date;
        TextView record;
    }
}
