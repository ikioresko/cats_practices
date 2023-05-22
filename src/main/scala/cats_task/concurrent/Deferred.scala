package cats_task.concurrent

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration.DurationInt

object Deferred extends IOApp {
  case class Item(id: Long)

  def loadItems() = {
    IO.println("Loading items") *>
      IO.sleep(2.seconds) *>
      IO.println("loading done")
        .as(List(Item(1), Item(2)))
  }

  def initUI() = {
    IO.println("Init UI") *>
      IO.sleep(2.seconds) *>
      IO.println("UI init done")

  }

  def showItems(item: List[Item]) = {
    IO.println("Showing items")
  }

  def showError() = {
    IO.println("Showing error")
  }

  def setupUI() = {
    initUI() *>
      loadItems()
        .flatMap(items => showItems(items))
        .handleError(_ => showError())
  }

  override def run(args: List[String]): IO[ExitCode] = {
    setupUI().as(ExitCode.Success)
  }
}
