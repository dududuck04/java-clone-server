package com.mzc.stc.caa.carrot.sample.model;

import java.util.List;

/**
 * 검색한 사원 정보를 담기 위한 용도로 사용하는 모델 클래스.
 * 
 * @author 김종현
 */
public class EmployeeSearchResult {

	/**
	 * 사원 목록
	 */
	private List<Employee> list;
	/**
	 * 검색된 사원의 전체 수
	 */
	private Integer count;
	
	/**
	 * 검색된 사원 목록 반환
	 * 
	 * @return 사원 목록
	 */
	public List<Employee> getList() {
		return list;
	}
	
	/**
	 * 검색된 사원 목록 저장
	 * 
	 * @param list 사원 목록
	 */
	public void setList(List<Employee> list) {
		this.list = list;
	}

	/**
	 * 검색된 사원 수 반환
	 * 
	 * @return 검색된 사원 수
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * 검색된 사원 수 저장
	 * 
	 * @param count 검색된 사원 수
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
}
