package org.openxdata.proto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A workitem is an object holding references of forms
 * 
 * In this case it holds the openxdata form ids.
 * 
 * On the mobile a workitem can be thought of a a list of prefilled forms grouped together.
 * 
 * NB: To avoid breaking backwards compatibility, do not change method signatures or remove methods from this class.
 */
public class OxdWorkitem {

	/**
	 * This is a string that uniquely identifies a workitem. This id is usually needed by the workitem engine that is
	 * generating the workitems
	 */
	private String workitemId;
	/**
	 * Name of workitem that will be displayed
	 */
	private String workitemName;
	/**
	 * List of openxdata forms(ids)
	 */
	private List<WorkitemFormRef> workitemForms = new ArrayList<WorkitemFormRef>();

	/**
	 * Hash map to contain any other parameter that that API users may wish to add to the workitem. This is simply to
	 * provide flexibility in case the API user needs to add more information into the workitem.
	 */
	private Map<String, Object> parameters = new HashMap<String, Object>();

	public OxdWorkitem() {
	}

	public OxdWorkitem(String workitemId, String workitemName) {
		this.workitemId = workitemId;
		this.workitemName = workitemName;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemName(String workitemName) {
		this.workitemName = workitemName;
	}

	public String getWorkitemName() {
		return workitemName;
	}

	public List<WorkitemFormRef> getWorkitemForms() {
		return workitemForms;
	}

	public void setWorkitemForms(List<WorkitemFormRef> workitemForms) {
		this.workitemForms = workitemForms;
	}

	public void addWorkitemFormRef(WorkitemFormRef workitemFormRef) {
		this.workitemForms.add(workitemFormRef);
	}

	/**
	 * Returns all parameters contained in this workitem
	 * 
	 * @return map of all parameters in this workitem
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * Add any other parameters or information you may wish to the workitem
	 * 
	 * @param paramName
	 *            paramater name
	 * @param obj
	 *            object
	 */
	public void addParameter(String paramName, Object obj) {
		parameters.put(paramName, obj);
	}

	/**
	 * Remove a given parameter from a workitem
	 * 
	 * @param paramName
	 *            parameter name
	 * @return object removed from the workitem
	 */
	public Object removeParameter(String paramName) {
		return parameters.remove(paramName);
	}

}
