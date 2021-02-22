package edu.fiuba.fpfiuba43.models

import java.time.LocalDateTime

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
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
                    wDiff: Double) {

  def getField(field: String): Any = {
    field match {
      case "open" => open.orNull
      case "high" => high.orNull
      case "low" => low.orNull
      case "last" => last
      case "close" => close
      case "diff" => diff
      case "OVol" => oVol.orNull
      case "ODiff" => oDiff.orNull
      case "OpVol" => opVol.orNull
      case "dollarBN" => dollarBN
      case "dollarItau" => dollarItau
      case "wDiff" => wDiff
    }
  }
}

object InputRow {
  implicit val decoder: Decoder[InputRow] = deriveDecoder
  implicit val encoder: Encoder[InputRow] = deriveEncoder

}


case class Score(score: Double)

object Score {
  implicit val decoder: Decoder[Score] = deriveDecoder
  implicit val encoder: Encoder[Score] = deriveEncoder
}