package ls.main.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netease.nim.demo.R;

import java.util.List;

import ls.main.adapter.ImageBrowserAdapter;
import ls.main.bean.InfolistItem;
import ls.main.utils.ToastUtils;

public class ImageBrowserActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager vp_image_brower;
    private TextView tv_image_index;
    private Button btn_save;
    private Button btn_original_image;

    private InfolistItem item;
    private int position;
    private ImageBrowserAdapter adapter;
    private List<String> imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_browser);

        initData();
        initView();
        setData();
    }

    private void initData() {
        item = (InfolistItem) getIntent().getSerializableExtra("item");
        position = getIntent().getIntExtra("position", 0);
        // 获取图片数据集合(单图也有对应的集合,集合的size为1)
        imgUrls = item.getImg_list();
    }

    private void initView() {
        vp_image_brower = (ViewPager) findViewById(R.id.vp_image_brower);
        tv_image_index = (TextView) findViewById(R.id.tv_image_index);
        btn_save = (Button) findViewById(R.id.btn_save);
//        btn_original_image = (Button) findViewById(R.id.btn_original_image);

        btn_save.setOnClickListener(this);
//        btn_original_image.setOnClickListener(this);
    }

    private void setData() {
        adapter = new ImageBrowserAdapter(this, imgUrls);
        vp_image_brower.setAdapter(adapter);

        final int size = imgUrls.size();
        int initPosition = Integer.MAX_VALUE / 2 / size * size + position;

        if(size > 1) {
            tv_image_index.setVisibility(View.VISIBLE);
            tv_image_index.setText((position+1) + "/" + size);
        } else {
            tv_image_index.setVisibility(View.GONE);
        }

        vp_image_brower.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int index = arg0 % size;
                tv_image_index.setText((index+1) + "/" + size);

//                PicUrls pic = adapter.getPic(arg0);
//                btn_original_image.setVisibility(pic.isShowOriImag() ?
//                        View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        vp_image_brower.setCurrentItem(initPosition);
    }

    @Override
    public void onClick(View v) {
        String picUrl = adapter.getPic(vp_image_brower.getCurrentItem());

        switch (v.getId()) {
            case R.id.btn_save:
                Bitmap bitmap = adapter.getBitmap(vp_image_brower.getCurrentItem());

                String fileName = picUrl.substring(picUrl.lastIndexOf("/"+1));

                String title = fileName.substring(0, fileName.lastIndexOf("."));
                String insertImage = MediaStore.Images.Media.insertImage(
                        getContentResolver(), bitmap, title, "BoreWBImage");
                if(insertImage == null) {
                    showToast("图片保存失败");
                } else {
                    showToast("图片保存成功");
                }

//			try {
//				ImageUtils.saveFile(this, bitmap, fileName);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

                break;

        }
    }

    private void showToast(String message){
        ToastUtils.showToast(this,message,0);
    }
}
