<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../../../favicon.ico">

<title>You have Logged in</title>

<!-- Bootstrap core CSS -->
<link
	href="C:\Users\roryh\Documents\workspace-sts-3.9.6.RELEASE\login\target\classes\static"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="index.css" rel="stylesheet">
</head>

<body class="text-center">
	Hello ${user.email}
	 <%-- <br>Image here: <img src= ${picUrl} />--%>
	
	

	<table align="center" cellpadding="5" cellspacing="5" border="1">
		<thead>
			<tr bgcolor="#A52A2A">
				<th>Id</th>
				<th>Name</th>
				<th>Arena</th>
				<th>Price</th>
				<th>Date</th>
				<th>Time</th>
				<th>Purchase a ticket</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="list" items="${lists}">
				<tr bgcolor="#DEB887">
					<td>${list.id}</td>
					<td>${list.displayName}</td>
					<td>${list.arena}</td>
					<td>${list.price}</td>
					<td>${list.date}</td>
					<td>${list.time}</td>
					<td><a href="<c:url value='/purchase-tickets/${list.id}' />">Purchase</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>



	<form method="POST" action="http://localhost:8080/tickets">
		<button class="btn btn-info btn-block" name="tickets" type="submit">
			View Events</button>
	</form>
</body>
</html>
