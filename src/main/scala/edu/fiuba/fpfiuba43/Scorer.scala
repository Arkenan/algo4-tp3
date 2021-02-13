package edu.fiuba.fpfiuba43

import java.io.File
import java.util
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.dmg.pmml.FieldName
import org.jpmml.evaluator.{EvaluatorUtil, FieldValue, InputField, LoadingModelEvaluatorBuilder, ModelEvaluator}

trait Scorer {
  def score(row: InputRow): Score
}

class JpmmlScorer(modelPathname: String) extends Scorer {
  val evaluator: ModelEvaluator[_] = new LoadingModelEvaluatorBuilder().load(new File(modelPathname)).build
  val inputFields: util.List[InputField] = evaluator.getInputFields
  val arguments = new util.LinkedHashMap[FieldName, FieldValue]

  override def score(row: InputRow): Score = {
    inputFields forEach (inputField => {
      val inputName = inputField.getName
      val inputValue = inputField.prepare(row.getField(inputName.getValue))
      arguments.put(inputName, inputValue)
    })

    val results: util.Map[FieldName, _] = evaluator.evaluate(arguments)

    val resultRecord = EvaluatorUtil.decodeAll(results)

    Score(resultRecord.get("prediction").toString.toDouble)
  }
}