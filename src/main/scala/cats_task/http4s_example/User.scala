package cats_task.http4s_example

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class User(login: String, firstName: String) {
}

object User {
  implicit val fooDecoder: Decoder[User] = deriveDecoder[User]
  implicit val fooEncoder: Encoder[User] = deriveEncoder[User]
}