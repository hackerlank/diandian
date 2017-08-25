package com.dingli.diandiaan.login.chuqinlv.studentCql;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class StudentCqlActivity extends BaseActivity implements View.OnClickListener{

    private TextView class_name;
    private TextView class_cql;
    private SideBar sideBar;
    private TextView dialog;
    private ImageView cqlback;
    private ListView student_cql_listView;
    private List<StudentModel> data;
    private SortAdapter adapter;
    private String className;
    private String classCql;
    private int classId;
    private int courseId;
    HttpHeaders headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_cql);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
        initData();
    }

    private void fillData() {
        class_name.setText(className);
        class_cql.setText(classCql);
        adapter = new SortAdapter(this,data);
        student_cql_listView.setAdapter(adapter);
    }

    private void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            className = bundle.getString("className");
            classCql = bundle.getString("classCql");
            classId =bundle.getInt("classId");
            courseId =bundle.getInt("courseId");
        }
        if(DianTool.isConnectionNetWork(this)) {
            DianTool.showDialog(this,"加载中...");
            DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE)+"-"+
                    DianApplication.sharedPreferences.getStringValue(Constant.SPLITTWO)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITTHREE)
                    +"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFOUR)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFIVE);
            HttpParams params=new HttpParams();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            params.put("classId", classId);
            params.put("courseId", courseId);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getrollcallbyclassandcourse")).tag(this)
                    .params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    if (TextUtils.isEmpty(s)) {
                        DianTool.showTextToast(StudentCqlActivity.this, "数据为空");
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            data = JSON.parseArray(jsonArray.toString(), StudentModel.class);
                            sort();
                            fillData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }

    }
    private void sort(){
        data=getUserList();
        Collections.sort(data, new Comparator<StudentModel>() {
            @Override
            public int compare(StudentModel lhs, StudentModel rhs) {
                if (lhs.getCode().equals("#")) {
                    return 1;
                } else if (rhs.getCode().equals("#")) {
                    return -1;
                } else {
                    return lhs.getCode().compareTo(rhs.getCode());
                }
            }
        });
    }

    private void initView() {
        class_name= (TextView) findViewById(R.id.class_name);
        class_cql= (TextView) findViewById(R.id.class_cql);
        cqlback= (ImageView) findViewById(R.id.cqlback);
        cqlback.setOnClickListener(this);
        sideBar = (SideBar) findViewById(R.id.sideBar);
        dialog = (TextView) findViewById(R.id.dialog);
        student_cql_listView= (ListView) findViewById(R.id.student_cql_listView);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
//                student_cql_listView.setSelection();
                int position = adapter.getPositionForSection(s.charAt(0));
                student_cql_listView.setSelection(position);
            }
        });
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
    }
    private List<StudentModel> getUserList() {
        List<StudentModel> li = new ArrayList<StudentModel>();
        for (int i = 0; i < data.size(); i++) {
            StudentModel studentModel = new StudentModel();
            String studentName = data.get(i).getStudentName();
            String pinyin = ChineseToPinyinHelper.getInstance().getPinyin(studentName);
            String firstLetter = pinyin.substring(0, 1).toUpperCase();
            if (firstLetter.matches("[A-Z]")) {
                studentModel.setCode(firstLetter);
            } else {
                studentModel.setCode("#");
            }
            studentModel.setAllCount(data.get(i).getAllCount());
            studentModel.setStudentId(data.get(i).getStudentId());
            studentModel.setStudentName(data.get(i).getStudentName());
            studentModel.setType1Count(data.get(i).getType1Count());
            studentModel.setType2Count(data.get(i).getType2Count());
            studentModel.setType3Count(data.get(i).getType3Count());
            studentModel.setType4Count(data.get(i).getType4Count());
            studentModel.setType5Count(data.get(i).getType5Count());
            li.add(studentModel);
        }
        return li;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cqlback:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
