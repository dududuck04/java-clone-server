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
public class BlockDto {
	
	private int blockIdx;
	
	private int userIdx;
	 
	private int targetIdx;
	
	
}
