package edu.fiuba.fpfiuba43.services

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import edu.fiuba.fpfiuba43.{Cache, Scorer}

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_] : Sync : Monad](cache: Cache[F], scorer: Scorer[F]) extends ScoreService[F] {
  override def getScore(row: InputRow): F[Score] = {
    val hashedRow = row.hashCode()
    cache.getScoreFromCache(hashedRow).flatMap {
      case Some(score) => score.pure[F]
      case None =>
        for {
          score <- scorer.score(row)
          _ <- cache.saveScoreInCache(hashedRow, score)
        } yield(score)
    }
  }
}
