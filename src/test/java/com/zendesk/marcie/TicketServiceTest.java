package com.zendesk.marcie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.zendesk.marcie.entity.Root;
import com.zendesk.marcie.exception.NoDataAvailableException;
import com.zendesk.marcie.service.TicketService;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TicketService ticketService;

    private String baseUrl = "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/";
    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(ticketService, "baseUrl",
                "https://z3nzendeskcodingchallenge6305.zendesk.com/api/v2/tickets/");
    }

    @Test
    public void testGetTicketData() throws NoDataAvailableException {
        // Given
        Root root = new Root();
        // set your root's fields here...
        HttpHeaders headers = new HttpHeaders(); // Create headers
        headers = ticketService.createHeaders(username, password);
        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);
        // Mock restTemplate's exchange method
        when(restTemplate.exchange(
                eq(baseUrl),
                eq(HttpMethod.GET),
                eq(requestEntity),
                eq(Root.class))).thenReturn(new ResponseEntity<>(root, HttpStatus.OK));

        // When
        Root result = ticketService.getTicketData();

        // Then
        assertEquals(root, result); // Verify that returned Root object is as expected
    }

    @Test
    public void testGetTicketById() {
        // Given
        int ticketId = 1;
        Root root = new Root();
        // set your root's fields here...
        HttpHeaders headers = new HttpHeaders(); // Create headers
        headers = ticketService.createHeaders(username, password);

        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);

        // Mock the restTemplate exchange method
        when(restTemplate.exchange(
                eq(baseUrl + ticketId),
                eq(HttpMethod.GET),
                eq(requestEntity),
                eq(Root.class))).thenReturn(new ResponseEntity<>(root, HttpStatus.OK));

       
        Root result = ticketService.getTicketById(ticketId);

       
        assertEquals(root, result); 
    }

}
