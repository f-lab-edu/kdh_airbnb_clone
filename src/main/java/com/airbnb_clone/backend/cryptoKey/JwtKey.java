package com.airbnb_clone.backend.cryptoKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

// JwtKey used for identifying each verification code.
public class JwtKey {
    private String encryptedKey;    // Fundamentally jwt token.
    private static final String PHONENUM_CLAIM = "PHONENUM_CLAIM";
    private static final String CODE_CLAIM = "CODE_CLAIM";
    private static final Algorithm algorithm = RsaAlgorithm.getInstance().getAlgorithm();

    public JwtKey(String phoneNum, String code) {
        try {
            // Generate encryptedKey based on user info(phoneNumber and verification code)
            encryptedKey = JWT.create()
                    .withClaim(PHONENUM_CLAIM, phoneNum)
                    .withClaim(CODE_CLAIM, code)
                    //.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
            exception.printStackTrace();
        }
    }

    public String getKey() {
        return encryptedKey;
    }

    public static String getPhoneNum(String token) {
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim(PHONENUM_CLAIM)
                    .asString();
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String getCode(String token) {
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim(CODE_CLAIM)
                    .asString();
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
