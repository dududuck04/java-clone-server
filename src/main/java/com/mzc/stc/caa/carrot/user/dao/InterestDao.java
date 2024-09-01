package com.mzc.stc.caa.carrot.user.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.user.model.InterestDto;
import com.mzc.stc.caa.carrot.user.model.RegionDto;
import com.mzc.stc.caa.carrot.user.model.ReviewDto;

@Mapper
public interface InterestDao {
	
	// check
	/**
	 * 유저 지역 제거 API 
	 * 
	 * @param LogoutReqDto logoutReqDto
	 */
	boolean checkInterest(InterestDto interestDto);
	
	boolean checkInterestAccess(InterestDto interestDto);
	
	boolean checkInterestList(InterestDto interestDto);
	
	boolean checkInterestHistory(InterestDto interestDto);
	
	int createInterest(InterestDto interestDto);
	
	int patchInterestStatus(InterestDto interestDto);
	
	int createAgainInterest(InterestDto interestDto);
	
	List<HashMap<String, Object>> getInterestList(int productReviewIdx);
	
	
}