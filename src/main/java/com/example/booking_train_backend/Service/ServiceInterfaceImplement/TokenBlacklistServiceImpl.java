package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.Service.ServiceInterface.TokenBlacklistService;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate; //redisTemplate duoc dung de doc ghi du lieu tu Redis
    private static final String BLACKLIST_PREFIX = "blacklisted_token:"; // Prefix duoc dung de phan biet token da duoc dua vao blacklist hay chua
    @Autowired
    public TokenBlacklistServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addToBlacklist(String token) {
        // Lưu token với timeToLive là thời gian sống còn lại của token
        long timeToLive = extractRemainingSeconds(token);
        // dua ttl vao blacklist
        // 'revoked' dung de danh dau token nay da bi thu hoi
        // timeToLive dung de dam bao rang token se bi xoa sau khi het han
        // 'TimeUnit.SECONDS' dung de chi dinh timeToLive la giay
        // cau lenh co nghia SET key value EX 3600 trong redis , EX=Expire in seconds va do redis dinh nghia
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "revoked", timeToLive, TimeUnit.SECONDS);
    }

    @Override
    public boolean isBlacklisted(String token) {
        // Kiem tra token da ton tai trong blacklist chua
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
    // ham tinh thoi gian song con lai cua token
    private long extractRemainingSeconds(String token) {
        try {
            // phan tich token de lay thoi gian song
            SignedJWT signedJWT = SignedJWT.parse(token); // phan tich chuoi jwt
            Date expirationTimeToken = signedJWT.getJWTClaimsSet().getExpirationTime();

            long expirationTime = expirationTimeToken.toInstant().getEpochSecond(); // lay thoi gian het han va chuyen thanh epoch seconds
            long nowTime = Instant.now().getEpochSecond(); // lay thoi gian hien tai
            long remaining = expirationTime - nowTime; // tinh thoi gian con lai
            // neu con song co nghia thoi gian het han lon hon thoi gian hien tai thi tra ve thoi gian con lai
            return remaining > 0 ? remaining : 0;
        } catch (Exception e) {
            //  truong hop khong lay duoc timeToLive, luc nay set mac dinh la 3600 giay de sau 3600 thi token se bi xoa khoi redis
            log.error("Không lấy được TTL từ token. Dùng mặc định 3600 giây.", e);
            return 3600;

        }
    }
}

