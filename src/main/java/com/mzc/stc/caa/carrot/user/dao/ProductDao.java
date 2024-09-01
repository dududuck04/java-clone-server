package com.mzc.stc.caa.carrot.user.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mzc.stc.caa.carrot.user.model.ProductDto;



/**
 * 유저 기본 데이터 처리 DAO
 * 
 * @return 사원 목록
 */
@Mapper
public interface ProductDao {

	// check

	boolean checkRegionName(String regionName);
	
	boolean checkProduct(int productIdx);
	
	boolean checkProductAccessUser(ProductDto productDto);
	
	boolean checkProductInterest(ProductDto productDto);
	
	boolean checkProductInterestIdx(int productInterestIdx);
	
	boolean checkProductInterestAccess(HashMap<String, Object> interest);
	
	boolean checkIsPurchased(int productIdx);
	
	boolean checkIsMyProduct(ProductDto productDto);
	
	//read
	
	List<HashMap<String, Object>> getProductList(String regionName);
	
	List<HashMap<String, Object>> getProduct(int productIdx);
	
	List<HashMap<String, Object>> getProductSale(int productUserIdx);
	
	List<HashMap<String, Object>> getProductComplete(int productUserIdx);
	
	List<HashMap<String, Object>> getProductPurchased(int userIdx);
	
	List<HashMap<String, Object>> getProductInterest(int userIdx);
	
	//patch
	
	int patchProductStatus(ProductDto productDto);
	
	int patchProductInterest(HashMap<String, Object> interest);
	
	int patchProductSaleStatus(ProductDto productDto);
	
	int patchProductPurchasing(ProductDto productDto);

	
	//create
	
	int createProduct(HashMap<String, Object> product);
	
	int createProductImg(HashMap<String, Object> product);
}

	