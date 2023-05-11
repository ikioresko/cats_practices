package cats_task.concurrent

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxParallelTraverse1}

import scala.concurrent.duration.DurationInt

object RaceEx extends IOApp {
  case class Quote(author: String, text: String)

  def fetchEth(num: Int): IO[List[Quote]] = {
    IO.sleep(100.millis) *>
      (1 to num).toList.map(i => Quote(s"authorEth $i", s"text $i")).pure[IO]
  }

  def fetchLocal(num: Int): IO[List[Quote]] = {
    IO.sleep(200.millis) *>
      (1 to num).toList.map(i => Quote(s"authorLocal $i", s"text $i")).pure[IO]
  }

  def fetchAge(author: String): IO[Int] = {
    IO.sleep(150.millis) *> IO((math.random() * 100).toInt)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val num = 3
    IO.race(fetchEth(num), fetchEth(num))
      .flatTap(IO.println)
      .flatMap {_.fold(identity, identity).parTraverse(q => fetchAge(q.author))}
      .flatTap(IO.println)
      .map(ages => ages.sum / num.toDouble)
      .flatTap(IO.println)
      .as(ExitCode.Success)
  }
}
