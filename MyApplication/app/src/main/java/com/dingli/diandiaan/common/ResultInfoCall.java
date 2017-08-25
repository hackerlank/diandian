package com.dingli.diandiaan.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/28.
 */

public class ResultInfoCall {
      public int totalCount;
      public int pageCount;
      public int offset;
//      public int limit;
      public List<CourseMing> data;
      public RollCallList[] rollCallList;
      public String dayOfWeek;
      public List<Course> courseList;
      public String openTime;
      public int commitCount;
      public String className;
      public ArrayList<ResultInfoCallOne> classList;
      public String phoneNumber;
      public boolean status;

}
