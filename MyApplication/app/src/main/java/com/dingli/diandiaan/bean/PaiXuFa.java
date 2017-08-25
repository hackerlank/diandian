package com.dingli.diandiaan.bean;

import com.dingli.diandiaan.common.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dingliyuangong on 2016/5/9.
 */
public class PaiXuFa {

    public static List getpaixu(List<Course> list){
        Map<Integer ,Course> map=new HashMap<Integer, Course>();
        ArrayList<Course> arrd=new ArrayList<>();
        int[]lsd=new int[list.size()];
        for (int i=0;i<list.size();i++){
            int less=list.get(i).lessonOrderNum;
            map.put(less,list.get(i));
            lsd[i]=less;
        }
        for (int i = 0; i < lsd.length; i++) {
            for (int j = i+1; j < lsd.length; j++) {
                if (lsd[i]>lsd[j]) {
                    int temp=lsd[i];
                    lsd[i] = lsd[j];
                    lsd[j]=temp;
                }
            }
        }
        for(int a=0;a<lsd.length;a++){
            arrd.add(map.get(lsd[a]));
        }
        return arrd;
    }
}
