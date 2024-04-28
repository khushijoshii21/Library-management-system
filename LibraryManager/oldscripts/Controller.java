package com.librarymanagement.demo;

public class Controller {

    public static String Handler(String messageBody, String senderNumber){
        String userauth = Database.getAuth(senderNumber);
        System.out.println(userauth);
        if (userauth.equalsIgnoreCase("Admin")){
            String mode = Database.getData("admindata", "Mode", senderNumber);
            if (mode.equalsIgnoreCase("AdminMenu")){
                if (messageBody.equalsIgnoreCase("1")){
                    return AdminController.issueBookMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("2")){
                    return AdminController.returnBookMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("3")){
                    return AdminController.AddBookMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("4")){
                    return AdminController.RemoveBookMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("5")){
                    return AdminController.BanUserMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("6")){
                    return AdminController.unBanUserMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("7")){
                    return AdminController.addAdminMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("8")){
                    return AdminController.removeAdminMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("9")){
                    return AdminController.ViewAllBooks(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("10")){
                    return AdminController.clearUserDataMenu(senderNumber);
                }
                else{
                    return AdminController.AdminMenu();
                }
            }
            else if (mode.equalsIgnoreCase("AddBookDetails")){
                return AdminController.AddBook(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("RemoveBook")){
                return AdminController.RemoveBook(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("BanUser")){
                return AdminController.BanUser(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("unBanUser")){
                return AdminController.unBanUser(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("addAdmin")){
                return AdminController.addAdmin(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("removeAdmin")){
                return AdminController.removeAdmin(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("clearUser")){
                return AdminController.clearUserData(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("issueBook")){
                return AdminController.issueBook(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("returnBook")){
                return AdminController.returnBook(messageBody, senderNumber);
            }
            return "-1";
        }
        else if (userauth.equalsIgnoreCase("User")){
            String isbanned = Database.getData("userdata", "Banned", senderNumber);
            if (isbanned.equalsIgnoreCase("1")){
                return "You are banned.";
            }
            String mode = Database.getData("userdata", "Mode", senderNumber);
            if (mode.equalsIgnoreCase("GetUserName")){
                return UserController.GetUserName(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("UserMenu")){
                if (messageBody.equalsIgnoreCase("1")){
                    return Database.viewBooksInfo(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("2")){
                    int suc = Database.setData("userdata", "Mode", senderNumber, "BookReservationMenu");
                    if (suc==-1){
                        return "Could not connect with server. Please try again.";
                    }
                    return UserController.ReservationMenu();
                }
                else if (messageBody.equalsIgnoreCase("3")){
                    return UserController.viewBooks();
                }
                else{
                    return UserController.UserMenu();
                }
            }
            else if (mode.equalsIgnoreCase("BookReservationMenu")){
                if (messageBody.equalsIgnoreCase("1")){
                    return UserController.reserveBookMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("2")){
                    return UserController.cancelreserveBookMenu(senderNumber);
                }
                else if (messageBody.equalsIgnoreCase("3")){
                    return UserController.viewReservation(senderNumber);
                }
                else{
                    Database.setData("userdata", "Mode", senderNumber, "UserMenu");
                    return UserController.UserMenu();
                }
            }
            else if (mode.equalsIgnoreCase("reserveBook")){
                return UserController.reserveBook(messageBody, senderNumber);
            }
            else if (mode.equalsIgnoreCase("cancelreserveBook")){
                return UserController.cancelreserveBook(messageBody, senderNumber);
            }
            return "-1";
        }
        else{
            int suc = Database.addUserData(senderNumber, "Default", "0", "0", "NA", "NA", "NA", "0", "0", "NA", "NA", "NA", "0", "GetUserName", "0");
            if (suc==-1){
                System.out.println(senderNumber);
                return "Could not connect with server. Please try again.";
            }
            return """
                    Hi! It seems this is your first time here.
                    Please enter your name.
                    """;
        }
    }
}
