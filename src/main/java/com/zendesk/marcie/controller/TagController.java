package com.zendesk.marcie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zendesk.marcie.service.TagService;

@RestController
@RequestMapping("/api")
public class TagController {

    @Autowired
    private TagService tagService;

      @GetMapping(value="/getTicketData")
    public ResponseEntity<String> getTicketData(){
        return tagService.getTicketData();
    }
}