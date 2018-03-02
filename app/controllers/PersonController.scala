package controllers

import java.util.UUID
import javax.inject._

import dtos.ErrorResponse
import play.api.libs.json._
import play.api.mvc._
import services.PersonService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NoStackTrace

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class PersonController @Inject()(cc: ControllerComponents, personService: PersonService[Future])(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {
  def create: Action[JsValue] = Action.async(parse.json) { request =>
    validated[dtos.Person](request.body) { dtoPerson =>
      generateId().flatMap { id =>
        val person = dtoPerson.toModel(id)
        personService
          .create(person)
          .map(person => person.toString)
          .map(Created(_))
      }
    }
  }

  def generateId(triesRemaining: Int = 10): Future[UUID] =
    if (triesRemaining <= 0) Future.failed(new Exception("Could not generate an ID, tried 10 times") with NoStackTrace)
    else {
      val candidate = UUID.randomUUID()
      personService.find(candidate).flatMap {
        case None    => Future.successful(candidate)
        case Some(_) => generateId(triesRemaining - 1)
      }
    }

  def validated[A](body: JsValue)(success: A => Future[Result])(implicit r: Reads[A]): Future[Result] =
    body.validate match {
      case JsSuccess(value, _) => success(value)

      case JsError(errors) =>
        val validationErrors = errors.map {
          case (jsPath, validationErrors: Seq[JsonValidationError]) =>
            jsPath.toString.tail -> validationErrors.head.message
        }.toMap
        Future.successful(BadRequest(Json.toJson(ErrorResponse(validationErrors))))
    }
}
