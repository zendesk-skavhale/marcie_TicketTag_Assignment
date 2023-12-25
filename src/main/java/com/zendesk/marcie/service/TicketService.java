package com.zendesk.marcie.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zendesk.marcie.entity.Root;

@Service
public class TicketService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("apiUsername")
    private String apiUsername;

    @Autowired
    @Qualifier("apiPassword")
    private String apiPassword;

    @Autowired

    public Root getTicketData() {
        HttpHeaders headers = createHeaders(apiUsername, apiPassword);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Root> response = restTemplate.exchange(
                "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/",
                HttpMethod.GET,
                entity,
                Root.class);

        // Check the response status and body etc here...
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();// response.getBody();
        } else {
            // Handle non-200 status codes appropriately
            return null;
        }
    }

    public Root getTicketById(String ticketId) {
        HttpHeaders headers = createHeaders(apiUsername, apiPassword);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Root> response = restTemplate.exchange(
                "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/" + ticketId,
                HttpMethod.GET, entity, Root.class);
        System.out.println(response.getBody());

        return response.getBody();
    }

    public HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                setContentType(MediaType.APPLICATION_JSON);
            }
        };
    }
}
