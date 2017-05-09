package controllers

import java.util.UUID
import javax.inject._

import models.Person
import models.dtos.{CreatePerson, ErrorCode, ErrorResponse}
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.mvc._
import repositories.PersonRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonController @Inject()(personRepo: PersonRepository[Future])(implicit ec: ExecutionContext) extends Controller {

  def create: Action[JsValue] = Action.async(parse.json) { request =>
    onValidationSuccess[CreatePerson](request.body) { createPerson =>
      val person = Person(UUID.nameUUIDFromBytes(createPerson.studentId.getBytes()), createPerson.firstName,
        createPerson.lastName, createPerson.studentId, createPerson.gender.toModel)
      personRepo.find(person.id).flatMap {
        case None => personRepo.create(person).map(createdPerson => Created(createdPerson.toJson))
        case Some(existing) => Future.successful(Conflict(existing.toJson))
      }.recover { case _ => ServiceUnavailable }
    }
  }

  def find(id: UUID): Action[AnyContent] = Action.async (
    personRepo.find(id).map {
      case None => NotFound
      case Some(existingPerson) => Ok(existingPerson.toJson)
    }.recover { case _ => ServiceUnavailable }
  )

  def findAll: Action[AnyContent] = Action.async(
    personRepo.findAll.map(seqPerson => Ok(seqPerson.toJson))
  )

  def delete(id: UUID): Action[AnyContent] = Action.async (
    personRepo.find(id).flatMap {
      case None => Future.successful(NotFound)
      case Some(_) => personRepo.deleteById(id).map(_ => Ok)
    }.recover { case _ => ServiceUnavailable }
  )

  def onValidationSuccess[A](body: JsValue)(success: A => Future[Result])
                            (implicit rds: Reads[A]): Future[Result] = {
    def handleValidation(body: JsValue)(implicit rds: Reads[A]): Either[ErrorResponse, A] =
      body.validate[A].asEither.left.map {
        (errors: Seq[(JsPath, Seq[ValidationError])]) =>
          ErrorResponse(ErrorCode.BadInput, errors.map {
            case (jsPath, validationErrors) =>
              validationErrors.map(_.message).mkString(
                start = jsPath.path.mkString(start = "", sep = "/", end = ""),
                sep = ",",
                end = ""
              )
          })
      }
    handleValidation(body).fold(e => Future.successful(BadRequest(e.toJson)), success)
  }

  implicit class Jsonable[A](a: A) {
    def toJson(implicit writes: Writes[A]): JsValue = Json.toJson(a)(writes)
  }
}
