package edu.fiuba.fpfiuba43

import java.io.File

import edu.fiuba.fpfiuba43.models.InputRow
import org.jpmml.evaluator.{Evaluator, FieldValue, InputField, LoadingModelEvaluatorBuilder}
import org.dmg.pmml.FieldName
import java.util
import java.util.{LinkedHashMap, Map}

import org.dmg.pmml.FieldName
import org.jpmml.evaluator.TargetField


object Scorer {

    def score(row: InputRow): Double = {
      val evaluator = new LoadingModelEvaluatorBuilder().load(new File("model.pmml")).build

      val inputFields = evaluator.getInputFields
      val arguments = new util.LinkedHashMap[FieldName, FieldValue]

      // Mapping the record field-by-field from data source schema to PMML schema
      import scala.collection.JavaConversions._
      for (inputField <- inputFields) {
        val inputName = inputField.getName
        // Transforming an arbitrary user-supplied value to a known-good PMML value
        val inputValue = inputField.prepare(row)
        arguments.put(inputName, inputValue)
      }

      val results = evaluator.evaluate(arguments)

      val targetFields = evaluator.getTargetFields
      import scala.collection.JavaConversions._
      for (targetField <- targetFields) {
        val targetName = targetField.getName
        val targetValue = results.get(targetName)
      }
    }
}