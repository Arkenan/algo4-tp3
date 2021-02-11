package edu.fiuba.fpfiuba43.http

import cats.effect.Sync
import cats.implicits._
import edu.fiuba.fpfiuba43.models.{AoE2Input, InputRow}
import edu.fiuba.fpfiuba43.services.{AoE2Service, HealthCheck, ScoreService}
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

object Fpfiuba43Routes {

  def healthCheckRoutes[F[_]: Sync](h: HealthCheck[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "health-check" =>
        for{
          healthCheck <- h.healthCheck
          resp <- Ok(healthCheck.asJson)
        } yield resp
    }
  }

  def scoreRoute[F[_]: Sync](scoreService: ScoreService[F]): HttpRoutes[F] = {

    val dsl = new Http4sDsl[F]{}
    import dsl._
    implicit val decoder = jsonOf[F, InputRow]
    HttpRoutes.of[F]{
      case req @  POST -> Root / "score"  =>
        for {
          re <- req.as[InputRow]
          r <-  scoreService.getScore(re)
          resp <- Ok {r.asJson}
        } yield resp
    }
  }

}