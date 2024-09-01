package com.mzc.stc.caa.carrot.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : User Request 오류  (validation)
     */
    POST_USERS_EMPTY_PHONENUMBER(false, 2000, "전화번호를 입력해주세요."),
    POST_USERS_INVALID_PHONENUMBER(false, 2001, "전화번호 형식을 확인해주세요."),
    POST_lOGINS_EMPTY_AUTHCODE(false, 2002, "인증번호를 입력해주세요."),
    POST_lOGINS_INVALID_AUTHCODE(false, 2003, "인증번호는 네 자리 숫자만 입력 가능합니다."),
    POST_USERS_EMPTY_NICKNAME(false, 2004, "조회할 닉네임을 입력해주세요."),
    POST_USERS_FAIL_ALERT_SMS(false, 2005, "사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다."),
    POST_lOGINS_WRONG_AUTHCODE(false, 2006, "인증번호가 다릅니다."),
    EMPTY_TEMPAUTHCODEIDX(false, 2007, "tempAuthCodeIdx를 입력해주세요."),

    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "jWT가 변조되었거나 만료시간이 지났습니다. 다시 확인해 주세요."),
    INVALID_USER_JWT(false,2012,"권한이 없는 유저의 접근입니다."),
    FAIL_LOGIN_STATUS(false, 2013, "로그인 상태 변경에 실패하였습니다."),
    INVALID_USER (false, 2014 , "잘못된 로그인 유저의 접근입니다."),
    POST_USERS_FAIL_PHONENUMBER(false, 2015, "전화번호를 가져올 수 없습니다."),
    EXPIRED_AUTHCODE_STATUS(false, 2016, "사용된 인증번호 입니다."),
    BLOCK_USER_FAIL(false, 2017, "차단에 실패하였습니다."),
    

    /**
     * 2000 : Product Request 오류  (validation)
     */
    POST_PRODUCTS_EMPTY_TITLE(false, 2020, "제목을 입력해주세요."),
    POST_PRODUCTS_INVALID_TITLE(false, 2021, "제목의 형식, 길이를 확인해주세요."),
    POST_PRODUCTS_EMPTY_PRICE(false, 2022, "상품의 가격을 입력해주세요."),
    POST_PRODUCTS_INVALID_PRICE(false, 2023, "상품의 가격 형식, 길이를 확인해주세요."),
    POST_PRODUCTS_EMPTY_OFFER(false, 2024, "상품의 가격 제안 여부를 입력해주세요."),
    POST_PRODUCTS_INVALID_OFFER(false, 2025, "상품의 가격 제안 여부 형식을 확인해주세요. (0 또는 1)"),
    POST_PRODUCTS_EMPTY_SALE_STATUS(false, 2026, "상품의 판매 상태를 입력해주세요."),
    POST_PRODUCTS_INVALID_SALE_STATUS(false, 2027, "상품의 판매 상태 형식을 확인해주세요. (상품 등록의 경우 1: 거래중, 2: 나눔중 둘 중 하나만 가능)"),
    POST_PRODUCTS_EMPTY_CATEGORY(false, 2028, "카테고리 인덱스를 입력해주세요."),
    POST_PRODUCTS_INVALID_CATEGORY(false, 2029, "카테고리 형식, 범위를 확인해주세요. (1~17)"),
    POST_PRODUCTS_EMPTY_REGION(false, 2030, "지역 이름을 입력해주세요."),
    POST_PRODUCTS_INVALID_REGION(false, 2031, "지역 이름의 형식, 길이를 확인해주세요."),
    POST_PRODUCTS_EMPTY_IMAGE(false, 2032, "상품 사진을 등록해주세요."),
    POST_PRODUCTS_INVALID_IMAGE(false, 2033, "상품 사진의 형식을 확인해주세요."),
    POST_PRODUCTS_EMPTY_FIRST(false, 2034, "첫 번째 사진 여부를 입력해주세요."),
    POST_PRODUCTS_INVALID_FIRST(false, 2035, "첫 번째 사진 여부의 형식을 입력해주세요. 값은 1만 들어올 수 있습니다."),
    PATCH_PRODUCTS_INVALID_SALE_STATUS(false, 2036, "상품의 판매 상태 형식을 확인해주세요. (0~5)"),


    /**
     * 2000 : Region Request 오류  (validation)
     */
    POST_REGIONS_EMPTY_NAME(false, 2037, "지역 이름을 입력해주세요."),
    POST_REGIONS_INVALID_NAME(false, 2038, "지역 이름 형식을 확인해주세요."),
    POST_REGIONS_EMPTY_LATITUDE_LONGITUDE(false, 2039, "위도와 경도를 입력해주세요."),
    POST_REGIONS_INVALID_LATITUDE_LONGITUDE(false, 2040, "위도와 경도의 형식을 확인해주세요."),
    POST_REGIONS_EMPTY_ALERT(false, 2041, "키워드 알림 여부를 입력해주세요."),
    POST_REGIONS_INVALID_ALERT(false, 2042, "키워드 알림 여부의 형식을 확인해주세요. (0 또는 1)"),
    POST_PRODUCTS_PRODUCTIDX_EMPTY(false, 2043, "productIdx 값을 입력해 주세요"),
    POST_PRODUCTS_EMPTY_CONTENT(false, 2044, "상품의 내용을 입력해주세요."),
    /**
     * 2000 : Chat Request 오류  (validation)
     */
    POST_CHATS_EMPTY_MESSAGE(false, 2070, "보낼 메세지를 입력해주세요."),
    POST_CHATS_INVALID_MESSAGE(false, 2071, "메세지의 형식을 확인해주세요."),
    POST_CHATS_SAME_USER(false, 2072, "받는 사람을 확인해주세요."),
    POST_CHATS_CHECK_USER(false, 2073, "사용자를 입력해주세요"),

    /**
     * 2000 : Review Request 오류  (validation)
     */
    POST_REVIEWS_EMPTY(false, 2110, "후기를 입력해주세요."),
    POST_REVIEWS_INVALID(false, 2111, "후기의 내용 형식을 확인해주세요."),
    POST_REVIEWS_EMPTY_PREFERENCE(false, 2112, "거래 선호도를 입력해주세요."),
    POST_REVIEWS_INVALID_PREFERENCE(false, 2113, "거래 선호도의 형식을 확인해주세요. (1~3)"),
    POST_REVIEWS_EMPTY_MANNER(false, 2114, "매너 타입을 입력해주세요."),
    POST_REVIEWS_INVALID_MANNER(false, 2115, "매너 타입 형식을 확인해주세요. (1~8)"),
    POST_REVIEWS_PRODUCTREVIEWIDX_EMPTY(false, 2116, "productReviewIdx를 입력해주세요."),
    POST_REVIEWS_PRODUCTIDX_EMPTY(false, 2117, "productIdx를 입력해주세요."),
    POST_USERS_REVIEW_TYPE_EMPTY(false, 2118, "review Type을 선택해 주세요."),

    /**
     * 2000 : Post Request 오류  (validation)
     */
    POST_SYMPATHIES_INVALID(false, 2150, "공감 인덱스 형식을 확인해주세요. (1~7)"),
    POST_COMMENTS_EMPTY(false, 2151, "댓글 내용을 입력해주세요."),
    POST_COMMENTS_INVALID(false, 2152, "댓글 형식을 확인해주세요."),
    POST_COMMENTS_INVALID_IMAGE(false, 2153, "이미지 형식을 확인해주세요."),
    POST_COMMENTS_INVALID_REGION_NAME(false, 2154, "장소의 이름 형식을 확인해주세요."),
    POST_COMMENTS_INVALID_ADDRESS(false, 2155, "장소의 주소 형식을 확인해주세요."),

    /**
     * 2000 : KeyWord Request 오류  (validation)
     */
    POST_KEYWORDS_EMPTY_KEYWORD(false, 2050, "키워드를 입력해주세요.(예:자전거)"),
    GET_KEYWORDS_EMPTY_KEYWORD(false, 2051, "설정한 지역과 알림키워드에 해당하는 상품이 없습니다."),
    POST_KEYWORDS_OVER_KEYWORD(false, 2052, "키워드는 10글자 이하만 입력 가능합니다."),

    /**
     * 2000 : townActivity Request 오류  (validation)
     */
    POST_TOWN_ACTIVITY_EMPTY_TOPICNAME(false, 2100, "게시글 주제를 입력해주세요."),
    POST_TOWN_ACTIVITY_EMPTY_CONTENT(false, 2101, "게시글 내용을 입력해주세요."),
    POST_TOWN_ACTIVITY_OVER_CONTENT(false, 2102, "300글자 이하만 작성 가능합니다."),
    POST_TOWN_ACTIVITY_EMPTY_FIRST(false, 2103, "메인 이미지 상태 숫자를 입력해주세요."),
    POST_TOWN_ACTIVITY_DIFFERENT_FIRST(false, 2104, "이미지 수와 메인 이미지 상태 수는 같아야 합니다."),
    POST_PRODUCT_EMPTY_PRODUCTIDX(false, 2105, "상품 pk값을 입력해주세요."),

    /**
     * 3000 : user Response 오류 (validation)
     */
    POST_USERS_EXISTS_PHONENUMBER(false,3000,"이미 가입된 전화번호 입니다."),
    POST_USERS_EXISTS_NICKNAME(false,3001,"사용중인 닉네임 입니다."),
    NOT_EXIST_USER(false,3002,"입력하신 인증코드에 해당하는 사용자가 없습니다."),

    FAILED_TO_JOIN_CHECK(false,3003,"가입되지 않은 전화번호입니다. 가입을 먼저 진행해 주세요."),
    MODIFY_FAIL_AUTHCODE(false,3304,"인증코드에 해당하는 사용자가 없어서 인증코드를 변경하지 못했습니다."),

    LOGOUT_USER_JWT(false, 3005, "로그아웃된 상태입니다."),
    AUTO_LOGOUT_FAIL_USER(false,3006,"아직 유저의 jwt 토큰이 만료되지 않아 자동 로그아웃에 실패했습니다."),

    PATCH_USERS_DELETE_USER(false,3007,"이미 탈퇴된 계정입니다."),
    POST_USERS_BLOCKS_NICKNAME(false,3008,"이미 차단한 사용자입니다."),
    POST_USERS_REPORT_NICKNAME(false,3009,"이미 신고한 사용자입니다."),
    POST_USERS_REPORT_PRODUCT(false,3010,"이미 신고한 상품 게시글 입니다."),
    POST_USERS_HIDDEN_NICKNAME(false,3011,"이미 미노출된 등록된 사용자입니다."),
    WRONG_USERS_AUTHCODE(false,3012,"인증코드를 잘못 입력하셨습니다."),
    ALREADY_USERS_AUTHCODE(false,3012,"인증코드가 이미 전송되었습니다. 60초 후에 다시 보내주세요. "),
    POST_USERS_REPORT_TYPE_EMPTY(false,3013, "신고이유를 알려주세요. "),
    POST_USERS_ALREADY_FREE(false,3014,"이미 차단해제한 사용자입니다."),
    WRONG_USERS_REPORT_TYPE(false,3015,"신고 유형을 잘못 선택하셨습니다."),
    POST_USERS_DELETE_TYPE_EMPTY(false,3016, "탈퇴 이유를 알려주세요. "),
    GET_USERS_NOT_EXIST(false,3017,"존재하지 않는 유저입니다."),
    GET_USERS_ALREADY_REPORT(false,3018,"이미 같은 이유로 신고한 유저입니다."),

    /**
     * 3000 : product Response 오류 (validation)
     */
    POST_PRODUCTS_FAIL(false, 3020, "상품 등록에 실패했습니다."),
    GET_PRODUCTS_LIST_FAIL(false, 3021, "전체 상품 조회에 실패했습니다."),
    GET_PRODUCTS_FAIL(false, 3022, "상품 조회에 실패했습니다."),
    PATCH_PRODUCTS_FAIL(false, 3023, "상품 삭제에 실패했습니다."),
    GET_PRODUCTS_FAIL_SALE(true, 3024, "판매중인 상품이 없습니다."),
    GET_PRODUCTS_FAIL_COMPLETE(true, 3025, "거래완료된 상품이 없습니다."),
    GET_PRODUCTS_FAIL_HIDED(true, 3026, "숨겨진 상품이 없습니다."),
    GET_PRODUCTS_FAIL_PURCHASED(true, 3027, "구매내역이 없습니다."),
    PATCH_PRODUCTS_FAIL_SALE_STATUS(false, 3028, "판매상태 변경에 실패했습니다."),
    POST_PRODUCTS_FAIL_INTEREST(false, 3029, "관심목록 등록에 실패했습니다."),
    POST_PRODUCTS_EXISTS_INTEREST(false, 3030, "이미 관심목록에 등록된 상품입니다."),
    GET_PRODUCTS_FAIL_INTEREST(true, 3031, "관심상품이 없습니다."),
    PATCH_PRODUCTS_FAIL_INTEREST(false, 3032, "관심목록 취소에 실패했습니다."),

    /**
     * 3000 : category product Response 오류 (validation)
     */
    POST_CATEGORIES_FAIL(false, 3033, "관심 카테고리 등록에 실패했습니다."),
    GET_CATEGORIES_FAIL(true, 3034, "관심 카테고리가 존재하지 않습니다."),
    PATCH_CATEGORIES_FAIL(false, 3035, "관심 카테고리 취소에 실패했습니다."),

    /**
     * 3000 : region product Response 오류 (validation)
     */
    GET_REGIONS_COUNT(false, 3036, "이미 설정된 동네가 두 곳이 있습니다. 기존 동네를 삭제하고 설정을 진행해주세요."),
    POST_REGIONS_FAIL(false, 3037, "내 동네 설정에 실패했습니다."),
    GET_REGIONS_FAIL(false, 3038, "내 동네 조회에 실패했습니다. 설정된 동네가 없습니다."),
    PATCH_REGIONS_FAIL(false, 3039, "내 동네 삭제에 실패했습니다."),
    PATCH_REGIONS_FAIL_MIN(false, 3040, "이 동네를 삭제한다면 설정한 동네가 없습니다. 동네 삭제를 하려면 다른 동네를 추가해주세요."),
    PATCH_REGIONS_FAIL_AUTH(false, 3041, "내 동네 인증에 실패했습니다."),
    PATCH_REGIONS_FAIL_NOW(false, 3042, "현재 설정한 동네가 아닙니다. 동네를 현재 동네로 설정하고 동네 인증을 진행해주세요."),
    PATCH_REGIONS_EXITS_NOW(false, 3043, "이미 설정된 지역입니다."),
    PATCH_REGIONS_FAIL_SET_NOW(false, 3044, "현재 동네 설정에 실패했습니다."),
    PATCH_REGIONS_FAIL_CHECK_LATITUDE_LONGITUDE(false, 3046, "위도 경도 값이 중복되는 주소가 존재합니다."),

    /**
     * 3000 : lookup product Response 오류 (validation)
     */
    POST_LOOK_UPS_FAIL(false, 3045, "조회에 실패했습니다."),

    /**
     * 3000 : chat product Response 오류 (validation)
     */
    POST_CHATS_EXITS(false, 3070, "이미 존재하는 채팅방입니다."),
    POST_CHATS_FAIL(false, 3071, "채팅방 생성에 실패했습니다."),
    POST_CHATS_FAIL_MESSAGE(false, 3072, "메세지 전송에 실패했습니다."),
    GET_CHATS_FAIL(true, 3073, "채팅 목록이 비어있습니다."),
    GET_CHATS_FAIL_LIST(false, 3074, "채팅 목록 조회에 실패했습니다."),

    /**
     * 3000 : review product Response 오류 (validation)
     */
    POST_REVIEWS_FAIL(false, 3110, "거래후기 등록에 실패했습니다."),
    GET_REVIEWS_FAIL(true, 3111, "거래후기가 존재하지 않습니다."),
    PATCH_REVIEWS_FAIL(false, 3112, "거래후기 취소에 실패했습니다"),
    GET_REVIEWS_ALREADY_EXIST(false, 3113, "해당 상품 거래후기를 이미 작성하셨습니다."),
    GET_REVIEWS_UNSUCCESSFUL(true, 3114, "거래후기 조회에 실패하였습니다."),
    POST_INTEREST_FAIL(false, 3110, "관심목록 등록에 실패했습니다."),
    GET_INTERESTS_FAIL(false, 3111, "관심목록 조회에 실패했습니다."),

    /**
     * 3000 : post product Response 오류 (validation)
     */
    POST_SYMPATHIES_EXITS(false, 3150, "이미 공감한 게시글입니다."),
    POST_SYMPATHIES_FAIL(false, 3151, "공감에 실패했습니다."),
    GET_SYMPATHIES_FAIL(true, 3152, "공감이 존재하지 않습니다."),
    PATCH_SYMPATHIES_FAIL(false, 3153, "공감 취소에 실패했습니다."),
    POST_COMMENTS_FAIL(false, 3154, "댓글 작성에 실패했습니다."),
    GET_COMMENTS_FAIL(true, 3155, "댓글이 존재하지 않습니다."),
    PATCH_COMMENTS_FAIL(false, 3156, "댓글 삭제에 실패했습니다."),

    /**
     * 3000 : Keyword product Response 오류 (validation)
     */
    POST_KEYWORDS_EXISTS_KEYWORD(false,3050,"이미 추가된 키워드에요"),
    PATCH_KEYWORDS_ACTIVE_REGION(false,3051,"이미 알림 동네가 활성화된 상태입니다."),
    PATCH_KEYWORDS_INACTIVE_REGION(false,3052,"이미 알림 동네가 비활성화된 상태입니다."),

    /**
     * 3000 : Gagther product Response 오류 (validation)
     */
    POST_KEYWORDS_EXISTS_GATHER(false,3090,"이미 모아보기에 추가된 사용자에요"),

    /**
     * 3000 : townActivity product Response 오류 (validation)
     */
    POST_TOWNACTIVITY_EXISTS(false,3130,"이미 작성하셨던 게시글 입니다."),


    /**
     * 3000 : Search product Response 오류 (validation)
     */
    POST_SEARCHS_CHECK(false,3170,"이미 등록한 검색어입니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    /**
     * 4000 : user Database, Server 오류
     */
    DATABASE_ERROR_CREATE_USER(false, 4002, "신규 유저 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CREATE_REGION(false, 4003, "유저의 동네 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_PHONENUMBER(false, 4004, "전화번호 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_CHECK_NICKNAME(false, 4005, "닉네임 중복 검사에 실패하였습니다."),

    SAVE_FAIL_jwt(false,4006,"JWT 토큰 저장에 실패하였습니다."),
    DATABASE_ERROR_NOT_EXISTS_USER(false, 4007, "사용자 정보를 DB에서 조회하지 못했습니다. "),
    DATABASE_ERROR_FAIL_LOGIN(false, 4008, "로그인 처리에 실패 하였습니다."),

    DATABASE_ERROR_FAIL_LOGOUT(false, 4009, "로그아웃에 처리에 실패 하였습니다."),
    DATABASE_ERROR_MODIFY_FAIL_USER_NICKNAME(false, 4010, "사용자 닉네임 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_FAIL_USER_IMAGE(false, 4011, "사용자 이미지 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_USER_INFO(false, 4012, "프로필 조회에 실패하였습니다."),

    DATABASE_ERROR_DELETE_CHECK_USER(false, 4013, "회원탈퇴 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_DELETE_USER(false, 4014, "회원 탈퇴(유저 비활성화)에 실패하였습니다."),
    DATABASE_ERROR_DELETE_USER_REGION(false, 4015, "회원 탈퇴(동네 비활성화)에 실패하였습니다."),

    DATABASE_ERROR_BLOCK_CHECK_USER(false, 4016, "사용자 차단 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_BLOCK_CANCELL_USER(false, 4017, "사용자 차단 해제에 실패했습니다. 닉네임을 확인해 주세요."),

    DATABASE_ERROR_BLOCK_USER(false, 4018, "사용자 차단에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_BLOCK_USER_INFO(false, 4019, "차단한 사용자 프로필 조회에 실패하였습니다."),

    DATABASE_ERROR_REPORT_USER(false, 4020, "사용자 신고에 실패했습니다."),
    DATABASE_ERROR_REPORT_CHECK_USER(false, 4021, "사용자 신고 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_REPORT_PRODUCT(false, 4022, "상품 게시글 신고에 실패했습니다."),
    DATABASE_ERROR_REPORT_CHECK_PRODUCT(false, 4023, "상품 게시글 신고 여부 확인에 실패하였습니다."),

    DATABASE_ERROR_HIDDEN_USER(false, 4024, "미노출 사용자 추가에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_HIDDEN_CHECK_USER(false, 4025, "미노출 사용자 추가 확인에 실패하였습니다."),
    DATABASE_ERROR_HIDDEN_CANCELL_USER(false, 4026, "미노출 사용자 해제에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_HIDDEN_USER_INFO(false, 4027, "미노출 사용자 조회에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4028, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4029, "비밀번호 복호화에 실패하였습니다."),

    /**
     * 4000 : product Database, Server 오류
     */
    DATABASE_ERROR_NOT_EXIST_REGION(false, 4030, "등록한 지역이 아닙니다."),
    DATABASE_ERROR_NOT_EXIST_PRODUCT(false, 4031, "존재하지 않는 상품입니다."),
    DATABASE_ERROR_NOT_ACCESS_PRODUCT(false, 4032, "(접근불가) 상품을 올린 사용자가 아닙니다. 사용자를 확인해주세요."),
    DATABASE_ERROR_NOT_EXIST_INTEREST(false, 4033, "존재하지 않는 관심 목록 입니다."),
    DATABASE_ERROR_NOT_ACCESS_INTEREST(false, 4034, "(접근불가) 관심 목록에 해당하는 사용자가 아닙니다. 사용자를 확인해주세요."),
    DATABASE_ERROR_NO_EXIST_REGION(false, 4030, "유저가 설정한 동네가 없습니다."),
    DATABASE_ERROR_PRODUCT_ALREADY_EXIST(false, 4031, "이미 관심목록에 등록된 상품입니다."),
    
    
    /**
     * 4000 : category Database, Server 오류
     */
    DATABASE_ERROR_NOT_EXIST_CATEGORY(false, 4035, "존재하지 않는 카테고리 인덱스입니다."),
    DATABASE_ERROR_NOT_ACCESS_CATEGORY(false, 4036, "(접근불가) 관심 카테고리에 해당하는 사용자가 아닙니다. 사용자를 확인해주세요."),

    /**
     * 4000 : region Database, Server 오류
     */
    DATABASE_ERROR_NOT_EXIST_REGION_ID(false, 4037, "존재하지 않는 지역 인덱스입니다."),
    DATABASE_ERROR_NOT_ACCESS_REGION(false, 4038, "(접근불가) 지역을 설정한 사용자가 아닙니다. 사용자를 확인해주세요."),
    DATABASE_ERROR_UPDATE_JWT(false, 4039, "JWT 업데이트 오류"),
    DATABASE_ERROR_PRODUCT_ALREADY_PURCHASED(false, 4040, "이미 팔린 상품입니다."),
    DATABASE_ERROR_PRODUCT_IS_MINE(false, 4041, "내가 올린 상품은 구매할 수 없습니다."),
    DATABASE_ERROR_PRODUCT_IS_NOT_MINE(false, 4041, "내가 구매한 상품이 아닙니다."),

    /**
     * 4000 : chat Database, Server 오류
     */
    DATABASE_ERROR_NOT_SAME(false, 4100, "(접근불가) 채팅방에 해당하는 사용자가 아닙니다."),
    DATABASE_ERRORS_NOT_EXITS_CHAT(false, 4101, "존재하지 않는 채팅방입니다."),
    DATABASE_ERRORS_NOT_EXITS_USER(false, 4102, "존재하지 않는 사용자입니다."),
    DATABASE_ERROR_NOT_ACCESS_USER(false, 4103, "접근할 수 없는 사용자 입니다. 사용자를 확인해주세요."),

    /**
     * 4000 : review Database, Server 오류
     */
    DATABASE_ERROR_NOT_ACCESS_REVIEW(false, 4140, "거래에 참여하지 않은 사용자가 존재합니다. 사용자를 확인해주세요."),
    DATABASE_ERROR_NOT_EXITS_REVIEW(false, 4141, "존재하지 않는 거래후기 입니다."),
    DATABASE_ERROR_NOT_ACCESS_REVIEW_USER(false, 4142, "(접근불가) 후기를 남긴 사용자가 아닙니다. 사용자를 확인해주세요."),

    /**
     * 4000 : post Database, Server 오류
     */
    DATABASE_ERROR_NOT_EXITS_POST(false, 4180, "존재하지 않는 게시글입니다."),
    DATABASE_ERROR_NOT_EXITS_SYMPATHIES(false, 4181, "사용자가 공감한 내역이 없습니다."),
    DATABASE_ERROR_NOT_EXITS_COMMENT(false, 4182, "존재하지 않는 댓글입니다."),
    DATABASE_ERROR_NOT_ACCESS_COMMENT(false, 4183, "(접근불가) 댓글을 남긴 사용자가 아닙니다. 사용자를 확인해주세요."),

    DATABASE_ERROR_USER_LOGOUT(false, 4030, "로그아웃 상태 확인에 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    //keyword 리소스
    DATABASE_ERROR_CREATE_KEYWORD(false, 4080, "키워드를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_KEYWORD(false, 4081, "키워드 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_DELETE_KEYWORD(false, 4082, "알림 키워드 삭제에 실패하였습니다."),

    DATABASE_ERROR_ACTIVE_REGION_STATUS(false, 4083, "알림 동네 활성화에 실패하였습니다."),
    DATABASE_ERROR_INACTIVE_REGION_STATUS(false, 4084, "알림 동네 비활성화에 실패하였습니다."),
    DATABASE_ERROR_CHECK_ACTIVE_REGION_STATUS(false, 4085, "알림 동네 활성 상태 확인 실패하였습니다."),

    DATABASE_ERROR_GET_ALERT_KEYWORD(false, 4086, "설정한 알림 키워드 조회에 실패하였습니다."),
    DATABASE_ERROR_GET_ALERT_REGION(false, 4087, "알림 설정한 동네 조회에 실패하였습니다."),

    DATABASE_ERROR_GET_ALERT_PRODUCT(false, 4088, "키워드 알림 상품 조회에 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    //gather 리소스
    DATABASE_ERROR_CHECK_GATHER(false, 4120, "모아보기한 사용자 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_CREATE_GATHER(false, 4121, "모아보기할 사용자를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_DELETE_GATHER(false, 4122, "모아보기 취소에 실패하였습니다."),



    DATABASE_ERROR_GET_GATHER_USER(false, 4123, "모아보기한 사용자 조회에 실패하였습니다."),
    DATABASE_ERROR_GET_GATHER_PRODUCT(false, 4124, "모아보기한 상품 조회에 실패하였습니다."),

    /**
     * 4000 :MytownActivity Database, Server 오류
     */
    DATABASE_ERROR_CREATE_TOWN_ACTIVITY(false, 4180, "동네 생활 게시글을 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CREATE_TOWN_ACTIVITY_IMAGE(false, 4181, "동네 생활 게시글 등록 과정에서 이미지를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_TOWN_ACTIVITY_INFO(false, 4182, "동네 생활 게시글을 불러오는데 실패하였습니다."),
    DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_TOPICNAME(false, 4183, "게시글 주제 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_IMAGE(false, 4184, "이미지 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_CONTENT(false, 4185, "게시글 내용 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_DELETE_TOWN_ACTIVITY(false, 4186, "게시글 삭제시 오류가 발생하였습니다."),
    DATABASE_ERROR_DELETE_TOWN_ACTIVITY_IMAGE(false, 4188, "게시글 이미지 삭제시 오류가 발생하였습니다."),
    DATABASE_ERROR_CHECK_TOWN_ACTIVITY(false, 4187, "게시글 중복 검사에 실패하였습니다."),

    /**
     * 4000 :search Database, Server 오류
     */
    DATABASE_ERROR_CHECK_SEARCH(false, 4200, "검색어 중복 등록 검사에 실패하였습니다."),
    DATABASE_ERROR_CREATE_SEARCH(false, 4201, "검색어 등록에 실패하였습니다."),
    DATABASE_ERROR_GET_RECENT_SEARCH(false, 4202, "최근 검색어 조회에 실패하였습니다."),
    DATABASE_ERROR_DELETE_RESENT_SEARCH(false, 4203, "최근 검색어 삭제에 실패하였습니다."),
    DATABASE_ERROR_DELETE_ALL_RESENT_SEARCH(false, 4204, "최근 검색어 전체 삭제에 실패하였습니다."),
    DATABASE_ERROR_GET_SEARCH_PRODUCT(false, 4205, "검색에 맞는 중고거래 글을 불러오지 못하였습니다."),
    DATABASE_ERROR_GET_SEARCH_TOWN_ACTIVITY(false, 4206, "검색에 맞는 동네생활 글을 불러오지 못하였습니다."),
    DATABASE_ERROR_GET_SEARCH_USER(false, 4207, "검색에 맞는 사용자들을 불러오지 못하였습니다."),
    DATABASE_ERROR_GET_RELATION_SEARCH(false, 4208, "연관 검색어 조회에 실패하였습니다."),
    DATABASE_ERROR_ALREADY_LOGOUT(false, 4209, "이미 로그아웃된 유저입니다."),
	DATABASE_ERROR_USER_NOT_LOGIN(false, 4210, "로그인 설정을 확인해주세요.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}