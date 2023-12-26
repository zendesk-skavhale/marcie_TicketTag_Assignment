package com.zendesk.marcie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zendesk.marcie.entity.TagRequest;
import com.zendesk.marcie.entity.TagResponse;
import com.zendesk.marcie.entity.Ticket;
import com.zendesk.marcie.service.TagService;

@RestController
@RequestMapping("/api")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping(value = "CreateTicketTag/{ticketId}")
    public TagResponse createTicketTag(@RequestBody TagRequest tagRequest, @PathVariable int ticketId) {
        return tagService.createTagInTicket(tagRequest, ticketId);
    }

    @DeleteMapping(value = "/delete/{id}")
    public TagResponse deleteTicket(@RequestBody Ticket ticket, @PathVariable int id) {
        return tagService.deleteTagsFromTicket(ticket, id);
    }

   

}