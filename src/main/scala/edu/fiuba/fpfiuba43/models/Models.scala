package edu.fiuba.fpfiuba43.models

import java.time.LocalDateTime

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.Response
case class InputRow(id: Int,
                    date: LocalDateTime,
                    open: Option[Double],
                    high: Option[Double],
                    low: Option[Double],
                    last: Double,
                    close: Double,
                    diff: Double,
                    curr: String,
                    oVol: Option[Int],
                    oDiff: Option[Int],
                    opVol: Option[Int],
                    unit: String,
                    dollarBN: Double,
                    dollarItau: Double,
                    wDiff: Double)

object InputRow {
  implicit val decoder: Decoder[InputRow] = deriveDecoder
  implicit val encoder: Encoder[InputRow] = deriveEncoder
}
case class InputRow2Score(score: Double)

object InputRow2Score {
  implicit val decoder: Decoder[InputRow2Score] = deriveDecoder
  implicit val encoder: Encoder[InputRow2Score] = deriveEncoder
}

case class Score(hash_code: Int,score: Double)

object Score {
  implicit val decoder: Decoder[Score] = deriveDecoder
  implicit val encoder: Encoder[Score] = deriveEncoder
}