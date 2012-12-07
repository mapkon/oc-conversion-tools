#!/usr/bin/env groovy

ZIP_FILENAME="dep.zip"
DESTINATION_FOLDERNAME="./temp"
TOMCAT_INSTALLATION_FOLDER="/usr/local/tomcat/"

println "OpenXdata-OpenClinica Installer".center(150, '-')

def OXD_FOLDERNAME
def exists = false
def tomcatWEBAPPSDIR = new File("${TOMCAT_INSTALLATION_FOLDER}/webapps")

// Confirm openxdata installation
tomcatWEBAPPSDIR.eachDir {

	if(it.name =~ /^[oO]pen[xX][dD]ata([0-9]+)?/) {

		OXD_FOLDERNAME = it.name
		exists = true
	}
}

if(exists) {

	println "OpenXData installed. Proceeding with install..."

	def builder = new AntBuilder()
	builder.unzip(src:ZIP_FILENAME, dest:DESTINATION_FOLDERNAME, overwrite:"true")

	def destFolder = new File("${DESTINATION_FOLDERNAME}")
	destFolder.eachDir {

		// Delete MACOSX folder
		if(it.name =~/^_{2}[A-Z]/) {
			builder.delete(dir:it)
		}
	}

	// Move jar files to openxdata lib folder

	def jarFiles = []
	def depFolder = new File("${DESTINATION_FOLDERNAME}/dep")

	if(depFolder.isDirectory()) {

		depFolder.eachFile {
			if(it.name =~ /\w+.jar/)
				jarFiles.add(it)
		}
	}

	def OXD_INSTALL_DIR = "${TOMCAT_INSTALLATION_FOLDER}/webapps/${OXD_FOLDERNAME}"
	def OXD_INSTALL_LIB_DIR = "${OXD_INSTALL_DIR}/WEB-INF/lib"

	// Make sure we move 3 jar files
	//
	// groovy-{version}.jar
	// xform-study-importer-{version}.jar
	// oc-converstion-tools-{version}.jar
	//
	if(jarFiles.size() == 3) {

		jarFiles.each {

			println "Moving ${it.name} to ${OXD_INSTALL_LIB_DIR}"

			builder.copy(todir:OXD_INSTALL_LIB_DIR, file:it, overwrite:"true")
		}
	}

	// Move the jsp file and css 
	println "Moving the jsp and css files..."

	builder.copy(todir:"${TOMCAT_INSTALLATION_FOLDER}/webapps/${OXD_FOLDERNAME}", overwrite:"true") {
		fileset(dir:"${DESTINATION_FOLDERNAME}/dep/jsp")
	}

	// Editing the web.xml file to map the openclinica servlet

	println "Mapping the openclinica servlet..."

	def xml = new XmlSlurper(false, false).parse("${OXD_INSTALL_DIR}/WEB-INF/web.xml")

	xml.appendNode {

		servlet {

			'servlet-name'('openclinica')
			'servlet-class'('org.openxdata.oc.servlet.OpenClinicaServlet')
		}

		'servlet-mapping' {

			'servlet-name'('openclinica')
			'url-pattern'('/openclinica')
			'url-pattern'('/openxdata/openclinica')
			'url-pattern'('/openXdata/openclinica')
		}
	}

	def file = new File("web.xml").setText(groovy.xml.XmlUtil.serialize(xml))
	builder.copy(file:"web.xml", tofile:"${OXD_INSTALL_DIR}/WEB-INF/web.xml")

	// Delete temporary extraction folder

	println "Cleaning up..."
	
	builder.delete(dir:"temp")
	builder.delete(file:"web.xml")
} 
else {
	println "openxdata is not installed in ${TOMCAT_INSTALLATION_FOLDER/webapps}. Please install openxdata to proceed"
}










