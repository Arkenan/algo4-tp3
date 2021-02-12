package edu.fiuba.fpfiuba43.services

import cats.effect.{Effect, Resource, Sync}
import doobie.hikari.HikariTransactor
import doobie.implicits.{toSqlInterpolator, _}
import edu.fiuba.fpfiuba43.Scorer
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.http4s.EntityDecoder
import org.http4s.circe._

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_]: Sync:Effect](tr: Resource[F, HikariTransactor[F]])  extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, Score] = jsonOf[F, Score]
  override def getScore(row: InputRow): F[Score] = {

    val hashedRow = row.hashCode()
    val result = getScoreFromCache(hashedRow)

    result match {
      case null => {
        val score = Scorer.score(row).to[F]
        saveScoreInCache(hashedRow, score)
      }
      case _ => result
    }

    def getScoreFromCache(hashedRow: Double): F[Option[Score]] = {
      tr.use { xa =>
        sql"select score from fptp.scores where hash_code= ${hashedRow}".query[Score].option.transact(xa)
      }
    }
  }
}
