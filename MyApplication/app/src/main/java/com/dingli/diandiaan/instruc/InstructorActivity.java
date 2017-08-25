package com.dingli.diandiaan.instruc;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.CourseMing;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.DuiHuaDialog;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.dingli.diandiaan.common.ResultOne;
import com.dingli.diandiaan.instruc.adapter.InstructorAdapter;
import com.dingli.diandiaan.instruc.adapter.KaiSuiAdapter;
import com.dingli.diandiaan.rule.ListItemRulView;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/4/13.
 */

public class InstructorActivity extends BaseActivity implements View.OnClickListener,CommInterface{

    private ImageView instructorback;
    private ListView listinstructor;
    Button btheadkai;
    TextView tvheadtime,tvliushu,tvheadzong,btheader;
    RelativeLayout reheaditem;
    VerticalSwipeRefreshLayout refinstruc;
    InstructorAdapter adapter;
    List<CourseMing> listming;
    HttpHeaders headers;
    int rollCallEverId;
    Button btninstrcancel;
    Button btninsdone;
    KaiSuiAdapter adapters;
    PopupWindow pop;
    int zide;
    ListView listdialog;
    float oldfirstVisibleItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        initView();
        initpopw();
    }
    @Override
    protected void onResume() {
        super.onResume();
        refinstruc.setRefreshing(true);
        initdata();
    }
    private void initView() {
        listming=new ArrayList<>();
        instructorback = (ImageView) findViewById(R.id.instructorback);
        listinstructor = (ListView) findViewById(R.id.listinstructor);
        refinstruc=(VerticalSwipeRefreshLayout) findViewById(R.id.refinstruc);
        instructorback.setOnClickListener(this);
        DianTool.refresh(refinstruc,this);
        listinstructor.addHeaderView(getview());
        oldfirstVisibleItem=getHeaderY();
        refinstruc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
    }
    private float getHeaderY(){
        View headerView= listinstructor.getChildAt(0);
        if (headerView!=null){
            return headerView.getY();
        }
        return -1f;
    }
    void initpopw(){
        View view=LayoutInflater.from(this).inflate(R.layout.dialog_kaiqisui,null);
        btninstrcancel=(Button)view.findViewById(R.id.btninstrcancel);
        btninsdone=(Button)view.findViewById(R.id.btninsdone);
        listdialog=(ListView)view.findViewById(R.id.listdialog);
        adapters=new KaiSuiAdapter(this);
        btninstrcancel.setOnClickListener(this);
        btninsdone.setOnClickListener(this);
        pop=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        view.getBackground().setAlpha(90);
        pop.update();
    }
     protected View getview(){
         View view= LayoutInflater.from(this).inflate(R.layout.header_item,null);
         btheadkai=(Button) view.findViewById(R.id.btheadkai);
         tvheadtime=(TextView) view.findViewById(R.id.tvheadtime);
         tvliushu=(TextView) view.findViewById(R.id.tvliushu);
         tvheadzong=(TextView) view.findViewById(R.id.tvheadzong);
         btheader=(TextView) view.findViewById(R.id.btheader);
         btheader.setOnClickListener(this);
         reheaditem=(RelativeLayout) view.findViewById(R.id.reheaditem);
         btheadkai.setOnClickListener(this);
         return view;
     }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btninsdone:
                if (DianTool.isConnectionNetWork(InstructorActivity.this)) {
                    kaiqisui();
                }else{
                    DianTool.showTextToast(InstructorActivity.this,"请检查网络");
                }
                break;
            case R.id.btninstrcancel:
                pop.dismiss();
                break;
            case R.id.btheadkai:
                if (btheadkai.getText().toString().equals("开启新点名")){
                    btheadkai.setClickable(false);
                    if ((DianTool.isConnectionNetWork(this))) {
                        OkGo.get(HostAdress.getRequest("/api/web/v1/findAllClassesByHeadTeacherId")).tag(this)
                                .headers(headers).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                btheadkai.setClickable(true);
                                if (!TextUtils.isEmpty(s)) {
                                    List<ResultOne> resultOnes = JSON.parseArray(s, ResultOne.class);
                                    if (zide == 0) {
                                        if (DianApplication.user.listid.size()!=0){
                                            DianApplication.user.listid.clear();
                                        }
                                        for (int i = 0; i < resultOnes.size(); i++) {
                                            DianApplication.user.listid.add(String.valueOf(resultOnes.get(i).id));
                                        }
                                    }
                                    if (DianApplication.user.listid.size() == 0) {
                                        btninsdone.setClickable(true);
                                        for (int i = 0; i < resultOnes.size(); i++) {
                                            DianApplication.user.listid.add(String.valueOf(resultOnes.get(i).id));
                                        }
                                    }
                                    adapters.refreshKaisui(resultOnes);
                                    listdialog.setAdapter(adapters);
                                    updow();
                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                btheadkai.setClickable(true);
                                DianTool.showTextToast(InstructorActivity.this, "没有班级");
                            }
                        });
                    }else{
                        DianTool.showTextToast(this,"请检查网络");
                    }
                }else if (btheadkai.getText().toString().equals("关闭点名")){
                    DuiHuaDialog dialog=new DuiHuaDialog(this, "", "确认要关闭点名吗？", "","确认", new DuiHuaDialog.SelectDialogButtonListener() {
                        @Override
                        public void checkButton(int id) {
                            switch (id){
                                case R.id.btnok:
                                    if (DianTool.isConnectionNetWork(InstructorActivity.this)) {
                                        guanbisui();
                                    }else{
                                        DianTool.showTextToast(InstructorActivity.this,"请检查网络");
                                    }
                                    break;
                            }
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.btheader:
                Intent intent=new Intent(this,InstructorDetailActivity.class);
                intent.putExtra(Constant.TYPE,rollCallEverId);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.instructorback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    void updow(){
            pop.showAtLocation(btheadkai, Gravity.BOTTOM, 0, 0);
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            headers=new HttpHeaders();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/queryEver")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    tvheadtime.setText("");
                    tvliushu.setText("");
                    tvheadzong.setText("");
                    refinstruc.setRefreshing(false);
                    btheadkai.setClickable(true);
                    if (!TextUtils.isEmpty(s)) {
                        ResultInfoCall resultInfoCall = JSON.parseObject(s, ResultInfoCall.class);
                        adapter = new InstructorAdapter(InstructorActivity.this);
                        if (resultInfoCall.data.size() != 0) {
                            if (resultInfoCall.data.get(0).status) {
                                reheaditem.setVisibility(View.VISIBLE);
                                btheadkai.setText("关闭点名");
                                tvheadtime.setText(resultInfoCall.data.get(0).openTime);
                                tvliushu.setText(resultInfoCall.data.get(0).commitCount + "/");
                                tvheadzong.setText(resultInfoCall.data.get(0).totalCount + "");
                                rollCallEverId = resultInfoCall.data.get(0).rollCallEverId;
                                listming.clear();
                                for (int i = 1; i < resultInfoCall.data.size(); i++) {
                                    listming.add(resultInfoCall.data.get(i));
                                    adapter.refreshInstruc(listming);
                                }
                            } else {
                                btheadkai.setText("开启新点名");
                                reheaditem.setVisibility(View.GONE);
                                adapter.refreshInstruc(resultInfoCall.data);
                            }
                        } else {
                            btheadkai.setText("开启新点名");
                            btheadkai.setVisibility(View.VISIBLE);

                        }
                        listinstructor.setAdapter(adapter);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refinstruc.setRefreshing(false);
                    refinstruc.setEnabled(false);
                    btheadkai.setClickable(true);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
   void kaiqisui(){
       if (DianApplication.user.listid.size()!=0) {
           pop.dismiss();
           if (DianTool.isConnectionNetWork(this)) {
               btheadkai.setClickable(false);
               HttpParams params = new HttpParams();
               params.putUrlParams("classIds", DianApplication.user.listid);
               OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/openEverV2")).tag(this).params(params)
                       .headers(headers).execute(new StringCallback() {
                   @Override
                   public void onSuccess(String s, Call call, Response response) {
                       DianTool.showTextToast(InstructorActivity.this, "成功开启");
                       initdata();
                   }

                   @Override
                   public void onError(Call call, Response response, Exception e) {
                       btheadkai.setClickable(true);
                   }
               });
           }else{
               DianTool.showTextToast(this,"请检查网络");
           }
       }else{
           DianTool.showTextToast(this,"请至少选择一个班级");
       }
   }
    void guanbisui(){
        if (DianTool.isConnectionNetWork(this)) {
            btheadkai.setClickable(false);
            HttpParams params = new HttpParams();
            params.put("rollCallEverId", rollCallEverId);
            OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/closeEver")).tag(this).params(params)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.showTextToast(InstructorActivity.this, "成功关闭");
                    initdata();
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    btheadkai.setClickable(true);
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
        DianApplication.user.listins.clear();
        DianApplication.user.listinstru.clear();
    }
    @Override
    public void KaiOrGuan(ListItemRulView ruleview, int id, String str, String name, int lateTime) {
    }
    @Override
    public void select(int id,ImageView iv) {
        zide=1;
       if (iv.getVisibility()==View.VISIBLE){
           if (DianApplication.user.listid.size()>1) {
               DianApplication.user.listid.remove(String.valueOf(id));
           }else{
               DianApplication.user.listid.clear();
           }
           if (DianApplication.user.listid.size()==0){
               btninsdone.setClickable(false);
               DianTool.showTextToast(this,"请至少选择一个班级");
           }else{
               btninsdone.setClickable(true);
           }
       }else{
           DianApplication.user.listid.add(String.valueOf(id));
           if (DianApplication.user.listid.size()!=0){
               btninsdone.setClickable(true);
           }
       }
        adapters.notifyDataSetChanged();
    }
}
