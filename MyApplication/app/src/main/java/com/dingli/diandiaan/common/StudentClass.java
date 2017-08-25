package com.dingli.diandiaan.common;

import java.util.List;

/**
 * Created by admin on 2016/8/18.
 */
public class StudentClass {
    /**
     * data : [{"score":3,"content":"凤凰健康","assessTime":"2016-08-18 11:42:08"},{"score":1,"content":"凭一分","assessTime":"2016-08-18 11:40:04"}]
     * classStudentCount : 3
     */

    private int classStudentCount;
    private List<DataEntity> data;

    public void setClassStudentCount(int classStudentCount) {
        this.classStudentCount = classStudentCount;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getClassStudentCount() {
        return classStudentCount;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * score : 3
         * content : 凤凰健康
         * assessTime : 2016-08-18 11:42:08
         */

        private int score;
        private String content;
        private String assessTime;

        public void setScore(int score) {
            this.score = score;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setAssessTime(String assessTime) {
            this.assessTime = assessTime;
        }

        public int getScore() {
            return score;
        }

        public String getContent() {
            return content;
        }

        public String getAssessTime() {
            return assessTime;
        }
    }

}
