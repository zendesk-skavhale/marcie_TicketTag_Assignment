# Zendesk assignment (marcie-ticket-service)
This is a application providing functionalities to list tickets, add tag in ticket, Remove tag from ticket.


## Features
- List Tickets: Fetches all the available tickets from Zendesk.
- Add Tag to Ticket: Allows adding a tag to a specific ticket.
- Delete/Remove tag from specific ticket: Allows deleting a tag from specific ticket.


## Prerequisite
The application uses the Zendesk API for its functionalities. In order to run this application, you need to have access to the Zendesk API.
- you should have mvn installed
- you should have java 17 version installed




## Getting Started
If you have access to the Zendesk API, you will need to have the following details:

- Zendesk Subdomain
- Zendesk Username
- Zendesk Password/API token
After you get these details, set the following environment variables in your system environment (Environment Variables in Intellij Idea):

- export SUBDOMAIN=your_zendesk_subdomain
- export USERNAME=your_zendesk_username
- export PASSWORD=your_zendesk_password
- Replace your_zendesk_subdomain, your_zendesk_username, and your_zendesk_password with your actual Zendesk subdomain, username and password/API token respectively.

Ensure that you've properly set these environment variables before running the application.


## Running the application
After setting the environment variables, you can run your service using your preferred method. For example, if you're running the service using a main method or from a packaged jar, you may use:

run maven project

- mvn compile
- mvn clean package
- mvn clean install

- mvn spring-boot:run

Then you are good to run project.

Endpoints
The service exposes the following endpoints for interaction:

- GET /api/v2/tickets: List all the tickets.
- PUT /api/v2/tickets/{ticket_id}/tags: Add a tag to the ticket with the given id.
- GET /api/v2/tickets/{ticket_id}/tags: Delete a tag from the ticket with the given id.