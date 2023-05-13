package cats_task.concurrent

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.{toFoldableOps, toTraverseOps}

import scala.concurrent.duration.DurationInt

object Mailing extends IOApp {
  trait Error extends Throwable

  object NegativeBalance extends Error

  object AccountExpired extends Error

  case class Client(name: String, email: String)

  case class Email(body: String, recipients: List[String])

  trait EmailTemplates {
    def buildEmailForClient(templateId: String, client: Client): Email
  }

  def loadEmailTemplates(): IO[EmailTemplates] =
    IO.println("Loading email templates") *>
      IO.sleep(5.seconds) *>
      IO.pure(new EmailTemplates {
        override def buildEmailForClient(templateId: String, client: Client): Email = {
          if (templateId == "negative-balance") Email(s"${client.name} your account has a negative balance", List(client.email))
          else Email(s"${client.name} your account has a problem", List(client.email))
        }
      })

  def processClient(client: Client): IO[Unit] = {
    IO.raiseError(NegativeBalance)
    //    IO.println(s"Processing ${client.name}")
  }

  def sendEmail(email: Email): IO[Unit] = IO.println("Sending email")

  def processClients(clients: List[Client]): IO[EmailTemplates] = {
    loadEmailTemplates().flatTap { templates =>
      clients.traverse { client =>
        processClient(client).handleErrorWith {
          case NegativeBalance =>
            val email = templates.buildEmailForClient("negative-balance", client)
            sendEmail(email)
          case _ =>
            val email = templates.buildEmailForClient("generic-error", client)
            sendEmail(email)
        }
      }
    }
  }

  def processClients2(clients: List[Client]): IO[Unit] = {
    clients.traverse_ { client =>
      processClient(client).handleErrorWith { error =>
        loadEmailTemplates().flatTap { templates =>
          error match {
            case NegativeBalance =>
              val email = templates.buildEmailForClient("negative-balance", client)
              sendEmail(email)
            case _ =>
              val email = templates.buildEmailForClient("generic-error", client)
              sendEmail(email)
          }
        }
      }
    }
  }

  def processClients3(clients: List[Client]) = {
    loadEmailTemplates().memoize.flatTap { templatesIO =>
      clients.traverse_ { client =>
        processClient(client).handleErrorWith { error =>
          templatesIO.flatMap { templates =>
            error match {
              case NegativeBalance =>
                val email = templates.buildEmailForClient("negative-balance", client)
                sendEmail(email)
              case _ =>
                val email = templates.buildEmailForClient("generic-error", client)
                sendEmail(email)
            }
          }
        }
      }
    }
  }


  override def run(args: List[String]): IO[ExitCode] = {
    val clients = List(Client("Tom", "tom@mail.com"), Client("Sam", "sam@mail.com"))
    processClients3(clients).as(ExitCode.Success)
  }
}
