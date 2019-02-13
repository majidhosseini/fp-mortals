package me.fpmortals.oauth2

import me.fpmortals.drone.Epoch

trait LocalClock[F[_]] {
  def now: F[Epoch]
}
