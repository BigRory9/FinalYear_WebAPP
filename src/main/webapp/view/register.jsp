<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<head>
<link rel="stylesheet" href="http://127.0.0.1:127/css/css/style.css">
<link href="http://127.0.0.1:127/css/bootstrap.min.css" rel="stylesheet"
	id="bootstrap-css">
<script src="http://127.0.0.1:127/js/bootstrap.min.js"></script>
<script src="http://127.0.0.1:127/js/jquery.min.js"></script>
<title>Welcome to GigzEaze</title>
</head>
<body background="http://127.0.0.1:127/images/Concert3.jpg">

	<!------ Include the above in your HEAD tag ---------->
	<div id="login-overlay" class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Register to GigzEaze</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-6">
						<div class="well">
							<form method="POST" action="/register"
								enctype="multipart/form-data">
								<span>${msg}</span>
								<div class="form-group">
									<label for="name" class="control-label">Name</label> <input
										type="text" name="name" id="name" class="form-control"
										placeholder="Name" required autofocus value="" required=""
										title="Please enter you name"> <span
										class="help-block"></span>
								</div>
								<div class="form-group">
									<label for="email" class="control-label">Email</label> <input
										type="email" class="form-control" id="email" name="email"
										placeholder="Email" value="" required=""
										title="Please enter your email"> <span
										class="help-block"></span>
								</div>
								<div class="form-group">
									<label for="file" class="control-label">Picture</label> <input
										type="file" name="file" accept="image/*">
								</div>
								<div class="form-group">
									<label for="inputPassword" class="control-label">Password</label>
									<input type="password" class="form-control" id="password"
										name="password" placeholder="Password" value="" required=""
										title="Please enter your password"> <span
										class="help-block"></span>
								</div>
								<div id="loginErrorMsg" class="alert alert-error hide">Wrong
									inputEmail or inputPassword</div>
								<button type="submit" class="btn btn-success btn-block">Register</button>
							</form>
						</div>
					</div>

					<div class="col-xs-6">
						<p class="lead">
							Already have an account <span class="text-success">Login</span>
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
						<form method="POST" action="/">
							<button class="btn btn-info btn-block" name="register"
								type="submit">Return to login page</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
</body>
</html>
