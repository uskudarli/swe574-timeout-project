angular.module("timeout", ["ngRoute"])
	.config(function($routeProvider) {
	  $routeProvider
	   	 .when("/", {
		    templateUrl: "main.html",
		    controller: "mainController"
		  })
		  .when("/home", {
		    templateUrl: "home.html",
		    controller: "homeController"
		  })
		  .when("/createEvent", {
		  	templateUrl: "createEvent.html",
		  	controller: "createEvent"
		  })
		  .when("/createGroup", {
		  	templateUrl: "createGroup.html",
		  	controller: "createGroup"
		  })
		  .when("/profileEdit", {
		  	templateUrl: "profileEdit.html",
		  	controller: "profileEdit"
		  })
		  .when("/eventsInvited", {
		  	templateUrl: "eventsInvited.html",
		  	controller: "eventsInvited"
		  })
		  .when("/eventsCreated", {
		  	templateUrl: "eventsCreated.html",
		  	controller: "eventsCreated"
		  })
		  .when("/friendsGroups", {
		  	templateUrl: "friendsGroups.html",
		  	controller: "friendsGroups"
		  })
		  .when("/myEvents", {
		  	templateUrl: "myEvents.html",
		  	controller: "myEvents"
		  })
		  .when("/myGroups", {
		  	templateUrl: "myGroups.html",
		  	controller: "myGroups"
		  })
		  .when("/newGroups", {
		  	templateUrl: "newGroups.html",
		  	controller: "newGroups"
		  })
		  .when("/search", {
		  	templateUrl: "search.html",
		  	controller: "searchController"
		  })
		  .when("/suggestedGroups", {
		  	templateUrl: "suggestedGroups.html",
		  	controller: "suggestedGroups"
		  })
		  .when("/myFriends", {
		  	templateUrl: "myFriends.html",
		  	controller: "myFriends"
		  })
		  .when("/myProfile", {
		  	templateUrl: "myProfile.html",
		  	controller: "myProfile"
		  })
		   .when("/eventsInvited", {
		  	templateUrl: "eventsInvited.html",
		  	controller: "eventsInvited"
		  })
		    .when("/eventsCreated", {
		  	templateUrl: "eventsCreated.html",
		  	controller: "eventsCreated"
		  })
		    .when("/myGroups", {
		  	templateUrl: "myGroups.html",
		  	controller: "myGroups"
		  })
		    .when("/newGroups", {
		  	templateUrl: "newGroups.html",
		  	controller: "newGroups"
		  })

		    .when("/friendsGroups", {
		  	templateUrl: "friendsGroups.html",
		  	controller: "friendsGroups"
		  })

		    .when("/suggestedGroups", {
		  	templateUrl: "suggestedGroups.html",
		  	controller: "suggestedGroups"
		  })
		    .when("/eventsInvited", {
		  	templateUrl: "eventsInvited.html",
		  	controller: "eventsInvited"
		  })
		  .otherwise({redirectTo: '/'});
	})

	.controller("indexController", function($scope, $http, $location, $window, timeOutFactory) {
		console.log("indexController works");
		$scope.doLogin = function() {
			// Simple GET request example :
			var loginUrl = timeOutFactory.getBackendUrl() + "/login?userEmail=" + $scope.userEmail + "&password=" + $scope.loginPassword;
			console.log(loginUrl);
			$http({method: "GET",  url: loginUrl})
			  .success(function(data, status) {
			    if(data.type == "Success") {
			    	$location.path("/home");
			    	timeOutFactory.setUserLoggedIn(true);
			    	timeOutFactory.setSessionId(data.cookie);
			    } else {
			    	$window.alert(data.type + ": " + data.message);
			    }
			  })
			  .error(function(data, status) {
			 	$window.alert("Specified username or password do not match with the records!!!");
			  });
		};
		$scope.isCookieSet = function() {
			return timeOutFactory.isUserLoggedIn();
		};
		$scope.goToPage = function(url) {
			console.log("GoToPage:" + url);
			$location.path(url);
		};
		$scope.search = function(url){
			timeOutFactory.setSearchText($scope.searchText);
			$scope.goToPage(url);
		};
	})

	.controller("mainController", function($scope, $http, $location, $window, timeOutFactory) {
		$scope.signUp = function() {
			// Simple GET request example :
			$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/register?userEmail=" + $scope.email + "&password=" + $scope.sigUpPassword})
			  .success(function(data, status) {
			    if(data.type == "Success") {
			    	$window.alert(data.message);
			    	$scope.name = "";
			    	$scope.lastName = "";
			    	$scope.email = "";
			    	$scope.reEmail = "";
			    	$scope.sigUpPassword = "";
			    	$scope.rePassword = "";
			    }
			  })
			  .error(function(data, status) {
			 	$window.alert(JSON.stringify(data));
			  });
		};
	})

	.controller("homeController", function($scope, $http, $window, $location) {
		console.log("homeController works");
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
			//$location.path("/eventsCreated");
			//$location.path("/suggestedGroups");
			//$location.path("/newGroups");
			//$location.path("/friendsGroups");
			//$location.path("/eventsInvited");
		};
	})

	.controller("searchController", function($scope, $http, $location, $window, timeOutFactory) {
		$scope.resultSet = ["deneme", "deneme"];
		// Simple GET request example :
		console.log("searchController: " + timeOutFactory.getSearchText());
		// $http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/searchTag?tag=" + timeOutFactory.getSearchText()})
		//   .success(function(data, status) {
		//     console.log(JSON.stringify(data));
		//     $scope.resultSet = data;
		//   })
		//   .error(function(data, status) {
		//  	$window.alert("Specified username or password do not match with the records!!!");
		//   });
	})

	.controller("eventsCreated", function($scope, $http, $window, $location) {
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller("profileEdit", function($scope, $http, $window, $location, timeOutFactory) {
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
		$scope.genderOption = ['male', 'female', 'other'];
		$scope.selectedLanguage = ['Turkish','English', 'French', 'German', 'Italian', 'Spanish'];
		$scope.profileEdit = function(){
		$http.post(timeOutFactory.getBackendUrl() + '/profile/edit?userEmail=' + $scope.userName )
		 .success(function(data, status) {
			$window.alert("Success " + data.actionId);
		  })
		  .error(function(data, status) {
		 	console.log("Error");
		  });
		 }
	})

	.controller("createEvent", function($scope, $http, $window, $location, timeOutFactory) {
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};

		$scope.createEvent = function(){
			var sessionId = timeOutFactory.getSessionId();
			var config = {headers: {'Set-Cookie': String(sessionId)} };
			$http.get(timeOutFactory.getBackendUrl() + '/event/create' + '?eventName=' + $scope.eventName + '&eventDescription=' + $scope.eventDescription, config)
			 .success(function(data, status) {
				$window.alert("Success " + data.actionId);
			  })
			  .error(function(data, status) {
			 	console.log("Error " + data);
			  });
		}
	})

	.controller("createGroup", function($scope, $http, $window, $location, timeOutFactory) {
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};

		$scope.createGroup = function() {
			$http.post('/group/create?groupName=' + $scope.groupName + '&groupDescription=' + $scope.groupDescription + '&tag=' + $scope.tag)
			 .success(function(data, status) {
				$window.alert("Success " + data.actionId);
			  })
			  .error(function(data, status) {
			 	console.log("Error");
			  });
		};
	})

	.controller("myFriends", function($scope, $http, $window, $location) {
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('myProfile', function($scope, $http, $window, $location){
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};

	})

	.controller('myEvents', function($scope, $http, $window, $location){



		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('eventsInvited', function($scope, $http, $window, $location){
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('eventsCreated', function($scope, $http, $window, $location){
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('myGroups', function($scope, $http, $window, $location){
		$scope.myGroups = [{name:"KediSevenler", count:"5"}, {name:"FareSevenler", count:"10"}, {name:"KopekSevenler", count:"15"}];



		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('newGroups', function($scope, $http, $window, $location){
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('friendsGroups', function($scope, $http, $window, $location){
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('suggestedGroups', function($scope, $http, $window, $location){
		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.controller('eventsInvited', function($scope, $http, $window, $location, timeOutFactory){
		var sessionId = timeOutFactory.getSessionId();
		var config = {headers: {'Set-Cookie': String(sessionId)} };

		$http.get(timeOutFactory.getBackendUrl() + '/event/invited', config)
		 .success(function(data, status) {
			$window.alert("Success " + data.actionId);
			$scope.eventsInvited = data;
		  })
		  .error(function(data, status) {
		 	console.log("Error " + data);
		  });

		$scope.goToPage = function(url) {
			console.log("GoToPage: " + url);
			$location.path(url);
		};
	})

	.factory("timeOutFactory", function(){
		var timeOutFactory = {};
		var lists = [];
		var userLoggedIn = false;
		//var backendUrl = "http://localhost:8080";
		var backendUrl = "http://timeoutswe5743.appspot.com";
		var sessionId = "";
		var searchText = "";

		timeOutFactory.getLists = function(){
			return lists;
		};

		timeOutFactory.getList = function(listId){
			return lists[listId];
		};

		timeOutFactory.addList = function(newList){
			lists.push({
				id:lists.length,
				name:newList,
				items:[]
			});
		};

		timeOutFactory.addItem = function(listId, newItem){
			lists[listId].items.push(newItem);
		};

		timeOutFactory.isUserLoggedIn = function(){
			return userLoggedIn;
		};

		timeOutFactory.setUserLoggedIn = function(value){
			userLoggedIn = value;
		};

		timeOutFactory.getBackendUrl = function(){
			return backendUrl;
		};

		timeOutFactory.getSessionId = function(){
			return sessionId;
		};

		timeOutFactory.setSessionId = function(cookie){
			sessionId = cookie;
		};

		timeOutFactory.getSearchText = function(){
			return searchText;
		};

		timeOutFactory.setSearchText = function(text){
			searchText = text;
		};

		return timeOutFactory;
	});