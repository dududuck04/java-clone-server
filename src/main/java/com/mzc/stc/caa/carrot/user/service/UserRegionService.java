package com.mzc.stc.caa.carrot.user.service;

import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.user.dao.JwtDao;
import com.mzc.stc.caa.carrot.user.dao.RegionDao;
import com.mzc.stc.caa.carrot.user.dao.UserDao;
import com.mzc.stc.caa.carrot.user.model.RegionDto;
import com.mzc.stc.caa.carrot.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;

import java.util.HashMap;
import java.util.List;

// Service Create, Update, Delete 의 로직 처리
/**
 * 유저 지역 정보 CUD 를 위한 Service
 * 
 * @author 김경민
 * @version 1.0
 */
@Service
public class UserRegionService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final RegionDao regionDao;

	private final JwtDao jwtDao;

	private final UserDao userDao;

	private final JwtService jwtService;

	/**
	 * 유저 지역 생성자
	 * 
	 * @param RegionDao JwtService JwtDao UserDao
	 */
	public UserRegionService(RegionDao regionDao, JwtService jwtService, JwtDao jwtDao, UserDao userDao) {

		this.regionDao = regionDao;
		this.jwtService = jwtService;
		this.jwtDao = jwtDao;
		this.userDao = userDao;
	}

	/**
	 * 유저 동네 추가를 위한 control 메소드
	 * 
	 * @param RegionDto regionDto , (userIdx)
	 * @return int
	 * @exception
	 */
	public void patchRegion(RegionDto regionDto) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(regionDto.getUserIdx()) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(regionDto.getUserIdx())) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}
		

		logger.debug("count : {}", this.regionDao.getRegionCount(regionDto.getUserIdx()));

		/**
		 * 설정된 유효한 지역의 개수 확인 ( 최대 2개 )
		 */
		if (this.regionDao.getRegionCount(regionDto.getUserIdx()) < 2) {
			
			
			/**
			 * 위도 경도 값 중복 확인
			 */
			if (this.regionDao.checkRegionDetail(regionDto)) {
				throw new BaseException(PATCH_REGIONS_FAIL_CHECK_LATITUDE_LONGITUDE);
			}
			
			/**
			 * 지역 이름 값 중복 확인
			 */
			if (this.regionDao.checkRegionName(regionDto)) {
				throw new BaseException(PATCH_REGIONS_EXITS_NOW);
			}
			
				/**
				 * 과거에 설정한 적이 없다면 egion status 값이 0인 값을 새로운 지역 정보값으로 덮어 씌운다.
				 * 
				 * 설정한 적이 있다면 status 값만 변경해준다.
				 */
			
				if (this.regionDao.patchTargetRegion(regionDto) == 0) {
					
					this.regionDao.patchRegion(regionDto);
				}

			// region status 값이 0인 경우 그 값을 새로운 지역 정보값으로 덮어 씌운다.
		} else {

			logger.debug("count : {}", this.regionDao.getRegionCount(regionDto.getUserIdx()));
			throw new BaseException(GET_REGIONS_COUNT);
		}
	}

	public void patchRegionStatus(RegionDto regionDto) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(regionDto.getUserIdx()) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(regionDto.getUserIdx())) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}

		/**
		 * 존재하는 지역인지 확인
		 */
		if (!this.regionDao.checkRegionIdx(regionDto.getRegionIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION_ID);
		}

		/**
		 * 유저가 가지고 있는 지역의 개수 확인
		 */
		if (this.regionDao.getRegionCount(regionDto.getUserIdx()) == 1) {
			throw new BaseException(PATCH_REGIONS_FAIL_MIN);
		}

		/**
		 * 유저가 삭제 접근 가능한 지역인지 확인
		 */
		if (!this.regionDao.checkRegionAccess(regionDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REGION);
		}

		/**
		 * 동네 삭제
		 */
		if (this.regionDao.deleteRegionStatus(regionDto) == 0) {
			throw new BaseException(PATCH_REGIONS_FAIL);
		}

	}

	public void patchRegionAuth(RegionDto regionDto) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(regionDto.getUserIdx()) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(regionDto.getUserIdx())) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}

		/**
		 * 실제 존재하는 pk 값인지 확인
		 */
		if (!this.regionDao.checkRegionIdx(regionDto.getRegionIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION_ID);
		}

		/**
		 * 현재 내 동내로 설정된 곳만 인증 할 수 있음
		 */
		if (!this.regionDao.checkRegionNow(regionDto)) {
			throw new BaseException(PATCH_REGIONS_FAIL_NOW);
		}

		/**
		 * 내가 설정한 동네만 인증할 수 있다.
		 */
		if (!this.regionDao.checkRegionAccess(regionDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REGION);
		}

		/**
		 * 동네 인증
		 */
		if (this.regionDao.patchRegionAuth(regionDto) == 0) {
			throw new BaseException(PATCH_REGIONS_FAIL_AUTH);
		}

	}

	public void patchRegionNow(RegionDto regionDto) throws BaseException {

		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(regionDto.getUserIdx()) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}

		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(regionDto.getUserIdx())) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}

		/**
		 * 실제 존재하는 pk 값인지 확인
		 */
		if (!this.regionDao.checkRegionIdx(regionDto.getRegionIdx())) {
			throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION_ID);
		}

		/**
		 * 이미 대표 동네 설정된 지역인지 확인
		 */
		if (this.regionDao.checkRegionNow(regionDto)) {
			throw new BaseException(PATCH_REGIONS_EXITS_NOW);
		}

		/**
		 * 내가 설정한 동네만 인증할 수 있다.
		 */
		if (!this.regionDao.checkRegionAccess(regionDto)) {
			throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REGION);
		}

		/**
		 * 내 동네로 설정된 곳인지 확인 ( 만약 기존에 다른 지역을 nowStatus 로 등록했다면 그 지역 대신 현재 지역으로 변경한다. )
		 * 유저가 가지고 있는 지역의 개수 확인
		 */
		if (this.regionDao.getRegionCount(regionDto.getUserIdx()) == 2) {

			/**
			 * nowStatus 값이 1로 설정된 것이 있다면 우선 조회
			 */
			List<HashMap<String, Object>> getRegionResult = regionDao.getRegion(regionDto.getUserIdx());

			logger.debug("region result : {}", getRegionResult);

			// 가지고 있는 대표 지역의 pk 값 추출

			int currentNowRegionIdx = Integer.parseInt(String.valueOf(getRegionResult.get(0).get("regionIdx")));
			logger.debug("region nowStatus id result : {}", currentNowRegionIdx);

			//
			if (currentNowRegionIdx != regionDto.getRegionIdx()) {

				/**
				 * 만약 내가 설정하려는 지역이 아닌 다른 내 지역이 이미 대표 설정 되어있다면 그 지역 대신 내가 설정하려는 지역을 대표 지역으로 바꿔준다.
				 */
				if (this.regionDao.checkRegionNowByRegionId(currentNowRegionIdx)) {

					/**
					 * 대표 지역 제거
					 */
					this.regionDao.deleteRegionNowStatus(currentNowRegionIdx);

					/**
					 * 내가 설정하려는 지역 대표 설정
					 */
					if (this.regionDao.patchRegionNow(regionDto) == 0) {

						throw new BaseException(PATCH_REGIONS_FAIL_SET_NOW);
					}
				}

			}

		}
		
		if (this.regionDao.patchRegionNow(regionDto) == 0) {
			throw new BaseException(PATCH_REGIONS_FAIL_SET_NOW);
		}

	}

	public List<HashMap<String, Object>> getRegion(int userIdx) throws BaseException {
		
		/**
		 * 로그인 상태 확인
		 */
		if (this.jwtDao.checkLoginStatus(userIdx) == 0) {
			throw new BaseException(DATABASE_ERROR_USER_NOT_LOGIN);
		}
		
		/**
		 * 회원 탈퇴 여부 확인
		 */
		if (this.userDao.checkDeleteUser(userIdx)) {
			throw new BaseException(PATCH_USERS_DELETE_USER);
		}

		try {
			List<HashMap<String, Object>> getRegionResult = regionDao.getRegion(userIdx);

			System.out.println(getRegionResult);

			return getRegionResult;

		} catch (Exception exception) {

			logger.error("error message", exception);
			throw new BaseException(DATABASE_ERROR);
		}
	}

}