package edu.fiuba.fpfiuba43

import cats.effect.Sync
import java.io.File
import java.util
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.dmg.pmml.FieldName
import org.jpmml.evaluator.{EvaluatorUtil, FieldValue, InputField, LoadingModelEvaluatorBuilder, ModelEvaluator}

import scala.util.Try

trait Scorer[F[_]] {
  def score(row: InputRow): F[Score]
}

class JpmmlScorer[F[_]: Sync](modelPathname: String) extends Scorer[F] {
  val evaluator: ModelEvaluator[_] = new LoadingModelEvaluatorBuilder().load(new File(modelPathname)).build
  val inputFields: util.List[InputField] = evaluator.getInputFields

  override def score(row: InputRow): F[Score] = Sync[F].delay {
    val arguments = new util.LinkedHashMap[FieldName, FieldValue]

    inputFields forEach (inputField => {
      val inputName = inputField.getName
      val inputValue = inputField.prepare(row.getField(inputName.getValue))
      arguments.put(inputName, inputValue)
    })

    val results: util.Map[FieldName, _] = evaluator.evaluate(arguments)

    val resultRecord = EvaluatorUtil.decodeAll(results)

    Score(Try(resultRecord.get("prediction").toString.toDouble).getOrElse(0.0))
  }
}