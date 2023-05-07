package cats_task.concurrent

import cats.effect.std.Queue
import cats.effect.{Async, IO, IOApp, Resource}
import cats.implicits.toFunctorOps

import scala.concurrent.duration.DurationInt

object Resources extends IOApp.Simple {
  def producer(queue: Queue[IO, Int]): Resource[IO, Unit] = {
    val fiber = (for {
      isDone <- queue.tryOffer(0)
      _ <- IO.delay(println(s"producer $isDone"))
      _ <- IO.sleep(500.millis)
    } yield {})
      .foreverM
      .onCancel(IO.delay(println("Producer stop")))
    Resource.make(fiber.start)(_.cancel).void
  }

  def consumer(queue: Queue[IO, Int]): Resource[IO, Unit] = {
    val fiber = (for {
      isDone <- queue.tryTake
      _ <- IO.delay(println(s"consumer $isDone"))
      _ <- IO.sleep(1000.millis)
    } yield ())
      .foreverM
      .onCancel(IO.delay(println("Consumer stop")))
    Resource.make(fiber.start) { fiber =>
      val isEmptyQueue = queue.size.map(_ != 0)
      val waitEmptyQueue = Async[IO].whileM_(isEmptyQueue) {
        for {
          size <- queue.size
          _ <- IO.delay(println(s"Queue size: $size"))
          _ <- IO.sleep(300.millis)
        } yield {}
      }
      waitEmptyQueue >> fiber.cancel
    }.void
  }

  override def run(): IO[Unit] = {
    val start = for {
      queue <- Resource.eval(Queue.bounded[IO, Int](100))
      _ <- consumer(queue)
      _ <- producer(queue)
    } yield {}
    start.useForever.void
  }
}
