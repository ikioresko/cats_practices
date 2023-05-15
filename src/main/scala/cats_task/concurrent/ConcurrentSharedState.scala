package cats_task.concurrent

import cats.implicits._
import cats.effect._
import cats.effect.implicits._

object ConcurrentSharedState extends IOApp {
  case class Activity()

  case class Purchase()

  case class Account()

  case class Customer(
                       id: Long,
                       name: String,
                       account: List[Account],
                       activity: List[Activity],
                       purchase: List[Purchase]
                     )

  def loadName(id: Long): IO[String] = {
    IO.println(s"Loading name for customer $id") *>
    IO.pure(s"Customer $id")
  }

  def loadAccounts(id: Long): IO[List[Account]] = {
    IO.println(s"Loading Account for customer $id") *>
    IO.pure(List(Account()))
  }

  def loadActivities(id: Long): IO[List[Activity]] = {
    IO.println(s"Loading Activity for customer $id") *>
    IO.pure(List(Activity()))

  }

  def loadPurchases(id: Long): IO[List[Purchase]] = {
    IO.println(s"Loading Purchase for customer $id") *>
    IO.pure(List(Purchase()))
  }

  def loadCustomer(id: Long): IO[Customer] = (
    loadName(id),
    loadAccounts(id),
    loadActivities(id),
    loadPurchases(id)
    ).parMapN { case (name, accounts, activities, purchases) =>
    Customer(id, name, accounts, activities, purchases)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    List(2L, 5L, 10L)
      .traverse(loadCustomer)
      .flatTap(IO.println)
      .as(ExitCode.Success)

//    loadCustomer(0
  }
}
