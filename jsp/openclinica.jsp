<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>OpenClinica</title>
	<link type="text/css" rel="stylesheet" href="static/css/system.css" />
</head>
<body>
	<form action="openclinica" method="post">
	   <div class = "container">
	   <div class = "header"></div>
	   <div class = "bodyDiv">
		   <div class = "heading">OpenClinica Study Management</div>
		   <div class="studies-table">
		   		<div class = "studies-table-label">Study</div>
		   		<div class="study-table">
					<c:if test="${request.getParameter("study") != null}">
						<table cellpadding="0" cellspacing="0" width="800px">
							<thead>
								<tr>
									<th>Name</th>
									<th>OID</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>study.name</td>
								</tr>
								<tr>
									<td>study.studyKey</td>
								</tr>
								<tr></tr>
							</tbody>
						</table>
					</c:if test>

		   		</div>
				
				Study OID: <input type="text" name="oid"/>
		   		<div class="buttonStrip">
		   			<input class="button-style" type="submit" value="Import" name="action"/>
		   			<input class="button-style" type="submit" value="Export" name="action"/>
		   		</div>
		   </div>
		</div>
	 </div>
	</form>
</body>
</html>
