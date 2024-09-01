package com.mzc.stc.caa.carrot.utils;


import com.mzc.stc.caa.carrot.config.BaseException;
import com.mzc.stc.caa.carrot.config.secret.Secret;
import com.mzc.stc.caa.carrot.user.model.JwtInfoDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static com.mzc.stc.caa.carrot.config.BaseResponseStatus.*;

@Service
public class JwtService {
	
	Key key = Keys.hmacShaKeyFor(Secret.JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(int userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String createJwt(HashMap<String, Object> result){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",result.get("userIdx"))
                .claim("phoneNumber", result.get("phoneNumber"))
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");    //X-ACCESS-TOKEN의 키에 대한 값을 가져옴 (헤더에 넣어주어야 한다)
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public int getUserIdx() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();   //
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parserBuilder()  //유효한 토큰인지 확인,  즉 로그인시 부여한 jwt 토큰인지 확인
                    .setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);       //유효한 토큰인지 확인 (만료 여부 알수 있음)
        }

        // 3. userIdx 추출  (위의 과정에서 문제가 없다면 수행)
        return claims.getBody().get("userIdx",Integer.class);
    }
    
    
    public String getPhoneNumber() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();   //
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parserBuilder()  //유효한 토큰인지 확인,  즉 로그인시 부여한 jwt 토큰인지 확인
                    .setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);       //유효한 토큰인지 확인 (만료 여부 알수 있음)
        }

        // 3. userIdx 추출  (위의 과정에서 문제가 없다면 수행)
        return claims.getBody().get("phoneNumber",String.class);
    }



////////////////////////////////////////////////////////////////////
    //토큰 만료 여부 확인 메서드  (만료시간이 지났을 경우 토큰을 만료시킴)
    public int checkJwtTime() throws BaseException {  //Claims
    	
        String accessToken = getJwt();   //
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }
    	
        Jws<Claims> claims;
        try {
            claims = Jwts.parserBuilder()  //유효한 토큰인지 확인,  즉 로그인시 부여한 jwt 토큰인지 확인
            		.setSigningKey(key).build().parseClaimsJws(accessToken);
            return 0;  //토큰이 만료가 되지 않았다면 0 반환
        }
        catch (Exception ignored){
        	System.out.println(ignored);
//            throw new BaseException(INVALID_JWT);
            return 1;  //토큰이 만료 되었다면 1 반환
        }

    }



}