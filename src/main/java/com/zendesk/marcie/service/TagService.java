package com.zendesk.marcie.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.marcie.conf.CredentialsConfg;

public class TagService {

      private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CredentialsConfg credential;

     @Autowired
    public TagService(CredentialsConfg credential, RestTemplateBuilder restTemplateBuilder,ObjectMapper objectMapper) {
        this.credential = credential;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<String> getTicketData() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // encode username:password to base64 then set to header
        String auth = credential.getApiUsername()+ ":" + credential.getApiPassword();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(
                "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets",
                HttpMethod.GET, entity, String.class);
    }
    
}
