package edu.fiuba.fpfiuba43

import java.io.File
import java.util

import cats.effect.IO
import edu.fiuba.fpfiuba43.models.{InputRow, Score}
import org.dmg.pmml.FieldName
import org.jpmml.evaluator.{EvaluatorUtil, FieldValue, InputField, LoadingModelEvaluatorBuilder}

object Scorer {

    def score(row: InputRow): IO[Score] = {
      val evaluator = new LoadingModelEvaluatorBuilder().load(new File("model.pmml")).build

      val inputFields : util.List[InputField] = evaluator.getInputFields
      val arguments = new util.LinkedHashMap[FieldName, FieldValue]

      inputFields forEach (inputField => {
        val inputName = inputField.getName
        val inputValue = inputField.prepare(row)
        arguments.put(inputName, inputValue)
      })

      val results : util.Map[FieldName, _] = evaluator.evaluate(arguments)

      val resultRecord = EvaluatorUtil.decodeAll(results)

      IO(Score(resultRecord.get("prediction").toString.toDouble))

    }
}

