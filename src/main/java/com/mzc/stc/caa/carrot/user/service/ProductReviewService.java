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
import com.mzc.stc.caa.carrot.user.dao.ReviewDao;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.JwtInfoDto;
import com.mzc.stc.caa.carrot.user.model.ProductDto;
import com.mzc.stc.caa.carrot.user.model.RegionDto;
import com.mzc.stc.caa.carrot.user.model.ReviewDto;
import com.mzc.stc.caa.carrot.user.model.TempAuthCodeDto;
import com.mzc.stc.caa.carrot.user.model.UserDto;
import com.mzc.stc.caa.carrot.utils.JwtService;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

//Service Create, Update, Delete 의 로직 처리
/**
 * 유저 기본 인증 정보 CUD 를 위한 Service
 * 
 * @author 김경민
 * @version 1.0, 유저 회원가입, 인증, 로그인, 로그아웃, 탈퇴
 */
@Service
public class ProductReviewService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final JwtService jwtService;

	private final ProductDao productDao;
	
	private final ReviewDao reviewDao;

	/*
	 * 생성자
	 * 
	 * @param jwtService checkAuthDao saveJwtDao userBasicDao
	 */
	public ProductReviewService(JwtService jwtService, ProductDao productDao, ReviewDao reviewDao) {

		this.jwtService = jwtService;
		this.productDao = productDao;
		this.reviewDao = reviewDao;

	}

	public void createReview(ReviewDto reviewDto) throws BaseException {
		
		ProductDto productDto = new ProductDto();
		productDto.setProductIdx(reviewDto.getProductIdx());
		productDto.setProductUserIdx(reviewDto.getReviewWriterIdx());
		
		/**
		 * 리뷰를 달 상품이 실제 존재하는 상품인지 확인
		 */
		if (!this.productDao.checkProduct(reviewDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}
		
		/** 
		 * 판매 완료된 상품인지 확인
		 */
		if (!this.productDao.checkIsPurchased(reviewDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_PRODUCT_ALREADY_PURCHASED);
		}
		
		/** 
		 * 내가 산 상품인지 확인
		 */
		if (this.productDao.checkIsMyProduct(productDto)) {
			throw new BaseException(DATABASE_ERROR_PRODUCT_IS_NOT_MINE);
		}
		
        /**
         * 조회하는 상품의 거래 후기가 존재하는지 확인
         */
		if (reviewDao.getReview(reviewDto) != null) {
			throw new BaseException(GET_REVIEWS_ALREADY_EXIST);
		}
		
		
		try {
			
			for(Integer s : (List<Integer>) reviewDto.getTypeList()) {
				
				reviewDto.setType(s);
				logger.debug("type number : {}", s);
				
				this.reviewDao.createProductReview(reviewDto);
			}

		} catch (Exception exception) {
			
			logger.error("error message : ", exception);
			throw new BaseException(POST_REVIEWS_FAIL);
		}
	}
	
    public HashMap<String, Object> getProductReview(ReviewDto reviewDto) throws BaseException{
   
		ProductDto productDto = new ProductDto();
		productDto.setProductIdx(reviewDto.getProductIdx());
		productDto.setProductUserIdx(reviewDto.getReviewWriterIdx());
    	
		/**
		 * 조회할 상품이 실제 존재하는 상품인지 확인
		 */
		if (!this.productDao.checkProduct(reviewDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}
		
		logger.debug("message {} ", reviewDto);

        /**
         * 조회하는 상품의 거래 후기가 존재하는지 확인
         */
		if (reviewDao.getReview(reviewDto) == null) {
			throw new BaseException(GET_REVIEWS_FAIL);
		}
		
		/** 
		 * 내가 산 상품인지 확인
		 */
		if (this.productDao.checkIsMyProduct(productDto)) {
			throw new BaseException(DATABASE_ERROR_PRODUCT_IS_NOT_MINE);
		}
		
		HashMap<String, Object> getReview = reviewDao.getReview(reviewDto);
        
        logger.debug("getReview : {} ", getReview);
        
        List<String> typeList = reviewDao.getReviewType(reviewDto.getProductIdx());
        
        logger.debug("List type : {} ", typeList);
       

        getReview.put("type", typeList);
        
        return getReview;

    }
	
	

	public void deleteReview(ReviewDto reviewDto) throws BaseException {
		
		/**
		 * 존재하는 리뷰인지 확인
		 */
		
		logger.debug("reviewDto : {}", reviewDto);
		
		if (reviewDao.getReview(reviewDto) == null) {
			throw new BaseException(GET_REVIEWS_FAIL);
		}
		
		/**
		 * 내가 작성한 리뷰인지 확인
		 */
		if (!this.reviewDao.checkReviewAccess(reviewDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REVIEW_USER);
		}
		
		try {

			reviewDao.patchReviewStatus(reviewDto);

		} catch (Exception exception) {
			
        	logger.error("error message : ", exception);
			throw new BaseException(PATCH_REVIEWS_FAIL);
		}
	}

}