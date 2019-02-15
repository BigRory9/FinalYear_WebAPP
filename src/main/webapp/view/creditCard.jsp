<!DOCTYPE html >
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
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
<h2 align="center">
	<font><strong>Tickets that can be purchased</strong></font>
</h2>
<body>
	Choose Payment
	Type:
	<br> Pay With Credit Card:
	<form action="${requestScope['javax.servlet.forward.request_uri']}/process" method="POST">
		<script src="https://checkout.stripe.com/checkout.js"
			class="stripe-button" data-key="pk_test_6ypfwwRrX7SPmxZNpY6Fl49M"
			data-amount="999" data-name="TicketFare"
			data-description="Example charge"
			data-image="https://stripe.com/img/documentation/checkout/marketplace.png"
			data-locale="auto" data-currency="eur">
			
		</script>

	</form>
</body>
</html>
