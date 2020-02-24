package com.github.dfauth.sneaky

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import com.github.dfauth.sneaky.JsonSupport._
import spray.json._

case object SneakyStreamManager {
  def apply(system: ActorSystem) = new SneakyStreamManager(system)
}

case class SneakyStreamManager(system: ActorSystem) {

  var streams:Seq[SneakyStream] = Seq.empty[SneakyStream]

  def route = {
    path("manage") { concat (
      get {
        complete(streams.map {_.toJson.toString})
      },
      post {
        entity(as[SneakyStream]) { s =>
          streams :+ s
          s.run(system)
          complete(streams.map {_.toJson.toString})
        }
      },
      pathSuffix("d".r) { id =>
        delete {
          entity(as[SneakyStream]) { s =>
            streams.filter(_.localPort == s.localPort).foreach(_.stop)
            streams = streams.filterNot(_.localPort == s.localPort)
            complete(streams.map {_.toJson.toString})
          }
        }
      }
    )}
  }
}
