package me.fpmortals.oauth2

import scalaz._
import Scalaz._
import UrlQueryWriter.ops._

class OAuth2client[F[_]: Monad](
  config: ServerConfig
)(
  user: UserInteraction[F],
  client: JsonClient[F],
  clock: LocalClock[F]
) {

  def authenticate: F[CodeToken] = for {
    callback <- user.start
    params = AuthRequest(callback, config.scope, config.clientId)
    _ <- user.open(params.toUrlQuery.forUrl(config.auth))
    code <- user.stop
  } yield code

  def access(code: CodeToken): F[(RefreshToken, BearerToken)] = for {
    request <- AccessRequest(
      code.token,
      code.redirect_uri,
      config.clientId,
      config.clientSecret
    ).pure[F]
    msg <- client.post[AccessRequest, AccessResponse](config.access, request)
    time <- clock.now
    expires = time + msg.expires_in.seconds
    refresh = RefreshToken(msg.refresh_token)
    bearer = BearerToken(msg.access_token, expires)
  } yield (refresh, bearer)

}
