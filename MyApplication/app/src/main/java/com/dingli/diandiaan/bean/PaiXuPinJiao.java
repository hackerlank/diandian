package com.dingli.diandiaan.bean;

import com.dingli.diandiaan.common.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/5/9.
 */
public class PaiXuPinJiao {

    public static List getpaixu(List<Course> list){
        List<Course> list1=new ArrayList<>();
        for(int i=list.size()-1;i>=0;i--){
            list1.add(list.get(i));
        }
        return list1;
    }
}
