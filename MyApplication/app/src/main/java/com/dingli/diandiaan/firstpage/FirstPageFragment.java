package com.dingli.diandiaan.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.PaiXuFa;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.firstpage.callset.CallSetActivity;
import com.dingli.diandiaan.login.ActionSheetDialog;
import com.dingli.diandiaan.rollcall.NewCallRollActivity;
import com.dingli.diandiaan.setting.HelpActivity;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.alibaba.fastjson.JSON;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/3/7.
 */
public class FirstPageFragment extends BaseActivity implements View.OnClickListener,ListItemFirstPageView.onCancelCollectListener{

    RelativeLayout rlxian;
    ListView xlvFirstPages;
    TextView firstpageriqi,firstpageweek;
    ZhuanYeAdapter adapter;
    FirstPageFragment parent;
    boolean firstIn=true;
    TextView dijizhou;
    ImageView firstback;
    public static FirstPageFragment newInstance(){
        FirstPageFragment firstPageFragment=new FirstPageFragment();
        return  firstPageFragment;
    }
    HttpHeaders headers;
    String stritem;
    SimpleDateFormat formatter;
    VerticalSwipeRefreshLayout reffirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_firstpage);
        parent=FirstPageFragment.this;
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        init();
        adapter = new ZhuanYeAdapter(parent,this);
        xlvFirstPages.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        reffirst.setRefreshing(true);
        showlistfirst(1, stritem);
    }
    public void init() {
        reffirst=(VerticalSwipeRefreshLayout)findViewById(R.id.reffirst);
        reffirst.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        reffirst.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showlistfirst(1, stritem);
            }
        });
        rlxian = (RelativeLayout) findViewById(R.id.rlxian);
        firstback = (ImageView) findViewById(R.id.firstback);
       firstback.setOnClickListener(this);
       ImageView ivwenhao=(ImageView)findViewById(R.id.ivwenhao);
       ivwenhao.setOnClickListener(this);
       xlvFirstPages = (ListView) findViewById(R.id.xlvFirstPages);
        reffirst.setViewGroup(xlvFirstPages);
        xlvFirstPages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem==0){
                        reffirst.setEnabled(true);
                    }else{
                        reffirst.setEnabled(false);
                    }
            }
        });
       firstpageweek = (TextView) findViewById(R.id.firstpageweek);
       firstpageriqi = (TextView) findViewById(R.id.firstpageriqi);
       dijizhou = (TextView) findViewById(R.id.dijizhou);
       MaterialSpinner spinner=(MaterialSpinner)findViewById(R.id.spinnere);
       List<String> list=new ArrayList<>();
       formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       Date date=new Date();
       for (int i=0;i<7;i++){
           long longs=date.getTime()-(i*24*60*60*1000);
           Date date1=new Date(longs);
           String strs1=formatter.format(date1);
           String[] strings=strs1.split(" ");
           list.add(strings[0]);
       }
       spinner.setItems(list);
       spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
               stritem=String.valueOf(item);
               firstIn=true;
               showlistfirst(1,stritem);
           }
       });
   }
   public void showlistfirst(final int page,String ttime){
       if(DianTool.isConnectionNetWork(this)) {
           HttpParams params = new HttpParams();
           params.put("offset", page);
           params.put("teachTime",ttime);
           params.put("limit",500);
           DianTool.huoqutoken();
           headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
           OkGo.getInstance().addCommonHeaders(headers);
           OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/course/getList")).tag(this).params(params).execute(new StringCallback() {
               @Override
               public void onSuccess(String s, Call call, Response response) {
                   reffirst.setRefreshing(false);
                   if (!TextUtils.isEmpty(s)) {
                       if (!s.equals("{}")) {
                           ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                           if (resultInfo.data[0].weekName != null) {
                               dijizhou.setText(getResources().getString(R.string.di) + resultInfo.data[0].weekName + getResources().getString(R.string.zhou));
                           }
                           firstpageriqi.setText(resultInfo.data[0].teach_time);
                           if (resultInfo.data[0].dayOfWeek.equals("4")) {
                               firstpageweek.setText("星期四");
                           } else if (resultInfo.data[0].dayOfWeek.equals("1")) {
                               firstpageweek.setText("星期一");
                           } else if (resultInfo.data[0].dayOfWeek.equals("2")) {
                               firstpageweek.setText("星期二");
                           } else if (resultInfo.data[0].dayOfWeek.equals("3")) {
                               firstpageweek.setText("星期三");
                           } else if (resultInfo.data[0].dayOfWeek.equals("5")) {
                               firstpageweek.setText("星期五");
                           } else if (resultInfo.data[0].dayOfWeek.equals("6")) {
                               firstpageweek.setText("星期六");
                           } else if (resultInfo.data[0].dayOfWeek.equals("7")) {
                               firstpageweek.setText("星期日");
                           }
                           if (resultInfo.data[0].courseList == null || resultInfo.data[0].courseList.size() == 0) {
                               rlxian.setVisibility(View.VISIBLE);
                               xlvFirstPages.setVisibility(View.GONE);
                           } else {
                               rlxian.setVisibility(View.GONE);
                               xlvFirstPages.setVisibility(View.VISIBLE);
                               adapter.refreshFirstlist(resultInfo.data[0].courseList);
                               adapter.notifyDataSetChanged();
                           }
                       }
                   }
               }
               @Override
               public void onError(Call call, Response response, Exception e) {
                   reffirst.setRefreshing(false);
                   DianTool.response(response,parent);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.firstback:
                firstback.setClickable(false);
                DianApplication.user.libiao=null;
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.ivwenhao:
                Intent intent=new Intent(this, HelpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
        }
    }
    @Override
    public void onCancelCollect(View v,final int courseid,int id,final String courseName,String teach_times,String classEndTime,boolean blea,int lateTime,int reUser) {
            v.setClickable(false);
            intdata(v, courseid, id, teach_times, courseName, classEndTime, blea,lateTime,reUser);
    }
    void intdata(final View v,final int coureseid,final int id,final String teach_times,final String courseName,final String classEndTime, final boolean blea,final int lateTime,
                 final  int reUser){
        if(DianTool.isConnectionNetWork(this)) {
                    Date date=new Date();
                    v.setClickable(true);
                    long timeMillis = date.getTime();
                    try {
                        String isshool = "";
                        Date classendtime = formatter.parse(teach_times + " " + classEndTime);
                        if (classendtime.getTime() - timeMillis < 0) {
                            isshool = "false";
                        } else {
                            isshool = "true";
                            }
                        if (blea == true) {
                            Intent intent = new Intent(parent, NewCallRollActivity.class);
                            intent.putExtra(Constant.KE_ID, id);
                            intent.putExtra(Constant.COURSEID, coureseid);
                            intent.putExtra(Constant.COURSE_NAME, courseName);
                            intent.putExtra(Constant.ISSHOOL, isshool);
                            intent.putExtra(Constant.LATE_TIME,lateTime);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        } else {
                            new ActionSheetDialog(parent)
                                    .builder()
                                    .setTitle("您尚未开启点名，不能查看考勤哦")
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(true)
                                    .addSheetItem("马上开启", ActionSheetDialog.SheetItemColor.Green,
                                            new ActionSheetDialog.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(int which) {
                                                    Intent intent = new Intent(parent, CallSetActivity.class);
                                                    intent.putExtra(Constant.KE_ID, id);
                                                    intent.putExtra(Constant.COURSEID,coureseid);
                                                    intent.putExtra(Constant.COURSE_NAME, courseName);
                                                    intent.putExtra(Constant.RUKOU, "2");
                                                    intent.putExtra(Constant.LATE_TIME,lateTime);
                                                    intent.putExtra(Constant.CODE,"e");
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                                }
                                            })
                                    .show();
                        }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
            }else{
            DianTool.showTextToast(FirstPageFragment.this, "请检查网络");
        }
    }
}
