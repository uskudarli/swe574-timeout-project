angular.module("timeout", ["ngRoute"])
	.config(function($routeProvider) {
	  $routeProvider
	   	 .when("/", {
		    templateUrl: "main.html",
		    controller: "MainController"
		  })
		  .when("/home", {
		    templateUrl: "home.html",
		    controller: "HomeController"
		  })
		  .when("/createEvent", {
		  	templateUrl: "CreateEvent.html",
		  	controller: "createEvent"
		  })
		  .when("/createGroup", {
		  	templateUrl: "CreateGroup.html",
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
		  .when("/Search", {
		  	templateUrl: "Search.html",
		  	controller: "Search"
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
		  });
	})

	.controller("IndexController", function($scope, $http, $location, $window, timeOutFactory) {
		console.log("IndexController works");
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
	})

	.controller("MainController", function($scope, $http, $location, $window, timeOutFactory) {
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
			 	$window.alert("Specified username or password do not match with the records!!!");
			  });
		};
	})

	.controller("HomeController", function($scope, $http, $window, $location) {
		$scope.goCreateGroup = function() {
			console.log("createGroup");
			$location.path("/createGroup");
		}

		$scope.goCreateEvent = function() {
			console.log("createEvent");
			$location.path("/createEvent");
		}

		$scope.goHome = function() {
			console.log("home");
			$location.path("/home");
		}

		$scope.goMyFriends = function() {
			console.log("myFriends");
			$location.path("/myFriends");
		}

		$scope.goMyGroups = function() {
			console.log("myGroups");
			$location.path("/myGroups");
		}

		$scope.goMyEvents = function() {
			console.log("myEvents");
			$location.path("/myEvents");
		}

		$scope.goMyProfile = function() {
			console.log("myProfile");
			$location.path("/myProfile");
		}

		$scope.goEditProfile = function() {
			console.log("profileEdit");
			$location.path("/profileEdit");
		}

		$scope.goSearch = function() {
			console.log("search");
			$location.path("/search");
		}

		$scope.goEventsCreated = function() {
			console.log("eventsCreated");
			$location.path("/eventsCreated");
		}

		$scope.goSuggestedGroups = function() {
			console.log("suggestedGroups");
			$location.path("/suggestedGroups");
		}

		$scope.goNewGroups = function() {
			console.log("newGroups");
			$location.path("/newGroups");
		}

		$scope.goFriendsGroups = function() {
			console.log("friendsGroups");
			$location.path("/friendsGroups");
		}

		$scope.goEventsInvited = function() {
			console.log("eventsInvited");
			$location.path("/eventsInvited");
		}

	})

	.controller("eventsCreated", function($scope, $http, $window, $location) {

		$scope.goCreateGroup = function() {
			console.log("createGroup");
			$location.path("/createGroup");
		}

		$scope.goCreateEvent = function() {
			console.log("createEvent");
			$location.path("/createEvent");
		}

		$scope.goHome = function() {
			console.log("home");
			$location.path("/home");
		}

		$scope.goMyFriends = function() {
			console.log("myFriends");
			$location.path("/myFriends");
		}

		$scope.goMyGroups = function() {
			console.log("myGroups");
			$location.path("/myGroups");
		}

		$scope.goMyEvents = function() {
			console.log("myEvents");
			$location.path("/myEvents");
		}

		$scope.myProfile = function() {
			console.log("myProfile");
			$location.path("/myProfile");
		}

		$scope.goEditProfile = function() {
			console.log("profileEdit");
			$location.path("/profileEdit");
		}

		$scope.goSearch = function() {
			console.log("search");
			$location.path("/search");
		}
	})

	.controller("profileEdit", function($scope, $http, $window, $location) {

		$scope.goCreateGroup = function() {
			console.log("createGroup");
			$location.path("/createGroup");
		}

		$scope.goCreateEvent = function() {
			console.log("createEvent");
			$location.path("/createEvent");
		}

		$scope.goHome = function() {
			console.log("home");
			$location.path("/home");
		}

		$scope.goMyFriends = function() {
			console.log("myFriends");
			$location.path("/myFriends");
		}

		$scope.goMyGroups = function() {
			console.log("myGroups");
			$location.path("/myGroups");
		}

		$scope.goMyEvents = function() {
			console.log("myEvents");
			$location.path("/myEvents");
		}

		$scope.myProfile = function() {
			console.log("myProfile");
			$location.path("/myProfile");
		}

		$scope.goEditProfile = function() {
			console.log("profileEdit");
			$location.path("/profileEdit");
		}

		$scope.goSearch = function() {
			console.log("search");
			$location.path("/search");
		}

		$scope.profileEdit = function(){

		$http.post("http://localhost:8080/profile/edit?userName=" + $scope.userName )
		 .success(function(data, status) {
			$window.alert("Success " + data.actionId);
		  })
		  .error(function(data, status) {
		 	console.log("Error");
		  });
		 }
	})


	.controller("createEvent", function($scope, $http, $window, $location) {

		$scope.goCreateGroup = function() {
			console.log("createGroup");
			$location.path("/createGroup");
		}

		$scope.goCreateEvent = function() {
			console.log("createEvent");
			$location.path("/createEvent");
		}

		$scope.goHome = function() {
			console.log("home");
			$location.path("/home");
		}

		$scope.goMyFriends = function() {
			console.log("myFriends");
			$location.path("/myFriends");
		}

		$scope.goMyGroups = function() {
			console.log("myGroups");
			$location.path("/myGroups");
		}

		$scope.goMyEvents = function() {
			console.log("myEvents");
			$location.path("/myEvents");
		}

		$scope.myProfile = function() {
			console.log("myProfile");
			$location.path("/myProfile");
		}

		$scope.goEditProfile = function() {
			console.log("profileEdit");
			$location.path("/profileEdit");
		}

		$scope.goSearch = function() {
			console.log("search");
			$location.path("/search");
		}

		$scope.createEvent = function(){

		$http.post("http://localhost:8080/event/create?eventName=" + $scope.eventName + "&eventDescription=" + $scope.eventDescription)
		 .success(function(data, status) {
			$window.alert("Success " + data.actionId);
		  })
		  .error(function(data, status) {
		 	console.log("Error");
		  });
		 }

	})

	.controller("createGroup", function($scope, $http, $window, $location) {

		$scope.goCreateGroup = function() {
			console.log("createGroup");
			$location.path("/createGroup");
		}

		$scope.goCreateEvent = function() {
			console.log("createEvent");
			$location.path("/createEvent");
		}

		$scope.goHome = function() {
			console.log("home");
			$location.path("/home");
		}

		$scope.goMyFriends = function() {
			console.log("myFriends");
			$location.path("/myFriends");
		}

		$scope.goMyGroups = function() {
			console.log("myGroups");
			$location.path("/myGroups");
		}

		$scope.goMyEvents = function() {
			console.log("myEvents");
			$location.path("/myEvents");
		}

		$scope.myProfile = function() {
			console.log("myProfile");
			$location.path("/myProfile");
		}

		$scope.goEditProfile = function() {
			console.log("profileEdit");
			$location.path("/profileEdit");
		}

		$scope.goSearch = function() {
			console.log("search");
			$location.path("/search");
		}

		$scope.createGroup = function() {
			$http.post("http://localhost:8080/group/create?groupName=" + $scope.groupName + "&groupDescription=" + $scope.groupDescription + "&tag=" + $scope.tag)
			 .success(function(data, status) {
				$window.alert("Success " + data.actionId);
			  })
			  .error(function(data, status) {
			 	console.log("Error");
			  });
		}
	})

	.controller("myFriends", function($scope, $http, $window, $location) {

		$scope.goCreateGroup = function() {
			console.log("createGroup");
			$location.path("/createGroup");
		}

		$scope.goCreateEvent = function() {
			console.log("createEvent");
			$location.path("/createEvent");
		}

		$scope.goHome = function() {
			console.log("home");
			$location.path("/home");
		}

		$scope.goMyFriends = function() {
			console.log("myFriends");
			$location.path("/myFriends");
		}

		$scope.goMyGroups = function() {
			console.log("myGroups");
			$location.path("/myGroups");
		}

		$scope.goMyEvents = function() {
			console.log("myEvents");
			$location.path("/myEvents");
		}

		$scope.myProfile = function() {
			console.log("myProfile");
			$location.path("/myProfile");
		}

		$scope.goEditProfile = function() {
			console.log("profileEdit");
			$location.path("/profileEdit");
		}

		$scope.goSearch = function() {
			console.log("search");
			$location.path("/search");
		}
	})

	.factory("timeOutFactory", function(){
		var timeOutFactory = {};
		var lists = [];
		var userLoggedIn = false;
		var backendUrl = "http://localhost:8080";
		var sessionId = "";

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

		return timeOutFactory;
	});