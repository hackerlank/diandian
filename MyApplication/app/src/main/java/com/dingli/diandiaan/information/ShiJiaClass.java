package com.dingli.diandiaan.information;


import com.dingli.diandiaan.common.QingJiaSty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/8/16.
 */
public class ShiJiaClass {
    public static List<QingJiaSty> get(List<QingJiaSty> list){
        List<QingJiaSty> lis=new ArrayList<>();
        for (int i=list.size()-1;i>=0;i--){
            lis.add(list.get(i));
        }
        return  lis;
    }
}
