$(document).ready(function() {
	
	$("#login_form").on('submit', function(event) {
		event.preventDefault();
		$.ajax({
			url: '/api/login',
			type: 'POST',
			data: 
			{ 	"username": $("#loginName").val(),
				"password": $("#loginPassword").val()
			},
			dataType: 'json',
			success: function(response) {
				console.log(response);
				if (response.token == undefined || response.token == null) {
					window.location.href = "login.html";
				}
				sessionStorage.setItem('token', response.token);
				console.log(response);
				window.location.href="index.html";
			},
			error: function(error) {
				console.log(error);
				window.location.href="index.html";
			}
		})

	})

})

function checkToken() {
	$.ajax({
		url: '/api/checkToken',
		type: 'POST',
		data: { "token": sessionStorage.getItem('token') },
		dataType: 'json',
		success: function(autorizado) {
			if (!(autorizado)) {
				window.location.href = "login.html";
			}
		},
		error: function(error) {
			window.location.href = "login.html";
		}
	})
}