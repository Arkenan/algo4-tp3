package edu.fiuba.fpfiuba43.services

import cats.effect.{Effect, Resource, Sync}
import doobie.hikari.HikariTransactor
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.http4s.EntityDecoder
import org.http4s.circe._
import edu.fiuba.fpfiuba43.Scorer

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_]: Sync:Effect]( tr: Resource[F, HikariTransactor[F]])  extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, Score] = jsonOf[F, Score]
  override def getScore(row: InputRow): F[Score] = {
    Scorer.score(row).to[F]
  }



}
