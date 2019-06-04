<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- <%@ include file="header.html" %>  Bringing in nav bar  --%>
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
<link href="https://fonts.googleapis.com/css?family=Montserrat"
	rel="stylesheet">

<title>You have Logged in</title>

<!-- Bootstrap core CSS -->
<link href="http://127.0.0.1:127/pulse/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="http://127.0.0.1:127/css/home.css" rel="stylesheet">

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
				<li class="nav-item active"><a class="nav-link"
					href="http://localhost:8080/homepage">Home <span
						class="sr-only">(current)</span>
				</a></li>
				<li class="nav-item"><a class="nav-link" href="/viewTickets">View
						Your Tickets</a></li>
				<li class="nav-item"><a class="nav-link" href="/createGroup">Create
						Group</a></li>
				<li class="nav-item"><a class="nav-link"
					href="http://localhost:8080/logout">Logout</a></li>
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
	<div id="gig-list">
		<%-- Select a Category:&nbsp; <select name="category">
			<c:forEach items="${groups}" var="group">
				<option value="${group.id}">${group.groupName}</option>
			</c:forEach>
		</select> <br /> --%>
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
				<form action="/purchase-tickets/process-group" method="POST">
						Select a Group:&nbsp; <select name="group">
							<c:forEach items="${groups}" var="group">
								<option  value="${group.id}">${group.groupName}</option>
							</c:forEach>
					<input type="hidden" name="id" value=${list.id } />
					<!-- <input name="id"  value ={id}/> -->
					<script src="https://checkout.stripe.com/checkout.js"
						class="stripe-button" data-key="pk_test_6ypfwwRrX7SPmxZNpY6Fl49M"
						data-amount=data-name= "Gigz
						Eaze"
							data-description="Making it simply a better experience"
						data-image="http://127.0.0.1:127/images/logo_2.png"
						data-locale="auto" data-currency="eur">
						
					</script>
					<button type="submit" class="purchase-button">Purchase For
						A Group</button>
				</form>
				<!-- 	<button type="submit" class="purchase-button">Purchase</button> -->

			</div>
		</div>
	</div>
</body>
</html>
