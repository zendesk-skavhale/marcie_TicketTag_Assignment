package com.zendesk.marcie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zendesk.marcie.entity.Root;
import com.zendesk.marcie.exception.NoDataAvailableException;
import com.zendesk.marcie.service.TicketService;

@RestController
@RequestMapping("/api")
public class TicketController {

  @Autowired
  private TicketService ticketService;

  @GetMapping(value = "/getTicketData")
  public Root getTicketData() throws NoDataAvailableException {
    return ticketService.getTicketData();
  }

  @GetMapping(value = "/getTicketById/{ticketId}")
  public Root getTicketById(@PathVariable int ticketId) {
    return ticketService.getTicketById(ticketId);
  }
}
