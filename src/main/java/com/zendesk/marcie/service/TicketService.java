package com.zendesk.marcie.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.zendesk.marcie.entity.Root;
import com.zendesk.marcie.exception.NoDataAvailableException;

@Service
public class TicketService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String apiUsername;

    @Autowired
    private String apiPassword;

    @Value("${zendesk.subdomain}")
    private String baseUrl;

    public Root getTicketData() throws NoDataAvailableException {
        HttpHeaders headers = createHeaders(apiUsername, apiPassword);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Root> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                entity,
                Root.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new NoDataAvailableException("Data not available for the specified ticket id");

        }
    }

    public Root getTicketById(int ticketId) throws NoDataAvailableException {
        try {
            HttpHeaders headers = createHeaders(apiUsername, apiPassword);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<Root> response = restTemplate.exchange(
                    baseUrl + ticketId,
                    HttpMethod.GET,
                    entity,
                    Root.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new NoDataAvailableException("Data not available for the specified ticket id");
            }

            return response.getBody();
        } catch (RestClientException ex) {
            throw new NoDataAvailableException("Error occured during the exchange.", ex);

        }
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
