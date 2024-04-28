import mysql.connector
from mysql.connector import Error
  
class connector:
  def __init__(self):
    self.mydb = mysql.connector.connect(
      host="localhost",   
      user="root",
      password="vibhor23",
      database="librarymanager"
    )
    self.mycursor = self.mydb.cursor()
    self.maxissue = 2
    self.maxreserve = 2
    self.fineincrement = 1
    
  def execute(self, query):
    try:
      self.mycursor.execute(query)
      if query.lower().startswith('select'):
        return self.mycursor.fetchall()  
      else:
        self.mydb.commit() 
        return True
    except mysql.connector.Error as err:
      print(f"Error: {err}")
      return None 
  
  def close(self):
    self.mycursor.close()
    self.mydb.close()
    
  def SearchForNumber(self, table, num):
      query = 'SELECT * FROM '+table+' WHERE Phone_Number = '+num
      result = self.execute(query)
      if len(result) == 0:
          return False
      else:
          return True
      
  def GetAuth(self, num):
      if self.SearchForNumber('admin', num) == 1:
          return 'Admin'
      elif self.SearchForNumber('user', num) == 1:
          return 'User'
      else:
          return 'NA'
