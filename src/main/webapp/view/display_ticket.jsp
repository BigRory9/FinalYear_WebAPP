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
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="sat, 01 Dec 2001 00:00:00 GMT">
</head>

<title>Homepage</title>

<!-- Custom styles for this template -->
<style type="text/css">
body {
	font-family: sans-serif;
	background-color:#E1D1EC;
	color: #E1D1EC !important;
	margin: 5px;
}

table {
	border-collapse: collapse;
	padding: 0;
	width: 100%;
}

td {
	padding: 0;
	vertical-align: top;
}

.ticket-title {
	color: #999;
	font-size: 16px;
	letter-spacing: 0;
	line-height: 16px;
	margin-top: 10px;
}

.ticket-info {
	color: #535353;
	font-size: 14px;
	line-height: 21px;
}

.ticket-wrapper {
	border: 2px solid #999;
	border-top: 12px solid rgb(33, 150, 243);
	margin: 15px auto 0;
	padding-bottom: 15px;
	background-color: white;
	width: 800px;
}

.ticket-wrapper:first-child {
	margin-top: 0;
}

.ticket-table {
	
}

.ticket-table .first-col {
	width: 570px;
}

.ticket-logo {
	border-left: 2px dashed #ccc;
	text-align: center;
	vertical-align: middle;
	background-color: white;
}

.ticket-logo img {
	height: 189px;
	width: 150px;
}

.ticket-name-div {
	border-bottom: 2px dotted #ccc;
	margin: 0 12px 0 22px;
	padding: 15px 0px 15px 0;
	text-align: left;
}

.ticket-event-longtitle {
	color: #535353;
	font-size: 22px;
	letter-spacing: -1px;
	line-height: 22px;
	background-color: white;
}

.ticket-event-shorttitle {
	color: #535353;
	font-size: 18px;
	letter-spacing: -1px;
	line-height: 22px;
}

.ticket-event-details {
	border-bottom: 2px dotted #ccc;
	margin: 0 12px 0px 22px;
	padding: 15px 0px 15px 0;
	text-align: left;
}

.ticket-event-details .first-col {
	text-align: left;
	width: 40%;
}

.ticket-event-details .second-col {
	text-align: right;
	vertical-align: top;
	width: 60%;
}

.ticket-venue {
	color: #535353;
	font-size: 14px;
	line-height: 21px;
}

.ticket-venue:first-child {
	font-size: 16px;
}

.ticket-ticket-details {
	margin: 0 12px 0px 22px;
	text-align: left;
}

.ticket-ticket-details .first-col {
	border-right: 2px dashed #ccc;
	padding-top: 4px;
	text-align: left;
	vertical-align: top;
	width: 150px;
}

.ticket-ticket-details .second-col {
	padding: 4px 0px 0px 32px;
	text-align: left;
	width: 225px;
}

.ticket-ticket-details .third-col {
	text-align: right;
}

.ticket-qr-code {
	height: 95px;
	margin-top: 10px;
	width: 95px;
}

/* Print specific styles */
@media print {
	a[href]:after, abbr[title]:after {
		content: "";
	}
	.ticket-wrapper {
		width: 16cm;
	}
	.ticket-table .first-col {
		width: 13.8cm;
	}
	.ticket-logo img {
		height: auto;
		max-width: 100%;
	}
	.ticket-ticket-details .first-col {
		width: 4cm;
	}
	.ticket-ticket-details .second-col {
		width: 6cm;
	}
	.ticket-ticket-details .third-col {
		width: 2.5cm;
	}
	@page {
		margin: 1cm;
	}
}
</style>

<link href="http://127.0.0.1:127/pulse/bootstrap.min.css"
	rel="stylesheet">

<link href="http://127.0.0.1:127/css/success.css" rel="stylesheet">
</head>
<body>
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
				<li class="nav-item"><a class="nav-link" href="#">Account
						Information</a></li>
				<li class="nav-item"><a class="nav-link" href="http://localhost:8080/logout">Logout</a></li>
			</ul>
			<form action="/searchEvents" method="GET"
				class="form-inline my-2 my-lg-0">
				<input id="keyword" name="keyword" class="form-control mr-sm-2"
					type="text" placeholder="Search">
				<button class="btn btn-secondary my-2 my-sm-0" type="submit">Search</button>
			</form>
		</div>
	</nav>
	<!-- Start Ticket -->
	<div class="ticket-wrapper">
		<table class="ticket-table">
			<tr>
				<td class="first-col">
					<!-- title -->
					<div class="ticket-name-div">
						<span class="ticket-event-longtitle">${event.displayName}</span>
					</div> <!-- /.ticket-name-div --> <!-- venue details start -->
					<div class="ticket-event-details">
						<table>
							<tr>
								<td class="first-col">
									<div class="ticket-info">${event.date}</div> <!-- /.ticket-info -->
									<div class="ticket-title">TIME</div> <!-- /.ticket-title -->
									<div class="ticket-info">${event.time}</div> <!-- /.ticket-info -->
								</td>
								<!-- /.first-col -->
								<td class="second-col">
									<div class="ticket-venue">Place's name</div> <!-- /.ticket-venue -->
									<div class="ticket-venue">${event.arena}</div> <!-- /.ticket-venue -->
								</td>
								<!-- /.second-col -->
							</tr>
						</table>
					</div> <!-- /.ticket-event-details --> <!-- ticket details start -->
					<div class="ticket-ticket-details">
						<table>
							<tr>
								<td class="first-col">
									<div class="ticket-title">TICKET #</div> <!-- /.ticket-title -->
									<div class="ticket-title">PRICE</div> <!-- /.ticket-title -->
									<div class="ticket-info">€${event.price}</div> <!-- /.ticket-info -->
								</td>
								<!-- /.first-col -->
								<td class="second-col">
									<div class="ticket-title">TICKET OWNER</div> <!-- /.ticket-title -->
									<div class="ticket-info">${name}</div> <!-- /.ticket-info -->
									<div class="ticket-title">TICKET TYPE</div> <!-- /.ticket-title -->
									<div class="ticket-info">General Access</div> <!-- /.ticket-info -->
								</td>
								<!-- /.second-col -->
								<td class="third-col"><a href="#" target="_blank"> <img
										class="ticket-qr-code"
										src='data:image/jpeg;base64,${QRcode}' alt="qrcode" />
								</a></td>
								<!-- /.third-col -->
							</tr>
						</table>
					</div> <!-- /.ticket-ticket-details -->
				</td>
				<!-- /.first-col -->
				<td class="ticket-logo">
				<img src="${picUrl }" alt="Logo" /><br>
				<a href="<c:url value="/download-PDF/${ticket_id}" />"  target="_blank" method="POST" novalidate="novalidate">Download as PDF</a>	
				</td>
				<!-- /.ticket-logo -->
			</tr>
		</table>
		<!-- /.ticket-table -->
	</div>
	<!-- /.ticket-wrapper -->
	<!-- End Ticket -->
</body>
