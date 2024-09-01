package com.mzc.stc.caa.carrot.user.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.user.model.RegionDto;
import com.mzc.stc.caa.carrot.user.model.ReviewDto;

@Mapper
public interface ReviewDao {
	
	// check
	/**
	 * 유저 지역 제거 API 
	 * 
	 * @param LogoutReqDto logoutReqDto
	 */
	boolean checkReview(int productReviewIdx);
	
	boolean checkReviewAccess(ReviewDto reviewDto);
	
	int createReview(ReviewDto reviewDto);
	
	int patchReviewStatus(ReviewDto reviewDto);
	
	int createProductReview(ReviewDto reviewDto);
	
	List<String> getReviewType(int productIdx);
	
	HashMap<String, Object> getReview(ReviewDto reviewDto);
	
}