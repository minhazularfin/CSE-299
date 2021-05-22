package com.nsu.shipbid.Domains;

import java.io.Serializable;
import java.util.Objects;

public class Ride implements Serializable {
    private String name;
    private String carNumber;
    private String leavingFrom;
    private String time;
    private String email;

    public Ride(String name, String carNumber, String leavingFrom, String time, String email) {
        this.name = name;
        this.carNumber = carNumber;
        this.leavingFrom = leavingFrom;
        this.time = time;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getLeavingFrom() {
        return leavingFrom;
    }

    public void setLeavingFrom(String leavingFrom) {
        this.leavingFrom = leavingFrom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Ride() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ride)) return false;
        Ride ride = (Ride) o;
        return getName().equals(ride.getName()) &&
                getCarNumber().equals(ride.getCarNumber()) &&
                getLeavingFrom().equals(ride.getLeavingFrom()) &&
                getTime().equals(ride.getTime()) &&
                getEmail().equals(ride.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCarNumber(), getLeavingFrom(), getTime(), getEmail());
    }

    @Override
    public String toString() {
        return "Ride{" +
                "name='" + name + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", leavingFrom='" + leavingFrom + '\'' +
                ", time='" + time + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
