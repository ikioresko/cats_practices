package cats_task.concurrent

import cats.effect.{ExitCode, IO, IOApp}

import java.util.concurrent.Executors
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

object AsyncApp extends IOApp {

  case class Person(id: String, name: String)

  def findPersonById(id: String)(implicit ec: ExecutionContext): Future[Person] = Future {
    println(s"Thread: ${Thread.currentThread().getName}")

    Person(id, "name")
  }

  def findPersonByIdIO(id: String): IO[Person] = {
    implicit val ec: ExecutionContext = ExecutionContext.global
    IO(Await.result(findPersonById(id), Duration.Inf))
  }

  def findPersonByIdIO2(id: String): IO[Person] = {
    IO.executionContext.flatMap { implicit ec =>
      IO.async_(cb => findPersonById(id).onComplete {
        case Success(person) => cb(Right(person))
        case Failure(exception) => cb(Left(exception))
      })
    }
  }

  def findPersonByIdIO3(id: String): IO[Person] = {
    IO.executionContext.flatMap { implicit ec =>
      IO.fromFuture(IO(findPersonById(id)))
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    //    findPersonByIdIO3("1")
    //      .flatTap(IO.println)
    //      .as(ExitCode.Success)
    val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())
    findPersonByIdIO3("1")
      .evalOn(ec)
      .flatTap(IO.println)
      .as(ExitCode.Success)
  }
}