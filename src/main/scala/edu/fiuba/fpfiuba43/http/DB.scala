package edu.fiuba.fpfiuba43.http

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar

import cats.effect.{ExitCode, IO, Resource}
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.implicits._
import edu.fiuba.fpfiuba43.models.Score


/** Database interaction for datasets using a specified transactor. */
case class DB(transactor: Resource[IO, HikariTransactor[IO]]) {
  def getScore(code: Int):IO[Score] ={
    transactor.use { xa =>
      for {
        score <- sql"select score from fptp.scores where hash_code= {code}".query[Score].unique.transact(xa)
      } yield score
    }
  }
}
