package com.mzc.stc.caa.carrot.user.service;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_CREATE_REGION;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_CREATE_USER;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.POST_USERS_EXISTS_PHONENUMBER;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.SAVE_FAIL_jwt;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_FAIL_LOGOUT;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_FAIL_LOGIN;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.PATCH_USERS_DELETE_USER;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_DELETE_USER;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_DELETE_USER_REGION;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.FAIL_LOGIN_STATUS;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_UPDATE_JWT;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_ALREADY_LOGOUT;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.WRONG_USERS_AUTHCODE;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.ALREADY_USERS_AUTHCODE;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.EXPIRED_AUTHCODE_STATUS;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.DATABASE_ERROR_USER_NOT_LOGIN;

import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.user.dao.JwtDao;
import com.mzc.stc.caa.carrot.user.dao.RegionDao;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.TempAuthCodeDto;
import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.utils.JwtService;

//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import net.nurigo.java_sdk.api.Message;
//import net.nurigo.java_sdk.exceptions.CoolsmsException;

//Service Create, Update, Delete 의 로직 처리

/**
 * 유저 기본 인증 정보 CUD 를 위한 Service
 * 
 * @author 김경민
 * @version 1.0, 유저 회원가입, 인증, 로그인, 로그아웃, 탈퇴
 */
@Service
public class UserAuthService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final JwtService jwtService;

	private final JwtDao jwtDao;

	private final UserDao userDao;

	private final RegionDao regionDao;

	/**
	 * 생성자
	 * @param jwtService jwtDao userDao regionDao
	 */
	public UserAuthService(JwtService jwtService, JwtDao jwtDao, UserDao userDao, RegionDao regionDao) {

		this.jwtService = jwtService;
		this.jwtDao = jwtDao;
		this.userDao = userDao;
		this.regionDao = regionDao;

	}

	/**
	 * 회원 가입 전 유저 인증 절차 service 메소드 ( authCode 생성 )
	 * 
	 * @param UserDto userDto , (PhoneNumber)
	 * @return UserDto UserDto , (userIdx, authCode)
	 * @exception
	 */
	@Transactional
	public HashMap<String, Object> createAuthCode(UserDto userDto) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		// 임시 테이블 전화번호 중복 검사
		if (this.userDao.checkTempPhoneNumber(userDto.getPhoneNumber())) {
			throw new BaseException(ALREADY_USERS_AUTHCODE); // "이미 가입된 전화번호 입니다."
		}

		/**
		 * 로그인 상태 확인
		 */
		// 인증 코드 생성 (4자리 랜덤 숫자)
		Random rand = new Random();
		String randomSum = "";
		for (int i = 0; i < 4; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			randomSum += ran;
		}
		int authCode = Integer.parseInt(randomSum);

		// 인증코드 SMS 문자 알림 구현
//		try {
//			String api_key = Secret.SMS_ALERT_API_KEY; // 본인의 API KEY
//			String api_secret = Secret.SMS_ALERT_API_SECRET; // 본인의 API SECRET
//			Message coolsms = new Message(api_key, api_secret);
//
//			// 4 @params (to, from, type, text)
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("to", userDto.getPhoneNumber()); // 수신전화번호
//			params.put("from", Secret.SMS_PHONE_NUMBER); // "발송할 번호 입력" , ( 개발자 번호 )
//			params.put("type", "SMS");
//			params.put("text", "당근마켓 휴대폰인증 메시지 : 인증번호는" + "[" + authCode + "]" + "입니다.");
//			params.put("app_version", "test app 1.2"); // application name and version
//
//			JSONObject obj = coolsms.send(params);
//			logger.debug(obj.toString()); // 오류가 있나 확인
//
//			if (obj.toString().contains("error_list")) {
//				
//				logger.error("error_list", obj.toString().contains("error_list"));
//				System.out.println(obj.toString().contains("error_list"));
//				throw new BaseException(POST_USERS_FAIL_ALERT_SMS); // 사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다.
//			}
//
//		} catch (CoolsmsException e) { // 문자 알림 실패시
//			
//			logger.error("CoolsmsException 처리 오류", e);
//			System.out.println(e);
//			throw new BaseException(POST_USERS_FAIL_ALERT_SMS); // 사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다.
//		}

		// 인증코드 전송 전, 인증코드를 임시 DB에 저장
		try {

			HashMap<String, Object> tempReq = new HashMap<String, Object>();
			tempReq.put("authCode", authCode);
			tempReq.put("phoneNumber", userDto.getPhoneNumber());

			/**
			 * 로그인 상태 확인
			 */
			// 인증 코드를 임시 테이블에 저장 후 그 인증 코드에 대한 pk 값 발급
			this.userDao.saveAuthCode(tempReq);

//			int tempAuthCodeIdx = (int) tempReq.get("tempAuthCodeIdx");
//			
//			System.out.println("final : " + tempReq);

			return tempReq;

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_CREATE_USER); // 유저 생성 실패 에러
		}

	}

	/**
	 * 회원 가입 전 유저 인증 절차 service 메소드 ( 유저가 보낸 authCode 체크 )
	 * 
	 * @param TempAuthCodeDto tempAuthCodeDto
	 * @return
	 * @exception
	 */
	@Transactional
	public void checkAuthCode(TempAuthCodeDto tempAuthCodeDto) throws BaseException {

		// 유저가 보낸 인증코드 확인 절차

		try {

			/**
			 * 로그인 상태 확인
			 */
			// 올바른 인증코드인지 확인
			if (!this.userDao.checkAuthCode(tempAuthCodeDto)) {
				throw new BaseException(WRONG_USERS_AUTHCODE);
			}
			;

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(WRONG_USERS_AUTHCODE); // 유저 생성 실패 에러
		}

	}

	/**
	 * 유저 회원가입을 위한 service 메소드
	 * 
	 * @param UserDto userDto , (PhoneNumber)
	 * @return UserDto UserDto , (userIdx, authCode)
	 * @exception
	 */
	@Transactional
	public HashMap<String, Object> createUser(int tempAuthCode) throws BaseException {

		if (!this.userDao.checkAuthCodeStatus(tempAuthCode)) {
			throw new BaseException(EXPIRED_AUTHCODE_STATUS); // "이미 사용된 인증번호 입니다."
		}

		/**
		 * 로그인 상태 확인
		 */
		// 인증번호 만료 검사

		/**
		 * 로그인 상태 확인
		 */
		String phoneNumber = this.userDao.getPhoneNumberByTempIdx(tempAuthCode);
		// 전화번호 중복 검사
		System.out.println(phoneNumber);

		if (this.userDao.checkPhoneNumber(phoneNumber)) {
			throw new BaseException(POST_USERS_EXISTS_PHONENUMBER); // "이미 가입된 전화번호 입니다."
		}

		// 유저 테이블에서 유저 등록
		try {

			// response 객체 생성
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("phoneNumber", phoneNumber);

			// 기본 닉네임 생성 (핸드폰 번호 8자리만 붙인다.)
			String nickName = "당근" + phoneNumber.substring(phoneNumber.length() - 9, phoneNumber.length());

			result.put("nickName", nickName);

			// 유저 생성 - 닉네임, 전화번호 삽입
			this.userDao.createUser(result);

			System.out.println("createResult : " + result);

			Integer userIdx = (Integer) result.get("userIdx");

			System.out.println("userIdx : " + userIdx);

			String token = jwtService.createJwt(result);

			result.put("jwt", token);

			// 인증 완료된 코드 삭제
			if (this.userDao.deleteAuthCode(tempAuthCode) == 0) {
				throw new BaseException(DATABASE_ERROR);
			}
			;

			return result;

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_CREATE_USER); // 유저 생성 실패 에러
		}

	}

	/**
	 * 유저 회원가입시 동네 등록을 위한 Service 클래스
	 * 
	 * @param int userIdx
	 * @return
	 * @exception
	 */
	@Transactional
	public void createRegion(int userIdx) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		// 동네 등록
		try {
			int regionIdx = this.userDao.createRegion(userIdx); // 유저 IDX를 이용하여 region 정보 생성.

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_CREATE_REGION); // 동네 등록 실패 에러
		}

	}

	/**
	 * 유저 jwt토큰을 DB에 저장하기위한 메소드
	 * 
	 * @param
	 * @return
	 * @exception
	 */
	@Transactional
	public void saveJwt() throws BaseException {

		try {

			int userIdx = jwtService.getUserIdx();
			System.out.println("UserIdx : " + userIdx);

			String jwt = jwtService.getJwt();

			HashMap<String, Object> result = new HashMap<>();
			result.put("userIdx", userIdx);
			result.put("jwt", jwt);

			// 기존에 jwt 토큰이 존재하는 지 확인
			if (this.jwtDao.checkJwt(userIdx)) {
				// 존재한다면 jwt 토큰 갱신
				this.jwtDao.updateJwt(result);

			} else if (!this.jwtDao.saveJwt(result)) {
				throw new BaseException(SAVE_FAIL_jwt);
			}

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_UPDATE_JWT); // jwt 업데이트 실패
		}

	}

	/**
	 * 로그인 상태 설정 클래스
	 * 
	 * @param String phoneNumber
	 * @return
	 * @exception
	 */
	/* 회원가입 인증 - userJoinCheck() */
	@Transactional
	public void logIn(String phoneNumber) throws BaseException {

		try {

			/**
			 * 로그인 상태 확인
			 */
			// userIndex값 불러오는 메소드
			int tempUserIdx = this.userDao.getUserIdxByPhoneNumber(phoneNumber);

			System.out.println("tempIdx : " + tempUserIdx);

			/**
			 * 로그인 상태 확인
			 */
			// 현재 로그인 상태 체크
			if (this.jwtDao.checkLoginStatus(tempUserIdx) == 0) {
				
				/**
				 * 로그인 상태 확인
				 */
				// 로그인 상태 변경 (userIdx 활용)
				if (!this.jwtDao.patchLoginStatus(tempUserIdx)) {
					throw new BaseException(FAIL_LOGIN_STATUS);
				}
				
			}

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_FAIL_LOGIN); // "로그인에 실패했습니다."
		}

	}

	/**
	 * 로그인 상태 확인
	 */
	/* 유저 로그아웃 - logout() */
	@Transactional
	public void logout(int userIdx) throws BaseException {
		
		/**
		 * 로그인 상태 확인
		 */
		// 이미 로그아웃된 유저인지 확인
		if (this.jwtDao.checkLoginStatus(userIdx) == 0) {
			throw new BaseException(DATABASE_ERROR_ALREADY_LOGOUT);
		}

		try {
			
			if (!this.jwtDao.logout(userIdx)) {

				throw new BaseException(DATABASE_ERROR_FAIL_LOGOUT);
			}

		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_FAIL_LOGOUT); // "로그 아웃에 실패했습니다."
		}

	}

	/**
	 * 로그인 상태 확인
	 */
	/* 회원탈퇴 (유저 비활성화) - deleteUser() */
	@Transactional
	public void deleteUser(int userIdx) throws BaseException {
		
		/**
		 * 로그인 상태 확인
		 */
		// 로그인 상태 확인
		if (this.jwtDao.checkLoginStatus(userIdx) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}
		
		/**
		 * 로그인 상태 확인
		 */
		// 회원 탈퇴 여부 확인
		if (this.userDao.checkDeleteUser(userIdx)) {

			throw new BaseException(PATCH_USERS_DELETE_USER); // "이미 탈퇴된 계정입니다."
		}
		
		try {

			/**
			 * 로그인 상태 확인
			 */
			// 회원 탈퇴
			if (this.userDao.deleteUser(userIdx) == 0) {

				throw new BaseException(DATABASE_ERROR_DELETE_USER); // 탈퇴 실패
			}
		} catch (Exception exception) {

			logger.error("DB 처리 오류", exception);
			throw new BaseException(DATABASE_ERROR_DELETE_USER);
		}

	}

	/**
	 * 로그인 상태 확인
	 */
	/* 회원탈퇴 (동네 비활성화) - deleteUser() */
	@Transactional
	public void deleteRegion(int userIdx) throws BaseException {

		try {
			
			/**
			 * 로그인 상태 확인
			 */
			// 동네 비활성화
			this.regionDao.deleteRegion(userIdx);

		} catch (Exception exception) {

			throw new BaseException(DATABASE_ERROR_DELETE_USER_REGION); // '회원 탈퇴(동네 비활성화)에 실패하였습니다.'
		}
	}

}
