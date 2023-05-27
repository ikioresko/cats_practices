package cats_task.http4s_example.api

import cats.data.Kleisli
import cats.effect.IO
import cats.effect.std.Random
import io.prometheus.client.Gauge
import io.prometheus.client.exporter.HTTPServer
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

import scala.concurrent.duration.DurationInt

object Api {
  val randomIO: IO[Random[IO]] = Random.scalaUtilRandom[IO]
  val requestLatency: Gauge = getGauge("gauge_name", "labels_name")

  def getGauge(name: String, label: String): Gauge = Gauge.build()
    .name(name)
    .labelNames(label)
    .help("latency in seconds")
    .register()

  val endpoint: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root => for {
      requestTimer <- IO.delay(requestLatency.labels("first endpoint").startTimer())
      r <- randomIO
      n <- r.betweenInt(1, 10)
      _ <- IO.sleep(n.millis)
      time <- IO.blocking(requestTimer.setDuration())
      response <- Ok(s"$time")
    } yield response
    case GET -> Root / "g2" => for {
      requestTimer <- IO.delay(requestLatency.labels("second endpoint").startTimer())
      r <- randomIO
      n <- r.betweenInt(1, 10)
      _ <- IO.sleep(n.millis)
      time <- IO.blocking(requestTimer.setDuration())
      response <- Ok(s"$time")
    } yield response
  }.orNotFound

  val server: HTTPServer = new HTTPServer.Builder()
    .withPort(9096)
    .build()
}
