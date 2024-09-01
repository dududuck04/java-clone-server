package com.mzc.stc.caa.carrot.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.BaseResponse;
import com.mzc.stc.caa.carrot.user.model.RegionDto;
import com.mzc.stc.caa.carrot.user.service.UserRegionService;
import com.mzc.stc.caa.carrot.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;

/**
 * 유저 지역 정보 처리를 위한 Controller
 * 
 * @author 김경민
 * @version 1.0
 */
@RestController
@RequestMapping("/regions")
public class UserRegionController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserRegionService userRegionService;

	private final JwtService jwtService;
	
	/**
	 * 유저 지역 정보 처리 생성자 
	 * 
	 * @param userRegionService jwtService
	 * 
	 */
	public UserRegionController(UserRegionService userRegionService, JwtService jwtService) {
		this.userRegionService = userRegionService;
		this.jwtService = jwtService;
	}

	/**
	 * 유저 동네 추가를 위한 control 메소드
	 * 
	 * @param RegionDto regionDto 
	 * @return String
	 * @exception 
	 */
	@ResponseBody
	@PostMapping("")
	public BaseResponse<String> patchRegion(@RequestBody RegionDto regionDto) {

		/**
		 * 지역 이름 입력 검사
		 */
		if (regionDto.getRegionName() == null) {
			return new BaseResponse<>(POST_REGIONS_EMPTY_NAME);
		}

		/**
		 * 지역 이름 길이 형식 검사
		 */
		if (regionDto.getRegionName().length() <= 2 || regionDto.getRegionName().length() > 15) {
			return new BaseResponse<>(POST_REGIONS_INVALID_NAME);
		}

		/**
		 * 지역 위도 입력 검사
		 */
		if (regionDto.getLongitude() == 0 || regionDto.getLatitude() == 0) {
			return new BaseResponse<>(POST_REGIONS_EMPTY_LATITUDE_LONGITUDE);
		}

		/**
		 * 지역 경도 입력 검사
		 */
		if (regionDto.getLongitude() < -180 || regionDto.getLongitude() > 180 || regionDto.getLatitude() < -90
				|| regionDto.getLatitude() > 90) {
			return new BaseResponse<>(POST_REGIONS_INVALID_LATITUDE_LONGITUDE);
		}
		

		try {

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 
			
			regionDto.setUserIdx(userIdxByJwt);
			
			/**
			 * 새로운 지역 추가
			 */
			this.userRegionService.patchRegion(regionDto);

			return new BaseResponse<>("지역 추가 성공");
		} catch (BaseException exception) {
			logger.error("지역 처리 예외 발생", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 유저 동네 조회를 위한 control 메소드
	 * 
	 * @param int userIdx 
	 * @return BaseResponse<GetRegion>
	 * @exception
	 */
	@ResponseBody
	@GetMapping("") // (GET) 127.0.0.1:8081/regions/:userIdx
	public BaseResponse<List<HashMap<String, Object>>> getRegion() {
		
		
		try {

			/**
			 * jwt를 통한 userIdx조회
			 */
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 

			List<HashMap<String, Object>> getRegionResult = this.userRegionService.getRegion(userIdxByJwt);
			
			if (getRegionResult.size() == 0) {
				return new BaseResponse<>(GET_REGIONS_FAIL);
			}
			
			return new BaseResponse<>(getRegionResult);
		} catch (BaseException exception) {
			
			logger.error("error message", exception);
			System.out.println(exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 내 동네 삭제 API [PATCH] /regions/:regionIdx/status
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@DeleteMapping("/{regionIdx}/status")
	public BaseResponse<String> patchRegionStatus(@PathVariable("regionIdx") int regionIdx) {
		try {
			int userIdxByJwt = jwtService.getUserIdx();
			
			System.out.println("UserId : " + userIdxByJwt);

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 
			
			RegionDto regionDto = new RegionDto();
			regionDto.setRegionIdx(regionIdx);
			regionDto.setUserIdx(userIdxByJwt);
			
			System.out.println("UserId : " + regionDto);

			this.userRegionService.patchRegionStatus(regionDto);

			return new BaseResponse<>("성공");
		} catch (BaseException exception) {
			
			logger.error("error message", exception);
			System.out.println(exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 내 동네 인증하기 API [PATCH] /regions/:idx/userIdx/auth-status
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@PutMapping("/{regionIdx}/myTownAuth")
	public BaseResponse<String> patchRegionAuth(@PathVariable("regionIdx") int regionIdx) {
		try {
			int userIdxByJwt = jwtService.getUserIdx();
			
			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 
			
			
			RegionDto regionDto = new RegionDto();
			regionDto.setRegionIdx(regionIdx);
			regionDto.setUserIdx(userIdxByJwt);
			
			System.out.println(regionDto);

			
			this.userRegionService.patchRegionAuth(regionDto);
			
			return new BaseResponse<>("성공");
			
		} catch (BaseException exception) {
			
			logger.error("error message", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 유저 대표 동네 설정하기 API
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@PutMapping("/{regionIdx}/nowStatus")
	public BaseResponse<String> patchRegionNow(@PathVariable("regionIdx") int regionIdx) {
		try {
			int userIdxByJwt = jwtService.getUserIdx();
			
			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 
			
			RegionDto regionDto = new RegionDto();
			regionDto.setRegionIdx(regionIdx);
			regionDto.setUserIdx(userIdxByJwt);

			this.userRegionService.patchRegionNow(regionDto);

			return new BaseResponse<>("성공");
			
		} catch (BaseException exception) {
			
			logger.error("error message", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
