package ls.main.fragments.mywidgt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class WrapHeightGridView extends GridView {

	public WrapHeightGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WrapHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapHeightGridView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int heightSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, heightSpec);
	}
}
