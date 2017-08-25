package com.dingli.diandiaan.evaluation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.Event;
import com.dingli.diandiaan.bean.PaiXuPinJiao;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class EvaluationActivity extends BaseActivity implements View.OnClickListener, OnChartValueSelectedListener {
    int currentPage = 1;
    Result resultInfo;
    TextView pinjunfen, wuwenzi, shaixuan;
    ImageView wupiniv;
    LinearLayout lipingj;
    int kid;
    String dianminzongren;
    TextView tvevaluation;
    PieChart mChartfen;
    boolean scrollFlag = false;
    TextView shaixuans, tvall, wuxingeshu, wuxinbili,
            sixingeshu, sixinbili, sanxingeshu, sanxinbili, erxingeshu, erxinbili, yixingeshu, yixinbili;
    String names;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout refeva;
    LinearLayout liwuxin,lisixin,lisanxin,lierxin,liyixin;
//    ListView evaluationlist;
    RelativeLayout rlheader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        EventBus.getDefault().register(this);
        String name = getIntent().getStringExtra(Constant.COURSE_NAME);
        String dianmiren = getIntent().getStringExtra("dimingren");
        String id = getIntent().getStringExtra("id_ke");
        names = getIntent().getStringExtra("className");
        dianminzongren = getIntent().getStringExtra("dimingzongren");
        headers = new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview(name, dianmiren, dianminzongren);
        refeva.setRefreshing(true);
        if (TextUtils.isEmpty(id)) {
           final int classIds = getIntent().getIntExtra("classId", 0);
           final int scheduleIds = getIntent().getIntExtra("scheduleId", 0);
            initdata(classIds, scheduleIds);
            shaixuan.setVisibility(View.GONE);
            shaixuans.setVisibility(View.GONE);
            refeva.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initdata(classIds, scheduleIds);
                }
            });
        } else {
            shaixuan.setVisibility(View.VISIBLE);
            shaixuans.setVisibility(View.GONE);
            kid = Integer.parseInt(id);
            showlistEvaluation(1, Constant.REFRESH);
            refeva.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    showlistEvaluation(1, Constant.REFRESH);
                }
            });
        }
    }

    void initdata(int clasid, int schedulId) {
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            HttpParams params = new HttpParams();
            params.put("classId", clasid);
            params.put("scheduleId", schedulId);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getassessbyclass")).tag(this).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refeva.setRefreshing(false);
                    if (!TextUtils.isEmpty(s)) {
                        resultInfo = JSON.parseObject(s, Result.class);
                        if (resultInfo.data.size() == 0) {
                            wupiniv.setVisibility(View.VISIBLE);
                            wuwenzi.setVisibility(View.VISIBLE);
                            lipingj.setVisibility(View.GONE);
                            rlheader.setVisibility(View.GONE);
//                        ivup.setVisibility(View.GONE);
                            shaixuan.setClickable(false);
                        } else {
                            shaixuan.setClickable(true);
                            wupiniv.setVisibility(View.GONE);
                            wuwenzi.setVisibility(View.GONE);
                            lipingj.setVisibility(View.GONE);
                            DecimalFormat df = new DecimalFormat("######0.00");
                            String dou = df.format(resultInfo.scour);
                            pinjunfen.setText(dou);
                            rlheader.setVisibility(View.VISIBLE);
//                        evaluationlist.addHeaderView(getView(resultInfo.data.size() + ""));
                            pie(resultInfo.data);
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refeva.setRefreshing(false);
                    DianTool.response(response,EvaluationActivity.this);
                }
            });

        } else {
            DianTool.showTextToast(EvaluationActivity.this, "请设置网络");
        }

    }

    public void initview(String name, String dianm, String di) {
        refeva=(VerticalSwipeRefreshLayout)findViewById(R.id.refeva);
        refeva.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        shaixuan = (TextView) findViewById(R.id.shaixuan);
        shaixuans = (TextView) findViewById(R.id.shaixuans);
        lipingj = (LinearLayout) findViewById(R.id.lipingj);
        wuwenzi = (TextView) findViewById(R.id.wuwenzi);
        wupiniv = (ImageView) findViewById(R.id.wupiniv);
//        ivup = (ImageView) findViewById(R.id.ivup);
        pinjunfen = (TextView) findViewById(R.id.pinjunfen);
        rlheader = (RelativeLayout) findViewById(R.id.rlheader);
        tvevaluation = (TextView) findViewById(R.id.tvevaluation);
        if (TextUtils.isEmpty(dianm)) {
            tvevaluation.setText("查看评教");
        } else {
            tvevaluation.setText(getResources().getString(R.string.pingjiaos) + dianm + "/" + di + ")");
//            evaluationlist.addHeaderView(getView(dianm));
        }
        shaixuan.setOnClickListener(this);
//        ivup.setOnClickListener(this);
        ImageView evaluationback = (ImageView) findViewById(R.id.evaluationback);
        evaluationback.setOnClickListener(this);
        getView(dianm);
    }

    public void showlistEvaluation(int page, final int type) {
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            currentPage = page;
            HttpParams params = new HttpParams();
            params.put("schedule_id", kid);
            params.put("offset", page);
            params.put("limit", 500);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.get(HostAdress.getZheRe("/api/phone/v1/assess/get")).tag(this).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refeva.setRefreshing(false);
                    if (!TextUtils.isEmpty(s)) {
                        resultInfo = JSON.parseObject(s, Result.class);
                        if (resultInfo.data.size() == 0) {
                            wupiniv.setVisibility(View.VISIBLE);
                            wuwenzi.setVisibility(View.VISIBLE);
                            rlheader.setVisibility(View.GONE);
                            lipingj.setVisibility(View.GONE);
//                        ivup.setVisibility(View.GONE);
                            shaixuan.setClickable(false);
                        } else {
                            shaixuan.setClickable(true);
                            wupiniv.setVisibility(View.GONE);
                            wuwenzi.setVisibility(View.GONE);
                            rlheader.setVisibility(View.VISIBLE);
//                        ivup.setVisibility(View.VISIBLE);
                            lipingj.setVisibility(View.VISIBLE);
                            DecimalFormat df = new DecimalFormat("######0.00");
                            String dou = df.format(resultInfo.scour);
                            pinjunfen.setText(dou);
                            pie(resultInfo.data);
//                        List<Course> ared = PaiXuPinJiao.getpaixu(resultInfo.data);
//                        adapter.refreshPinglist(ared);
//                        adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refeva.setRefreshing(false);
                    DianTool.response(response,EvaluationActivity.this);
                }
            });
        } else {
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    void pie(List<Course> data) {
        int cishuone = 0;
        mChartfen.setHoleColor(Color.TRANSPARENT);
        mChartfen.setHoleRadius(30f);  //半径
        mChartfen.setTransparentCircleRadius(31f); // 半透明圈
        mChartfen.setDrawCenterText(true);  //饼状图中间可以添加文字
        mChartfen.setDescription(" ");
        mChartfen.setDrawHoleEnabled(true);
        mChartfen.setRotationEnabled(true);// 不可以手动旋转
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).score.equals("1")) {
                cishuone += 1;
                continue;
            }
            if (data.get(i).score.equals("2")) {
                cishuone += 1;
                continue;
            }
            if (data.get(i).score.equals("3")) {
                cishuone += 1;
                continue;
            }
            if (data.get(i).score.equals("4")) {
                cishuone += 1;
                continue;
            }
            if (data.get(i).score.equals("5")) {
                cishuone += 1;
                continue;
            }

        }
        PieData pieData = getPieData(cishuone, data);

        //设置数据
        mChartfen.setData(pieData);
        Legend mLegend = mChartfen.getLegend();  //设置比例图
        mLegend.setEnabled(false);// 不展示比例图
        mChartfen.animateXY(1000, 1000);  //设置动画
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evaluationback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
            case R.id.shaixuan:
                Intent in = new Intent(EvaluationActivity.this, ChoceClassActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
//            case R.id.ivup:
//                evaluationlist.scrollTo(0, 0);
//                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    /**
     * @param count 分成几部分
     */
    private PieData getPieData(int count, List<Course> arrsy) {
        float a = 0;
        float b = 0;
        float c = 0;
        float d = 0;
        float f = 0;
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        for (int i = 0; i < count; i++) {
            xValues.add("");
        }
        List<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < arrsy.size(); i++) {
            if (arrsy.get(i).score.equals("1")) {
                a += 1;
            } else if (arrsy.get(i).score.equals("2")) {
                b += 1;
            } else if (arrsy.get(i).score.equals("3")) {
                c += 1;
            } else if (arrsy.get(i).score.equals("4")) {
                d += 1;
            } else if (arrsy.get(i).score.equals("5")) {
                f += 1;
            }
        }
//        NumberFormat num = NumberFormat.getPercentInstance();
//        num.setMaximumIntegerDigits(3);
//        num.setMaximumFractionDigits(2);
//        double csdn = 0.20;

        if (a != 0) {
            liyixin.setVisibility(View.VISIBLE);
            float as = (a / (a + b + c + d + f)) * 100;
            float l1 = Math.round(as);
            int l2 = (int) as;
            yValues.add(new PieEntry(l2, ""));
            String wuff="";
            String wuf=String.valueOf(a);
            if (wuf.contains(".")){
                wuff=wuf.substring(0, wuf.indexOf("."));
            }else{
                wuff=String.valueOf(a);
            }
            yixingeshu.setText(wuff+"人");
            yixinbili.setText(l2+"%");
            colors.add(Color.rgb(179, 218, 41));
        }else{
            liyixin.setVisibility(View.GONE);
        }
        if (b != 0) {
            lierxin.setVisibility(View.VISIBLE);
            float as = (b / (a + b + c + d + f)) * 100;
            float l1 = Math.round(as);
            int l2 = (int) as;
            String wuff="";
            String wuf=String.valueOf(b);
            if (wuf.contains(".")){
                wuff=wuf.substring(0, wuf.indexOf("."));
            }else{
                wuff=String.valueOf(b);
            }
            erxingeshu.setText(wuff+"人");
            erxinbili.setText(l2+"%");
            yValues.add(new PieEntry(l2, ""));
            colors.add(Color.rgb(120, 192, 242));
        }else{
            lierxin.setVisibility(View.GONE);
        }
        if (c != 0) {
            lisanxin.setVisibility(View.VISIBLE);
            float as = (c / (a + b + c + d + f)) * 100;
            float l1 = Math.round(as);   //四舍五入
            int l2 = (int) as;
            String wuff="";
            String wuf=String.valueOf(c);
            if (wuf.contains(".")){
                wuff=wuf.substring(0, wuf.indexOf("."));
            }else{
                wuff=String.valueOf(c);
            }
            sanxingeshu.setText(wuff+"人");
            sanxinbili.setText(l2+"%");
            yValues.add(new PieEntry(l2, ""));
            colors.add(Color.rgb(254, 180, 29));
        }else{
            lisanxin.setVisibility(View.GONE);
        }
        if (d != 0) {
            lisixin.setVisibility(View.VISIBLE);
            float as = (d / (a + b + c + d + f)) * 100;
            float l1 = Math.round(as);   //四舍五入
            int l2 = (int) as;
            String wuff="";
            String wuf=String.valueOf(d);
            if (wuf.contains(".")){
                wuff=wuf.substring(0, wuf.indexOf("."));
            }else{
                wuff=String.valueOf(d);
            }
            sixingeshu.setText(wuff+"人");
            sixinbili.setText(l2+"%");
            yValues.add(new PieEntry(l2, ""));
            colors.add(Color.rgb(197, 115, 226));
        }else{
            lisixin.setVisibility(View.GONE);
        }
        if (f != 0) {
            liwuxin.setVisibility(View.VISIBLE);
            float as = (f / (a + b + c + d + f)) * 100;
            int l2 = (int) as;
            float l1 = Math.round(as);   //四舍五入
            String wuff="";
            String wuf=String.valueOf(f);
            if (wuf.contains(".")){
               wuff=wuf.substring(0, wuf.indexOf("."));
            }else{
                wuff=String.valueOf(f);
            }
            wuxingeshu.setText(wuff+"人");
            wuxinbili.setText(l2 + "%");
            yValues.add(new PieEntry(l2, ""));
            colors.add(Color.rgb(255, 85, 60));
        }else{
            liwuxin.setVisibility(View.GONE);
        }
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "Quarterly Revenue 2014"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(9f);
        pieData.setValueTextColor(Color.BLACK);
        return pieData;
    }
    public void getView(String di) {
        tvall = (TextView)findViewById(R.id.tvall);
        wuxingeshu = (TextView)findViewById(R.id.wuxingeshu);
        wuxinbili = (TextView)findViewById(R.id.wuxinbili);
        sixingeshu = (TextView)findViewById(R.id.sixingeshu);
        sixinbili = (TextView)findViewById(R.id.sixinbili);
        sanxingeshu = (TextView)findViewById(R.id.sanxingeshu);
        sanxinbili = (TextView)findViewById(R.id.sanxinbili);
        erxingeshu = (TextView)findViewById(R.id.erxingeshu);
        erxinbili = (TextView)findViewById(R.id.erxinbili);
        yixingeshu = (TextView)findViewById(R.id.yixingeshu);
        yixinbili = (TextView)findViewById(R.id.yixinbili);
        liwuxin=(LinearLayout)findViewById(R.id.liwuxin);
        lisixin=(LinearLayout)findViewById(R.id.lisixin);
        lisanxin=(LinearLayout)findViewById(R.id.lisanxin);
        lierxin=(LinearLayout)findViewById(R.id.lierxin);
        liyixin=(LinearLayout)findViewById(R.id.liyixin);
        if (TextUtils.isEmpty(names)) {
            tvall.setText("全部班级");
        } else {
            tvall.setText(names);
        }
        mChartfen = (PieChart)findViewById(R.id.mChartfen);
        mChartfen.setNoDataText("");
        mChartfen.setNoDataTextDescription("");
        if (TextUtils.isEmpty(di)){
            mChartfen.setCenterText("");
        }else {
            mChartfen.setCenterText("共评教" + di + "次");
        }
        mChartfen.setTouchEnabled(false);
//        mChartfen.setCenterTextColor(R.color.wenziyanse);
    }

    public void onEventMainThread(Event event) {
        switch (event.event) {
            case Event.EVENT_SOUSUO:
                int classid = Integer.parseInt(event.string);
                selectlist(classid);
                break;
            case Event.EVENT_SHAKE_INFO:
                tvall.setText(event.string);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);
    }

    void selectlist(final int classid) {
        resultInfo.data.clear();
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            HttpParams params = new HttpParams();
            params.put("scheduleId", kid);
            params.put("classId", classid);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getassessbyclass")).tag(this).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (!TextUtils.isEmpty(s)) {
                        Result courses = JSON.parseObject(s, Result.class);
//                    adapter.addPinglist(courses.data);
                        pie(courses.data);
                        List<Course> ared = PaiXuPinJiao.getpaixu(courses.data);
//                    adapter.refreshPinglist(ared);
//                    adapter.notifyDataSetChanged();
                        mChartfen.setCenterText("共评教" + ared.size() + "次");
                        double fenshu = 0;
                        for (int i = 0; i < courses.data.size(); i++) {
                            fenshu += Integer.parseInt(courses.data.get(i).score);
                        }
                        double dou = fenshu / courses.data.size();
                        DecimalFormat df = new DecimalFormat("######0.00");
                        String dous = df.format(dou);
                        pinjunfen.setText(dous);
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.showTextToast(EvaluationActivity.this, "没有数据");
                }
            });
        } else {
            DianTool.showTextToast(this, "请检查网络");
        }
    }
}
