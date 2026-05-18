package hotel;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportService {
    private static final String CSV_FILE = "monthly_reservation_report.csv";

    public static void exportReservationsToCSV(List<Reservation> reservations) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write structured Header Line
            out.println("ReservationID,RoomNumber,GuestName,Email,Phone,CheckIn,CheckOut,Nights,TotalBilling,Status");
            
            for (Reservation res : reservations) {
                out.println(res.getId() + "," + res.getRoomNumber() + "," + res.getGuestName() + "," + 
                            res.getEmail() + "," + res.getPhone() + "," + res.getCheckInDate() + "," + 
                            res.getCheckOutDate() + "," + res.getNumberOfNights() + "," + 
                            res.getTotalAmount() + "," + res.getStatus());
            }
            System.out.println("System Alert: Data exported into: " + CSV_FILE);
        } catch (IOException e) {
            System.out.println("System Failure writing data track to CSV: " + e.getMessage());
        }
    }
}