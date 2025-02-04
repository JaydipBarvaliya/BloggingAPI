package com.blogging.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String authType;

}
