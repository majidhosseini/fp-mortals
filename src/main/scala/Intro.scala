package me.fpmortals

//import scalaz._, Scalaz._
//import simulacrum._

import scala.concurrent.Future

trait TerminalSync {
  def read(): String
  def write(t: String): Unit
}

trait TerminalAsync {
  def read(): Future[String]
  def write(t: String): Future[Unit]
}




object Intro {

  trait Foo[C[_]] {
    def create(i: Int): C[Int]
  }

  object FooList extends Foo[List] {
    def create(i: Int): List[Int] = List(i)
  }

  object FooOption extends Foo[Option] {
    def create(i: Int): Option[Int] = Option(i)
  }

  type EitherString[T] = Either[String, T]

  object FooEitherString extends Foo[EitherString] {
    def create(i: Int): Either[String, Int] = Right(i)
  }

  type Id[T] = T

  object FooId extends Foo[Id] {
    def create(i: Int): Int = i
  }


  trait Terminal[C[_]] {
    def read: C[String]
    def write(t: String): C[Unit]
  }

  type Now[X] = X

  object TerminalSync extends Terminal[Now] {
    def read: String = ???
    def write(t: String): Unit = ???
  }

  object TerminalAsync extends Terminal[Future] {
    def read: Future[String] = ???
    def write(t: String): Future[Unit] = ???
  }

  // is Monad
  trait Execution[C[_]] {
    // is bind
    def chain[A, B](c: C[A])(f: A => C[B]): C[B]
    // is pure
    def create[B](b: B): C[B]
  }

  def echo[C[_]](t: Terminal[C], e: Execution[C]): C[String] = {
    e.chain(t.read) { in: String =>
      e.chain(t.write(in)) { _: Unit =>
        e.create(in)
      }
    }
  }

  val reatT = TerminalSync.read
  TerminalSync.write(reatT)

  // Monad
  object Execution {
    implicit class Ops[A, C[_]](c: C[A]) {
      def flatMap[B](f: A => C[B])(implicit e: Execution[C]): C[B] = e.chain(c)(f)
      def map[B](f: A => B)(implicit e: Execution[C]): C[B] = e.chain(c)(f andThen e.create)
    }
  }

  import Execution._
  def echo2[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] = {
    t.read.flatMap { in: String =>
      t.write(in).map { _: Unit =>
        in
      }
    }
  }

  def echo3[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] = {
    for {
      in <- t.read
      _ <- t.write(in)
    } yield in
  }

  final class IO[A](val interpret: () => A) {
    def map[B](f: A => B): IO[B] = IO(f(interpret()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(f(interpret())).interpret()
    def flatMap_Unknown[B](f: A => IO[B]): IO[B] = f(interpret()) // this is maybe unsafe
  }

  object IO {
    def apply[A](a: => A): IO[A] = new IO(() => a)
  }

  object TerminalIO extends Terminal[IO] {
    def read: IO[String] = IO {
      io.StdIn.readLine
    }

    def write(t: String): IO[Unit] = IO { println(t) }
  }

  import TerminalIO._
  import IO._
  //val dealyed: IO[String] = echo3[IO]

//  delayd.interpret()
}

