package com.airbnb_clone.backend.service.signin;

import com.airbnb_clone.backend.JedisDb;
import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.ErrorType;
import com.airbnb_clone.backend.common.HashingService;
import com.airbnb_clone.backend.controller.signinout.dto.SigninRequestDto;
import com.airbnb_clone.backend.controller.signinout.dto.SigninResponseDto;
import com.airbnb_clone.backend.controller.signinout.dto.SignoutRequestDto;
import com.airbnb_clone.backend.cryptoKey.Jwt_signin;

import com.airbnb_clone.backend.repository.UserEntity;
import com.airbnb_clone.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Slf4j
@Service
public class SigninoutService {
    private final Jedis jedisPool = JedisDb.getInstance().getJedisPool();
    @Autowired
    private UserRepository userRepository;

    public SigninResponseDto signin(SigninRequestDto dto) {
        String phoneNumber = dto.getPhoneNumber();
        String password = dto.getPassword();

        log.info("phoneNumber2 : " + phoneNumber);
        log.info("password2 : " + password);

        Optional<UserEntity> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            UserEntity userEntityFound = user.get();

            if (!userEntityFound.getPassword().equals(HashingService.hash(password))) {
                throw new CommonException(ErrorType.SIGNIN_PASSWORD_NOT_MATCH, null);
            }

            Long id = userEntityFound.getId();
            String phoneNumber_u = userEntityFound.getPhoneNumber();
            String name = userEntityFound.getName();
            String accessToken = new Jwt_signin(id, phoneNumber_u, name, Jwt_signin.TYPE_NORMAL).getJwt();
            String refreshToken = new Jwt_signin(id, phoneNumber_u, name, Jwt_signin.TYPE_REFRESH).getJwt();

            String accessTokenKey = id + ":accessToken:" + accessToken;
            String refreshTokenKey = id + ":refreshToken:" + refreshToken;

            jedisPool.set(accessTokenKey, accessToken);
            jedisPool.set(refreshTokenKey, refreshToken);

            return new SigninResponseDto(accessToken, refreshToken);
        }
        else {
            throw new CommonException(ErrorType.SIGNIN_USER_NOT_FOUND, null);
        }
    }

    public String signout(SignoutRequestDto dto) {
        String accessTokenFromDto = dto.accessToken();
        String refreshTokenFromDto = dto.refreshToken();

        log.info("accessTokenFromDto : " + accessTokenFromDto);
        log.info("refreshTokenFromDto : " + refreshTokenFromDto);

        String userId = Jwt_signin.getUserId(accessTokenFromDto);
        log.info("userId : " + userId);
        if (userId == null) {
            throw new CommonException(ErrorType.SIGNOUT_INVALID_TOKEN, null);
        }

        String accessTokenKey = userId + ":accessToken:" + accessTokenFromDto;
        String refreshTokenKey = userId + ":refreshToken:" + refreshTokenFromDto;

        jedisPool.del(accessTokenKey);
        jedisPool.del(refreshTokenKey);

        return "success";
    }
}
