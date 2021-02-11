package edu.fiuba.fpfiuba43.services

import cats.effect.Sync
import edu.fiuba.fpfiuba43.models.{InputRow, InputRow2Score}
import org.http4s.EntityDecoder
import org.http4s.client.Client
import org.http4s.circe._

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[InputRow2Score]
}

class ScoreServiceRest[F[_]: Sync]() extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, InputRow2Score] = jsonOf[F, InputRow2Score]

  override def getScore(row: InputRow): F[InputRow2Score] = {
    InputRow2Score(93.166753131)
  }
}