package com.zendesk.marcie.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TagRequest {

    public List<String> tags;

}
