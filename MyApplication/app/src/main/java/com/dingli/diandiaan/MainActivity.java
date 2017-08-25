package com.dingli.diandiaan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.firstpage.HybridShouyeFragment;
import com.dingli.diandiaan.information.InformationFragment;
import com.dingli.diandiaan.login.LoginActivity;
import com.dingli.diandiaan.login.PersoncenterFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.umeng.message.PushAgent;

import java.util.List;

//import cn.hugeterry.updatefun.UpdateFunGO;
//import cn.hugeterry.updatefun.config.UpdateKey;
import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final String KEY_FRAGMENT_TAG = "fragment_tag";
    private static final String FRAGMENT_TAG_HOME = "home";
    private static final String FRAGMENT_TAG_PRODUCT = "kebiao";
    private static final String FRAGMENT_TAG_MY_ASSERT = "wo";
    private static final String FRAGMENT_TAG_XIAOXI = "xiaoxi";
    private String[] mFragmentTags = new String[]{FRAGMENT_TAG_HOME, FRAGMENT_TAG_PRODUCT,FRAGMENT_TAG_XIAOXI, FRAGMENT_TAG_MY_ASSERT};
    private String mFragmentCurrentTag = FRAGMENT_TAG_HOME;
    private  LinearLayout[] mLayouts = null;
    private int[] NORMAL_BACKGROUD = new int[]{R.mipmap.firstpagexuan,R.mipmap.kebiao,R.mipmap.xiaoxian,R.mipmap.mine};
    private SparseIntArray mImgBgMap = new SparseIntArray(NORMAL_BACKGROUD.length);
    RelativeLayout vpDianDian;
    long mWaitTime = 2000;
    long mTouchTime = 0;
    HybridShouyeFragment shouyeFragment;
//    SyllFragment syllbusFragment;
    PersoncenterFragment personcenterFragment;
    InformationFragment informationFragment;
    LinearLayout home_tab_main,home_tab_kebiao,home_tab_wo,home_tab_xiaoxi;
   public ImageView ivfirstpage,ivkebiao,ivmine,ivxiaoxi;
    TextView tvmine,tvfirstpage,tvkebiao,tvxiaoxi,tvinfom;
    String deviceId;
    HttpHeaders headers;
    int studeshu,qiandaoshu;
    int zizen=0;
    ResultInfo resultInfotoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreFragments();
            mFragmentCurrentTag =  savedInstanceState.getString(KEY_FRAGMENT_TAG);
        }
        setContentView(R.layout.activity_main);
       headers = new HttpHeaders();
        DianApplication.user.main=this;
        DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
        boolean mFirst = DianTool.isFirstEnter(MainActivity.this, MainActivity.this.getClass().getName());
        if(mFirst) {
            Intent mIntent = new Intent();
            mIntent.setClass(MainActivity.this, YiDaoTuActivity.class);
            startActivity(mIntent);
           this.finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        } else{
            init();
            setListener();
//            UpdateKey.API_TOKEN = "6dd794ecd5ffbe92dd7c53b0a44b2389";
//            UpdateKey.APP_ID = "57d12f64ca87a865bb001768";
//            UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;//通过通知栏来进行下载(默认)
//            UpdateFunGO.init(this);
            PgyCrashManager.register(this);
            PgyUpdateManager.register(MainActivity.this,
                    new UpdateManagerListener() {
                        @Override
                        public void onUpdateAvailable(final String result) {

                            // 将新版本信息封装到AppBean中
                            final AppBean appBean = getAppBeanFromString(result);
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("更新")
                                    .setMessage(appBean.getReleaseNote())
                                    .setPositiveButton(
                                            "确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
//                                                    startDownloadTask(
//                                                            MainActivity.this,
//                                                            appBean.getDownloadURL());
                                                    Intent intent=new Intent();
                                                    intent.setAction("android.intent.action.VIEW");
                                                    Uri content_url = Uri.parse("https://www.pgyer.com/MD7w");
                                                    intent.setData(content_url);
                                                    startActivity(intent);
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }

                        @Override
                        public void onNoUpdateAvailable() {
                        }
                    });
        }
    }
    private void initUMeng() {
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
//        mPushAgent.enable();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                do {
                    deviceId = mPushAgent.getRegistrationId();
                    if (TextUtils.isEmpty(DianApplication.user.token)) {
                    } else {
                        inithttp();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (TextUtils.isEmpty(deviceId));
            }
        }, 500);
    }
    void initoken(){
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITONE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, "");
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, "");
        DianApplication.user.token="";
        DianApplication.user.token_type="";
        if (DianTool.isConnectionNetWork(this)){
            DianApplication.user.accounts=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNT);
            DianApplication.user.passwords=DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD);
            if(!TextUtils.isEmpty(DianApplication.user.accounts)){
                HttpParams params1 = new HttpParams();
                headers.put("Content-Type", Constant.APPLICATION_FORMURL);
                headers.put("Encoding", "UTF-8");
                headers.put("Accept", Constant.APPLICATION_JSON);
                headers.put("Authorization", "Basic ZGxlZHVBcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==");
                params1.put("username", DianApplication.user.accounts);
                params1.put("password", DianApplication.user.passwords);
                params1.put("grant_type", "password");
                params1.put("scope", "read write");
                params1.put("client_secret", "mySecretOAuthSecret");
                params1.put("client_id", "dleduApp");
                OkGo.getInstance().addCommonHeaders(headers);
                OkGo.post(HostAdress.getRequestUrl("/oauth/token"))     // 请求方式和请求url
                        .tag(this).params(params1)                       // 请求的 tag, 主要用于取消对应的请求
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                // s 即为所需要的结果
                                resultInfotoken = JSON.parseObject(s, ResultInfo.class);
                                DianApplication.user.token = resultInfotoken.access_token;
                                DianApplication.user.token_type = resultInfotoken.token_type;
                                DianApplication.user.refresh_token = resultInfotoken.refresh_token;
                                String[] strings = resultInfotoken.access_token.split("-");
                                DianApplication.sharedPreferences.saveString(Constant.SPLITONE, strings[0]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, strings[1]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, strings[2]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, strings[3]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, strings[4]);
                                DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, resultInfotoken.access_token);
                                DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, resultInfotoken.token_type);
                                DianApplication.sharedPreferences.saveString(Constant.REFRESHED, resultInfotoken.refresh_token);
                                initinform(resultInfotoken);
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                            }
                        });
            }
        }else{
            DianTool.showTextToast(MainActivity.this,"请检查网络");
        }

    }
    void inithttp() {
            //教师端的推送(请假)
           HttpParams params=new HttpParams();
            params.put("androidTeacher", deviceId);
        headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
        OkGo.getInstance().addCommonHeaders(headers);
        OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/postdevicetoken")).tag(this).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                DianTool.dissMyDialog();
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.dissMyDialog();
            }
        });
    }
    void initinform(ResultInfo resultInfo){
        if(DianTool.isConnectionNetWork(this)){
//            DianApplication.user.token_type=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
//            DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE)+"-"+
//                    DianApplication.sharedPreferences.getStringValue(Constant.SPLITTWO)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITTHREE)
//                    +"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFOUR)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFIVE);
            headers.put("Authorization", resultInfo.token_type + " " + resultInfo.access_token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/getstatus")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    zizen=1;
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            if (arg.get(i).module.equals("leave")) {
                                if (!TextUtils.isEmpty(arg.get(i).function)) {
                                    if (arg.get(i).function.equals("teacher_approval")) {
                                        studeshu=arg.get(i).notRead;
                                    if (arg.get(i).function.equals("teacher_notice")) {
                                        qiandaoshu=arg.get(i).notRead;
                                    }
                                   if (studeshu+qiandaoshu!=0){
                                       tvinfom.setVisibility(View.VISIBLE);
                                       tvinfom.setText(String.valueOf(studeshu+qiandaoshu));
                                   }
                                }
                            } else {
                                   tvinfom.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    if (response==null){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        if (response.code() == 401) {
                            Initoken.initoken(MainActivity.this);
                        }
                    }
                }
            });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    void  restoreFragments() {
        FragmentManager manager =getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        shouyeFragment = (HybridShouyeFragment) manager.findFragmentByTag(FRAGMENT_TAG_HOME);
//        syllbusFragment = (SyllFragment) manager.findFragmentByTag(FRAGMENT_TAG_PRODUCT);
        personcenterFragment = (PersoncenterFragment) manager.findFragmentByTag(FRAGMENT_TAG_MY_ASSERT);
        informationFragment = (InformationFragment) manager.findFragmentByTag(FRAGMENT_TAG_XIAOXI);
        transaction.show(shouyeFragment);
//        transaction.hide(syllbusFragment);
        transaction.hide(personcenterFragment);
        transaction.hide(informationFragment);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_FRAGMENT_TAG, mFragmentCurrentTag);
        super.onSaveInstanceState(outState);
    }

    public void init(){
        tvinfom=(TextView)findViewById(R.id.tvinfom);
        vpDianDian=(RelativeLayout)findViewById(R.id.vpDianDian);
        home_tab_main=(LinearLayout)findViewById(R.id.home_tab_main);
        home_tab_kebiao=(LinearLayout)findViewById(R.id.home_tab_kebiao);
        home_tab_wo=(LinearLayout)findViewById(R.id.home_tab_wo);
        home_tab_xiaoxi=(LinearLayout)findViewById(R.id.home_tab_xiaoxi);
        ivfirstpage=(ImageView)findViewById(R.id.ivfirstpage);
        ivkebiao=(ImageView)findViewById(R.id.ivkebiao);
        ivmine=(ImageView)findViewById(R.id.ivmine);
        ivxiaoxi=(ImageView)findViewById(R.id.ivxiaoxi);
        tvfirstpage=(TextView)findViewById(R.id.tvfirstpage);
        tvkebiao=(TextView)findViewById(R.id.tvkebiao);
        tvmine=(TextView)findViewById(R.id.tvmine);
        tvxiaoxi=(TextView)findViewById(R.id.tvxiaoxi);
        home_tab_main.setTag(new TabViewHolder(ivfirstpage, tvfirstpage));
        home_tab_kebiao.setTag(new TabViewHolder(ivkebiao, tvkebiao));
        home_tab_wo.setTag(new TabViewHolder(ivmine, tvmine));
        home_tab_xiaoxi.setTag(new TabViewHolder(ivxiaoxi, tvxiaoxi));
        mLayouts = new LinearLayout[]{home_tab_main,home_tab_kebiao,home_tab_xiaoxi, home_tab_wo};
        mImgBgMap.put(R.id.ivfirstpage,R.mipmap.firstpage);
        mImgBgMap.put(R.id.ivkebiao,R.mipmap.keibaoxuan);
        mImgBgMap.put(R.id.ivxiaoxi,R.mipmap.xiaoxiliang);
        mImgBgMap.put(R.id.ivmine,R.mipmap.minexuan);
    }
    private void setListener() {
        for (int i = 0; i < mLayouts.length; i++) {
            mLayouts[i].setOnClickListener(this);
        }
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.KEY))){
            mFragmentCurrentTag=FRAGMENT_TAG_XIAOXI;
            DianApplication.sharedPreferences.saveString(Constant.KEY,"");
        }
        if (TextUtils.equals(FRAGMENT_TAG_HOME, mFragmentCurrentTag)) {
            home_tab_main.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_PRODUCT, mFragmentCurrentTag)) {
            home_tab_kebiao.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_MY_ASSERT, mFragmentCurrentTag)) {
            home_tab_wo.performClick();
        }else if (TextUtils.equals(FRAGMENT_TAG_XIAOXI, mFragmentCurrentTag)) {
            home_tab_xiaoxi.performClick();
        }
    }
    /**
     * 先全部隐藏
     * @param fragmentManager
     * @param transaction
     */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < mFragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(mFragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }
    /**
     * 设置底部背景为正常状态
     */
    private void setNormalBackgrounds() {
        for (int i = 0; i < mLayouts.length; i++) {
            setTabBackgroud(mLayouts[i], NORMAL_BACKGROUD[i], R.color.qianblue);
        }
    }
    /**
     * 设置底部背景为选中状态
     */
    private void setSelectedBackgroud(LinearLayout linearLayout) {
        TabViewHolder tabViewHolder = (TabViewHolder) linearLayout.getTag();
        int imgResId = mImgBgMap.get(tabViewHolder.img.getId());
        setTabBackgroud(linearLayout, imgResId, R.color.qianblue);
    }
    private void setTabBackgroud(LinearLayout linearLayout, int imgResId, int colorResId) {
        TabViewHolder tabViewHolder = (TabViewHolder) linearLayout.getTag();
        tabViewHolder.img.setImageResource(imgResId);
        tabViewHolder.txt.setTextColor(getResources().getColor(colorResId));
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
       if(event.getAction()==KeyEvent.ACTION_DOWN
               &&KeyEvent.KEYCODE_BACK==keyCode){
           long currentTime=System.currentTimeMillis();
           if((currentTime-mTouchTime)>=mWaitTime){
               DianTool.showTextToast(this,getResources().getString(R.string.zaichu));
               mTouchTime=currentTime;
           }else{
               finish();
           }
           DianApplication.user.libiao=null;
           return true;
       }
       return super.onKeyDown(keyCode,event);
        }
        @Override
        public void onClick(View v) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            hideFragments(manager, transaction);
            setNormalBackgrounds();
            switch (v.getId()){
                case R.id.home_tab_main:
                    if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                        if (zizen == 0) {
                            initoken();
                        }
                    }
                    DianApplication.user.libiao=null;
                    setSelectedBackgroud((LinearLayout)v);
                    mFragmentCurrentTag = FRAGMENT_TAG_HOME;
                    if (shouyeFragment == null) {
                        shouyeFragment = HybridShouyeFragment.newInstance();
                        transaction.add(R.id.vpDianDian, shouyeFragment, FRAGMENT_TAG_HOME);
                    }
                    transaction.show(shouyeFragment);
                break;
            case R.id.home_tab_kebiao:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                }else {
                    initinform(resultInfotoken);
                    DianApplication.user.libiao = "kebiao";
                    setSelectedBackgroud((LinearLayout) v);
                    mFragmentCurrentTag = FRAGMENT_TAG_PRODUCT;
//                    if (syllbusFragment == null) {
//                        syllbusFragment = SyllFragment.newInstance();
//                        transaction.add(R.id.vpDianDian, syllbusFragment, FRAGMENT_TAG_PRODUCT);
//                    }
//                    transaction.show(syllbusFragment);
                }
                break;
                case R.id.home_tab_xiaoxi:
                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                        Intent intent=new Intent(this,LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                    }else {
                        tvinfom.setVisibility(View.GONE);
                        DianApplication.user.libiao = "infor";
                        v.clearAnimation();
                        tvinfom.setVisibility(View.GONE);
                        setSelectedBackgroud((LinearLayout) v);
                        mFragmentCurrentTag = FRAGMENT_TAG_XIAOXI;
                        if (informationFragment == null) {
                            informationFragment = InformationFragment.newInstance();
                            transaction.add(R.id.vpDianDian, informationFragment, FRAGMENT_TAG_XIAOXI);
                        }
                        transaction.show(informationFragment);
                    }
                    break;
            case R.id.home_tab_wo:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                }else {
                    initinform(resultInfotoken);
                    DianApplication.user.libiao = "mine";
                    setSelectedBackgroud((LinearLayout) v);
                    mFragmentCurrentTag = FRAGMENT_TAG_MY_ASSERT;
                    if (personcenterFragment == null) {
                        personcenterFragment = PersoncenterFragment.newInstance();
                        transaction.add(R.id.vpDianDian, personcenterFragment, FRAGMENT_TAG_MY_ASSERT);
                    }
                    transaction.show(personcenterFragment);
                }
                break;
        }
            transaction.commit();
    }

    public void mainactivity_onclick(View view){
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        setNormalBackgrounds();
        switch (view.getId()){
            case R.id.kebiao:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }else {
                    LinearLayout v1 = (LinearLayout) findViewById(R.id.home_tab_kebiao);
                    setSelectedBackgroud(v1);
                    mFragmentCurrentTag = FRAGMENT_TAG_PRODUCT;
//                    if (syllbusFragment == null) {
//                        syllbusFragment = SyllFragment.newInstance();
//                        transaction.add(R.id.vpDianDian, syllbusFragment, FRAGMENT_TAG_PRODUCT);
//                    } else {
//                        transaction.show(syllbusFragment);
//                    }
                }
                break;
//            case R.id.kebiao_ivback:
//                LinearLayout v0 = (LinearLayout) findViewById(R.id.home_tab_main);
//                setSelectedBackgroud(v0);
//                mFragmentCurrentTag = FRAGMENT_TAG_HOME;
//                if (shouyeFragment == null) {
//                    shouyeFragment = HybridShouyeFragment.newInstance();
//                    transaction.add(R.id.vpDianDian, syllbusFragment, FRAGMENT_TAG_HOME);
//                }
//                transaction.show(shouyeFragment);
//                break;
        }
        transaction.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            initUMeng();
        }
//        UpdateFunGO.onResume(this);
        if (TextUtils.isEmpty(DianApplication.user.libiao)){
                mFragmentCurrentTag=FRAGMENT_TAG_HOME;
            }else if(DianApplication.user.libiao.equals("kebiao")){
                mFragmentCurrentTag=FRAGMENT_TAG_PRODUCT;
            }else if(DianApplication.user.libiao.equals("infor")){
              mFragmentCurrentTag=FRAGMENT_TAG_XIAOXI;
            }else if(DianApplication.user.libiao.equals("mine")){
             mFragmentCurrentTag=FRAGMENT_TAG_MY_ASSERT;
            }
        if (TextUtils.equals(FRAGMENT_TAG_HOME, mFragmentCurrentTag)) {
            home_tab_main.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_PRODUCT, mFragmentCurrentTag)) {
            home_tab_kebiao.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_MY_ASSERT, mFragmentCurrentTag)) {
            home_tab_wo.performClick();
        }else if (TextUtils.equals(FRAGMENT_TAG_XIAOXI, mFragmentCurrentTag)) {
            home_tab_xiaoxi.performClick();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
//        UpdateFunGO.onStop(this);
        PgyUpdateManager.unregister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    private static class TabViewHolder {
    public ImageView img;
    public TextView txt;
    public TabViewHolder(ImageView img, TextView txt) {
        this.img = img;
        this.txt = txt;
    }
}
}


