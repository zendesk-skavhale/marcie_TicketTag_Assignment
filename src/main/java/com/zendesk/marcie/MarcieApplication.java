package com.zendesk.marcie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.zendesk.marcie.console_module.TicketManager;

@SpringBootApplication
public class MarcieApplication implements CommandLineRunner {

	@Autowired
	TicketManager ticketManager;

	public static void main(String[] args) {
		SpringApplication.run(MarcieApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ticketManager.start();

	}

}
