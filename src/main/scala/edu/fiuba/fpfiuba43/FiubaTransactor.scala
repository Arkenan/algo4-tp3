package edu.fiuba.fpfiuba43

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object FiubaTransactor {

  def transactor[F[_]: ConcurrentEffect](implicit cs: ContextShift[F]):  Resource[F, HikariTransactor[F]] = {
    // Transactor to connect to the DB.
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
  }
}
