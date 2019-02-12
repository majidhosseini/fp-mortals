package me.fpmortals.drone

import scalaz.Scalaz._
import scalaz.{Scalaz, _}

object Data {
  val node1 = MachineNode("1243d1af-828f-4ba3-9fc0-a19d86852b5a")
  val node2 = MachineNode("550c4943-229e-47b0-b6be-3d686c5f013f")
  val managed = NonEmptyList(node1, node2)

  val time1: Epoch = epoch"2017-03-03T18:07:00Z"
  val time2: Epoch = epoch"2017-03-03T18:59:00Z" // +52 mins
  val time3: Epoch = epoch"2017-03-03T19:06:00Z" // +59 mins
  val time4: Epoch = epoch"2017-03-03T23:07:00Z" // +5 hours

  val needsAgents = WorldView(5, 0, managed, Map.empty, Map.empty, time1)
}

//object EpochInterpolator extends Verifier[Epoch] {
//  def check(s: String): Either[(Int, String), Epoch] = {
//    try Right(Epoch(Instant.parse(s).toEpochMilli))
//    catch {
//      case _ =>
//        Left((0, "not in ISO-8601 format"))
//    }
//  }
//}
//
//implicit class EpochMillisStringContext(sc: StringContext) {
//  val epoch = Prefix(EpochInterpolator, sc)
//  Prefix(EpochInterpolator, sc)
//}

class Mutable(state: WorldView) {
  var started, stopped: Int = 0

  private val D: Drone[Id] = new Drone[Id] {
    def getBacklog: Scalaz.Id[Int] = state.backlog
    def getAgents: Scalaz.Id[Int] = state.agents
  }

  private val M: Machines[Id] = new Machines[Id] {
    def getTime: Scalaz.Id[Epoch] = state.time
    def getManaged: Scalaz.Id[NonEmptyList[MachineNode]] = state.managed
    def getAlive: Scalaz.Id[Map[MachineNode, Epoch]] = state.alive
    def start(node: MachineNode): Scalaz.Id[MachineNode] = { started += 1; node }
    def stop(node: MachineNode): Scalaz.Id[MachineNode] = { stopped += 1; node }
  }

  val program = new DynAgentsModule[Id](D, M)
}