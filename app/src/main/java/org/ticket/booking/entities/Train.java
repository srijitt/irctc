package org.ticket.booking.entities;

import java.util.List;
import java.util.Map;

public class Train {
    private String trainName;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTime;
    private List<String> stations;

    public Train(String trainName, String trainNo, List<List<Integer>> seats, Map<String, String> stationTime, List<String> stations){
        this.trainName = trainName;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTime = stationTime;
        this.stations = stations;
    }

    public Train() {  }

    public List<String> getStations(){
        return stations;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats){
        this.seats = seats;
    }

    public String getTrainName(){
        return trainName;
    }

    public Map<String, String> getStationTimes(){
        return stationTime;
    }

    public String getTrainNo(){
        return trainNo;
    }

    public void setTrainNo(String trainNo){
        this.trainNo = trainNo;
    }

    public void setStationTimes(Map<String, String> stationTimes){
        this.stationTime = stationTimes;
    }

    public void setStations(List<String> stations){
        this.stations = stations;
    }

    public String getTrainInfo(){
        return String.format("Train No: %s Train Name: %s", trainNo, trainName);
    }
}
