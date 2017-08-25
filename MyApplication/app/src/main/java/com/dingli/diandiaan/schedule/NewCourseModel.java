package com.dingli.diandiaan.schedule;

import android.text.TextUtils;

/**
 * Created by dong on 2016/3/15.
 */
public class NewCourseModel {
    private int id;
    private String dayOfWeek;
    private String classRoom;
    private String courseName;
    private String classBeginTime;
    private String classEndTime;
    private String teach_time;
    private String weekName;
    private String whichLesson;
    private int lessonOrderNum;
    private String teacher;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassBeginTime() {
        return classBeginTime;
    }

    public void setClassBeginTime(String classBeginTime) {
        this.classBeginTime = classBeginTime;
    }

    public String getClassEndTime() {
        return classEndTime;
    }

    public void setClassEndTime(String classEndTime) {
        this.classEndTime = classEndTime;
    }

    public String getTeach_time() {
        return teach_time;
    }

    public void setTeach_time(String teach_time) {
        this.teach_time = teach_time;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getWhichLesson() {
        return whichLesson;
    }

    public void setWhichLesson(String whichLesson) {
        this.whichLesson = whichLesson;
    }

    public int getLessonOrderNum() {
        return lessonOrderNum;
    }

    public void setLessonOrderNum(int lessonOrderNum) {
        this.lessonOrderNum = lessonOrderNum;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "NewCourseModel{" +
                "id=" + id +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", courseName='" + courseName + '\'' +
                ", classBeginTime='" + classBeginTime + '\'' +
                ", classEndTime='" + classEndTime + '\'' +
                ", teach_time='" + teach_time + '\'' +
                ", weekName='" + weekName + '\'' +
                ", whichLesson='" + whichLesson + '\'' +
                ", lessonOrderNum=" + lessonOrderNum +
                ", teacher='" + teacher + '\'' +
                '}';
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        if (TextUtils.isEmpty(courseName)) return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return courseName.equals(((NewCourseModel) o).courseName);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
