angular.module("timeout", ["ngRoute"])
	.config(function($routeProvider) {
	  $routeProvider
	   .when("/", {
	    templateUrl: "main.html"
	  })
	  .when("/home", {
	    templateUrl: "home.html"
	  })
	})

	.controller("IndexController", function($scope, $http, $location, $window) {
		console.log("IndexController works");
		$scope.doLogin = function(username, loginPassword) {
			// Simple GET request example :
			$http({method: "GET",  url: "http://localhost:8080/login?userName=" + $scope.username + "&password=" + $scope.loginPassword})
			  .success(function(data, status) {
			    if(data.type == "Success") {
			    	$location.path("/home");
			    	//setUser();
			    }
			  })
			  .error(function(data, status) {
			 	console.log("Specified username or password do not match with the records!!!");
			  });
		}
		$scope.signUp = function() {
			// Simple GET request example :
			$http({method: "GET",  url: "http://localhost:8080/register?userName=" + $scope.email + "&password=" + $scope.sigUpPassword})
			  .success(function(data, status) {
			    if(data.type == "Success") {
			    	$window.alert(data.message);
			    	//setUser();
			    }
			  })
			  .error(function(data, status) {
			 	console.log("Specified username or password do not match with the records!!!");
			  });
		}
	});

	// .controller("HomeController", function($scope, $http) {
	// 	$scope.doLogin = function(username, loginPassword){
	// 		$scope.name = $scope.username;
	// 		$scope.lastname = $scope.loginPassword;
	// 		$scope.name = "Method is running";
	// 		// Simple GET request example :
	// 		$http({method: "GET",  url: "http://localhost:8080/login?userName=" + $scope.username + "&password=" + $scope.loginPassword})
	// 		.success(function(data, status) {
	// 		  if(data.type == "Success") {
	// 			$scope.name = "Success is taken" + data.type;
	// 				$location.path("/home");
	// 			}
	// 		})
	// 		.error(function(data, status) {
	// 			console.log("Specified username or password do not match with the records!!!");
	// 		});
	// 	}
	// });

/*$http.post('/someUrl', {msg:'hello word!'}).
  success(function(data, status, headers, config) {

    // this callback will be called asynchronously
    // when the response is available
  }).
  error(function(data, status, headers, config) {
    // called asynchronously if an error occurs
    // or server returns response with an error status.
  });
*/
/*function Hello($scope, $http) {
	$http.get('http://rest-service.guides.spring.io/greeting').success(function(data, status, headers, config) {
		$scope.greeting = data;
	}).
  error(function(data, status, headers, config) {
    // called asynchronously if an error occurs
    // or server returns response with an error status.
  });

  	// Simple POST request example (passing data) :
	$http.post('http://localhost:8080/', {msg:'hello word!'}).
	  success(function(data, status, headers, config) {
	    // this callback will be called asynchronously
	    // when the response is available
	  }).
	  error(function(data, status, headers, config) {
	    // called asynchronously if an error occurs
	    // or server returns response with an error status.
	  });
}*/