package edu.fiuba.fpfiuba43.services

import cats.effect.{Effect, Resource, Sync}
import doobie.hikari.HikariTransactor
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.http4s.EntityDecoder
import org.http4s.circe._
import org.http4s.client.Client
import doobie.implicits._

trait ScoreService[F[_]] {
  def getScore(row: InputRow): F[Score]
}

class ScoreServiceRest[F[_]: Sync:Effect](client: Client[F], tr: Resource[F, HikariTransactor[F]])  extends ScoreService[F] {

  implicit val decoder: EntityDecoder[F, Score] = jsonOf[F, Score]

  override def getScore(row: InputRow): F[Score] = {
    tr.use { xa =>
       sql"select score from fptp.scores where hash_code= ${row.id}".query[Score].unique.transact(xa)
      }
    }
  }
