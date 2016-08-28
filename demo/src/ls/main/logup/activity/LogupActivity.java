package ls.main.logup.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.netease.nim.demo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ls.main.logup.myutil.mydialog.ActionSheetDialog;
import ls.main.logup.myutil.mydialog.MyAlertDialog;
import ls.main.logup.myutil.photo.CropOption;
import ls.main.logup.myutil.photo.CropOptionAdapter;
import ls.main.logup.myutil.view.wheelcity.AddressData;
import ls.main.logup.myutil.view.wheelcity.OnWheelChangedListener;
import ls.main.logup.myutil.view.wheelcity.WheelView;
import ls.main.logup.myutil.view.wheelcity.adapters.AbstractWheelTextAdapter;
import ls.main.logup.myutil.view.wheelcity.adapters.ArrayWheelAdapter;
import ls.main.logup.myutil.view.wheelview.JudgeDate;
import ls.main.logup.myutil.view.wheelview.ScreenInfo;
import ls.main.logup.myutil.view.wheelview.WheelMain;
import ls.main.utils.ToastUtils;

//import ls.main.logup.myutil.view.wheelview.WheelMain;

public class LogupActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Uri imgUri;

    private int index,len;
    private ViewFlipper viewFlipper;
    private EditText et_uname;
    private Button btn_next1,btn_next2;
    private TextView tv_hometown,tv_birthday,tv_tianjia;
    private WheelMain wheelMain;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ImageButton  btn_photo,btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);
        initViews();

    }

    class MyTextWatch implements TextWatcher{

            private int index;
            public MyTextWatch(int index){
                this.index = index;
            }

            private boolean flag ;

            public boolean getFlag(){
                return  this.flag;
            }



            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TAG",s.toString()+s.length());

                if(s.length()>0){
//                    btn_next1.setBackgroundResource(R.drawable.nextstep_selector);
//                    btn_next1.setEnabled(true);
                    flag = true;
                }else{

//                    btn_next1.setBackgroundColor(0xffb0b0b0);
//                    btn_next1.setEnabled(false);
                    flag = false;
                }

                switch (index){
                    case 1:
                       setBtnStyle(btn_next1,firstPagrIsOk());
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }

            }
        }
    class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    if(index!=0){
                        viewFlipper.showPrevious();
                        index--;
                    }else{
                    }

                    break;
                case R.id.btn_next1:
                    if(index!=len-1){
                        viewFlipper.showNext();
                       index++;
                    }else {

                    }
                    break;
                case R.id.btn_next2:
                    if(index!=len-1){
                        viewFlipper.showNext();
                        index++;
                    }
                    showConfirmDialog();
                    break;
                case R.id.tv_birthday:
                    showDateDialog();
                    setBtnStyle(btn_next2,secondPagrIsOk());
                    break;
                case R.id.tv_hometown:
                    showAreaDialog();
                    setBtnStyle(btn_next2,secondPagrIsOk());
                    break;
                case R.id.btn_photo:
                    showPhotoDialog();
                    break;
                default:
                    break;
            }
        }
    }


    private void initViews(){
        viewFlipper = (ViewFlipper) findViewById(R.id.reg_vf_viewflipper);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.layout_fragment_unick,null);
        View view2 = layoutInflater.inflate(R.layout.layout_fragment_ubaseinfo,null);
        final View[] views = {view1,view2};
        len = views.length;
        for(int i=0;i<views.length;i++){
            viewFlipper.addView(views[i]);
        }
        tv_tianjia = (TextView) view2.findViewById(R.id.tv_tianjia);
        btn_next1 = (Button) view1.findViewById(R.id.btn_next1);
        btn_next2 = (Button) view2.findViewById(R.id.btn_next2);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_photo = (ImageButton) view2.findViewById(R.id.btn_photo);
         sexSelector = new SexSelector((LinearLayout) findViewById(R.id.sex_selector),this);
        MyOnclickListener mlistener = new MyOnclickListener();
        btn_next1.setOnClickListener(mlistener);
        btn_next2.setOnClickListener(mlistener);
        btn_back.setOnClickListener(mlistener);
        btn_photo.setOnClickListener(mlistener);

        et_uname = (EditText) view1.findViewById(R.id.et_uname);
        unameTextWatch = new MyTextWatch(1);
        et_uname.addTextChangedListener(unameTextWatch);

        tv_birthday = (TextView) view2.findViewById(R.id.tv_birthday);
        tv_hometown = (TextView) view2.findViewById(R.id.tv_hometown);
        tv_hometown.setOnClickListener(mlistener);
        tv_birthday.setOnClickListener(mlistener);
    }
    private void showDateDialog(){
//        Calendar calendar = Calendar.getInstance();
//        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//		/*月份从0计数*/
//                String theDate = String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth);
//                System.out.println(theDate);
//                tv_birthday.setText(theDate);
//                tv_birthday.setTextColor(0xffa4adb7);
////                btnChooseDate.setText(theDate);
//            }
//        },calendar.get(Calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
        LayoutInflater inflater1 = LayoutInflater.from(this);
        final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
                null);
        ScreenInfo screenInfo1 = new ScreenInfo(this);
         wheelMain = new WheelMain(timepickerview1);
        wheelMain.screenheight = screenInfo1.getHeight();
        Calendar calendar1 = Calendar.getInstance();
        String time1 = calendar1.get(Calendar.YEAR)+"-"+calendar1.get(Calendar.MONTH)+1+"-"+calendar1.get(Calendar.DAY_OF_MONTH);
        if (JudgeDate.isDate(time1, "yyyy-MM-dd")) {
            try {
                calendar1.setTime(dateFormat.parse(time1));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year1, month1, day1);
        final MyAlertDialog dialog = new MyAlertDialog(this)
                .builder()
                .setTitle("选择日期")
                // .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                // .setEditText("1111111111111")
                .setView(timepickerview1)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.setPositiveButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),
//                        wheelMain.getTime(), 0).show();
//                ToastUtils.showToast(getApplicationContext(),wheelMain.getTime(),0);
                tv_birthday.setText(wheelMain.getTime());
            }
        });
        dialog.show();

    }
    private void showAreaDialog(){
        View view = dialogm();
        final MyAlertDialog dialog1 = new MyAlertDialog(this)
                .builder()
                .setTitle("填写地址")
                // .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                // .setEditText("1111111111111")
                .setView(view)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog1.setPositiveButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), cityTxt, 1).show();
//                ToastUtils.showToast(getApplicationContext(),cityTxt,0);
                tv_hometown.setText(cityTxt);
            }
        });
        dialog1.show();
    }

    private SexSelector sexSelector;
    private MyTextWatch unameTextWatch;

    private boolean secondPagrIsOk(){
        if(!(sexSelector.getSelctedSex().equals(""))
                && !(tv_birthday.getText().toString().equals("选择生日"))
                && !(tv_hometown.getText().toString().equals("选择家乡"))){
            return  true;
        }
        return false;
    }
    private boolean firstPagrIsOk(){
        if(unameTextWatch.getFlag()){
            return true;
        }
        return  false;
    }
    private void setBtnStyle(Button btn,boolean isOk){
        Log.e("TAG",isOk+"--------------=================");
        if(isOk){
            btn.setBackgroundResource(R.drawable.nextstep_selector);
            btn.setEnabled(true);
        }else{
            btn.setBackgroundColor(0xffb0b0b0);
            btn.setEnabled(false);
        }
    }

    public void checkBySexSelector(){
        setBtnStyle(btn_next2,secondPagrIsOk());
    }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意：");
        builder.setMessage("注册成功后性别不能再更改,是否继续？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do next
                RequestParams params = new RequestParams();

                String json = "{\"name\":\"Obb\",\"age\":12}";
                params.addBodyParameter("name1",json);
                params.addBodyParameter("photo",new File(photo_path));
                HttpUtils utils = new HttpUtils();
                utils.send(
                        HttpRequest.HttpMethod.POST,
                        "http://192.168.1.7:8080/ISport/servlet/DoLogup",
                        params,
                        new RequestCallBack<Object>() {

                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                super.onLoading(total, current, isUploading);
                            }

                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {

                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {

                            }
                        }
                );
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    private void showPhotoDialog(){
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("用相机更换头像", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                getPhotoFromCamera();
                            }
                        })
                .addSheetItem("去相册选择头像", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                getPhotoFromFile();
                            }
                        }).show();
    }

    private View dialogm() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(R.id.wheelcity_country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(this));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setVisibleItems(0);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(R.id.wheelcity_ccity);
        ccity.setVisibleItems(0);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(city, cities, newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + "|"
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + "|"
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(ccity, ccities, country.getCurrentItem(),
                        newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + "|"
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + "|"
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + "|"
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + "|"
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    private String cityTxt;
    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }


    private void getPhotoFromFile(){
        // 方式1，直接打开图库，只能选择图库的图片
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 方式2，会先让用户选择接收到该请求的APP，可以从文件系统直接选取图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_FILE);
    }
    private void getPhotoFromCamera(){
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "avatar_"
                + String.valueOf(System.currentTimeMillis())
                + ".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);
                }
                break;
        }
    }


    private void doCrop() {

        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            ToastUtils.showToast(this,"can't find crop app",0);
            return;
        } else {
            intent.setData(imgUri);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            // only one
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                // many crop app
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("choose a app");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imgUri != null) {
                            getContentResolver().delete(imgUri, null, null);
                            imgUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            btn_photo.setImageBitmap(mBitmap);
            btn_photo.setBackgroundColor(Color.TRANSPARENT);
            tv_tianjia.setVisibility(View.GONE);
            photo_path = Environment.getExternalStorageDirectory() + "/crop_"
                    + System.currentTimeMillis() + ".png";
            saveBitmap(photo_path, mBitmap);
        }
    }

    public static   String photo_path;
    /**
     * save the crop bitmap
     *
     * @param fileName
     * @param mBitmap
     */
    public void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
