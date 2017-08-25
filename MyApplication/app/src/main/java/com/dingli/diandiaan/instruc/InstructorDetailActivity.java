package com.dingli.diandiaan.instruc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.ServerApi;
import com.dingli.diandiaan.callname.InstructorDialog;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.DuiHuaDialog;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.dingli.diandiaan.instruc.adapter.InstrucAdapter;
import com.dingli.diandiaan.rollcall.ViewHolder;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/4/14.
 */

public class InstructorDetailActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener,ViewHolder.onCancelCollectListener{

    private ImageView instructordetailback;
    private TextView tvcancelinde,tvkaitime,tvluren,tvqianren;
    private XListView xlistdetal;
    private VerticalSwipeRefreshLayout refindetail;
    InstrucAdapter adapter;
    HttpHeaders headers;
    int rollCallEverId;
    List<ResultInfoCall> lismind;
    String phoneNumbers;
    int type;
    RelativeLayout rlindetail;
    FixPopupWindow pops;
    TextView recquanbu,recchidao,reczaotui,recq;
    TextView[] imgs;
    TextView spinnerins;
    int count;
    int success;
    LinearLayout llshai,llzong;
    ImageView ivdownup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructordetail);
        initView();
        initpops();
        refindetail.setRefreshing(true);
        initdata(type);
    }
    private void initView() {
        lismind=new ArrayList<>();
        rollCallEverId=getIntent().getIntExtra(Constant.TYPE,0);
        instructordetailback = (ImageView) findViewById(R.id.instructordetailback);
        rlindetail=(RelativeLayout) findViewById(R.id.rlindetail);
        tvcancelinde = (TextView) findViewById(R.id.tvcancelinde);
        tvcancelinde.setOnClickListener(this);
        tvkaitime=(TextView) findViewById(R.id.tvkaitime);
        tvqianren=(TextView) findViewById(R.id.tvqianren);
        tvluren=(TextView) findViewById(R.id.tvluren);
        ImageView ivfuzong=(ImageView) findViewById(R.id.ivfuzong);
        xlistdetal = (XListView) findViewById(R.id.xlistdetal);
        adapter=new InstrucAdapter(this,this);
        xlistdetal.setAdapter(adapter);
        refindetail = (VerticalSwipeRefreshLayout) findViewById(R.id.refindetail);
        llshai=(LinearLayout)findViewById(R.id.llshai);
        llzong=(LinearLayout)findViewById(R.id.llzong);
        DianTool.refresh(refindetail,this);
        xlistdetal.setXListViewListener(this);
        xlistdetal.setPullRefreshEnable(true);
        xlistdetal.setPullLoadEnable(false);
        instructordetailback.setOnClickListener(this);
        spinnerins=(TextView) findViewById(R.id.spinnerins);
        ivdownup=(ImageView) findViewById(R.id.ivdownup);
        ivdownup.setBackgroundResource(R.mipmap.next_gray);
        llshai.setOnClickListener(this);
        ivfuzong.setOnClickListener(this);
    }
    void initpops(){
        final View view= LayoutInflater.from(this).inflate(R.layout.popupwindow_instruc,null);
        recquanbu=(TextView) view.findViewById(R.id.recquanbu);
        recchidao=(TextView) view.findViewById(R.id.recchidao);
        reczaotui=(TextView) view.findViewById(R.id.reczaotui);
        recq=(TextView) view.findViewById(R.id.recq);
        imgs=new TextView[]{recquanbu,recchidao,reczaotui,recq};
        for (int i=0;i<imgs.length;i++){
            imgs[i].setOnClickListener(this);
        }
        pops=new FixPopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        view.getBackground().setAlpha(90);
        pops.setOutsideTouchable(false);
        pops.setFocusable(true);
        pops.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    initdiss();
                    return true;
                }
                return false;
            }
        });
        pops.setBackgroundDrawable(new BitmapDrawable());
        pops.setAnimationStyle(R.style.mypopwindow_anim_style);
        pops.update();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recq:
                initdiss();
                spinnerins.setText("请假");
                drawable(recq);
                type=3;
                initdata(type);
                break;
            case R.id.ivfuzong:
                if (success==1) {
                    Intent intent = new Intent(this, InsMoreLocationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
                break;
            case R.id.reczaotui:
                initdiss();
                spinnerins.setText("未提交");
                drawable(reczaotui);
                type=2;
                initdata(type);
                break;
            case R.id.recchidao:
                initdiss();
                spinnerins.setText("已提交");
                drawable(recchidao);
                type=1;
                initdata(type);
                break;
            case R.id.recquanbu:
                initdiss();
                spinnerins.setText("全部");
                drawable(recquanbu);
                type=0;
                initdata(type);
                break;
            case R.id.llshai:
                llshai.setBackgroundResource(R.drawable.circle_cornerintrc);
                ivdownup.setBackgroundResource(R.mipmap.icon_up);
                pops.showAsDropDown(llzong);
                break;
            case R.id.instructordetailback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.tvcancelinde:
                    DuiHuaDialog dialog=new DuiHuaDialog(this, "", "确认要关闭点名吗？", "","确认", new DuiHuaDialog.SelectDialogButtonListener() {
                        @Override
                        public void checkButton(int id) {
                            switch (id){
                                case R.id.btnok:
                                    if (DianTool.isConnectionNetWork(InstructorDetailActivity.this)) {
                                        guanbisui();
                                    }else{
                                        DianTool.showTextToast(InstructorDetailActivity.this,"请检查网络");
                                    }
                                    break;
                            }
                        }
                    });
                    dialog.show();
                break;
        }
    }
    void initdiss(){
        llshai.setBackgroundResource(R.color.bg_White);
        ivdownup.setBackgroundResource(R.mipmap.next_gray);
        pops.dismiss();
    }
    @Override
    public void onRefresh() {
      initdata(type);
    }
    @Override
    public void onLoadMore() {
    }
    @Override
    public void onCancelCollect(ImageView iv, RelativeLayout le, View v, int classId, final String className, int courseId, final long ids, int scheduleId, int studentScheduleId, final int teacherId, final String types, int userId, final String userName, ImageView checkbox) {

        InstructorDialog dialog=new InstructorDialog(this, "location", new InstructorDialog.SelectDialogButtonListener() {
            @Override
            public void checkButton(int id) {
                switch (id){
                    case R.id.linoutlocation:
                        Intent intent=new Intent(InstructorDetailActivity.this,InsLocationActivity.class);
                        intent.putExtra("signTime",userName);
                        intent.putExtra("studentName",className);
                        intent.putExtra("gpsLocation",types);
                        intent.putExtra("studentId",teacherId);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                        break;
                    case R.id.linclick:
                        initLeave(ids);
                        break;
                }
            }
        });
        dialog.show();
    }
    @Override
    public void refresh(final int i,final long ids) {
        if (DianTool.isConnectionNetWork(this)){
            InstructorDialog dialog=new InstructorDialog(this, "call", new InstructorDialog.SelectDialogButtonListener() {
                @Override
                public void checkButton(int id) {
                   switch (id){
                       case R.id.linoutlocation:
                           bocall(i);
                           break;
                       case R.id.linclick:
                           initLeave(ids);
                           break;
                   }
                }
            });
            dialog.show();
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    void  initLeave(long ids){
        if (DianTool.isConnectionNetWork(this)){
            DianTool.showDialog(this,"");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                 DianTool.dissMyDialog();
                }
            },5000);
            HttpParams params=new HttpParams();
            params.put("ID",ids);
            OkGo.get(HostAdress.getRequest(ServerApi.Instruc)).params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    initdata(type);
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                    DianTool.showTextToast(InstructorDetailActivity.this,"请假失败,请重新请假");
                }
            });
        }else{
            DianTool.showTextToast(this,"请检测网络");
        }
    }
    void bocall(int i){
        if (DianTool.isConnectionNetWork(this)) {
            HttpParams params = new HttpParams();
            params.put("studentId", i);
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
                                        DianTool.showTextToast(InstructorDetailActivity.this, "这个同学没有手机号");
                                    }
                                } else {
                                    DianTool.showTextToast(InstructorDetailActivity.this, "这个同学没有手机号");
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.showTextToast(InstructorDetailActivity.this, "这个同学没有手机号");
                        }
                    });
        }else{
            DianTool.showTextToast(this,"请检测网络");
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void callphone(){
        if (DianTool.getsdkbanbe()>22){
            if (DianTool.getsdkbanbe()>22){
                int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                            100);
                    return;
                }
            }
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumbers));
        startActivity(intent);
    }
    void initdata(int type){
        if (DianTool.isConnectionNetWork(this)){
            count=0;
            HttpParams params=new HttpParams();
            headers=new HttpHeaders();
            DianTool.huoqutoken();
            if (type==1){
                params.put("haveReport",true);
            }else if (type==2){
                params.put("haveReport",false);
            }else if (type==3){
                params.put("leaveStatus",true);
            }
            params.put("rollCallEverId",rollCallEverId);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/queryEverDetailV2")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    success=1;
                    refindetail.setRefreshing(false);
                    refindetail.setEnabled(false);
                    xlistdetal.stopRefresh();
                    DianTool.dissMyDialog();
                    if (!TextUtils.isEmpty(s)) {
                            CourseMing courseMings = JSON.parseObject(s, CourseMing.class);
                        if (courseMings.success) {
                            tvkaitime.setText(courseMings.data.get(0).openTime);
                            tvluren.setText(String.valueOf(courseMings.data.get(0).commitCount));
                            tvqianren.setText("/" + courseMings.data.get(0).totalCount);
                            if (lismind.size()!=0) {
                                lismind.clear();
                            }
                            for (int i = 1; i < courseMings.data.size(); i++) {
                                lismind.add(courseMings.data.get(i));
                                for (int j = 0; j < courseMings.data.get(i).classList.size(); j++) {
                                    count += courseMings.data.get(i).classList.size();
                                    if (!TextUtils.isEmpty(courseMings.data.get(i).classList.get(j).gpsLocation)) {
                                        if (!courseMings.data.get(i).classList.get(j).gpsLocation.equals("null")) {
                                            DianApplication.user.listgps.add(courseMings.data.get(i).classList.get(j).gpsLocation);
                                            DianApplication.user.liststud.add(courseMings.data.get(i).classList.get(j).studentId);
                                            DianApplication.user.listsname.add(courseMings.data.get(i).classList.get(j).studentName);
                                            DianApplication.user.listsign.add(courseMings.data.get(i).classList.get(j).signTime);
                                            DianApplication.user.listdetail.add(courseMings.data.get(i).classList.get(j).gpsDetail);
                                        }
                                    }
                                }
                            }
                            if (count > 10) {
                                adapter.refreshInstruclist(lismind);
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.refreshInstruclist(lismind);
                                xlistdetal.setAdapter(adapter);
                            }
                            if (courseMings.data.get(0).status == true) {
                                rlindetail.setVisibility(View.VISIBLE);
                            } else {
                                rlindetail.setVisibility(View.GONE);
                            }
                        }else{
                            if (lismind.size()!=0) {
                                lismind.clear();
                            }
                            adapter.refreshInstruclist(lismind);
                            xlistdetal.setAdapter(adapter);
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    success=1;
                    xlistdetal.stopRefresh();
                    refindetail.setRefreshing(false);
                    refindetail.setEnabled(false);
                    DianTool.dissMyDialog();
                   DianTool.response(response,InstructorDetailActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        DianApplication.user.listgps.clear();
        DianApplication.user.liststud.clear();
        DianApplication.user.listsname.clear();
        DianApplication.user.listsign.clear();
        DianApplication.user.listdetail.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callphone();
            }else{
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("该应用需要赋予访问定位的权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    callphone();
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
    void guanbisui(){
        tvcancelinde.setClickable(false);
        HttpParams params=new HttpParams();
        params.put("rollCallEverId",rollCallEverId);
        OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/closeEver")).tag(this).params(params)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                tvcancelinde.setClickable(true);
                DianTool.showTextToast(InstructorDetailActivity.this,"成功关闭");
                initdata(type);
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                tvcancelinde.setClickable(true);
            }
        });
    }
    void kaiqisui(){
        List<String> list=new ArrayList<>();
        list.add(String.valueOf(rollCallEverId));
        tvcancelinde.setClickable(false);
        HttpParams params=new HttpParams();
        params.putUrlParams("classIds",list);
        OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/openEverV2")).tag(this).params(params)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                tvcancelinde.setClickable(true);
                DianTool.showTextToast(InstructorDetailActivity.this,"成功开启");
                initdata(type);
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                tvcancelinde.setClickable(true);
            }
        });
    }
    void drawable(TextView tv){
        for (int i=0;i<imgs.length;i++){
            if (imgs[i]!=tv){
                imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
            }else{
                tv.setBackgroundResource(R.mipmap.icon_z);
            }
        }
    }
}
