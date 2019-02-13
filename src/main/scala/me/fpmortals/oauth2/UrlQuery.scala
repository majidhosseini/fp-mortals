package me.fpmortals.oauth2

import java.net.URLEncoder
import java.util.regex.Pattern

import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.string.Url
import scalaz._
import Scalaz._
import simulacrum.typeclass

sealed abstract class UrlEncoded
object UrlEncoded {
  private[this] val valid: Pattern =
    Pattern.compile("\\A(\\p{Alnum}++|[-.*_+=&]++|%\\p{XDigit}{2})*\\z") // scalafix:ok

  implicit def urlValidate: Validate.Plain[String, UrlEncoded] =
    Validate.fromPredicate(
      s => valid.matcher(s).find(),
      identity,
      new UrlEncoded {}
    )
}

final case class UrlQuery(params: List[(String, String)])

@typeclass trait UrlQueryWriter[A] {
  def toUrlQuery(a: A): UrlQuery
}

@typeclass trait UrlEncodeWriter[A] {
  def toUrlEncoded(a: A): String Refined UrlEncoded
}

object UrlEncodeWriter {
  import ops._

  def instance[T](f: T => String Refined UrlEncoded): UrlEncodeWriter[T] =
    new UrlEncodeWriter[T] {
      override def toUrlEncoded(t: T): String Refined UrlEncoded = f(t)
    }

  implicit val encoded: UrlEncodeWriter[String Refined UrlEncoded] = instance(identity)
  implicit val string: UrlEncodeWriter[String] = (s => Refined.unsafeApply(URLEncoder.encode(s, "UTF-8")))
  implicit val url: UrlEncodeWriter[String Refined Url] = (s => s.value.toUrlEncoded)
  implicit val long: UrlEncodeWriter[Long] = (s => Refined.unsafeApply(s.toString))
  implicit def ilist[K: UrlEncodeWriter, V: UrlEncodeWriter]: UrlEncodeWriter[IList[(K, V)]] = { m =>
    val raw = m.map {
      case (k, v) => k.toUrlEncoded.value + "=" + v.toUrlEncoded.value
    }.intercalate("&")
    Refined.unsafeApply(raw)
  }
}
