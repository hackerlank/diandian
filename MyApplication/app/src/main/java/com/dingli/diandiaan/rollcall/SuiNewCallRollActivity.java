package com.dingli.diandiaan.rollcall;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.dingli.diandiaan.common.SuiRollDialogs;
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
public class SuiNewCallRollActivity extends BaseActivity implements View.OnClickListener,ViewHolder.onCancelCollectListener,XListView.IXListViewListener{
    XListView xlistnew;
    PopupWindow pop;
    RelativeLayout rlzhuangtai;
    boolean firstIn=true;
    int keid;
    NewRollCallAdapter adapter;
    int classIds;String classNames;int courseIds;long ids;int scheduleIds;int studentScheduleIds;
    int teacherIds;String types;int userIds;String userNames;
    HttpHeaders headers;
    LinearLayout llbianji,llbianjizt;
    TextView newbianji,tvsousuo,tvyanz,tvcancelout;
    int count;
    String coursename;
    String code;
    String type;
    ResultInfoCall resultInfo;
    RelativeLayout llbgs;
    VerticalSwipeRefreshLayout refnewcall;
    TextView yidaoren,chidaoren,zaotuiren,kuangkeren,qingjiaren;
    LinearLayout llcode;
    int zizeng;
    LinearLayout lifugao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suinewcallroll);
        keid=getIntent().getIntExtra(Constant.KE_ID,0);
        initview();
        initpop();
        Point p=new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth=p.x;
        int screenHeight=p.y;
        adapter=new NewRollCallAdapter(this,pop,this,"",screenHeight,screenWidth);
        xlistnew.setAdapter(adapter);
        xlistnew.setXListViewListener(this);
    }
  void initview(){
      coursename=getIntent().getStringExtra(Constant.COURSE_NAME);
      headers=new HttpHeaders();
      headers.put("Content-Type", Constant.APPLICATION_FORMURL);
      headers.put("Encoding", "UTF-8");
      headers.put("Accept", Constant.APPLICATION_JSON);
      refnewcall=(VerticalSwipeRefreshLayout)findViewById(R.id.refnewcall);
      refnewcall.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
              initdata(keid, type);
          }
      });
      refnewcall.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
              getResources().getColor(R.color.holo_green_light),
              getResources().getColor(R.color.holo_orange_light),
              getResources().getColor(R.color.holo_red_light));

      TextView tvcouname=(TextView)findViewById(R.id.tvcouname);
      tvcouname.setText(coursename+":");
      yidaoren=(TextView)findViewById(R.id.yidaoren);
      kuangkeren=(TextView)findViewById(R.id.kuangkeren);
      chidaoren=(TextView)findViewById(R.id.chidaoren);
      zaotuiren=(TextView)findViewById(R.id.zaotuiren);
      qingjiaren=(TextView)findViewById(R.id.qingjianren);
      xlistnew=(XListView)findViewById(R.id.xlistnew);
      xlistnew.setPullLoadEnable(false);
      xlistnew.setPullRefreshEnable(true);
      llbianji=(LinearLayout)findViewById(R.id.llbianji);
      tvcancelout=(TextView)findViewById(R.id.tvcancelout);
      tvyanz=(TextView)findViewById(R.id.tvyanz);
      llcode=(LinearLayout) findViewById(R.id.llcode);
      newbianji=(TextView)findViewById(R.id.newbianji);
       tvsousuo=(TextView)findViewById(R.id.tvsousuo);
      ImageView newback=(ImageView)findViewById(R.id.newback);
      llbianjizt=(LinearLayout)findViewById(R.id.llbianjizt);
      rlzhuangtai=(RelativeLayout)findViewById(R.id.rlzhuangtai);
      lifugao=(LinearLayout)findViewById(R.id.lifugao);
      lifugao.setEnabled(false);
      lifugao.getBackground().setAlpha(100);
      tvcancelout.setOnClickListener(this);
      newbianji.setOnClickListener(this);
      tvsousuo.setOnClickListener(this);
      newback.setOnClickListener(this);
  }
    void initpop(){
        View view= LayoutInflater.from(this).inflate(R.layout.popupwindow_change,null);
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
        String hostadress="";
        if (DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            if (firstIn) {
                firstIn = false;
            }
            HttpParams params=new HttpParams();
            params.put("schedule_id",keid);
            params.put("name","");
            params.put("isSchoolTime", true);
            params.put("type",type);
            boolean boo=getIntent().getBooleanExtra(Constant.ROLLCALL,true);
            if (zizeng==0) {
                if (boo == false) {
                    hostadress = HostAdress.getRequest("/api/phone/v1/teacher/rollcall/getRollCallSchoolTime");
                } else {
                    hostadress = HostAdress.getRequest("/api/phone/v1/teacher/rollcall/get");
                }
                zizeng=1;
            }else{
                hostadress = HostAdress.getRequest("/api/phone/v1/teacher/rollcall/get");
            }
            OkGo.get(hostadress).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    xlistnew.stopRefresh();
                    refnewcall.setRefreshing(false);
                    refnewcall.setEnabled(false);
                    if (!TextUtils.isEmpty(s)) {
                        resultInfo = JSON.parseObject(s, ResultInfoCall.class);
                        if (resultInfo.data.size() != 0) {
                            if (resultInfo.data.get(0).classroomRollcall == true) {
                                tvcancelout.setText("关 闭");
                                lifugao.setVisibility(View.GONE);
                                tvsousuo.setEnabled(true);
                                newbianji.setEnabled(true);
                                xlistnew.setPullRefreshEnable(true);
                            } else {
                                tvcancelout.setText("开 启");
                                lifugao.setVisibility(View.VISIBLE);
                                tvsousuo.setEnabled(false);
                                newbianji.setEnabled(false);
                                xlistnew.setPullRefreshEnable(false);
                            }
                            if (TextUtils.isEmpty(resultInfo.data.get(0).authCode)) {
                                llcode.setVisibility(View.GONE);
                                code = "";
                            } else {
                                if (resultInfo.data.get(0).authCode.contains("-")) {
                                    code = "";
                                    llcode.setVisibility(View.GONE);
                                } else {
                                    code = "code";
                                    llcode.setVisibility(View.VISIBLE);
                                    tvyanz.setText(String.valueOf(resultInfo.data.get(0).authCode));
                                }
                            }
                            int yidao = 0;
                            int chidao = 0;
                            int qingjia = 0;
                            int kuangke = 0;
                            int zaotui = 0;
                            for (int i = 0; i < resultInfo.data.size(); i++) {
                                for (int j = 0; j < resultInfo.data.get(i).rollCallList.size(); j++) {
                                    if (resultInfo.data.get(i).rollCallList.get(j).type.equals("1")) {
                                        yidao++;
                                    }
                                    if (resultInfo.data.get(i).rollCallList.get(j).type.equals("2")) {
                                        kuangke++;
                                    }
                                    if (resultInfo.data.get(i).rollCallList.get(j).type.equals("3")) {
                                        chidao++;
                                    }
                                    if (resultInfo.data.get(i).rollCallList.get(j).type.equals("4")) {
                                        qingjia++;
                                    }
                                    if (resultInfo.data.get(i).rollCallList.get(j).type.equals("5")) {
                                        zaotui++;
                                    }
                                }
                            }
                            yidaoren.setText(String.valueOf(yidao));
                            kuangkeren.setText(String.valueOf(kuangke));
                            chidaoren.setText(String.valueOf(chidao));
                            qingjiaren.setText(String.valueOf(qingjia));
                            zaotuiren.setText(String.valueOf(zaotui));
                        }
                        adapter.refreshNewCalllist(resultInfo.data);
                        adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    xlistnew.stopRefresh();
                    refnewcall.setRefreshing(false);
                    DianTool.response(response,SuiNewCallRollActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rela:
                pop.dismiss();
                llbgs.setBackgroundResource(R.color.bg_White);
                break;
            case R.id.tvcancelout:
                  if (tvcancelout.getText().toString().equals("开 启")){
                      SuiRollDialogs dialogs=new SuiRollDialogs(this, "确认开启！", "开启后,所有学生的考勤将初始化为”未提交“,需要提醒学生" +
                              "再次提交签到信息", "","确认", new SuiRollDialogs.SelectDialogButtonListener() {
                          @Override
                          public void checkButton(int id) {
                              switch (id){
                                  case R.id.btnok:
                                      openapi();
                                      break;
                              }
                          }
                      });
                      dialogs.show();
                  }else{
                      SuiRollDialogs dialogs=new SuiRollDialogs(this, "确认关闭！", "关闭后,所有未提交的学生状态变成”旷课“," +
                              "请及时通知未提交的学生进行签到", "","确认", new SuiRollDialogs.SelectDialogButtonListener() {
                          @Override
                          public void checkButton(int id) {
                              switch (id){
                                  case R.id.btnok:
                                      downapi();
                                      break;
                              }
                          }
                      });
                      dialogs.show();
                  }
                break;
            case R.id.newbianji:
                if (resultInfo.data.size()!=0) {
                        if (!resultInfo.data.get(0).rollCallList.get(0).type.equals("9")) {
                            type="";
                            Intent intente=new Intent(this,PiNewCallRollActivity.class);
                            intente.putExtra(Constant.KE_ID,keid);
                            intente.putExtra(Constant.ISSHOOL,"true");
                            startActivity(intente);
                            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                        }
                }
                break;
            case R.id.tvsousuo:
                type="";
                Intent intent=new Intent(this, SouSuoActivity.class);
                intent.putExtra(Constant.KE_ID, keid);
                intent.putExtra(Constant.ISSHOOL,"true");
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
  void openapi(){
      if (DianTool.isConnectionNetWork(this)) {
          HttpParams params = new HttpParams();
          params.put("scheduleId",keid);
          OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/openClassrommRollcall")).tag(this)
                  .headers(headers).params(params).execute(new StringCallback() {
              @Override
              public void onSuccess(String s, Call call, Response response) {
                  initdata(keid,type);
                  DianTool.showTextToast(SuiNewCallRollActivity.this,"开启随堂点名");
              }
          });
      }else{
          DianTool.showTextToast(this,"请检查网络");
      }
  }
    void downapi(){
        if (DianTool.isConnectionNetWork(this)) {
            HttpParams params = new HttpParams();
            params.put("scheduleId",keid);
            OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/closeClassrommRollcall")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    initdata(keid,type);
                    DianTool.showTextToast(SuiNewCallRollActivity.this,"关闭随堂点名");
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    @Override
    public void onCancelCollect(ImageView iv,RelativeLayout lela,View v,int classId,String className,int courseId,long id,int scheduleId,int studentScheduleId,
                                int teacherId,String type,int userId,String userName,ImageView checkbox) {
        if (!resultInfo.data.get(0).rollCallList.get(0).type.equals("9")){
            if (lifugao.getVisibility()==View.GONE) {
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
                llbgs = lela;
                lela.setBackgroundResource(R.color.xuanzhong);
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        }
    }
    void update(final String type){
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
            json.put("type",type);
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
                            initdata(keid,"");
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.showTextToast(SuiNewCallRollActivity.this,"修改失败");
                            DianTool.response(response,SuiNewCallRollActivity.this);
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
                            DianTool.showTextToast(SuiNewCallRollActivity.this, "本次考勤取消失败");
                            DianTool.response(response,SuiNewCallRollActivity.this);
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
        count=0;
        initdata(keid,type);
    }
    @Override
    public void onLoadMore() {}
}
