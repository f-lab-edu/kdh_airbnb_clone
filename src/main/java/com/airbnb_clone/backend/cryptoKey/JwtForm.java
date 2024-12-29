package com.airbnb_clone.backend.cryptoKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

public class JwtForm {
    protected String jwtString;    // Fundamentally jwt token.
    protected static final Algorithm algorithm = RsaAlgorithm.getInstance().getAlgorithm();

    public String getJwt() {
        return jwtString;
    }

    public static Map<String, Claim> getClaims(String token) {
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaims();
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
