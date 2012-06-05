
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>OpenClinica-OpenXData Study Management</title>
			<link type="text/css" rel="stylesheet" href="static/css/system.css" />
	</head>
	<body>
		<form action="openclinica" method="post">
			<div class="container">
				<div class="header"></div>
				<div class="bodyDiv">
					<div class="heading">OpenClinica OpenXdata Study Management</div>
					<div class="studies-table">

					<div class="studies-table-label">Enter Details and Choose action to perform</div>
						
						<div class="input-div">
							Study OID:
							<input type="text" name="oid" />
						</div>
						<div class="buttonStrip">
							<input class="button-style" type="submit" value="Import"
								name="action" title="Import" />
							<input class="button-style" type="submit" value="Export"
								name="action" title="Export" />
						</div>
						<div class="study-table">

							<c:if test="${not empty name }">
								<table cellpadding="0" cellspacing="0" width="600px">
									<thead>
										<tr>
											<th>Study Name</th>
											<th>Study OID</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>${name}</td>
											<td>${key}</td>
										</tr>
									</tbody>
								</table>
							</c:if>
						</div>
						
						<div class="studies-table-label">Notification Center</div>
						<c:forEach items="${message}" var="msg">
							<div class="message-div">${msg.value} ${msg.key}  </div>
						</c:forEach>
					</div>
				</div>
			</div>
		</form>
	</body>
</html>
