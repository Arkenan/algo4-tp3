package edu.fiuba.fpfiuba43.http

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import edu.fiuba.fpfiuba43.FiubaTransactor
import edu.fiuba.fpfiuba43.services.{HealthCheckImpl, ScoreServiceRest}
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object Fpfiuba43Server {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], cs: ContextShift[F]): Stream[F, Nothing] = {

    val transactor = FiubaTransactor.transactor

    for {
      _ <- BlazeClientBuilder[F](global).stream
      scoreService = new ScoreServiceRest[F](transactor)
      healthCheck = new HealthCheckImpl[F]("changeme")
      httpApp = (Fpfiuba43Routes.healthCheckRoutes[F](healthCheck)<+>
        Fpfiuba43Routes.scoreRoute[F](scoreService)).orNotFound
      finalHttpApp = Logger.httpApp(true, true)(httpApp)
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
