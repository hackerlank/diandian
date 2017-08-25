package com.dingli.diandiaan.login.chuqinlv.studentCql;

import java.util.Comparator;

public class PinyinComparator implements Comparator<StudentModel> {
    public int compare(StudentModel o1, StudentModel o2) {
        if (o1.getCode().equals("@")
                || o2.getCode().equals("#")) {
            return -1;
        } else if (o1.getCode().equals("#")
                || o2.getCode().equals("@")) {
            return 1;
        } else {
            return o1.getCode().compareTo(o2.getCode());
        }
    }

}
