package com.dingli.diandiaan.common;

import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/28.
 */

public class ResultInfo {
      public String personId;
      public String access_token;
      public int expires_in;
      public String role;
      public String avatar;
      public String login;
      public int totalCount;
      public int pageCount;
      public int offset;
      public int limit;
      public Data[] data;
      public Result result;
      public List<RollCallList> rollCallList;
      public String token_type;
      public String refresh_token;
      public String type;
//      public int createdDate;
//      public int lastModifiedDate;
      public int id;
      public int organId;
      public int semesterId;
      public String name;
      public List<Course> weekList;
      public Data[] courseList;
      public List<Course> assessList;
      public String organName;
      public String organLogo;
      public String dayOfWeek;


}
