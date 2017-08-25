package com.dingli.diandiaan.schedule;



import android.util.SparseArray;


import com.dingli.diandiaan.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/4/13.
 */
public class ScheduleDrawableSelector {
    private final static int[] background = new int[]{R.drawable.course_colorone, R.drawable.course_colortwo,
            R.drawable.course_colorthree, R.drawable.course_colorfour,
            R.drawable.course_colorfive, R.drawable.course_colorsix, R.drawable.course_colorseven
            };
    private static final SparseArray<Integer> drawableCollection = new SparseArray(10);
//
//    public static int get(int id) {
//        int size = drawableCollection.size();
//        if (drawableCollection.get(id) == null) {
//            drawableCollection.put(id, background[size-1]);
//            return background[size-1];
//        }
//        return background[size - 1];
//    }
//    public static void clearSelector(){
//        drawableCollection.clear();
//    }
private static final Map<String, Integer> selectors = new HashMap<>();
    public static int get(String tag) {
        int size = selectors.size();
        if (!selectors.containsKey(tag)) {
            selectors.put(tag, background[size % 7]);
            return background[size % 7];
        }
        return selectors.get(tag);
    }
    public static void clearSelectors() {
        selectors.clear();
    }

}
