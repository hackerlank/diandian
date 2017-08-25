package com.dingli.diandiaan.common;

import com.dingli.diandiaan.bean.BaseHttpURL;

/**
 * Created by dingliyuangong on 2016/3/11.
 */
public class HostAdress {
    public static final String qiniu="http://io.aizhixin.com/api/v2/service/qiniu?expires=3600&fsizeLimit=1069547520&mimeLimit=image/*&deleteAfterDays=10";
    //测试
//    public static final String HOST = "http://dledutest.aizhixin.com/zhixin_api";
//    public static final String HOSTONE="http://ddtest.aizhixin.com/diandian_api";
//    public static final String HOSTZHENG="http://ddtest.aizhixin.com/diandian_api";
//    public static final String HOSTLIQIANG="http://ddtest.aizhixin.com/diandian_api";
//    public static final String HOSTBANBEN="http://ddtest.aizhixin.com/diandian_api";
//    public static final String HOSTONE="http://172.16.23.122:3333/diandian";
//    public static final String HOSTZHENG="http://172.16.23.122:3333/diandian";
//    public static final String HOSTLIQIANG="http://172.16.23.122:3333/diandian";
//    public static final String HOSTBANBEN="http://172.16.23.122:3333/diandian";
    //开发
//       public static final String HOST = "http://dledutest.aizhixin.com/zhixin_api";
//       public static final String HOSTONE="http://172.16.15.97:8080";
//       public static final String HOSTZHENG="http://172.16.15.97:8080";
//       public static final String HOSTLIQIANG="http://172.16.15.97:8080";
//       public static final String HOSTBANBEN="http://172.16.15.97:8080";
//    生产
//    public static final String HOST = "http://api.aizhixin.com/zhixin_api";
//    public static final String HOSTONE="http://api.aizhixin.com/diandian_api";
//    public static final String HOSTZHENG="http://api.aizhixin.com/diandian_api";
//    public static final String HOSTLIQIANG="http://api.aizhixin.com/diandian_api";
//    public static final String HOSTBANBEN="http://dd.aizhixin.com/diandian_api";
    //伪生产
//    public static final String HOST = "http://dledu.dlztc.com/zhixin_api";
//    public static final String HOSTONE="http://dd.dlztc.com/diandian_api";
//    public static final String HOSTZHENG="http://dd.dlztc.com/diandian_api";
//    public static final String HOSTLIQIANG="http://dd.dlztc.com/diandian_api";
//    public static final String HOSTBANBEN="http://dd.dlztc.com/diandian_api";
    //文章现网
//    public static  final String WENZHANG="http://hy.aizhixin.com/ew_api";
    //本机
//    public static final String HOST = "http://dledutest.aizhixin.com/zhixin_api";
//    public static final String HOSTONE="http://192.168.3.56:8080";
//    public static final String HOSTZHENG="http://192.168.3.56:8080";
//    public static final String HOSTLIQIANG="http://192.168.3.56:8080";
//    public static final String HOSTBANBEN="http://192.168.3.56:8080";
    //文章测试
//    public static  final String WENZHANG="http://172.16.23.42:8006/ew_api";
    //文章伪生产
//    public static  final String WENZHANG="http://hy.dlztc.com/ew_api";
    public static String getWenZhang(String relativePath) {
        return WENZHANG + relativePath;
    }
    public static String getRequestUrl(String relativePath) {
        return HOST+ relativePath;
    }
    public static String getRequest(String rp){
        return  HOSTONE+rp;
    }
    public static String getBanben(String ree){
        return HOSTBANBEN+ree;
    }
    public static String getZheRe(String re){
        return HOSTONE+re;
    }
    public static String getLiQ(String re){
        return HOSTONE+re;
    }

    public static final String HOST = BaseHttpURL.PASSPORT_LOAD_BASEURL;
    public static final String HOSTONE=BaseHttpURL.LOAD_BASEURL;
    public static final String HOSTBANBEN=BaseHttpURL.BANBEN_LOAD_BASEURL;
    public static  final String WENZHANG= BaseHttpURL.WENZHANG_LOAD_BASEURL;

}
