package com.dingli.diandiaan.instruc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.GoSetDialog;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/23.
 */
public class InsLocationActivity extends BaseActivity implements View.OnClickListener,LocationSource,AMapLocationListener{
    private MapView map;
    private AMap aMap;
    int zizeng;
    int zizengs;
    int firstone;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    double longitu;
    double laitu;
    String addressName;
    int id;
    String studentname;
    String gpsLocation;
    String gpsDetail;
    String signtime;
    TextView tvsuiloca;
    String phoneNumbers;
    int rollCallEverId;
    int types;
    ListView gralistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suilocation);
        map = (MapView) findViewById(R.id.suimap);
        map.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        id=getIntent().getIntExtra("studentId",0);
        signtime=getIntent().getStringExtra("signTime");
        gpsLocation=getIntent().getStringExtra("gpsLocation").split("/")[0];
        gpsDetail=getIntent().getStringExtra("gpsLocation").split("/")[1];
        studentname=getIntent().getStringExtra("studentName");
        ImageView locationback = (ImageView) findViewById(R.id.suilocationback);
        locationback.setOnClickListener(this);
        TextView tvstuname=(TextView)findViewById(R.id.tvstuname);
        TextView tvzhutype=(TextView)findViewById(R.id.tvzhutype);
        TextView  tvsuitime=(TextView)findViewById(R.id.tvsuitime);
        ImageView  ivsuiloc=(ImageView)findViewById(R.id.ivsuiloc);
        TextView tvsuiloca=(TextView)findViewById(R.id.tvsuiloca);
        TextView suiquanbu=(TextView) findViewById(R.id.suiquanbu);
        suiquanbu.setOnClickListener(this);
        tvstuname.setText(studentname);
        if (!TextUtils.isEmpty(signtime)){
            tvzhutype.setText("已提交");
            tvsuitime.setText(signtime);
            tvsuiloca.setText(gpsDetail);
            tvzhutype.setTextColor(getResources().getColor(R.color.qianblue));
            tvzhutype.setBackgroundResource(R.drawable.circle_cornersuiyi);
        }else{
            tvzhutype.setText("未提交");
            tvsuitime.setText("- - - -   - - - -   - - - -");
            tvsuiloca.setText("- - - -   - - - -   - - - -");
            tvzhutype.setTextColor(getResources().getColor(R.color.weizi));
            tvzhutype.setBackgroundResource(R.drawable.circle_cornersuiwei);
        }
        ivsuiloc.setOnClickListener(this);
        if (aMap==null){
            aMap=map.getMap();
        }
        setUpMap();
    }
    void setUpMap(){
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        laitu=Double.parseDouble(gpsLocation.split("-")[0]);
        longitu=Double.parseDouble(gpsLocation.split("-")[1]);
        double dlong=Double.valueOf(longitu);
        double dlat=Double.valueOf(laitu);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(dlat, dlong));
        markerOption.draggable(true);
        markerOption.setFlat(true);
        markerOptionlst.add(markerOption);
        aMap.addMarkers(markerOptionlst, true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.suiquanbu:
                Intent intent=new Intent(this,InsMoreLocationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.suilocationback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.ivsuiloc:
                if (DianTool.isConnectionNetWork(this)){
                    DianTool.huoqutoken();
                    HttpParams params=new HttpParams();
                    HttpHeaders headers=new HttpHeaders();
                    headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                    params.put("studentId",id);
                    OkGo.get(HostAdress.getRequest("/api/web/v1/user/info/rollcall")).tag(this).headers(headers).params(params)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    if (!TextUtils.isEmpty(s)) {
                                        ResultInfoCall resultInfoCall = JSON.parseObject(s, ResultInfoCall.class);
                                        if (!TextUtils.isEmpty(resultInfoCall.phoneNumber)) {
                                            if (!resultInfoCall.phoneNumber.equals("null")) {
                                                phoneNumbers = resultInfoCall.phoneNumber;
                                                callphone();
                                            } else {
                                                DianTool.showTextToast(InsLocationActivity.this, "这个同学没有手机号");
                                            }
                                        } else {
                                            DianTool.showTextToast(InsLocationActivity.this, "这个同学没有手机号");
                                        }
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    DianTool.showTextToast(InsLocationActivity.this,"这个同学没有手机号");
                                }
                            });
                }else{
                    DianTool.showTextToast(this,"请检查网络");
                }
                break;
        }
    }
    void drawMarkers(double longitu,double laitu){
        DianTool.showTextToast(this,longitu+"::"+laitu);
//             LatLng latLng = new LatLng(laitu, longitu);
//             Marker marker = aMap.addMarker(new MarkerOptions()
//                     .position(latLng));
//             aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        double dlong=Double.valueOf(longitu);
        double dlat=Double.valueOf(laitu);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(dlat, dlong));
        markerOption.draggable(true);
        markerOption.setFlat(true);
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    @TargetApi(Build.VERSION_CODES.M)
    void callphone(){
        if (DianTool.getsdkbanbe()>22){
            if (DianTool.getsdkbanbe()>22){
                int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                            101);
                    return;
                }
            }
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumbers));
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                firstone++;
                if (firstone==1){
                    GoSetDialog dialog=new GoSetDialog(this, "请开启应用定位权限及GPS", new GoSetDialog.SelectDialogButtonListener() {
                        @Override
                        public void checkButton(int id) {
                            switch (id){
                                case R.id.btnSelectDialogDetermineset:
                                    Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setOnceLocation(true);
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(2000);

            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
            //在activity中启动自定义本地服务LocationService
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
