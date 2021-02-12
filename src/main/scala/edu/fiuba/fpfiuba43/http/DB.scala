package edu.fiuba.fpfiuba43.http

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import edu.fiuba.fpfiuba43.models.Score


/** Database interaction for datasets using a specified transactor. */
case class DB(transactor: Resource[IO, HikariTransactor[IO]]) {
  def getScore[F](code: Int): F[Score] ={
    transactor.use { xa =>
      for {
        score <- sql"select score from fptp.scores where hash_code= {code}".query[Score].unique.transact(xa)
      } yield score
    }

  }
}
