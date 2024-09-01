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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.BaseResponse;
import com.mzc.stc.caa.carrot.user.dao.ProductDao;
import com.mzc.stc.caa.carrot.user.model.ProductDto;
import com.mzc.stc.caa.carrot.user.service.ProductService;
import com.mzc.stc.caa.carrot.utils.JwtService;

/**
 * 상품 정보 처리를 위한 Controller
 * 
 * @author 김경민
 * @version 1.1
 */
@RestController
@RequestMapping("/products")
public class ProductController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ProductService productService;

	private final JwtService jwtService;

	/**
	 * 상품 정보 처리 생성자 
	 * @param productService jwtService
	 * 
	 */
	public ProductController(ProductService productService, JwtService jwtService) {
		this.productService = productService;
		this.jwtService = jwtService;
	}

	/**
	 * 지역별 전체 상품 조회 control 메소드
	 * 
	 * @param String regionName
	 * @return List<HashMap>
	 * @exception 
	 */
	@ResponseBody
	@GetMapping("") // products?regionName=
	public BaseResponse<List<HashMap<String, Object>>> getProductList(@RequestParam String regionName) {
		
		/**
		 * 지역 정보 입력 누락
		 */
		if (regionName == null) {
			return new BaseResponse<>(POST_PRODUCTS_EMPTY_REGION);
		}

		/**
		 * 지역이름 입력 형식 오류
		 */
		if (regionName.length() < 2 || regionName.length() > 15) {
			return new BaseResponse<>(POST_PRODUCTS_INVALID_REGION);
		}
		
		try {

			/**
			 * 유저 인덱스 정보 추출 ( 로그인 여부 확인 ) 
			 */
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			HashMap<String, Object> product = new HashMap<>();

			product.put("regionName", regionName);
			product.put("userIdx", userIdxByJwt);

			/**
			 * 지역별 상품 정보 리스트 
			 */
			List<HashMap<String, Object>> getProductList = productService.getProductList(product);

			/**
			 * 조회된 결과가 없을 경우 실패처리
			 */
			if (getProductList.size() == 0) {
				return new BaseResponse<>(GET_PRODUCTS_FAIL_SALE);
			}

			return new BaseResponse<>(getProductList);

		} catch (BaseException exception) {

			logger.error("error Message : ", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 특정 상품 조회 control 메소드
	 * 
	 * @return BaseResponse<GetProduct>
	 */
	@ResponseBody
	@GetMapping("/{productIdx}")
	public BaseResponse<List<HashMap<String, Object>>> getProduct(@PathVariable("productIdx") int productIdx) {
		
		/**
		 * 상품 PK 정보 입력 누락
		 */
		if (productIdx == 0) {
			return new BaseResponse<>(POST_PRODUCTS_PRODUCTIDX_EMPTY);
		}

		try {

			/**
			 * 유저 인덱스 정보 추출 ( 로그인 여부 확인 ) 
			 */
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			/**
			 * 특정 상품 조회 
			 */
			List<HashMap<String, Object>> getProduct = this.productService.getProduct(productIdx);
			
			/**
			 * 조회된 결과가 없을 경우 실패처리
			 */
			if (getProduct.size() == 0) {
				return new BaseResponse<>(GET_PRODUCTS_FAIL);
			}
			
			return new BaseResponse<>(getProduct);

		} catch (BaseException exception) {

			logger.error("Error Message : ", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 특정 상품 삭제 control 메소드
	 * 
	 * @return BaseResponse<GetProduct>
	 */
	@ResponseBody
	@DeleteMapping("/{productIdx}/status")
	public BaseResponse<String> patchProductStatus(@PathVariable("productIdx") int productIdx) {

		try {

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			ProductDto productDto = new ProductDto();
			
			productDto.setProductUserIdx(userIdxByJwt);
			productDto.setProductIdx(productIdx);
			
			/**
			 * 내가 올린 상품의 상태값을 0으로 수정 = 삭제 API
			 */
			productService.patchProductStatus(productDto);

			return new BaseResponse<>("내가 올린 상품 삭제 성공");

		} catch (BaseException exception) {

			logger.error("Error Message : ", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 내가 판매중인 상품 조회 control 메소드
	 * 
	 * @return BaseResponse<GetProduct>
	 */
	// Path-variable
	@ResponseBody
	@GetMapping("/sale") 
	public BaseResponse<List<HashMap<String, Object>>> getProductSale() {
		try {
			
			int userIdxByJwt = jwtService.getUserIdx();
			
			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			/**
			 * 판매중인 상품 목록 조회
			 */
			List<HashMap<String, Object>> getProductSale = this.productService.getProductSale(userIdxByJwt);
			
			/**
			 * 판매중인 상품이 없는 경우
			 */
			if (getProductSale.size() == 0) {
				return new BaseResponse<>(GET_PRODUCTS_FAIL_SALE);
			}
			
			return new BaseResponse<>(getProductSale);
			
		} catch (BaseException exception) {
			
			logger.error("Error Message : ", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 내가 판매 완료한 상품 조회  control 메소드
	 * 
	 * @return BaseResponse<GetProductComplete>
	 */
	// Path-variable
	@ResponseBody
	@GetMapping("/complete") // (GET) 127.0.0.1:9000/product/:userIdx/complete
	public BaseResponse<List<HashMap<String, Object>>> getProductComplete() {
		try {
			
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 * 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			/**
			 * 판매완료한 상품 목록 조회
			 */
			List<HashMap<String, Object>> getProductComplete = this.productService.getProductComplete(userIdxByJwt);
			
			if (getProductComplete.size() == 0) {
				return new BaseResponse<>(GET_PRODUCTS_FAIL_COMPLETE);
			}
			
			return new BaseResponse<>(getProductComplete);
			
		} catch (BaseException exception) {
			
			logger.error("Error Message : ", exception);
			return new BaseResponse<>((exception.getStatus()));
		}
	}
	
	/**
	 * 상품 구매 control 메소드
	 * 
	 * @return BaseResponse<GetProductComplete>
	 */
	// Path-variable
	@ResponseBody
	@PutMapping("/purchasing") // 
	public BaseResponse <String> patchProductPurchasing(@RequestParam(required = false) Integer productIdx) {
		
		ProductDto productDto = new ProductDto();
		productDto.setProductIdx(productIdx);
		
		/**
		 * 상품 pk 값 입력 확인
		 */
		if (productDto.getProductIdx() == 0) {
			return new BaseResponse<>(POST_PRODUCT_EMPTY_PRODUCTIDX);
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
			
			productDto.setProductUserIdx(userIdxByJwt);
			productDto.setProductIdx(productDto.getProductIdx());
			
			/**
			 *  로그인 한 유저 pk 값을 상품 테이블에 구매자 pk로 삽입
			 */
			this.productService.patchProductPurchasing(productDto);
			
			return new BaseResponse<>("상품 구매 성공");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}
	
	
	

	/**
	 * 나의 구매 내역 조회  control 메소드
	 * 
	 * @return BaseResponse<GetProductComplete>
	 */
	// Path-variable
	@ResponseBody
	@GetMapping("/purchased") // (GET) 127.0.0.1:9000/products/:userIdx/purchased
	public BaseResponse<List<HashMap<String, Object>>> getProductPurchased() {
		
		try {
			
			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 *  로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			List<HashMap<String, Object>> getProductPurchased = this.productService.getProductPurchased(userIdxByJwt);
			
			return new BaseResponse<>(getProductPurchased);
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 상품 등록 API [POST] /products/:userIdx
	 * 
	 * @return BaseResponse<String>
	 */
	// Body
	@ResponseBody
	@PostMapping("")
	public BaseResponse<String> createProduct(@RequestBody ProductDto productDto) {
		
		HashMap<String, Object> product = new HashMap<>();
		
		/**
		 *  제목 입력
		 */
		if ((productDto.getTitle()) == null) {
			return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
		}
		
		product.put("title", productDto.getTitle());
		
		/**
		 *  제목 입력 형식 오류
		 */
		if (productDto.getTitle().length() < 2 || productDto.getTitle().length() > 30) {
			return new BaseResponse<>(POST_PRODUCTS_INVALID_TITLE);
		}
		
		/**
		 *  가격 입력
		 */
		if (productDto.getPrice() == null) {
			return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRICE);
		}
		
		product.put("price", productDto.getPrice());
		
		/**
		 *  내용 입력
		 */
		if (productDto.getContent() == null) {
			return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONTENT);
		}
		
		product.put("content", productDto.getContent());

		/**
		 *  카테고리 인덱스값 미입력
		 */
		if (productDto.getCategoryIdx() == 0) {
			return new BaseResponse<>(POST_PRODUCTS_EMPTY_CATEGORY);
		}	
		
		/**
		 *  카테고리 인덱스값 입력 형식 오류
		 */
		if (productDto.getCategoryIdx() < 1 || productDto.getCategoryIdx() > 19) {
			return new BaseResponse<>(POST_PRODUCTS_INVALID_CATEGORY);
		}
		
		product.put("categoryIdx", productDto.getCategoryIdx());	
	
		/**
		 *  상품 이미지 입력 확인
		 */
		if (productDto.getImage() == null) {
			return new BaseResponse<>(POST_PRODUCTS_EMPTY_IMAGE);
		}
		
		product.put("image", productDto.getImage());	
		
		logger.debug("product : {} ", product.get("image"));
		
		try {

			int userIdxByJwt = jwtService.getUserIdx();

			/**
			 *  로그아웃된 유저 (만료된 토큰 접근)인지 확인
			 */
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			product.put("productUserIdx", userIdxByJwt);	

			this.productService.createProduct(product);
			
			return new BaseResponse<>("제품 등록에 성공하였습니다");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 관심 상품 등록 API [POST] /products/:userIdx/:productIdx
	 * 
	 * @return BaseResponse<String>
	 */
	// Path-variable
	@ResponseBody
	@PostMapping("/{productIdx}/interest")
	public BaseResponse<String> createInterestProduct(@PathVariable("productIdx") int productIdx) {
		
		try {
			int userIdxByJwt = jwtService.getUserIdx();

			// 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			ProductDto productDto = new ProductDto();
			productDto.setProductUserIdx(userIdxByJwt);
			productDto.setProductIdx(productIdx);

			this.productService.createInterestProduct(productDto);

			return new BaseResponse<>("관심 등록에 성공하였습니다");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 관심 상품 목록 조회 
	 * 
	 * @return BaseResponse<String>
	 */
	// Path-variable
	@ResponseBody
	@GetMapping("/interest-list") 
	public BaseResponse<List<HashMap<String, Object>>> getProductInterest() {
		
		try {
			
			int userIdxByJwt = jwtService.getUserIdx();

			// 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}

			List<HashMap<String, Object>> getProductInterest = this.productService.getProductInterest(userIdxByJwt);
			
			if (getProductInterest.size() == 0) {
				return new BaseResponse<>(GET_PRODUCTS_FAIL_INTEREST);
			}
			
			return new BaseResponse<>(getProductInterest);
			
		} catch (BaseException exception) {
			
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 관심 목록 삭제 
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@PatchMapping("/interest/{productInterestIdx}/status")
	public BaseResponse<String> patchProductInterest(@PathVariable("productInterestIdx") int productInterestIdx) {
		
		try {
			int userIdxByJwt = jwtService.getUserIdx();

			// 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			HashMap<String, Object> interest = new HashMap<>();
			interest.put("productInterestIdx", productInterestIdx);
			interest.put("userIdx", userIdxByJwt);
			
			productService.patchProductInterest(interest);

			return new BaseResponse<>("성공");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 유저 판매 상품 상태 변경
	 * 
	 * @return BaseResponse<String>
	 */
	@ResponseBody
	@PatchMapping("/{productIdx}/{saleStatus}")
	public BaseResponse<String> patchProductSaleStatus(
			@PathVariable("productIdx") int productIdx, @PathVariable("saleStatus") int saleStatus) {
		
		try {
			if (saleStatus < 0 || saleStatus > 5) {
				return new BaseResponse<>(PATCH_PRODUCTS_FAIL_SALE_STATUS);
			}

			int userIdxByJwt = jwtService.getUserIdx();

			// 로그아웃된 유저 (만료된 토큰 접근)인지 확인
			if (jwtService.checkJwtTime() == 1) {

				System.out.println("jwt 시간확인 : " + jwtService.checkJwtTime());

				return new BaseResponse<>(INVALID_JWT);
			}
			
			
			
			ProductDto productDto = new ProductDto();

			productService.patchProductSaleStatus(productDto);

			return new BaseResponse<>("성공");
			
		} catch (BaseException exception) {
			
			return new BaseResponse<>((exception.getStatus()));
		}
	}

}
