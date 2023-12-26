package com.zendesk.marcie.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.marcie.entity.TagRequest;
import com.zendesk.marcie.entity.TagResponse;
import com.zendesk.marcie.entity.Ticket;

@Service
public class TagService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("apiUsername")
    private String apiUsername;

    @Autowired
    @Qualifier("apiPassword")
    private String apiPassword;

    @Autowired
    private TicketService ticketService;

    @Value("${api.base.url}")
    private String baseUrl;

    @Autowired
    public TagService(RestTemplate restTemplate, ObjectMapper objectMapper) {

        this.restTemplate = restTemplate;
    }

    public TagService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TagResponse createTagInTicket(TagRequest tagRequest, int ticket_id) {

        String url = baseUrl + ticket_id + "/tags";

        HttpHeaders headers = createHeaders(apiUsername, apiPassword);
        HttpEntity<TagRequest> requestEntity = new HttpEntity<>(tagRequest, headers);
        ResponseEntity<TagResponse> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                TagResponse.class);

        return responseEntity.getBody();
    }

    public TagResponse deleteTagsFromTicket(@RequestBody Ticket ticket, int ticketId) {

        HttpHeaders headers = createHeaders(apiUsername, apiPassword);

        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);
        ResponseEntity<TagResponse> responseEntity = restTemplate.exchange(
                baseUrl + ticketId + "/tags",
                HttpMethod.DELETE,
                requestEntity,
                TagResponse.class);
        System.out.println("deletion operation : " + responseEntity.getBody());
        // }
        return responseEntity.getBody();
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
