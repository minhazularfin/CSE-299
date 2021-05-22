package com.nsu.shipbid.Domains;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private String name;
    private String time;
    private String leavingFrom;
    private String destination;
    private String email;
    private int seats;

    public Request(String name, String time, String leavingFrom, String destination,String email, int seats) {
        this.name = name;
        this.time = time;
        this.leavingFrom = leavingFrom;
        this.destination = destination;
        this.email = email;
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return getSeats() == request.getSeats() &&
                getName().equals(request.getName()) &&
                getTime().equals(request.getTime()) &&
                getLeavingFrom().equals(request.getLeavingFrom()) &&
                getDestination().equals(request.getDestination()) &&
                getEmail().equals(request.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTime(), getLeavingFrom(), getDestination(), getEmail(), getSeats());
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", leavingFrom='" + leavingFrom + '\'' +
                ", destination='" + destination + '\'' +
                ", email='" + email + '\'' +
                ", seats=" + seats +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeavingFrom() {
        return leavingFrom;
    }

    public void setLeavingFrom(String leavingFrom) {
        this.leavingFrom = leavingFrom;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Request() {
    }

    public boolean match(Ride ride){
        return (time.equals("Any Time")|ride.getTime().equals(time)|ride.getTime().equals("Any Time"))&(ride.getLeavingFrom().equals(leavingFrom));
    }
}
