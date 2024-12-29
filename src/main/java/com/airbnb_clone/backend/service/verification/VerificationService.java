package com.airbnb_clone.backend.service.verification;

import com.airbnb_clone.backend.JedisDb;
import com.airbnb_clone.backend.common.CommonResponse;
import com.airbnb_clone.backend.controller.verification.dto.GeneratedCodeResponse;
import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.ErrorType;
import com.airbnb_clone.backend.cryptoKey.Jwt_verification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import java.util.Random;

@Slf4j
@Service
public class VerificationService {
    private final Jedis jedisPool = JedisDb.getInstance().getJedisPool();
    private Jwt_verification jwtKey;    // camel case 로 스타일 변경할 것.
    public static final int MAX_REQUEST_COUNT_PER_DAY = 5;
    public static final int CODE_TTL = 180; // 3 minute
    public static final int COUNT_TTL = 86400; // 24 hours
    // 횟수보단 요청 간 시간 간격을 두는 게 좋음.

    public GeneratedCodeResponse generateCode(String phoneNumber) {

        String countKey = "auth:" + phoneNumber + ":count";
        String codeKey = "auth:" + phoneNumber + ":code";
        String jwtKey_ = "auth:" + phoneNumber + ":jwt";

        String countStr = jedisPool.get(countKey);
        log.info("countStr : " + countStr);
        int count = countStr == null ? 0 : Integer.parseInt(countStr);
        log.info("count : " + count);
        if (count >= MAX_REQUEST_COUNT_PER_DAY) {
            log.info("GENERATE_CODE_COUNT_LIMIT_EXCEEDED");
            throw new CommonException(ErrorType.GENERATE_CODE_COUNT_LIMIT_EXCEEDED, null);
        }

        String code = String.format("%06d", new Random().nextInt(999999));  // Ensure 6 digits
        jedisPool.setex(codeKey, CODE_TTL, code);   // Save code with codeKey as a key. Expires after CODE_TTL(180 seconds)
        jedisPool.incr(countKey);   // Increment countKey +1. when countKey is null, init as 1.
        jedisPool.expire(countKey, COUNT_TTL);  // Delete countKey after 24 hours

        // Jwt token used as an encrypted key for each verification process
        jwtKey = new Jwt_verification(phoneNumber, code);
        jedisPool.setex(jwtKey_, CODE_TTL, jwtKey.getJwt());    // Save encrypted key for verification

        // has to return both jwtKey and code at once with DTO.
        return new GeneratedCodeResponse(jwtKey.getJwt(), code);
    }

    public CommonResponse<String> verifyCode(String encryptedKey, String phoneNumber) {
        // return success when token is valid and meets all conditions.
        String jwtKey_ = "auth:" + phoneNumber + ":jwt";
        String jwtKeyFromRequest = jedisPool.get(jwtKey_);
        log.info("jwtKeyFromRequest : " + jwtKeyFromRequest);
        if (jwtKeyFromRequest == null) {
            // jwtKey is not valid or expired.
            throw new CommonException(ErrorType.VERIFICATION_KEY_EXPIRED, null);
        }
        if (!jwtKeyFromRequest.equals(encryptedKey)) {
            throw new CommonException(ErrorType.VERIFICATION_INVALID_JWT_KEY, null);
        }
        else {
            jwtKey = null;
            jedisPool.del(jwtKey_);

            // Save verified phoneNumber for signup later
            final int VERIFIED_TTL = 900; // 15 minutes
            String verifiedKey = "verified:" + phoneNumber + ":phoneNum";
            jedisPool.setex(verifiedKey, VERIFIED_TTL, "true"); // 인증 후 암호화된 키 값도 같이 저장할 필요가 있음. 중복가입 방지 목적

            return CommonResponse.success("Verified");  // void 를 반환하면 될 것 같음.
        }
    }
}
