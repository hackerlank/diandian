package com.dingli.diandiaan.bean;


import com.dingli.diandiaan.BuildConfig;

public class BaseHttpURL {

    private BaseHttpURL() {
    }
    private static final boolean isLive = BuildConfig.DIANTEACHER_LIVE;
    private static final boolean isFalseLive = BuildConfig.DIANTEACHER_FALSELIVE;
    private static final boolean isDev = BuildConfig.DIANTEACHER_DEV;
    public static final boolean isTest = BuildConfig.DIANTEACHER_TEST;
    /**
     * -----------------passport认证（知新）接口请求地址----------------
     */
    private static final String PASSPORT_BASE_LIVE_URL = "http://api.aizhixin.com/zhixin_api";// 生产环境
    private static final String PASSPORT_BASE_DEV_URL = "http://dledudev.aizhixin.com/zhixin_api";// 开发环境
    private static final String PASSPORT_BASE_TEST_URL = "http://dledutest.aizhixin.com/zhixin_api";// 测试环境
    public static final String PASSPORT_BASE_FALSE_LIVE_URL = "http://dledu.dlztc.com/zhixin_api";//伪生产环境

    public static final String PASSPORT_LOAD_BASEURL = isLive ? PASSPORT_BASE_LIVE_URL : isFalseLive ? PASSPORT_BASE_FALSE_LIVE_URL : isDev ? PASSPORT_BASE_DEV_URL : PASSPORT_BASE_TEST_URL;

    /**
     * -----------------点点基础数据接口请求地址----------------
     */
    public static final String BASE_LIVE_URL="http://api.aizhixin.com/diandian_api";
    public static final String BASE_DEV_URL="http://172.16.23.120:3333/diandian";
    public static final String BASE_FALSE_LIVE_URL="http://dd.dlztc.com/diandian_api";
    public static final String BASE_TEST_URL="http://ddtest.aizhixin.com/diandian_api";

    public static final String LOAD_BASEURL = isLive ? BASE_LIVE_URL : isFalseLive ? BASE_FALSE_LIVE_URL:isDev ? BASE_DEV_URL : BASE_TEST_URL;

    /**
     * -----------------版本更新数据接口请求地址----------------
     */
    public static final String BASE_BANBEN_LIVE_URL="http://dd.aizhixin.com/diandian_api";
    public static final String BASE_BANBEN_DEV_URL="http://172.16.23.120:3333/diandian";
    public static final String BASE_BANBEN_FALSE_LIVE_URL="http://dd.dlztc.com/diandian_api";
    public static final String BASE_BANBEN_TEST_URL="http://ddtest.aizhixin.com/diandian_api";

    public static final String BANBEN_LOAD_BASEURL = isLive ? BASE_BANBEN_LIVE_URL : isFalseLive ? BASE_BANBEN_FALSE_LIVE_URL:isDev ? BASE_BANBEN_DEV_URL : BASE_BANBEN_TEST_URL;

    /**
     * -----------------文章接口请求地址----------------
     */
    public static final String BASE_WENZHANG_LIVE_URL="http://hy.aizhixin.com/ew_api";
    public static final String BASE_WENZHANG_DEV_URL="http://hydev.aizhixin.com/ew_api";
    public static final String BASE_WENZHANG_FALSE_LIVE_URL="http://hy.dlztc.com/ew_api";
    public static final String BASE_WENZHANG_TEST_URL="http://172.16.23.42:8006/ew_api";

    public static final String WENZHANG_LOAD_BASEURL = isLive ? BASE_WENZHANG_LIVE_URL : isFalseLive ? BASE_WENZHANG_FALSE_LIVE_URL:isDev ? BASE_WENZHANG_DEV_URL : BASE_WENZHANG_TEST_URL;
    /**
     * -----------------慧眼基础数据接口请求地址----------------
     */
    public static final String BASE_HY_DEV_URL="http://hy.aizhixindev.com/ew_api";
    public static final String BASE_HY_TEST_URL="http://hy.aizhixintest.com/ew_api";
    public static final String BASE_HY_FALSE_LIVE_URL="http://hy.dlztc.com/ew_api";
    public static final String BASE_HY_LIVE_URL="http://hy.aizhixin.com/ew_api";

    public static final String HY_LOAD_BASEURL = isLive ? BASE_HY_LIVE_URL : isFalseLive ? BASE_HY_FALSE_LIVE_URL: isDev ? BASE_HY_DEV_URL :  BASE_HY_TEST_URL;

    /**
     * -----------------警告提醒H5请求地址----------------
     */
    public static final String BASE_QUESTION_LIVE_URL="http://dd.aizhixin.com";
    public static final String BASE_QUESTION_FALSE_LIVE_URL="http://61.143.60.84:64038";
    public static final String BASE_QUESTION_DEV_URL="http://61.143.60.84:64030";
    public static final String BASE_QUESTION_TEST_URL="http://61.143.60.84:64030";

    public static final String QUESTION_LOAD_BASEURL = isLive ? BASE_QUESTION_LIVE_URL : isFalseLive ? BASE_QUESTION_FALSE_LIVE_URL: isDev ? BASE_QUESTION_DEV_URL :  BASE_QUESTION_TEST_URL;

    /**
     * -----------------帮助H5请求地址----------------
     */
    public static final String BASE_WEBHELP_LIVE_URL="http://dd.aizhixin.com";
    public static final String BASE_WEBHELP_FALSE_LIVE_URL="http://61.143.60.84:64038";
    public static final String BASE_WEBHELP_DEV_URL="http://ddtest.aizhixin.com";
    public static final String BASE_WEBHELP_TEST_URL="http://61.143.60.84:64030";

    public static final String WEBHELP_LOAD_BASEURL = isLive ? BASE_WEBHELP_LIVE_URL : isFalseLive ? BASE_WEBHELP_FALSE_LIVE_URL: isDev ? BASE_WEBHELP_DEV_URL :  BASE_WEBHELP_TEST_URL;

    /**
     * -----------------WEB導出考勤h5接口请求地址----------------
     */
    public static final String BASE_WEBKAOSHI_LIVE_URL="http://dd.aizhixin.com/mobileui/attendreport";
    public static final String BASE_WEBKAOSHI_FALSE_LIVE_URL="http://61.143.60.84:64038/mobileui/attendreport";
    public static final String BASE_WEBKAOSHI_DEV_URL="http://61.143.60.84:64030/mobileui/attendreport";
    public static final String BASE_WEBKAOSHI_TEST_URL="http://61.143.60.84:64030/mobileui/attendreport";

    public static final String WEBKAOSHI_LOAD_BASEURL = isLive ? BASE_WEBKAOSHI_LIVE_URL : isFalseLive ? BASE_WEBKAOSHI_FALSE_LIVE_URL: isDev ? BASE_WEBKAOSHI_DEV_URL :  BASE_WEBKAOSHI_TEST_URL;
    /**
     * -----------------WEB失物招领h5接口请求地址----------------
     */
    public static final String BASE_LOST_LIVE_URL="http://dd.aizhixin.com/mobileui/";
    public static final String BASE_LOST_FALSE_LIVE_URL="http://61.143.60.84:64038/mobileui/";
    public static final String BASE_LOST_DEV_URL="http://61.143.60.84:64030/mobileui/";
    public static final String BASE_LOST_TEST_URL="http://61.143.60.84:64030/mobileui/";

    public static final String LOST_LOAD_BASEURL = isLive ? BASE_LOST_LIVE_URL : isFalseLive ? BASE_LOST_FALSE_LIVE_URL: isDev ? BASE_LOST_DEV_URL :  BASE_LOST_TEST_URL;



    /**
     * -----------------WEB文章h5接口请求地址----------------
     */
    public static final String BASE_WEB_WENZHANG_LIVE_URL="http://dd.aizhixin.com";
    public static final String BASE_WEB_WENZHANG_FALSE_LIVE_URL="http://61.143.60.84:64038";
    public static final String BASE_WEB_WENZHANG_DEV_URL="http://ddtest.aizhixin.com";
    public static final String BASE_WEB_WENZHANG_TEST_URL="http://61.143.60.84:64030";

    public static final String WEB_WENZHANG_LOAD_BASEURL = isLive ? BASE_WEB_WENZHANG_LIVE_URL : isFalseLive ? BASE_WEB_WENZHANG_FALSE_LIVE_URL: isDev ? BASE_WEB_WENZHANG_DEV_URL :  BASE_WEB_WENZHANG_TEST_URL;

    /**
     * -----------------慧眼测评h5请求地址----------------
     */
    public static final String BASE_WEB_HY_LIVE_URL="http://hy.aizhixin.com";
    public static final String BASE_WEB_HY_FALSE_LIVE_URL="http://172.16.23.31:9008";
    public static final String BASE_WEB_HY_DEV_URL="http://hytest.aizhixin.com";
    public static final String BASE_WEB_HY_TEST_URL="http://172.16.23.32:9008";

    public static final String WEB_HY_LOAD_BASEURL = isLive ? BASE_WEB_HY_LIVE_URL : isFalseLive ? BASE_WEB_HY_FALSE_LIVE_URL: isDev ? BASE_WEB_HY_DEV_URL :  BASE_WEB_HY_TEST_URL;
}
