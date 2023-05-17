package cats_task.concurrent

import cats.effect.kernel.Ref
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxParallelTraverse_

object ConcurrentSharedStateEx extends IOApp {

  case class User(username: String, age: Int, friends: List[User])

  def findOldestRef(user: User, ref: Ref[IO, User]): IO[Unit] = {
    ref.update { oldestUser =>
      if (user.age > oldestUser.age) user else oldestUser
    } *> user.friends.parTraverse_(friend => findOldestRef(friend, ref))
  }

  def findOldest(user: User): IO[User] = {
    Ref.of[IO, User](user).flatMap { ref =>
      findOldestRef(user, ref) *> ref.get
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val a = User("a", 60, Nil)
    val b = User("b", 35, Nil)
    val c = User("c", 45, Nil)
    val d = User("d", 50, List(a, b))
    val e = User("e", 55, List(c))
    val f = User("f", 15, List(d, e))

    findOldest(f).flatTap(IO.println).as(ExitCode.Success)
  }

}
