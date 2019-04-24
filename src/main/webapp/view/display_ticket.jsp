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
<script src="https://checkout.stripe.com/checkout.js"></script>
<script type="text/javascript">
	Stripe.setPublishableKey('pk_test_6ypfwwRrX7SPmxZNpY6Fl49M');
</script>
</head>

<title>Homepage</title>

<!-- Bootstrap core CSS -->
<link
	href="C:\Users\roryh\Documents\workspace-sts-3.9.6.RELEASE\login\target\classes\static"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="http://127.0.0.1:127/css/index.css" rel="stylesheet">
</head>

<body class="text-center">
	<p>Your Ticket ${name}</p>
	<br>${eventName}
	<!-- <img src="http://127.0.0.1:127/images/FYPBRAND.png" alt="logo"/>  <br> -->
	<div style="background-size: 30% 30%;">
		<img src="http://127.0.0.1:127/images/logo_2.png" alt="logo"
			style="position: relative; object-fit: cover; object-position: center; max-height: 300px; max-width: 300px; padding-left: 250px;"
			align="middle"  /> <br>
	</div>

	<div class="column">
		<img role="presentation" sizes="100vw" src=${picUrl }
			style="position: relative; object-fit: cover; object-position: center; max-height: 300px; max-width: 300px">
	</div>
	<br>
	<div class="column">
		<img src="data:image/jpeg;base64,${QRcode}" align="middle" />
	</div>
	<br>
	<a href="<c:url value="/download-PDF/${ticket_id}" />"  target="_blank" method="POST" novalidate="novalidate">Open as PDF</a>
	<br>
	<br>

</body>
</html>
