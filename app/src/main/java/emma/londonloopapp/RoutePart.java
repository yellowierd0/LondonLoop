package emma.londonloopapp;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Emma on 13/05/2015.
 */
public class RoutePart {

    private String mode;
    private String from_point_name;
    private String to_point_name;
    private String destination;
    private String line_name;
    private String duration;
    private String departure_time;
    private String arrival_time;
    private ArrayList<Location> coordinates;


    public RoutePart(String mode, String from_point_name, String to_point_name,
                     String destination, String line_name, String duration,
                     String departure_time, String arrival_time, ArrayList<Location> coordinates){

        this.mode = mode;
        this.from_point_name = from_point_name;
        this.to_point_name = to_point_name;
        this.destination = destination;
        this.line_name = line_name;
        this.duration = duration;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.coordinates = coordinates;

    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFrom_point_name() {
        return from_point_name;
    }

    public void setFrom_point_name(String from_point_name) {
        this.from_point_name = from_point_name;
    }

    public String getTo_point_name() {
        return to_point_name;
    }

    public void setTo_point_name(String to_point_name) {
        this.to_point_name = to_point_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public ArrayList<Location> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Location> coordinates) {
        this.coordinates = coordinates;
    }
}