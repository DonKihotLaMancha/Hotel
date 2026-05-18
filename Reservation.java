package hotel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private String id;
    private String roomNumber;
    private String guestName;
    private String email;
    private String phone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private long numberOfNights;
    private double totalAmount;
    private String status; // confirmed / cancelled / completed / in-progress
    private String specialNotes;

    public Reservation(String id, String roomNumber, String guestName, String email, String phone, 
                       LocalDate checkInDate, LocalDate checkOutDate, double pricePerNight, 
                       String status, String specialNotes) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.guestName = guestName;
        this.email = email;
        this.phone = phone;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.specialNotes = specialNotes;
        
        calculateFinancials(pricePerNight);
    }

    public void calculateFinancials(double pricePerNight) {
        if (checkInDate != null && checkOutDate != null && !checkOutDate.isBefore(checkInDate)) {
            this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            this.totalAmount = this.numberOfNights * pricePerNight;
        } else {
            this.numberOfNights = 0;
            this.totalAmount = 0.0;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getGuestName() { return guestName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public long getNumberOfNights() { return numberOfNights; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSpecialNotes() { return specialNotes; }

    @Override
    public String toString() {
        return "Res ID: " + id + " | Room: " + roomNumber + " | Guest: " + guestName + 
               " | " + checkInDate + " to " + checkOutDate + " (" + numberOfNights + " nights)" +
               " | Total: $" + totalAmount + " | Status: " + status;
    }
}