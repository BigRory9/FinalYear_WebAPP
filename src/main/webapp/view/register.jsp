<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<head>
<script src="static/js/charge.js"></script>
<link rel="stylesheet"  href="static/css/style.css" >
<link href="static/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/jquery.min.js"></script>
<title>Welcome to FairTicket</title>
</head>
<body>

	<!------ Include the above in your HEAD tag ---------->
	<div id="login-overlay" class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Register to TicketFare</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-6">
						<div class="well">
							<form id="loginForm" method="POST" action="register" enctype="multipart/form-data">
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
										<label for="file" class="control-label">Picture</label>
										<input type="file" name="file" accept="image/*">
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
				</div>
			</div>
		</div>
	</div>
</body>
</html>
