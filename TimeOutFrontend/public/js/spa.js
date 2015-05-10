app = angular.module('timeout', ['ngRoute']);

app.config(function($routeProvider) {
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
	  .when("/uploadPhoto", {
	  	templateUrl: "uploadPhoto.html",
	  	controller: "profileEdit"
	  })
	  .otherwise({redirectTo: '/'});
});

app.controller("indexController", function($scope, $http, $location, $window, timeOutFactory) {
	console.log("indexController works");

	$scope.doLogin = function() {
		var loginUrl = timeOutFactory.getBackendUrl() + "/login?userEmail=" + $scope.userEmail + "&password=" + $scope.loginPassword;
		$http({method: "GET",  url: loginUrl})
		  .success(function(data, status) {
		    if(data.type == "Success") {
				setCookie("sessionId", data.sessionId, 60);
				$location.path("/home");
		    } else {
		    	$window.alert(data.type + ": " + data.message);
		    }
		  })
		  .error(function(data, status) {
		 	$window.alert("Specified username or password do not match with the records!!!");
		  });
	};

	$scope.isCookieSet = function() {
		return getCookie("sessionId") != undefined && getCookie("sessionId") != "";
	};

	$scope.goToPage = function(url) {
		console.log("GoToPage:" + url);
		$location.path(url);
	};

	$scope.search = function(url){
		timeOutFactory.setSearchText($scope.searchText);
		$scope.goToPage(url);
	};
});

app.controller("mainController", function($scope, $http, $location, $window, timeOutFactory) {
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
});

app.controller("homeController", function($scope, $http, $window, $location, timeOutFactory) {
	var sessionId = timeOutFactory.getSessionId();
	var config = {headers: {'Set-Cookie': String(sessionId)} };

	$http.get(timeOutFactory.getBackendUrl() + '/', config)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.eventsInvited = data;
	  })
	  .error(function(data, status) {
	 	console.log("Error " + data);
	  });


	var suggestedGroups = [{name:'' ,detail:'"Math "'}];
	console.log("homeController works properly");
	$scope.notificationList2 = [{name:'sara', detail:'"Math fans"'}];

	timeOutFactory.addList("notificationList", $scope.notificationList2);

	$scope.notificationList = timeOutFactory.getList("notificationList");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$scope.newsFeed = [
	{name:'ali',detail:'math'},
	{name:'ali', detail:'physics'}
	];
});

app.controller("searchController", function($scope, $http, $location, $window, timeOutFactory) {
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
});

app.controller("profileEdit", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$("#imgInp").change(function(){
		readURL(this);
	});

	$scope.data = 'none';
	$scope.add = function(){
		var f = document.getElementById('photo').files[0];
		r = new FileReader();
		r.onloadend = function(e){
			$scope.data = e.target.result;
		}
		r.readAsBinaryString(f);
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
});

app.controller("createEvent", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$scope.createEvent = function(){
		var params = '?sessionId=' + timeOutFactory.getSessionId();
		params += '&eventName=' + $scope.eventName + '&eventDescription=' + $scope.eventDescription;

		$http.get(timeOutFactory.getBackendUrl() + '/event/create' + params)
		 .success(function(data, status) {
			$window.alert("Success " + data.actionId);
		  })
		  .error(function(data, status) {
		 	console.log("Error " + data);
		  });
	};
});

app.controller("createGroup", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$scope.createGroup = function() {
		var params = '?sessionId=' + timeOutFactory.getSessionId();
		params += '&groupName=' + $scope.groupName + '&groupDescription=' + $scope.groupDescription + '&tag=' + $scope.tag;

		$http.get(timeOutFactory.getBackendUrl() + '/group/create' + params)
		 .success(function(data, status) {
			$window.alert("Success ");
		  })
		  .error(function(data, status) {
		 	console.log("Error");
		  });
	};
});

app.controller("myFriends", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('myProfile', function($scope, $http, $window, $location){

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('myEvents', function($scope, $http, $window, $location, timeOutFactory){
	var params = '?sessionId=' + timeOutFactory.getSessionId();

	$http.get(timeOutFactory.getBackendUrl() + '/event/created/' + params)
	 .success(function(data, status) {
		$scope.eventsCreated = data;
	  })
	  .error(function(data, status) {
	 	console.log("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('eventsCreated', function($scope, $http, $location, timeOutFactory, EventsService){
	var params = '?sessionId=' + timeOutFactory.getSessionId();

	$http.get(timeOutFactory.getBackendUrl() + '/event/created' + params)
	 .success(function(data, status) {
		$scope.eventsCreated = data;
	  })
	  .error(function(data, status) {
	 	console.log("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('eventsInvited', function($scope, $http, $window, $location, timeOutFactory){
	var params = '?sessionId=' + timeOutFactory.getSessionId();

	$http.get(timeOutFactory.getBackendUrl() + '/event/invited' + params)
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
});

app.controller('newGroups', function($scope, $http, $window, $location, timeOutFactory){
	var sessionId = timeOutFactory.getSessionId();
	var config = {headers: {'Set-Cookie': String(sessionId)} };

	$http.get(timeOutFactory.getBackendUrl() + '/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk', config)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.newGroups = data;
	  })
	  .error(function(data, status) {
	 	console.log("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('friendsGroups', function($scope, $http, $window, $location, timeOutFactory){
	var sessionId = timeOutFactory.getSessionId();
	var config = {headers: {'Set-Cookie': String(sessionId)} };

	$http.get(timeOutFactory.getBackendUrl() + '/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk', config)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.friendsGroups = data;
	  })
	  .error(function(data, status) {
	 	console.log("Error " + data);
	  });


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('suggestedGroups', function($scope, $http, $window, $location, timeOutFactory){
	var sessionId = timeOutFactory.getSessionId();
	var config = {headers: {'Set-Cookie': String(sessionId)} };

	$http.get(timeOutFactory.getBackendUrl() + '/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk', config)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.suggestedGroups = data;
	  })
	  .error(function(data, status) {
	 	console.log("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller('eventsInvited', function($scope, $http, $window, $location, timeOutFactory){
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
});

app.factory("timeOutFactory", function(){
	var timeOutFactory = {};
	var lists = {};
	var userLoggedIn = false;
	// var backendUrl = "http://localhost:8080";
	var backendUrl = "http://timeoutswe5743.appspot.com";
	var searchText = "";

	timeOutFactory.getLists = function(){
		return lists;
	};

	timeOutFactory.getList = function(listName){
		return lists[listName];
	};

	timeOutFactory.addList = function(name, list){
		lists[name] = list;
	};

	timeOutFactory.getBackendUrl = function(){
		return backendUrl;
	};

	timeOutFactory.getSearchText = function(){
		return searchText;
	};

	timeOutFactory.setSearchText = function(text){
		searchText = text;
	};

	return timeOutFactory;
});

// JS Functions
function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	console.log("Date d: " + d);
	d.setDate(d.getDate() + exdays);
	console.log("d.getDate + exdays: " + d);
	var expires = "expires=" + d;
	console.log(expires);
	document.cookie = cname + "=" + cvalue + "; " + expires;
};

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
	var c = ca[i];
	while (c.charAt(0)==' ') c = c.substring(1);
		if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
	}
	return "";
};

function checkCookie() {
	var user = getCookie("username");
	if (user != "") {
		alert("Welcome again " + user);
	} else {
		user = prompt("Please enter your name:", "");
		if (user != "" && user != null) {
			setCookie("username", user, 365);
		}
	}
};


