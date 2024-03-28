package com.cbo.sso.controllers;


import com.cbo.sso.models.AWSCredential;
import com.cbo.sso.models.EWSSimpleSend;

import com.cbo.sso.services.ExchangeServiceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class EWSController {



    @Autowired
    private ExchangeServiceWrapper exchangeServiceWrapper;



    @PostMapping("/authenticate-ews")
    public ResponseEntity<String> redirectToOutlook( @RequestBody AWSCredential credential) throws Exception {
        try{
            System.out.println("authenticating");
            String username = credential.getUsername();
            String password = credential.getPassword();
            Boolean useHardcodedCredentials = credential.getDontUseMe();

            boolean authenticationResult;
            if (useHardcodedCredentials) {
                // Use hardcoded credentials
              authenticationResult =   exchangeServiceWrapper.authenticateWithHardcodedCredentials();
            } else {
              authenticationResult =  exchangeServiceWrapper.authenticateWithUserCredentials(username, password);
            }

            if (authenticationResult) {
                System.out.println("done");
                return ResponseEntity.ok("Authentication successful");
            } else {
                System.out.println("undone");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }

        } catch (Exception e){
            System.out.println("error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email.");
        }
    }



    @PostMapping("/send-to")
    public void sendTo(@RequestBody EWSSimpleSend ewsSimpleSend) throws Exception{
        System.out.println(ewsSimpleSend + "hi there im tryhna send this");
        if(ewsSimpleSend.getShortCircuit()){
           exchangeServiceWrapper.authenticateWithHardcodedCredentials();
        }
        exchangeServiceWrapper.sendEmail(ewsSimpleSend);
    }
}

