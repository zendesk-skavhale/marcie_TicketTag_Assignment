package com.zendesk.marcie.entity;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Root {
    public Ticket ticket;

    @JsonProperty("tickets")
    public ArrayList<Ticket> ticketList;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @Override
    public String toString() {
        return "Root [ticket=" + ticket + ", ticketList=" + ticketList + "]";
    }

}
