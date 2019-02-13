package me.fpmortals.oauth2

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Url
import me.fpmortals.drone.Epoch

final case class CodeToken(
  token: String,
  redirect_uri: String Refined Url
)

final case class ServerConfig(
  auth: String Refined Url,
  access: String Refined Url,
  refresh: String Refined Url,
  scope: String,
  clientId: String,
  clientSecret: String
)

final case class RefreshToken(token: String)
final case class BearerToken(token: String, expires: Epoch)

trait UserInteraction[F[_]] {
  def start: F[String Refined Url]
  def open(uri: String Refined Url): F[Unit]
  def stop: F[CodeToken]
}
