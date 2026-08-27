package com.codealpha.hotel.model;

import java.io.Serializable;

/**
 * Represents a single hotel room.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int roomNumber;
    private final RoomType roomType;
    private boolean available;

    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPricePerNight() {
        return roomType.getBasePricePerNight();
    }

    @Override
    public String toString() {
        return String.format("Room #%-4d | %-8s | Rs.%-8.2f/night | %s",
                roomNumber, roomType, getPricePerNight(),
                available ? "AVAILABLE" : "BOOKED");
    }
}
