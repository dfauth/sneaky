package com.github.dfauth.sneaky

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("sneaky")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val httpPort = system.settings.config.getInt("sneaky.http.port")
  new ServiceLifecycleImpl(system, materializer, port = httpPort) {
    override val route: Route = SneakyStreamManager(system, materializer).route ~ static
  }.start()

  Await.result(system.whenTerminated, Duration.Inf)

}
