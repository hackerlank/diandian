package com.dingli.diandiaan.lostproperty;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.FileStorage;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.common.X5WebView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/12/23.
 */
public class WebViewLostActivity extends Activity implements View.OnClickListener, InJavaScript.UpPhone {
    X5WebView webView;
    ProgressBar bar;
    String url;
    String toutiao;
    String imageFilePath;
    File temp;
    int zizengs;
    int call, resume;
    String phones;
    List<String> arrl;
    List<String> listkey;
    List<File> filelist;
    JSONObject json;
    LinearLayout llout;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    webView.loadUrl("javascript:callAppNativeWithPhotosUrl('"+DianApplication.user.key+"')");
                    DianApplication.user.alist=null;
                    if (listkey.size()!=0){
                        listkey.clear();
                    }
                    break;
                case 2:
                    webView.loadUrl("javascript:noticeMessage('"+json.toString()+"')");
                    DianTool.showTextToast(WebViewLostActivity.this,"复制完成，快去粘贴吧");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Uri imageUri;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_lost);
        initview();
        webView.loadUrl(Constant.shouye+"lostproperty");
        webView.addJavascriptInterface(new InJavaScript(this, this,webView), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    bar.setVisibility(View.GONE);
                    llout.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
    }
    void initview() {
        listkey=new ArrayList<>();
        arrl=new ArrayList<>();
        filelist=new ArrayList<>();
        webView = (X5WebView) findViewById(R.id.webViewd);
        bar = (ProgressBar) findViewById(R.id.myProgressBard);
        llout=(LinearLayout) findViewById(R.id.llout);
        TextView tvtoutiaos = (TextView) findViewById(R.id.tvtoutiaos);
        tvtoutiaos.setText("失物招领");
        ImageView remenbacks = (ImageView) findViewById(R.id.remenbackd);
        remenbacks.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        OkGo.getInstance().cancelTag(this);
        if (DianApplication.user.alist!=null) {
            DianApplication.user.alist.clear();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackd:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }

    @Override
    public void upphone(String name, String str) {
        switch (name) {
            case "photograph":
                photo();
                break;
            case "album":
                gallery();
                break;
            case "phone":
                call = 1;
                phones = str;
                callphone(str);
                break;
            case "copy":
                android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(str);
                json=new JSONObject();
                try {
                    json.put("success",true);
                    json.put("type","");
                    json.put("param1","");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(2);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    void photo() {
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasWriteContactsPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED ||
                    hasWriteContactsPermissions != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1001);
                return;
            }
        }
        photoGraph();
    }

    void photoGraph() {
        if (DianTool.getsdkbanbe()<24) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            imageFilePath = Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
            temp = new File(imageFilePath);
            Uri imageFileUri = Uri.fromFile(temp);// 获取文件的Uri
            Intent it = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
            it.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(it, 102);
        }else{
            openCamera();
        }
    }
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
            imageUri = FileProvider.getUriForFile(this, "com.dingli.diandiaan.myprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                Intent intent = new Intent(this,
                        ImgFileListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            } else {
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储");
            }
        } else if (requestCode == 1001) {
            if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                photoGraph();
            } else {
                quanxian(Manifest.permission.CAMERA, "摄像头");
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
            } else {
                quanxian(Manifest.permission.CALL_PHONE, "打电话");
            }
        }
    }
    void quanxian(String string, String s) {
        if (zizengs == 0) {
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
                zizengs = 1;
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 102) {
            DianApplication.user.key=null;
            if (arrl.size()!=0) {
                arrl.clear();
            }
            if (listkey.size()!=0) {
                listkey.clear();
            }
            arrl.add(imageFilePath);
            upqiniu();
        }else if (requestCode == Constant.PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            crop();
        }else if(requestCode == Constant.PHOTO_REQUEST_CUT && data != null){
            DianApplication.user.key=null;
            if (arrl.size()!=0) {
                arrl.clear();
            }
            if (listkey.size()!=0) {
                listkey.clear();
            }
            arrl.add(file.getPath());
            upqiniu();
        }
    }
    void crop(){
        Intent intent = new Intent("com.android.camera.action.CROP");
            file = new FileStorage().createCropFile();
            Uri outputUri = Uri.fromFile(file);//缩略图保存地址
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
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
    void upqiniu(){
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.showDialog(WebViewLostActivity.this,"");
            OkGo.get(HostAdress.qiniu).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DianTool.dissMyDialog();
                        }
                    },3000);
                    upphones(s);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    void upphones(String s) {
        Result result = JSON.parseObject(s, Result.class);
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
//                .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        for (int i=0;i<arrl.size();i++) {
            File file = getFile(arrl.get(i));
            Bitmap bp = DianTool.getDiskBitmap(arrl.get(i));
            DianTool.saveBitmapFile(bp, file);
            uploadManager.put(file, null, result.token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            DianTool.dissMyDialog();
                            if (info.isOK()) {
                                try {
                                    String keys = res.getString("key");
                                    listkey.add("http://7xpscc.com1.z0.glb.clouddn.com/" + keys);
                                    JSONArray jsonArray=new JSONArray();
                                    jsonArray.add(listkey);
                                    DianApplication.user.key=jsonArray.toJSONString();
                                    DianApplication.user.key=DianApplication.user.key.substring(1,DianApplication.user.key.lastIndexOf("]"));
                                    handler.sendEmptyMessage(1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                DianTool.showTextToast(WebViewLostActivity.this,"请重新上传");
                            }
                        }
                    }, null);
        }
    }

    private File getFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void callphone(String phone) {
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = this.checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        1004);
                return;
            }
        }
        resume = 1;
        baoda(phone);
    }

    void baoda(String phone) {
        if (!phone.equals("null")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void gallery(){
        if (DianTool.getsdkbanbe()>22){
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
                return;
            }
        }
        Intent intent = new Intent(this,
                ImgFileListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (DianApplication.user.alist != null) {
            if (arrl.size()!=0){
                arrl.clear();
            }
            arrl.addAll(DianApplication.user.alist);
            upqiniu();
        }
    }
}
