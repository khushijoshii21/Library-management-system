from ast import Add
from typing import Set
import database

mydbobj = database.connector()

def AddAdmin(data):
    query = 'INSERT INTO Admin (Phone_Number, Name) VALUES (\'{}\', \'{}\')'.format(data[0], data[1]) 
    mydbobj.execute(query)
    return 'Admin added.'
 
def RemoveAdmin(num):
    query = 'DELETE FROM Admin WHERE Phone_Number = ' + num
    mydbobj.execute(query)
    return 'Admin removed.'
       
def GetAdminData(data, num):
    query = 'SELECT * FROM Admin WHERE Phone_Number = ' + num
    result = mydbobj.execute(query)
    if result:
        data_index = mydbobj.mycursor.column_names.index(data)
        return result[0][data_index]
    else:
        return "NA"

def SetUserData(data, num, value):
    query = 'UPDATE Admin SET ' + data + ' = \'' + value + '\' WHERE Phone_Number = ' + num
    print(query)
    mydbobj.execute(query)

SetUserData('Mode', '+919560405533', 'AdminMenu') 
print(GetAdminData('Mode', '+919999367202'))