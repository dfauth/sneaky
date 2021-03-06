package com.github.dfauth

import akka.http.scaladsl.server.Directives.{getFromResource, getFromResourceDirectory, path, pathPrefix, _}

package object sneaky {

  def static =
    path("") {
      getFromResource("static/index.html")
    } ~ pathPrefix("") {
      getFromResourceDirectory("static")
    }


}
