package edu.fiuba.fpfiuba43.services

import cats.effect.{Effect, IO, Sync}
import edu.fiuba.fpfiuba43.http.DB
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.http4s.EntityDecoder
import org.http4s.circe._
import org.http4s.client.Client

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_]: Sync:Effect](client: Client[F], db: DB) extends ScoreService[F] {

  //implicit val decoder: EntityDecoder[F, InputRow2Score] = jsonOf[F, InputRow2Score]
  implicit val decoder: EntityDecoder[F, Score] = jsonOf[F, Score]

  override def getScore(row: InputRow): F[Score] = {
    db.getScore(row.id)
  }
}