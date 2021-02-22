package edu.fiuba.fpfiuba43

import cats.effect.{Resource, Sync}
import doobie.hikari.HikariTransactor
import doobie.implicits.{toSqlInterpolator, _}
import edu.fiuba.fpfiuba43.models.Score

trait Cache[F[_]] {
  def getScoreFromCache(hashCode: Double): F[Option[Score]]
  def saveScoreInCache(hashCode: Double, score: Score): F[Int]
}

class PostgreSQLCache[F[_]: Sync](tr: Resource[F, HikariTransactor[F]]) extends Cache[F] {

  override def getScoreFromCache(hashCode: Double): F[Option[Score]] = {
    tr.use { transactor =>
      sql"select score from fptp.scores where hash_code= ${hashCode}"
        .query[Score]
        .option
        .transact(transactor)
    }
  }

  override def saveScoreInCache(hashCode: Double, score: Score): F[Int] =
    tr.use { transactor =>
      sql"INSERT INTO fptp.scores (hash_code, score) VALUES (${hashCode}, ${score.score})"
        .update.run.transact(transactor)
    }

}
