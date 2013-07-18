
package models
//import scala.slick.driver.H2Driver.simple._
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import java.sql.Date

object TmpTeasingMiniNews extends Table[(Int, Int, Int, Date, Option[String], Option[String], Option[Int],
  Option[Date])]("TmpTeasingMiniNews") {

  val ourIds = Set (
    14045958, //Mathieu
    2888342, //Jean-Luc
    19000174, // Inigo
    460555 // Francois
  )

  def miniNewsId = column[Int]("MiniNewsId", O.PrimaryKey)
  def memberId = column[Int]("MemberID")
  def categoryId = column[Int]("CategoryID")
  def modificationDate = column[Date]("ModificationDate")
  def infos = column[Option[String]]("Infos")
  def lang = column[Option[String]]("Lang")
  def idType = column[Option[Int]]("IDType")
  def creationDate = column[Option[Date]]("CreationDate")
  def * = miniNewsId ~ memberId ~ categoryId ~ modificationDate ~ infos ~ lang ~ idType ~ creationDate
}

object CompanyProfile extends Table[(Int, String)]("CompanyProfile") {

  def companyProfileId = column[Int]("CompanyProfileId", O.PrimaryKey)
  def name = column[String]("name")
  def * = companyProfileId ~ name 
}

object Position extends Table[(Int, Int, Int)]("Position") {

  def positionId = column[Int]("PositionID", O.PrimaryKey)
  def memberId = column[Int]("MemberID")
  def companyProfileId = column[Int]("CompanyProfileId")
  def * = positionId ~ memberId ~ companyProfileId
}

object Member extends Table[(Int, String, String, Option[String])]("Member") {

  def memberId = column[Int]("MemberID", O.PrimaryKey)
  def firstName = column[String]("FirstName")
  def lastName = column[String]("LastName")
  def nickName = column[Option[String]]("NickName")
  def * = memberId ~ firstName ~ lastName ~ nickName
}

object Contact extends Table[(Int, Int, Int)]("Contact") {

  def contactId = column[Int]("ContactID", O.PrimaryKey)
  def memberId = column[Int]("MemberID")
  def knownMemberId = column[Int]("KnownMemberID")
  def * = contactId ~ memberId ~ knownMemberId
}

object MainApp extends App {

  val POSITION_INFO_TYPE = 3
  val NEW_CONTACT_TYPE = 8 

  Database.forURL("jdbc:mysql://192.168.1.170:33633/viaduc", driver = "com.mysql.jdbc.Driver", user = "imediavilla", password =
    "Ohp0oa+goiCe") withSession {
      //Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
      // The session is never named explicitly. It is bound to the current
      // thread as the threadLocalSession that we imported
      //(TmpTeasingMiniNews.ddl ++ CompanyProfile.ddl ++ Position.ddl ++ Member.ddl ++ Contact.ddl).create

      val selectContactIds = for {
        contact <- Contact if contact.memberId inSet TmpTeasingMiniNews.ourIds
      } yield (contact.memberId, contact.knownMemberId)

      println("Member's contacts")
      println(selectContactIds.selectStatement)
      
      //val contacts = selectContactIds.list.groupBy(_._1).map(Function.tupled((member, contact) => member -> contact.map(_._2)))
      //println(contacts)

      val selectMiniNews = for {
          miniNews <- TmpTeasingMiniNews if miniNews.memberId inSet TmpTeasingMiniNews.ourIds 
      } yield (miniNews)

      val selectCompanyProfile = for {
        miniNews <- selectMiniNews if miniNews.categoryId === POSITION_INFO_TYPE
        position <- Position if miniNews.idType === position.positionId
        companyProfile <- CompanyProfile if position.companyProfileId === companyProfile.companyProfileId
      } yield (miniNews, companyProfile)

      val selectNewContacts = for {
        miniNews <- selectMiniNews if miniNews.categoryId === NEW_CONTACT_TYPE
        newContact <- Member if miniNews.idType === newContact.memberId
      } yield (miniNews, newContact)

      println("New Positions")
      println(selectCompanyProfile.selectStatement)
      println("New Contacts")
      println(selectNewContacts.selectStatement)
  }

}

