package com.zendesk.marcie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;


import com.zendesk.marcie.entity.TagRequest;
import com.zendesk.marcie.entity.TagResponse;
import com.zendesk.marcie.entity.Ticket;
import com.zendesk.marcie.service.TagService;



@ExtendWith(MockitoExtension.class) 
public class TagServiceTest {


    @Mock
    private RestTemplate restTemplate;

    @InjectMocks 
    private TagService tagService;

    @Captor
    private ArgumentCaptor<HttpEntity<TagRequest>> requestEntityCaptor;

    private String baseUrl = "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/";

    @BeforeEach  
     public void setup() {
    ReflectionTestUtils.setField(tagService, "baseUrl", "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/");
}

    @Test
    public void testCreateTagInTicket() {
        // Given
        int ticketId = 1;
        TagRequest tagRequest = new TagRequest();   
        TagResponse tagResponse = new TagResponse();   

        HttpHeaders headers = new HttpHeaders();  
        HttpEntity<TagRequest> requestEntity = new HttpEntity<>(tagRequest, headers);  

        when(restTemplate.exchange(   
                eq(baseUrl + ticketId + "/tags"),  
                eq(HttpMethod.PUT),  
                requestEntityCaptor.capture(),  
                eq(TagResponse.class))  
        ).thenReturn(new ResponseEntity<>(tagResponse, HttpStatus.OK));

        TagResponse result = tagService.createTagInTicket(tagRequest, ticketId);

        assertEquals(tagResponse, result); 
    
        assertEquals(tagRequest, requestEntityCaptor.getValue().getBody());
    }

    @Test 
    public void testDeleteTagsFromTicket() {
        // Given
        int ticketId = 123;
        Ticket ticket = new Ticket();
        TagResponse tagResponse = new TagResponse();
        
        HttpHeaders headers = new HttpHeaders(); // Create headers
        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);

        when(restTemplate.exchange(
                eq(baseUrl + ticketId + "/tags"),
                eq(HttpMethod.DELETE),
                requestEntityCaptor.capture(),
                eq(TagResponse.class))
        ).thenReturn(new ResponseEntity<>(tagResponse, HttpStatus.OK));

        TagResponse result = tagService.deleteTagsFromTicket(ticket, ticketId);

        assertEquals(tagResponse, result); 
    
        assertEquals(ticket, requestEntityCaptor.getValue().getBody());
    }

   
}


