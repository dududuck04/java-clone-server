package com.mzc.stc.caa.carrot.user.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.user.model.JwtInfoDto;


/**
 * 모든 사원 목록을 조회한다.
 * 
 * @param authCode
 * @param userReq
 * 
 * @return 사원 목록
 */
@Mapper
public interface JwtDao {

	/**
	 * jwt 토큰을 DB에 저장
	 * 
	 * @param JwtDto jwtDto ( jwt , userIdx )
	 * @return 사원 목록
	 */
	boolean saveJwt(HashMap<String, Object> result);
	
	boolean checkJwt(int userIdx);

	boolean patchLoginStatus(int userIdx);
	
	boolean updateJwt(HashMap<String, Object> result);
	
	boolean checkLoginByPhoneNumber(String phoneNumber);
	
	boolean logout(int userIdx);
	
	int checkLoginStatus(int userIdx);
	
}
