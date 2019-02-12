package me.fpmortals.data

import scalaz._
import Scalaz._
import eu.timepit.refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import refined.api.Refined

object survey {

  final case class Person private(name: String, age: Int)

  object Person {
    def apply(name: String, age: Int): Either[String, Person] = {
      if (name.nonEmpty && age > 0) Right(new Person(name, age))
      else Left(s"bad bad so bad input: $name, $age")
    }
  }

  def welcome(person: Person): String = s"$(person.name) you look like Luky like"

  for {
    p <- Person("", -1)
  } yield welcome(p)
}

final case class PersonM(
  name: String Refined NonEmpty,
  age: Int Refined Positive
)