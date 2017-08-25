package com.dingli.diandiaan.rule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.DuiHuaDialog;
import com.dingli.diandiaan.common.DuiHuaDialogs;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.firstpage.callset.CallSetActivity;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/24.
 */
public class RuleActivity extends BaseActivity implements View.OnClickListener,CommInterface{
    private ListView xlvrule;
    HttpHeaders headers;
    RuleAdapter adapter;
    boolean firstIn=true;
    VerticalSwipeRefreshLayout refrule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
        adapter=new RuleAdapter(this,this);
        xlvrule.setAdapter(adapter);
    }
    private void initView() {
       ImageView ruleback = (ImageView) findViewById(R.id.ruleback);
        xlvrule = (ListView) findViewById(R.id.xlvrule);
        refrule=(VerticalSwipeRefreshLayout)findViewById(R.id.refrule);
        refrule.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        refrule.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refrule.setViewGroup(xlvrule);
        xlvrule.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem==0){
                    refrule.setEnabled(true);
                }else{
                    refrule.setEnabled(false);
                }
            }
        });
        ruleback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ruleback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    void initdata(){
          if (DianTool.isConnectionNetWork(this)){
              if (firstIn){
                  firstIn=false;
              }
              DianTool.huoqutoken();
              headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
              OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getCourse")).tag(this)
                      .headers(headers).execute(new StringCallback() {
                  @Override
                  public void onSuccess(String s, Call call, Response response) {
                      refrule.setRefreshing(false);
                      if (!s.contains("success")) {
                      if (!TextUtils.isEmpty(s)) {
                          List<Course> alist = JSON.parseArray(s, Course.class);
                          adapter.refreshRule(alist);
                          adapter.notifyDataSetChanged();
                      }
                      }
                  }

                  @Override
                  public void onError(Call call, Response response, Exception e) {
                      refrule.setRefreshing(false);
                      DianTool.showTextToast(RuleActivity.this,"数据加载失败，请刷新重试");
                      DianTool.response(response,RuleActivity.this);
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
    }
    @Override
    public void KaiOrGuan(final ListItemRulView listItemRulView,final int ids,String str,final String name,final int lateTime) {
        if (str.equals("true")){
            Intent intent = new Intent(RuleActivity.this, CallSetActivity.class);
            intent.putExtra(Constant.COURSE_NAME,name);
            intent.putExtra(Constant.COURSEID,ids);
            intent.putExtra(Constant.RUKOU,"2");
            intent.putExtra(Constant.LATE_TIME,lateTime);
            intent.putExtra(Constant.CODE,"e");
            listItemRulView.tbrule.setVisibility(View.VISIBLE);
            listItemRulView.tbrules.setChecked(false);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }else {
            DuiHuaDialogs dialog=new DuiHuaDialogs(this, "", "关闭后该课程后续将不会自动开启点名哦", "","确认", new DuiHuaDialogs.SelectDialogButtonListener() {
                @Override
                public void checkButton(int id) {
                    switch (id){
                        case R.id.btnok:
                            listItemRulView.tbrule.setChecked(true);
                            listItemRulView.tbrules.setVisibility(View.VISIBLE);
                            listItemRulView.tbrule.setVisibility(View.GONE);
                            guanbi(ids);
                            break;
                        case R.id.btncal:
                            listItemRulView.tbrule.setChecked(true);
                            listItemRulView.tbrules.setVisibility(View.VISIBLE);
                            listItemRulView.tbrule.setVisibility(View.GONE);
                            initdata();
                            break;
                    }
                }
            });
            dialog.show();
        }
    }

    @Override
    public void select(int id, ImageView iv) {
    }
    void guanbi(int ids){
       if (DianTool.isConnectionNetWork(this)) {
           HttpParams params = new HttpParams();
           params.put("courseId", ids);
           OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/closeRollcall")).tag(this)
                   .params(params).execute(new StringCallback() {
               @Override
               public void onSuccess(String s, Call call, Response response) {
                   DianTool.showTextToast(RuleActivity.this, "关闭成功，后续课程如需点名请手动开启");
                   initdata();
               }
               @Override
               public void onError(Call call, Response response, Exception e) {
                   DianTool.response(response,RuleActivity.this);
               }
           });
       } else {
           DianTool.showTextToast(this, "请检查网络");
       }
   }
    @Override
    protected void onResume() {
        super.onResume();
        refrule.setRefreshing(true);
        initdata();
    }
}
