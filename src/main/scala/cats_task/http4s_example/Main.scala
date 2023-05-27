package cats_task.http4s_example

import cats.effect._
import cats_task.http4s_example.api.Api.endpoint
import org.http4s.blaze.server.BlazeServerBuilder

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(endpoint)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
