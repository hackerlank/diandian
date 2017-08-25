package com.dingli.diandiaan.schedule;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.schedule.picture.ImgFileListFormActivity;

/**
 * Created by dingliyuangong on 2017/5/18.
 */

public class SyFormActivity extends BaseActivity implements View.OnClickListener{

    ImageView pitchupone,pitchuptwo,pitchupthree,pitchupfour,pitchupfive;
    ImageView defaultone,defaulttwo,defaultthree,defaultfour,defaultfive;
    ImageView[] iv;
    ImageView ivformes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syform);
        initview();
    }
    void initview(){
        pitchupone=(ImageView) findViewById(R.id.pitchupone);
        pitchuptwo=(ImageView) findViewById(R.id.pitchuptwo);
        pitchupthree=(ImageView) findViewById(R.id.pitchthree);
        pitchupfour=(ImageView) findViewById(R.id.pitchupfour);
        pitchupfive=(ImageView) findViewById(R.id.pitchupfive);
        defaultone=(ImageView) findViewById(R.id.defaultone);
        defaulttwo=(ImageView) findViewById(R.id.defaulttwo);
        defaultthree=(ImageView) findViewById(R.id.defaultthree);
        defaultfour=(ImageView) findViewById(R.id.defaultfour);
        defaultfive=(ImageView) findViewById(R.id.defaultfive);
        ImageView vacateback=(ImageView) findViewById(R.id.vacateback);
        LinearLayout lineaphoto=(LinearLayout) findViewById(R.id.lineaphoto);
        ivformes=(ImageView) findViewById(R.id.ivformes);
        lineaphoto.setOnClickListener(this);
        vacateback.setOnClickListener(this);
        defaultone.setOnClickListener(this);
        defaulttwo.setOnClickListener(this);
        defaultthree.setOnClickListener(this);
        defaultfour.setOnClickListener(this);
        defaultfive.setOnClickListener(this);
        iv=new ImageView[]{pitchupone,pitchuptwo,pitchupthree,pitchupfour,pitchupfive};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.defaultone:
                visib(0);
                DianApplication.sharedPreferences.saveString("defalt","1");
                break;
            case R.id.defaulttwo:
                visib(1);
                DianApplication.sharedPreferences.saveString("defalt","2");
                break;
            case R.id.defaultthree:
                visib(2);
                DianApplication.sharedPreferences.saveString("defalt","3");
                break;
            case R.id.defaultfour:
                visib(3);
                DianApplication.sharedPreferences.saveString("defalt","4");
                break;
            case R.id.defaultfive:
                visib(4);
                DianApplication.sharedPreferences.saveString("defalt","6");
                break;
            case R.id.lineaphoto:
                jumpto();
                break;
            case R.id.vacateback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;

        }
    }
    void visib(int vd){
        ivformes.setVisibility(View.GONE);
        DianApplication.sharedPreferences.saveString("fileone","");
        for (int i=0;i<iv.length;i++){
            if (i==vd){
                iv[i].setVisibility(View.VISIBLE);
            }else{
                iv[i].setVisibility(View.GONE);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void jumpto(){
        if (DianTool.getsdkbanbe()>22){
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
                return;
            }
        }
        Intent intent = new Intent(this,
                ImgFileListFormActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意使用write
                Intent intent = new Intent(this,
                        ImgFileListFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }else{
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储");
            }
        }
    }
    int zizengs=0;
    void quanxian(String string,String s){
        if (zizengs==0) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, string)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("该功能需要赋予访问" + s + "的权限，不开启将无法正常工作！")
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

    @Override
    protected void onResume() {
        super.onResume();
        String strfile=DianApplication.sharedPreferences.getStringValue("fileone");
        if (!TextUtils.isEmpty(strfile)) {
            ivformes.setVisibility(View.VISIBLE);
            for (int i=0;i<iv.length;i++){
                    iv[i].setVisibility(View.GONE);
            }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(strfile,options);
                ivformes.setImageBitmap(bm);
                DianApplication.sharedPreferences.saveString("defalt","5");
        }else{
            String str=DianApplication.sharedPreferences.getStringValue("defalt");
            if (!TextUtils.isEmpty(str)) {
                pitchupfive.setVisibility(View.GONE);
                switch (str) {
                    case "1":
                        pitchupone.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        pitchuptwo.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        pitchupthree.setVisibility(View.VISIBLE);
                        break;
                    case "4":
                        pitchupfour.setVisibility(View.VISIBLE);
                        break;
                    case "6":
                        pitchupfive.setVisibility(View.VISIBLE);
                        break;
                }
            }else{
                pitchupfive.setVisibility(View.VISIBLE);
            }
        }
    }
}
