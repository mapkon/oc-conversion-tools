package org.openxdata.proto.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This holds reference to a specific fromVersionId and studyId.
 * 
 * NB: To avoid breaking backwards compatibility, do not change method signatures or remove methods from this class.
 */
public class WorkitemFormRef {
	/**
	 * List maps that will be used to pre-fill forms
	 */
	private List<ParameterQuestionMap> paramQuestionMaps = new ArrayList<ParameterQuestionMap>();;

	private int formVersionId;
	private int studyId;

	public WorkitemFormRef() {
	}

	public WorkitemFormRef(int studyId, int formVersionId) {
		this.studyId = studyId;
		this.formVersionId = formVersionId;
	}

	public void addParamQuestionMap(ParameterQuestionMap... paramQuestionMaps) {
		for (ParameterQuestionMap parameterQuestionMap : paramQuestionMaps) {
			this.paramQuestionMaps.add(parameterQuestionMap);
		}
	}

	public int getFormVersionId() {
		return formVersionId;
	}

	public List<ParameterQuestionMap> getParamQuestionMap() {
		return paramQuestionMaps;
	}

	public void setParamQuestionMaps(List<ParameterQuestionMap> paramQuestionMaps) {
		this.paramQuestionMaps = paramQuestionMaps;
	}

	public int getStudyId() {
		return studyId;
	}

	public void setFormVersionId(int formVersionId) {
		this.formVersionId = formVersionId;
	}

	public void setStudyId(int studyId) {
		this.studyId = studyId;
	}

	public boolean addParameterQuetionMap(ParameterQuestionMap e) {
		return paramQuestionMaps.add(e);
	}

	public boolean addAllParameterQuetionMap(Collection<? extends ParameterQuestionMap> c) {
		return paramQuestionMaps.addAll(c);
	}

}
