package com.airbnb_clone.backend.cryptoKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.util.Objects;

public class Jwt_verification extends JwtForm{
    public static final String PHONENUM_CLAIM = "PHONENUM_CLAIM";
    public static final String CODE_CLAIM = "CODE_CLAIM";

    public Jwt_verification(String phoneNum, String code) {
        super();
        try {
            // Generate encryptedKey based on user info(phoneNumber and verification code)
            jwtString = JWT.create()
                    .withClaim(PHONENUM_CLAIM, phoneNum)
                    .withClaim(CODE_CLAIM, code)
                    //.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
            exception.printStackTrace();
        }
    }

    public static String getPhoneNum(String encryptedKey) {
        return Objects.requireNonNull(getClaims(encryptedKey)).get(PHONENUM_CLAIM).asString();
    }

    public static String getCode(String encryptedKey) {
        return Objects.requireNonNull(getClaims(encryptedKey)).get(CODE_CLAIM).asString();
    }
}
