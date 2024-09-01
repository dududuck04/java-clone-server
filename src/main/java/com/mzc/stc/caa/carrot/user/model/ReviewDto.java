package com.mzc.stc.caa.carrot.user.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 내 동네 추가 model
@Getter
@Setter
@ToString
public class ReviewDto {
	
	private int productReviewIdx;
	
	private int productIdx;
	
	private int reviewWriterIdx;
	
	private int status;
	
	private String content;
	
	private List<Integer> typeList;
	
	private int type;
	
}
