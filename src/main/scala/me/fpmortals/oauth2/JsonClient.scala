package me.fpmortals.oauth2

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Url
import jsonformat.JsDecoder
import scalaz.IList

trait JsonClient[F[_]] {
  def get[A: JsDecoder](
    uri: String Refined Url,
    headers: IList[(String, String)]
  ): F[A]

  def post[P: UrlEncodeWriter, A: JsDecoder](
    uri: String Refined Url,
    payload: P,
    headers: IList[(String, String)] = IList.empty
  ): F[A]
}
