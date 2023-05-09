package cats_task.http4s_example.service

import cats.effect.{ExitCode, IO, IOApp}

import java.time.LocalTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object MultiService extends IOApp {

  private def getByIdFuture(id: Int): Future[String] = {
    Future {
      println(s" function for $id evaluated at ${LocalTime.now()}")
      s"Customer_$id"
    }
  }

  private def getByIdIo(id: Int): IO[String] = {
    for {
      customer <- IO.fromFuture(IO(getByIdFuture(id)))
      _ <- IO.sleep(2 seconds)
    } yield {
      customer
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      customer <- getByIdIo(1)
    } yield {
      println(s"returned $customer, at ${LocalTime.now()}")
      ExitCode.Success
    }
  }
}
