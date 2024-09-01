package com.mzc.stc.caa.carrot.user.controller;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.BaseResponse;
import com.mzc.stc.caa.carrot.user.dao.ProductDao;
import com.mzc.stc.caa.carrot.user.model.ProductDto;
import com.mzc.stc.caa.carrot.user.model.ReviewDto;
import com.mzc.stc.caa.carrot.user.service.ProductReviewService;
import com.mzc.stc.caa.carrot.user.service.ProductService;
import com.mzc.stc.caa.carrot.utils.JwtService;

/**
 * 상품 리뷰 정보 처리를 위한 Controller
 * 
 * @author 김경민
 * @version 1.0, 유저 동네 인증, 현재위치 조회
 */
@RestController
@RequestMapping("/reviews")
public class ProductReviewController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ProductReviewService productReviewService;

	private final JwtService jwtService;

	/**
	 * 상품 리뷰 정보 처리 생성자 
	 * 
	 * @param productReviewService jwtService
	 * 
	 */
	public ProductReviewController(JwtService jwtService, ProductReviewService productReviewService) {
		this.productReviewService = productReviewService;
		this.jwtService = jwtService;
	}

	/**
	 * 상품을 통한 유저 리뷰 등록 control 메소드
	 * 
	 * @param
	 * @return
	 * @exception
	 */
	@ResponseBody
	@PostMapping("/{productIdx}")
	public BaseResponse<String> createReview(@PathVariable("productIdx") int productIdx,
			@RequestBody ReviewDto reviewDto) {
		
		/**
		 * 리뷰 등록할 상품 PK 입력
		 */
		if (productIdx == 0) {
			return new BaseResponse<>(POST_REVIEWS_PRODUCTIDX_EMPTY);
		}
		
		/**
		 * 리뷰 타입 입력 검사 
		 */
		if (reviewDto.getTypeList() == null) {
			return new BaseResponse<>(POST_USERS_REVIEW_TYPE_EMPTY);
		}

		try {

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			reviewDto.setProductIdx(productIdx);
			reviewDto.setReviewWriterIdx(userIdxByJwt);

			/**
			 * 리뷰 생성
			 */
			this.productReviewService.createReview(reviewDto);

			return new BaseResponse<>("리뷰를 성공적으로 작성하였습니다.");

		} catch (BaseException exception) {

			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 내가 구매한 상품 리뷰 조회 control 메소드
	 * 
	 * @param
	 * @return
	 * @exception
	 */
	@ResponseBody
	@GetMapping("/{productIdx}")
	public BaseResponse<HashMap<String, Object>> getReview(@PathVariable("productIdx") int productIdx) {
		
		/**
		 * 리뷰 조회할 상품 PK 입력
		 */
		if (productIdx == 0) {
			return new BaseResponse<>(POST_REVIEWS_PRODUCTIDX_EMPTY);
		}
		
		try {

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
	    	ReviewDto reviewDto = new ReviewDto();
	    	reviewDto.setProductIdx(productIdx);
	    	reviewDto.setReviewWriterIdx(userIdxByJwt);

			HashMap<String, Object> getReview = this.productReviewService.getProductReview(reviewDto);

			return new BaseResponse<>(getReview);

		} catch (BaseException exception) {

			logger.error("Error Message : ", exception);
			return new BaseResponse<>((GET_REVIEWS_UNSUCCESSFUL));
		}
	}

	/**
	 * 내가 작성한 상품 리뷰 삭제 control 메소드
	 * 
	 * @param
	 * @return
	 * @exception
	 */
	@ResponseBody
	@DeleteMapping("{productIdx}")
	public BaseResponse<String> patchReviewStatus(@PathVariable("productIdx") int productIdx) {
			
		/**
		 * 삭제할 리뷰 상품 PK 입력
		 */
		if (productIdx == 0) {
			return new BaseResponse<>(POST_REVIEWS_PRODUCTIDX_EMPTY);
		}
		try {
			
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			ReviewDto reviewDto = new ReviewDto();
			
			reviewDto.setProductIdx(productIdx);
			reviewDto.setReviewWriterIdx(userIdxByJwt);
			
			/**
			 * 리뷰 삭제 메소드
			 */
			this.productReviewService.deleteReview(reviewDto);

			return new BaseResponse<>("리뷰 삭제 성공");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

}
