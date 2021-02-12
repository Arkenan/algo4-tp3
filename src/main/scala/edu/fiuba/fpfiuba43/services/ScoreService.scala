package edu.fiuba.fpfiuba43.services

import cats.effect.{Effect, IO, Resource, Sync}
import doobie.hikari.HikariTransactor
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.http4s.EntityDecoder
import org.http4s.circe._
import org.http4s.client.Client
import doobie.implicits._
import edu.fiuba.fpfiuba43.Scorer

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_]: Sync:Effect](client: Client[F], tr: Resource[F, HikariTransactor[F]])  extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, Score] = jsonOf[F, Score]

  override def getScore(row: InputRow): F[Score] = {
    val hashedRow = row.hashCode()
    val result = getValueFromCache(hashedRow)

    result match {
      case null => {
        val score = Scorer.score(row)
        saveScoreInCache(score)
      }
      case _ => result
    }
  }

  def getValueFromCache(hashedRow: Double): F[Option[Score]] = {
    tr.use { xa =>
      sql"select score from fptp.scores where hash_code= ${hashedRow}".query[Score].option.transact(xa)
    }
  }

  def saveScoreInCache(score: Score): Unit = {

  }
}
