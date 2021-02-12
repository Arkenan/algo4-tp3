package edu.fiuba.fpfiuba43

object Scorer {

    def score(row: InputRow): Double = {
        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
	        .load(new File("model.pmml"))
	        .build();

            // Perforing the self-check
            evaluator.verify();

            List<InputField> inputFields = evaluator.getInputFields()
            for (inputField <- inputFields) {
	            FieldName inputName = inputField.getName()

	            Object rawValue = inputDataRecord.get(inputName.getValue())

	            // Transforming an arbitrary user-supplied value to a known-good PMML value
	            // The user-supplied value is passed through: 1) outlier treatment, 2) missing value treatment, 3) invalid value treatment and 4) type conversion
	            FieldValue inputValue = inputField.prepare(row)

	            arguments.put(inputName, inputValue)

                Map<FieldName, FieldValue> results = evaluator.evaluate(arguments)
            }
    }
    

}