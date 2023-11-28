package com.cbo.sso.response;

import com.cbo.sso.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class JwtResponse {
    private User user;
    private String accessToken;
}

