package edu.fiuba.fpfiuba43

import cats.effect.{Effect, Resource, Sync}
import doobie.hikari.HikariTransactor
import doobie.implicits.{toSqlInterpolator, _}
import edu.fiuba.fpfiuba43.models.Score

case class Cache[F[_]: Sync:Effect](tr: Resource[F, HikariTransactor[F]]) {

  def getScoreFromCache(hashedRow: Double): F[Option[Score]] = {
    tr.use { xa =>
      sql"select score from fptp.scores where hash_code= ${hashedRow}".query[Score].option.transact(xa)
    }
  }

  def saveScoreInCache(hashRow: Double, score: Double): F[Nothing] = {
    tr.use { xa =>
      sql"INSERT INTO fptp.dataset" ++
        sql"(hash_code, score)" ++
        sql"VALUES" ++
        sql"(${hashRow}, ${score})"
          .query[Boolean].option.transact(xa)
    }
  }
}
