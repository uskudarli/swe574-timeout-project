app = angular.module('timeout', ['ngRoute']);

// When html address is typed with one of the following links,
// RouteProvider (ng-route) will directly stick controller and html together
// and serve them in the place of dynamic content .
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
	  	templateUrl: "profileEdit.html",
	  	controller: "profileEdit"
	  })
	  .when("/uploadPhoto", {
	  	templateUrl: "uploadPhoto.html",
	  	controller: "profileEdit"
	  })
	  .when("/theGroup", {
	  	templateUrl: "theGroup.html",
	  	controller: "theGroup"
	  })
	  .when("/thePost", {
	  	templateUrl: "thePost.html",
	  	controller: "thePost"
	  })
	  .otherwise({redirectTo: '/'});
});

// To avoid see any pages without login.
app.run(function($rootScope, $location, $window) {
	$rootScope.$on( "$routeChangeStart", function(event, next, current) {
		if ((getCookie("sessionId") == undefined || getCookie("sessionId") == "")) {
			$location.path("/");
		}
	});
});

// Non-dynamic content is controlled by this controller.
app.controller("indexController", function($scope, $http, $location, $window, timeOutFactory) {
	// For logging
	console.log("indexController works");

	// Get sessionId from backend by calling login rest api, then set sessionId cookie
	$scope.doLogin = function() {
		console.log("DoLogin works");
		var params = "?userEmail=" + $scope.userEmail + "&password=" + $scope.loginPassword;

		var loginUrl = timeOutFactory.getBackendUrl() + "/login" + params;
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

	// Set sessionId cookies to ""
	$scope.doLogout = function() {
		setCookie("sessionId", "", 0);
		$location.path("/");
	};

	// In order to decide whether showing search or login on the right top corner,
	// check if the cookie is set or not
	$scope.isCookieSet = function() {
		return getCookie("sessionId") != undefined && getCookie("sessionId") != "";
	};

	// To change dynamic content when a link is pressed, $location.path method is used.
	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	// When a search is being done, search string is saved to the factory to be reached from other controllers.
	$scope.search = function(url){
		timeOutFactory.setSearchText($scope.searchText);
		$scope.goToPage(url);
	};
});

// SignUp screen or root address shows the html which is managed by this controller.
app.controller("mainController", function($scope, $http, $location, $window, timeOutFactory) {
	if(getCookie("sessionId") != undefined && getCookie("sessionId") != "") {
		console.log("GoToPage: /home");
		$location.path("/home");
	}

	// This method will be used when a user tries to be a member of the system.
	$scope.signUp = function() {
		// Parameters for register is sent
		var params = "?userEmail=" + $scope.email + "&password=" + $scope.sigUpPassword;

		// register api of backend is called
		$http.get(timeOutFactory.getBackendUrl() + "/register" + params)
		  // if register api call is successful
		  .success(function(data, status) {
		    if(data.type == "Success") {
		    	$window.alert("Welcome among us, you can now log in!");
		    	$scope.name = "";
		    	$scope.lastName = "";
		    	$scope.email = "";
		    	$scope.reEmail = "";
		    	$scope.sigUpPassword = "";
		    	$scope.rePassword = "";
		    }
		  })
		  // if register api call is unsuccessful
		  .error(function(data, status) {
		 	$window.alert(JSON.stringify(data));
		  });
	};
});

// After user logged in, home.html will be seen which managed by this controller
app.controller("homeController", function($scope, $http, $window, $location, timeOutFactory) {
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + '/' + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.eventsInvited = data;
	  })
	  .error(function(data, status) {
	 	console.alert("Error " + data);
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

// When a search request is done, search.html and this controller shows the dynamic content.
app.controller("searchController", function($scope, $http, $location, $window, timeOutFactory) {
	console.log("searchController: " + timeOutFactory.getSearchText());

	var params = "?sessionId=" + getCookie("sessionId");
	params += "&tag=" + timeOutFactory.getSearchText();

	//$scope.resultSet = [{"title": "title", "desc": "desc"}];

	$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/findRelatedGroupsforTag" + params})
	  .success(function(data, status) {
	    $scope.resultSet = data;
	    console.log(JSON.stringify(data));
	  })
	  .error(function(data, status) {
	 	$window.alert("No records have been found!!!");
	  });
});

// When user wants to edit own profile, this controller works to support the html on the dynamic content.
app.controller("profileEdit", function($scope, $http, $window, $location, timeOutFactory) {

	var params = "?sessionId=" + getCookie("sessionId");

	$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/profile/get" + params})
	  .success(function(data, status) {
	    $scope.email = data.userName;
	    $scope.password = data.password;
	    console.log(JSON.stringify(data));
	  })
	  .error(function(data, status) {
	 	$window.alert("No records have been found!!!");
	  });



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
		var paramsEdit = "?sessionId=" + getCookie("sessionId");
		paramsEdit += "&firstName=" + $scope.firstName +
					  "&lastName=" + $scope.lastName +
					  "&Gsm=" + $scope.Gsm +
					  "&address=" + $scope.address +
					  "&birthdate=" + $scope.birthdate +
					  "&about=" + $scope.about +
					  "&interests=" + $scope.interests +
					  "&gender=" + $scope.gender +
					  "&languages=" + $scope.languages;


		$http.post(timeOutFactory.getBackendUrl() + "/profile/edit" + paramsEdit)
			.success(function(data, status) {
				$window.alert("Success " + data.actionId);
			})
			.error(function(data, status) {
				$window.alert("Error " + data);
			});
	};

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

// Tested by ogzcm
app.controller("createEvent", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$scope.createEvent = function(){
		var params = "?sessionId=" + getCookie("sessionId");
		params += "&eventName=" + $scope.eventName +
				  "&eventDescription=" + $scope.eventDescription +
				  //"&startTime=" + $scope.startTime +
				  //"&endTime=" + $scope.endTime +
				  //"&invitedPeople=" + $scope.invitedPeople +
				  "&tag=" + $scope.tag +
				  "&privacy=" + $scope.privacy;

		$http.get(timeOutFactory.getBackendUrl() + "/event/create" + params)
		 .success(function(data, status) {
			$window.alert("Success " + data.actionId);
		  })
		  .error(function(data, status) {
		 	$window.alert("Error " + data);
		  });
	};
});

// Code reviewed due to backend is not available by ogzcm
app.controller("createGroup", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$scope.createGroup = function() {
		var params = "?sessionId=" + getCookie("sessionId");
		params += "&groupName=" + $scope.groupName +
				  "&groupDescription=" + $scope.groupDescription +
				  "&tag=" + $scope.tag;

		$http.get(timeOutFactory.getBackendUrl() + "/group/create" + params)
		 .success(function(data, status) {
			$window.alert("Success ");
		  })
		  .error(function(data, status) {
		 	$window.alert("Error " + data);
		  });
	};
});

app.controller("myFriends", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

// app.controller("myProfile", function($scope, $http, $window, $location){

// 	$scope.goToPage = function(url) {
// 		console.log("GoToPage: " + url);
// 		$location.path(url);
// 	};
// });

// Tested by ogzcm
app.controller("myEvents", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	console.log("myEvents");

	$http.get(timeOutFactory.getBackendUrl() + "/event/created/" + params)
	 .success(function(data, status) {
		$scope.myEvents = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("eventsCreated", function($scope, $http, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/created" + params)
	 .success(function(data, status) {
		$scope.eventsCreated = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.eventsInvited = data;
		console.log(data);
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

// Code reviewed due to backend is not available by ogzcm
app.controller("myGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/my" + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.myGroups = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("newGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.newGroups = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("theGroup", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("thePost", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("friendsGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.friendsGroups = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("suggestedGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.suggestedGroups = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	 .success(function(data, status) {
		$window.alert("Success " + data.actionId);
		$scope.eventsInvited = data;
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

// This factory keeps values shared among controllers.
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
// For setting a cookie on the browser
function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	console.log("Date d: " + d);
	d.setDate(d.getDate() + exdays);
	console.log("d.getDate + exdays: " + d);
	var expires = "expires=" + d;
	console.log(expires);
	document.cookie = cname + "=" + cvalue + "; " + expires;
};

// For getting the value of cookie which was set before.
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

// It looks if the cookie is set, or gets a value to set in order to check cookies.
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


