package com.dingli.diandiaan.instruc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
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
import java.util.List;

import anet.channel.util.StringUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/23.
 */
public class InsMoreLocationActivity extends BaseActivity implements View.OnClickListener,LocationSource,AMapLocationListener,AMap.OnMarkerClickListener {
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
    String phoneNumbers;
    int rollCallEverId;
    int types;
    int index=0;
    private List<MarkerOptions> markers=new ArrayList<MarkerOptions>();
    private List<Marker> mList=new ArrayList<Marker>();
    StringUtils strutils=new StringUtils();
    private ArrayList<String> hslist=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morelocation);
        map = (MapView) findViewById(R.id.suimap);
        map.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        ImageView locationback = (ImageView) findViewById(R.id.suilocationback);
        locationback.setOnClickListener(this);
        if (aMap==null){
            aMap=map.getMap();
        }
        setUpMap();
    }
    @TargetApi(Build.VERSION_CODES.M)
    void setUpMap(){
        aMap.setOnMarkerClickListener(this);
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        if(DianApplication.user.listgps.size()!=0){
            for (int i=0;i<DianApplication.user.listgps.size();i++) {
                String coordinatearr=DianApplication.user.listgps.get(i);
                        String longitude=coordinatearr.split("-")[1];
                        String latitude=coordinatearr.split("-")[0];
                        double dlong=Double.valueOf(longitude);
                        double dlat=Double.valueOf(latitude);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(new LatLng(dlat, dlong));
                        markerOption.draggable(true);
                        markerOption.setFlat(true);
                        markerOptionlst.add(markerOption);
                    }
            mList = aMap.addMarkers(markerOptionlst, true);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.suilocationback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    void drawMarkers(double longitu,double laitu,String addressName){
             LatLng latLng = new LatLng(laitu, longitu);
             Marker marker = aMap.addMarker(new MarkerOptions()
                     .position(latLng));
             aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//                drawMarkers(longitu,laitu,addressName);// 添加10个带有系统默认icon的marker
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpMap();
                map.onResume();
            }else{
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("该应用需要赋予访问定位的权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                                    setUpMap();
                                  map.onResume();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    return;
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0;i<mList.size();i++){
            if (marker.equals(mList.get(i))) {
                if (aMap != null) {
                    Intent intent=new Intent(this,InsLocationActivity.class);
                    intent.putExtra("signTime",DianApplication.user.listsign.get(i));
                    intent.putExtra("studentName",DianApplication.user.listsname.get(i));
                    intent.putExtra("gpsLocation",DianApplication.user.listgps.get(i)+"/"+DianApplication.user.listdetail.get(i));
                    intent.putExtra("studentId",DianApplication.user.liststud.get(i));
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                }
            }
        }
        return false;
    }
}
