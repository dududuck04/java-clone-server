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
public class ProductDto {
	
	private int productIdx;
	
	private String title;
	
	private String price;
	
	private int priceOfferStatus;
	
	private String content;
	
	private int salesStatus;
	
	private int lookupCount;
	
	private int interestCount;
	
	private int categoryIdx;
	
	private int productUserIdx;
	
	private Date createAt;
	
	private Date updateAt;
	
	private int status;
	
	private String regionName;
	
	private List<String> image;
}
