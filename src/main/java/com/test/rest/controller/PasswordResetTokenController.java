package com.test.rest.controller;

import com.test.model.dto.PasswordResetTokenDto;
import com.test.model.dto.UserDto;
import com.test.model.entity.PasswordResetToken;
import com.test.service.AuthService;
import com.test.service.MailService;
import com.test.service.PasswordResetTokenService;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Павел on 29.09.2016.
 */

@RestController
@RequestMapping("${route.users}")
public class PasswordResetTokenController {
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(HttpServletRequest request,
                                         @RequestParam Long userId,
                                         @RequestParam String email) throws Exception {
        try {
            mailService.sendMessage(request, userId, email);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public ResponseEntity<?> changePassword(@RequestParam(name = "id") Long userId,
                                            @RequestParam(name = "token") String token,
                                            Device device) throws Exception {
        UserDto userDto;
        try {
            passwordResetTokenService.isPasswordResetTokenValid(userId, token);
            userDto = userService.findOne(userId);
            if(userDto == null){
                return ResponseEntity.badRequest().build();
            }
            String authToken = authService.createToken(userDto.getUsername(), userDto.getPassword(), device);
            return new ResponseEntity<>(authToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/savePassword", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePassword(@RequestHeader(name = "Authorization") String token,
                                            @RequestParam String newPassword) throws Exception {
        try {
            passwordResetTokenService.saveNewPassword(newPassword);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        passwordResetTokenService.saveNewPassword(newPassword);
        return ResponseEntity.ok().build();
    }
}
