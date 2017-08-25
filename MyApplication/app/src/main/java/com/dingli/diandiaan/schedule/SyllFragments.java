package com.dingli.diandiaan.schedule;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.MainActivity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.Syllabus.CourseActivity;
import com.dingli.diandiaan.Syllabus.HorizontalAdapter;
import com.dingli.diandiaan.bean.CourseData;
import com.dingli.diandiaan.bean.TableData;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.view.HorizontalListView;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/4/7.
 */
public class SyllFragments extends BaseActivity implements NewSchedule.OnCourseClickListener,View.OnClickListener{
    private NewSchedule schedule;
    private LinearLayout tabContainer;
    //    private ScheduleContainerScrollView scrollView_navigateBar;
//    private ScheduleContainerScrollView scrollView_scheduleScroll;
    TextView tvbenzhou,tvbenzhous,tvbenzhoud;
    ImageView ivdowa;
    LinearLayout llcurrent;
    HorizontalAdapter adapter;
    String[] zhouyi={"周一","周二","周三","周四","周五","周六","周日"};
    ResultInfo resultInfotoken;
    ResultInfo resultInfoweek;
    ImageView schedback,ddschedul;
    HttpHeaders headers;
    private VerticalSwipeRefreshLayout swipeLayout;
    LinearLayout llform;
    List<String> listdate;
    String mWay;
    TextView tvmonth;
    ScheduleContainerScrollView scroview;
    LinearLayout lletv;
    String weekName;
    List<Integer> lisnid;
    List<String> listed;
    List<TextView> listtv;
    String name;
    int index;
    int juli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview();
        swipeLayout.setRefreshing(true);
        initdata();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvbenzhou:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroview.scrollTo(index*juli,0);
                    }
                },50);
                if (llform.getVisibility()==View.VISIBLE){
                    ivdowa.setBackgroundResource(R.mipmap.sanjiao);
                    llform.setVisibility(View.GONE);
                    tvbenzhou.setVisibility(View.VISIBLE);
                    llcurrent.setVisibility(View.GONE);
                    tvbenzhou.setText("第" + resultInfoweek.name+ "周");
                }else{
                    llform.setVisibility(View.VISIBLE);
                    ivdowa.setBackgroundResource(R.mipmap.icon_uparrows);
                }
                break;
            case R.id.ivdowa:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroview.scrollTo(index*juli,0);
                    }
                },50);
                if (llform.getVisibility()==View.VISIBLE){
                    if (llform.getVisibility()==View.VISIBLE) {
                        listdate = DianTool.weekday();
                        forearcher(Integer.parseInt(DianTool.getLastTimeInterval(1).get(0).split("-")[0]) + "", mWay, tvmonth);
                        adapter.setSelectedWeek(resultInfoweek.name);
                        initweek(resultInfoweek.id);
                    }
                    ivdowa.setBackgroundResource(R.mipmap.sanjiao);
                    llform.setVisibility(View.GONE);
                    tvbenzhou.setVisibility(View.VISIBLE);
                    llcurrent.setVisibility(View.GONE);
                    tvbenzhou.setText("第" + resultInfoweek.name + "周");
                }else{
                    llform.setVisibility(View.VISIBLE);
                    ivdowa.setBackgroundResource(R.mipmap.icon_uparrows);
                }
                break;
            case R.id.llcurrent:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroview.scrollTo(index*juli,0);
                    }
                },50);
                if (llform.getVisibility()==View.VISIBLE){
                    ivdowa.setBackgroundResource(R.mipmap.sanjiao);
                    llform.setVisibility(View.GONE);
                    tvbenzhou.setVisibility(View.VISIBLE);
                    llcurrent.setVisibility(View.GONE);
                    tvbenzhou.setText("第" + resultInfoweek.name+ "周");
                    listdate=DianTool.weekday();
                    adapter.setSelectedWeek(resultInfoweek.name);
                    forearcher(Integer.parseInt(DianTool.getLastTimeInterval(1).get(0).split("-")[0])+"",mWay,tvmonth);
                    initweek(resultInfoweek.id);
                }else{
                    llform.setVisibility(View.VISIBLE);
                    ivdowa.setBackgroundResource(R.mipmap.icon_uparrows);
                }
                break;
            case R.id.tvqinjia:
                Intent intent=new Intent(this, SyFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.schedback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        String str=DianApplication.sharedPreferences.getStringValue("defalt");
        if (!TextUtils.isEmpty(str)){
            switch (str){
                case "1":
                    Bitmap bp1=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morenone);
                    schedule.setBackground(new BitmapDrawable(bp1));
                    break;
                case "2":
                    Bitmap bp2=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morentwo);
                    schedule.setBackground(new BitmapDrawable(bp2));
                    break;
                case "3":
                    Bitmap bp3=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morenthree);
                    schedule.setBackground(new BitmapDrawable(bp3));
                    break;
                case "4":
                    Bitmap bp4=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morenfour);
                    schedule.setBackground(new BitmapDrawable(bp4));
                    break;
                case "5":
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(DianApplication.sharedPreferences.getStringValue("fileone"),options);
                    schedule.setBackground(new BitmapDrawable(bm));
                    break;
                case "6":
                    Bitmap bp6=BitmapFactory.decodeResource(getResources(),R.mipmap.kebg);
                    schedule.setBackground(new BitmapDrawable(bp6));
                    break;
                default:
                    break;
            }
        }else{
            schedule.setBackgroundResource(R.mipmap.kebg);
        }
    }

    void initview(){
        lisnid=new ArrayList<>();
        listed=new ArrayList<>();
        listtv=new ArrayList<>();
        lletv=(LinearLayout) findViewById(R.id.lletv);
        scroview=(ScheduleContainerScrollView)findViewById(R.id.scroview);
        swipeLayout = (VerticalSwipeRefreshLayout)findViewById(R.id.swipe_containersch);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        ImageView tvqinjia=(ImageView)findViewById(R.id.tvqinjia);
        tvqinjia.setOnClickListener(this);
        ddschedul=(ImageView)findViewById(R.id.ddschedul);
        ddschedul.setOnClickListener(this);
        schedback=(ImageView)findViewById(R.id.schedback);
        schedback.setOnClickListener(this);
        tvbenzhou=(TextView)findViewById(R.id.tvbenzhou);
        tvbenzhous=(TextView)findViewById(R.id.tvbenzhous);
        tvbenzhoud=(TextView)findViewById(R.id.tvbenzhoud);
        llcurrent=(LinearLayout) findViewById(R.id.llcurrent);
        ivdowa=(ImageView) findViewById(R.id.ivdowa);
        ivdowa.setBackgroundResource(R.mipmap.sanjiao);
        ivdowa.setOnClickListener(this);
        tvbenzhou.setOnClickListener(this);
        llcurrent.setOnClickListener(this);
        schedule = (NewSchedule)findViewById(R.id.schedulef);
        schedule.setOnCourseClickListener(this);
        tabContainer = (LinearLayout)findViewById(R.id.tabContainerf);
//        scrollView_navigateBar = (ScheduleContainerScrollView)findViewById(R.id.scrollView_navigateBarf);
//        scrollView_scheduleScroll = (ScheduleContainerScrollView)findViewById(R.id.scrollView_scheduleScrollf);
        tvmonth=(TextView) findViewById(R.id.tvmonth);
        llform=(LinearLayout) findViewById(R.id.llform);
        adapter=new HorizontalAdapter(this);
        final Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        listdate=DianTool.weekday();
        forearcher(Integer.parseInt(DianTool.getLastTimeInterval(1).get(0).split("-")[0])+"",mWay,tvmonth);
    }
    private void forearcher(String mMonth,String mWay,TextView tvmonth){
        if (!TextUtils.isEmpty(mMonth)){
            tvmonth.setText(mMonth+"月");
        }
        tabContainer.removeAllViews();
        for (int i = 0; i < zhouyi.length; i++) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(schedule.getColumnWidth()+3, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setText(listdate.get(i)+"\n"+zhouyi[i]);
            textView.setTextColor(getResources().getColor(R.color.sycolor));
            textView.setBackgroundResource(R.drawable.circle_weekbg);
            if("1".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(6)+"\n"+"周日"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==6) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else if("4".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(2)+"\n"+"周三"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==2) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else  if("3".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(1)+"\n"+"周二"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==1) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else if("2".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(0)+"\n"+"周一"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==0) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else if("5".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(3)+"\n"+"周四"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==3) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else if("6".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(4)+"\n"+"周五"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==4) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else  if("7".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(5)+"\n"+"周六"))
                    textView.setTextColor(getResources().getColor(R.color.sys));
                if(i==5) {
                    textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }
            tabContainer.addView(textView);
        }
    }
    private void initSchedule(String jsonStr){
        List<TableData> list = JSON.parseArray(jsonStr, TableData.class);
        schedule.fillSchedule(list);
    }
    void initdata(){
        if(DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/week/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (TextUtils.isEmpty(s)) {
                        swipeLayout.setRefreshing(false);
                        DianTool.showTextToast(SyllFragments.this, "没有数据");
                    } else {
                        resultInfoweek = JSON.parseObject(s, ResultInfo.class);
                        tvbenzhou.setText("第"+resultInfoweek.name+"周");
                        weekName=resultInfoweek.name;
                        adapter.setCurrentWeek(resultInfoweek.name);
                        initweek(resultInfoweek.id);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    swipeLayout.setRefreshing(false);
                    DianTool.response(response,SyllFragments.this);
                }
            });
        }else{
            if(TextUtils.isEmpty(DianApplication.user.json)) {
                DianTool.showTextToast(SyllFragments.this, "没有数据");
            }else {
                DianTool.showProgressDialog(this,0);
                initSchedule(DianApplication.user.json);
                DianTool.dismissProgressDialog();
            }
        }
    }
    void initweek(int id){
        DianTool.huoqutoken();
        HttpParams params=new HttpParams();
        params.put("weekId", id);
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/course/getweek")).tag(this)
                .headers(headers).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setEnabled(false);
                if (!TextUtils.isEmpty(s)) {
                    initSchedule(s);
                }
                datapop();
                DianApplication.sharedPreferences.saveString("contenttable", s);
                DianApplication.user.json = s;
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                swipeLayout.setRefreshing(false);
                DianTool.response(response,SyllFragments.this);
            }
        });
    }
    void datapop(){
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        OkGo.get(HostAdress.getRequest("/api/phone/v1/week/getList")).tag(this)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if(TextUtils.isEmpty(s)){
                    DianTool.showTextToast(SyllFragments.this,"没有数据");
                }else {
                    ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                    if (resultInfo.weekList.size()!=0) {
                        initwed(resultInfo.weekList);
                    }
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.response(response,SyllFragments.this);
            }
        });
    }
    void initwed(final List<Course> weeklist){
        lletv.removeAllViews();
        if (listed.size()!=0){
            listed.clear();
            lisnid.clear();
            listtv.clear();
        }
        for (int i=0;i<weeklist.size();i++){
            lisnid.add(weeklist.get(i).id);
            listed.add(weeklist.get(i).name);
            final TextView textView = new TextView(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(schedule.getColumnWidth()+3, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setText(weeklist.get(i).name);
            textView.setPadding(0,5,0,5);
            juli=schedule.getColumnWidth();
            if (!TextUtils.isEmpty(weekName)) {
                if (weekName.equals(weeklist.get(i).name)) {
                    index = i;
                    textView.setTextColor(getResources().getColor(R.color.sytv));
                    textView.setTextSize(20);
                    textView.setBackgroundResource(R.drawable.syform_corner);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.sy));
                    textView.setBackgroundResource(R.color.shoutous);
                    textView.setTextSize(15);
                }
            }
            if (!TextUtils.isEmpty(name)){
                if (textView.getText().toString().trim().equals(name)){
                    textView.setTextColor(getResources().getColor(R.color.sys));
                }else{
                    textView.setTextColor(getResources().getColor(R.color.sy));
                }
            }
            listtv.add(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name=textView.getText().toString().trim();
                    int ids=listed.indexOf(name);
                    if (!name.equals(resultInfoweek.name)) {
                        for (int i=0;i<listtv.size();i++) {
                            if (listtv.get(i).getText().toString().trim().equals(name)) {
                                listtv.get(i).setTextColor(getResources().getColor(R.color.sys));
                            }else{
                                listtv.get(i).setTextColor(getResources().getColor(R.color.sy));
                            }
                        }
                        tvbenzhou.setVisibility(View.GONE);
                        llcurrent.setVisibility(View.VISIBLE);
                        tvbenzhous.setText("第" +name + "周");
                        tvbenzhoud.setText("返回当前周");
                    }else{
                        llcurrent.setVisibility(View.GONE);
                        tvbenzhou.setVisibility(View.VISIBLE);
                        tvbenzhou.setText("第" + name + "周");
                    }
                    if (listdate.size()!=0){
                        listdate.clear();
                    }
                    if (!TextUtils.isEmpty(name)) {
                        if (!TextUtils.isEmpty(resultInfoweek.name)) {
                            int nameone=Integer.parseInt(name);
                            int nametwo=Integer.parseInt(resultInfoweek.name);
                            if (nameone > nametwo) {
                                List<String> listred=DianTool.getNextTimeInterval(nameone-nametwo);
                                for (int i=0;i<listred.size();i++){
                                    listdate.add(listred.get(i).split("-")[1]);
                                }
                                forearcher(DianTool.getNextTimeInterval(nameone-nametwo).get(0).split("-")[0],mWay,tvmonth);
                            } else {
                                List<String> listred=DianTool.getLastTimeInterval(nametwo-nameone);
                                for (int i=0;i<listred.size();i++){
                                    listdate.add(listred.get(i).split("-")[1]);
                                }
                                forearcher(listred.get(0).split("-")[0],mWay,tvmonth);
                            }
                        }
                    }
                    initweek(lisnid.get(ids));
                }
            });
            lletv.addView(textView);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onCourseClick(TableData.CourseListEntity model) {
        Intent intent=new Intent(this, CourseActivity.class);
        intent.putExtra(Constant.KE_ID,model.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
}
