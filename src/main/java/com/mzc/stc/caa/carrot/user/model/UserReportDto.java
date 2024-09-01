package com.mzc.stc.caa.carrot.user.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 유저 정보
 * 
 * @author 김경민
 *
 */

@Getter
@Setter
@ToString
//@AllArgsConstructor
//@RequiredArgsConstructor
public class UserReportDto {
	
	public UserReportDto() {
		super();
	}
	
	/**
	 * 유저 번호
	 * @return the userIdx
	 * @param userIdx the userIdx to set
	 */
//	@NonNull
    private int userReportIdx;
    
	/**
	 * 유저 전화번호
	 * @return the phoneNumber
	 * @param phoneNumber the phoneNumber to set
	 */
    private int userIdx;
    
	/**
	 * 유저 프로필 사진
	 * @return the userIdx
	 * @param userIdx the userIdx to set
	 */

    private int reportTargetIdx;
    
	/**
	 * 유저 데이터 갱신 시각
	 * @return the updateAt
	 * @param updateAt the updateAt to set
	  
	 */
    
    private String content;
    
    private int type;
    
    private int status;
    
    private String nickName;

}