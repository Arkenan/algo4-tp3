package edu.fiuba.fpfiuba43.http

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, IO, Resource, Timer}
import edu.fiuba.fpfiuba43.services.{HealthCheckImpl, ScoreServiceRest}
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import cats.implicits._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object Fpfiuba43Server {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], cs: ContextShift[F]  ): Stream[F, Nothing] = {
    // Transactor to connect to our DB.
    val transactor: Resource[F, HikariTransactor[F]] =
      for {
        ce <- ExecutionContexts.fixedThreadPool[F](32)
        be <- Blocker[F]
        xa <- HikariTransactor.newHikariTransactor[F](
          "org.postgresql.Driver",
          "jdbc:postgresql://localhost:5432/fpalgo",
          "fiuba",
          "password",
          ce,
          be
        )
      } yield xa

    for {
      client <- BlazeClientBuilder[F](global).stream
      scoreService = new ScoreServiceRest[F](client, transactor)
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
