package com.dingli.diandiaan.login.chuqinlv.studentCql;

import java.io.Serializable;

/**
 * 城市实体类
 * 
 * @author id 城市id
 * @author name 城市名
 * @author code 城市编码

 */
public class StudentModel implements Serializable {
	/**
			* allCount : 2
			* type1Count : 0
			* type2Count : 1
			* type3Count : 0
			* type4Count : 0
			* type5Count : 1
			* studentId : 29602
			* studentName : 吴晓梅
	*/

	private int allCount;
	private int type1Count;
	private int type2Count;
	private int type3Count;
	private int type4Count;
	private int type5Count;
	private int studentId;
	private String studentName;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public void setType1Count(int type1Count) {
		this.type1Count = type1Count;
	}

	public void setType2Count(int type2Count) {
		this.type2Count = type2Count;
	}

	public void setType3Count(int type3Count) {
		this.type3Count = type3Count;
	}

	public void setType4Count(int type4Count) {
		this.type4Count = type4Count;
	}

	public void setType5Count(int type5Count) {
		this.type5Count = type5Count;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getAllCount() {
		return allCount;
	}

	public int getType1Count() {
		return type1Count;
	}

	public int getType2Count() {
		return type2Count;
	}

	public int getType3Count() {
		return type3Count;
	}

	public int getType4Count() {
		return type4Count;
	}

	public int getType5Count() {
		return type5Count;
	}

	public int getStudentId() {
		return studentId;
	}

	public String getStudentName() {
		return studentName;
	}
}
