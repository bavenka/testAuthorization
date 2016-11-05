package com.test.model.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * Created by Павел on 17.09.2016.
 */
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Collection<? extends GrantedAuthority> roles;

    public UserDto() {
    }
}
