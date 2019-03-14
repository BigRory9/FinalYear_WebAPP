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
	Your Ticket ${name}
	<br>${eventName}</br>
	<div style="background-size: 30% 30%;">
		<img role="presentation" sizes="100vw" src=${picUrl } 
		 style="position: relative; object-fit: cover; object-position: center; max-height: 300px; max-width: 300px">
	</div>
	<br>
	<br> QR CODE here :
	<img src="data:image/jpeg;base64,${QRcode}" />
	<br>
	<br>
	<br>

</body>
</html>
