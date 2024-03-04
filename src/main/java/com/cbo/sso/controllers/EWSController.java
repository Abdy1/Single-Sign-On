package com.cbo.sso.controllers;


import com.cbo.sso.services.EWSAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EWSController {

    @Autowired
    private EWSAuthenticationService ewsAuthenticationService;

    @GetMapping("/redirect-to-outlook")
    public ResponseEntity<String> redirectToOutlook() throws Exception {
        // Implement logic to interact with EWS
        String result = ewsAuthenticationService.doEWSOperation();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/send-message")
    public void sendMessage() throws Exception {
        ewsAuthenticationService.sendEmail();

    }
}

