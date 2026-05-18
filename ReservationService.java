package hotel;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Reservation> reservations;
    private RoomService roomService;
    private final String FILE_NAME = "reservations.txt";

    public ReservationService(RoomService roomService) {
        this.roomService = roomService;
        this.reservations = loadReservationsFromFile();
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    // Core verification algorithm: Date Overlap Detection
    public boolean isRoomAvailable(String roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation res : reservations) {
            if (res.getRoomNumber().equals(roomNumber) && !res.getStatus().equalsIgnoreCase("cancelled")) {
                // Mathematical Overlap Check: (StartA < EndB) && (EndA > StartB)
                if (checkIn.isBefore(res.getCheckOutDate()) && checkOut.isAfter(res.getCheckInDate())) {
                    return false; 
                }
            }
        }
        return true;
    }

    public boolean createReservation(String id, String roomNumber, String name, String email, String phone, 
                                     LocalDate checkIn, LocalDate checkOut, String notes) {
        // Form validations
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            System.out.println("Validation Error: Check-out date must follow your check-in date.");
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Validation Error: Invalid email sequence standard missing.");
            return false;
        }
        if (phone.length() < 7) {
            System.out.println("Validation Error: Phone number length is inadequate.");
            return false;
        }

        Room targetRoom = roomService.findRoomByNumber(roomNumber);
        if (targetRoom == null) {
            System.out.println("System Error: Designated Room Number does not exist.");
            return false;
        }

        if (!isRoomAvailable(roomNumber, checkIn, checkOut)) {
            System.out.println("System Error: Selected room is booked out for this frame.");
            return false;
        }

        Reservation newRes = new Reservation(id, roomNumber, name, email, phone, checkIn, checkOut, 
                                             targetRoom.getPricePerNight(), "confirmed", notes);
        reservations.add(newRes);
        saveReservationsToFile();
        return true;
    }

    public void processCheckIn(String resId) {
        for (Reservation res : reservations) {
            if (res.getId().equals(resId) && res.getStatus().equalsIgnoreCase("confirmed")) {
                res.setStatus("in-progress");
                roomService.updateRoomStatus(res.getRoomNumber(), "occupied");
                saveReservationsToFile();
                System.out.println("Check-In Event Logged successfully.");
                return;
            }
        }
        System.out.println("Error: No active booking found matched to that code.");
    }

    public void processCheckOut(String resId) {
        for (Reservation res : reservations) {
            if (res.getId().equals(resId) && res.getStatus().equalsIgnoreCase("in-progress")) {
                res.setStatus("completed");
                roomService.updateRoomStatus(res.getRoomNumber(), "available");
                saveReservationsToFile();
                System.out.println("Check-Out Event Logged successfully.");
                return;
            }
        }
        System.out.println("Error: No active operational stay found under that code.");
    }

    public void saveReservationsToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Reservation res : reservations) {
                out.println(res.getId() + "," + res.getRoomNumber() + "," + res.getGuestName() + "," + 
                            res.getEmail() + "," + res.getPhone() + "," + res.getCheckInDate() + "," + 
                            res.getCheckOutDate() + "," + res.getStatus() + "," + res.getSpecialNotes());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservation modifications: " + e.getMessage());
        }
    }

    private List<Reservation> loadReservationsFromFile() {
        List<Reservation> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] t = line.split(",");
                if (t.length >= 9) {
                    Room r = roomService.findRoomByNumber(t[1]);
                    double baseRate = (r != null) ? r.getPricePerNight() : 0.0;
                    list.add(new Reservation(t[0], t[1], t[2], t[3], t[4], 
                             LocalDate.parse(t[5]), LocalDate.parse(t[6]), baseRate, t[7], t[8]));
                }
            }
        } catch (Exception e) {
            System.out.println("Notice: Could not parse reservation metrics file.");
        }
        return list;
    }
}