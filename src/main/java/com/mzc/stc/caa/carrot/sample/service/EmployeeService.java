package com.mzc.stc.caa.carrot.sample.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mzc.stc.caa.carrot.sample.dao.EmployeeDao;
import com.mzc.stc.caa.carrot.sample.model.Employee;
import com.mzc.stc.caa.carrot.sample.model.EmployeeSearchResult;

/**
 * 사원 정보 처리를 위한 서비스 클래스.
 * 
 * @author 김종현
 */
@Service
public class EmployeeService {

	/**
	 * 사원 데이터 처리를 위한 DAO.
	 */
	private EmployeeDao employeeDao;
	
	/**
	 * 생성자
	 * 
	 * @param employeeDao 사원 데이터 처리 DAO
	 */
	public EmployeeService(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
	/**
	 * 조건에 맞는 사원을 검색한다.
	 * 
	 * @param searchCondition 검색할 사원 조건
	 * @return 검색된 사원 정보
	 */
	public EmployeeSearchResult selectEmployeeList(Employee searchCondition) {
		// TODO - 페이징 처리할 수 있도록 변경해야 함
		EmployeeSearchResult result = new EmployeeSearchResult();

		result.setList(this.employeeDao.selectEmployeeList(searchCondition));
		result.setCount(this.employeeDao.selectEmployeeCount(searchCondition));
		
		return result; 
	}
	
	/**
	 * 사원 정보를 DB에서 조회한다.
	 * 
	 * @param employeeNo 조회할 사원 번호
	 * @return 조회된 사원 정보. 없으면 Null.
	 */
	public Employee getEmployee(Integer employeeNo) {
		return this.employeeDao.getEmployee(employeeNo);
	}
	
	/**
	 * 사원 정보를 DB에 저장한다.
	 * 
	 * @param employee 저장할 사원 정보
	 */
	public void createEmployee(Employee employee) {
		this.employeeDao.createEmployee(employee);
	}
}
