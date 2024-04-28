from ast import Add
from typing import Set
import database
import datetime
import bookmanager

mydbobj = database.connector()

def AddUser(data):
    query = 'INSERT INTO User (Phone_Number, Name) VALUES (\'{}\', \'{}\')'.format(data[0], data[1]) 
    mydbobj.execute(query)
    return 'User added.'

def RemoveUser(num):
    query = 'DELETE FROM User WHERE Phone_Number = ' + num
    mydbobj.execute(query)
    return 'User removed.'

def GetUserData(data, num):
    query = 'SELECT * FROM User WHERE Phone_Number = ' + num
    result = mydbobj.execute(query)
    if result:
        data_index = mydbobj.mycursor.column_names.index(data)
        return result[0][data_index]
    else:
        return "NA"

def SetUserData(data, num, value):
    query = 'UPDATE User SET ' + data + ' = \'' + value + '\' WHERE Phone_Number = ' + num
    if value == 'NULL':
        query = 'UPDATE User SET ' + data + ' = ' + value + ' WHERE Phone_Number = ' + num
    mydbobj.execute(query)

def GetUserBookIssued(num):
    query = 'SELECT * FROM Issue WHERE IssuedBy = ' + num
    result = mydbobj.execute(query)
    if result:
        ans = ''
        for i in range(0, len(result)):
            issue_id_data_index = mydbobj.mycursor.column_names.index('Issue_ID')
            id_data_index = mydbobj.mycursor.column_names.index('ID')
            issuedate_data_index = mydbobj.mycursor.column_names.index('IssuedDate')
            duedate_data_index = mydbobj.mycursor.column_names.index('DueDate')
            bookfine_data_index = mydbobj.mycursor.column_names.index('Book_Fine') 
            issue_id = result[i][issue_id_data_index]          
            bookid = result[i][id_data_index]
            bookname = bookmanager.GetBookData('Name', bookid)
            issuedate = result[i][issuedate_data_index]
            duedate = result[i][duedate_data_index]
            bookfine = result[i][bookfine_data_index]

            current_date = datetime.date.today()
            if current_date > duedate:
                # Calculate the number of days overdue
                days_overdue = (current_date - duedate).days
                # Assuming the fine is a fixed amount per day overdue
                bookfine = days_overdue * mydbobj.fineincrement  # replace fixed_fine_per_day with the actual fine rate
                query2 = 'UPDATE Issue SET Book_Fine = ' + str(bookfine) + 'WHERE Issue_ID = ' + str(issue_id)
                mydbobj.execute(query2)
                
            ans += 'ID: ' + str(bookid) + '\n' + 'Name: ' + str(bookname) + '\n'
            ans += 'IssuedDate: ' + str(issuedate) + '\n' + 'DueDate: ' + str(duedate) + '\n'
            ans += 'Book Fine: ' + str(bookfine) + '\n\n'
            
        return ans
    else:
        return 'No books issued.'

def GetUserBookReserved(num):
    query = 'SELECT * FROM Reserve WHERE ReservedBy = ' + num
    result = mydbobj.execute(query)
    if result:
        ans = ''
        for i in range(0, len(result)):
            id_data_index = mydbobj.mycursor.column_names.index('ID')
            reservedate_data_index = mydbobj.mycursor.column_names.index('ReservedDate')          
            bookid = result[i][id_data_index]
            bookname = bookmanager.GetBookData('Name', bookid)
            reservedate = result[i][reservedate_data_index]
            ans += 'ID: ' + str(bookid) + '\n' + 'Name: ' + str(bookname) + '\n'
            ans += 'ReserveDate: ' + str(reservedate) + '\n\n'
        return ans
    else:
        return 'No books reserved.'
    
def BanUser(num, adminnum):
    SetUserData('Banned', num, '1')
    SetUserData('BannedBy', num, adminnum)
    return 'User banned.'
    
def UnbanUser(num):
    SetUserData('Banned', num, 'NULL')
    SetUserData('BannedBy', num, 'NULL')
    return 'User unbanned.'

def Issue(bookid, num, adminnum):
    if int(bookmanager.GetBookData('Semaphore', bookid)) == 0:
        query = 'SELECT * FROM Reserve WHERE ID = ' + str(bookid)
        result = mydbobj.execute(query)
        if result:
            data_index = mydbobj.mycursor.column_names.index('ReservedBy')
            reservedby = result[0][data_index]
            if reservedby != num:
                return 'Book unavailable.'
            else:
                CancelReserve(bookid, num)
        else:
            return 'Book unavailable.'
    if int(GetUserData('BooksIssued', num)) >= mydbobj.maxissue:
        return 'Max books already issued.'
    # Calculate dates
    current_date = datetime.date.today()
    future_date = current_date + datetime.timedelta(days=15)

    # Format dates as strings
    current_date_str = current_date.strftime('%Y-%m-%d')
    future_date_str = future_date.strftime('%Y-%m-%d')
    query = 'INSERT INTO Issue (ID, IssuedBy, IssuedThrough, IssuedDate, DueDate) VALUES (\'{}\', \'{}\', \'{}\', \'{}\', \'{}\')'.format(bookid, num, adminnum, current_date_str, future_date_str) 
    mydbobj.execute(query)
    
    SetUserData('BooksIssued', num, str(GetUserData('BooksIssued', num)+1))
    bookmanager.SetBookData('Semaphore', bookid, '0')
    
    return 'Book issued.'

def Return(bookid, num):
    if int(GetUserData('BooksIssued', num)) <= 0:
        return 'No Books issued.'
    query = 'SELECT * FROM Issue WHERE ID = ' + str(bookid)
    result = mydbobj.execute(query)
    if result:
        data_index = mydbobj.mycursor.column_names.index('Issue_ID')
        issue_id = result[0][data_index]
        
        query = 'DELETE FROM Issue WHERE Issue_ID = ' + str(issue_id)
        mydbobj.execute(query)
        
        SetUserData('BooksIssued', num, str(GetUserData('BooksIssued', num)-1))
        bookmanager.SetBookData('Semaphore', bookid, '1')
        
        return 'Book returned.'
    else:
        return 'No book issued found with the given id.'

def Reserve(bookid, num):
    if int(bookmanager.GetBookData('Semaphore', bookid)) == 0:
        return 'Book unavailable.'
    if int(GetUserData('BooksReserved', num)) >= mydbobj.maxreserve:
        return 'Max books already reserved.'
    # Calculate dates
    current_date = datetime.date.today()

    # Format dates as strings
    current_date_str = current_date.strftime('%Y-%m-%d')
    query = 'INSERT INTO Reserve (ID, ReservedBy, ReservedDate) VALUES (\'{}\', \'{}\', \'{}\')'.format(bookid, num, current_date_str) 
    mydbobj.execute(query)
    
    SetUserData('BooksReserved', num, str(GetUserData('BooksReserved', num)+1))
    bookmanager.SetBookData('Semaphore', bookid, '0')
    
    return 'Book reserved.'
    
def CancelReserve(bookid, num):
    if int(GetUserData('BooksReserved', num)) <= 0:
        return 'No Books reserved.'
    query = 'SELECT * FROM Reserve WHERE ID = ' + str(bookid)
    result = mydbobj.execute(query)
    if result:
        data_index = mydbobj.mycursor.column_names.index('Reservation_ID')
        reservation_id = result[0][data_index]
        
        query = 'DELETE FROM Reserve WHERE Reservation_ID = ' + str(reservation_id)
        mydbobj.execute(query)
        
        SetUserData('BooksReserved', num, str(GetUserData('BooksReserved', num)-1))
        bookmanager.SetBookData('Semaphore', bookid, '1')
        
        return 'Book reservation cancelled.'
    else:
        return 'No book reserved with the given id.'
    
#UnbanUser('+917882345621')
#print(Return(10, '+917882345621'))
#print(Issue(10, '+917882345621', '+919999367202'))
#print(CancelReserve(9, '+917882345621'))
#print(GetUserBookIssued('+917882345621'))