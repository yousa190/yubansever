package com.yuban.shop.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类（适配JJWT 0.11.x及以上版本）
 */
public class JwtUtils {

    // 有效期为30天（43200000毫秒）
    public static final Long JWT_TTL =  Duration.ofDays(30).toMillis();
    // 建议使用256位以上的秘钥（Base64编码后）
    public static final String signKey = "comYubanshopYigou";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成JWT
     * @param subject token中存放的数据（建议为JSON格式字符串）
     * @return JWT字符串
     */
    public static String createJWT(String subject) {
        return createJWT(subject, JWT_TTL);
    }

    /**
     * 生成JWT
     * @param subject token数据
     * @param ttlMillis 过期时间（毫秒）
     * @return JWT字符串
     */
    public static String createJWT(String subject, Long ttlMillis) {
        return createJWT(getUUID(), subject, ttlMillis);
    }

    /**
     * 完整参数生成JWT
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 计算过期时间
        Date expDate = new Date(nowMillis + (ttlMillis == null ? JWT_TTL : ttlMillis));

        // 新版JWT构建方式（使用builder模式）
        return Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuer("sg")
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate)
                .compact();
    }

    /**
     * 生成加密密钥（新版推荐方式）
     */
    public static Key generalKey() {
        // 确保秘钥长度至少256位（32字节），否则会警告
        byte[] keyBytes;
        String key = signKey;
        if (key.getBytes().length < 32) {
            key = key + "yubanyousa2471998500"; // 补全长度
        }
        keyBytes = key.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 解析JWT
     */
    public static Claims parseJWT(String jwt) {
        try {
            Key secretKey = generalKey();
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("JWT解析失败", e);
        }
    }

    /**
     * 验证JWT有效性并获取载荷
     */
    public static Claims verifyJWT(String jwt) {
        try {
            return parseJWT(jwt);
        } catch (Exception e) {
            return null; // 无效JWT返回null
        }
    }

    public static void main(String[] args) {
        // 生成JWT示例
        String token = createJWT("user:123");
        System.out.println("生成的JWT: " + token);

        // 解析JWT示例
        try {
            Claims claims = parseJWT(token);
            System.out.println("解析结果:");
            System.out.println("  id: " + claims.getId());
            System.out.println("  subject: " + claims.getSubject());
            System.out.println("  过期时间: " + claims.getExpiration());
        } catch (Exception e) {
            System.err.println("解析错误: " + e.getMessage());
        }
    }
}