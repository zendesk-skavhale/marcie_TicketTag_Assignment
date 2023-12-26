package com.zendesk.marcie.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Ticket {

    public String url;
    public Integer id;

    public Date created_at;
    public Date updated_at;

    public List<String> tags;

}

