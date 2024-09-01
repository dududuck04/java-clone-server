package com.mzc.stc.caa.carrot.user.model;

import java.sql.Timestamp;

import lombok.Getter;
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
public class UserDto {
	
	public UserDto() {
		super();
	}
	
	/**
	 * 유저 pk
	 */
    private int userIdx;
    
	/**
	 * 유저 닉네임
	 */
    private String nickName;
    
	/**
	 * 유저 전화번호
	 */
    private String phoneNumber;
    
	/**
	 * 유저 프로필 사진
	 */
    private String image;
    
	/**
	 * 소셜로그인 사용 여부
	 */
    private String socialStatus;
    
	/**
	 * 유저 매너온도
	 */
    private double mannerTemp;
    
	/**
	 * 재거래 희망률
	 */
    private double tradeRate;
    
	/**
	 * 유저 응답률
	 */
    private double responseRate;
    
	/**
	 * 유저 회원가입 시각
	 */
    private Timestamp createAt;
    
	/**
	 * 유저 데이터 갱신 시각
	 */
    private Timestamp updateAt;
    
	/**
	 * 유저 상태 정보 ( ACTIVE , INACTIVE , DORMANT ) 
	 */
    private int status;

}