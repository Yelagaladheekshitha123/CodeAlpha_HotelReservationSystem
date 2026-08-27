# CodeAlpha_HotelReservationSystem

A console-based Hotel Reservation System built in **Java** using core Object-Oriented
Programming principles, developed as part of the **CodeAlpha Java Programming Internship**
(Task 4).

## ✨ Features

- **Room categorization** — Standard, Deluxe, and Suite rooms, each with its own pricing.
- **Search & filter** available rooms by category.
- **Book a room** for a guest across a date range, with automatic total calculation.
- **Cancel a reservation** — frees the room and simulates a refund.
- **Payment simulation** — generates a mock transaction reference on booking.
- **View all reservations** and pull up a full receipt for any reservation ID.
- **Persistent storage** — all bookings are saved to disk (`data/reservations.dat`)
  via Java object serialization, so data survives between runs.

## 🏗 Project Structure

```
CodeAlpha_HotelReservationSystem/
├── src/
│   └── com/codealpha/hotel/
│       ├── Main.java                  # Console UI / entry point
│       ├── model/
│       │   ├── Room.java              # Room entity
│       │   ├── RoomType.java          # Enum: STANDARD, DELUXE, SUITE (with pricing)
│       │   ├── Guest.java             # Guest entity
│       │   ├── Reservation.java       # Links guest + room + dates + payment
│       │   ├── ReservationStatus.java # Enum: BOOKED, CANCELLED, CHECKED_OUT
│       │   └── Payment.java           # Simulated payment processing
│       ├── service/
│       │   └── HotelService.java      # Business logic: search, book, cancel
│       └── util/
│           └── FileManager.java       # Save/load reservations (serialization)
├── data/                              # Created automatically to store reservations.dat
├── README.md
└── .gitignore
```

## ▶️ How to Run

**Requirements:** JDK 17 or later.

```bash
# From the project root
javac -d out $(find src -name "*.java")
java -cp out com.codealpha.hotel.Main
```

## 🖥 Sample Menu

```
----- MENU -----
1. View all rooms
2. Search available rooms
3. Book a room
4. Cancel a reservation
5. View all reservations
6. View reservation details / receipt
0. Exit
```

## 🧱 Design Notes

- Built with clean **OOP separation**: models (`model` package), business logic
  (`service` package), and persistence (`util` package) are kept independent.
- `RoomType` is an enum carrying its own base price, keeping pricing rules in one place.
- `HotelService` is the single source of truth for room/reservation state and persists
  changes to disk after every booking or cancellation.
- Payment is simulated (no real gateway) — it calculates totals and generates a mock
  transaction reference, per the task requirements.

## 📌 About

Built for the **CodeAlpha Java Programming Internship** — Task 4: Hotel Reservation System.

- Website: [www.codealpha.tech](https://www.codealpha.tech)
