package com.nsu.shipbid.Domains;

import java.io.Serializable;
import java.util.Objects;

public class Profile implements Serializable {
    private String name;
    private String phoneNumber;
    private String profileType;
    private String carNumber;
    private String email;

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileType='" + profileType + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Profile() {
    }

    public Profile(String name, String phoneNumber, String profileType, String carNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileType = profileType;
        this.carNumber = carNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile profile = (Profile) o;
        return getName().equals(profile.getName()) &&
                getPhoneNumber().equals(profile.getPhoneNumber()) &&
                getProfileType().equals(profile.getProfileType()) &&
                Objects.equals(getCarNumber(), profile.getCarNumber()) &&
                getEmail().equals(profile.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPhoneNumber(), getProfileType(), getCarNumber(), getEmail());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
