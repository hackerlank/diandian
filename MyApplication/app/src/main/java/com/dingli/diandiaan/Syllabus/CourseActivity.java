package com.dingli.diandiaan.Syllabus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.PaiXuPinJiao;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Coursecenter;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.evaluation.PingJiaoAdapter;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.adpter.CourseAdapter;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.evaluation.EvaluationActivity;
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
 * Created by Administrator on 2016/3/7.
 */
public class CourseActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener{

     ImageView courseback;
    TextView tvcourse,classroomdatecourse,classroomweekcourse,classroomzhoucourse,classroomjiecourse,classroomtimecourse,classroomjiaocourse
            ,pingjiacourse,chidaotv;
    String id;
    int ide;
    Course course;
    int keid;
    TextView syllabustv,zongrenshu,daorenshu;
    LinearLayout licouresping,tvchid;
    String json;
    ListView lvcousrse;
    CourseAdapter adapter;
    List<Coursecenter> arraylist;
    HttpHeaders headers;
    boolean firstIn=true;
    XListView courstlists;
    PingJiaoAdapter adapters;
    VerticalSwipeRefreshLayout refcourse;
    View qicheview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
      ide= getIntent().getIntExtra(Constant.KE_ID,0);
         headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
            init();
            adapter=new CourseAdapter(this);
            lvcousrse.setAdapter(adapter);
        refcourse.setRefreshing(true);
        initdata();
        }
    public void init(){
        refcourse=(VerticalSwipeRefreshLayout)findViewById(R.id.refcourse);
        refcourse.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refcourse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        courstlists=(XListView)findViewById(R.id.courstlists);
        adapters=new PingJiaoAdapter(this);
        courstlists.setAdapter(adapters);
        courstlists.setPullRefreshEnable(true);
        courstlists.setPullLoadEnable(false);
        courstlists.setXListViewListener(this);
        courstlists.addHeaderView(getView());
        courseback=(ImageView)findViewById(R.id.courseback);
        tvcourse=(TextView)findViewById(R.id.tvcourse);
        courseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
            }
        });
    }
    View getView(){
        View view= LayoutInflater.from(this).inflate(R.layout.activity_courselist,null);
        qicheview=view.findViewById(R.id.qicheview);
        chidaotv=(TextView)view.findViewById(R.id.chidaotv);
        lvcousrse=(ListView)view.findViewById(R.id.lvcousrse);
        licouresping=(LinearLayout)view.findViewById(R.id.licouresping);
        licouresping.setOnClickListener(this);
        daorenshu=(TextView)view.findViewById(R.id.daorenshu);
        daorenshu.setOnClickListener(this);
        zongrenshu=(TextView)view.findViewById(R.id.zongrenshu);
        zongrenshu.setOnClickListener(this);
        syllabustv=(TextView)view.findViewById(R.id.syllabustvc);
        classroomdatecourse=(TextView)view.findViewById(R.id.classroomdatecourse);
        classroomweekcourse=(TextView)view.findViewById(R.id.classroomweekcourse);
        classroomzhoucourse=(TextView)view.findViewById(R.id.classroomzhoucourse);
        classroomjiecourse=(TextView)view.findViewById(R.id.classroomjiecourse);
        classroomtimecourse=(TextView)view.findViewById(R.id.classroomtimecourse);
        classroomtimecourse=(TextView)view.findViewById(R.id.classroomtimecourse);
        classroomjiaocourse=(TextView)view.findViewById(R.id.classroomjiaocourse);
        pingjiacourse=(TextView)view.findViewById(R.id.pingjiacourse);
        tvchid=(LinearLayout)view.findViewById(R.id.tvchid);
        pingjiacourse.setOnClickListener(this);
        return  view;
    }
    public void initdata(){
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
           if (firstIn){
               firstIn=false;
           }
            HttpParams params = new HttpParams();
            params.put("schedule_id", ide);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/course/get")).tag(this).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    json = s;
                    if (TextUtils.isEmpty(s)) {
                        DianTool.showTextToast(CourseActivity.this, getResources().getString(R.string.meiju));
                        classroomweekcourse.setText("");
                    } else {
                        course = JSON.parseObject(s, Course.class);
                        syllabustv.setText(course.courseName);
                        keid = course.id;
                        tvcourse.setText(course.courseName);
                        classroomdatecourse.setText(course.teach_time);
                        classroomzhoucourse.setText(getResources().getString(R.string.di) + course.weekName + getResources().getString(R.string.zhou));
                        if (course.classBeginTime != null) {
                            if (course.classEndTime != null) {
                                classroomtimecourse.setText(course.classBeginTime + "-" + course.classEndTime);
                            }
                        }
                        if(course.lateTime!=0) {
                            tvchid.setVisibility(View.VISIBLE);
                            chidaotv.setText("上课" + course.lateTime + "分钟之后");
                        }else{
                            tvchid.setVisibility(View.GONE);
                        }
                        classroomjiecourse.setText(course.whichLesson);
                        classroomjiaocourse.setText(course.classRoom);
                        daorenshu.setText(course.assessPeopelNum + "");
                        zongrenshu.setText("/" + course.totalPeopelNum + ")");
                        if (course.classInfor.size() != 0) {
                            qicheview.setVisibility(View.VISIBLE);
                            adapter.refreshOutlist(course.classInfor);
                            adapter.notifyDataSetChanged();
                            arraylist = course.classInfor;
                        }else{
                            qicheview.setVisibility(View.GONE);
                        }
                        if (course.dayOfWeek.equals("3")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "三");
                        } else if (course.dayOfWeek.equals("1")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "一");
                        } else if (course.dayOfWeek.equals("2")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "二");
                        } else if (course.dayOfWeek.equals("4")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "四");
                        } else if (course.dayOfWeek.equals("5")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "五");
                        } else if (course.dayOfWeek.equals("6")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "六");
                        } else if (course.dayOfWeek.equals("7")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "日");
                        }

                    }
                    showlistCourse(1, Constant.REFRESH);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.response(response,CourseActivity.this);
                }
            });
        }else{
           DianTool.showTextToast(this, "请检查网络");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.licouresping:
                Intent intent1=new Intent(this, EvaluationActivity.class);
                if(TextUtils.isEmpty(json)){
                    DianTool.showTextToast(CourseActivity.this,getResources().getString(R.string.meiju));
                }else {
                    intent1.putExtra(Constant.COURSE_NAME, course.courseName);
                    intent1.putExtra("id_ke",String.valueOf(course.id));
                    intent1.putExtra("dimingren", String.valueOf(course.assessPeopelNum));
                    intent1.putExtra("dimingzongren", String.valueOf(course.totalPeopelNum));
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
                break;
            case R.id.pingjiacourse:
                Intent intent2=new Intent(this, EvaluationActivity.class);
                if(TextUtils.isEmpty(json)){
                    DianTool.showTextToast(CourseActivity.this,getResources().getString(R.string.meiju));
                }else {
                    intent2.putExtra(Constant.COURSE_NAME, course.courseName);
                    intent2.putExtra("id_ke",String.valueOf(course.id));
                    intent2.putExtra("dimingren", String.valueOf(course.assessPeopelNum));
                    intent2.putExtra("dimingzongren", String.valueOf(course.totalPeopelNum));
                    startActivity(intent2);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
                break;
            case R.id.daorenshu:
                Intent intent3=new Intent(this, EvaluationActivity.class);
                if(TextUtils.isEmpty(json)){
                    DianTool.showTextToast(CourseActivity.this,getResources().getString(R.string.meiju));
                }else {
                    intent3.putExtra(Constant.COURSE_NAME, course.courseName);
                    intent3.putExtra("id_ke",String.valueOf(course.id));
                    intent3.putExtra("dimingren", String.valueOf(course.assessPeopelNum));
                    intent3.putExtra("dimingzongren", String.valueOf(course.totalPeopelNum));
                    startActivity(intent3);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
                break;
            case R.id.zongrenshu:
                Intent intent4=new Intent(this, EvaluationActivity.class);
                if(TextUtils.isEmpty(json)){
                    DianTool.showTextToast(CourseActivity.this,getResources().getString(R.string.meiju));
                }else {
                    intent4.putExtra(Constant.COURSE_NAME, course.courseName);
                    intent4.putExtra("id_ke",String.valueOf(course.id));
                    intent4.putExtra("dimingren", String.valueOf(course.assessPeopelNum));
                    intent4.putExtra("dimingzongren", String.valueOf(course.totalPeopelNum));
                    startActivity(intent4);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
                break;
        }
    }
   void showlistCourse(int page,int type){
       if (DianTool.isConnectionNetWork(this)) {
           DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE)+"-"+
                   DianApplication.sharedPreferences.getStringValue(Constant.SPLITTWO)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITTHREE)
                   +"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFOUR)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFIVE);
           HttpParams params = new HttpParams();
           params.put("schedule_id", id);
           params.put("offset", page);
           params.put("limit", 500);
           headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
           OkGo.getInstance().addCommonHeaders(headers);
           OkGo.get(HostAdress.getZheRe("/api/phone/v1/assess/get")).tag(this).params(params).execute(new StringCallback() {
               @Override
               public void onSuccess(String s, Call call, Response response) {
                   refcourse.setRefreshing(false);
                   refcourse.setEnabled(false);
                   courstlists.stopRefresh();
                   if (!TextUtils.isEmpty(s)) {
                       Result resultInfo = JSON.parseObject(s, Result.class);
                       if (resultInfo.data.size() == 0) {
                       } else {
                           List<Course> ared = PaiXuPinJiao.getpaixu(resultInfo.data);
                           adapters.refreshPinglist(ared);
                           adapters.notifyDataSetChanged();
                       }
                   }
               }

               @Override
               public void onError(Call call, Response response, Exception e) {
                   courstlists.stopRefresh();
                   refcourse.setRefreshing(false);
                   refcourse.setEnabled(false);
                   DianTool.response(response,CourseActivity.this);
               }
           });
       } else {
           DianTool.showTextToast(this, "请检查网络");
       }
   }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onRefresh() {
        initdata();
    }

    @Override
    public void onLoadMore() {
    }
}

