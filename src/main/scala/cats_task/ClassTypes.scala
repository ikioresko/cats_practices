package cats_task

class ClassTypes {
}

object ClassTypes extends App {
  def sumInts(list: List[Int]): Int = list.foldRight(0)(_ + _)

  def concatStrings(list: List[String]): String = list.foldRight("")(_ ++ _)

  def unionSets[A](list: List[Set[A]]): Set[A] = list.foldRight(Set.empty[A])(_ union _)

  println(sumInts(List(1, 2, 3, 4, 5)))
  println(concatStrings(List("a", "b", "c")))
  println(unionSets(List(Set(1, 2, 3), Set(1, 2, 3, 4, 5))))


  def combineAll[A](list: List[A], m: Monoid[A]): A = list.foldRight(m.empty)(m.combine)
  println(combineAll(List(1, 2, 3, 4, 5), new Monoid[Int] {
    def empty: Int = 0
    def combine(x: Int, y: Int): Int = x + y
  }))

  println(combineAll(List("a", "b", "c"), new Monoid[String] {
    def empty: String = ""
    def combine(x: String, y: String): String = x ++ y
  }))

  println(combineAll(List(Set(1, 2, 3), Set(1, 2, 3, 4, 5)), new Monoid[Set[Int]] {
    def empty: Set[Int] = Set.empty[Int]
    def combine(x: Set[Int], y: Set[Int]): Set[Int] = x union y
  }))
}

trait Monoid[A] {
  def empty: A
  def combine(x: A, y: A): A
}