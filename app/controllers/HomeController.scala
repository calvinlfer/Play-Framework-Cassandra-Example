package controllers

import java.util.UUID
import javax.inject._

import models.{Gender, Person}
import play.api.mvc._
import repositories.PersonRepository

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (personRepo: PersonRepository[Future])(implicit ec: ExecutionContext) extends Controller {
  def index = Action { implicit request =>
    personRepo.create(Person(UUID.randomUUID(), "Calvin", "Fer", "901234", Gender.Male))
    Created("created a person")
  }

  def find(id: UUID): Action[AnyContent] = Action.async {
    personRepo
      .find(id)
      .map(_.toString)
      .map(Ok(_))
  }
}
