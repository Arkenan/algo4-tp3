package edu.fiuba.fpfiuba43.services

import cats.Monad
import cats.effect.{Effect, Resource, Sync}
import doobie.hikari.HikariTransactor
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import edu.fiuba.fpfiuba43.{Cache, Scorer}
import org.http4s.EntityDecoder
import org.http4s.circe._

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_]: Sync: Monad](tr: Resource[F, HikariTransactor[F]])  extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, Score] = jsonOf[F, Score]
  override def getScore(row: InputRow): F[Score] = {
    val db = Cache(tr)

    val hashedRow = row.hashCode()
    implicitly[Monad[F]].flatMap(db.getScoreFromCache(hashedRow))(result => {
      result match {
        case Some(score) => implicitly[Monad[F]].pure(score)
        case None => {
          val promisedScore = implicitly[Monad[F]].pure(Scorer.score(row))
          implicitly[Monad[F]].flatMap(promisedScore)(score => {
            db.saveScoreInCache(hashedRow, score)
            implicitly[Monad[F]].pure(score)
          })
        }
      }
    })
  }
}
