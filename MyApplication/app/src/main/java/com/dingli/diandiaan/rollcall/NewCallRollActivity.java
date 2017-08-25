package com.dingli.diandiaan.rollcall;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.DuiHuaDialog;
import com.dingli.diandiaan.common.DuiHuaDialogs;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.dingli.diandiaan.firstpage.callset.CallSetActivity;
import com.dingli.diandiaan.rollcall.sousuo.SouSuoActivity;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/25.
 */
public class NewCallRollActivity extends BaseActivity implements View.OnClickListener,ViewHolder.onCancelCollectListener,XListView.IXListViewListener{
    XListView xlistnew;
    PopupWindow pop,pops;
    RelativeLayout rlzhuangtai;
    boolean firstIn=true;
    int keid;
    NewRollCallAdapter adapter;
    int classIds;String classNames;int courseIds;long ids;int scheduleIds;int studentScheduleIds;
    int teacherIds;String types;int userIds;String userNames;
    HttpHeaders headers;
    String trueorfalse;
    LinearLayout llbianji,llbianjizt;
    TextView newbianji,tvsousuo,tvyanz,tvcancelout,tvcancelin,tvquanbus;
    int count;
    List<String> list,listid;
    String coursename;
    String code;
    String type;
    ResultInfoCall resultInfo;
    RelativeLayout llbgs;
    TextView[] imgs;
    VerticalSwipeRefreshLayout refnewcall;
    TextView recquanbu,recchidao,reczaotui,reckuangke,recqingjia,recyidao,recweiyijiao,recyichang;
    View viewqi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcallroll);
        keid=getIntent().getIntExtra(Constant.KE_ID,0);
        initview();
        initpop();
        initpops();
        Point p=new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth=p.x;
        int screenHeight=p.y;
        adapter=new NewRollCallAdapter(this,pop,this,"",screenHeight,screenWidth);
        xlistnew.setAdapter(adapter);
        xlistnew.setXListViewListener(this);
        xlistnew.setPullRefreshEnable(true);
    }
  void initview(){
      coursename=getIntent().getStringExtra(Constant.COURSE_NAME);
      trueorfalse=getIntent().getStringExtra(Constant.ISSHOOL);
      headers=new HttpHeaders();
      headers.put("Content-Type", Constant.APPLICATION_FORMURL);
      headers.put("Encoding", "UTF-8");
      headers.put("Accept", Constant.APPLICATION_JSON);
      refnewcall=(VerticalSwipeRefreshLayout)findViewById(R.id.refnewcall);
      refnewcall.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
              getResources().getColor(R.color.holo_green_light),
              getResources().getColor(R.color.holo_orange_light),
              getResources().getColor(R.color.holo_red_light));
      xlistnew=(XListView)findViewById(R.id.xlistnew);
      xlistnew.setPullLoadEnable(false);
      xlistnew.setPullRefreshEnable(true);
      llbianji=(LinearLayout)findViewById(R.id.llbianji);
      tvcancelout=(TextView)findViewById(R.id.tvcancelout);
      tvcancelin=(TextView)findViewById(R.id.tvcancelin);
      tvyanz=(TextView)findViewById(R.id.tvyanz);
      newbianji=(TextView)findViewById(R.id.newbianji);
       tvsousuo=(TextView)findViewById(R.id.tvsousuo);
      ImageView newback=(ImageView)findViewById(R.id.newback);
      llbianjizt=(LinearLayout)findViewById(R.id.llbianjizt);
      rlzhuangtai=(RelativeLayout)findViewById(R.id.rlzhuangtai);
      tvquanbus=(TextView)findViewById(R.id.tvquanbus);
      viewqi=findViewById(R.id.viewqi);
      tvquanbus.setOnClickListener(this);
      tvcancelin.setOnClickListener(this);
      tvcancelout.setOnClickListener(this);
      newbianji.setOnClickListener(this);
      tvsousuo.setOnClickListener(this);
      newback.setOnClickListener(this);
  }
    void initpops(){
     final View view=LayoutInflater.from(this).inflate(R.layout.popupwindow_changes,null);
        recquanbu=(TextView) view.findViewById(R.id.recquanbu);
        recchidao=(TextView) view.findViewById(R.id.recchidao);
        reczaotui=(TextView) view.findViewById(R.id.reczaotui);
        recqingjia=(TextView) view.findViewById(R.id.recqingjia);
        reckuangke=(TextView) view.findViewById(R.id.reckuangke);
        recyichang=(TextView) view.findViewById(R.id.recyichang);
        recyidao=(TextView) view.findViewById(R.id.recyidao);
        recweiyijiao=(TextView) view.findViewById(R.id.recweiyijiao);
        imgs=new TextView[]{recquanbu,recchidao,reczaotui,recqingjia,reckuangke,recyidao,recweiyijiao,recyichang};
        for (int i=0;i<imgs.length;i++){
            imgs[i].setOnClickListener(this);
        }
     final View tvshengyu=view.findViewById(R.id.tvshengyu);
        tvshengyu.setOnClickListener(this);
        tvshengyu.getBackground().setAlpha(90);
     pops=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
     view.getBackground().setAlpha(90);
     pops.setOutsideTouchable(false);
     pops.setFocusable(true);
        pops.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pops.dismiss();
                    return true;
                }
                viewqi.setVisibility(View.VISIBLE);
                tvquanbus.setBackgroundResource(R.drawable.circle_corner);
                tvquanbus.setTextColor(getResources().getColor(R.color.qianblue));
                return false;
            }
        });
        pops.setBackgroundDrawable(new BitmapDrawable());
     pops.setAnimationStyle(R.style.mypopwindow_anim_style);
     pops.update();
    }
    void initpop(){
        View view= LayoutInflater.from(this).inflate(R.layout.popupwindow_change,null);
        list=new ArrayList<>();
        listid=new ArrayList<>();
        TextView tvyidao=(TextView)view.findViewById(R.id.tvyidao);
        TextView tvlaste=(TextView)view.findViewById(R.id.tvlaste);
        TextView tvearly=(TextView)view.findViewById(R.id.tvearly);
        TextView tvkuds=(TextView)view.findViewById(R.id.tvkuds);
        TextView tvleav=(TextView)view.findViewById(R.id.tvleav);
        RelativeLayout rela=(RelativeLayout)view.findViewById(R.id.rela);
        rela.setOnClickListener(this);
        tvyidao.setOnClickListener(this);
        tvearly.setOnClickListener(this);
        tvlaste.setOnClickListener(this);
        tvkuds.setOnClickListener(this);
        tvleav.setOnClickListener(this);
        pop=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        view.getBackground().setAlpha(110);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        pop.update();
    }
    void initdata(int keid,String type){
        if (DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            if (firstIn) {
                firstIn = false;
            }
            HttpParams params=new HttpParams();
            params.put("schedule_id",keid);
            params.put("name","");
            if (trueorfalse.equals("true")) {
                params.put("isSchoolTime", true);
            }else{
                params.put("isSchoolTime",false);
            }
            params.put("type",type);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/get")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    xlistnew.stopRefresh();
                    refnewcall.setRefreshing(false);
                    refnewcall.setEnabled(false);
                    if (!TextUtils.isEmpty(s)) {
                        resultInfo = JSON.parseObject(s, ResultInfoCall.class);
                        if (resultInfo.data.size() != 0) {
                            if (TextUtils.isEmpty(resultInfo.data.get(0).authCode)) {
                                tvyanz.setVisibility(View.GONE);
                                code = "";
                            } else {
                                if (resultInfo.data.get(0).authCode.contains("-")) {
                                    code = "";
                                    tvyanz.setVisibility(View.GONE);
                                } else {
                                    code = "code";
                                    tvyanz.setVisibility(View.VISIBLE);
                                    tvyanz.setText("验证码:" + resultInfo.data.get(0).authCode);
                                }
                            }
                            if (listid.size() != 0) {
                                listid.clear();
                            }
                            for (int i = 0; i < resultInfo.data.size(); i++) {
                                count += resultInfo.data.get(i).rollCallList.size();
                                for (int j = 0; j < resultInfo.data.get(i).rollCallList.size(); j++) {
                                    listid.add(String.valueOf(resultInfo.data.get(i).rollCallList.get(j).id));
                                }
                            }
                            if (trueorfalse.equals("false")) {
                                if (!resultInfo.data.get(0).rollCallList.get(0).type.equals("9")) {
                                    rlzhuangtai.setVisibility(View.VISIBLE);
                                    tvcancelin.setVisibility(View.GONE);
                                    tvcancelout.setVisibility(View.VISIBLE);
                                } else {
                                    rlzhuangtai.setVisibility(View.GONE);
                                }
                            } else {
                                rlzhuangtai.setVisibility(View.VISIBLE);
                                tvcancelin.setVisibility(View.VISIBLE);
                                tvcancelout.setVisibility(View.GONE);
                            }
                        }
                        if (count > 10) {
                            adapter.refreshNewCalllist(resultInfo.data);
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.refreshNewCalllist(resultInfo.data);
                            xlistnew.setAdapter(adapter);
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    xlistnew.stopRefresh();
                    refnewcall.setEnabled(false);
                    refnewcall.setRefreshing(false);
                    DianTool.response(response,NewCallRollActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    void diss(){
        viewqi.setVisibility(View.VISIBLE);
        tvquanbus.setBackgroundResource(R.drawable.circle_corner);
        tvquanbus.setTextColor(getResources().getColor(R.color.qianblue));
    }
    void shows(){
        viewqi.setVisibility(View.GONE);
        tvquanbus.setBackgroundResource(R.drawable.circle_cornerall);
        tvquanbus.setTextColor(getResources().getColor(R.color.qiangzi));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvshengyu:
                pops.dismiss();
                diss();
                break;
            case R.id.tvquanbus:
                pops.showAsDropDown(v);
                shows();
                break;
            case R.id.reczaotui:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=reczaotui){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        reczaotui.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("早退");
                diss();
                pops.dismiss();
                count=0;
                type="5";
                 firstIn=true;
                 initdata(keid,"5");
                break;
            case R.id.recqingjia:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=recqingjia){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        recqingjia.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("请假");
                pops.dismiss();
                diss();
                count=0;
                type="4";
               firstIn=true;
                initdata(keid,"4");
                break;
            case R.id.recyichang:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=recyichang){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        recyichang.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("异常");
                diss();
                pops.dismiss();
                count=0;
                type="8";
                firstIn=true;
                initdata(keid,"8");
                break;
            case R.id.recquanbu:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=recquanbu){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        recquanbu.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("全部");
                diss();
                pops.dismiss();
                count=0;
                type="";
                firstIn=true;
                 initdata(keid,"");
                break;
            case R.id.recchidao:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=recchidao){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        recchidao.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("迟到");
                diss();
                pops.dismiss();
                count=0;
                type="3";
                firstIn=true;
                initdata(keid,"3");
                break;
            case R.id.reckuangke:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=reckuangke){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        reckuangke.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("旷课");
                diss();
                pops.dismiss();
                count=0;
                 type="2";
                firstIn=true;
               initdata(keid,"2");
                break;
            case R.id.recweiyijiao:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=recweiyijiao){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        recweiyijiao.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("未提交");
                diss();
                pops.dismiss();
               count=0;
               type="7";
               firstIn=true;
               initdata(keid, "7");
                break;
            case R.id.recyidao:
                for (int i=0;i<imgs.length;i++){
                    if (imgs[i]!=recyidao){
                        imgs[i].setBackgroundResource(R.mipmap.icon_weiz);
                    }else{
                        recyidao.setBackgroundResource(R.mipmap.icon_z);
                    }
                }
                tvquanbus.setText("已到");
                diss();
                pops.dismiss();
                count=0;
                type="1";
                firstIn=true;
                initdata(keid,"1");
                break;
            case R.id.rela:
                pop.dismiss();
                llbgs.setBackgroundResource(R.color.bg_White);
                break;
            case R.id.tvcancelout:
                DuiHuaDialogs dialogs=new DuiHuaDialogs(this, "确认取消", "本次课程所有学生签到记录将取消", "","确认", new DuiHuaDialogs.SelectDialogButtonListener() {
                    @Override
                    public void checkButton(int id) {
                         switch (id){
                             case R.id.btnok:
                                 type="";
                                 DianTool.showDialog(NewCallRollActivity.this,"");
                                 updatecurrent();
                                 break;
                         }
                    }
                });
                dialogs.show();
                break;
            case R.id.tvcancelin:
                DuiHuaDialogs dialog=new DuiHuaDialogs(this,"","规则修改后，本次课已经生成的考勤状态将被初始化为“未提交”，" +
                        "所有学生需要重新提交签到信息","","确认修改", new DuiHuaDialogs.SelectDialogButtonListener() {
                    @Override
                    public void checkButton(int id) {
                        switch (id){
                            case R.id.btnok:
                                type="";
                                Intent intents=new Intent(NewCallRollActivity.this, CallSetActivity.class);
                                intents.putExtra(Constant.COURSE_NAME,coursename);
                                intents.putExtra(Constant.COURSEID,getIntent().getIntExtra(Constant.COURSEID,0));
                                intents.putExtra(Constant.LATE_TIME,getIntent().getIntExtra(Constant.LATE_TIME,0));
                                intents.putExtra(Constant.CODE,code);
                                intents.putExtra(Constant.RUKOU, "2");
                                startActivity(intents);
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                break;
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.newbianji:
                if (resultInfo!=null) {
                    if (resultInfo.data.size() != 0) {
                        if (trueorfalse.equals("false")) {
                            if (!resultInfo.data.get(0).rollCallList.get(0).type.equals("9")) {
//                            type="";
                                Intent intente = new Intent(this, PiNewCallRollActivity.class);
                                intente.putExtra(Constant.KE_ID, keid);
                                intente.putExtra(Constant.ISSHOOL, trueorfalse);
                                intente.putExtra(Constant.TYPE, type);
                                startActivity(intente);
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }
                        } else {
//                        type="";
                            Intent intente = new Intent(this, PiNewCallRollActivity.class);
                            intente.putExtra(Constant.KE_ID, keid);
                            intente.putExtra(Constant.ISSHOOL, trueorfalse);
                            intente.putExtra(Constant.TYPE, type);
                            startActivity(intente);
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }
                    }
                }
                break;
            case R.id.tvsousuo:
                type="";
                Intent intent=new Intent(this, SouSuoActivity.class);
                intent.putExtra(Constant.KE_ID, keid);
                intent.putExtra(Constant.ISSHOOL,trueorfalse);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.newback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.tvyidao:
                pop.dismiss();
                update("1");
                break;
            case R.id.tvleav:
                pop.dismiss();
                update("4");
                break;
            case R.id.tvearly:
                pop.dismiss();
                update("5");
                break;
            case R.id.tvlaste:
                pop.dismiss();
                update("3");
                break;
            case R.id.tvkuds:
                pop.dismiss();
                update("2");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onCancelCollect(ImageView iv,RelativeLayout lela,View v,int classId,String className,int courseId,long id,int scheduleId,int studentScheduleId,
                                int teacherId,String type,int userId,String userName,ImageView checkbox) {
        if (!resultInfo.data.get(0).rollCallList.get(0).type.equals("9")){
            classIds = classId;
            classNames = className;
            courseIds = courseId;
            ids = id;
            scheduleIds = scheduleId;
            studentScheduleIds = studentScheduleId;
            teacherIds = teacherId;
            types = type;
            userIds = userId;
            userNames = userName;
            llbgs=lela;
            lela.setBackgroundResource(R.color.xuanzhong);
            pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
    }
    void update(String typed){
        firstIn=true;
        JSONObject json=new JSONObject();
        try {
            json.put("classId",classIds);
            json.put("className",classNames);
            json.put("courseId",courseIds);
            json.put("id",ids);
            json.put("scheduleId",scheduleIds);
            json.put("studentScheduleId",studentScheduleIds);
            json.put("teacherId",teacherIds);
            json.put("type",typed);
            json.put("userId",userIds);
            json.put("userName",userNames);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (DianTool.isConnectionNetWork(this)) {
            HttpHeaders headers=new HttpHeaders();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/update")).tag(this)
                    .headers(headers).upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            count=0;
                            initdata(keid,type);
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.showTextToast(NewCallRollActivity.this,"修改失败");
                            DianTool.response(response,NewCallRollActivity.this);
                        }
                    });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    void updatecurrent(){
        firstIn=true;
        if (DianTool.isConnectionNetWork(this)){
            HttpParams params=new HttpParams();
            params.put("scheduleId", keid);
            OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/cancleCurrentRollCall")).tag(this)
                    .headers(headers).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            count=0;
                            initdata(keid, type);
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.showTextToast(NewCallRollActivity.this, "本次考勤取消失败");
                            DianTool.response(response,NewCallRollActivity.this);
                        }
                    });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }

    @Override
    public void refresh(int i,long ids) {
        if (i==0){
            refnewcall.setEnabled(true);
        }else{
            refnewcall.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refnewcall.setRefreshing(true);
        count=0;
        initdata(keid,type);
    }

    @Override
    public void onRefresh() {
        initdata(keid,type);
    }

    @Override
    public void onLoadMore() {
    }
}
