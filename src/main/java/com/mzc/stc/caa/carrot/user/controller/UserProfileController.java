package com.mzc.stc.caa.carrot.user.controller;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;
import static com.mzc.stc.caa.carrot.utils.ValidationRegex.isRegexPhoneNumber;
import static com.mzc.stc.caa.carrot.utils.ValidationRegex.isRegexType;


import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.BaseResponse;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.BlockDto;
import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.user.model.UserReportDto;
import com.mzc.stc.caa.carrot.user.service.UserProfileService;
import com.mzc.stc.caa.carrot.utils.JwtService;


/**
 * 유저 프로필 정보 처리를 위한 Controller
 * 
 * @author 김경민
 * @version 1.0
 */
@RestController
@RequestMapping("/profiles")
public class UserProfileController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserProfileService userProfileService;

	private final JwtService jwtService;
	

	/**
	 * 유저 프로필 정보 처리 생성자 
	 * 
	 * @param userService jwtService
	 * 
	 */
	public UserProfileController(UserProfileService userProfileService, JwtService jwtService, UserDao userDao) {
		this.userProfileService = userProfileService;
		this.jwtService = jwtService;
	}
	
	/**
	 * 유저 프로필 조회 Controller
	 * 
	 * @param 
	 * @return HashMap<String,Object>
	 * @exception 
	 */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<HashMap<String,Object>> retrieveUserProfile() {     

        try {
        	
    		/**
    		 * 토큰에서 Idx 추출
    		 */
            int userIdxByJwt = jwtService.getUserIdx();

    		/**
    		 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
    		 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 


			/**
			 * 프로필 조회 - getUserProfile()
			 */
			HashMap<String,Object> retrieveUserProfile = new HashMap<String,Object>();
			
			retrieveUserProfile.put("userIdx", userIdxByJwt);
			
			/**
			 * 프로필 조회
			 */
			retrieveUserProfile = this.userProfileService.getMyProfile(retrieveUserProfile);

            return new BaseResponse<>(retrieveUserProfile);
            
        } catch (BaseException exception) {
        	
        	logger.error("error message : " ,exception);
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    
	/**
	 * 유저 프로필 수정을 위한 control 메소드
	 * 
	 * @param UserDto userDto 
	 * @return String
	 * @exception 
	 */
    @ResponseBody
    @PutMapping("") 
    public BaseResponse<String> modifyInfo(@RequestBody UserDto userDto){


        try {
        	
    		/**
    		 * 토큰에서 Idx 추출
    		 */
            int userIdxByJwt = jwtService.getUserIdx();

    		/**
    		 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
    		 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 

            userDto.setUserIdx(userIdxByJwt);

    		/**
    		 * 유저 정보 변경
    		 */
            this.userProfileService.modifyProfile(userDto);

            return new BaseResponse<>("회원 정보 변경이 완료되었습니다.");
            
        } catch (BaseException exception) {
        	
            return new BaseResponse<>((exception.getStatus())); 
        }
    }

	/**
	 * 다른 유저 차단을 위한 control 메소드  
	 * 
	 * @param  String nickName
	 * @return String
	 * @exception 
	 */
    @ResponseBody
    @PostMapping("/blocks")
    public BaseResponse<String> blockUser(@RequestBody String nickName){   
		
        try {

    		/**
    		 * body 값으로 JSON 타입이 들어간 부분을 파싱
    		 */
        	JSONObject jObject = new JSONObject(nickName);
        	String TempNickName = jObject.getString("nickName");
        	nickName = TempNickName;
        	
    		/**
    		 * 닉네임 입력 검사 
    		 */
        	
    		if (nickName == null) {
    			return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
    		}
    		

    		HashMap<String, Object> blockUserResult = new HashMap<>();
    		
    		blockUserResult.put("nickName", nickName);
    	
    		logger.debug("'{}'", blockUserResult.get("nickName"));
        	
        	

    		/**
    		 * 토큰에서 Idx 추출
    		 */
            int userIdxByJwt = jwtService.getUserIdx();
            
            logger.debug("'{}'",userIdxByJwt);

    		/**
    		 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
    		 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 

				
			/**
			 * 사용자 차단
			 */
			blockUserResult.put("userIdx", userIdxByJwt);
			
//			System.out.println(blockUserResult);
			logger.debug("{}", blockUserResult);

			/**
			 * 차단 메소드
			 */
            this.userProfileService.blockUser(blockUserResult);

            return new BaseResponse<>(nickName + " 님을 차단했어요.");
            
        } catch (BaseException exception) {
        	logger.error("Error occured!!", exception);
//			logger.error("error message : {} ", exception.getStatus().getMessage());
			if (exception.getStatus() != null) {
				return new BaseResponse<>(exception.getStatus());
			}
			else {
	            return new BaseResponse<>(BLOCK_USER_FAIL);
			}
        }

    }

 
	/**
	 * 유저가 전체 차단한 사용자 조회 control 메소드  
	 * 
	 * @param 
	 * @return List (nickName)
	 * @exception 
	 */
    @ResponseBody            
    @GetMapping("/blocks") 
    public BaseResponse<List<BlockDto>> getBlockUser() {           

        try {
        	
    		/**
    		 * 토큰에서 Idx 추출
    		 */
            int userIdxByJwt = jwtService.getUserIdx();

    		/**
    		 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
    		 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 


			/**
			 * 차단한 사용자 정보 조회 - getBlockUser()
			 */
            List<BlockDto> blockDto = this.userProfileService.getBlockUser(userIdxByJwt);

            return new BaseResponse<>(blockDto);
            
        } catch (BaseException exception) {
        	
        	logger.error("error Message : " ,exception );
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    
	/**
	 * 차단한 사용자 해제 control 메소드  
	 * 
	 * @param UserDto userDto 
	 * @return String
	 * @exception 
	 */
    @ResponseBody
    @DeleteMapping("/blocks/free")
    public BaseResponse<String> blockCancell(@RequestBody String nickName){
    	
		/**
		 * 닉네임 입력 검사 
		 */
		if (nickName == null) {
			return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
		}
    	
		/**
		 * body 값으로 JSON 타입이 들어간 부분을 파싱
		 */
    	JSONObject jObject = new JSONObject(nickName);
    	String TempNickName = jObject.getString("nickName");
    	nickName = TempNickName;

		HashMap<String, Object> blockCancelResult = new HashMap<>();
		
		blockCancelResult.put("nickName", nickName);
    	
        try {
        	
    		/**
    		 * 토큰에서 Idx 추출
    		 */
            int userIdxByJwt = jwtService.getUserIdx();

    		/**
    		 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
    		 */
			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 
			
			blockCancelResult.put("userIdx", userIdxByJwt);
			
			/**
			 * 사용자 차단 해제
			 */
            this.userProfileService.blockCancel(blockCancelResult);

            return new BaseResponse<>(nickName + "님 차단을 해제했어요.");
            
        } catch (BaseException exception) {
        	
        	logger.error("error Message : " , exception);
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    
	/**
	 * 사용자 신고 control 메소드  
	 * 
	 * @param UserReportDto userReportDto (nickName , type)
	 * @return String (type 내용 리턴)
	 * @exception 
	 */
    @ResponseBody
    @PostMapping("/reports")
    public BaseResponse<String> reportUser(@RequestBody UserReportDto userReportDto){   
    	
		/**
		 * 닉네임 입력 검사 
		 */
		if (userReportDto.getNickName() == null) {
			return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
		}
		
		/**
		 * 타입 입력 검사 
		 */
		if (userReportDto.getType() == 0) {
			return new BaseResponse<>(POST_USERS_REPORT_TYPE_EMPTY);
		}
		
		/**
		 * 타입 형식 검사
		 */
		if(!isRegexType(userReportDto.getType())) {
			return new BaseResponse<>(WRONG_USERS_REPORT_TYPE);
		}

        try {
        	
    		/**
    		 * 토큰에서 Idx 추출
    		 */
            int userIdxByJwt = jwtService.getUserIdx();

    		/**
    		 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
    		 */

			if(jwtService.checkJwtTime() == 1) {
				
				System.out.println("jwt 시간확인 : " +jwtService.checkJwtTime());
				
				return new BaseResponse<>(INVALID_JWT);
			} 

			userReportDto.setUserIdx(userIdxByJwt);
			
			/**
			 * 사용자 신고
			 */
			String typeMessage = this.userProfileService.reportUser(userReportDto);

            return new BaseResponse<>(typeMessage);
            
        } catch (BaseException exception) {
        	
            return new BaseResponse<>((exception.getStatus()));
        }

    }
	
}
