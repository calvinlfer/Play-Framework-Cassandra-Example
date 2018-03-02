package filters

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import akka.stream.Materializer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class RequestTimeFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    val startTime = System.nanoTime()
    f(rh).map { result =>
      val endTime          = System.nanoTime()
      val timeDifferenceMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)
      result.withHeaders("x-request-time" -> s"$timeDifferenceMs ms")
    }
  }
}
