package com.test.service;

import com.test.model.entity.PasswordResetToken;
import com.test.model.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Павел on 04.10.2016.
 */
@Service
public interface PasswordResetTokenService {
    PasswordResetToken constructPasswordResetTokenForUser(User user);

    void isPasswordResetTokenValid(Long userId, String token) throws Exception;

    void saveNewPassword(String newPassword) throws Exception;
}
