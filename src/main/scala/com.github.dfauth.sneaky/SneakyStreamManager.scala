package com.github.dfauth.sneaky

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import com.github.dfauth.sneaky.JsonSupport._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global

case class SneakyStreamManager(system: ActorSystem, materializer:ActorMaterializer) {

  var streams:Seq[SneakyStream] = Seq.empty[SneakyStream]

  def route = {
    path("manage") { concat (
      get {
        complete(streams.map {_.toJson})
      },
      post {
        entity(as[SneakyStream]) { s =>
          streams = streams :+ s
          complete(s.run(system, materializer).map(r => streams.map {_.toJson}))
//          s.run(system).onComplete {
//            case Success(u) => complete(streams.map {_.toJson.toString})
//            case Failure(t) => complete(StatusCodes.InternalServerError)
//          }
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
