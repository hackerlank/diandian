package com.dingli.diandiaan.firstpage;


import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ant.liao.GifView;
import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.Coursecenter;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.NetUtil;
import com.dingli.diandiaan.common.ResultOne;
import com.dingli.diandiaan.firstpage.adapter.GlideImageLoader;
import com.dingli.diandiaan.firstpage.adapter.ShouYeAdapter;
import com.dingli.diandiaan.firstpage.adapter.ShouYeGridAdapter;
import com.dingli.diandiaan.information.EntrtyActivity;
import com.dingli.diandiaan.instruc.InstructorActivity;
import com.dingli.diandiaan.login.LoginActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebsActivity;
import com.dingli.diandiaan.login.pingjiao.PingjiaoActivity;
import com.dingli.diandiaan.lostproperty.ImgFileListActivity;
import com.dingli.diandiaan.lostproperty.WebViewLostActivity;
import com.dingli.diandiaan.newProject.moudle.camera.CaptureActivity;
import com.dingli.diandiaan.newProject.utils.ToastUtils;
import com.dingli.diandiaan.rule.RuleActivity;
import com.dingli.diandiaan.setting.HelpActivity;
import com.dingli.diandiaan.view.MyGrideView;
import com.dingli.diandiaan.view.MyListView;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.github.mikephil.charting.data.LineRadarDataSet;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;


public class ShouyeFragment extends Fragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{

    private List<ResultOne> listid;
    private ListView listView;
    ShouYeAdapter shouyeadapter;
    VerticalSwipeRefreshLayout refresh;
    MyGrideView myshouye;
    LinearLayout lineaone,lineatwo;
    LinearLayout[] llall;
    LinearLayout tvduanwang;
    Banner banner;
    ResultOne resultOned;
    TextView tvSaoSao;
    public static ShouyeFragment newInstance(){
        ShouyeFragment shouyeFragment=new ShouyeFragment();
        return  shouyeFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_blank, container, false);
        initView(v);
        refresh.setRefreshing(true);
        initdata();
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            if (DianApplication.sharedPreferences.getStringValue(Constant.ROLE).equals("ROLE_CLASSROOMTEACHE")){
                initbanner();
            }else {
                bannerv2();
            }
        }else{
            initbanner();
        }
        return  v;
    }
    private void initView(View v) {
        listView= (ListView) v.findViewById(R.id.listView);
        refresh=(VerticalSwipeRefreshLayout)v.findViewById(R.id.refreshLi);
        DianTool.refresh(refresh,getActivity());
        tvSaoSao= (TextView) v.findViewById(R.id.tvSaoSao);
        listView.addHeaderView(getViews());
        shouyeadapter=new ShouYeAdapter();
        listView.setAdapter(shouyeadapter);
       tvduanwang=(LinearLayout)v.findViewById(R.id.tvduanwang);
        TextView textnames=(TextView)v.findViewById(R.id.tvblank);
        if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.ORGAINER))){
            textnames.setText("首页");
        }else{
            textnames.setText(DianApplication.sharedPreferences.getStringValue(Constant.ORGAINER));
        }
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
                if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    if (DianApplication.sharedPreferences.getStringValue(Constant.ROLE).equals("ROLE_CLASSROOMTEACHE")){
                        initbanner();
                    }else {
                        bannerv2();
                    }
                }else{
                    DianApplication.sharedPreferences.saveString("homepage","");
                    initbanner();
                }
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
            Intent intent=new Intent();
            intent.setClass(getActivity(),LoginActivity.class);
            startActivity(intent);
            return;
        }
        tvSaoSao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(ShouyeFragment.this.getContext(), Manifest.permission.CAMERA)) {
                    ShouyeFragment.this.startActivity(new Intent(getActivity(), CaptureActivity.class));
                } else {
                    EasyPermissions.requestPermissions(ShouyeFragment.this.getActivity(), ShouyeFragment.this.getString(R.string.permission_hint),
                            CAMERA_PERMISSIONS, Manifest.permission.CAMERA);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.dianming:
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(getActivity(), LoginActivity.class);
                } else {
                    intent.setClass(getActivity(), RuleActivity.class);
                }
                startintent(intent);
                break;
            case R.id.suill:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                    startintent(intent);
                }else {
                    Initoken.SingId(getActivity());
                }
                break;
            case R.id.veryll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), EntrtyActivity.class);
                }
                startintent(intent);
                break;
            case R.id.chakanll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), FirstPageFragment.class);
                }
                startintent(intent);
                break;
            case R.id.kebiao:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), PingjiaoActivity.class);
                }
                startintent(intent);
                break;
            case R.id.daochu_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), DaochuWebsActivity.class);
                }
                startintent(intent);
                break;
            case R.id.school_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), DaochuWebActivity.class);
                    intent.putExtra("url", "/mobileui/schoolNews");
                    intent.putExtra("title", "校园动态");
                    intent.putExtra("isRefresh", false);
                    intent.putExtra("isStatusBar", true);
                    intent.putExtra("domainName", "dd_mobile");
                }
                startintent(intent);
                break;
            case R.id.zhiye_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewOneActivity.class);
                    intent.putExtra("url","/mobileui_hy");
                    intent.putExtra("title","");
                }
                startintent(intent);
                break;
            case R.id.diandian_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewsActivity.class);
                    intent.putExtra("url", "/mobileui/allArticle");
                }
                startintent(intent);
                break;
            case R.id.rollcall_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), InstructorActivity.class);
                }
                startintent(intent);
                break;
            case R.id.hyall:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewsActivity.class);
                    intent.putExtra("url", "/mobileui/allArticle");
                }
                startintent(intent);
                break;
            default:
                DianTool.showTextToast(getActivity(),"当前版本不支持此功能，请升级最新版本");
                break;
        }
    }
    void startintent(Intent intent){
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }

    private View getViews(){
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shouye,null);
        LinearLayout hyall=(LinearLayout) v.findViewById(R.id.hyall);
        TextView tvmachine=(TextView) v.findViewById(R.id.tvmachine);
        TextView tvsuill=(TextView) v.findViewById(R.id.tvsuill);
        TextView tvverd=(TextView) v.findViewById(R.id.tvverd);
        TextView tvchake=(TextView) v.findViewById(R.id.tvchake);
        TextView tvkenumb=(TextView) v.findViewById(R.id.tvkenumb);
        TextView tvdaochull=(TextView) v.findViewById(R.id.tvdaochull);
        TextView tvschool=(TextView) v.findViewById(R.id.tvschool);
        TextView tvzhiye=(TextView) v.findViewById(R.id.tvzhiye);
        TextView tvdianxin=(TextView) v.findViewById(R.id.tvdianxin);
        TextView tvrollca=(TextView) v.findViewById(R.id.tvrollca);
        LinearLayout suill=(LinearLayout) v.findViewById(R.id.suill);
        LinearLayout veryll=(LinearLayout) v.findViewById(R.id.veryll);
        LinearLayout chakanll=(LinearLayout) v.findViewById(R.id.chakanll);
        LinearLayout dianming= (LinearLayout) v.findViewById(R.id.dianming);
        LinearLayout kebiao= (LinearLayout) v.findViewById(R.id.kebiao);
        LinearLayout daochu_ll= (LinearLayout) v.findViewById(R.id.daochu_ll);
        LinearLayout school_ll= (LinearLayout) v.findViewById(R.id.school_ll);
        LinearLayout zhiye_ll= (LinearLayout) v.findViewById(R.id.zhiye_ll);
        LinearLayout diandian_ll= (LinearLayout) v.findViewById(R.id.diandian_ll);
        LinearLayout rollcall_ll= (LinearLayout) v.findViewById(R.id.rollcall_ll);
        ImageView ivcardmachine=(ImageView) v.findViewById(R.id.ivcardmachine);
        lineaone=(LinearLayout) v.findViewById(R.id.lineaone);
        lineatwo=(LinearLayout) v.findViewById(R.id.lineatwo);
        ImageView ivsuill=(ImageView) v.findViewById(R.id.ivsuill);
        ImageView ivverd=(ImageView) v.findViewById(R.id.ivverd);
        ImageView ivchake=(ImageView) v.findViewById(R.id.ivchake);
        ImageView ivkenumb=(ImageView) v.findViewById(R.id.ivkenumb);
        ImageView ivdaochull=(ImageView) v.findViewById(R.id.ivdaochull);
        ImageView ivschool=(ImageView) v.findViewById(R.id.ivschool);
        ImageView ivzhiye=(ImageView) v.findViewById(R.id.ivzhiye);
        ImageView ivdianxin=(ImageView) v.findViewById(R.id.ivdianxin);
        ImageView ivrollcall=(ImageView) v.findViewById(R.id.ivrollcall);
        myshouye=(MyGrideView) v.findViewById(R.id.myshouye);
        banner = (Banner) v.findViewById(R.id.myviewPager);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent=new Intent();
                if (resultOned!=null){
                    if (resultOned.banner.length!=0){
                        for (int i=0;i<resultOned.banner.length;i++){
                            String targetTitle=resultOned.banner[i].targetTitle;
                            String targetUrl=resultOned.banner[i].targetUrl.trim();
                            if (position==i){
                                if (targetUrl.contains("helperCenter")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        intent.setClass(getActivity(), HelpActivity.class);
                                    }
                                }else if (targetUrl.contains("lostproperty")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        intent.setClass(getActivity(), WebViewLostActivity.class);
                                    }
                                }else if (targetUrl.contains("article?")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        String id = targetUrl.substring(targetUrl.indexOf("?") + 1);
                                        intent.setClass(getActivity(), WebViewFiveActivity.class);
                                        intent.putExtra("targetTitle", targetTitle);
                                        intent.putExtra("targetUrl", targetUrl);
                                        intent.putExtra("id", id);
                                    }
                                }else if (targetUrl.contains("allArticle")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        intent.setClass(getActivity(), WebViewsActivity.class);
                                        intent.putExtra("url", "/mobileui/allArticle");
                                    }
                                }else{
                                    intent.setClass(getActivity(), WebViewTwoActivity.class);
                                    intent.putExtra("targetTitle", targetTitle);
                                    intent.putExtra("targetUrl", targetUrl);
                                }
                                getActivity().startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }
                        }
                    }
                }
            }
        });
        hyall.setOnClickListener(this);
        llall=new LinearLayout[]{zhiye_ll,diandian_ll,rollcall_ll,veryll,chakanll,kebiao,daochu_ll,school_ll,suill,dianming};
        for (int i=0;i<llall.length;i++){
            llall[i].setOnClickListener(this);
        }
        listid = new ArrayList<ResultOne>();
        return v;
    }
    void falseclick(){
        for (int i=0;i<llall.length;i++){
            llall[i].setClickable(false);
        }
    }
    void trueclick(){
        for (int i=0;i<llall.length;i++){
            llall[i].setClickable(true);
        }
    }
    void initbanner(){
        String json=DianApplication.sharedPreferences.getStringValue("homepage");
        if (TextUtils.isEmpty(json)) {
            if (DianTool.isConnectionNetWork(getActivity())) {
                falseclick();
                tvduanwang.setVisibility(View.GONE);
                HttpParams params = new HttpParams();
                params.put("role", "teacher");
                params.put("version","V2");
                OkGo.get(HostAdress.getRequest("/api/phone/v1/listHomePageV2")).tag(this)
                        .params(params).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        initnorefresh();
                        if (!TextUtils.isEmpty(s)) {
                            reshouye(s);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        initnorefresh();
                        visible();
                        trueclick();
                    }
                });
            } else {
                trueclick();
                initnorefresh();
                tvduanwang.setVisibility(View.VISIBLE);
                DianTool.showTextToast(getActivity(), getResources().getString(R.string.net));
            }
        }else{
            initnorefresh();
            reshouye(json);
        }
    }
    void bannerv2(){
        if (DianTool.isConnectionNetWork(getActivity())){
            HttpParams params = new HttpParams();
            params.put("role", "teacher");
            params.put("version","V2");
            HttpHeaders headers=new HttpHeaders();
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/listHomePageV2WithAuthorization")).tag(this).headers(headers)
                    .params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    initnorefresh();
                    if (!TextUtils.isEmpty(s)) {
                        reshouye(s);
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    initnorefresh();
                    visible();
                    trueclick();
                }
            });
        }else{
            trueclick();
            initnorefresh();
            tvduanwang.setVisibility(View.VISIBLE);
            DianTool.showTextToast(getActivity(), getResources().getString(R.string.net));
        }
    }
    void visible(){
        myshouye.setVisibility(View.GONE);
        lineaone.setVisibility(View.VISIBLE);
        lineatwo.setVisibility(View.VISIBLE);
    }
    void reshouye(String s){
        List<String> imgUrl=new ArrayList<>();
        imgUrl.clear();
        ResultOne resultOne = JSON.parseObject(s, ResultOne.class);
        resultOned=resultOne;
        for (int i = 0; i < resultOne.banner.length; i++) {
            listid.add(resultOne.banner[i]);
            imgUrl.add(resultOne.banner[i].iconUrl);
        }
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imgUrl);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        if (resultOne.menu.size() != 0) {
            lineaone.setVisibility(View.GONE);
            lineatwo.setVisibility(View.GONE);
            myshouye.setVisibility(View.VISIBLE);
                ShouYeGridAdapter grideadapter = new ShouYeGridAdapter(resultOne.menu);
            myshouye.setAdapter(grideadapter);
        } else {
            visible();
            trueclick();
        }
    }
    void initnorefresh(){
        refresh.setRefreshing(false);
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(getActivity())){
            OkGo.get(HostAdress.getWenZhang("/api/web/v1/articleManagement/articleManagementShow/articleList4")).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    initnorefresh();
                    DianApplication.sharedPreferences.saveString("listbiao",s);
                    if (!TextUtils.isEmpty(s)){
                        if (!s.equals("[]")) {
                            List<ResultOne> coursecenter = JSON.parseArray(s, ResultOne.class);
                            shouyeadapter.refreshShouYe(coursecenter);
                            shouyeadapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    initnorefresh();
                    if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue("listbiao"))){
                        String s=DianApplication.sharedPreferences.getStringValue("listbiao");
                        if (!TextUtils.isEmpty(s)) {
                            if (!s.equals("[]")) {
                                List<ResultOne> coursecenter = JSON.parseArray(s, ResultOne.class);
                                shouyeadapter.refreshShouYe(coursecenter);
                                shouyeadapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }else{
            initnorefresh();
            DianTool.showTextToast(getActivity(),getResources().getString(R.string.net));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    //以下为相机权限管理
    private static final int CAMERA_PERMISSIONS = 0x001;
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            startActivity(new Intent(getActivity(), CaptureActivity.class));
        } else {
            ToastUtils.showLong(getContext(), R.string.permission_hint_content);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == CAMERA_PERMISSIONS) {
            ToastUtils.showLong(getContext(), R.string.permission_hint_content);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

