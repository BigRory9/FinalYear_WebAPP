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
<link href="https://fonts.googleapis.com/css?family=Montserrat"
	rel="stylesheet">

<title>You have Logged in</title>

<!-- Bootstrap core CSS -->
<link
	href="C:\Users\roryh\Documents\workspace-sts-3.9.6.RELEASE\login\target\classes\static"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="http://127.0.0.1:127/css/success.css" rel="stylesheet">
</head>

<body class="text-center">
	Hello ${user.email}
	<%-- <br>Image here: <img src= ${picUrl} />--%>

	<div id="gig-search">
		<c:forEach var="list" items="${lists}">
			<div class="search-result">
				<img src="${list.imageUrl}" />
				<!-- <p>${list.id}</p> -->
				<div class="search-content">
					<p class="artist">${list.displayName}</p>
					<div class="search-info">
						<p class="location">${list.arena}</p>
						<div class="datetime">
							<p class="date">${list.date}</p>
							<p class="time">${list.time}</p>
						</div>
					</div>
				</div>
				<div class="pricing-info">
					<p class="price">€ ${list.price}</p>
					<!-- <p><a href="<c:url value='/purchase-tickets'><c:param name="id" value="${list.id}"/></c:url>">Purchase</a></p> -->
					<button class="purchase-button">Purchase</button>
				</div>
			</div>
		</c:forEach>
	</div>

	<div id="gig-list">
		<c:forEach var="list" items="${lists}">
			<div class="gig">
				<div class="start">
					<img src="${list.imageUrl}" />
					<!-- <p>${list.id}</p> -->
					<p class="artist">${list.displayName}</p>
				</div>
				<div class="end">
					<div class="info">
						<p class="location">${list.arena}</p>
						<div class="datetime">
							<p class="date">${list.date}</p>
							<p class="time">${list.time}</p>
						</div>
					</div>
					<p class="price">€ ${list.price}</p>
					<!-- <p><a href="<c:url value='/purchase-tickets'><c:param name="id" value="${list.id}"/></c:url>">Purchase</a></p> -->
					<button class="purchase-button">Purchase</button>
				</div>
			</div>
		</c:forEach>
	</div>

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
				<th>Image Url</th>
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
					<td><a
						href="<c:url value='/purchase-tickets'><c:param name="id" value="${list.id}"/></c:url>">Purchase</a></td>
					<td>${list.imageUrl}</td>
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
