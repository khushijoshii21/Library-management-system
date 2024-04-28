import adminmanager
import bookmanager
import usermanager
import database

def AdminMenu():
    return """
                Admin Menu:
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
                11. View user issued books
                """

def UserMenu():
    return """
            Main Menu:
            1. View your issued books
            2. Book Reservation
            3. View books
            """

def ReservationMenu():
    return """
            Reservation Menu:
            1. Reserve a book
            2. Cancel reservation
            3. View reservation
            4. Return
            """

def Handler(msgbody, sender):
    mydbobj = database.connector()
    userauth = mydbobj.GetAuth(sender)
    print(userauth)
    if userauth == 'Admin':
        mode = adminmanager.GetAdminData('Mode', sender)
        if mode == 'AdminMenu':
            if msgbody == '1':
                adminmanager.SetUserData('Mode', sender, 'IssueBook')
                return "Enter user phone number and book ID in the following format: \nPhone_Number/Book_ID"
            elif msgbody == '2':
                adminmanager.SetUserData('Mode', sender, 'ReturnBook')
                return "Enter user phone number and book ID in the following format: \nPhone_Number/Book_ID"
            elif msgbody == '3':
                adminmanager.SetUserData('Mode', sender, 'AddBook')
                return """
                Enter Book details in the given format:
                Book_Name/Book_Genre
                """
            elif msgbody == '4':
                adminmanager.SetUserData('Mode', sender, 'RemoveBook')
                return """
                Enter the Book ID that you wish to remove:
                """
            elif msgbody == '5':
                adminmanager.SetUserData('Mode', sender, 'BanUser')
                return "Enter user phone number: "
            elif msgbody == '6':
                adminmanager.SetUserData('Mode', sender, 'UnbanUser')
                return "Enter user phone number: "
            elif msgbody == '7':
                adminmanager.SetUserData('Mode', sender, 'AddAdmin')
                return "Enter new admin details: \nPhone_Number/Name"
            elif msgbody == '8':
                adminmanager.SetUserData('Mode', sender, 'RemoveAdmin')
                return "Enter admin phone number: (+91XXXXXXXXXX)"
            elif msgbody == '9':
                return bookmanager.ViewAllBooks()
            elif msgbody == '10':
                adminmanager.SetUserData('Mode', sender, 'ClearUserData')
                return 'Enter user phone number: (+91XXXXXXXXXX)'
            elif msgbody == '11':
                adminmanager.SetUserData('Mode', sender, 'ViewUserBooksIssued')
                return 'Enter user phone number: (+91XXXXXXXXXX)'
            else:
                return AdminMenu()
        elif mode == 'IssueBook':
            num, bookid = msgbody.split('/')
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return usermanager.Issue(bookid, num, sender)
        elif mode == 'ReturnBook':
            num, bookid = msgbody.split('/')
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return usermanager.Return(bookid, num)
        elif mode == 'AddBook':
            name, genres = msgbody.split('/')
            genres_list = genres.split(' ')
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return bookmanager.AddBook([name, sender], genres_list)
        elif mode == 'RemoveBook':
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return bookmanager.RemoveBook(msgbody)
        elif mode == 'BanUser':
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return usermanager.BanUser(msgbody, sender)
        elif mode == 'UnbanUser':
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return usermanager.UnbanUser(msgbody)
        elif mode == 'AddAdmin':
            adminnum, name = msgbody.split('/')
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return adminmanager.AddAdmin([adminnum, name])
        elif mode == 'RemoveAdmin':
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return adminmanager.RemoveAdmin(msgbody)
        elif mode == 'ClearUserData':
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return usermanager.RemoveUser(msgbody)
        elif mode == 'ViewUserBooksIssued':
            adminmanager.SetUserData('Mode', sender, 'AdminMenu')
            return usermanager.GetUserBookIssued(msgbody)
        else:
            return '-1'
    elif userauth == 'User':
        isBanned = usermanager.GetUserData('Banned', sender)
        if isBanned == 1:
            return 'You are banned.'
        mode = usermanager.GetUserData('Mode', sender)
        if mode == 'GetUserName':
                usermanager.SetUserData('Name', sender, msgbody)
                usermanager.SetUserData('Mode', sender, 'UserMenu')
                return UserMenu()
        if mode == 'UserMenu':
            if msgbody == '1':
                return usermanager.GetUserBookIssued(sender)
            elif msgbody == '2':
                usermanager.SetUserData('Mode', sender, 'ReservationMenu')
                return ReservationMenu()
            elif msgbody == '3':
                return bookmanager.ViewAvailableBooks()
            else:
                return UserMenu()
        elif mode == 'ReservationMenu':
            if msgbody == '1':
                usermanager.SetUserData('Mode', sender, 'ReserveBook')
                return "Enter book ID: "
            elif msgbody == '2':
                usermanager.SetUserData('Mode', sender, 'CancelReserveBook')
                return "Enter book ID: "
            elif msgbody == '3':
                return usermanager.GetUserBookReserved(sender)
            elif msgbody == '4':
                usermanager.SetUserData('Mode', sender, 'UserMenu')
                return UserMenu()
            else:
                return ReservationMenu()       
        elif mode == 'ReserveBook':
            usermanager.SetUserData('Mode', sender, 'ReservationMenu')
            return usermanager.Reserve(msgbody, sender)
        elif mode == 'CancelReserveBook':
            usermanager.SetUserData('Mode', sender, 'ReservationMenu')
            return usermanager.CancelReserve(msgbody, sender)
        else:
            return '-1'
    else:
        usermanager.AddUser([sender, 'NA'])
        usermanager.SetUserData('Mode', sender, 'GetUserName')
        return """
        Hi! It seems this is your first time here.
        Please enter your name.
        """