package edu.fiuba.fpfiuba43.services

import cats.effect.{IO, Sync}
import edu.fiuba.fpfiuba43.models.{InputRow, InputRow2Score}
import org.http4s.EntityDecoder
import org.http4s.circe._
import org.http4s.client.Client

trait ScoreService[F[_]] {
  def getScore(row: InputRow): InputRow2Score
}

class ScoreServiceRest[F[_]: Sync](client: Client[F]) extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, InputRow2Score] = jsonOf[F, InputRow2Score]

  override def getScore(row: InputRow): InputRow2Score = {
    InputRow2Score(93.166753131)
  }
}