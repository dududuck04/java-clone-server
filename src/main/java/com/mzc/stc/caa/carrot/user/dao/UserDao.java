package com.mzc.stc.caa.carrot.user.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.user.model.BlockDto;
import com.mzc.stc.caa.carrot.user.model.TempAuthCodeDto;
import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.user.model.UserReportDto;


/**
 * 유저 기본 데이터 처리 DAO
 * 
 * @return 사원 목록
 */
@Mapper
public interface UserDao {
	
	// validation check
	
	/**
	 * 삭제된 유저인지 확인
	 * 
	 * @return 삭제된 유저라면 True
	 */
	boolean checkDeleteUser(int userIdx);
	
	/**
	 * 기존에 존재하는 휴대폰 번호인지 확인
	 * 
	 * @return 기존에 존재하는 휴대폰 번호라면 True
	 */
	boolean checkPhoneNumber(String phoneNumber);
	
	/**
	 * 유저 인증 임시 테이블에서 기존에 존재하는 휴대폰 번호인지 확인
	 * 
	 * @return 기존에 존재하는 휴대폰 번호라면 True
	 */
	boolean checkTempPhoneNumber(String phoneNumber);
	
	/**
	 * 유저 인증코드가 유효한지 확인
	 * 
	 * @return 유효한 코드라면 True
	 */
	boolean checkAuthCodeStatus(int tempAuthCode);
	
	/**
	 * 인증코드를 검증하는 메소드
	 * 
	 * @return 올바른 인증코드를 넣었다면 True
	 */
	boolean checkAuthCode(TempAuthCodeDto tempAuthCodeDto);
	
	
	/**
	 * 닉네임 중복여부 확인 메소드
	 * 
	 * @return 중복된 닉네임이 있다면 True
	 */
	boolean checkNickName(String nickname);
	
	
	/**
	 * 닉네임 중복여부 확인 메소드
	 * 
	 * @return 중복된 닉네임이 있다면 True
	 */
	boolean checkCountBlockUser(HashMap<String, Object> blockUserResult);
	
	boolean blockUser(HashMap<String, Object> blockUserResult);
	
	boolean blockAgainUser(HashMap<String, Object> blockUserResult);
	
	boolean checkBlcokUser(BlockDto blockDto);
	
	boolean checkReportUser(UserReportDto userReportDto);
	
	boolean checkBlockStatus(HashMap<String, Object> blockCancelResult);
	
	boolean checkReportStatus(UserReportDto userReportDto);
	
	////////////////////////////////////////// create
	
	/**
	 * 사용자 정보를 저장한다.
	 * 
	 * @param user 저장할 사용자 정보.
	 */
	int createUser(HashMap<String, Object> result);
	
	/**
	 * 회원 가입 생성시 추가 유저 정보 생성 API 
	 * 
	 * @param UserReqDto userReq
	 */
	int createRegion(int userIdx);
	
	int saveAuthCode(HashMap<String, Object> tempReq);
	
	int reportUser(UserReportDto userReportDto);
	
	
	/////////////////////////////////////////////////// status 패치를 이용한 DELETE
	
	int deleteAuthCode(int tempAuthCode);
	
	/**
	 * 유저 탈퇴 API 
	 * 
	 * @param LogoutReqDto logoutReqDto
	 */
	int deleteUser(int userIdx);
	
	
	///////////////////////////////////////////////// read
	
	int getUserIdxByPhoneNumber(String phoneNumber);
	
	int getUserIdxByAuthCode(Integer authCode);
	
	Integer getUserIdxByNickName(HashMap<String, Object> blockUserResult);
	
	Integer getUserIdxByNickName2(UserReportDto userReportDto);
	
	/**
	 * 유저에게 보낸 인증코드를 임시 저장하는 생성 API 
	 * 
	 * @param TempAuthCodeDto tempAuthCodeDto
	 * @return int tempAuthCodeIdx
	 */
	
	String getPhoneNumberByTempIdx (int tempAuthCode);
	
	HashMap<String, Object> getMyProfile(HashMap<String, Object> retrieveUserProfile);
	
	List<BlockDto> getBlockUser(int userIdx);

	
	
	
	/////////////////////////////////////// PATCH
	
	
	int modifyImage(UserDto userDto);
	
	int modifyNickName(UserDto userDto);
	
	int blockCancel(HashMap<String, Object> blockCancelResult);

}
