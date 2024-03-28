package com.cbo.sso.models;

import lombok.Data;

import java.util.Map;
@Data
public class AWSCredential{
        private String username;
        private String password;
        private Boolean dontUseMe;
}
