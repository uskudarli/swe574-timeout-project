app = angular.module('timeout', ['ngRoute', 'angular-md5', 'ngTouch', 'angucomplete']);

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
	.when("/theGroup/:groupId", {
		templateUrl: "theGroup.html",
		controller: "theGroup"
	})
	.when("/thePost", {
		templateUrl: "thePost.html",
		controller: "thePost"
	})
	.when("/theUser", {
		templateUrl: "theUser.html",
		controller: "theUser"
	})
	.when("/updateGroup", {
		templateUrl: "updateGroup.html",
		controller: "updateGroup"
	})
	.when("/updateEvent", {
		templateUrl: "updateEvent.html",
		controller: "updateEvent"
	})
	.when("/newEvents", {
		templateUrl: "newEvents.html",
		controller: "newEvents"
	})
	.when("/eventInvitation", {
		templateUrl: "eventInvitation.html",
		controller: "eventInvitation"
	})
	.when("/groupInvitation", {
		templateUrl: "groupInvitation.html",
		controller: "groupInvitation"
	})
	.when("/suggestedEvents", {
		templateUrl: "suggestedEvents.html",
		controller: "suggestedEvents"
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
app.controller("indexController", function($scope, $http, $location, $window, timeOutFactory, md5, getProfile) {
	// For logging
	$scope.searchText = [];
	$scope.searchContextUrl = timeOutFactory.getBackendUrl() + '/searchContext?tag=';
	$scope.profileInfo = null;

	// Get sessionId from backend by calling login rest api, then set sessionId cookie
	$scope.doLogin = function() {
		// For security reasons, password is encrypted with MD5 hash
		var params = "?userEmail=" + $scope.userEmail + "&password=" + md5.createHash($scope.loginPassword);

		var loginUrl = timeOutFactory.getBackendUrl() + "/login" + params;
		$http.get(loginUrl)
		.success(function(data, status) {
			if(data.type == "Success") {
				setCookie("sessionId", data.sessionId, 60);
				$scope.getProfile();

				// Trigger backend to run insert recommendation
				$http.get(timeOutFactory.getBackendUrl() + "/insertRecommendation?sessionId=" + data.sessionId)
					.success(function(data, status) {
						console.log("Insert recommentation has been called (1036)");
					});
				// Get event notification for user
				$http.get(timeOutFactory.getBackendUrl() + '/getEventRecommendation?sessionId=' + data.sessionId)
					.success(function(data, status) {
						$scope.eventRecommendation = data;
						console.log("Event recommendation " + JSON.stringify(data) + " (1038)");
				  	})
				  	.error(function(data, status) {
				 		console.log("Error (1037)");
				  	});

				// Get group notification for user
				$http.get(timeOutFactory.getBackendUrl() + '/getGroupRecommendation?sessionId=' + data.sessionId)
					.success(function(data, status) {
						$scope.groupRecommendation = data;
						console.log("Group recommendation " + JSON.stringify(data));
				  	})
				  	.error(function(data, status) {
				 		console.log("Error (1076)");
				  	});

				// Get event notification for user
				$http.get(timeOutFactory.getBackendUrl() + '/getUserRecommendation?sessionId=' + data.sessionId)
					.success(function(data, status) {
						$scope.userRecommendation = data;
						console.log("User recommendation " + JSON.stringify(data) + " (1039)");
				  	})
				  	.error(function(data, status) {
				 		console.log("Error (1075)");
				  	});

				$location.path("/home");
			} else {
				$window.alert(data.type + ": " + data.message + " (1001)");
			}
		})
		.error(function(data, status) {
			$window.alert("Specified username or password do not match with the records!!!" + " (1002)");
		});
	};

	if($scope.profileInfo == null && getCookie("sessionId")) {
		var getProfilePromise = getProfile.getData();
	    getProfilePromise.then(function(result) {  // this is only run after $http completes
	    	$scope.profileInfo = result;
	    });
	}
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

	// Gets profile and keeps it in the scope
	$scope.getProfile = function() {
		if($scope.profileInfo == null && getCookie("sessionId") != undefined && getCookie("sessionId") != "") {
			var getProfilePromise = getProfile.getData();
		    getProfilePromise.then(function(result) {  // this is only run after $http completes
		       $scope.profileInfo = result;
		    });
		}
	}

	// To change dynamic content when a link is pressed, $location.path method is used.
	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1040)");
		$location.path(url);
	};

	// When a search is being done, search string is saved to the factory to be reached from other controllers.
	$scope.search = function(url){
		timeOutFactory.setSearchText($scope.searchText);
		console.log("SearchText= " + JSON.stringify($scope.searchText) + " (1200)");
		$scope.goToPage(url);
	};
});

// SignUp screen or root address shows the html which is managed by this controller.
app.controller("mainController", function($scope, $http, $location, $window, timeOutFactory, md5) {
	if(getCookie("sessionId") != undefined && getCookie("sessionId") != "") {
		console.log("GoToPage: /home" + " (1041)");
		$location.path("/home");
	}

	$scope.roleSet = ["student", "prep. student", "alumni", "erasmus & exchange","professor", "staff", ];

	// This method will be used when a user tries to be a member of the system.
	$scope.signUp = function() {
		// Check if specified information is OK
		// validate();

		// validate if email is unique
		var emailOk = "";
		$http.get(timeOutFactory.getBackendUrl() + "/email/isAvailable")
			.success(function(data){
				if (data.type == "Success") {
					emailOk == "success";
				} else {
					$window.alert(data.message + " (1034)");
					return;
				}
			})
			.error(function(data){
				$window.alert("An error occurred ()" + " (1003)");
			});

		// Parameters for register is adjusted and password is encrypted with MD5 hash
		var params = "?userEmail=" + $scope.email + "&password=" + md5.createHash($scope.sigUpPassword);
		params = params + "&firstName=" + $scope.name + "&lastName=" + $scope.lastName + "&role=" + $scope.role;

		// register api of backend is called
		$http.get(timeOutFactory.getBackendUrl() + "/register" + params)
			// if register api call is successful
			.success(function(data, status) {
				if(data.type == "Success") {
					$window.alert("Welcome among us, you can now log in!" + " (1004)");
					$scope.name = "";
					$scope.lastName = "";
					$scope.email = "";
					$scope.reEmail = "";
					$scope.sigUpPassword = "";
					$scope.rePassword = "";
				} else {
					$window.alert(data.message + " (1033)");
				}
			})
			// if register api call is unsuccessful
			.error(function(data, status) {
				$window.alert(JSON.stringify(data) + " (1005)");
			});
		};
	});

// After user logged in, home.html will be seen which managed by this controller
app.controller("homeController", function($scope, $http, $window, $location, timeOutFactory) {
	var suggestedGroups = [{name:'' ,detail:'"Math "'}];
	$scope.notificationList2 = [{name:'sara', detail:'"Math fans"'}];

	timeOutFactory.addList("notificationList", $scope.notificationList2);

	$scope.notificationList = timeOutFactory.getList("notificationList");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1042)");
		$location.path(url);
	};

	$scope.newsFeed = [
	{name:'ali',detail:'math'},
	{name:'ali', detail:'physics'}
	];
});

// When a search request is done, search.html and this controller shows the dynamic content.
app.controller("searchController", function($scope, $http, $location, $window, timeOutFactory) {
	var search = timeOutFactory.getSearchText();
	console.log("Search= " + JSON.stringify(search));
	var params = "?contextId=" + search[0].originalObject.id;

	$http({method: "GET",  url: timeOutFactory.getBackendUrl() + "/find" + params})
		.success(function(data, status) {
			$scope.resultSet = data;
		})
		.error(function(data, status) {
			$window.alert("No records have been found!!!" + " (1007)");
		});
});

// When user wants to edit own profile, this controller works to support the html on the dynamic content.
app.controller("profileEdit", function($scope, $http, $window, $location, timeOutFactory) {

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/profile/get" + params)
	.success(function(data, status) {
		$scope.email = data.userName;
		$scope.password = data.password;
		console.log(JSON.stringify(data) + " (1045)");
	})
	.error(function(data, status) {
		$window.alert("No records have been found!!!" + " (1008)");
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


		$http.get(timeOutFactory.getBackendUrl() + "/profile/edit" + paramsEdit)
		.success(function(data, status) {
			$window.alert("Success " + data.actionId) + " (1009)";
		})
		.error(function(data, status) {
			$window.alert("Error " + data + " (1010)");
		});
	};

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1046)");
		$location.path(url);
	};
});

// Tested by ogzcm
app.controller("createEvent", function($scope, $http, $window, $location, $filter, timeOutFactory) {

	// Initialize variables and set default values
	$scope.selectedTags = [];
	$scope.searchContextUrl = timeOutFactory.getBackendUrl() + '/searchContext?tag=';
	var defaultDate = new Date();
	$scope.startTime = $filter('date')(defaultDate, "dd/MM/yyyy HH:mm:ss");
	defaultDate.setMinutes(defaultDate.getMinutes() + 30);
	$scope.endTime = $filter('date')(defaultDate, "dd/MM/yyyy HH:mm:ss");

	// If user wants to delete an entered tag
	$scope.deleteTag = function(index) {
		$scope.selectedTags.splice(index, 1);
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1047)");
		$location.path(url);
	};

	$scope.createEvent = function(){
		// Convert to date
		var startDate = convertDate($scope.startTime);
		var endDate = convertDate($scope.endTime);

		if (startDate > endDate) {
			$window.alert("Start time should not be later than end time!!!" + " (1011)");
			return;
		}
		if ($scope.eventName == undefined || $scope.eventName == "" ||
			$scope.eventDescription == undefined || $scope.eventDescription == "" ||
			$scope.startTime == undefined || $scope.startTime == "" ||
			$scope.endTime == undefined || $scope.endTime == "") {
			$window.alert("Please fill required fields which have *!!!" + " (1012)");
		} else {
			var params = "?sessionId=" + getCookie("sessionId");
			params += 	"&eventName=" + $scope.eventName +
						"&eventDescription=" + $scope.eventDescription +
						"&startTime=" + $scope.startTime +
						"&endTime=" + $scope.endTime +
						//"&invitedPeople=" + $scope.invitedPeople +
						"&tag=" + JSON.stringify(timeOutFactory.adjustTagsForRest($scope.selectedTags)) +
						"&privacy=" + ($scope.privacy == true ? "P" : "C");

			$http.get(timeOutFactory.getBackendUrl() + "/event/create" + params)
				.success(function(data, status) {
					$window.alert("Success " + data.actionId + " (1013)");
				})
				.error(function(data, status) {
					$window.alert("Error " + data + " (1014)");
				});
		}
	};
});

// Code reviewed due to backend is not available by ogzcm
app.controller("createGroup", function($scope, $http, $window, $location, timeOutFactory) {

	// Initialize variables
	$scope.selectedTags = [];
	$scope.searchContextUrl = timeOutFactory.getBackendUrl() + '/searchContext?tag=';

	// If user wants to delete an entered tag
	$scope.deleteTag = function(index) {
		$scope.selectedTags.splice(index, 1);
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1048)");
		$location.path(url);
	};

	$scope.createGroup = function() {
		var params = "?sessionId=" + getCookie("sessionId");
		params += 	"&groupName=" + $scope.groupName +
					"&groupDescription=" + $scope.groupDescription +
					"&tag=" + JSON.stringify(timeOutFactory.adjustTagsForRest($scope.selectedTags)) +
					"&privacy=" + ($scope.privacy == true ? "P" : "C");

		$http.get(timeOutFactory.getBackendUrl() + "/group/create" + params)
		.success(function(data, status) {
			$window.alert("Success " + " (1015)");
		})
		.error(function(data, status) {
			$window.alert("Error " + data + " (1016)");
		});
	};
});

app.controller("myFriends", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1049)");
		$location.path(url);
	};
});

app.controller("newEvents", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1070)");
		$location.path(url);
	};
});

app.controller("eventInvitation", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1071)");
		$location.path(url);
	};
});

app.controller("groupInvitation", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1072)");
		$location.path(url);
	};
});

app.controller("suggestedEvents", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1073)");
		$location.path(url);
	};
});

// Tested by ogzcm
app.controller("myEvents", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/created/" + params)
	.success(function(data, status) {
		$scope.myEvents = data;
	})
	.error(function(data, status) {
		$window.alert("Error " + data + " (1017)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1050)");
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
		$window.alert("Error " + data + " (1018)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1051)");
		$location.path(url);
	};
});

app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	.success(function(data, status) {
		$window.alert("Success " + data.actionId + " (1019)");
		$scope.eventsInvited = data;
		console.log(data + " (1052)");
	})
	.error(function(data, status) {
		$window.alert("Error " + data + " (1020)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1053)");
		$location.path(url);
	};
});

app.controller("myGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/my" + params)
	.success(function(data, status) {
		console.log("Success " + " (1021)");
		$scope.myGroups = data;
	})
	.error(function(data, status) {
		$window.alert("Error " + data + " (1022)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1054)");
		$location.path(url);
	};
});

app.controller("newGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/new" + params)
	.success(function(data, status) {
		$window.alert("Success (1023)");
		$scope.newGroups = data;
	})
	.error(function(data, status) {
		$window.alert("Error (1024)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1055)");
		$location.path(url);
	};
});

app.controller("theGroup", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1056)");
		$location.path(url);
	};
});

app.controller("thePost", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1057)");
		$location.path(url);
	};
});

app.controller("theUser", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1058)");
		$location.path(url);
	};
});

app.controller("updateGroup", function($scope, $http, $window, $location, timeOutFactory){
	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1059)");
		$location.path(url); };

		$scope.updateGroup = function(){
			var params ="?sessionId=" + getCookie("sessionId")+
						"&groupName=" + $scope.groupName +
						"&groupDescription=" + $scope.groupDescription +
						"&tag=" + $scope.tag;

			  $http.get(timeOutFactory.getBackendUrl() + "no backend part yet" + params)
			  .success(function(data, status) {
			  	$window.alert("Success " + " (1035)");
			  	$scope.myGroups = data;
			  	$location.path("/myGroups");
			  })
			  .error(function(data, status) {
			  	$window.alert("Error " + data + " (1036)");
			  });
		}
});

app.controller("updateEvent", function($scope, $http, $window, $location, timeOutFactory){
	//var params = "?sessionId=" + getCookie("sessionId");

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1060)");
		$location.path(url);
	};


		$scope.updateEvent = function(){

			var params ="?sessionId=" + getCookie("sessionId") +
						"&eventName=" + $scope.eventName +
						"&eventDescription=" + $scope.eventDescription +
						//"&startTime=" + $scope.startTime +
						//"&endTime=" + $scope.endTime +
						//"&invitedPeople=" + $scope.invitedPeople +
						"&tag=" + JSON.stringify($scope.selectedTags) +
						"&privacy=" + $scope.privacy;

			  $http.get(timeOutFactory.getBackendUrl() + "no backend part yet" + params)
			  .success(function(data, status) {
			  	$window.alert("Success " + " (1035)");
			  	$scope.myGroups = data;
			  	$location.path("/myEvents");
			  })
			  .error(function(data, status) {
			  	$window.alert("Error " + data + " (1036)");
			  });
		}
});

app.controller("friendsGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	.success(function(data, status) {
		$window.alert("Success " + data.actionId + " (1025)");
		$scope.friendsGroups = data;
	})
	.error(function(data, status) {
		$window.alert("Error " + data + " (1026)");
	});


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1061)");
		$location.path(url);
	};
});

app.controller("suggestedGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/degisecekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk/" + params)
	.success(function(data, status) {
		$window.alert("Success " + data.actionId + " (1027)");
		$scope.suggestedGroups = data;
	})
	.error(function(data, status) {
		$window.alert("Error " + data + " (1028)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1062)");
		$location.path(url);
	};
});

app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	.success(function(data, status) {
		$window.alert("Success " + data.actionId + " (1029)");
		$scope.eventsInvited = data;
	})
	.error(function(data, status) {
		$window.alert("Error " + data + " (1030)");
	});


	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1063)");
		$location.path(url);
	};
});

// This factory keeps values shared among controllers.
app.factory("timeOutFactory", function($http){
	var timeOutFactory = {};
	var lists = {};
	var userLoggedIn = false;
	// var backendUrl = " http://localhost:8080";
	var backendUrl = "http://timeout5746.appspot.com";
	var searchText = [];

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

	timeOutFactory.adjustTagsForRest = function(selectedTags) {
		toBeSendTags = [];
		for (var i = 0; i < selectedTags.length; i++) {
			tag = {};
			tag.tagName = selectedTags[i].searchString;
			tag.contextId = selectedTags[i].originalObject.id;
			tag.url = selectedTags[i].originalObject.url;
			if (selectedTags[i].originalObject.aliases != null) {
				tag.alias = selectedTags[i].originalObject.aliases;
			}
			tag.description = selectedTags[i].originalObject.description;
			tag.label = selectedTags[i].originalObject.label;
			toBeSendTags.push(tag);
		};
		return toBeSendTags;
	};

	return timeOutFactory;
});

// This factory gets profile and show it in the screen
app.factory('getProfile', function($http, timeOutFactory) {
	var getData = function() {
		return $http.get(timeOutFactory.getBackendUrl() + "/profile/get?sessionId=" + getCookie("sessionId")).then(function(result){
			return result.data;
		});
	};
	return { getData: getData };
});

// JS Functions
// For setting a cookie on the browser
function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	d.setDate(d.getDate() + exdays);
	var expires = "expires=" + d;
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

// JS Functions
// For converting a string with format 25/12/2015 09:20:59 to JS Date
function convertDate(time) {
	if (time == undefined || time.length != 19) {
		console.log("Time is not valid!" + " (1064)");
		return new Date();
	}
	var d = new Date(time.substring(6,10), time.substring(3,5), time.substring(0,2), time.substring(11,13), time.substring(14,16), time.substring(17,19));
	console.log("Converted time from " + time + " to " + d + " (1065)");
	return d;
};
