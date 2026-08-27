package com.codealpha.hotel.util;

import com.codealpha.hotel.model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of reservation data to disk using Java object
 * serialization, so bookings survive between program runs.
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + File.separator + "reservations.dat";

    /**
     * Saves the full list of reservations to disk, overwriting any
     * previous save file.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveReservations(List<Reservation> reservations) {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
                oos.writeObject(reservations);
            }
        } catch (IOException e) {
            System.out.println("⚠ Could not save reservations: " + e.getMessage());
        }
    }

    /**
     * Loads reservations from disk. Returns an empty list if no save
     * file exists yet (e.g., first run of the program).
     */
    @SuppressWarnings("unchecked")
    public static List<Reservation> loadReservations() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠ Could not load reservations, starting fresh: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
