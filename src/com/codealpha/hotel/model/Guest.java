package com.codealpha.hotel.model;

import java.io.Serializable;

/**
 * Represents a guest making a reservation.
 */
public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String email;
    private final String phone;

    public Guest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name + " (" + phone + ", " + email + ")";
    }
}
