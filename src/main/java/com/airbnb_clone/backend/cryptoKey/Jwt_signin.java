package com.airbnb_clone.backend.cryptoKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;
@Slf4j
public class Jwt_signin extends JwtForm{
    public static final String ID_CLAIM = "ID_CLAIM";
    public static final String PHONENUM_CLAIM = "PHONENUM_CLAIM";
    public static final String NAME_CLAIM = "NAME_CLAIM";
    public static final String TYPE_NORMAL = "TYPE_NORMAL";
    public static final String TYPE_REFRESH = "TYPE_REFRESH";

    public Jwt_signin(Long id, String phoneNum, String name, String type) {
        try {
            // 1 hour for normal, 1 year for refresh token
            long expiracyTimeMillis = type.equals(TYPE_NORMAL) ? 1000 * 60 * 60 : 1000L * 60 * 60 * 24 * 365;
            jwtString = JWT.create()
                    .withClaim(ID_CLAIM, id)
                    .withClaim(PHONENUM_CLAIM, phoneNum)
                    .withClaim(NAME_CLAIM, name)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiracyTimeMillis))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
            exception.printStackTrace();
        }
    }

    public static String getUserId(String encryptedKey) {
        return getClaims(encryptedKey).get(ID_CLAIM).toString();
    }
}
