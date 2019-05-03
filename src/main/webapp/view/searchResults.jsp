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
</body>
</html>