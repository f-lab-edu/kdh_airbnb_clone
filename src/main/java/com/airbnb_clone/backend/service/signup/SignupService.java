package com.airbnb_clone.backend.service.signup;

import com.airbnb_clone.backend.JedisDb;
import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.ErrorType;
import com.airbnb_clone.backend.common.HashingService;
import com.airbnb_clone.backend.controller.signup.dto.SubmitFormRequestDto;
import com.airbnb_clone.backend.repository.UserEntity;
import com.airbnb_clone.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
public class SignupService {
    private final Jedis jedisPool = JedisDb.getInstance().getJedisPool();
    @Autowired
    private UserRepository userRepository;

    public String checkDuplicate(String phoneNumber) {
        checkPhoneNumberValidity(phoneNumber);
        return "success";
    }

    public String submitForm(SubmitFormRequestDto dto) {
        String phoneNumber = dto.getPhoneNumber();
        String name = dto.getName();
        String password = dto.getPassword();
        String birthDate = dto.getBirthDate();

        checkPhoneNumberValidity(phoneNumber);

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setName(name);
        newUserEntity.setBirthDate(birthDate);
        newUserEntity.setPhoneNumber(phoneNumber);
        newUserEntity.setPassword(HashingService.hash(password));

        userRepository.save(newUserEntity);

        String verifiedKey = "verified:" + phoneNumber + ":phoneNum";
        jedisPool.del(verifiedKey);
        return "success";
    }

    // Check phone number at every stage of sign up to prevent malicious request
    public void checkPhoneNumberValidity(String phoneNumber) {
        String verifiedKey = "verified:" + phoneNumber + ":phoneNum";
        boolean isVerified = jedisPool.get(verifiedKey) != null;

        if (isVerified) {
            UserEntity userEntityToFind = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (userEntityToFind != null) {
                throw new CommonException(ErrorType.SIGNUP_ALREADY_SIGNED_UP, null);
            }
        }
        else {
            throw new CommonException(ErrorType.CHECK_DUPLICATE_PHONE_NUMBER_EXPIRED, null);
        }
    }
}
