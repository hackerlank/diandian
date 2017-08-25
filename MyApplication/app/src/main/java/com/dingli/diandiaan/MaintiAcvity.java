package com.dingli.diandiaan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.model.MarkerOptions;
import com.dingli.diandiaan.bean.BaseTipsView;
import com.dingli.diandiaan.bean.ShowMemberTipsView;
import com.dingli.diandiaan.bean.ShowMoreTipsView;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.GengXiDialog;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.login.FirstAlterActivity;
import com.dingli.diandiaan.login.LoginActivity;
import com.dingli.diandiaan.view.NoScrollViewPager;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/11.
 */
public class MaintiAcvity extends BaseActivity implements View.OnClickListener{
    NoScrollViewPager vpdiandian;
   LinearLayout home_tab_main,home_tab_kebiao,home_tab_xiaoxi,home_tab_wo;
    ImageView ivfirstpage,ivkebiao,ivxiaoxi,ivmine;
    TextView tvinfom;
    long mWaitTime = 2000;
    long mTouchTime = 0;
    String deviceId;
    HttpHeaders headers;
    int zizen=0;
    int studeshu,qiandaoshu,leaveshu;
    private ShowMoreTipsView mShowMoreTipsView;
    private ShowMemberTipsView mShowMemberTipsView;
    TextView tvfirstpage,tvkebiao,tvxiaoxi,tvmine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        DianApplication.user.main=this;
        boolean mFirst = DianTool.isFirstEnter(MaintiAcvity.this, MaintiAcvity.this.getClass().getName());
        headers = new HttpHeaders();
        if(mFirst) {
            Intent mIntent = new Intent();
            mIntent.setClass(MaintiAcvity.this, YiDaoTuActivity.class);
            startActivity(mIntent);
            this.finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        } else {
            initView();
            MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
            vpdiandian.setAdapter(adapter);
            shoumoreTips();
            if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                DianTool.huoqutoken();
                initUMeng();
                initform();
                initbanben();
                if (DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD).equals("123456")){
                    Intent intent = new Intent(MaintiAcvity.this, FirstAlterActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
    void initView(){
        tvfirstpage=(TextView) findViewById(R.id.tvfirstpage);
        tvkebiao=(TextView) findViewById(R.id.tvkebiao);
        tvxiaoxi=(TextView) findViewById(R.id.tvxiaoxi);
        tvmine=(TextView) findViewById(R.id.tvmine);
        tvinfom=(TextView)findViewById(R.id.tvinfom);
        ivfirstpage=(ImageView)findViewById(R.id.ivfirstpage);
        ivkebiao=(ImageView)findViewById(R.id.ivkebiao);
        ivxiaoxi=(ImageView)findViewById(R.id.ivxiaoxi);
        ivmine=(ImageView)findViewById(R.id.ivmine);
        vpdiandian=(NoScrollViewPager)findViewById(R.id.vpDianDian);
        home_tab_main=(LinearLayout)findViewById(R.id.home_tab_main);
        home_tab_kebiao=(LinearLayout)findViewById(R.id.home_tab_kebiao);
        home_tab_xiaoxi=(LinearLayout)findViewById(R.id.home_tab_xiaoxi);
        home_tab_wo=(LinearLayout)findViewById(R.id.home_tab_wo);
        home_tab_wo.setOnClickListener(this);
        home_tab_xiaoxi.setOnClickListener(this);
        home_tab_kebiao.setOnClickListener(this);
        home_tab_main.setOnClickListener(this);
        ivfirstpage.setBackgroundResource(R.mipmap.firstpage);
        ivkebiao.setBackgroundResource(R.mipmap.kebiao);
        ivxiaoxi.setBackgroundResource(R.mipmap.xiaoxian);
        ivmine.setBackgroundResource(R.mipmap.mine);
        vpdiandian.setOffscreenPageLimit(4);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.home_tab_main:
                  DianApplication.user.libiao=null;
                  vpdiandian.setCurrentItem(0);
                  tabone();
              if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                  if (tvinfom.getVisibility()!=View.VISIBLE) {
                      initform();
                  }
              }
              break;
          case R.id.home_tab_kebiao:
              if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                  Intent intent=new Intent(this,LoginActivity.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
              }else {
                  DianApplication.user.libiao="kebiao";
                  vpdiandian.setCurrentItem(2);
                  tabtwo();
                  if (tvinfom.getVisibility()!=View.VISIBLE) {
                      initform();
                  }
              }
              break;
          case R.id.home_tab_xiaoxi:
              if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                  Intent intent=new Intent(this,LoginActivity.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
              }else {
                  DianApplication.user.libiao="infor";
                  vpdiandian.setCurrentItem(1);
                  tabthree();
              }
              break;
          case R.id.home_tab_wo:
              if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                  Intent intent=new Intent(this,LoginActivity.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
              }else {
                  DianApplication.user.libiao="mine";
                  vpdiandian.setCurrentItem(3);
                  tabfour();
                  if (tvinfom.getVisibility()!=View.VISIBLE) {
                      initform();
                  }
              }
              break;
      }
    }
   void initform(){
       tvinfom.setVisibility(View.GONE);
       studeshu=0;
       qiandaoshu=0;
       leaveshu=0;
       if(DianTool.isConnectionNetWork(this)){
           DianTool.huoqutoken();
           headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
           OkGo.get(HostAdress.getRequest("/api/phone/v1/getstatus")).tag(this)
                   .headers(headers).execute(new StringCallback() {
               @Override
               public void onSuccess(String s, Call call, Response response) {
                   zizen = 1;
                   if (s.length() != 0) {
                       List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                       for (int i = 0; i < arg.size(); i++) {
                           if (arg.get(i).module.equals("leave")) {
                               if (!TextUtils.isEmpty(arg.get(i).function)) {
                                   if (arg.get(i).function.equals("teacher_approval")) {
                                       studeshu = arg.get(i).notRead;
                                   }
                                   if (arg.get(i).function.equals("teacher_notice")) {
                                       qiandaoshu = arg.get(i).notRead;
                                   }
                               }
                           }
                           if (arg.get(i).module.equals("electricFence")) {
                               if (!TextUtils.isEmpty(arg.get(i).function)) {
                                   if (arg.get(i).function.equals("leaveTeacher_notice")) {
                                       leaveshu = arg.get(i).notRead;
                                   }
                               }
                           }
                       }
                   }
                   if (studeshu + qiandaoshu +leaveshu != 0) {
                       tvinfom.setVisibility(View.VISIBLE);
                       tvinfom.setText(String.valueOf(studeshu + qiandaoshu+leaveshu));
                   } else {
                       tvinfom.setVisibility(View.GONE);
                   }
               }

               @Override
               public void onError(Call call, Response response, Exception e) {
                   if (response == null) {
                       Intent intent = new Intent(MaintiAcvity.this, LoginActivity.class);
                       startActivity(intent);
                       MaintiAcvity.this.finish();
                       overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                   } else {
                       if (response.code() == 401) {
                           Initoken.initoken(MaintiAcvity.this);
                       }
                   }
               }
           });
       }else{
           DianTool.showTextToast(this, "请检查网络");
       }
   }
    void tabone(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpage);
        ivkebiao.setBackgroundResource(R.mipmap.contact);
        ivxiaoxi.setBackgroundResource(R.mipmap.xiaoxian);
        ivmine.setBackgroundResource(R.mipmap.mine);
        tvfirstpage.setTextColor(getResources().getColor(R.color.qianblued));
        tvkebiao.setTextColor(getResources().getColor(R.color.wenzis));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.wenzis));
        tvmine.setTextColor(getResources().getColor(R.color.wenzis));
    }
    void tabtwo(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpagexuan);
        ivkebiao.setBackgroundResource(R.mipmap.contact_click);
        ivxiaoxi.setBackgroundResource(R.mipmap.xiaoxian);
        ivmine.setBackgroundResource(R.mipmap.mine);
        tvfirstpage.setTextColor(getResources().getColor(R.color.wenzis));
        tvkebiao.setTextColor(getResources().getColor(R.color.qianblued));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.wenzis));
        tvmine.setTextColor(getResources().getColor(R.color.wenzis));
    }
    void tabthree(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpagexuan);
        ivkebiao.setBackgroundResource(R.mipmap.contact);
        ivxiaoxi.setBackgroundResource(R.mipmap.xiaoxiliang);
        ivmine.setBackgroundResource(R.mipmap.mine);
        tvinfom.setVisibility(View.GONE);
        tvfirstpage.setTextColor(getResources().getColor(R.color.wenzis));
        tvkebiao.setTextColor(getResources().getColor(R.color.wenzis));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.qianblued));
        tvmine.setTextColor(getResources().getColor(R.color.wenzis));
    }
    void tabfour(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpagexuan);
        ivkebiao.setBackgroundResource(R.mipmap.contact);
        ivxiaoxi.setBackgroundResource(R.mipmap.xiaoxian);
        ivmine.setBackgroundResource(R.mipmap.minexuan);
        tvfirstpage.setTextColor(getResources().getColor(R.color.wenzis));
        tvkebiao.setTextColor(getResources().getColor(R.color.wenzis));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.wenzis));
        tvmine.setTextColor(getResources().getColor(R.color.qianblued));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.KEY))){
            DianApplication.user.libiao=DianApplication.sharedPreferences.getStringValue(Constant.KEY);
        }
        if (TextUtils.isEmpty(DianApplication.user.libiao)){
            vpdiandian.setCurrentItem(0);
            tabone();
        }else if(DianApplication.user.libiao.equals("kebiao")){
            vpdiandian.setCurrentItem(2);
            tabtwo();
        }else if(DianApplication.user.libiao.equals("infor")){
            vpdiandian.setCurrentItem(1);
            DianApplication.sharedPreferences.saveString(Constant.KEY,"");
            tabthree();
            tvinfom.setVisibility(View.GONE);
        }else if(DianApplication.user.libiao.equals("mine")){
            vpdiandian.setCurrentItem(3);
            tabfour();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        PgyUpdateManager.unregister();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    private void initUMeng() {
              PushAgent mPushAgent = PushAgent.getInstance(this);
                    deviceId=mPushAgent.getRegistrationId();
              if (TextUtils.isEmpty(DianApplication.user.dev)) {
                  if (TextUtils.isEmpty(deviceId)) {
                      deviceId=mPushAgent.getRegistrationId();
                  } else {
                      inithttp(deviceId);
                  }
              }else{
                  inithttp(DianApplication.user.dev);
              }
    }
    void inithttp(String sd) {
        //教师端的推送(请假)
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            HttpParams params = new HttpParams();
            params.put("androidTeacher", sd);
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
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
            if (mShowMoreTipsView != null) {
                if (mShowMoreTipsView.isShowing()) {
                    mShowMoreTipsView.dismiss(this);
                    return true;
                }
            }
            if (mShowMemberTipsView != null) {
                if (mShowMemberTipsView.isShowing()) {
                    mShowMemberTipsView.dismiss(this);
                    return true;
                }
            }
//            long currentTime=System.currentTimeMillis();
//            if((currentTime-mTouchTime)>=mWaitTime){
//                DianTool.showTextToast(this, getResources().getString(R.string.zaichu));
//                mTouchTime=currentTime;
//            }else{
//                finish();
//            }
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    void shoumoreTips(){
        if (mShowMoreTipsView == null) {
            mShowMoreTipsView = new ShowMoreTipsView(this);
        }
        mShowMoreTipsView.setOnCloseListener(new ShowMoreTipsView.OnCloseListener() {
            @Override
            public void onClose(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MaintiAcvity.this);
            }
        });
        mShowMoreTipsView.setOnSureListener(new ShowMoreTipsView.OnSureListener() {
            @Override
            public void onSure(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MaintiAcvity.this);
                showMemberTipsView();
            }
        });
        mShowMoreTipsView.show(this);
    }
    private void showMemberTipsView() {
        if (mShowMemberTipsView == null) {
            mShowMemberTipsView = new ShowMemberTipsView(this);
        }
        mShowMemberTipsView.setOnCloseListener(new ShowMemberTipsView.OnCloseListener() {
            @Override
            public void onClose(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MaintiAcvity.this);
            }
        });
        mShowMemberTipsView.setOnSureListener(new ShowMemberTipsView.OnSureListener() {
            @Override
            public void onSure(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MaintiAcvity.this);
            }
        });
        mShowMemberTipsView.show(this);
    }
    void initbanben(){
        if (DianTool.isConnectionNetWork(this)) {
            HttpHeaders headers = new HttpHeaders();
            HttpParams params = new HttpParams();
            headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
            params.put("type", "android");
            params.put("role", "teacher");
            OkGo.get(HostAdress.getBanben("/api/web/v1/upgrade/getInfo")).tag(this)
                    .params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("{}")) {
                            Result result = JSON.parseObject(s, Result.class);
                            boolean isRequre=result.isRequired;
                            String str = result.version.substring(0, 1) + result.version.substring(2, 3) + result.version.substring(4, 5);
                            String strs = DianApplication.code.substring(0, 1) + DianApplication.code.substring(2, 3) + DianApplication.code.substring(4, 5);
                            if (Integer.parseInt(str) > Integer.parseInt(strs)) {
                                GengXiDialog dialog = new GengXiDialog(MaintiAcvity.this, isRequre,result.versionDescrip, new GengXiDialog.SelectDialogButtonListener() {
                                    @Override
                                    public void checkButton(int id) {
                                        switch (id) {
                                            case R.id.btnSelectDialogDetermineguan:
                                                DianTool.genxin(MaintiAcvity.this);
                                                DianTool.kongtoken();
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
                public void onError(Call call, Response response, Exception e) {
                }
            });
        }
    }
}
