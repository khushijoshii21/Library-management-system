package com.librarymanagement.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Calendar;

public class Database {
    static final String DB_URL = "jdbc:mysql://localhost:3306/librarymanager";
    static final String USER = "root";
    static final String PASS = "vibhor23";
    static Connection conn;

    public static int searchForNumber(String tablename, String num){
        String sql = "SELECT * FROM "+tablename+" WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return 0;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getAuth(String num){
        if (searchForNumber("admindata", num)==1){
            return "Admin";
        }
        else if (searchForNumber("userdata", num)==1){
            return "User";
        }
        else{
            return "NA";
        }
    }

    public static String getBookData(String data, String bookid){
        String sql = "SELECT * FROM booksdata WHERE Book_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, bookid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve data from the result set
                String Data = resultSet.getString(data);
                // Retrieve other columns as needed

                return Data;
            }
            return "0";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static int setBookData(String data, String bookid, String val){
        String sql = "UPDATE booksdata SET "+data+" = ? WHERE Book_ID = ?";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, val);
            preparedStatement.setString(2, bookid);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(bookid+" book data has been modified.");
            }
            preparedStatement.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getSettingData(String settingid, String data){
        String sql = "SELECT * FROM settings WHERE Setting_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, settingid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve data from the result set
                String Data = resultSet.getString(data);
                // Retrieve other columns as needed

                return Data;
            }
            return "0";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String getData(String tablename, String data, String num){
        String sql = "SELECT * FROM "+tablename+" WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve data from the result set
                String Data = resultSet.getString(data);
                // Retrieve other columns as needed

                return Data;
            }
            return "0";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static int setData(String tablename, String data, String num, String val){
        String sql = "UPDATE "+tablename+" SET "+data+" = ? WHERE Phone_Number = ?";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, val);
            preparedStatement.setString(2, num);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(num+" user data has been modified.");
            }
            preparedStatement.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String viewAllBooks(){

        String res = "";

        String bookid;
        String bookname;
        String bookgenre;
        String bookissuedby;
        String bookreservedby;
        String booksemaphore;

        String sql = "SELECT * FROM booksdata";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                bookid = resultSet.getString("Book_ID");
                bookname = resultSet.getString("Book_Name");
                bookgenre = resultSet.getString("Book_Genre");
                bookissuedby = resultSet.getString("Book_IssuedBy");
                bookreservedby = resultSet.getString("Book_ReservedBy");
                booksemaphore = resultSet.getString("Book_Semaphore");

                res += "ID: "+bookid+"\nName: "+bookname+"\nGenre: "+bookgenre+"\nSemaphore: "+booksemaphore+"\nIssued By: "+bookissuedby+"\nReserved By: "+bookreservedby+"\n\n"; 

            }
            if (res.equalsIgnoreCase("")){
                return "No books found.";
            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }

    public static String viewAvailableBooks(){

        String res = "";

        String bookid;
        String bookname;
        String bookgenre;

        String sql = "SELECT * FROM booksdata where Book_Semaphore = 1";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                bookid = resultSet.getString("Book_ID");
                bookname = resultSet.getString("Book_Name");
                bookgenre = resultSet.getString("Book_Genre");


                res += "ID: "+bookid+"\nName: "+bookname+"\nGenre: "+bookgenre+"\n\n"; 

            }
            if (res.equalsIgnoreCase("")){
                return "No books found.";
            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }


    public static String viewBooksInfo(String num){
        String booksissued;

        String book1id;
        String book1name;
        String book1issued;
        String book1due;

        String book2id;
        String book2name;
        String book2issued;
        String book2due;

        String sql = "SELECT * FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                booksissued = resultSet.getString("BooksIssued");

                book1id = resultSet.getString("Book1_ID");
                book1name = resultSet.getString("Book1_Name");
                book1issued = resultSet.getString("Book1_Issued");
                book1due = resultSet.getString("Book1_Due");

                book2id = resultSet.getString("Book2_ID");
                book2name = resultSet.getString("Book2_Name");
                book2issued = resultSet.getString("Book2_Issued");
                book2due = resultSet.getString("Book2_Due");

                if (book1id.equalsIgnoreCase("0")&&(book2id.equalsIgnoreCase("0"))){
                    return "No books issued.";
                }
                else if (book1id.equalsIgnoreCase("0")&&(!book2id.equalsIgnoreCase("0"))){
                    return "Books Issued: "+booksissued+"\nNo book issued for Book1.\nBook2 ID: "+book2id+"\nBook2 Name: "+book2name+"\nBook2 Issued: "+book2issued+"\nBook2 Due: "+book2due;
                }
                else if (book2id.equalsIgnoreCase("0")&&(!book1id.equalsIgnoreCase("0"))){
                    return "Books Issued: "+booksissued+"\nBook1 ID: "+book1id+"\nBook1 Name: "+book1name+"\nBook1 Issued: "+book1issued+"\nBook1 Due: "+book1due+"\nNo book issued for Book2.";
                }
                else{
                    return "Books Issued: "+booksissued+"\nBook1 ID: "+book1id+"\nBook1 Name: "+book1name+"\nBook1 Issued: "+book1issued+"\nBook1 Due: "+book1due+"\nBook2 ID: "+book2id+"\nBook2 Name: "+book2name+"\nBook2 Issued: "+book2issued+"\nBook2 Due: "+book2due;
                }

            }
            return "No result found.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }

    public static String reserveBook(String bookid, String bookslot, String num){
        String booksemaphore = getBookData("Book_Semaphore", bookid);
        if (booksemaphore.equalsIgnoreCase("0")){
            return "Book is unavailable at the moment.";
        }
        String sql = "SELECT * FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                
                String isbookreservedinslot = resultSet.getString("Book"+bookslot+"_ReservedID");
                if (isbookreservedinslot.equalsIgnoreCase("0")){
                    Database.setData("userdata", "Book"+bookslot+"_ReservedID", num, bookid);
                    Database.setBookData("Book_Semaphore", bookid, "0");
                    Database.setBookData("Book_ReservedBy", bookid, num);
                    return "Book has been reserved successfully.";
                }
                else{
                    return "Book slot full.";
                }

            }
            return "User data not found.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }

    public static String cancelreserveBook(String bookslot, String num){
        String sql = "SELECT * FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                
                String bookid = resultSet.getString("Book"+bookslot+"_ReservedID");
                if (bookid.equalsIgnoreCase("0")){
                    return "No book found in this reserved slot.";
                }
                else{
                    Database.setData("userdata", "Book"+bookslot+"_ReservedID", num, "0");
                    Database.setBookData("Book_Semaphore", bookid, "1");
                    Database.setBookData("Book_ReservedBy", bookid, "NA");
                    return "Book reservation cancelled successfully.";
                }

            }
            return "User data not found.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }

    public static String issueBook(String bookid, String bookslot, String num){
        String booksemaphore = getBookData("Book_Semaphore", bookid);
        if (booksemaphore.equalsIgnoreCase("0")){
            String reservedby = getBookData("Book_ReservedBy", bookid);
            String issuedby = getBookData("Book_IssuedBy", bookid);
            if ((!reservedby.equalsIgnoreCase(num))&&(!issuedby.equalsIgnoreCase("NA"))){
                return "Book is unavailable at the moment.";
            }
        }
        String sql = "SELECT * FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                
                String bookissuedinslot = resultSet.getString("Book"+bookslot+"_ID");
                if (!bookissuedinslot.equalsIgnoreCase("0")){
                    return "A book has already been issued in the given slot.";
                }
                
                String bookreservedinslot = resultSet.getString("Book"+bookslot+"_ReservedID");
                if (bookreservedinslot.equalsIgnoreCase(bookid)){
                    Database.setData("userdata", "Book"+bookslot+"_ReservedID", num, "0");
                    Database.setBookData("Book_ReservedBy", bookid, "NA");
                }
                Database.setBookData("Book_Semaphore", bookid, "0");
                Database.setBookData("Book_IssuedBy", bookid, num);
                Database.setData("userdata", "BooksIssued", num, String.valueOf(Integer.parseInt(Database.getData("userdata", "BooksIssued", num))+1));
                Database.setData("userdata", "Book"+bookslot+"_ID", num, bookid);
                Database.setData("userdata", "Book"+bookslot+"_Name", num, Database.getBookData("Book_Name", bookid));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date issuedate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(issuedate);
                calendar.add(Calendar.DATE, Integer.parseInt(Database.getSettingData("1", "Issue_Duration")));
                Date duedate = calendar.getTime();
                Database.setData("userdata", "Book"+bookslot+"_Issued", num, dateFormat.format(issuedate));
                Database.setData("userdata", "Book"+bookslot+"_Due", num, dateFormat.format(duedate));
                return "Book has been issued successfully.";

            }
            return "User data not found.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }

    public static String returnBook(String bookslot, String num){
        String sql = "SELECT * FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                
                String bookissuedinslot = resultSet.getString("Book"+bookslot+"_ID");
                if (bookissuedinslot.equalsIgnoreCase("0")){
                    return "No book has been issued in this slot.";
                }
                
                Database.setBookData("Book_Semaphore", bookissuedinslot, "1");
                Database.setBookData("Book_IssuedBy", bookissuedinslot, "NA");
                Database.setData("userdata", "BooksIssued", num, String.valueOf(Integer.parseInt(Database.getData("userdata", "BooksIssued", num))-1));
                Database.setData("userdata", "Book"+bookslot+"_ID", num, "0");
                Database.setData("userdata", "Book"+bookslot+"_Name", num, "NA");
                Database.setData("userdata", "Book"+bookslot+"_Issued", num, "NA");
                Database.setData("userdata", "Book"+bookslot+"_Due", num, "NA");
                return "Book has been returned successfully.";

            }
            return "User data not found.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }

    public static String viewReservation(String num){
        String book1reservationid;
        String book2reservationid;

        String sql = "SELECT * FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                
                book1reservationid = resultSet.getString("Book1_ReservedID");
                book2reservationid = resultSet.getString("Book2_ReservedID");

                if (book1reservationid.equalsIgnoreCase("0")&&(book2reservationid.equalsIgnoreCase("0"))){
                    return "No books reserved.";
                }
                else if (book1reservationid.equalsIgnoreCase("0")&&(!book2reservationid.equalsIgnoreCase("0"))){
                    return "Book2 Reservation ID: "+book2reservationid;
                }
                else if (book2reservationid.equalsIgnoreCase("0")&&(!book1reservationid.equalsIgnoreCase("0"))){
                    return "Book1 Reservation ID: "+book1reservationid;
                }
                else{
                    return "Book1 Reservation ID: "+book1reservationid+"\nBook2 Reservation ID: "+book2reservationid;
                }

            }
            return "No result found.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }

    }
    
    public static int addUserData
    (String phone_num, String name, String booksissued, 
    String book1id, String book1name, String book1issued, String book1due, String book1reservedid,
    String book2id, String book2name, String book2issued, String book2due, String book2reservedid,
    String mode, String banned)
    {
        String sql = "INSERT INTO userdata(Phone_number, Name, BooksIssued, Book1_ID, Book1_Name, Book1_Issued, Book1_Due, Book1_ReservedID, Book2_ID, Book2_Name, Book2_Issued, Book2_Due, Book2_ReservedID, Mode, Banned) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, phone_num);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, booksissued);

            preparedStatement.setString(4, book1id);
            preparedStatement.setString(5, book1name);
            preparedStatement.setString(6, book1issued);
            preparedStatement.setString(7, book1due);
            preparedStatement.setString(8, book1reservedid);

            preparedStatement.setString(9, book2id);
            preparedStatement.setString(10, book2name);
            preparedStatement.setString(11, book2issued);
            preparedStatement.setString(12, book2due);
            preparedStatement.setString(13, book2reservedid);

            preparedStatement.setString(14, mode);
            preparedStatement.setString(15, banned);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user data has been inserted successfully.");
            }
            preparedStatement.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String removeUserData(String num){
        String sql = "DELETE FROM userdata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, num);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User has been removed.");
            }
            preparedStatement.close();
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static int addAdminData
    (String phone_num, String name)
    {
        String sql = "INSERT INTO admindata(Phone_number, Name, Mode) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, phone_num);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, "AdminMenu");

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new admin data has been inserted successfully.");
            }
            preparedStatement.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String removeAdminData(String num){
        String sql = "DELETE FROM admindata WHERE Phone_Number = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, num);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Admin has been removed.");
            }
            preparedStatement.close();
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String addBookData(String bookid, String bookname, String bookgenre){
        String sql = "INSERT INTO booksdata(Book_ID, Book_Name, Book_Genre, Book_Semaphore, Book_IssuedBy, Book_ReservedBy) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, bookid);
            preparedStatement.setString(2, bookname);
            preparedStatement.setString(3, bookgenre);
            preparedStatement.setString(4, "1");
            preparedStatement.setString(5, "NA");
            preparedStatement.setString(6, "NA");

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new book data has been inserted successfully.");
            }
            preparedStatement.close();
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String removeBookData(String bookid){
        String booksemaphore = getBookData("Book_Semaphore", bookid);
        if (booksemaphore.equalsIgnoreCase("0")){
            return "0";
        }
        String sql = "DELETE FROM booksdata WHERE Book_ID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, bookid);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book has been removed.");
            }
            preparedStatement.close();
            return "1";
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static void Init(){
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected.");
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

}
