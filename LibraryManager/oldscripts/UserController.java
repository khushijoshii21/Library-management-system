package com.librarymanagement.demo;

public class UserController {

    public static String UserMenu(){
        return """
            Main Menu:
            What would you like to do?
            1. View your issued books information
            2. Book Reservation
            3. View books
            """;
    }

    public static String ReservationMenu(){
        return """
            Reservation Menu:
            What would you like to do?
            1. Reserve a book
            2. Cancel reservation
            3. View reservation
            4. Return
            """;
    }

    public static String GetUserName(String messageBody, String senderNumber){
        int suc = Database.setData("userdata", "Name", senderNumber, messageBody);
        int suc2 = Database.setData("userdata", "Mode", senderNumber, "UserMenu");
        if (suc==-1||suc2==-1){
            return "Could not connect with server. Please try again.";
        }
        return UserMenu();
    }

    public static String viewReservation(String senderNumber){
        return Database.viewReservation(senderNumber);
    }

    public static String viewBooks(){
        return Database.viewAvailableBooks();
    }

    public static String reserveBookMenu(String senderNumber){
        Database.setData("userdata", "Mode", senderNumber, "reserveBook");
        return "Enter book ID and reserve slot: \n(Book_ID/Book_Slot(1 or 2))";
    }

    public static String reserveBook(String messageBody, String senderNumber){
        String[] arrOfStr = messageBody.split("/", 2);
        Database.setData("userdata", "Mode", senderNumber, "BookReservationMenu");
        return Database.reserveBook(arrOfStr[0], arrOfStr[1], senderNumber);
    }

    public static String cancelreserveBookMenu(String senderNumber){
        Database.setData("userdata", "Mode", senderNumber, "cancelreserveBook");
        return "Enter reserve slot: \n(Book_Slot(1 or 2))";
    }

    public static String cancelreserveBook(String messageBody, String senderNumber){
        Database.setData("userdata", "Mode", senderNumber, "BookReservationMenu");
        System.out.println("YOOO");
        return Database.cancelreserveBook(messageBody, senderNumber);
    }

}
