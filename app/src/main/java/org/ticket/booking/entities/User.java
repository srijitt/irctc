package org.ticket.booking.entities;

import java.util.List;

public class User {
    private String name;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userID;

    public User(String name, String hashedPassword, List<Ticket> ticketsBooked, String userID) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = ticketsBooked;
        this.userID = userID;
    }

    public User() {  }

    public String getName() {
        return name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public String getUserID() {
        return userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userID) {
        this.userID = userID;
    }

    public void printTickets() {
        if(ticketsBooked.isEmpty()) {
            System.out.println("No tickets booked yet");
            return;
        }
        System.out.println("Tickets for: "+ name);
        for (int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }
}
