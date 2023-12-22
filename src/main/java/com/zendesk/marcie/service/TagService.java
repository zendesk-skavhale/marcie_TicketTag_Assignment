package com.zendesk.marcie.service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.marcie.conf.CredentialsConf;
import com.zendesk.marcie.entity.Root;
import com.zendesk.marcie.entity.TagRequest;
import com.zendesk.marcie.entity.Ticket;
import com.zendesk.marcie.exceptions.RecordNotFoundException;

@Service
public class TagService {

      private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CredentialsConf credential;

     @Autowired
    public TagService(CredentialsConf credential, RestTemplateBuilder restTemplateBuilder,ObjectMapper objectMapper) {
        this.credential = credential;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<String> getTicketData() {

        String username = credential.getApiUsername();
        String password = credential.getApiPassword();

        HttpHeaders headers = createHeaders(username, password);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(
                "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets",
                HttpMethod.GET, entity, String.class);
    }

    public TagRequest createTagInTicket(TagRequest tagRequest, String ticket_id) {

        String url="https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/" + ticket_id + "/tags";
        
       String username = credential.getApiUsername();
        String password = credential.getApiPassword();

        HttpHeaders headers = createHeaders(username, password);
        HttpEntity<TagRequest> requestEntity = new HttpEntity<>(tagRequest, headers);
        ResponseEntity<TagRequest> responseEntity=restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                TagRequest.class
        );

        System.out.println(tagRequest);
        return responseEntity.getBody();
    }

     public String deleteTagsFromTicket(@RequestBody Ticket ticket, String ticketId) {
       Root root = getTicketById(ticketId);
       if (root == null) {
           throw new IllegalArgumentException("Ticket not found with id " + ticketId);
       }
        String username = credential.getApiUsername();
        String password = credential.getApiPassword();

        HttpHeaders headers = createHeaders(username, password);
        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);

       List<String> tags = root.ticket.tags;
       for (String tag : tags) {
           //restTemplate.exchange("https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets" + ticketId + "/tags", HttpMethod.DELETE, headers, String.class);
           restTemplate.exchange(
            "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/" + ticketId + "/tags",
            HttpMethod.DELETE,
            requestEntity,
            String.class
        );
        }
       return "deleted";}

       public Root getTicketById(String ticketId) {
        String username = credential.getApiUsername();
        String password = credential.getApiPassword();

        HttpHeaders headers = createHeaders(username, password);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Root> response = restTemplate.exchange(
                "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/" + ticketId,
                HttpMethod.GET, entity, Root.class);
        System.out.println(response.getBody());
 //        Ticket ticket = null;
 //        try {
 //            ticket = objectMapper.readValue(response.getBody(), Ticket.class);
 //        } catch (IOException e) {
 //            // handle the exception
 //        }
 
        return response.getBody();
    }

     public ResponseEntity<String> getPaginatedData(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "25") int perPage) {
        System.out.println("GetData called!!!");
        try {
           String username = credential.getApiUsername();
           String password = credential.getApiPassword();

            HttpHeaders headers = createHeaders(username, password);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response =  restTemplate.exchange(
                    "https://z3nzendeskcodingchallenge9654.zendesk.com/api/v2/tickets?page=" + page + "&per_page=" + perPage,
                    HttpMethod.GET, entity, String.class);
            if(response == null || response.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new RecordNotFoundException("No tickets found");
            }
            return response;

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    public HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }
}
