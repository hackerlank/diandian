package com.dingli.diandiaan.contact;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.ServerApi;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.common.RollCallList;
import com.dingli.diandiaan.login.ActionSheetDialog;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/5/16.
 */

public class ContactSearchActivity extends BaseActivity implements View.OnClickListener,ListContactView.ContactListener{
    ContactAdapter adapter;
    String phones, names, stuId;
    LinearLayout llbigdata,llzuisearch;
    EditText contactsearch;
    String wenzi;
    TextView tvqingk;
    TextView tvsearchone,tvsearchtwo,tvsearchthree,tvsearchfour,tvsearchfive,tvsearchsix,tvsearchseven
            ,tvsearcheight,tvsearchnine,tvsearchten;
    TextView[] listTextView;
    ListView lvsousuo;
    String strnews;
    VerticalSwipeRefreshLayout searchrefresh;
    List<String> listcontact;
    List<String> listcontacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intview();
    }
    void intview(){
        listcontact=new ArrayList<>();
        listcontacts=new ArrayList<>();
        searchrefresh=(VerticalSwipeRefreshLayout) findViewById(R.id.searchrefresh);
        DianTool.refresh(searchrefresh,this);
        TextView callrollsousuo=(TextView) findViewById(R.id.callrollsousuo);
        lvsousuo=(ListView) findViewById(R.id.lvsousuo);
        adapter=new ContactAdapter(this,"student",this);
        lvsousuo.setAdapter(adapter);
        llbigdata=(LinearLayout) findViewById(R.id.llbigdata);
        callrollsousuo.setOnClickListener(this);
        contactsearch=(EditText) findViewById(R.id.contactsearch);
        final ImageView ivclear=(ImageView) findViewById(R.id.ivclear);
        tvqingk=(TextView) findViewById(R.id.tvqingk);
        llzuisearch=(LinearLayout) findViewById(R.id.llzuisearch);
        tvsearchone=(TextView) findViewById(R.id.tvsearchone);
        tvsearchtwo=(TextView) findViewById(R.id.tvsearchtwo);
        tvsearchthree=(TextView) findViewById(R.id.tvsearchthree);
        tvsearchfour=(TextView) findViewById(R.id.tvsearchfour);
        tvsearchfive=(TextView) findViewById(R.id.tvsearchfive);
        tvsearchsix=(TextView) findViewById(R.id.tvsearchsix);
        tvsearchseven=(TextView) findViewById(R.id.tvsearchseven);
        tvsearcheight=(TextView) findViewById(R.id.tvsearcheight);
        tvsearchnine=(TextView) findViewById(R.id.tvsearchnine);
        tvsearchten=(TextView) findViewById(R.id.tvsearchten);
        listTextView=new TextView[]{tvsearchone,tvsearchtwo,tvsearchthree,tvsearchfour,tvsearchfive,tvsearchsix,tvsearchseven
                ,tvsearcheight,tvsearchnine,tvsearchten};
        for (int i=0;i<listTextView.length;i++){
            listTextView[i].setOnClickListener(this);
        }
        ivclear.setOnClickListener(this);
        tvqingk.setOnClickListener(this);
        contactsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
              wenzi=contactsearch.getText().toString().trim();
                if (TextUtils.isEmpty(wenzi)){
                    ivclear.setVisibility(View.GONE);
                }else{
                    ivclear.setVisibility(View.VISIBLE);
                }
            }
        });
        contactsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strnews=DianApplication.sharedPreferences.getStringValue("strkey").trim();
                if (listcontacts.size()!=0){
                    listcontacts.clear();
                }
                if (strnews.contains(",")){
                    String[] strname=strnews.split(",");
                        for (int i = strname.length-1; i >=0; i--) {
                            listcontacts.add(strname[i]);
                        }
                      for(int i=0;i<listcontacts.size();i++){
                          listTextView[i].setText(listcontacts.get(i));
                          listTextView[i].setVisibility(View.VISIBLE);
                      }
                }else{
                    tvsearchone.setText(strnews);
                    tvsearchone.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(strnews)){
                    searchrefresh.setVisibility(View.VISIBLE);
                    llbigdata.setVisibility(View.GONE);
                }else{
                    searchrefresh.setVisibility(View.GONE);
                    llbigdata.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    void initdata(String name){
        searchrefresh.setVisibility(View.VISIBLE);
        llbigdata.setVisibility(View.GONE);
        if (DianTool.isConnectionNetWork(this)){
            searchrefresh.setRefreshing(true);
            HttpHeaders headers = new HttpHeaders();
            HttpParams params = new HttpParams();
            params.put("name", name);
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest(ServerApi.StaffSearch)).tag(this).params(params)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    searchrefresh.setRefreshing(false);
                    if (!TextUtils.isEmpty(s)) {
                        List<RollCallList> resultCallOne = JSON.parseArray(s, RollCallList.class);
                        if (resultCallOne.size() != 0) {
                            adapter.refreshListContact(resultCallOne);
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.refreshListContact(resultCallOne);
                            adapter.notifyDataSetChanged();
                            DianTool.showTextToast(ContactSearchActivity.this, "没有数据");
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    searchrefresh.setRefreshing(false);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.callrollsousuo:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.ivclear:
                contactsearch.setText("");
                break;
            case R.id.tvqingk:
                DianApplication.sharedPreferences.saveString("strkey","");
                searchrefresh.setVisibility(View.VISIBLE);
                llbigdata.setVisibility(View.GONE);
                break;
            case R.id.tvsearchone:
                String contentone=tvsearchone.getText().toString().trim();
                if (!TextUtils.isEmpty(contentone)){
                    initdata(contentone);
                }
                break;
            case R.id.tvsearchtwo:
                String contenttwo=tvsearchtwo.getText().toString().trim();
                if (!TextUtils.isEmpty(contenttwo)){
                    initdata(contenttwo);
                }
                break;
            case R.id.tvsearchthree:
                String contentthree=tvsearchthree.getText().toString().trim();
                if (!TextUtils.isEmpty(contentthree)){
                    initdata(contentthree);
                }
                break;
            case R.id.tvsearchfour:
                String contentfour=tvsearchfour.getText().toString().trim();
                if (!TextUtils.isEmpty(contentfour)){
                    initdata(contentfour);
                }
                break;
            case R.id.tvsearchfive:
                String contentfive=tvsearchfive.getText().toString().trim();
                if (!TextUtils.isEmpty(contentfive)){
                    initdata(contentfive);
                }
                break;
            case R.id.tvsearchsix:
                String contentsix=tvsearchsix.getText().toString().trim();
                if (!TextUtils.isEmpty(contentsix)){
                    initdata(contentsix);
                }
                break;
            case R.id.tvsearchseven:
                String contentseven=tvsearchseven.getText().toString().trim();
                if (!TextUtils.isEmpty(contentseven)){
                    initdata(contentseven);
                }
                break;
            case R.id.tvsearcheight:
                String contenteight=tvsearcheight.getText().toString().trim();
                if (!TextUtils.isEmpty(contenteight)){
                    initdata(contenteight);
                }
                break;
            case R.id.tvsearchnine:
                String contentnine=tvsearchnine.getText().toString().trim();
                if (!TextUtils.isEmpty(contentnine)){
                    initdata(contentnine);
                }
                break;
            case R.id.tvsearchten:
                String contentten=tvsearchten.getText().toString().trim();
                if (!TextUtils.isEmpty(contentten)){
                    initdata(contentten);
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            String skey=DianApplication.sharedPreferences.getStringValue("strkey").trim();
            if (!TextUtils.isEmpty(skey)){
                if (skey.contains(",")){
                    String [] sname=skey.split(",");
                    for (String snam:sname){
                          snam=snam.trim();
                        listcontact.add(snam);
                    }
                }else{
                    listcontact.add(skey);
                }
            }
            listcontact.add(wenzi);
            List<String> newList = new  ArrayList<String>();
            for (String cd:listcontact) {
                if(!newList.contains(cd)){
                    newList.add(cd);
                }
            }
             if (newList.size()>10){
                 newList=newList.subList(newList.size()-10,newList.size());
             }
                  String strnew = newList.toString();
                  List<String> ld = Arrays.asList(strnew);
                  strnew = strnew.replace("[", "");
                  strnew = strnew.replace("]", "");
                  DianApplication.sharedPreferences.saveString("strkey", strnew);
            if (!TextUtils.isEmpty(wenzi)) {
                initdata(wenzi);
            }else{
                DianTool.showTextToast(this,"请输入姓名");
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onContactListener(String tv, final String phone, String name,final String stuIds) {
        if (!TextUtils.isEmpty(tv)) {
            phones = phone;
            names = name;
            stuId = stuIds;
            new ActionSheetDialog(this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拨打电话", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    callphone(phone, stuIds);
                                }
                            })
                    .addSheetItem("保存到手机通讯录", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    baocuncontact(phone);
                                }
                            })
                    .addSheetItem("复制", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboardManager.setText(phone);
                                }
                            })
                    .show();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void callphone(String phone, String stuIds) {
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        1004);
                return;
            }
        }
        baoda(phone, stuIds);
    }
    void baoda(String phone, String stuIds) {
        if (!phone.equals("null")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
            if (DianTool.isConnectionNetWork(this)) {
                JSONObject jsonObject = new JSONObject();
                HttpHeaders headers = new HttpHeaders();
                DianTool.huoqutoken();
                headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                try {
                    jsonObject.put("calledPhone", phone);
                    jsonObject.put("calledStudentId", stuIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (DianTool.isConnectionNetWork(this)) {
                    OkGo.post(HostAdress.getRequest(ServerApi.StaffBo)).headers(headers).tag(this).upJson(jsonObject)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                }
                            });
                }
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void baocuncontact(String phone) {
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
            int hasWriteContactsPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED ||
                    hasWriteContactsPermissions != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS,
                        },
                        1002);
                return;
            }
        }
        if (!phone.equals("null")) {
            DianTool.zhiding(this, phones, names);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002) {
            if (permissions[0].equals(Manifest.permission.WRITE_CONTACTS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && permissions[1].equals(Manifest.permission.READ_CONTACTS)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (!phones.equals("null")) {
                    DianTool.zhiding(this, phones, names);
                }
            } else {
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_CONTACTS, "保存到通讯录且读取通讯录");
            }
        } else if (requestCode == 1004) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!phones.equals("null")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }else{
                quanxian(Manifest.permission.CALL_PHONE,"打电话");
            }
        }
    }
    int zizengs;
    void quanxian(String quanxian, String s){
        if (zizengs==0) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,quanxian)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("该功能需要赋予调用" + s + "的权限，不开启将无法正常工作！")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                zizengs=1;
                return;
            }
        }
    }
}
