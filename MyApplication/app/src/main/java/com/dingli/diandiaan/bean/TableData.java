package com.dingli.diandiaan.bean;

import java.util.List;

/**
 * Created by admin on 2016/4/27.
 */
public class TableData {


    private String dayOfWeek;
    private List<CourseListEntity> courseList;

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setCourseList(List<CourseListEntity> courseList) {
        this.courseList = courseList;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public List<CourseListEntity> getCourseList() {
        return courseList;
    }

    public static class CourseListEntity {
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
        private String rollcallType;
        private String periodType;

        public void setId(int id) {
            this.id = id;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public void setClassRoom(String classRoom) {
            this.classRoom = classRoom;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public void setClassBeginTime(String classBeginTime) {
            this.classBeginTime = classBeginTime;
        }

        public void setClassEndTime(String classEndTime) {
            this.classEndTime = classEndTime;
        }

        public void setTeach_time(String teach_time) {
            this.teach_time = teach_time;
        }

        public void setWeekName(String weekName) {
            this.weekName = weekName;
        }

        public void setWhichLesson(String whichLesson) {
            this.whichLesson = whichLesson;
        }

        public void setLessonOrderNum(int lessonOrderNum) {
            this.lessonOrderNum = lessonOrderNum;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public void setRollcallType(String rollcallType) {
            this.rollcallType = rollcallType;
        }

        public void setPeriodType(String periodType) {
            this.periodType = periodType;
        }

        public int getId() {
            return id;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public String getClassRoom() {
            return classRoom;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getClassBeginTime() {
            return classBeginTime;
        }

        public String getClassEndTime() {
            return classEndTime;
        }

        public String getTeach_time() {
            return teach_time;
        }

        public String getWeekName() {
            return weekName;
        }

        public String getWhichLesson() {
            return whichLesson;
        }

        public int getLessonOrderNum() {
            return lessonOrderNum;
        }

        public String getTeacher() {
            return teacher;
        }

        public String getRollcallType() {
            return rollcallType;
        }

        public String getPeriodType() {
            return periodType;
        }
    }
}