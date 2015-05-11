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
	  .otherwise({redirectTo: '/'});
});

// Tested by ogzcm, remove cookie set when error occurs
app.controller("indexController", function($scope, $http, $location, $window, timeOutFactory) {
	console.log("indexController works");

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
		    	$window.alert(data.type + ": " + data.message + " (1000)");
		    }
		  })
		  .error(function(data, status) {
			$window.alert("Specified username or password do not match with the records!!! (1001)");

			// setCookie("sessionId", "ogzcm58", 60);
			// $location.path("/home");
		  });
	};

	$scope.isCookieSet = function() {
		return getCookie("sessionId") != undefined && getCookie("sessionId") != "";
	};

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};

	$scope.search = function(url){
		timeOutFactory.setSearchText($scope.searchText);
		$scope.goToPage(url);
	};
});

// Tested by ogzcm
app.controller("mainController", function($scope, $http, $location, $window, timeOutFactory) {
	if(getCookie("sessionId") != undefined && getCookie("sessionId") != "") {
		console.log("GoToPage: /home");
		$location.path("/home");
	}

	$scope.signUp = function() {
		// Simple GET request example :
		var params = "?userEmail=" + $scope.email + "&password=" + $scope.sigUpPassword;

		$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/register" + params})
		  .success(function(data, status) {
		    if(data.type == "Success") {
		    	$window.alert("Welcome among us, you can now log in! (1002)");
		    	$scope.name = "";
		    	$scope.lastName = "";
		    	$scope.email = "";
		    	$scope.reEmail = "";
		    	$scope.sigUpPassword = "";
		    	$scope.rePassword = "";
		    }
		  })
		  .error(function(data, status) {
		 	$window.alert(JSON.stringify(data) + " (1003)");
		  });
	};
});

app.controller("homeController", function($scope, $http, $window, $location, timeOutFactory) {

	// NOTIFICATIONS -- Backend is being waited
	// var params = "?sessionId=" + getCookie("sessionId");

	// $http.get(timeOutFactory.getBackendUrl() + '/' + params)
	//  .success(function(data, status) {
	// 	$scope.eventsInvited = data;
	// 	console.log("Success: " + data.actionId + " (1004)");
	//   })
	//   .error(function(data, status) {
	//  	$window.alert("Error: " + data + " (1005)");
	//   });

	var suggestedGroups = [{name:'' ,detail:'"Math "'}];
	$scope.notificationList2 = [{name:'sara', detail:'"Math fans"'}];
	timeOutFactory.addList("notificationList", $scope.notificationList2);
	$scope.notificationList = timeOutFactory.getList("notificationList");

	$scope.newsFeed = [
		{name:'ali',detail:'math'},
		{name:'ali', detail:'physics'}
	];

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url);
		$location.path(url);
	};
});

app.controller("searchController", function($scope, $http, $location, $window, timeOutFactory) {
	console.log("searchController: " + timeOutFactory.getSearchText() + " (1006)");

	var params = "?sessionId=" + getCookie("sessionId");
	params += "&tag=" + timeOutFactory.getSearchText();

	//$scope.resultSet = [{"title": "title", "desc": "desc"}];

	$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/findRelatedGroupsforTag" + params})
	  .success(function(data, status) {
	    $scope.resultSet = data;
	    console.log(JSON.stringify(data) + " (1007)");
	  })
	  .error(function(data, status) {
	 	$window.alert("No records have been found!!! (1008)");
	  });
});

app.controller("profileEdit", function($scope, $http, $window, $location, timeOutFactory) {

	var params = "?sessionId=" + getCookie("sessionId");

	$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/profile/get" + params})
	  .success(function(data, status) {
	    $scope.email = data.userName;
	    $scope.password = data.password;
	    console.log(JSON.stringify(data));
	  })
	  .error(function(data, status) {
	 	$window.alert("No records have been found!!! (1009)");
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
				$window.alert("Success: " + data.actionId + " (1010)");
			})
			.error(function(data, status) {
				$window.alert("Error: " + data + " (1011)");
			});
	};

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1012)");
		$location.path(url);
	};
});

// Tested by ogzcm
app.controller("createEvent", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1013)");
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
			$window.alert("Success " + data.actionId + " (1014)");
		  })
		  .error(function(data, status) {
		 	$window.alert("Error " + data + " (1015)");
		  });
	};
});

// Code reviewed due to backend is not available by ogzcm
app.controller("createGroup", function($scope, $http, $window, $location, timeOutFactory) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1016)");
		$location.path(url);
	};

	$scope.createGroup = function() {
		var params = "?sessionId=" + getCookie("sessionId");
		params += "&groupName=" + $scope.groupName +
				  "&groupDescription=" + $scope.groupDescription +
				  "&tag=" + $scope.tag;

		$http.get(timeOutFactory.getBackendUrl() + "/group/create" + params)
		 .success(function(data, status) {
			$window.alert("Success: " + data + " (1017)");
		  })
		  .error(function(data, status) {
		 	$window.alert("Error: " + data + " (1018)");
		  });
	};
});

app.controller("myFriends", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1019)");
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

	console.log("myEvents (1020)");

	$http.get(timeOutFactory.getBackendUrl() + "/event/created/" + params)
	 .success(function(data, status) {
		$scope.myEvents = data;
		console.log("Success: " + data + " (1042)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data + " (1021)");
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1022)");
		$location.path(url);
	};
});

app.controller("eventsCreated", function($scope, $http, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/created" + params)
	 .success(function(data, status) {
		$scope.eventsCreated = data;
		console.log("Success: " + data + " (1043)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data + " (1023)");
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1024)");
		$location.path(url);
	};
});

app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	 .success(function(data, status) {
		$scope.eventsInvited = data;
		console.log("Success: " + data + " (1025)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1026)");
		$location.path(url);
	};
});

// Code reviewed due to backend is not available by ogzcm
app.controller("myGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/my" + params)
	 .success(function(data, status) {
		$scope.myGroups = data;
		console.log("Success: " + data + " (1027)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data);
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1028)");
		$location.path(url);
	};
});

app.controller("newGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	 .success(function(data, status) {
		$scope.newGroups = data;
		console.log("Success: " + data + " (1029)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data + " (1030)");
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1031)");
		$location.path(url);
	};
});

app.controller("theGroup", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1032)");
		$location.path(url);
	};
});

app.controller("friendsGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	 .success(function(data, status) {
		$scope.friendsGroups = data;
		console.log("Success: " + data + " (1033)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data + " (1034)");
	  });


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1035)");
		$location.path(url);
	};
});

app.controller("suggestedGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	 .success(function(data, status) {
		$scope.suggestedGroups = data;
		console.log("Success: " + data + " (1036)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data + " (1037)");
	  });

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1038)");
		$location.path(url);
	};
});

app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	 .success(function(data, status) {
		$scope.eventsInvited = data;
		console.log("Success: " + data + " (1039)");
	  })
	  .error(function(data, status) {
	 	$window.alert("Error " + data + " (1040)");
	  });


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1041)");
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


