package com.zendesk.marcie.console_module;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zendesk.marcie.entity.Root;
import com.zendesk.marcie.entity.TagRequest;
import com.zendesk.marcie.entity.TagResponse;
import com.zendesk.marcie.entity.Ticket;
import com.zendesk.marcie.exception.NoDataAvailableException;
import com.zendesk.marcie.service.TagService;
import com.zendesk.marcie.service.TicketService;

@Component
public class TicketManager {

    private Scanner scanner;

    @Autowired
    private TagService tagService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TagRequest tagRequest;

    public TicketManager() {
        this.scanner = new Scanner(System.in);
    }

    public void start() throws NoDataAvailableException {
        while (true) {
            showMenu();
            System.out.print("Enter Option : ");
            int choice = scanner.nextInt();
            handleChoice(choice);
        }
    }

    private void handleChoice(int choice) throws NoDataAvailableException {

        switch (choice) {
            case 1:
                String leftAlignFormat = getLeftAlignFormat();
                try {

                    Root obj = ticketService.getTicketData();
                    obj.ticketList.stream().forEach(ticket -> {
                        System.out.format(leftAlignFormat, ticket.getId(), ticket.getCreated_at(),
                                ticket.getUpdated_at(), ticket.getTags());
                    });
                } catch (Exception e) {
                    System.out.println("Something went wrong..");
                    e.printStackTrace();
                }
                System.out.println("\n\n\n");
                break;

            case 2:
                try {
                    System.out.println("Enter ticket no in which you want to add Tag");
                    int ticket_no = scanner.nextInt();
                    System.out.println("Enter tag to add : ");
                    String tag = scanner.next();
                    List<String> list = new ArrayList<>();
                    list.add(tag);

                    tagRequest.setTags(list);
                    TagResponse obj = tagService.createTagInTicket(tagRequest, ticket_no);
                    if (obj != null && null != obj.getTags()) {
                        Root root = ticketService.getTicketById(ticket_no);
                        System.out.format(getLeftAlignFormat(), root.getTicket().getId(),
                                root.getTicket().getCreated_at(),
                                root.getTicket().getUpdated_at(), root.getTicket().getTags());

                    } else {
                        System.out.println("Someting went wrong to add tags");
                    }

                } catch (Exception e) {
                    System.out.println("Something went wrong..");
                }
                System.out.println("\n\n\n");
                break;

            case 3:
                System.out.println("Enter ticket no in which you want to delete Tag");
                int ticket_no = scanner.nextInt();
                System.out.println("Enter tag to delete : \n");
                String tag = scanner.next();

                Root rootObject = ticketService.getTicketById(ticket_no);
                Ticket ticket = rootObject.getTicket();
                if (ticket == null) {
                    System.out.println("Ticket " + ticket_no + " does not exist.");
                    break;
                }
                if (ticket.getTags() == null || !ticket.getTags().contains(tag)) {
                    System.out.println("\n Tag '" + tag + "' not found in Ticket " + ticket_no);
                    break;
                }
                List<String> list = new ArrayList<>();
                list.add(tag);
                ticket.setTags(list);

                tagService.deleteTagsFromTicket(ticket, ticket_no);
                System.out.format(getDelAlignFormat(), ticket_no, ticket.getTags());
                System.out.println();

                break;
            case 4:

                System.out.println("\nClosing application...");
                System.exit(0);

                break;

            default:
                break;
        }

    }

    private String getDelAlignFormat() {
        String leftAlignFormat = "| %-10s | %-30s %n";
        System.out.println("\n\n\n");
        System.out.format(
                "+------------+--------------------+%n");
        System.out.format(
                "| Ticket_No  |   Tags              %n");
        System.out.format(
                "+------------+--------------------+%n");
        return leftAlignFormat;
    }

    private String getLeftAlignFormat() {
        String leftAlignFormat = "| %-10s | %-30s | %-30s | %-70s |%n";
        System.out.println("\n\n\n");
        System.out.format(
                "+------------+--------------------------------+------------+------------+--------------------------------------------------------------+%n");
        System.out.format(
                "| Ticket_No         | Created at                    |  Updated_at       |   Tags                                                       |%n");
        System.out.format(
                "+------------+--------------------------------+------------+------------+--------------------------------------------------------------+%n");
        return leftAlignFormat;
    }

    private void showMenu() {
        String leftAlignFormat = "| %-10s | %-30s |%n";

        System.out.format("+------------+--------------------------------+%n");
        System.out.format("| Option     | Description                    |%n");
        System.out.format("+------------+--------------------------------+%n");
        System.out.format(leftAlignFormat, "1", "Show All Ticket");
        System.out.format(leftAlignFormat, "2", "Add Ticket Tag");
        System.out.format(leftAlignFormat, "3", "Delete Ticket Tag");
        System.out.format(leftAlignFormat, "4", "Exit");
        System.out.format("+------------+--------------------------------+%n");
        ;
    }

}
