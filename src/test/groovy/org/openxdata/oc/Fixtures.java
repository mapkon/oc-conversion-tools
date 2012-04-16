/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.oc.model.StudySubject;

/**
 *
 * @author kay
 */
public class Fixtures {

	public static StudyDef getOXDStudy() {
		try {
			InputStream resourceAsStream = Fixtures.class.getClassLoader().getResourceAsStream("Default_Study.xml");
			String toString = IOUtils.toString(resourceAsStream);
			Editable importStudyItem = StudyImport.importStudyItem(toString);

			return (StudyDef) importStudyItem;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static List<StudySubject> getStudySubjectEvents() {
		try {
			InputStream resourceAsStream = Fixtures.class.getClassLoader().getResourceAsStream(
					"subject-event-response.xml");
			String studySubjectResponse = IOUtils.toString(resourceAsStream);
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
