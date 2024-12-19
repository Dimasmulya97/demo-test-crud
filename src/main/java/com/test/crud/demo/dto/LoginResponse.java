package com.test.crud.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String username;
    private String nama;
    private String accessToken;
    private String tokenType;
    private int expiresIn;
    private String role;
}
