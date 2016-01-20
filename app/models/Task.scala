package models
import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._

/**
  * Created by Wojtek on 2016-01-20.
  */
case class Task(id: Long, label: String)

// We created a companion object to manage Task operations
object Task {
  def all(): List[Task] =
    DB.withConnection{implicit c =>
      SQL("SELECT * FROM task").as(task *)
    }
  // DB.withConnection - it's helper to create an release automatically a JDBC connection
  // task * - parser parse as many task rows as possible and then return a List[Task]
  // as - method allows to parse the ResultSet
  def create(label: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("INSERT INTO task (label) VALUES ({label})").on(
        'label -> label
      ).executeUpdate()
    }
  }
  def delete(id: Long): Unit = {
    DB.withConnection{ implicit c =>
      SQL("DELETE FROM task WHERE id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  // Here, task is a parser that, given a JDBC ResultSet row with at least an id and a label column, is able to create a Task value.
  val task = {
    get[Long]("id") ~
      get[String]("label") map {
      case id~label => Task(id, label)
    }
  }

}

