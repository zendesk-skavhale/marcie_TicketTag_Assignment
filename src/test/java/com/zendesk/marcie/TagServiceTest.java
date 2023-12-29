package com.zendesk.marcie;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${zendesk.subdomain}")
    private String subdomain;

    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(tagService, "subdomain", "");
    }

    @Test
    public void testCreateTagInTicket_Success() {
        // Given
        int ticketId = 1;
        TagRequest tagRequest = new TagRequest();
        TagResponse tagResponse = new TagResponse();

        when(restTemplate.exchange(
                anyString(), // match any URL
                eq(HttpMethod.PUT), // match PUT method precisely
                any(HttpEntity.class), // match any HttpEntity
                eq(TagResponse.class))) // match TagResponse.class precisely
                .thenReturn(new ResponseEntity<>(tagResponse, HttpStatus.OK));

        // No exceptions should be thrown
        assertDoesNotThrow(() -> {
            TagResponse returnedTagResponse = tagService.createTagInTicket(tagRequest, ticketId);
            assertEquals(tagResponse, returnedTagResponse);
        });
    }

    @Test
    public void testCreateTagInTicket() {
        int ticketId = 1;
        TagRequest tagRequest = new TagRequest();
        TagResponse tagResponse = new TagResponse();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TagRequest> requestEntity = new HttpEntity<>(tagRequest, headers);

        when(restTemplate.exchange(
                eq("https://" + subdomain + ".zendesk.com/api/v2/tickets/" + ticketId + "/tags"),
                eq(HttpMethod.PUT),
                eq(requestEntity),
                eq(TagResponse.class))).thenReturn(new ResponseEntity<>(tagResponse, HttpStatus.BAD_REQUEST));

        assertThrows(RuntimeException.class, () -> {
            tagService.createTagInTicket(tagRequest, ticketId);
        });
    }

    @Test
    public void testDeleteTagsFromTicket_Success() {
        // Given
        int ticketId = 123;
        Ticket ticket = new Ticket();
        TagResponse tagResponse = new TagResponse();

        // Mock the restTemplate to return OK status
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                ArgumentMatchers.<Class<TagResponse>>any()))
                .thenReturn(new ResponseEntity<>(tagResponse, HttpStatus.OK));

        // No exceptions should be thrown
        assertDoesNotThrow(() -> {
            TagResponse returnedTagResponse = tagService.deleteTagsFromTicket(ticket, ticketId);
            assertEquals(tagResponse, returnedTagResponse);
        });
    }

    @Test
    public void testDeleteTagsFromTicket() {
        int ticketId = 123;
        Ticket ticket = new Ticket();
        TagResponse tagResponse = new TagResponse();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);

        when(restTemplate.exchange(
                eq("https://" + subdomain + ".zendesk.com/api/v2/tickets/" + ticketId + "/tags"),
                eq(HttpMethod.DELETE),
                eq(requestEntity),
                eq(TagResponse.class))).thenReturn(new ResponseEntity<>(tagResponse, HttpStatus.BAD_REQUEST));

        assertThrows(RuntimeException.class, () -> {
            tagService.deleteTagsFromTicket(ticket, ticketId);
        });
    }

}
