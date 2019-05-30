<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<head>
<link href="http://127.0.0.1:127/css/bootstrap.min.css" rel="stylesheet"
	id="bootstrap-css">
<script src="http://127.0.0.1:127/css/bootstrap.css" rel="stylesheet"
	id="bootstrap-css"></script>
<title>Welcome to FairTicket</title>
</head>
<body background="http://127.0.0.1:127/images/Concert3.jpg">

	<!------ Include the above in your HEAD tag ---------->
	<!--  Brand Logo: <img src="http://127.0.0.1:127/images/FYPBRAND.png" alt="logo"/>  <br>  -->
	<div id="login-overlay" class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Login to GigzEaze</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-6">
						<div class="well">
							<form id="loginForm" method="POST" action="/login/"
								novalidate="novalidate" commandName="user" modelAttribute="user">
								<span>${msg}</span>
								<div class="form-group">
									<label for="inputEmail" class="control-label">Email</label> <input
										type="text" class="form-control" id="inputEmail"
										name="inputEmail" value="" required=""
										title="Please enter you inputEmail"
										placeholder="example@gmail.com"> <span
										class="help-block"></span>
								</div>
								<div class="form-group">
									<label for="inputPassword" class="control-label">Password</label>
									<input type="password" class="form-control" id="inputPassword"
										name="inputPassword" value="" required=""
										title="Please enter your inputPassword" placeholder="password">
									<span class="help-block"></span>
								</div>
								<div id="loginErrorMsg" class="alert alert-error hide">Wrong
									Email or Password Try Again!!</div>
								<button type="submit" class="btn btn-success btn-block">Login</button>
							</form>
						</div>
					</div>
					<div class="col-xs-6">
						<p class="lead">
							Register now for <span class="text-success">FREE</span>
						</p>
						<ul class="list-unstyled" style="line-height: 2">
							<li><span class="fa fa-check text-success"></span> See all
								your tickets</li>
							<li><span class="fa fa-check text-success"></span> Order
								drinks for collection</li>
							<li><span class="fa fa-check text-success"></span> Save your
								favorites Acts</li>
							<li><span class="fa fa-check text-success"></span> Fast
								checkout</li>
							
						</ul>
						<form method="POST" action="registerPage">
							<button class="btn btn-info btn-block" name="register"
								type="submit">Yes please, register now!</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>