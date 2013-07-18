package fr.viadeo

import scala.slick.driver.SQLiteDriver.simple._
import Database.threadLocalSession
import java.sql.Date

object Event extends Table[(String, String)]("CompanyProfile") {

    def time = column[String]("time")
    def actorname = column[String]("actorname")
    def * = time ~ actorname
}


object MainApp extends App {

  val POSITION_INFO_TYPE = 3
  val NEW_CONTACT_TYPE = 8

  Database.forURL("jdbc:sqlite:sample.db", driver = "org.sqlite.JDBC") withSession {

    // The session is never named explicitly. It is bound to the current
    // thread as the threadLocalSession that we imported
    (Event.ddl).create

    val selectContactIds = for {
      contact <- Event
    } yield (contact.time, contact.actorname)


    println(selectContactIds)


  }

}

