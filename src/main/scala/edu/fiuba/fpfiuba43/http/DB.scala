package edu.fiuba.fpfiuba43.http

import cats.effect.{Blocker, ContextShift, IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import edu.fiuba.fpfiuba43.models.Score


/** Database interaction for datasets using a specified transactor. */
case class DB[F](implicit cs: ContextShift[F]) {

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

  def initDb(): Unit ={
    // Transactor to connect to our DB.


  }
  def getScore[F](code: Int): IO[Score] ={
    transactor.use { xa =>
      for {
        score <- sql"select score from fptp.scores where hash_code= {code}".query[Score].unique.transact(xa)
      } yield score
    }

  }
}
