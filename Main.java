package hotel;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RoomService roomService = new RoomService();
        ReservationService resService = new ReservationService(roomService);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Hotel Inventory & Reservation Engine ===");

        while (true) {
            System.out.println("\n--- MAIN PANEL ---");
            System.out.println("1. Print Room Status Inventory");
            System.out.println("2. Review Date Specific Occupancy Panel");
            System.out.println("3. Input New Guest Reservation");
            System.out.println("4. Commit Room Check-In Status");
            System.out.println("5. Commit Room Check-Out Status");
            System.out.println("6. Export Ledger to CSV File");
            System.out.println("7. Export Ledger to Excel File");
            System.out.println("8. Shutdown");
            System.out.print("Select operational node (1-8): ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.println("\n[Current Room Matrix]");
                    for (Room r : roomService.getAllRooms()) {
                        System.out.println(r);
                    }
                    break;

                case "2":
                    System.out.print("Enter targeted analysis date (YYYY-MM-DD): ");
                    try {
                        LocalDate checkDate = LocalDate.parse(scanner.nextLine());
                        System.out.println("\n[Occupancy Panel Ledger For " + checkDate + "]");
                        for (Room r : roomService.getAllRooms()) {
                            boolean free = resService.isRoomAvailable(r.getRoomNumber(), checkDate, checkDate.plusDays(1));
                            System.out.println("Room " + r.getRoomNumber() + " -> " + (free ? "FREE/AVAILABLE" : "RESERVED/OCCUPIED"));
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Entry String. Follow YYYY-MM-DD template.");
                    }
                    break;

                case "3":
                    try {
                        System.out.println("\n[New Booking Matrix Submission Form]");
                        System.out.print("ID: "); String id = scanner.nextLine();
                        System.out.print("Targeted Room Number: "); String rn = scanner.nextLine();
                        System.out.print("Guest Full Name: "); String name = scanner.nextLine();
                        System.out.print("Guest Email: "); String email = scanner.nextLine();
                        System.out.print("Guest Contact Line: "); String ph = scanner.nextLine();
                        System.out.print("Inbound Date (YYYY-MM-DD): "); LocalDate in = LocalDate.parse(scanner.nextLine());
                        System.out.print("Outbound Date (YYYY-MM-DD): "); LocalDate out = LocalDate.parse(scanner.nextLine());
                        System.out.print("Special Conditions: "); String note = scanner.nextLine();

                        if (resService.createReservation(id, rn, name, email, ph, in, out, note)) {
                            System.out.println("Reservation verified and saved.");
                        }
                    } catch (Exception e) {
                        System.out.println("Input Parse Failure. Check configuration variables and formatting values.");
                    }
                    break;

                case "4":
                    System.out.print("Input Reservation verification code for Check-In: ");
                    resService.processCheckIn(scanner.nextLine());
                    break;

                case "5":
                    System.out.print("Input Reservation verification code for Check-Out: ");
                    resService.processCheckOut(scanner.nextLine());
                    break;

                case "6":
                    ExportService.exportReservationsToCSV(resService.getAllReservations());
                    break;

                case "7":
                    ExportService.exportReservationsToExcel(resService.getAllReservations());
                    break;

                case "8":
                    System.out.println("Closing Core Registry Database safely... Goodbye.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Operational code unassigned. Try again.");
            }
        }
    }
}