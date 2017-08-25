package com.dingli.diandiaan.rollcall;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
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
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.dingli.diandiaan.firstpage.callset.CallSetActivity;
import com.dingli.diandiaan.rollcall.sousuo.SouSuoActivity;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/25.
 */
public class PiNewCallRollActivity extends BaseActivity implements View.OnClickListener,ViewHolder.onCancelCollectListener,XListView.IXListViewListener{
    XListView xlistnew;
    PopupWindow pop;
    boolean firstIn=true;
    int keid;
    HttpHeaders headers;
    String trueorfalse;
    ImageView ivweibianji,ivbianji;
    TextView tvyanz;
    List<String> list,listid,listrollid,listrollids;
    String code;
    ResultInfoCall resultInfo;
    VerticalSwipeRefreshLayout refpicall;
    int zie;
    String qiantype;
    NewRollCallAdapter adapters;
    int zid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinewcallroll);
        keid=getIntent().getIntExtra(Constant.KE_ID,0);
        initview();
    }
  void initview(){
      list=new ArrayList<>();
      listid=new ArrayList<>();
      listrollid=new ArrayList<>();
      listrollids=new ArrayList<>();
      trueorfalse=getIntent().getStringExtra(Constant.ISSHOOL);
      qiantype=getIntent().getStringExtra(Constant.TYPE);
      headers=new HttpHeaders();
      headers.put("Content-Type", Constant.APPLICATION_FORMURL);
      headers.put("Encoding", "UTF-8");
      headers.put("Accept", Constant.APPLICATION_JSON);
      refpicall=(VerticalSwipeRefreshLayout)findViewById(R.id.refpicall);
      refpicall.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
              getResources().getColor(R.color.holo_green_light),
              getResources().getColor(R.color.holo_orange_light),
              getResources().getColor(R.color.holo_red_light));
      xlistnew=(XListView)findViewById(R.id.pixlistnew);
      xlistnew.setPullRefreshEnable(true);
      xlistnew.setPullLoadEnable(false);
      xlistnew.setXListViewListener(this);
      ivweibianji=(ImageView)findViewById(R.id.piivweibianji);
      ivbianji=(ImageView)findViewById(R.id.piivbianji);
      tvyanz=(TextView)findViewById(R.id.pitvyanz);
      ImageView newback=(ImageView)findViewById(R.id.pinewback);
      TextView pibianjiyidao=(TextView)findViewById(R.id.pibianjiyidao);
      TextView pibianjichidao=(TextView)findViewById(R.id.pibianjichidao);
      TextView pibianjizaotui=(TextView)findViewById(R.id.pibianjizaotui);
      TextView pibianjikuangke=(TextView)findViewById(R.id.pibianjikuangke);
      TextView pibianjiqingjia=(TextView)findViewById(R.id.pibianjiqingjia);
      LinearLayout llbianji=(LinearLayout) findViewById(R.id.llbianji);
      llbianji.setOnClickListener(this);
      ivweibianji.setOnClickListener(this);
      pibianjiyidao.setOnClickListener(this);
      pibianjichidao.setOnClickListener(this);
      pibianjizaotui.setOnClickListener(this);
      pibianjikuangke.setOnClickListener(this);
      pibianjiqingjia.setOnClickListener(this);
      ivbianji.setOnClickListener(this);
      newback.setOnClickListener(this);
      adapters=new NewRollCallAdapter(PiNewCallRollActivity.this,pop,PiNewCallRollActivity.this,"全部",0,0);
      xlistnew.setAdapter(adapters);
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
                    refpicall.setRefreshing(false);
                    refpicall.setEnabled(false);
                    xlistnew.stopRefresh();
                    ivbianji.setVisibility(View.GONE);
                    ivweibianji.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(s)) {
                        resultInfo = JSON.parseObject(s, ResultInfoCall.class);
                        if (resultInfo.data.size() != 0) {
                            if (listid.size() != 0) {
                                listid.clear();
                            }
                            if (listrollid.size() != 0) {
                                listrollid.clear();
                            }
                            if (list.size() != 0) {
                                list.clear();
                            }
                            if (listrollids.size() != 0) {
                                listrollids.clear();
                            }
                            DianApplication.user.listOut.clear();
                            DianApplication.user.listIn.clear();
                            for (int i = 0; i < resultInfo.data.size(); i++) {
                                for (int j = 0; j < resultInfo.data.get(i).rollCallList.size(); j++) {
                                    listid.add(String.valueOf(resultInfo.data.get(i).rollCallList.get(j).userId));
                                    listrollid.add(String.valueOf(resultInfo.data.get(i).rollCallList.get(j).id));
                                    zie += resultInfo.data.get(i).rollCallList.size();
                                }
                            }
                        }
                        if (zid == 5) {
//                        NewRollCallAdapter adapter = new NewRollCallAdapter(PiNewCallRollActivity.this, pop, PiNewCallRollActivity.this, "全部", 0, 0);
                            adapters.refreshNewCalllist(resultInfo.data);
                            xlistnew.setAdapter(adapters);
                            zid = 0;
                        } else {
                            adapters.refreshNewCalllist(resultInfo.data);
                            adapters.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    refpicall.setRefreshing(false);
                    xlistnew.stopRefresh();
                    DianTool.response(response,PiNewCallRollActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llbianji:
                if (ivweibianji.getVisibility()==View.VISIBLE) {
                    ivbianji.setVisibility(View.VISIBLE);
                    ivweibianji.setVisibility(View.GONE);
                    NewRollCallAdapter adapter = new NewRollCallAdapter(this, pop, this, "选中", 0, 0);
                    adapter.refreshNewCalllist(resultInfo.data);
                    adapter.notifyDataSetChanged();
                    xlistnew.setAdapter(adapter);
                    list.addAll(listid);
                    listrollids.addAll(listrollid);
                    zid=5;
                }else{
                    ivbianji.setVisibility(View.GONE);
                    ivweibianji.setVisibility(View.VISIBLE);
                    NewRollCallAdapter adapters=new NewRollCallAdapter(this,pop,this,"全部",0,0);
                    adapters.refreshNewCalllist(resultInfo.data);
                    adapters.notifyDataSetChanged();
                    xlistnew.setAdapter(adapters);
                    list.clear();
                    listrollids.clear();
                }
                break;
            case R.id.pinewback:
                DianApplication.user.listIn.clear();
                DianApplication.user.listOut.clear();
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.pibianjiyidao:
                if (list.size()!=0) {
                        ivbianji.setVisibility(View.GONE);
                        ivweibianji.setVisibility(View.VISIBLE);
                    v.setClickable(false);
                    DianApplication.user.listIn.clear();
                    DianApplication.user.listOut.clear();
                    moreupdate(v,"1");
                }else{
                    DianTool.showTextToast(this,"请至少勾选一名学生");
                }
                break;
            case R.id.pibianjichidao:
                if(list.size()!=0) {
                        ivbianji.setVisibility(View.GONE);
                        ivweibianji.setVisibility(View.VISIBLE);
                    v.setClickable(false);
                    DianApplication.user.listIn.clear();
                    DianApplication.user.listOut.clear();
                    moreupdate(v, "3");
                }else{
                    DianTool.showTextToast(this, "请至少勾选一名学生");
                }
                break;
            case R.id.pibianjizaotui:
                if (list.size()!=0) {
                        ivbianji.setVisibility(View.GONE);
                        ivweibianji.setVisibility(View.VISIBLE);
                    v.setClickable(false);
                    DianApplication.user.listIn.clear();
                    DianApplication.user.listOut.clear();
                    moreupdate(v, "5");
                }else{
                    DianTool.showTextToast(this, "请至少勾选一名学生");
                }
                break;
            case R.id.pibianjikuangke:
                if (list.size()!=0) {
                        ivbianji.setVisibility(View.GONE);
                        ivweibianji.setVisibility(View.VISIBLE);
                    v.setClickable(false);
                    DianApplication.user.listIn.clear();
                    DianApplication.user.listOut.clear();
                    moreupdate(v, "2");
                }else{
                    DianTool.showTextToast(this, "请至少勾选一名学生");
                }
                break;
            case R.id.pibianjiqingjia:
                if (list.size()!=0) {
                        ivbianji.setVisibility(View.GONE);
                        ivweibianji.setVisibility(View.VISIBLE);
                    v.setClickable(false);
                    DianApplication.user.listIn.clear();
                    DianApplication.user.listOut.clear();
                    moreupdate(v, "4");
                }else{
                    DianTool.showTextToast(this, "请至少勾选一名学生");
                }
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
            ColorDrawable colorDrawable=(ColorDrawable) lela.getBackground();
            if(colorDrawable.getColor()==getResources().getColor(R.color.xuanzhong)){
                iv.setBackgroundResource(R.mipmap.icon_weiyuan);
                lela.setBackgroundResource(R.color.bg_White);
                list.remove(String.valueOf(userId));
                listrollids.remove(String.valueOf(id));
                for (int i=0;i<resultInfo.data.size();i++){
                    for (int j=0;j<resultInfo.data.get(i).rollCallList.size();j++){
                        if (resultInfo.data.get(i).rollCallList.get(j).id==id){
                            DianApplication.user.listIn.add(id);
                        }
                    }
                }
            }else {
                lela.setBackgroundResource(R.color.xuanzhong);
                iv.setBackgroundResource(R.mipmap.icon_zongyuan);
                list.add(String.valueOf(userId));
                listrollids.add(String.valueOf(id));
                for (int i=0;i<resultInfo.data.size();i++){
                    for (int j=0;j<resultInfo.data.get(i).rollCallList.size();j++){
                        if (resultInfo.data.get(i).rollCallList.get(j).id==id){
                            DianApplication.user.listOut.add(id);
                        }
                    }
                }
            }
        if (list.size()!=zie){
            ivbianji.setVisibility(View.GONE);
            ivweibianji.setVisibility(View.VISIBLE);
        }else{
            ivbianji.setVisibility(View.VISIBLE);
            ivweibianji.setVisibility(View.GONE);
        }
    }
    void moreupdate(final View v,String types){
        firstIn=true;
        if (DianTool.isConnectionNetWork(this)){
            HttpParams params=new HttpParams();
            params.put("type", types);
//            params.put("scheduleId",keid);
//                params.putUrlParams("studentIds", list);
                params.putUrlParams("rollCallIds", listrollids);
                OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/updateRollCall")).tag(this)
                        .headers(headers).params(params)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                v.setClickable(true);
                                list.clear();
                                resultInfo.data.clear();
                                initdata(keid, qiantype);
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                                v.setClickable(true);
                                DianTool.showTextToast(PiNewCallRollActivity.this, "考勤修改失败");
                                DianTool.response(response,PiNewCallRollActivity.this);
                            }
                        });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }

    @Override
    public void refresh(int i,long ids) {
        if (i==0){
            refpicall.setEnabled(true);
        }else{
            refpicall.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refpicall.setRefreshing(true);
        initdata(keid,qiantype);
    }

    @Override
    public void onRefresh() {
        xlistnew.stopRefresh();
    }

    @Override
    public void onLoadMore() {

    }
}
