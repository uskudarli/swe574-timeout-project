angular.module('deneme', [])

	.controller('IndexController', function($scope, $window, $http, $templateCache) {
		$scope.doLogin = function(username, loginPassword){
			$scope.name = $scope.username;
			$scope.lastname = $scope.loginPassword;
			// Simple GET request example :
			$http({method: 'GET',  url: 'http://localhost:8080/login?userName=' + $scope.username + '&password=' + $scope.loginPassword, 
				cache: $templateCache}).
			  success(function(data, status) {
			    $scope.name = JSON.stringify(data);
			    $scope.lastname = status;
			    $scope.email = 'Success';
			  }).
			  error(function(data, status) {
			    $scope.name = JSON.stringify(data);
			    $scope.lastname = status;
			    $scope.email = 'Error';
			  });
					}
	});

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