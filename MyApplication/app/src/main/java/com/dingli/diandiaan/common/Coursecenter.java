package com.dingli.diandiaan.common;

import java.io.Serializable;
import java.util.List;

public class Coursecenter implements Serializable{
	public int id;
	public String dayOfWeek;
	public String classRoom;
	public String courseName;
	public String classBeginTime;
	public String className;
	public String classEndTime;
	public String teach_time;
	public String weekName;
	public String whichLesson;
	public String lessonOrderNum;
	public String teacher;
	public int rollCallPercent;
	public int assessPeopelNum;
	public int totalPeopelNum;
	public int studentCount;
	public int classId;
	public List<Coursecenter> classInfor;
	public double avgScore;
	public int scheduleId;
	public int count;
	public List<ResultOne> data;
}
