package cats_task.model

object Library {
  case class Author(name: String)
  case class Book(user: User, title: String, year: Int, author: Author)

  val books = List(
    Book(User("Tom"), "The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe")),
    Book(User("Sam"), "On the Niemen", 1888, Author("Eliza Orzeszkowa")),
    Book(User("Bob"), "The Art of Computer Programming", 1968, Author("Donald Knuth")),
    Book(User("Mike"), "Pharaoh", 1897, Author("Boleslaw Prus"))
  )
}
