import database

mydbobj = database.connector()
    
def ViewAllBooks():
    query = 'SELECT * FROM Books'  
    result = mydbobj.execute(query)
    if len(result) > 0:
        ans = ''
        for i in range(0, len(result)):
            data_index = mydbobj.mycursor.column_names.index('ID')
            bookid = result[i][data_index]
            bookname = GetBookData('Name', bookid)
            query2 = 'SELECT * FROM BookGenres WHERE ID =' + str(bookid)  
            result2 = mydbobj.execute(query2)
            
            Genres = ''
            if len(result2) > 0:
                for j in range(0, len(result2)):
                    genre = result2[j][1]
                    if j == len(result2)-1:
                        Genres += genre
                    else:
                        Genres += genre + ', '
            
            ans = ans + 'ID: ' + str(bookid) + '\n' + 'Name: ' + str(bookname) + '\n' + 'Genres: ' + Genres + '\n\n'
        return ans
    else:
        return 'NA'   
    
def ViewAvailableBooks():
    query = 'SELECT * FROM Books WHERE Semaphore = 1'  
    result = mydbobj.execute(query)
    if len(result) > 0:
        ans = ''
        for i in range(0, len(result)):
            data_index = mydbobj.mycursor.column_names.index('ID')
            bookid = result[i][data_index]
            bookname = GetBookData('Name', bookid)
            query2 = 'SELECT * FROM BookGenres WHERE ID =' + str(bookid)  
            result2 = mydbobj.execute(query2)
            
            Genres = ''
            if len(result2) > 0:
                for j in range(0, len(result2)):
                    print(result2)
                    genre = result2[j][1]
                    if j == len(result2)-1:
                        Genres += genre
                    else:
                        Genres += genre + ', '
            
            ans = ans + 'ID: ' + str(bookid) + '\n' + 'Name: ' + str(bookname) + '\n' + 'Genres: ' + Genres + '\n\n'
        return ans
    else:
        return 'NA'  

def GetBookData(data, id):
    if data == 'Genre':
        query = 'SELECT * FROM BookGenres WHERE ID = ' + str(id)
        result = mydbobj.execute(query)
        if result:
            string = ''
            for i in range(0, len(result)):
                data_index = mydbobj.mycursor.column_names.index(data)
                if i != len(result)-1:
                    string += result[i][data_index] + ', '
                else:
                    string += result[i][data_index]
            return string
        else:
            return "NA"
    query = 'SELECT * FROM Books WHERE ID = ' + str(id)
    result = mydbobj.execute(query)
    if result:
        data_index = mydbobj.mycursor.column_names.index(data)
        return result[0][data_index]
    else:
        return "NA"

def GetBookID(name):
    query = 'SELECT * FROM Books WHERE Name = \'' + name + '\''
    result = mydbobj.execute(query)
    if result:
        data_index = mydbobj.mycursor.column_names.index('ID')
        return result[0][data_index]
    else:
        return "NA"
    
def SetBookData(data, id, value):
    query = 'UPDATE Books SET ' + data + ' = ' + value + ' WHERE ID = ' + str(id)
    mydbobj.execute(query)
    
def AddBook(data, genres):
    query = 'INSERT INTO Books (Name, Book_AddedThrough) VALUES (\'{}\', \'{}\')'.format(data[0], data[1]) 
    mydbobj.execute(query)
    
    id = GetBookID(data[0])
    
    for genre in genres:
        query = 'INSERT INTO BookGenres (ID, Genre) VALUES (\'{}\', \'{}\')'.format(id, genre) 
        mydbobj.execute(query)
    return 'Book added.'
    
def RemoveBook(id):
    semaphore = int(GetBookData('Semaphore', id))
    if semaphore == 0:
        return 'Book is unavailable at the moment.'
    query0 = 'DELETE FROM BookGenres WHERE ID = ' + str(id)
    mydbobj.execute(query0)
    query = 'DELETE FROM Books WHERE ID = ' + str(id)
    mydbobj.execute(query)
    return 'Book removed.'

#SetBookData('Book_Fine', 8, '500')
#print(GetBookData('Book_Fine', 8))

#print(GetBookData('Genre', GetBookID('Chainsaw Man')))
#RemoveBook(7)
#print(ViewAllBooks())

    