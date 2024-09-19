package assignment3.darren.util

import assignment3.darren.model.TodoItem
import scalikejdbc._

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"

  Class.forName(derbyDriverClassname)

  ConnectionPool.singleton(dbURL,"Darren","yes")

  implicit val session = AutoSession
}

object Database extends Database{
  def setupDB() = {
    if (!hasDBInitialize){
      TodoItem.initializeTable()
    }
  def hasDBInitialize: Boolean = {
    DB getTable "todoItem" match{
      case Some(x) => true
      case None => false
    }
  }}
}
