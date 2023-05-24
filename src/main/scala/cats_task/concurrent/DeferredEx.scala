package cats_task.concurrent

import cats.effect.{Deferred, ExitCode, IO, IOApp}

object DeferredEx extends IOApp {
  class Producer[A](name: String, deferred: Deferred[IO, A], exec: IO[A]) {
    def run(): IO[Unit] = {
      IO.println(s"Producer $name running") *>
        exec.flatMap(a => deferred.complete(a)).void
    }
  }

  class Consumer[A](name: String, deferred: Deferred[IO, A], consume: A => IO[Unit]) {
    def run(): IO[Unit] = {
      IO.println(s"Consumer $name running") *>
        deferred.get.flatMap(a => consume(a)).void
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    Deferred[IO, Int].flatMap { deferred =>
      val producer = new Producer[Int]("P", deferred, IO.pure(20))
      val consumer = new Consumer[Int]("C", deferred, i => IO.println(i))
      consumer.run().both(producer.run())
    }.as(ExitCode.Success)
  }
}