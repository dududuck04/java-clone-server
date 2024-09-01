package com.mzc.stc.caa.carrot.user.service;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;
import static com.mzc.stc.caa.carrot.config.secret.Secret.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.BaseResponse;
import com.mzc.stc.caa.carrot.config.secret.Secret;
import com.mzc.stc.caa.carrot.user.dao.JwtDao;
import com.mzc.stc.caa.carrot.user.dao.ProductDao;
import com.mzc.stc.caa.carrot.user.dao.RegionDao;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.JwtInfoDto;
import com.mzc.stc.caa.carrot.user.model.ProductDto;
import com.mzc.stc.caa.carrot.user.model.RegionDto;
import com.mzc.stc.caa.carrot.user.model.TempAuthCodeDto;
import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.utils.JwtService;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

//Service Create, Update, Delete 의 로직 처리
/**
 * 상품 정보 CUD 를 위한 Service
 * 
 * @author 김경민
 * @version 1.0, 유저 회원가입, 인증, 로그인, 로그아웃, 탈퇴
 */
@Service
public class ProductService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final JwtService jwtService;

	private final ProductDao productDao;
	
	private final RegionDao regionDao;

	/**
	 * 상품 정보 생성자
	 * 
	 * @param jwtService productDao regionDao
	 */
	public ProductService(JwtService jwtService, ProductDao productDao, RegionDao regionDao) {

		this.jwtService = jwtService;
		this.productDao = productDao;
		this.regionDao = regionDao;

	}

	/**
	 * 전체 상품 조회 Service 메소드
	 * 
	 * @param HashMap
	 * @return List<Map>
	 * @exception 
	 */
	public List<HashMap<String, Object>> getProductList(HashMap<String, Object> product) throws BaseException {

		RegionDto regionDto = new RegionDto();
		regionDto.setUserIdx((Integer) product.get("userIdx"));
		regionDto.setRegionName((String) product.get("regionName"));
		
		/**
		 * 실제 존재하는 지역인지 체크 + 내가 등록한 지역인지 체크
		 */
		if (!this.regionDao.checkRegionName(regionDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION);
		}
	
		try {
			
			/**
			 * 내가 등록한 지역의 상품정보 조회 
			 */
			List<HashMap<String, Object>> productList = productDao.getProductList((String) product.get("regionName"));

			return productList;

		} catch (Exception exception) {

			logger.error("Error Message : ", exception);
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public List<HashMap<String, Object>> getProduct(int productIdx) throws BaseException {

		/**
		 * 존재하는 상품인지 확인
		 */
		if (!this.productDao.checkProduct(productIdx)) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}

		try {

			List<HashMap<String, Object>> getProduct = productDao.getProduct(productIdx);
			return getProduct;

		} catch (Exception exception) {

			logger.error("Error Message : ", exception);
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public void patchProductStatus(ProductDto productDto) throws BaseException {

		// 존재하는 상품인지 확인
		if (!this.productDao.checkProduct(productDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}

		// 내가 작성한 상품인지 확인
		if (!this.productDao.checkProductAccessUser(productDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_PRODUCT);
		}

		try {

			this.productDao.patchProductStatus(productDto);

		} catch (Exception exception) {

			logger.error("Error Message : ", exception);
			throw new BaseException(PATCH_PRODUCTS_FAIL);
		}
	}

	/**
	 * 내가 판매중인 상품 조회
	 */
	public List<HashMap<String, Object>> getProductSale(int productUserIdx) throws BaseException {

		try {
			
			
			List<HashMap<String, Object>> getProductSale = this.productDao.getProductSale(productUserIdx);
			
			return getProductSale;
			
		} catch (Exception exception) {

			logger.error("Error Message : ", exception);
			throw new BaseException(DATABASE_ERROR);
		}
	}

	/**
	 * 내가 판매완료한 상품 조회
	 */
	public List<HashMap<String, Object>> getProductComplete(int productUserIdx) throws BaseException {

		try {

			List<HashMap<String, Object>> getProductComplete = productDao.getProductComplete(productUserIdx);

			return getProductComplete;
		}

		catch (Exception exception) {

			logger.error("Error Message : ", exception);
			throw new BaseException(DATABASE_ERROR);
		}
	}

	/**
	 * 내 상품 등록하기
	 */
	public void createProduct(HashMap<String, Object> product) throws BaseException {
		
		/**
		 * 상품을 올리기 전 유저가 설정한 지역이 있는 지 확인
		 */
		if (!this.regionDao.checkRegionNull((Integer) product.get("productUserIdx"))) {
			throw new BaseException(DATABASE_ERROR_NO_EXIST_REGION);
		}

		try {
			
			/**
			 * 상품 등록 
			 */
			this.productDao.createProduct(product);
			
			Integer productIdx = (Integer) product.get("productIdx");
			
			product.put("productIdx", productIdx);
			
			/**
			 * 상품 이미지 등록
			 */
			
			System.out.println(product.get("image"));
			
			for(String s : (List<String>) product.get("image")) {
				
				System.out.println(s);

				product.put("image", s);
				this.productDao.createProductImg(product);
			}

		} catch (Exception exception) {
			
			logger.error("Error Message : ", exception);
			throw new BaseException(POST_PRODUCTS_FAIL);
		}
	}
	
    public void createInterestProduct(ProductDto productDto) throws BaseException {
    	
        try{
    		// 존재하는 상품인지 확인
    		if (!this.productDao.checkProduct(productDto.getProductIdx())) {
    			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
    		}
    		
    		// 이미 관심 등록한 상품인지 확인
    		if (!this.productDao.checkProductInterest(productDto)) {
    			throw new BaseException(POST_PRODUCTS_EXISTS_INTEREST);
    		}

            productDao.checkProductInterest(productDto);

        } catch(Exception exception){
        	
			logger.error("Error Message : ", exception);
            throw new BaseException(POST_PRODUCTS_FAIL_INTEREST);
        }
    }
    
    public List<HashMap<String, Object>> getProductInterest(int userIdx) throws BaseException{
    	
        try{
        	List<HashMap<String, Object>> getProductInterest = this.productDao.getProductInterest(userIdx);
        	
			if (getProductInterest.size() == 0) {
				throw new BaseException(GET_PRODUCTS_FAIL_INTEREST);
			}
        	
            return getProductInterest;
        }
        catch (Exception exception) {
        	
			logger.error("Error Message : ", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    public void patchProductInterest(HashMap<String, Object> interest) throws BaseException {
    	
		// 존재하는 상품인지 확인
		if (!this.productDao.checkProductInterestIdx((Integer) interest.get("productInterestIdx"))) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_INTEREST);
		}
		
		if (!this.productDao.checkProductInterestAccess(interest)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_INTEREST);
		}
    	
        try{

            productDao.patchProductInterest(interest);

        } catch(Exception exception){
        	
			logger.error("Error Message : ", exception);
            throw new BaseException(PATCH_PRODUCTS_FAIL_INTEREST);
        }
    }
    
    public void patchProductSaleStatus(ProductDto productDto) throws BaseException {
    	
		// 존재하는 상품인지 확인
		if (!this.productDao.checkProduct(productDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}
		
		// 내가 작성한 상품인지 확인
		if (!this.productDao.checkProductAccessUser(productDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_PRODUCT);
		}
    	
        try{

            productDao.patchProductSaleStatus(productDto);
            
        } catch(Exception exception){
        	
            throw new BaseException(PATCH_PRODUCTS_FAIL_SALE_STATUS);
        }
    }
    
	/**
	 * 내가 구매한 상품 목록 조회
	 * 
	 * @param int userIdx
	 * @return Map
	 */
    public List<HashMap<String, Object>> getProductPurchased(int userIdx) throws BaseException{
    	
    	List<HashMap<String, Object>> getProductPurchased = productDao.getProductPurchased(userIdx);
    	
		if (getProductPurchased.size() == 0) {
			throw new BaseException(GET_PRODUCTS_FAIL_PURCHASED);
		}
    	
        return getProductPurchased;
        
    }
    
    public void patchProductPurchasing(ProductDto productDto) throws BaseException{
    	
    	/**
    	 * 내가 올린 상품입니다.
    	 */
		if (this.productDao.checkIsMyProduct(productDto)) {
			throw new BaseException(DATABASE_ERROR_PRODUCT_IS_MINE);
		}
    	
    	/**
    	 *  존재하는 상품인지 확인
    	 */
		if (!this.productDao.checkProduct(productDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}
		
    	/**
    	 * 이미 팔린 상품인지 확인
    	 */
		if (this.productDao.checkIsPurchased(productDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_PRODUCT_ALREADY_PURCHASED);
		}
    	
        try{
        	/**
        	 * 상품 구매시 내 productStatus 값이 판매 완료 값인 0으로 변경
        	 */
        	productDao.patchProductPurchasing(productDto);

        }
        
        catch (Exception exception) {
        	
        	logger.error("Error Message", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    
}
