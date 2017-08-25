package com.dingli.diandiaan.common;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.dingli.diandiaan.R;
import com.lzy.okgo.OkGo;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by Administrator on 2016/3/4.
 */
public class DianApplication extends Application {
    public static DianSharedPreferences sharedPreferences;
    private static DianApplication instance;
     public static UserInfo user;
   public static Data data;
    public static int mNetWorkState;
    private Thread.UncaughtExceptionHandler androidDefaultUEH;
    public static List<Map> alist=new ArrayList<Map>();
    public static Map<Integer,String> map=new HashMap<Integer,String>();
    public static List<Activity> activityList = new ArrayList<>();
    public static Timer timer;
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    public static String de;
    public static String code;
    private Thread.UncaughtExceptionHandler handlers = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("TestApplication  " + "Uncaught exception is: " + ex, "");
            exit();
            androidDefaultUEH.uncaughtException(thread, ex);
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        initYaoData();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        //开机启动
        mPushAgent.onAppStart();
        mNetWorkState=NetUtil.getNetworkState(this);
        UmengMessageHandler messageHandler = new UmengMessageHandler()
        {
            @Override
            public void dealWithCustomMessage(final Context context,final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable(){

                    @Override
                    public void run() {
                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                    }
                });
            }
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id){
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }
            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                super.dealWithCustomAction(context, uMessage);
            }
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                DianApplication.sharedPreferences.saveString(Constant.KEY,"infor");
                super.launchApp(context, uMessage);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        timer = new Timer();
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handlers);
        OkGo.init(this);
        try {
            OkGo.getInstance()
                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo")
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS);   //全局的写入超时时间
//                    .setCacheMode(CacheMode.NO_CACHE)
//                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
             DianApplication.user.dev=s;
            }
            @Override
            public void onFailure(String s, String s1) {
            }
        });
        code=getVersionCode(this);
    }
    private void initYaoData() {
            user = new UserInfo();
        data=new Data();
        sharedPreferences = new DianSharedPreferences();
    }
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
        MobclickAgent.onKillProcess(this);
    }
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void remove(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }
    public static  DianApplication getInstance(){
      return instance;
  }
    public String getVersionCode(Context context) {
        return getPackageInfo(context).versionName;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 因为API数超过了64K，用的MultiDex库,application中没有重写attachBaseContext方法。导致找不到类库的方法；
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
