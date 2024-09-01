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
import com.mzc.stc.caa.carrot.user.model.InterestDto;
import com.mzc.stc.caa.carrot.user.model.ProductDto;
import com.mzc.stc.caa.carrot.user.model.ReviewDto;
import com.mzc.stc.caa.carrot.user.service.ProductInterestService;
import com.mzc.stc.caa.carrot.user.service.ProductReviewService;
import com.mzc.stc.caa.carrot.user.service.ProductService;
import com.mzc.stc.caa.carrot.utils.JwtService;

/**
 * 상품 관심목록 처리를 위한 Controller
 * 
 * @author 김경민
 * @version 1.0, 유저 동네 인증, 현재위치 조회
 */
@RestController
@RequestMapping("/interests")
public class ProductInterestController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ProductInterestService productInterestService;

	private final JwtService jwtService;

	public ProductInterestController(JwtService jwtService, ProductInterestService productInterestService) {
		this.productInterestService = productInterestService;
		this.jwtService = jwtService;
	}

	/**
	 * 관심 상품 등록 control 메소드
	 * 
	 * @param productIdx
	 * @return String
	 * @exception
	 */
	// Path-variable
	@ResponseBody
	@PostMapping("/{productIdx}")
	public BaseResponse<String> createReview(@PathVariable("productIdx") int productIdx) {
		
		/**
		 * 관심목록 등록할 상품 PK 입력
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
			
			InterestDto interestDto = new InterestDto();
			
			interestDto.setProductIdx(productIdx);
			interestDto.setUserIdx(userIdxByJwt);

			this.productInterestService.createInterest(interestDto);

			return new BaseResponse<>("상품을 성공적으로 관심목록에 저장하였습니다.");

		} catch (BaseException exception) {

			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 관심 상품 조회 control 메소드
	 * 
	 * @param
	 * @return
	 * @exception
	 */
	// Path-variable
	@ResponseBody
	@GetMapping("")
	public BaseResponse<List<HashMap<String, Object>>> getReview() {

		try {

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			List<HashMap<String, Object>> getReview = this.productInterestService.getInterestList(userIdxByJwt);

			return new BaseResponse<>(getReview);

		} catch (BaseException exception) {

			return new BaseResponse<>(GET_INTERESTS_FAIL);
		}
	}

	/**
	 * 관심 목록 상품 삭제 control 메소드
	 * 
	 * @param
	 * @return
	 * @exception
	 */
	@ResponseBody
	@DeleteMapping("{productIdx}")
	public BaseResponse<String> patchReviewStatus(@PathVariable("productIdx") int productIdx) {
		
		/**
		 * 관심목록 해제할 상품 PK 입력
		 */
		if (productIdx == 0) {
			return new BaseResponse<>(POST_PRODUCTS_PRODUCTIDX_EMPTY);
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
			
			InterestDto interestDto = new InterestDto();
			interestDto.setProductIdx(productIdx);
			interestDto.setUserIdx(userIdxByJwt);
			
			this.productInterestService.patchInterestStatus(interestDto);

			return new BaseResponse<>("관심 목록 상품 해제 성공");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

}
