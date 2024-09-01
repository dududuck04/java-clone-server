package com.mzc.stc.caa.carrot.user.model;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 내 동네 추가 model
@Getter
@Setter
@ToString
public class InterestDto {
	
	private int productInterestIdx;
	
	private int userIdx;
	
	private int productIdx;
	
	private int status;
}
