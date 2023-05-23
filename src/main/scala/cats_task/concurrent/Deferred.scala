package cats_task.concurrent

import cats.effect._
import cats.implicits._

import scala.concurrent.duration.DurationInt

object Deferred extends IOApp {
  case class Item(id: Long)

  def loadItems(): IO[List[Item]] = {
//    IO.raiseError(new Exception("Failed to load items"))
                IO.println("Loading items") *>
                  IO.sleep(2.seconds) *>
                  IO.println("loading done")
                    .as(List(Item(1), Item(2)))
  }

  def initUI(): IO[Unit] = {
    IO.println("Init UI") *>
      IO.sleep(2.seconds) *>
      IO.println("UI init done")
  }

  def showItems(item: List[Item]): IO[Unit] = IO.println("Showing items")

  def showError(): IO[Unit] = IO.println("Showing error")

  //  def setupUI(): IO[Unit] = {
  //    initUI() *> loadItems().flatMap(items => showItems(items)).handleError(x => showError())
  //  }

//  def setupUI(): IO[Unit] = {
//    (initUI(), loadItems())
//      .parMapN { case (_, items) =>
//        showItems(items)
//      }
//      .flatten
//      .handleError(_ => showError())
//  }

  def setupUI(): IO[Unit] = {
    (initUI(), loadItems().attempt)
      .parMapN {
        case (_, Right(items)) => showItems(items)
        case (_, Left(error)) => showError()
      }
      .flatten
  }

  override def run(args: List[String]): IO[ExitCode] = {
    setupUI().as(ExitCode.Success)
  }
}
