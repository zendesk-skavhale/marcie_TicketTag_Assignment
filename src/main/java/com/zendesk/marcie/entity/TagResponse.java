package com.zendesk.marcie.entity;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TagResponse {
    public Integer id;

    public Date created_at;
    public Date updated_at;
    List<String> tags;
}
