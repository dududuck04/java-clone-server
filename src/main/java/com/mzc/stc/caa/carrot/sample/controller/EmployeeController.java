package com.mzc.stc.caa.carrot.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mzc.stc.caa.carrot.sample.model.Employee;
import com.mzc.stc.caa.carrot.sample.model.EmployeeSearchResult;
import com.mzc.stc.caa.carrot.sample.service.EmployeeService;

/**
 * 사원 정보 처리를 위한 Controller
 * 
 * @author 김종현
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	/**
	 * 사용 정보 처리를 위한 서비스 클래스.
	 */
	EmployeeService employeeService;
	
	/**
	 * 생성자
	 * 
	 * @param employeeService Employee Service
	 */
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	 
	/**
	 * [GET] 전체 사원 목록을 조회한다.
	 * 
	 * @return 검색된 사원 정보
	 */
	@GetMapping("/search")
	public EmployeeSearchResult searchEmployee(@ModelAttribute Employee searchCondition) {
		return this.employeeService.selectEmployeeList(searchCondition);
	}

	/**
	 * [POST] 사원 정보를 추가한다.
	 * 
	 * @param employee 추가할 사원 정보
	 */
	@PostMapping
	public void createEmployee(@RequestBody Employee employee) {
		this.employeeService.createEmployee(employee);
	}
	
}
