package edu.eckerd.gpa

import java.util.concurrent.{ExecutorService, Executors}

import cats.data.NonEmptyList
import fs2._
import org.http4s.CacheDirective._
import org.http4s.MediaType._
import org.http4s.dsl._
import org.http4s.headers._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp
import org.http4s.{HttpService, _}

import scala.util.Properties.envOrNone
import scalatags.Text.TypedTag

object Server extends StreamApp {

  val port: Int = envOrNone("HTTP_PORT") map (_.toInt) getOrElse 8080
  val ip: String = "0.0.0.0"
  val pool: ExecutorService = Executors.newCachedThreadPool()


  val supportedStaticExtensions =
    List(".html", ".js", ".map", ".css", ".png", ".ico")

  override def stream(args: List[String]): Stream[Task, Unit] =
    BlazeBuilder
      .bindHttp(port, ip)
      .mountService(service)
      .withServiceExecutor(pool)
      .serve


  val index : TypedTag[String] = {
    import scalatags.Text.all._
    html(
      head(),
      body(
        script(src:="frontend-fastopt.js"),
        script(src:="frontend-jsdeps.js"),
        script("edu.eckerd.gpa.GpaApp().main()")
      )
    )
  }

  val service = HttpService {
    case req @ GET -> Root =>
      Ok(index.render)
        .withContentType(Some(`Content-Type`(`text/html`, Charset.`UTF-8`)))
        .putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`())))
    case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
      StaticFile
        .fromResource(req.pathInfo, Some(req))
        .map(_.putHeaders())
        .orElse(Option(getClass.getResource(req.pathInfo)).flatMap(
          StaticFile.fromURL(_, Some(req))))
        .map(_.putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
        .map(Task.now)
        .getOrElse(NotFound())
  }
}
