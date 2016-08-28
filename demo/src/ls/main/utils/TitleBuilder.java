package ls.main.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.demo.R;


public class TitleBuilder {

	private View viewTitle;
	private TextView tvTitle;
	private ImageView ivLeft;
	private ImageView ivRight;
	private TextView tvLeft;
	private TextView tvRight;

	public TitleBuilder(Activity context) {
		viewTitle = context.findViewById(R.id.rl_titlebar);
		tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv);
		ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
		ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
		tvLeft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
		tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
	}
	
	public TitleBuilder(View context) {
		if(context==null){
			Log.e("TAG","null=============================================================");
		}
		viewTitle = context.findViewById(R.id.rl_titlebar);
		tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv);
		ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
		ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
		tvLeft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
		tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
	}

	public void setLeftImgCenterCrop(){
		ivLeft.setScaleType(ImageView.ScaleType.MATRIX);
	}

	public void setTextColor(int color){
		tvTitle.setTextColor(color);
	}

	// title

	public TitleBuilder setTitleBgRes(int resid) {
		viewTitle.setBackgroundResource(resid);
		return this;
	}

	public TitleBuilder setTitleText(String text) {
		tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
				: View.VISIBLE);
		tvTitle.setText(text);
		return this;
	}

	// left

	public TitleBuilder setLeftImage(int resId) {
		ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
		ivLeft.setImageResource(resId);
		return this;
	}

	public TitleBuilder setLeftText(String text) {
		tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
		tvLeft.setText(text);
		return this;
	}

	public TitleBuilder setLeftOnClickListener(OnClickListener listener) {
		if (ivLeft.getVisibility() == View.VISIBLE) {
			ivLeft.setOnClickListener(listener);
		} else if (tvLeft.getVisibility() == View.VISIBLE) {
			tvLeft.setOnClickListener(listener);
		}
		return this;
	}

	// right

	public TitleBuilder setRightImage(int resId) {
		ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
		ivRight.setImageResource(resId);
		return this;
	}

	public TitleBuilder setRightText(String text) {
		tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
				: View.VISIBLE);
		tvRight.setText(text);
		return this;
	}

	public TitleBuilder setRightOnClickListener(OnClickListener listener) {
		if (ivRight.getVisibility() == View.VISIBLE) {
			ivRight.setOnClickListener(listener);
		} else if (tvRight.getVisibility() == View.VISIBLE) {
			tvRight.setOnClickListener(listener);
		}
		return this;
	}

	public View build() {
		return viewTitle;
	}


}
