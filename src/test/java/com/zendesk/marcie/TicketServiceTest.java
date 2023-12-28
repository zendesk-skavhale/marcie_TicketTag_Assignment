package com.zendesk.marcie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.web.client.RestClientException;
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

    @Value("${zendesk.subdomain}")
    private String baseUrl;

    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(ticketService, "baseUrl",
                "");
    }

    @Test
    public void testGetTicketData() throws NoDataAvailableException {
        Root mockRoot = new Root();

        ResponseEntity<Root> mockResponse = new ResponseEntity<>(mockRoot, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Root.class))).thenReturn(mockResponse);

        Root result = ticketService.getTicketData();

        assertEquals(mockRoot, result);
    }

    @Test
    public void testGetTicketById() throws NoDataAvailableException {
        int ticketId = 1;
        Root root = new Root();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Root.class))).thenReturn(new ResponseEntity<>(root, HttpStatus.OK));

        Root result = ticketService.getTicketById(ticketId);

        assertEquals(root, result);
    }

    @Test
    public void testGetTicketDataNegative() throws NoDataAvailableException {
        Root mockRoot = new Root();

        ResponseEntity<Root> mockResponse = new ResponseEntity<>(mockRoot, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Root.class))).thenReturn(mockResponse);

        assertThrows(NoDataAvailableException.class, () -> {
            ticketService.getTicketData();
        });
    }

    @Test
    public void testGetTicketByIdNegative() {
        int ticketId = 1;
        String username = "testUsername";
        String password = "testPassword";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(Root.class)))
                .thenThrow(new RestClientException("Error during exchange"));

        assertThrows(NoDataAvailableException.class, () -> {
            ticketService.getTicketById(ticketId);
        });
    }

}
