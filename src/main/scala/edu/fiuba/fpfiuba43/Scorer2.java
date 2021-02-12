package edu.fiuba.fpfiuba43;

import edu.fiuba.fpfiuba43.models.InputRow;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scorer2 {

	public void dsdfs (InputRow row) throws JAXBException, SAXException, IOException {
		// Building a model evaluator from a PMML file
		Evaluator evaluator = new LoadingModelEvaluatorBuilder()
				.load(new File("model.pmml"))
				.build();

		List<? extends InputField> inputFields = evaluator.getInputFields();
		Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

		// Mapping the record field-by-field from data source schema to PMML schema
		for(InputField inputField : inputFields){
			FieldName inputName = inputField.getName();

			// Transforming an arbitrary user-supplied value to a known-good PMML value
			FieldValue inputValue = inputField.prepare(row);

			arguments.put(inputName, inputValue);
		}
	}
}
