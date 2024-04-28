package com.librarymanagement.demo;

public class AdminController {

    public static String AdminMenu(){
        return """
                Admin Menu:
                What would you like to do?
                0. Show admin menu
                1. Issue book
                2. Return book
                3. Add book
                4. Remove book
                5. Ban user
                6. Unban user
                7. Add admin
                8. Remove admin
                9. View all books
                10. Clear User Data
                """;
    }

    public static String AddBook(String messageBody, String senderNumber){
        String[] arrOfStr = messageBody.split("/", 3);
        Database.addBookData(arrOfStr[0], arrOfStr[1], arrOfStr[2]);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        return "Book added successfully.";
    }

    public static String AddBookMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "AddBookDetails");
        return """
                Enter book details in the given format:
                Book_ID/Book_Name/Book_Genre
                """;
    }

    public static String RemoveBook(String messageBody, String senderNumber){
        String suc = Database.removeBookData(messageBody);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        if (suc.equalsIgnoreCase("0")){
            return """
                The book is currently being accessed by others or not found.
                Please try again later or check if the ID is correct.
                """;
        }
        return "Book removed successfully.";
    }

    public static String RemoveBookMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "RemoveBook");
        return """
                Enter the Book ID that you wish to remove:
                """;
    }

    public static String ViewAllBooks(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        return Database.viewAllBooks();
    }

    public static String BanUser(String messageBody, String senderNumber){
        int suc = Database.setData("userdata", "Banned", messageBody, "1");
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        if (suc==1){
            return "User has been banned successfully.";
        }
        return "An error has occured.";
    }

    public static String BanUserMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "BanUser");
        return "Enter user phone number: ";
    }

    public static String unBanUser(String messageBody, String senderNumber){
        int suc = Database.setData("userdata", "Banned", messageBody, "0");
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        if (suc==1){
            return "User has been unbanned successfully.";
        }
        return "An error has occured.";
    }

    public static String unBanUserMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "unBanUser");
        return "Enter user phone number: ";
    }

    public static String addAdminMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "addAdmin");
        return "Enter new admin details: \nPhone_Number/Name";
    }

    public static String addAdmin(String messageBody, String senderNumber){
        String[] arrOfStr = messageBody.split("/", 2);
        int suc = Database.addAdminData(arrOfStr[0], arrOfStr[1]);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        if (suc==1){
            return "Admin added successfully.";
        }
        return "An error has occured";
    }

    public static String removeAdminMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "removeAdmin");
        return "Enter admin phone number: (+91XXXXXXXXXX)";
    }

    public static String removeAdmin(String messageBody, String senderNumber){
        if (messageBody.equalsIgnoreCase("+919999367202")){
            return "You can not remove the owner.";
        }
        String suc = Database.removeAdminData(messageBody);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        if (suc.equalsIgnoreCase("1")){
            return "Admin has been removed successfully.";
        }
        return "An error has occured";
    }

    public static String clearUserDataMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "clearUser");
        return "Enter user phone number: (+91XXXXXXXXXX)";
    }

    public static String clearUserData(String messageBody, String senderNumber){
        String suc = Database.removeUserData(messageBody);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        if (suc.equalsIgnoreCase("1")){
            return "User data has been cleared successfully.";
        }
        return "An error has occured";
    }

    public static String issueBookMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "issueBook");
        return "Enter user phone number and book ID and book slot in the following format: \nPhone_Number/Book_ID/Book_Slot";
    }

    public static String issueBook(String messageBody, String senderNumber){
        String[] arrOfStr = messageBody.split("/", 3);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        return Database.issueBook(arrOfStr[1], arrOfStr[2], arrOfStr[0]);
    }

    public static String returnBookMenu(String senderNumber){
        Database.setData("admindata", "Mode", senderNumber, "returnBook");
        return "Enter user phone number and book slot in the following format: \nPhone_Number/Book_Slot";
    }

    public static String returnBook(String messageBody, String senderNumber){
        String[] arrOfStr = messageBody.split("/", 2);
        Database.setData("admindata", "Mode", senderNumber, "AdminMenu");
        return Database.returnBook(arrOfStr[1], arrOfStr[0]);
    }
}
