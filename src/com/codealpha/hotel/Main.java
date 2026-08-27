package com.codealpha.hotel;

import com.codealpha.hotel.model.*;
import com.codealpha.hotel.service.HotelService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Hotel Reservation System.
 * Provides a simple menu-driven interface to search rooms,
 * book, cancel, and view reservations.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HotelService hotelService = new HotelService();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   CodeAlpha Hotel Reservation System");
        System.out.println("=====================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> viewAllRooms();
                case "2" -> searchAvailableRooms();
                case "3" -> bookRoom();
                case "4" -> cancelReservation();
                case "5" -> viewAllReservations();
                case "6" -> viewReservationDetails();
                case "0" -> {
                    System.out.println("Thank you for using CodeAlpha Hotel Reservation System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("✘ Invalid choice, please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n----- MENU -----");
        System.out.println("1. View all rooms");
        System.out.println("2. Search available rooms");
        System.out.println("3. Book a room");
        System.out.println("4. Cancel a reservation");
        System.out.println("5. View all reservations");
        System.out.println("6. View reservation details / receipt");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void viewAllRooms() {
        System.out.println("\n--- All Rooms ---");
        for (Room room : hotelService.getAllRooms()) {
            System.out.println(room);
        }
    }

    private static void searchAvailableRooms() {
        RoomType type = askRoomTypeOrAny();
        List<Room> available = hotelService.searchAvailableRooms(type);
        System.out.println("\n--- Available Rooms ---");
        if (available.isEmpty()) {
            System.out.println("No available rooms found for that category.");
        } else {
            available.forEach(System.out::println);
        }
    }

    private static void bookRoom() {
        System.out.print("Enter room number to book: ");
        int roomNumber = readInt();
        if (roomNumber == -1) return;

        System.out.print("Enter guest name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter guest email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter guest phone: ");
        String phone = scanner.nextLine().trim();

        LocalDate checkIn = readDate("Enter check-in date (yyyy-MM-dd): ");
        if (checkIn == null) return;
        LocalDate checkOut = readDate("Enter check-out date (yyyy-MM-dd): ");
        if (checkOut == null) return;

        Guest guest = new Guest(name, email, phone);
        Reservation reservation = hotelService.bookRoom(roomNumber, guest, checkIn, checkOut);

        if (reservation != null) {
            System.out.println("\n✔ Booking confirmed!");
            printReceipt(reservation);
        }
    }

    private static void cancelReservation() {
        System.out.print("Enter reservation ID to cancel: ");
        String id = scanner.nextLine().trim();
        boolean success = hotelService.cancelReservation(id);
        if (success) {
            System.out.println("✔ Reservation " + id + " has been cancelled and refunded.");
        } else {
            System.out.println("✘ Reservation not found or already cancelled.");
        }
    }

    private static void viewAllReservations() {
        System.out.println("\n--- All Reservations ---");
        List<Reservation> reservations = hotelService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations yet.");
        } else {
            reservations.forEach(System.out::println);
        }
    }

    private static void viewReservationDetails() {
        System.out.print("Enter reservation ID: ");
        String id = scanner.nextLine().trim();
        hotelService.findReservation(id).ifPresentOrElse(
                Main::printReceipt,
                () -> System.out.println("✘ Reservation not found.")
        );
    }

    private static void printReceipt(Reservation r) {
        System.out.println("\n========= RESERVATION RECEIPT =========");
        System.out.println("Reservation ID : " + r.getReservationId());
        System.out.println("Guest          : " + r.getGuest());
        System.out.println("Room           : #" + r.getRoom().getRoomNumber() + " (" + r.getRoom().getRoomType() + ")");
        System.out.println("Check-in       : " + r.getCheckIn());
        System.out.println("Check-out      : " + r.getCheckOut());
        System.out.println("Nights         : " + r.getNumberOfNights());
        System.out.println("Total Amount   : Rs." + String.format("%.2f", r.calculateTotal()));
        System.out.println("Payment        : " + r.getPayment());
        System.out.println("Status         : " + r.getStatus());
        System.out.println("=========================================");
    }

    // ---------- Input helper methods ----------

    private static RoomType askRoomTypeOrAny() {
        System.out.println("Filter by category: 1) Standard  2) Deluxe  3) Suite  4) Any");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        return switch (choice) {
            case "1" -> RoomType.STANDARD;
            case "2" -> RoomType.DELUXE;
            case "3" -> RoomType.SUITE;
            default -> null;
        };
    }

    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✘ Invalid number.");
            return -1;
        }
    }

    private static LocalDate readDate(String prompt) {
        System.out.print(prompt);
        try {
            return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("✘ Invalid date format. Use yyyy-MM-dd.");
            return null;
        }
    }
}
