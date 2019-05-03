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

<link href="http://127.0.0.1:127/pulse/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="http://127.0.0.1:127/css/success.css" rel="stylesheet">
</head>

<body class="text-center">
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a class="navbar-brand" href="http://localhost:8080/homepage"><img
			src="http://127.0.0.1:127/images/logo_2.png" height="70px" /> Gigz
			Eaze</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarColor03" aria-controls="navbarColor03"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarColor03">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item"><a class="nav-link"
					href="http://localhost:8080/homepage">Home <span
						class="sr-only">(current)</span>
				</a></li>
				<li class="nav-item active"><a class="nav-link"
					href="/viewTickets">View Your Tickets</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Account
						Information</a></li>
				<li class="nav-item"><a class="nav-link"
					href="http://localhost:8080/logout">Logout</a></li>
			</ul>
			</ul>
			<form action="/searchEvents" method="GET"
				class="form-inline my-2 my-lg-0">
				<input id="keyword" name="keyword" class="form-control mr-sm-2"
					type="text" placeholder="Search">
				<button class="btn btn-secondary my-2 my-sm-0" type="submit">Search</button>
			</form>
		</div>
	</nav>
	<%-- <br>Image here: <img src= ${picUrl} />--%>
	<div id="gig-search">
		<c:forEach var="list" items="${lists}">
			<div class="search-result">
				<img src="http://127.0.0.1:127/images/logo_2.png" />
				<!-- <p>${list.id}</p> -->
				<div class="search-content">
					Your ticket
					<p class="artist">${list.name}</p>
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
					<form id="downloadPdf" method="GET"
						action="/download-PDF/${list.id}" novalidate="novalidate"
						target="_blank">
						<button class="purchase-button">Open as PDF</button>
					</form>
				</div>
			</div>
		</c:forEach>
	</div>
</body>
</html>