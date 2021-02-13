package edu.fiuba.fpfiuba43.services

import cats.Monad
import cats.effect.Sync
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import edu.fiuba.fpfiuba43.{Cache, Scorer}

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_] : Sync : Monad](cache: Cache[F], scorer: Scorer) extends ScoreService[F] {
  override def getScore(row: InputRow): F[Score] = {
    val hashedRow = row.hashCode()
    implicitly[Monad[F]].flatMap(cache.getScoreFromCache(hashedRow)) {
      case Some(score) => implicitly[Monad[F]].pure(score)
      case None =>
        val score = scorer.score(row)
        implicitly[Monad[F]].map(cache.saveScoreInCache(hashedRow, score)) { _ => score }
    }
  }
}
