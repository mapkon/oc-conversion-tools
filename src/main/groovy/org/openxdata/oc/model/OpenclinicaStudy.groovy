package org.openxdata.oc.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString;


/**
 * Simple representation of the OpenClinica Study.
 */
@ToString(includeNames=true)
@EqualsAndHashCode public class OpenclinicaStudy {
	
	String OID
	String name
	String identifier
}
