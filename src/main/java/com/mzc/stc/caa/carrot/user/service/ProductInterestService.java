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
import com.mzc.stc.caa.carrot.user.dao.InterestDao;
import com.mzc.stc.caa.carrot.user.dao.JwtDao;
import com.mzc.stc.caa.carrot.user.dao.ProductDao;
import com.mzc.stc.caa.carrot.user.dao.RegionDao;
import com.mzc.stc.caa.carrot.user.dao.ReviewDao;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.InterestDto;
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
public class ProductInterestService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final JwtService jwtService;

	private final ProductDao productDao;
	
	private final InterestDao interestDao;

	/*
	 * 생성자
	 * 
	 * @param jwtService checkAuthDao saveJwtDao userBasicDao
	 */
	public ProductInterestService(JwtService jwtService, ProductDao productDao, InterestDao interestDao) {

		this.jwtService = jwtService;
		this.productDao = productDao;
		this.interestDao = interestDao;

	}

	public void createInterest(InterestDto interestDto) throws BaseException {
		
		/**
		 * 관심목록에 등록할 상품이 실제 존재하는 상품인지 확인
		 */
		if (!this.productDao.checkProduct(interestDto.getProductIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);
		}
		
		/**
		 * 이미 관심목록에 등록된 상품인지 확인
		 */
		if (this.interestDao.checkInterest(interestDto)) {
			throw new BaseException(DATABASE_ERROR_PRODUCT_ALREADY_EXIST);
		}
		
		
		/**
		 * 과거에 등록한 경험이 있는지 확인
		 */
		if (this.interestDao.checkInterestHistory(interestDto)) {
			this.interestDao.createAgainInterest(interestDto);
		}
		else {
			this.interestDao.createInterest(interestDto);
		}
	}
	
    public List<HashMap<String, Object>> getInterestList(int userIdx) throws BaseException{
    	
    	
        try{

            List<HashMap<String, Object>> getInterestList = interestDao.getInterestList(userIdx);
            
            return getInterestList;
        }
        catch (Exception exception) {
        	
        	logger.error("error message : ", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
	
	

	public void patchInterestStatus(InterestDto interestDto) throws BaseException {
		
		/**
		 * 존재하는 상품인지 확인
		 */
		if (!this.interestDao.checkInterest(interestDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_INTEREST);
		}
		
		/**
		 * 내가 등록한 관심목록인지 확인 리뷰인지 확인
		 */
		if (!this.interestDao.checkInterestAccess(interestDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REVIEW_USER);
		}
		
		try {

			this.interestDao.patchInterestStatus(interestDto);

		} catch (Exception exception) {
			
        	logger.error("error message : ", exception);
			throw new BaseException(PATCH_REVIEWS_FAIL);
		}
	}

}