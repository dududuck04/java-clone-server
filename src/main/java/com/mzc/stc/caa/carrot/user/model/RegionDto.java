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
public class RegionDto {
	
	private int regionIdx;

	private String regionName;

	private double latitude;

	private double longitude;

	private int userIdx;

	private int authStatus;
	
	private int authCount;
	
	private int nowSatus;
	
	private Date createAt;
	
	private Date updateAt;
	
	private int status;

}