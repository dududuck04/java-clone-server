package com.mzc.stc.caa.carrot.sample.model;

import java.util.Date;


/**
 * 사원 정보
 * 
 * @author 김종현
 *
 */
public class Employee {
	
	/**
	 * 사원 번호
	 */
	private Integer employeeNumber;
	/**
	 * 생일
	 */
	private Date birthDay;
	/**
	 * 이름
	 */
	private String firstName;
	/**
	 * 성
	 */
	private String lastName;
	/**
	 * 성별
	 */
	private String gender;
	/**
	 * 고용일
	 */
	private Date hireDate;

	/**
	 * @return the employeeNo
	 */
	public Integer getEmployeeNumber() {
		return employeeNumber;
	}
	/**
	 * @param employeeNo the employeeNo to set
	 */
	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	/**
	 * @return the birthDay
	 */
	public Date getBirthDay() {
		return birthDay;
	}
	/**
	 * @param birthDay the birthDay to set
	 */
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the hireDate
	 */
	public Date getHireDate() {
		return hireDate;
	}
	/**
	 * @param hireDate the hireDate to set
	 */
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

}
