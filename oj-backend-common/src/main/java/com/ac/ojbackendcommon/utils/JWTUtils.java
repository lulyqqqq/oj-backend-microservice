package com.ac.ojbackendcommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: JWTUtils
 * @author: mafangnian
 * @date: 2023/10/14 14:40
 */
public class JWTUtils {

    protected static final long MILLIS_SECOND = 1000L;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;


    /**
     * token:
     *   header: Authorization
     *   secret: 02XDAC
     *   expireTime: 60
     *   tokenPrefix: "Bearer "
     * 令牌自定义头文件
     */
    private String Header;

    /**
     * token秘钥
     */
    private static final String TOKEN_SECRET = "02XDAC";

    /**
     * token过期时间
     * TOKEN的有效期1小时（S）
     */
    private static final int EXPIRE_TIME = 60;

    /**
     * token前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    // 生成Token
    public static  String createToken(Map params) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET) //加密方式
                .setExpiration(new Date(currentTime + EXPIRE_TIME * MILLIS_MINUTE)) //过期时间戳
                .addClaims(params)
                .compact();
    }


    /**
     * 获取Token中的claims信息
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token).getBody();
    }


    /**
     * 是否有效 true-有效，false-失效
     */
    public static boolean verifyToken(String token) {

        if (StringUtils.isEmpty(token)) {
            return false;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            //获得用户角色
            Object userRole = claims.get("userRole");
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 获取请求token
     *
     * @param token
     * @return token
     */
    public static String getToken(String token)
    {
        if (StringUtils.isNotEmpty(token) && token.startsWith(TOKEN_PREFIX))
        {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }
}
