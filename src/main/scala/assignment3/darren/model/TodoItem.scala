package assignment3.darren.model

import assignment3.darren.util.DateUtil.{DateFormatter, StringFormatter}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalikejdbc.DB.autoCommit
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import java.time.LocalDate
import scala.util.Try

class TodoItem (val shortDescriptionS: String, val detailsS: String){
  def this() = this("", "")
  var shortDescription = new StringProperty(shortDescriptionS)
  var details = new StringProperty(detailsS)
  var deadline = ObjectProperty[LocalDate](LocalDate.of(2024,8, 20))

  def save(): Try[Int] = {
    if (!(isExist)) {
      Try(DB autoCommit {implicit session =>
        sql"""
             insert into todoItem (shortDescription, details, deadline) values
             (${shortDescription.value}, ${details.value}, ${deadline.value.toString})
             """.update.apply()
      })
    } else {
      Try(DB autoCommit {implicit session =>
        sql"""
             update todoItem
             set
             shortDescription = ${shortDescription.value},
             details = ${details.value},
             deadline = ${deadline.value.asString}
              where shortDescription = ${shortDescription.value}
           """.update.apply()
      })
    }
  }

  def delete() : Try[Int] = {
    if (isExist){
      Try(DB autoCommit{ implicit session =>
        sql"""
             delete from todoItem where
              shortDescription = ${shortDescription.value}
           """.update.apply()
      })
    } else {
      throw new Exception ("To Do Item does not exists in database")
    }
  }

  def isExist: Boolean = {
    DB readOnly {implicit session =>
      sql"""
        select * from todoItem where
        shortDescription = ${shortDescription.value}
        """.map(rs => rs.string("shortDescription")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }
  }
}

object  TodoItem {
  def apply (
            shortDescriptionS: String,
            detailsS: String,
            deadlineS: String
            ): TodoItem ={
    new TodoItem(shortDescriptionS, detailsS){
      deadline.value = deadlineS.parseLocalDate
    }
  }

  def initializeTable() {
    DB autoCommit { implicit session =>
      sql"""
           create table todoItem (
            id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
            shortDescription varchar (60),
            details varchar (256),
            deadline varchar (64)
           )
         """.execute.apply()
    }
  }

  def getAllTodoItem: List[TodoItem] = {
    DB readOnly { implicit session =>
      sql"select * from todoItem".map(rs => TodoItem(rs.string("shortDescription"),
        rs.string("details"), rs.string("deadline") )).list.apply()
    }
  }
}
