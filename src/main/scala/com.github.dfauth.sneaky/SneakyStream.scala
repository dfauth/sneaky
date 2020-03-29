package com.github.dfauth.sneaky

import java.util.concurrent.CompletionStage

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl.{Keep, Sink, Source, Tcp}
import com.typesafe.scalalogging.LazyLogging
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.concurrent.Future

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val sneakyStreamJsonFormat: RootJsonFormat[SneakyStream] = jsonFormat3(SneakyStream)
}


case class SneakyStream(localPort: Int, hostname: String, port: Int) extends LazyLogging {

  def stop:Unit = {}

  def run(implicit system:ActorSystem) = {
    val connections: Source[IncomingConnection, Future[ServerBinding]] = Tcp().bind("0.0.0.0", localPort)

    connections.map((in: Tcp.IncomingConnection) => {
      logger.info("New connection from: " + in.remoteAddress)
      val out = Tcp().outgoingConnection(hostname, port)
      in.handleWith(out)
    }).to(Sink.ignore).run()
  }

}
