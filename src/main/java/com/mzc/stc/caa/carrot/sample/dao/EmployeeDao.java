package com.mzc.stc.caa.carrot.sample.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.sample.model.Employee;

/**
 * 사원 데이터 처리를 위한 DAO.
 * 
 * @author 김종현
 */
@Mapper
public interface EmployeeDao {

	/**
	 * 조건에 맞는 사원을 검색한다.
	 * <p>
	 * 검색 결과에서 최대 1,000 명의 사원 정보만 반환한다.
	 * 
	 * @param searchCondition 검색할 사원 정보
	 * @return 검색된 사원 목록
	 */
	List<Employee> selectEmployeeList(Employee searchCondition);

	/**
	 * 검색 조건에 맞는 사원의 전체 수
	 * 
	 * @param searchCondition 검색할 사원 정보
	 * @return 검색된 사원 수
	 */
	Integer selectEmployeeCount(Employee searchCondition);
	
	/**
	 * 선택된 사원에 대한 정보를 조회한다. (단건)
	 * 
	 * @param employeeNo 조회할 사원 번호
	 * @return 조회된 사원의 정보. 조회가 안되면 null을 반환.
	 */
	Employee getEmployee(Integer employeeNo);
	
	/**
	 * 새로운 사원을 저장한다.
	 * 
	 * @param employee 새로 추가할 사원 정보
	 * @return 저장이 완료되면, 1을 반환
	 */
	Integer createEmployee(Employee employee);
	
	/**
	 * 사원 정보를 업데이트한다.
	 * 
	 * @param employee 업데이트할 사원 정보
	 * @return 업데이트가 완료되면, 1을 반환
	 */
	Integer updateEmployee(Employee employee);
	
	/**
	 * 사원 정보를 삭제한다.
	 * 
	 * @param employeeNumber 삭제할 사원 번호
	 * @retur 삭제가 완료되면, 1을 반환
	 */
	Integer deleteEmployee(Integer employeeNumber);
}
