package me.fpmortals.oauth2

import UrlEncodeWriter.ops._
import scalaz.IList

object AuthRequest {
  implicit val query: UrlQueryWriter[AuthRequest] = { a =>
    UrlQuery(List(
      ("redirect_uri" -> a.redirect_uri.value),
      ("scope" -> a.scope),
      ("client_id" -> a.client_id),
      ("prompt" -> a.prompt),
      ("response_type" -> a.response_type),
      ("access_type" -> a.access_type)
    ))
  }
}

object AccessRequest {
  implicit val encoded: UrlEncodeWriter[AccessRequest] = { a =>
    IList(
      "code" -> a.code.toUrlEncoded,
      "redirect_uri" -> a.redirect_uri.toUrlEncoded,
      "client_id" -> a.client_id.toUrlEncoded,
      "client_secret" -> a.client_secret.toUrlEncoded,
      "scope" -> a.scope.toUrlEncoded,
      "grant_type" -> a.grant_type.toUrlEncoded,
    ).toUrlEncoded
  }
}

object RefteshRequest {
  implicit val encoded: UrlEncodeWriter[RefreshRequest] = { r =>
    IList(
      "client_secret" -> r.client_secret.toUrlEncoded,
      "refresh_token" -> r.refresh_token.toUrlEncoded,
      "client_id" -> r.client_id.toUrlEncoded
    ).toUrlEncoded
  }
}