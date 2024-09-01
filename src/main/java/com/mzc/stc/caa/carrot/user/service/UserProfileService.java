package com.mzc.stc.caa.carrot.user.service;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mzc.stc.caa.carrot.config.BaseException;

import com.mzc.stc.caa.carrot.user.dao.JwtDao;
import com.mzc.stc.caa.carrot.user.dao.RegionDao;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.BlockDto;

import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.user.model.UserReportDto;
import com.mzc.stc.caa.carrot.utils.JwtService;

//Service Create, Update, Delete 의 로직 처리
/**
 * 유저 프로필 정보 CUD 를 위한 Service
 * 
 * @author 김경민
 * @version 1.0, 유저 회원가입, 인증, 로그인, 로그아웃, 탈퇴
 */
@Service
public class UserProfileService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserDao userDao;

	private final JwtDao jwtDao;

	/**
	 * 유저 프로필 정보 생성자
	 * 
	 * @param userDao jwtDao 
	 */
	public UserProfileService(JwtService jwtService, JwtDao jwtDao, UserDao userDao, RegionDao regionDao) {

		this.userDao = userDao;
		this.jwtDao = jwtDao;
		
	}

	/**
	 * 유저 프로필 수정을 위한 control 메소드
	 * 
	 * @param UserDto userDto
	 * @return String
	 * @exception
	 */
	@Transactional
	public void modifyProfile(UserDto userDto) throws BaseException {
		
		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(userDto.getUserIdx()) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(userDto.getUserIdx())) {

			throw new BaseException(PATCH_USERS_DELETE_USER); // "이미 탈퇴된 계정입니다."
		}

		/**
		 * 닉네임 중복 검사
		 */
		if (this.userDao.checkNickName(userDto.getNickName())) {
			throw new BaseException(POST_USERS_EXISTS_NICKNAME); // "사용중인 닉네임 입니다."
		}

		/**
		 * 이미지 값 변경 전 유저가 선택한 이미지가 존재하는 지 확인
		 */
		if (userDto.getImage() != null) {

			/**
			 * 이미지 변경
			 */
			if (this.userDao.modifyImage(userDto) == 0) {
				throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_USER_IMAGE); // "이미지 변경에 실패"
			}
		}

		/**
		 * 닉네임 값 변경 전 유저가 입력한 닉네임이 존재하는 지 확인
		 */
		if (userDto.getNickName() != null) {

			/**
			 * 닉네임 변경
			 */
			if (this.userDao.modifyNickName(userDto) == 0) {
				throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_USER_NICKNAME);
			}
		}

	}

	/**
	 * 다른 유저 차단을 위한 Service 메소드
	 * 
	 * @param UserDto userDto
	 * @return String
	 * @exception
	 */
	@Transactional
	public void blockUser(HashMap<String, Object> blockUserResult) throws BaseException {

		
		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus((Integer) blockUserResult.get("userIdx")) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser((Integer) blockUserResult.get("userIdx"))) {

			throw new BaseException(PATCH_USERS_DELETE_USER); 
		}
		
		logger.debug("nickName check {} ",blockUserResult.get("nickName"));
		logger.debug("nickName check {} ",blockUserResult);
		
		
		/**
		 * 존재하는 닉네임인지 확인
		 */
		if (!this.userDao.checkNickName((String) blockUserResult.get("nickName"))) {
			throw new BaseException(GET_USERS_NOT_EXIST); 
		}

		/**
		 * 닉네임을 이용해 차단하려는 pk 값 확인
		 */
		Integer targetIdx = (Integer) this.userDao.getUserIdxByNickName(blockUserResult);

		blockUserResult.put("targetIdx", targetIdx);

		BlockDto blockDto = new BlockDto();
		blockDto.setUserIdx((Integer) blockUserResult.get("userIdx"));
		blockDto.setTargetIdx((Integer) blockUserResult.get("targetIdx"));
		
		
		/**
		 * 사용자 차단 여부 확인
		 */
		if (this.userDao.checkBlcokUser(blockDto)) {
			throw new BaseException(POST_USERS_BLOCKS_NICKNAME); 
			
			
		}
		
		/**
		 * 과거 차단 후 재 차단할 필요가 있다면
		 */
		if (this.userDao.checkBlockStatus(blockUserResult)) {
			
			this.userDao.blockAgainUser(blockUserResult);
		}
		else {
			
			/**
			 *  사용자 차단
			 */
			this.userDao.blockUser(blockUserResult);
		}

		
	}

	/**
	 *내 프로필 조회 - getMyProfile() 
	 */
	public HashMap<String, Object> getMyProfile(HashMap<String, Object> retrieveUserProfile) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus((Integer) retrieveUserProfile.get("userIdx")) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser((Integer) retrieveUserProfile.get("userIdx"))) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}

		try {

			/**
			 * 유저 정보를 요청
			 */
			retrieveUserProfile = this.userDao.getMyProfile(retrieveUserProfile);

			return retrieveUserProfile;

		} catch (Exception exception) {

			logger.error("error message : ", exception);
			throw new BaseException(DATABASE_ERROR_USER_INFO);
		}
	}

	/**
	 * 사용자 차단 여부 확인 - checkBlcokUser() 
	 */
	
	public void checkBlcokUser(HashMap<String, Object> blockUserResult) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus((Integer) blockUserResult.get("userIdx")) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser((Integer) blockUserResult.get("userIdx"))) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}
		
		
		if (this.userDao.checkCountBlockUser(blockUserResult)) {
			throw new BaseException(POST_USERS_BLOCKS_NICKNAME); 
		}

	}

	/**
	 *  차단한 사용자 정보 조회 - getBlockUser()
	 */
	public List<BlockDto> getBlockUser(int userIdx) throws BaseException {
		
		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(userIdx) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(userIdx)) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}
		
		try {

			List<BlockDto> getUserBlockRes = userDao.getBlockUser(userIdx);

			return getUserBlockRes;

		} catch (Exception exception) {

			logger.error("error Message : ", exception);
			throw new BaseException(DATABASE_ERROR_BLOCK_USER_INFO); // "차단한 사용자 프로필 조회에 실패하였습니다."
		}
	}

	/**
	 * 사용자 차단 해제
	 */
	@Transactional
	public void blockCancel(HashMap<String, Object> blockCancelResult) throws BaseException { 
		
		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus((Integer) blockCancelResult.get("userIdx")) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}
		
		
		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser((Integer) blockCancelResult.get("userIdx"))) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}
		
		/**
		 * 닉네임을 이용해 pk 값 추출
		 */
		Integer targetIdx = (Integer) this.userDao.getUserIdxByNickName(blockCancelResult);

		logger.debug("{}", targetIdx);

		blockCancelResult.put("targetIdx", targetIdx);
		
		/**
		 * 유저 차단 상태 확인
		 */
		if (this.userDao.checkBlockStatus(blockCancelResult)) {
			
			logger.debug("boolean : {}", this.userDao.checkBlockStatus(blockCancelResult));
			throw new BaseException(POST_USERS_ALREADY_FREE);
		}
		
		logger.debug("boolean : {}", this.userDao.checkBlockStatus(blockCancelResult));
		
		/**
		 * 유저 차단 해제
		 */
		if (this.userDao.blockCancel(blockCancelResult) == 0) {
			throw new BaseException(DATABASE_ERROR_BLOCK_CANCELL_USER);
		}
		
	}

	/**
	 * 사용자 신고
	 */
	@Transactional
	public String reportUser(UserReportDto userReportDto) throws BaseException {

//        //사용자 신고 여부 확인
//        if(this.userDao.checkReportUser(userReportDto)){         
//            throw new BaseException(POST_USERS_REPORT_NICKNAME);        //"이미 신고한 사용자 입니다."
//        }
		
		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(userReportDto.getUserIdx()) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(userReportDto.getUserIdx())) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}
		
		/**
		 * 존재하는 닉네임인지 확인
		 */
		if (!this.userDao.checkNickName(userReportDto.getNickName())) {
			throw new BaseException(GET_USERS_NOT_EXIST); 
		}
		
		/**
		 * 닉네임을 이용해 pk 값 추출
		 */
		Integer targetIdx = (Integer) this.userDao.getUserIdxByNickName2(userReportDto);
		userReportDto.setReportTargetIdx(targetIdx);
		
		/**
		 * 이미 신고한 유저인지 확인
		 */
		if (this.userDao.checkReportStatus(userReportDto)) {
			throw new BaseException(GET_USERS_ALREADY_REPORT); 
		}
		
		/**
		 * 사용자 신고
		 */
		try {
			userDao.reportUser(userReportDto);
			String typeMessage;

			switch (userReportDto.getType()) {
			case 1:
				typeMessage = "전문 판매업자 같아요";
				break;
			case 2:
				typeMessage = "비매너 사용자에요";
				break;
			case 3:
				typeMessage = "욕설을 해요";
				break;
			case 4:
				typeMessage = "성희롱을 해요";
				break;
			case 5:
				typeMessage = "거래 / 환불 분쟁 신고";
				break;
			case 6:
				typeMessage = "사기당했어요";
				break;
			case 7:
				typeMessage = "연애 목적의 대화를 시도해요";
				break;
			case 8:
				typeMessage = "다른 문제가 있어요";
				break;
			default:
				typeMessage = null;

			}

			return typeMessage;

		} catch (Exception exception) {

			logger.error("error Message : ", exception);
			throw new BaseException(DATABASE_ERROR_REPORT_USER); // "사용자 신고에 실패했습니다."
		}

	}

}