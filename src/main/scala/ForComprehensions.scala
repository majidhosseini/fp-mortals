package me.fpmortals

import scala.concurrent._
import scalaz._, Scalaz._
import simulacrum._


object ForComprehensions {

//  def getFromRedis(s: String): Future[Option[String]] = ???
//  def getFromSql(s: String): Future[Option[String]] = ???
//
//  val t1 = for {
//    i <- getFromRedis(???)
//    j <- getFromSql(???)
//  } yield i orElse j
//
//  val t2 = for {
//    cache <- getFromRedis(???)
//    res <- cache match {
//      case Some(_) => Future.successful(cache)
//      case _ => getFromSql(???)
//    }
//  } yield res





  ////


  def getA: Future[Option[Int]] = {
    Thread.sleep(1000)
    Future.successful(Some(1000))
  }
  def getB: Future[Option[Int]] = {
    Thread.sleep(1000)
    Future.successful(Some(400))
  }
  def getC: Future[Option[Int]] = {
    Thread.sleep(1000)
    Future.successful(Some(220))
  }
  def getD: Future[Option[Int]] = {
    Thread.sleep(1000)
    Future.successful(Some(333))
  }


//  val result = for {
//    a <- OptionT(getA)
//    b <- OptionT(getB)
//
//  } yield a * b
//
//
//  val res2 = for {
//    a <- getA |> ???
//  } yield true
}
