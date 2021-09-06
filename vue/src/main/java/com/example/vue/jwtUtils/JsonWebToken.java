package com.example.vue.jwtUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName JsonWebToken
 * @Description Jwt token生成解析
 * @Author lcz
 * @Date 2019/08/16 17:27
 */
@Slf4j
public class JsonWebToken {

    /**
     * 有效时间
     * 秘钥
     * 前缀
     */
    private static long EXPIRATION_TIME = 30 * 60 * 1000;

    private static final String SECRET = "vue-newland-scret-key";

    private static final String HEADER_STRING = "T-Authorization";

    private static final String TOKEN_PREFIX = "Bearer";

    /**
     * 登录认证成功之后签发token
     *
     * @param jsonObject 不敏感常用信息
     * @return token结果
     */
    public static String generateToken(JSONObject jsonObject) {
        /*token只加密不敏感信息*/

        /*Claim 内置字段提供选用
         * ISSUER = "iss"       签发人
         * SUBJECT = "sub"      主题
         * AUDIENCE = "aud"     受众
         * EXPIRATION = "exp"   过期时间
         * NOT_BEFORE = "nbf"   生效时间
         * ISSUED_AT = "iat"    签发时间
         * ID = "jti"           编号
         * */

        String jwt = Jwts.builder()
                .setIssuer("AAA")
                .setSubject("BBB")
                .setAudience("CCC")
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .setNotBefore(new Date(System.currentTimeMillis()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setId("DDD")
                /*
                 * 键值对   or   setPayload()
                 * */
                .addClaims(jsonObject.getInnerMap())
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
        /*简单前缀修饰   不记名token*/
        log.info("生成token数据：{}", jwt);
        return TOKEN_PREFIX + " " + jwt;
    }

    /**
     * token校验是否过期
     *
     * @param request 请求
     * @return 校验结果
     */
    public static ResponseEntity validateToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("未授权");
        }
        Claims claims;

        try {
           claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
        } catch (Exception e) {
            log.error("JsonWebToken validateToken error : {}", "授权码失效");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("授权码失效");
        }
        /*可以选择no-body*/
        /*return ResponseEntity.ok().build();*/
        return ResponseEntity.ok().body(claims);
    }

}
