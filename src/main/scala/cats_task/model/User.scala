package cats_task.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}


case class User(name: String) extends AnyVal

object User {
  implicit val fooDecoder: Decoder[User] = deriveDecoder[User]
  implicit val fooEncoder: Encoder[User] = deriveEncoder[User]
}