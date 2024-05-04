package org.ticket.booking.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticket.booking.entities.Ticket;
import org.ticket.booking.entities.Train;
import org.ticket.booking.entities.User;
import org.ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserBooking {
    private User user;
    private List<User> userList;
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String USER_PATH = "app/src/main/java/org/ticket/booking/localDB/users.json";

    public UserBooking(User user1) throws IOException {
        this.user = user1;
        userList = loadUsers();
    }

    public UserBooking() throws IOException {
        userList = loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USER_PATH);
        return mapper.readValue(users, new TypeReference<List<User>>() {});
    }



    public void saveUserList() throws IOException {
        File userFile = new File(USER_PATH);
        mapper.writeValue(userFile, userList);
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword())).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try{
            userList.add(user1);
            saveUserList();
            return Boolean.TRUE;
        } catch(IOException e) {
            return Boolean.FALSE;
        }
    }

    public void fetchBooking() {
        if(user!=null)
            user.printTickets();
        else
            System.out.println("\nSTATUS: FAILED (User must be logged in to view tickets)");
    }

    public Boolean cancelBooking(String ticketID) throws IOException {
        Optional<Ticket> ticket = user.getTicketsBooked().stream().filter(e -> Objects.equals(e.getTicketId(), ticketID)).findFirst();
        if(ticket.isPresent()) {
            user.getTicketsBooked().remove(ticket);
            saveUserList();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String src, String dest) throws IOException {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(src, dest);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Ticket bookTrainSeat(Train train, int row, int col, User user, String src, String dest) throws IOException {
        try {
            TrainService trainService = new TrainService();

            return trainService.bookSeat(train, row, col, user, src, dest);
        } catch (IOException e) {
            System.out.println("User must be logged in to book seat.");
            return null;
        }
    }
}
