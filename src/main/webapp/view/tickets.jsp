<!DOCTYPE html >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="sat, 01 Dec 2001 00:00:00 GMT">
</head>
<h2 align="center">
	<font><strong>Tickets that can be purchased</strong></font>
</h2>
<table align="center" cellpadding="5" cellspacing="5" border="1">
	<thead>
		<tr bgcolor="#A52A2A">
			<th>Id</th>
			<th>Name</th>
			<th>Purchase</th>
		</tr>
	</thead>
	<tbody>
	Image here: <img src="https://tickets-images-fare.s3-eu-west-1.amazonaws.com/Image%20Number%2033" />
		<c:forEach var="ticket" items="${tickets}">
			<tr bgcolor="#DEB887">
				<td>${ticket.id}</td>
				<td>${ticket.name}</td>
				<td><script src="https://checkout.stripe.com/checkout.js"
						class="stripe-button" data-key="pk_test_6ypfwwRrX7SPmxZNpY6Fl49M"
						data-amount="999" data-name="Demo Site"
						data-description="Example charge"
						data-image="https://stripe.com/img/documentation/checkout/marketplace.png"
						data-locale="auto" data-currency="eur">
				</script> <a href="<c:url value='/delete-tickets/${ticket.id}' />">Purchase</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</html>
