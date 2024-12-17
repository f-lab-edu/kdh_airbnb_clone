package com.airbnb_clone.backend.cryptoKey;

import com.auth0.jwt.algorithms.Algorithm;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaAlgorithm {
    private static RsaAlgorithm instance;
    private Algorithm algorithm;

    private RsaAlgorithm() {
        System.out.println("RSAKey Created!");
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
            algorithm = Algorithm.RSA256(publicKey, privateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RsaAlgorithm getInstance() {
        if (instance == null) {
            instance = new RsaAlgorithm(); // Create instance if it doesn't exist.
        }
        return instance;
    }

    public Algorithm getAlgorithm() {
        return this.algorithm;
    }
}