package com.codealpha.hotel.service;

import com.codealpha.hotel.model.*;
import com.codealpha.hotel.util.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core service layer that manages rooms and reservations.
 * This is where all the business rules live: searching, booking,
 * cancelling, and persisting data.
 */
public class HotelService {

    private final List<Room> rooms;
    private List<Reservation> reservations;
    private int reservationCounter;

    public HotelService() {
        this.rooms = new ArrayList<>();
        this.reservations = FileManager.loadReservations();
        this.reservationCounter = reservations.size();
        initializeRooms();
        syncRoomAvailability();
    }

    /**
     * Sets up a fixed inventory of rooms across the three categories.
     * Room numbers: 100s = Standard, 200s = Deluxe, 300s = Suite.
     */
    private void initializeRooms() {
        for (int i = 1; i <= 5; i++) rooms.add(new Room(100 + i, RoomType.STANDARD));
        for (int i = 1; i <= 3; i++) rooms.add(new Room(200 + i, RoomType.DELUXE));
        for (int i = 1; i <= 2; i++) rooms.add(new Room(300 + i, RoomType.SUITE));
    }

    /**
     * After loading saved reservations, mark the corresponding rooms
     * as unavailable so state is consistent across restarts.
     */
    private void syncRoomAvailability() {
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.BOOKED) {
                r.getRoom().setAvailable(false);
                findRoomByNumber(r.getRoom().getRoomNumber())
                        .ifPresent(room -> room.setAvailable(false));
            }
        }
    }

    private java.util.Optional<Room> findRoomByNumber(int roomNumber) {
        return rooms.stream().filter(r -> r.getRoomNumber() == roomNumber).findFirst();
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public List<Room> searchAvailableRooms(RoomType type) {
        return rooms.stream()
                .filter(Room::isAvailable)
                .filter(r -> type == null || r.getRoomType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Books a room for the given guest and date range.
     * Returns the created Reservation, or null if the room is unavailable
     * or the dates are invalid.
     */
    public Reservation bookRoom(int roomNumber, Guest guest, LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            System.out.println("✘ Check-out date must be after check-in date.");
            return null;
        }

        java.util.Optional<Room> roomOpt = findRoomByNumber(roomNumber);
        if (roomOpt.isEmpty() || !roomOpt.get().isAvailable()) {
            System.out.println("✘ Room #" + roomNumber + " is not available.");
            return null;
        }

        Room room = roomOpt.get();
        reservationCounter++;
        String id = "RES" + String.format("%04d", reservationCounter);

        Reservation reservation = new Reservation(id, guest, room, checkIn, checkOut);
        reservation.getPayment().processPayment(); // simulate payment at booking time
        room.setAvailable(false);
        reservations.add(reservation);
        persist();
        return reservation;
    }

    /**
     * Cancels an existing reservation by ID, frees up the room, and
     * simulates a refund.
     */
    public boolean cancelReservation(String reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equalsIgnoreCase(reservationId)
                    && r.getStatus() == ReservationStatus.BOOKED) {
                r.setStatus(ReservationStatus.CANCELLED);
                r.getPayment().refund();
                r.getRoom().setAvailable(true);
                persist();
                return true;
            }
        }
        return false;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public java.util.Optional<Reservation> findReservation(String reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equalsIgnoreCase(reservationId))
                .findFirst();
    }

    private void persist() {
        FileManager.saveReservations(reservations);
    }
}
