package com.codealpha.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a booking that links a Guest to a Room for a date range,
 * along with its payment and current status.
 */
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final Payment payment;
    private ReservationStatus status;

    public Reservation(String reservationId, Guest guest, Room room,
                        LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = ReservationStatus.BOOKED;
        this.payment = new Payment(calculateTotal());
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public double calculateTotal() {
        return getNumberOfNights() * room.getPricePerNight();
    }

    public String getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public Payment getPayment() {
        return payment;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "Reservation[%s] | %s | Room #%d (%s) | %s -> %s | %d night(s) | Rs.%.2f | %s | %s",
                reservationId, guest.getName(), room.getRoomNumber(), room.getRoomType(),
                checkIn, checkOut, getNumberOfNights(), calculateTotal(), status, payment.getStatus());
    }
}
