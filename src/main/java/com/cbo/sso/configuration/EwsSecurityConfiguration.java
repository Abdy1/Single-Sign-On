//
//package com.cbo.sso.configuration;
//import microsoft.exchange.webservices.data.credential.WebCredentials;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import microsoft.exchange.webservices.data.core.ExchangeService;
//import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
//
//import java.net.URI;
//
//@Configuration
//public class EwsSecurityConfiguration {
//
//    @Value("${ews.url}")
//    private String ewsUrl;
//
//    @Value("${ews.username}")
//    private String ewsUsername;
//
//    @Value("${ews.password}")
//    private String ewsPassword;
//
//    @Bean
//    public ExchangeService exchangeService() throws Exception {
//        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
//        service.setUrl(new URI(ewsUrl));
//        service.setCredentials( new WebCredentials(ewsUsername,ewsPassword));
//        return service;
//    }
//}
