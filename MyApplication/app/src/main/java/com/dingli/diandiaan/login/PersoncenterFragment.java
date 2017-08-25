package com.dingli.diandiaan.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.BuildConfig;
import com.dingli.diandiaan.MaintiAcvity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.FileStorage;
import com.dingli.diandiaan.bean.FileUtils;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.GoSetDialog;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.instruc.InstructorActivity;
import com.dingli.diandiaan.login.chuqinlv.CqlActivity;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.MainActivity;
import com.dingli.diandiaan.adpter.OutQingAdapter;
import com.dingli.diandiaan.adpter.PingFenAdapter;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.information.EntrtyActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebsActivity;
import com.dingli.diandiaan.login.pingjiao.PingjiaoActivity;
import com.dingli.diandiaan.rule.RuleActivity;
import com.dingli.diandiaan.setting.HelpActivity;
import com.dingli.diandiaan.setting.InstructionActivity;
import com.dingli.diandiaan.setting.SettingActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/3/9.
 */
public class PersoncenterFragment extends Fragment implements View.OnClickListener{

    private static final String PHOTO_FILE_PATH = getPath(Environment.getExternalStorageDirectory()+"/"+"zhijiao");
    File tempFile;
    CircleImageView personci;
    View personcenterView;
    TextView tvmingzi,tvorgain;
    ImageView rlson;
    TextView tvmingzix;
    HttpHeaders headers;
    int zizengs;
    int photo,phone,chenggong;
    LinearLayout llshezhi,ll_help,ll_helps,llwoshi,ll_setting,ll_settings;
    private String mFilePath;
    private String mFileName;
    Uri imageUri;
    File file;
    public static PersoncenterFragment newInstance(){
        PersoncenterFragment personcenterFragment=new PersoncenterFragment();
        return  personcenterFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         personcenterView=inflater.inflate(R.layout.activity_personcenter,container,false);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
       headers.put("Accept", Constant.APPLICATION_JSON);
        init(personcenterView);
        tempFile = getFile(PHOTO_FILE_PATH+"/diandian.jpg");
        FileUtils.init();
        mFilePath = FileUtils.getFileDir() + File.separator;
        return personcenterView;
    }
    public void init(View v){
        tvorgain=(TextView)v.findViewById(R.id.tvorgain);
        tvmingzix=(TextView)v.findViewById(R.id.tvmingzix);
        rlson=(ImageView)v.findViewById(R.id.rlson);
        tvmingzi=(TextView)v.findViewById(R.id.tvmingzi);
        LinearLayout touliout=(LinearLayout)v.findViewById(R.id.touliout);
        touliout.setOnClickListener(this);
        ll_help=(LinearLayout)v.findViewById(R.id.ll_help);
        ll_helps=(LinearLayout)v.findViewById(R.id.ll_helps);
        ll_help.setOnClickListener(this);
        ll_helps.setOnClickListener(this);
       ll_setting=(LinearLayout)v.findViewById(R.id.ll_setting);
       ll_settings=(LinearLayout)v.findViewById(R.id.ll_settings);
        ll_setting.setOnClickListener(this);
        ll_settings.setOnClickListener(this);
        llshezhi=(LinearLayout) v.findViewById(R.id.llshezhi);
        llwoshi=(LinearLayout) v.findViewById(R.id.llwoshi);
        llwoshi.setOnClickListener(this);
        if (DianApplication.sharedPreferences.getStringValue(Constant.ROLE).equals("ROLE_CLASSROOMTEACHE")){
            llwoshi.setVisibility(View.VISIBLE);
            ll_helps.setVisibility(View.GONE);
            ll_help.setVisibility(View.VISIBLE);
            ll_settings.setVisibility(View.GONE);
            ll_setting.setVisibility(View.VISIBLE);
            llshezhi.setVisibility(View.GONE);
        }else{
            llwoshi.setVisibility(View.GONE);
            ll_helps.setVisibility(View.VISIBLE);
            ll_help.setVisibility(View.GONE);
            ll_settings.setVisibility(View.VISIBLE);
            ll_setting.setVisibility(View.GONE);
            llshezhi.setVisibility(View.VISIBLE);
        }
        personci=(CircleImageView)v.findViewById(R.id.personci);
        ll_setting.setOnClickListener(this);
        personci.setOnClickListener(this);
       LinearLayout ll_chuqin=(LinearLayout)v.findViewById(R.id.ll_chuqin);
        ll_chuqin.setOnClickListener(this);
       LinearLayout ll_pinfeng=(LinearLayout)v.findViewById(R.id.ll_pinfeng);
        ll_pinfeng.setOnClickListener(this);
      LinearLayout ll_rule=(LinearLayout)v.findViewById(R.id.ll_rule);
        ll_rule.setOnClickListener(this);
      LinearLayout ll_daochu=(LinearLayout) v.findViewById(R.id.ll_daochu);
        ll_daochu.setOnClickListener(this);
    }
    public void initdatas(){
        if(DianTool.isConnectionNetWork(getActivity())) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/user/info")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (!TextUtils.isEmpty(s)) {
                        ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                        tvmingzi.setText(resultInfo.name);
                        tvmingzix.setText(resultInfo.personId);
                        tvorgain.setText(resultInfo.organName);
                        if (!TextUtils.isEmpty(resultInfo.avatar)) {
                            if (!resultInfo.avatar.equals("null")) {
                                Glide.with(getActivity()).load(resultInfo.avatar).into(personci);
                                DianApplication.sharedPreferences.saveString("phone", resultInfo.avatar);
                            }
                        }
                        DianApplication.sharedPreferences.saveString("organName", resultInfo.organName);
                        DianApplication.sharedPreferences.saveString("personId", resultInfo.personId);
                        DianApplication.sharedPreferences.saveString("mingName", resultInfo.name);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.response(response,getActivity());
                }
            });
        }else{
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }
    /**
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void gallery() {
        if (DianTool.getsdkbanbe()>22){
            chenggong=1;
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
                return;
            }
        }
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意使用write
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
            }else{
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储");
            }
        }else if (requestCode==1001){
            if (permissions[0].equals(Manifest.permission.CAMERA)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (DianTool.getsdkbanbe()<24) {
                    if (DianTool.hasSdcard()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
                    }
                }else{
                    openCamera();
                }
            }else{
                quanxian(Manifest.permission.CAMERA,"摄像头");
            }
        }
    }
    void quanxian(String string,String s){
        if (zizengs==0) {
            if (!shouldShowRequestPermissionRationale(string)) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("该应用需要赋予访问" + s + "的权限，不开启将无法正常工作！")
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
    /**
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void photo() {
        if (DianTool.getsdkbanbe()>22){
            chenggong=1;
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
            int hasWriteContactsPermissions = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED||
                    hasWriteContactsPermissions!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1001);
                return;
            }
        }
        if (DianTool.getsdkbanbe()<24) {
            if (DianTool.hasSdcard()) {
                chenggong = 1;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
            }
        }else{
            openCamera();
        }
    }
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
            imageUri = FileProvider.getUriForFile(getActivity(), "com.dingli.diandiaan.myprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
    }
    /**
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constant.PHOTO_REQUEST_GALLERY && data != null){
            crop(data.getData(), Uri.fromFile(tempFile));
        }else if(requestCode == Constant.PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK ){
            crop(Uri.fromFile(tempFile), Uri.fromFile(tempFile));
        }else if(requestCode == Constant.PHOTO_REQUEST_CUT && data != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                personci.setImageBitmap(bitmap);
//                File file=new File(imageUri.getPath());
                uploadFile(file);
            }else {
                Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(tempFile));//decode bitmap
//            Bitmap bitmap2 = BlurUtils.decodeBitmapToBlur(getActivity(), bitmap, 20);
//            rlson.setImageBitmap(bitmap2);
                personci.setImageBitmap(bitmap);
                uploadFile(tempFile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    void uploadFile(final File file){
        List<File> list=new ArrayList<>();
        list.add(file);
        HttpParams params=new HttpParams();
        params.putFileParams("file", list);
        if (DianTool.isConnectionNetWork(getActivity())) {
            OkGo.post(HostAdress.getRequest("/api/phone/v1/user/avatar")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.showTextToast(getActivity(), "上传成功");
                    initdatas();
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                }
            });
        }else{
            DianTool.showTextToast(getActivity(),"请检查网络");
        }
    }
    private void crop(Uri uri,Uri cutImgUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            file = new FileStorage().createCropFile();
            Uri outputUri = Uri.fromFile(file);//缩略图保存地址
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }else {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cutImgUri);
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 5);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 500);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, Constant.PHOTO_REQUEST_CUT);
    }
    /**
     * @return
     */
    private static String getPath(String path){
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        return f.getAbsolutePath();
    }
    /**
     * @param path
     * @return
     */
    private File getFile(String path){
        File f=new File(path);
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
    /**
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_chuqin:
                Intent intent = new Intent(getActivity(), CqlActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.ll_pinfeng:
                Intent intent2 = new Intent(getActivity(), PingjiaoActivity.class);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.touliout:
                Intent intentt=new Intent(getActivity(), EntrtyActivity.class);
                startActivity(intentt);
                getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.ll_setting:
                Intent intents = new Intent(getActivity(), SettingActivity.class);
                startActivity(intents);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.ll_help:
                Intent intent1=new Intent(getActivity(), HelpActivity.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.ll_settings:
                Intent intentst = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentst);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.ll_helps:
                Intent intenthe=new Intent(getActivity(), HelpActivity.class);
                startActivity(intenthe);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.llwoshi:
                Intent intentwo=new Intent(getActivity(), InstructorActivity.class);
                startActivity(intentwo);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.personci:
                new ActionSheetDialog(getActivity())
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        photo=1;
                                        photo();
                                    }
                                })
                        .addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        phone=1;
                                        gallery();
                                    }
                                })
                        .show();
                break;
            case R.id.ll_rule:
                Intent inte=new Intent(getActivity(), RuleActivity.class);
                startActivity(inte);
                getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.ll_daochu:
                Intent intentsd=new Intent(getActivity(), DaochuWebsActivity.class);
                startActivity(intentsd);
                getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (DianTool.getsdkbanbe()>22) {
            if (chenggong != 1) {
                if (photo == 1) {
                    photo = 0;
                    phone = 0;
                    photo();
                }
                if (phone == 1) {
                    photo = 0;
                    phone = 0;
                    gallery();
                }
            }
        }
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue("organName"))){
            if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue("phone"))) {
                Glide.with(getActivity()).load(DianApplication.sharedPreferences.getStringValue("phone")).into(personci);
            }
            tvmingzi.setText(DianApplication.sharedPreferences.getStringValue("mingName"));
            tvorgain.setText(DianApplication.sharedPreferences.getStringValue("organName"));
            tvmingzix.setText(DianApplication.sharedPreferences.getStringValue("personId"));
        }
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            initdatas();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
