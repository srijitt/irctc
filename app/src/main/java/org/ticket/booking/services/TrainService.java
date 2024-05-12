package org.ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticket.booking.entities.Ticket;
import org.ticket.booking.entities.Train;
import org.ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String TRAINS_PATH = "app/src/main/java/org/ticket/booking/localDB/trains.json";

    public TrainService() throws IOException {
        trainList = loadTrains();
    }

    public List<Train> loadTrains() throws IOException {
        File trains = new File(TRAINS_PATH);
        return mapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String src, String dest) throws IOException {
        return loadTrains().stream().filter(train-> isValid(train, src, dest)).collect(Collectors.toList());
    }

    public boolean isValid(Train train, String src, String dest) {
        List<String> stations = train.getStations();

        int sourceIndex = stations.indexOf(src.toLowerCase());
        int destIndex = stations.indexOf(dest.toLowerCase());

        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }

    public static String generateID() {
        final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom secureRandom = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(randomIndex));
        }
        return sb.toString();
    }

    public Ticket bookSeat(Train train, int row, int col, User user, String src, String dest) throws IOException {
        try {
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(row).size()) {
                if (seats.get(row).get(col) == 0) {
                    seats.get(row).set(col, 1);
                    train.setSeats(seats);
                    addTrain(train);
                    return new Ticket(generateID(), user.getUserID(), src, dest, train); // Booking successful
                } else {
                    return null; // Seat is already booked
                }
            } else {
                return null; // Invalid row or seat index
            }
        } catch(IOException e) {
            return null;
        }
    }

    public void updateTrain(Train updatedTrain) throws IOException {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainNo().equalsIgnoreCase(updatedTrain.getTrainNo()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainList();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }

    public void addTrain(Train newTrain) throws IOException {
        // Check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainNo().equalsIgnoreCase(newTrain.getTrainNo()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainList();
        }
    }

    public void saveTrainList() throws IOException {
        try {
            mapper.writeValue(new File(TRAINS_PATH), trainList);
        } catch (IOException e) {
            throw new IOException();
        }
    }
}
