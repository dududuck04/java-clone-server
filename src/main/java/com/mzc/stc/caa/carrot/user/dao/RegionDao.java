package com.mzc.stc.caa.carrot.user.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.user.model.ProductDto;
import com.mzc.stc.caa.carrot.user.model.RegionDto;

@Mapper
public interface RegionDao {
	
	// check
	/**
	 * 유저 지역 제거 API 
	 * 
	 * @param LogoutReqDto logoutReqDto
	 */
	boolean checkRegionName(RegionDto regionDto);
	
	boolean checkRegionName2 (ProductDto productDto);
	
	boolean checkRegionDetail(RegionDto regionDto);

	boolean checkRegionIdx(int regionIdx);

	boolean checkRegionAccess(RegionDto regionDto);

	boolean checkRegionNow(RegionDto regionDto);
	
	boolean checkRegionNowByRegionId(int regionIdx);
	
	boolean checkRegionNull(int userIdx);
	
	//delete
	
	
	int deleteRegion(int UserIdx);
	
	int deleteRegionNowStatus(int regionIdx);
	
	int deleteRegionStatus(RegionDto regionDto);
	
	//read
	
	int getRegionCount(int userIdx);
	
	List<HashMap<String, Object>> getRegion(int userIdx);
	
	String getNowRegionName(int userIdx);
	
	
	//patch

	int patchRegion(RegionDto regionDto);
	
	int patchTargetRegion(RegionDto regionDto);
	
	int patchRegionAuth(RegionDto regionDto);

	int patchRegionNow(RegionDto regionDto);

}
