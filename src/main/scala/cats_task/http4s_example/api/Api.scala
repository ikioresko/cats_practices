package cats_task.http4s_example.api

import cats.data.Kleisli
import cats.effect.IO
import cats_task.http4s_example.User
import io.circe.syntax._
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.dsl.io._
import org.http4s.implicits._

object Api {
  val helloService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / name =>
      Ok(s"Hello, ${User("yourLogin", name).asJson}.")
  }.orNotFound
}
