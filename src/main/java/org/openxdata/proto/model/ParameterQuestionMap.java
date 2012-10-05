package org.openxdata.proto.model;

/**
 * This holds the oxd question variables and workitem parameters (including values) and that will be used to pre-fill a
 * form
 * 
 * NB: To avoid breaking backwards compatibility, do not change method signatures or remove methods from this class.
 */
public class ParameterQuestionMap {

	/**
	 * Workitem parameterName/question.This parameter comes from the workflow engine
	 */
	private String workitemParameter;
	/**
	 * The openxdata question variable that will be pre-filled
	 */
	private String questionVariable;
	/**
	 * Value to fill into the question
	 */
	private String value;
	/**
	 * Flag determining whether question on the mobile will be editable. This is important because some fields do not
	 * have to be editable.. e.g Patient Ids
	 */
	private boolean isReadOnly;

	public ParameterQuestionMap() {
	}

	public ParameterQuestionMap(String workitemParameter, String questionVariable, String value, boolean isReadOnly) {
		this.workitemParameter = workitemParameter;
		this.questionVariable = questionVariable;
		this.value = value;
		this.isReadOnly = isReadOnly;
	}

	public String getWorkitemParameter() {
		return workitemParameter;
	}

	public void setWorkitemParameter(String workitemParameter) {
		this.workitemParameter = workitemParameter;
	}

	public String getQuestionVariable() {
		return questionVariable;
	}

	public void setQuestionVariable(String questionVariable) {
		this.questionVariable = questionVariable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

}
