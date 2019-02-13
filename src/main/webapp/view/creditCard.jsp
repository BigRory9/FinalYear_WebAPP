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
</head>
<h2 align="center">
	<font><strong>Tickets that can be purchased</strong></font>
</h2>
<body>
HELLO
	<form action='/charge' method='POST' id='checkout-form'novalidate="novalidate">
    <input type='hidden' th:value='${amount}' name='amount' />
    <label>Price:<span th:text='${amount/100}' /></label>
    <!-- NOTE: data-key/data-amount/data-currency will be rendered by Thymeleaf -->
    <script
       src='https://checkout.stripe.com/checkout.js'
       class='stripe-button'
       th:attr='data-key=${stripePublicKey}, 
         data-amount=${amount}, 
         data-currency=${currency}'
       data-name='Baeldung'
       data-description='Spring course checkout'
       data-image
         ='http://www.baeldung.com/wp-content/themes/baeldung/favicon/android-chrome-192x192.png'
       data-locale='auto'
       data-zip-code='false'>
   </script>
</form>
</body>
</html>
