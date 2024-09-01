package com.mzc.stc.caa.carrot.user.controller;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.POST_USERS_EMPTY_PHONENUMBER;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.POST_USERS_INVALID_PHONENUMBER;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.POST_USERS_REPORT_TYPE_EMPTY;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.POST_lOGINS_EMPTY_AUTHCODE;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.POST_lOGINS_INVALID_AUTHCODE;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.WRONG_USERS_REPORT_TYPE;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.INVALID_JWT;
import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.EMPTY_TEMPAUTHCODEIDX;
import static com.mzc.stc.caa.carrot.utils.ValidationRegex.*;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.BaseResponse;
import com.mzc.stc.caa.carrot.user.model.TempAuthCodeDto;
import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.user.service.UserAuthService;
import com.mzc.stc.caa.carrot.utils.JwtService;

/**
 * 유저 인증 정보 처리를 위한 Controller
 * 
 * @author 김경민
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
public class UserAuthController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserAuthService userAuthService;

	private final JwtService jwtService;

	/**
	 * 유저 인증 정보 처리 생성자
	 * 
	 * @param userService jwtService
	 * 
	 */
	public UserAuthController(UserAuthService userAuthService, JwtService jwtService) {
		this.userAuthService = userAuthService;
		this.jwtService = jwtService;
	}

	/**
	 * 회원 가입전 휴대폰 인증 절차, 전화번호를 이용한 인증코드 받기 control 메소드
	 * 
	 * @param UserDto userDto (PhoneNumber)
	 * @return tempAuthCodeIdx, authCode
	 * @exception
	 */
	@ResponseBody
	@PostMapping("auth")
	public BaseResponse<HashMap<String, Object>> getAuthCode(@RequestBody UserDto userDto) {

		/**
		 * 전화번호 입력 검사
		 */
		if (userDto.getPhoneNumber() == null) {
			return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
		}

		/**
		 * 전화번호 형식 검사
		 */
		if (!isRegexPhoneNumber(userDto.getPhoneNumber())) {
			return new BaseResponse<>(POST_USERS_INVALID_PHONENUMBER);
		}

		try {

			/**
			 * 인증코드를 받기위한 메소드
			 */
			HashMap<String, Object> tempRes = userAuthService.createAuthCode(userDto);
			tempRes.remove("phoneNumber");

			return new BaseResponse<>(tempRes);

		}

		catch (BaseException exception) {

			logger.error("BaseException", exception);
			return new BaseResponse<>((exception.getStatus()));
		}

	}

	/**
	 * 회원 가입전 유저 인증 절차, 유저로 부터 받은 인증코드를 검증하는 control 메소드
	 * 
	 * @param Integer authCode , int tempAuthCodeIdx
	 * @return "사용자 인증에 성공하였습니다."
	 * @exception
	 */
	@ResponseBody
	@GetMapping("/auth")
	public BaseResponse<String> checkAuthCode(@RequestParam(required = false) Integer authCode,
			@RequestParam(required = false) Integer tempAuthCodeIdx) {

		TempAuthCodeDto tempAuthCodeDto = new TempAuthCodeDto();

		/**
		 * 인증코드 입력란 NULL 값 체크
		 */
		if (authCode == null || authCode == 0) {
			return new BaseResponse<>(POST_lOGINS_EMPTY_AUTHCODE);
		}

		/**
		 * 인증코드 정규표현식 (총 1 ~ 4자리 수 형식)
		 */
		if (!isRegexAuthCode(authCode)) {
			return new BaseResponse<>(POST_lOGINS_INVALID_AUTHCODE);
		}

		/**
		 * tempAuthCodeIdx 입력란 NULL 값 체크
		 */
		if (tempAuthCodeIdx == null || tempAuthCodeIdx == 0) {
			return new BaseResponse<>(EMPTY_TEMPAUTHCODEIDX);
		}

		tempAuthCodeDto.setTempAuthCodeIdx(tempAuthCodeIdx);
		tempAuthCodeDto.setAuthCode(authCode);

		try {
			/**
			 * 유저로 부터 받은 인증코드 검증 메소드
			 */
			userAuthService.checkAuthCode(tempAuthCodeDto);

			return new BaseResponse<>("사용자 인증에 성공하였습니다.");

		}

		catch (BaseException exception) {

			logger.error("BaseException", exception);
			return new BaseResponse<>((exception.getStatus()));
		}

	}

	/**
	 * 유저 회원가입을 위한 control 메소드
	 * 
	 * @param TempAuthCodeDto tempAuthCodeDto , (tempAuthCodeIdx)
	 * @return jwt 토큰
	 * @exception
	 */
	@ResponseBody
	@PostMapping("")
	public BaseResponse<HashMap<String, Object>> createUser(@RequestBody TempAuthCodeDto tempAuthCodeDto) {

		/**
		 * tempAuthCodeIdx 입력란 NULL 값 체크
		 */
		if (tempAuthCodeDto.getTempAuthCodeIdx() == 0) {
			return new BaseResponse<>(EMPTY_TEMPAUTHCODEIDX);
		}

		try {

			/**
			 * 임시 테이블에 저장된 핸드폰 번호를 실제 유저 테이블로 옮겨서 유저 데이터를 저장하는 메소드
			 */
			HashMap<String, Object> createUserResult = userAuthService.createUser(tempAuthCodeDto.getTempAuthCodeIdx());

			System.out.println("hashmap userIdx : " + createUserResult.get("userIdx"));

			/**
			 * pk값을 이용해 유저 정보 보완, region 정보 입력
			 */
			userAuthService.createRegion((int) createUserResult.get("userIdx"));
			userAuthService.createRegion((int) createUserResult.get("userIdx"));
			createUserResult.remove("userIdx");
			createUserResult.remove("phoneNumber");

			return new BaseResponse<>(createUserResult);

		}

		catch (BaseException exception) {

			logger.error("BaseException", exception);
			return new BaseResponse<>((exception.getStatus()));
		}

	}

	/**
	 * 유저 로그인을 위한 메소드
	 * 
	 * @param
	 * @return "로그인에 성공하였습니다."
	 * @exception
	 */
	@ResponseBody
	@PostMapping("/login")
	public BaseResponse<String> logIn() {

		try {

			String phoneNumberByJwt = jwtService.getPhoneNumber();

			/**
			 * jwt 토큰 DB 저장 메소드
			 */
			this.userAuthService.saveJwt();

			/**
			 * 로그인 상태 설정 메소드
			 */
			userAuthService.logIn(phoneNumberByJwt);

			return new BaseResponse<>("로그인에 성공하였습니다.");

		} catch (BaseException exception) {

			logger.error("BaseException", exception);
			return new BaseResponse<>(exception.getStatus());
		}
	}

	/**
	 * 로그아웃
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@PutMapping("/logout")
	public BaseResponse<String> logout() {

		try {

			/**
			 * 클라이언트에서 받아온 토큰에서 Idx 추출
			 */
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			/**
			 * 유저 로그아웃 , 로그인 테이블 status 0 값 변경
			 */
			this.userAuthService.logout(userIdxByJwt);

			return new BaseResponse<>("유저가 로그아웃되었습니다.");

		} catch (BaseException exception) {

			logger.error("BaseException", exception);
			return new BaseResponse<>((exception.getStatus()));
		}

	}

	/**
	 * 회원 탈퇴
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@DeleteMapping("/status")
	public BaseResponse<String> deleteUser(@RequestParam(required = false) Integer deleteType) { // BaseResponse<String>


		try {
			
			/**
			 * 타입 입력 검사
			 */
			if (deleteType == null) {
				return new BaseResponse<>(POST_USERS_REPORT_TYPE_EMPTY);
			}

			/**
			 * 타입 형식 검사
			 */
			if (!isRegexDeleteType(deleteType)) {
				return new BaseResponse<>(WRONG_USERS_REPORT_TYPE);
			}
			
			
			
			

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			/**
			 * 유저 상태 비활성화
			 */
			userAuthService.deleteUser(userIdxByJwt);

			/**
			 * 동네 상태 비활성화
			 */
			userAuthService.deleteRegion(userIdxByJwt);

			String typeMessage;

			switch (deleteType) {
			case 1:
				typeMessage = "너무 많이 이용해요";
				break;
			case 2:
				typeMessage = "사고 싶은 물품이 없어요";
				break;
			case 3:
				typeMessage = "물품이 안팔려요";
				break;
			case 4:
				typeMessage = "비매너 사용자를 만났어요";
				break;
			case 5:
				typeMessage = "새 계정을 만들고 싶어요";
				break;
			case 6:
				typeMessage = "기타";
				break;
			default:
				typeMessage = null;
			}

			return new BaseResponse<>("계정 탈퇴 이유 : " + typeMessage);

		} catch (BaseException exception) {

			return new BaseResponse<>((exception.getStatus()));
		}
	}

}
