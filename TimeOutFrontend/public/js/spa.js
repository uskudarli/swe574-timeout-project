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
	.when("/thePost/:postId", {
		templateUrl: "thePost.html",
		controller: "thePost"
	})
	.when("/theUser/:userId", {
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
app.run(function($rootScope, $location) {
	$rootScope.$on( "$routeChangeStart", function(event, next, current) {
		if ((getCookie("sessionId") == undefined || getCookie("sessionId") == "")) {
			$location.path("/");
		}
	});
});

// Non-dynamic content is controlled by this controller.
app.controller("indexController", function($scope, $http, $location, $window, timeOutFactory, md5, getProfile, $interval, $route) {
	$scope.friendRequests = [];
	$scope.searchTextField = "";
	$scope.profileInfo = null;
	$scope.recommendedUsers = null;
	$scope.recommendedEvents = null;
	$scope.recommendedGroups = null;
	$scope.userName = "";
	timeOutFactory.setRecommendationUpdated(true);

	// Gets profile and keeps it in the scope
	$scope.getProfile = function() {
		if(getCookie("sessionId") != undefined && getCookie("sessionId") != "") {
			var getProfilePromise = getProfile.getData();
		    getProfilePromise.then(function(result) {  // this is only run after $http completes
		       	$scope.profileInfo = result;
		       	if(result.userBasicInfo == null || result.userBasicInfo.firstName == null){
		       		$scope.userName = result.userEmail;
		       	} else {
		       		$scope.userName = result.userBasicInfo.firstName + " " + result.userBasicInfo.lastName;
		       	}
		       $scope.friendRequests = [];
		       // Friend notification
				var paramsNotification = "?sessionId=" + getCookie("sessionId");
				$http.get(timeOutFactory.getBackendUrl() + "/friends/my" + paramsNotification)
					.success(function(data, status) {
						for (var i = 0; i < data.length; i++) {
							if(data[i].status == "IO"){
								$scope.friendRequests.push(data[i]);
							}
						};
					})
					.error(function(data, status) {
						console.log("Cloud was busy, please try again! (1611)");
					});
		    });
		}
	};

	$scope.getProfile();

	$interval(function() {
		if (getCookie("sessionId") != undefined && getCookie("sessionId") != "" && timeOutFactory.getRecommendationUpdated()){
			// Reset the flag
			timeOutFactory.setRecommendationUpdated(false);

			// Get event recommendation for current user
			$http.get(timeOutFactory.getBackendUrl() + '/getEventRecommendation?sessionId=' + getCookie("sessionId"))
				.success(function(data, status) {
					if(data.length > 0) {
						$scope.recommendedEvents = data;
					}
			  	})
			  	.error(function(data, status) {
			 		console.log("Cloud was busy, please try again! (1037)");
			  	});

			// Get group recommendation for current user
			$http.get(timeOutFactory.getBackendUrl() + '/getUserRecommendation?sessionId=' + getCookie("sessionId"))
				.success(function(data, status) {
					if(data.length > 0) {
						$scope.recommendedUsers = data;
					}
			  	})
			  	.error(function(data, status) {
			 		console.log("Cloud was busy, please try again! (1076)");
			  	});

			// Get user recommendation for current user
			$http.get(timeOutFactory.getBackendUrl() + '/getGroupRecommendation?sessionId=' + getCookie("sessionId"))
				.success(function(data, status) {
					if(data.length > 0) {
						$scope.recommendedGroups = data;
					}
			  	})
			  	.error(function(data, status) {
			 		console.log("Cloud was busy, please try again! (1075)");
			  	});
		}
	}, 20000);

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
				$location.path("/home");
			} else {
				$window.alert(data.type + ": " + data.message + " (1001)");
			}
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1002)");
		});
	};

	// Set sessionId cookies to ""
	$scope.doLogout = function() {
		setCookie("sessionId", "", -1);
		$scope.userEmail = "";
		$scope.userName = "";
		$scope.recommendedUsers = [];
		$scope.recommendedEvents = [];
		$scope.recommendedGroups = [];
		$location.path("/");
	};

	// In order to decide whether showing search or login on the right top corner,
	// check if the cookie is set or not
	$scope.isCookieSet = function() {
		return getCookie("sessionId") != undefined && getCookie("sessionId") != "";
	};

	// To change dynamic content when a link is pressed, $location.path method is used.
	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1040)");
		$location.path(url);
	};

	// When a search is being done, search string is saved to the factory to be reached from other controllers.
	$scope.search = function(url){
		timeOutFactory.setSearchText($scope.searchTextField);
		console.log("SearchText= " + $scope.searchTextField + " (1200)");
		$scope.searchTextField = "";
		if($route.current.templateUrl == "search.html") {
			$route.reload();
			console.log("(1700)");
		} else {
			$scope.goToPage(url);
			console.log("(1701)");
		}
	};

	// When a user accepts a friend invitation
	$scope.acceptFriend = function(friendName, friendEmail, friendshipId){
		var friendshipIds = [];
		friendshipIds.push(friendshipId);
		var paramAccept = "?sessionId=" + getCookie("sessionId") + "&friendshipIds=" + JSON.stringify(friendshipIds);
		var friendNameDisplay = "";
		$http.get(timeOutFactory.getBackendUrl() + "/friends/accept" + paramAccept)
			.success(function(data, status) {
				if(friendName != null && friendName != ""){
					friendNameDisplay = friendName;
				} else {
					friendNameDisplay = friendEmail;
				}
				var friendRequestsTemp = [];
				for (var i = 0; i < $scope.friendRequests; i++) {
					if($scope.friendRequests[i].friendshipId != friendshipId){
						friendRequestsTemp.push($scope.friendRequests[i]);
					}
				};
				$scope.friendRequests = friendRequestsTemp;
				$window.alert(friendNameDisplay + " is added to your friend list (1008)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1008)");
			});
	}

	// When a user accepts a friend invitation
	$scope.rejectFriend = function(friendName, friendEmail, friendshipId){
		var friendshipIds = [];
		friendshipIds.push(friendshipId);
		var paramAccept = "?sessionId=" + getCookie("sessionId") + "&friendshipIds=" + JSON.stringify(friendshipIds);
		var friendNameDisplay = "";
		$http.get(timeOutFactory.getBackendUrl() + "/friends/reject" + paramAccept)
			.success(function(data, status) {
				if(friendName != null && friendName != ""){
					friendNameDisplay = friendName;
				} else {
					friendNameDisplay = friendEmail;
				}
				$window.alert(friendNameDisplay + "\'s invitation is rejected (1008)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1008)");
			});
	}
});

// SignUp screen or root address shows the html which is managed by this controller.
app.controller("mainController", function($scope, $http, $location, $window, timeOutFactory, md5) {
	if(getCookie("sessionId") != undefined && getCookie("sessionId") != "") {
		console.log("GoToPage: /home" + " (1041)");
		$location.path("/home");
	}

	$scope.roleSet = ["student", "prep. student", "alumni", "erasmus & exchange","professor", "staff"];

	// This method will be used when a user tries to be a member of the system.
	$scope.signUp = function() {
		// Check if specified information is OK
		// validate();

		// validate if email is unique
		$http.get(timeOutFactory.getBackendUrl() + "/email/isAvailable?userEmail=" + $scope.email)
			.success(function(data){
				if (data.type == "Success") {
					// Parameters for register is adjusted and password is encrypted with MD5 hash
					var params = "?userEmail=" + $scope.email + "&password=" + md5.createHash($scope.sigUpPassword);
					params = params + "&firstName=" + $scope.firstName + "&lastName=" + $scope.lastName + "&role=" + $scope.role;

					// register api of backend is called
					$http.get(timeOutFactory.getBackendUrl() + "/register" + params)
						// if register api call is successful
						.success(function(data, status) {
							if(data.type == "Success") {
								$scope.firstName = "";
								$scope.lastName = "";
								$scope.email = "";
								$scope.reEmail = "";
								$scope.sigUpPassword = "";
								$scope.rePassword = "";
								$window.alert("Welcome among us, you can now log in!" + " (1033)");
							} else {
								$window.alert(data.message + " (1033)");
							}
						})
						// if register api call is unsuccessful
						.error(function(data, status) {
							$window.alert("Cloud was busy, please try again! (1005)");
						});
				} else {
					$window.alert(data.message + " (1034)");
					return;
				}
			})
			.error(function(data){
				$window.alert("Cloud was busy, please try again! (1003)");
			});
	};
});

// After user logged in, home.html will be seen which managed by this controller
app.controller("homeController", function($scope, $http, $window, $location, timeOutFactory, $interval) {
	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1042)");
		$location.path(url);
	};

	// News Feed is taken from backend
	var params = "?sessionId=" + getCookie("sessionId");
	$http.get(timeOutFactory.getBackendUrl() + "/newsFeed" + params)
		.success(function(data) {
			$scope.newsFeed = data;
		})
		.error(function(data) {
			$window.alert("Cloud was busy, please try again! (1551)");
		});

	// It triggers recommendation machine on server for this user
	$interval(function() {
		if (getCookie("sessionId") != undefined && getCookie("sessionId") != ""){
			// Trigger backend to run insert recommendation
			$http.get(timeOutFactory.getBackendUrl() + "/insertRecommendation?sessionId=" + getCookie("sessionId"))
				.success(function(data, status) {
					if(data.type != "Fail") {
						timeOutFactory.setRecommendationUpdated(true);
					}
				});
		}
	}, 1200000); // 20 minutes
});

// When a search request is done, search.html and this controller shows the dynamic content.
app.controller("searchController", function($scope, $http, $location, $window, timeOutFactory) {
	$scope.searchContextUrl = timeOutFactory.getBackendUrl() + '/searchContext?tag=';
	$scope.searchTag = [];

	var search = timeOutFactory.getSearchText();
	timeOutFactory.setSearchText("");
	if(search != "") {
		var params = "?keyword=" + search;
		$http.get(timeOutFactory.getBackendUrl() + "/find" + params)
			.success(function(data, status) {
				$scope.resultSet = data;
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1007)");
			});
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1046)");
		$location.path(url);
	};

	$scope.searchByTag = function() {
		var params = "?contextId=" + $scope.searchTag[0].originalObject.id;
		$http.get(timeOutFactory.getBackendUrl() + "/find" + params)
			.success(function(data, status) {
				$scope.resultSet = data;
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1777)");
			});
	}

	$scope.addFriend = function(friendId){
		var otherUserId = [];
		otherUserId.push(friendId);

		var inviteParams = 	"?sessionId=" + getCookie("sessionId") +
							"&userIds=" + JSON.stringify(otherUserId);
		$http.get(timeOutFactory.getBackendUrl() + "/friends/invite" + inviteParams)
			.success(function(data, status) {
				$window.alert("Your friend request has been sent (1888)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1611)");
			});
	}

	$scope.joinGroup = function(actionId){
		var groupIds = [];
		groupIds.push(actionId);

		var paramsJoin = 	"?sessionId=" + getCookie("sessionId") +
							"&actionIds=" + JSON.stringify($scope.groupIds);

		$http.get(timeOutFactory.getBackendUrl() + "/group/acceptInvitation" + paramsJoin)
			.success(function(data, status) {
				$window.alert("You have joined to the group! (1889)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1559)");
			});
	}

	$scope.joinEvent = function(actionId){
		var eventIds = [];
		eventIds.push(actionId);

		var paramsJoin = 	"?sessionId=" + getCookie("sessionId") +
							"&actionIds=" + JSON.stringify($scope.eventIds);

		$http.get(timeOutFactory.getBackendUrl() + "/event/acceptInvitation" + paramsJoin)
			.success(function(data, status) {
				$window.alert("You have joined to the event! (1900)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1559)");
			});
	}
});

// When user wants to edit own profile, this controller works to support the html on the dynamic content.
app.controller("profileEdit", function($scope, $http, $window, $location, timeOutFactory) {

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/profile/get" + params)
		.success(function(data, status) {
			$scope.email = data.userEmail;
			$scope.password = data.password;
			$scope.firstName = data.userBasicInfo.firstName;
			$scope.lastName = data.userBasicInfo.lastName;
			$scope.gsm = data.userCommInfo.mobilePhone;
			$scope.address = data.userCommInfo.address;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1008)");
		});

	// $("#imgInp").change(function(){
	// 	readURL(this);
	// });

	// $scope.data = 'none';
	// $scope.add = function(){
	// 	var f = document.getElementById('photo').files[0];
	// 	r = new FileReader();
	// 	r.onloadend = function(e){
	// 		$scope.data = e.target.result;
	// 	}
	// 	r.readAsBinaryString(f);
	// };

	$scope.genderOption = ['male', 'female', 'other'];
	$scope.selectedLanguage = ['Turkish','English', 'French', 'German', 'Italian', 'Spanish'];

	$scope.profileEdit = function(){
		var paramsEdit = "?sessionId=" + getCookie("sessionId");
		if($scope.firstName != undefined && $scope.firstName != ""){
			paramsEdit += 	"&firstName=" + $scope.firstName;
		}
		if($scope.lastName != undefined && $scope.lastName != ""){
			paramsEdit += 	"&lastName=" + $scope.lastName;
		}
		if($scope.Gsm != undefined && $scope.Gsm != ""){
			paramsEdit += 	"&Gsm=" + $scope.Gsm;
		}
		if($scope.address != undefined && $scope.address != ""){
			paramsEdit += 	"&address=" + $scope.address;
		}
		if($scope.birthdate != undefined && $scope.birthdate != ""){
			paramsEdit += 	"&birthdate=" + $scope.birthdate;
		}
		if($scope.about != undefined && $scope.about != ""){
			paramsEdit += 	"&about=" + $scope.about;
		}
		if($scope.interests != undefined && $scope.interests != ""){
			paramsEdit += 	"&interests=" + $scope.interests;
		}
		if($scope.gender != undefined && $scope.gender != ""){
			paramsEdit += 	"&gender=" + $scope.gender;
		}
		if($scope.languages != undefined && $scope.languages != ""){
			paramsEdit += 	"&languages=" + $scope.languages;
		}

		$http.get(timeOutFactory.getBackendUrl() + "/profile/edit" + paramsEdit)
			.success(function(data, status) {
				$window.alert("It has been succesfully done (1009)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1010)");
			});
	};

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1046)");
		$location.path(url);
	};
});

// This page is used to create new event
app.controller("createEvent", function($scope, $http, $window, $location, $filter, timeOutFactory) {
	$scope.friendsToInvite = [];

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
						"&endTime=" + $scope.endTime;
			if($scope.invitedPeople != undefined && $scope.invitedPeople != ""){
				params +=	"&invitedPeople=" + $scope.invitedPeople;
			}
			if($scope.selectedTags != undefined && $scope.selectedTags.length > 0){
				params +=	"&tag=" + JSON.stringify(timeOutFactory.adjustTagsForRest($scope.selectedTags));
			}
			if($scope.selectedTags != undefined && $scope.selectedTags.length > 0){
				params +=	"&privacy=" + ($scope.privacy == true ? "P" : "C");
			}

			$http.get(timeOutFactory.getBackendUrl() + "/event/create" + params)
				.success(function(data, status) {
					$window.alert("Your event created succesfully (1013)");
					$scope.eventName = "";
					$scope.eventDescription = "";
					$scope.invitedPeople = "";
					$scope.selectedTags = [];
					$scope.location = "";
				})
				.error(function(data, status) {
					$window.alert("Cloud was busy, please try again! (1014)");
				});
		}
	};
});

// This page is used to create new group
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
		if ($scope.groupName == undefined || $scope.groupName == "" ||
			$scope.groupDescription == undefined || $scope.groupDescription == "") {
			$window.alert("Please fill required fields which have *!!!" + " (1012)");
		} else {
			var params = "?sessionId=" + getCookie("sessionId");
			params += 	"&groupName=" + $scope.groupName +
						"&groupDescription=" + $scope.groupDescription;
			if($scope.selectedTags != undefined && $scope.selectedTags.length > 0){
				params +=	"&tag=" + JSON.stringify(timeOutFactory.adjustTagsForRest($scope.selectedTags));
			}
			if($scope.invitedPeople != undefined && $scope.invitedPeople != ""){
				params +=	"&invitedPeople=" + $scope.invitedPeople;
			}
			if($scope.privacy != undefined && $scope.privacy != ""){
				params +=	"&privacy=" + ($scope.privacy == true ? "P" : "C");
			}

			$http.get(timeOutFactory.getBackendUrl() + "/group/create" + params)
				.success(function(data, status) {
					$window.alert("Success (1015)");
				})
				.error(function(data, status) {
					$window.alert("Cloud was busy, please try again! (1016)");
				});
		}
	};
});

// This page is used to show user their own friends
app.controller("myFriends", function($scope, $http, $window, $location, timeOutFactory) {
	$scope.pendingRequests = [];
	$scope.myFriends = [];
	var params = "?sessionId=" + getCookie("sessionId");
	$http.get(timeOutFactory.getBackendUrl() + "/friends/my" + params)
		.success(function(data, status) {
			for (var i = 0; i < data.length; i++) {
				if(data[i].status == "IS"){
					$scope.pendingRequests.push(data[i]);
				} else if(data[i].status == "AS" || data[i].status == "AO"){
					$scope.myFriends.push(data[i]);
				}
			};
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1611)");
		});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1049)");
		$location.path(url);
	};
});

// This page is used to show user newest events in the system
app.controller("newEvents", function($scope, $http, $window, $location, timeOutFactory) {
	$scope.answerTaken = false;

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/new" + params)
		.success(function(data, status) {
			$scope.newEvents = data;
			$scope.answerTaken = true;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1017)");
		});

	$scope.joinEvent = function(actionId){
		var eventIds = [];
		eventIds.push(actionId);

		var paramsJoin = 	"?sessionId=" + getCookie("sessionId") +
							"&actionIds=" + JSON.stringify($scope.eventIds);

		$http.get(timeOutFactory.getBackendUrl() + "/event/acceptInvitation" + paramsJoin)
			.success(function(data, status) {
				$window.alert("You have joined to the event! (1990)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1559)");
			});
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1070)");
		$location.path(url);
	};
});

// This page is used to show event invitations which the user has been invited
app.controller("eventInvitation", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1071)");
		$location.path(url);
	};
});

// This page is used to show group invitations which the user has been invited
app.controller("groupInvitation", function($scope, $http, $window, $location) {

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1072)");
		$location.path(url);
	};
});

// This page is used to show recommended events to user
app.controller("suggestedEvents", function($scope, $http, $window, $location, timeOutFactory) {
	$scope.answerTaken = false;

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/getEventRecommendation" + params)
		.success(function(data, status) {
			$scope.suggestedEvents = data;
			$scope.answerTaken = true;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1017)");
		});

	$scope.joinEvent = function(actionId){
		var eventIds = [];
		eventIds.push(actionId);

		var paramsJoin = 	"?sessionId=" + getCookie("sessionId") +
							"&actionIds=" + JSON.stringify($scope.eventIds);

		$http.get(timeOutFactory.getBackendUrl() + "/event/acceptInvitation" + paramsJoin)
			.success(function(data, status) {
				$window.alert("You have joined to the event! (1991)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1559)");
			});
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1073)");
		$location.path(url);
	};
});

// This page is used to show user's own events
app.controller("myEvents", function($scope, $http, $window, $location, timeOutFactory){
	$scope.answerTaken = false;

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/my" + params)
		.success(function(data, status) {
			$scope.myEvents = data;
			$scope.answerTaken = true;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1017)");
		});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1050)");
		$location.path(url);
	};
});

// This page is used to show events which the user is invited
app.controller("eventsInvited", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/event/invited" + params)
	.success(function(data, status) {
		$window.alert("Success (1019)");
		$scope.eventsInvited = data;
		console.log(data + " (1052)");
	})
	.error(function(data, status) {
		$window.alert("Cloud was busy, please try again! (1020)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1053)");
		$location.path(url);
	};
});

// This page is used to show groups the user joined
app.controller("myGroups", function($scope, $http, $window, $location, timeOutFactory){
	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/my" + params)
	.success(function(data, status) {
		console.log("Success " + " (1021)");
		$scope.myGroups = data;
	})
	.error(function(data, status) {
		$window.alert("Cloud was busy, please try again! (1022)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1054)");
		$location.path(url);
	};
});

// This page is used to show newest groups in the system
app.controller("newGroups", function($scope, $http, $window, $location, timeOutFactory){
	$scope.answerTaken = false;
	$scope.newGroups = [];

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/group/new" + params)
		.success(function(data, status) {
			for (var i = 0; i < data.length; i++) {
				if(data[i].action.actionType == "E") {
					continue;
				} else {
					$scope.newGroups.push(data[i]);
				}
			};
			$scope.answerTaken = true;
			console.log("Success (1023)");
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1024)");
		});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1055)");
		$location.path(url);
	};
});

// This page is used to show selected groups' detail
app.controller("theGroup", function($scope, $http, $window, $location, $routeParams, timeOutFactory){
	$scope.groupId = $routeParams.groupId;

	var params = 	"?sessionId=" + getCookie("sessionId") +
					"&id=" + $scope.groupId;

	$http.get(timeOutFactory.getBackendUrl() + "/group/getById" + params)
		.success(function(data, status) {
			$scope.groupData = data;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1559)");
		});

	$scope.addComment = function() {
		$scope.comment;
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1056)");
		$location.path(url);
	};

	$scope.joinGroup = function() {
		var groupIds = [];
		groupIds.push($scope.groupId);

		var paramsJoin = 	"?sessionId=" + getCookie("sessionId") +
							"&actionIds=" + JSON.stringify($scope.groupIds);

		$http.get(timeOutFactory.getBackendUrl() + "/group/acceptInvitation" + paramsJoin)
			.success(function(data, status) {
				$window.alert("You have joined to the group! (1800)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1559)");
			});
	}
});

// This page is used to show selected post's detail
app.controller("thePost", function($scope, $http, $window, $location, $routeParams, timeOutFactory){
	$scope.postId = $routeParams.postId;

	var params = 	"?sessionId=" + getCookie("sessionId") +
					"&postId=" + $scope.postId;

	$http.get(timeOutFactory.getBackendUrl() + "/post/getById" + params)
		.success(function(data, status) {
			$scope.postData = data;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1557)");
		});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1057)");
		$location.path(url);
	};
});

// This page is used to show selected user's detail
app.controller("theUser", function($scope, $http, $window, $location, $routeParams, timeOutFactory){
	$scope.otherUserId = [];
	$scope.otherUserId.push($routeParams.userId);
	console.log("User Id = " + $scope.otherUserId + " route params " + $routeParams.userId + " (1588)");

	var params = 	"?sessionId=" + getCookie("sessionId") +
					"&userId=" + $scope.otherUserId;

	$http.get(timeOutFactory.getBackendUrl() + "/profile/getById" + params)
		.success(function(data, status) {
			$scope.otherUserData = data;
		})
		.error(function(data, status) {
			$window.alert("Cloud was busy, please try again! (1558)");
		});

	$scope.invite = function(){
		var inviteParams = 	"?sessionId=" + getCookie("sessionId") +
							"&userIds=" + JSON.stringify($scope.otherUserId);
		$http.get(timeOutFactory.getBackendUrl() + "/friends/invite" + inviteParams)
			.success(function(data, status) {
				console.log("Success (1610)");
			})
			.error(function(data, status) {
				$window.alert("Cloud was busy, please try again! (1611)");
			});
	}

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1058)");
		$location.path(url);
	};
});

// This page is used to update group information
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
			  	$window.alert("Cloud was busy, please try again! (1036)");
			  });
		}
});

// This page is used to update event information
app.controller("updateEvent", function($scope, $http, $window, $location, timeOutFactory){
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
		  	$window.alert("Cloud was busy, please try again! (1036)");
		  });
	}
});

// This page is used to show recommended groups by the system
app.controller("suggestedGroups", function($scope, $http, $window, $location, timeOutFactory){
	$scope.answerTaken = false;
	$scope.suggestedGroups = [];

	var params = "?sessionId=" + getCookie("sessionId");

	$http.get(timeOutFactory.getBackendUrl() + "/getGroupRecommendation" + params)
	.success(function(data, status) {
		$scope.suggestedGroups = data;
		$scope.answerTaken = true;
	})
	.error(function(data, status) {
		$window.alert("Cloud was busy, please try again! (1028)");
	});

	$scope.goToPage = function(url) {
		console.log("GoToPage: " + url + " (1062)");
		$location.path(url);
	};
});

// This factory keeps values shared among controllers.
app.factory("timeOutFactory", function($http){
	var timeOutFactory = {};
	var lists = {};
	// var backendUrl = " http://localhost:8080";
	var backendUrl = "http://timeout5742.appspot.com";
	var searchText = "";
	var recommendationUpdated = false;

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
				tag.alias = selectedTags[i].originalObject.aliases[0];
			}
			tag.description = selectedTags[i].originalObject.description;
			tag.label = selectedTags[i].originalObject.label;
			toBeSendTags.push(tag);
		};
		return toBeSendTags;
	};

	timeOutFactory.getRecommendationUpdated = function() {
		return recommendationUpdated;
	}

	timeOutFactory.setRecommendationUpdated = function(recom) {
		recommendationUpdated = recom;
	}

	return timeOutFactory;
});

// This factory gets profile information of logged user
app.factory('getProfile', function($http, timeOutFactory) {
	var getData = function() {
		return $http.get(timeOutFactory.getBackendUrl() + "/profile/get?sessionId=" + getCookie("sessionId")).then(function(result){
			return result.data;
		});
	};
	return { getData: getData };
});

// To show records with ng-repeat in reverse order
app.filter('reverse', function() {
  return function(items) {
  	if(items != null){
    	return items.slice().reverse();
    } else {
    	return items;
    }
  };
});

// JS Functions
// For setting a cookie on the browser
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

// For getting the value of cookie which was set before.
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

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
}

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
