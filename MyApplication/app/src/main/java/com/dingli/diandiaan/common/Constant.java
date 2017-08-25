package com.dingli.diandiaan.common;

import com.dingli.diandiaan.bean.BaseHttpURL;

/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class Constant {
    public static final String COURSE_NAME = "cName";
    public static final String USER_ACCOUNT = "USER_ACCOUNT";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String DAOJISHIEND="DAOJISHIEND";
    public static final String DATA_TOKEN = "DATA_TOKENS";
    public static final String USER_TOKEN="TOKEN_TYPE";
    public static final String KE_ID="KE_ID";
    public static final String CODE="CODE";
    public static final String COURSEID="COURSEID";
    public static final String FANGSHI="FANGSHI";
    public static final String URL="URL";
    public static final String YESHU="YESHU";
    public static final String SHIJIAN="SHIJIAN";
    public static final String PANDUAN="PANDUAN";
    public static final int REFRESH = 1;
    public static final int LOAD_MORE = 2;
    public static final String SERVICEE="SERVICE";
    public static final String SECOND="SECOND";
    public static final String SHUZI="SHUZI";
    public static final String FINISHDATE="FINISHDATE";
    public  static final String APPLICATION_JSON = "application/json;charset=utf-8";
    public  static final String APPLICATION_FORMURL = "application/x-www-form-urlencoded";
    public static final String SHAREDPREFERENCES_NAME = "my_pref";
    public static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    public static final int PHOTO_REQUEST_CAMERA = 1;
    public static final int PHOTO_REQUEST_GALLERY = 2;
    public static final int PHOTO_REQUEST_CUT = 3;
    public static final String SPLITONE="SPLITONE";
    public static final String SPLITTWO="SPLITTWO";
    public static final String SPLITTHREE="SPLITTHREE";
    public static final  String SPLITFOUR="SPLITFOUR";
    public static final String SPLITFIVE="SPLITFIVE";
    public static final String SUIJISHU="SUIJISHU";
    public static final String KEY="KEY";
    public static final String BINGDING="BINGDING";
    public static final String LATE_TIME="LATE_TIME";
    public static final String ROLLCALLTYPEORIGIN="rollCallTypeOrigin";
    public  static final String ISSHOOL="ISSHOOL";
    public static final String RUKOU="RUKOU";
    public static final String REUSER="REUSER";
    public static final String XIANSHI="XIANSHI";
    public static final String REFRESHED="REFRESH";
    public static final String ORGAINER="ORGAINER";
    public static final String ROLE="ROLE";
    public static final String ROLLCALL="ROLLCALL";
    public static final String TYPE="TYPE";
    public static final String SUISIGN="SUISIGN";
    public static final String INFO="INFO";
    //伪生产
//    public static final String webwarn="http://61.143.60.84:64038";
    //测试
//    public static final String webwarn="http://61.143.60.84:64030";
    //现网
//    public static final String webwarn="http://dd.aizhixin.com";
    //现网
//    public static final String shouye="http://dd.aizhixin.com/mobileui/";
    //测试
//    public static final String shouye="http://61.143.60.84:64030/mobileui/";
    //伪生产
//    public static final String shouye="http://61.143.60.84:64038/mobileui/";
    //现网
//    public static final String webhelp="http://dd.aizhixin.com";
//    测试
//    public static final String webhelp="http://61.143.60.84:64030";
    //伪生产
//    public static final String webhelp="http://61.143.60.84:64038";
    //考勤报表
    //测试
//    public static final String daochu="http://61.143.60.84:64030/mobileui/attendreport";
    //伪生产
//    public static final String daochu="http://61.143.60.84:64038/mobileui/attendreport";
    //现网
//    public static final String daochu="http://dd.aizhixin.com/mobileui/attendreport";
    //测试
//    public static final String webwenzhang="http://61.143.60.84:64030";
    //伪生产
//    public static final String webwenzhang="http://61.143.60.84:64038";
//    现网
//    public static final String webwenzhang="http://dd.aizhixin.com";
    //慧眼
    //测试
//    public static final String hy="http://172.16.23.32:9008";
    //伪生产
//    public static final String hy="http://172.16.23.31:9008";
    //生产
//    public static final String hy="http://hy.aizhixin.com";
    public static final String webwarn= BaseHttpURL.QUESTION_LOAD_BASEURL;
    public static final String webhelp= BaseHttpURL.WEBHELP_LOAD_BASEURL;
    public static final String daochu=BaseHttpURL.WEBKAOSHI_LOAD_BASEURL;
    public static final String shouye=BaseHttpURL.LOST_LOAD_BASEURL;
    public static final String webwenzhang=BaseHttpURL.WEB_WENZHANG_LOAD_BASEURL;
    public static final String hy=BaseHttpURL.WEB_HY_LOAD_BASEURL;
}
