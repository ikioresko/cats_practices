package cats_task.http4s_example

import cats.effect._
import cats_task.http4s_example.api.Api
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Main extends IOApp {
  val routes = Http4sServerInterpreter[IO]().toRoutes(Api.all)

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(Router("/" -> routes).orNotFound)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
