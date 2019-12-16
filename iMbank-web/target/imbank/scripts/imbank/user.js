/**
 * User Angular Module
 */
'use strict';
angular.module('app.user', []).controller("usersCtrl", ["$scope", "$filter", "userSvc", "bSvc","loginSvc", "localStorageService", "$userValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, userSvc,bSvc,loginSvc, localStorageService, $userValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.classes = [];
	$scope.groups = [];
	$scope.questions = [];
	$scope.isExisting = false;
	$scope.brokerListMode = false;
	$scope.providerListMode = false;
	$scope.memberListMode = false;

	var usr= {};
	/**
	 * for displaying branches
	 */
	$scope.loadClassesList = function () {
		$scope.classes = [];
		$scope.userEditMode = false;
		$scope.userEnrollment = false;
		userSvc.GetClasses().success(function (response) {
			$scope.classes = response;
			/*angular.forEach($scope.classes, function (item, index) {
				if (item.classId == 4) {
					$scope.classes.splice(index, 1);
				}
			});*/

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}

	/**
	 * for displaying groups data
	 */
	$scope.loadGroupList = function () {
		$scope.groups = [];
		userSvc.GetGroups($scope).success(function (response) {
			$scope.groups = response;

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	/**
	 * for displaying login types
	 */
	$scope.loadLoginTypes = function(){
		$scope.loginTypes = [];
		userSvc.getLoginTypes().success(function (response) {
			$scope.loginTypes = response;
		}).error(function (data,status,headers,config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
		});
	};



	$scope.$watch("classSelect", function (newValue, oldValue) {
		if ($userValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.coverCategoryEditMode = false;
				if (newValue != 4) {
					$scope.memberListMode = false;
					$scope.linkId = "-1";
					$scope.loadGroupList();
					$scope.loadUserData();
				}
			}
		}

	});

	$scope.$watch("loginTypeSelect", function (newValue, oldValue) {
		if ($userValid(newValue)) {
			if (newValue != oldValue) {
//				console.log("NewValue>>"+newValue)
//				console.log("OldValue>>"+oldValue)
			}
		}
	});
	/**
	 * for displaying all users data
	 */
	$scope.loadUserData = function () {
		$scope.users = [],
		$scope.searchKeywords = "", $scope.filteredUsers = [], $scope.row = "", $scope.userEditMode = false,$scope.userEnrollment=false;
		userSvc.users().success(function (response) {
			return $scope.users = response, $scope.searchKeywords = "", $scope.filteredUsers = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUsers = $scope.filteredUsers.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredUsers = $filter("filter")($scope.users, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUsers = $filter("orderBy")($scope.users, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUsers = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	$scope.loadUserData();

	//Assign Branch to user
	$scope.loadBranchData = function () {
		$scope.branches = [];

		bSvc.getbranch($scope).success(function (response) {
			$scope.branches = response;
			//console.log("user branch "+response);
		})
	}
	$scope.loadBranchData();

	/* $scope.selectBranch = function (index) {
        // If any entity is not checked, then uncheck the "allItemsSelected" checkbox
    	$scope.selection=[];
        for (var i = 0; i < $scope.branches.length; i++) {
        	// var idx = $scope.selection.indexOf(branchId);

        	   if (index > -1 && $scope.branches[i].isChecked) {
        		   $scope.selection.push(
     	        		  {"branchId": $scope.branches[i].branchId
     	        			  });


        	   }
        	  else if(!$scope.branches[i].isChecked){

        		  $scope.selection.splice(i, 1);

        	  }

            if (!$scope.branches[i].isChecked) {
                $scope.allItemsSelected = false;
                return;
            }
        }

        //If not the check the "allItemsSelected" checkbox
        $scope.allItemsSelected = true;
    };

    // This executes when checkbox in table header is checked
   $scope.selectAllBranch = function () {
    	// Loop through all the entities and set their isChecked property
    	$scope.selection=[];
    	for (var i = 0; i < $scope.branches.length; i++) {
    		$scope.branches[i].isChecked = $scope.allItemsSelected;
    		if ($scope.branches[i].isChecked) {
    			$scope.selection.push(
    					{"branchId": $scope.branches[i].branchId
    					});


    		}
    		else if(!$scope.branches[i].isChecked){

    			$scope.selection.splice(i, 1);

    		}

    	}
    };*/
	/* if($rootScope.UsrRghts.userClassId == 4) {
        $scope.classSelectMode = false;
        $scope.loadClassesList();
    }
    else if($rootScope.UsrRghts.userClassId == 1){
    	  $scope.classSelectMode = false;
          $scope.loadClassesList();

    }
    else {*/

	$scope.classSelectMode = true;
	$scope.classSelect = $rootScope.UsrRghts.userClassId;
	$scope.linkId = $rootScope.UsrRghts.linkId;
	$scope.loadGroupList();
	$scope.loadUserData();


	// }
/**
 * edit user
 */
	$scope.editUser = function (user) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the user.");
			return false;
		}


		$scope.usr= user;

		$scope.userEditMode = true;
		$scope.isExisting = true;
		$scope.userId = user.userId;
		//added
		$scope.firstName = user.firstName;
		$scope.middleName = user.middleName;
		$scope.lastName = user.lastName;
		$scope.userName = user.userName;
		$scope.userFullName = user.userFullName;
		$scope.memberSelect=user.userLinkedID;
		$scope.userPwd = user.userPwd;
		$scope.confirmPwd = user.userPwd;
		$scope.groupSelect = user.userGroupId;
		$scope.userEmail = user.userEmail;
		$scope.userPhone = parseFloat(user.userPhone);
		$scope.userSecretAns = user.userSecretAns;
		$scope.active = user.active;
		$scope.isDisabled = true;
		$scope.memberListMode = false;
		//$scope.branches=user.branchesList;
		//$scope.pfId=user.pfId;
		$scope.payrollNo=user.payrollNo;
		$scope.branchSelect = user.branchId;
		//console.log("branchSelect in edit: "+$scope.branchSelect);

		//console.log(user.userBioID);
	};
	/**
	 * for creating new user
	 */
	$scope.addUser = function () {
		/*if ($rootScope.UsrRghts.userClassId == 4) {
            if (!$userValid($scope.classSelect)) {
                logger.logWarning("Oops! you skipped selecting the Category. please select a Category and try again.")
                return false;
            }
        }*/
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create users.");
			return false;
		}

		$scope.userEditMode = true;
		$scope.isExisting = false;
		$scope.userEnrollment=false;
		$scope.userId = 0;
		$scope.firstName = "";
		$scope.middleName = "";
		$scope.lastName = "";
		$scope.userName = "";
		$scope.userFullName = "";
		$scope.userPwd = "";
		$scope.confirmPwd = "";
		$scope.groupSelect = "";
		$scope.userEmail = "";
		$scope.userPhone = "";
		$scope.questionSelect = "";
		$scope.userSecretAns = "";
		$scope.active = false;
		$scope.loginTypeSelect="";
		$scope.isDisabled = true;
		$scope.hideEnrollButton=true;
		//$scope.pfId="";
		$scope.payrollNo="";
		$scope.branchSelect = "";


	};
	/**
	 * cancel function
	 */
	$scope.cancelUser = function () {
		$scope.userEditMode = false;
		$scope.userEnrollment=false;
		$scope.isDisabled = false;
		$scope.active = false;

	}

	/**
	 * for creating new user
	 */
	$scope.updUser = function () {
		var user = {};
		if (!$userValid($scope.firstName)) {
			logger.logWarning("Oops! You may have skipped specifying the User's FirstName. Please try again.")
			return false;
		}
		if (!$userValid($scope.lastName)) {
			logger.logWarning("Oops! You may have skipped specifying the User's LastName. Please try again.")
			return false;
		}
		/*if (!$userValid($scope.userName)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Name. Please try again.")
			return false;
		}
		if ($scope.userName.length > 20) {
			logger.logWarning("Oops!Username should not exceed length of 20 ")
			return false;
		}*/
		/*if (!$userValid($scope.userFullName)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Full Name. Please try again.")
			return false;
		}*/

		if (!$userValid($scope.userPwd)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Password. Please try again.")
			return false;
		}

		if (!$userValid($scope.groupSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Group. Please try again.")
			return false;
		}
		if (!$userValid($scope.branchSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Branch. Please try again.")
			return false;
		}
		if (!$userValid($scope.userEmail)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Email. Please try again.")
			return false;
		}
		if (!$userValid($scope.userPhone)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Phone. Please try again.")
			return false;
		}

		if($scope.userPwd != $scope.confirmPwd){
			logger.logError("Oops! Passwords should match, Please try again.");
			return false;
		}

		if (!$userValid($scope.userId))
			user.userId = 0;
		else
			user.userId = $scope.userId;
		user.firstName = $scope.firstName;
		user.lastName = $scope.lastName;
		user.middleName = $scope.middleName;
		//user.userName = $scope.userName;
		//user.userName = $scope.firstName.charAt(0)+$scope.middleName.charAt(0)+$scope.lastName;
		user.userName = $scope.firstName+$scope.middleName+$scope.lastName;
		user.userPwd = $scope.userPwd;
		user.userGroupId = $scope.groupSelect;
		user.userEmail = $scope.userEmail;
		user.userPhone = $scope.userPhone;
		//user.questionSelect = "1";
		user.userSecretAns ="1";
		user.userFullName = $scope.userFullName;
		user.userLinkedID = $scope.linkId;
		user.active = $scope.active;
		//user.branchesList= $scope.branches;
		//user.pfId= $scope.pfId;
		user.payrollNo=$scope.payrollNo;
		user.branchId = $scope.branchSelect;
		//console.log("pwd entered: "+$scope.userPwd);
		//console.log("branchSelect: "+user.branchId);

		////
		/*   var branchObj=[];
         $('#brtable td').each(function() {
        var branchChecked = $(this).find('.allowed');
             if (branchChecked.is(':checked')) {
                branchObj.push(branchChecked[0].id);
                console.log("branchObj>>"+branchChecked[0].id);
                     }
                     });*/


		user.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		userSvc.UpdUser(user).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The user information was saved successfully")
				if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}


				//console.log("UserId>>"+user.userId);
				if(user.userId ==0){
					$scope.userEditMode = false;
					$scope.userEnrollment=false;
					$scope.userEnrollment=true;
				} else{
					$scope.userId = 0;
					$scope.firstName = "";
					$scope.middleName = "";
					$scope.lastName = "";
					$scope.userName = "";
					$scope.userFullName = "";
					$scope.userPwd = "";
					$scope.groupSelect = "";
					$scope.branchSelect = "";
					$scope.userEmail = "";
					$scope.userPhone = "";
					$scope.questionSelect = "";
					$scope.userSecretAns = "";
					$scope.active = false;
					$scope.userEditMode = false;
					$scope.userEnrollment=false;
					$scope.loginTypeSelect="";

				}

				$scope.loadUserData();
			} else {
				logger.logError("Oops!!"+response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};

} ]).factory('userSvc', function ($http) {
	var imUserSvc = {};
	imUserSvc.GetUsers = function ($scope) {
		return $http({
			url: '/imbank/rest/user/gtUsers/'+ $scope.linkId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	imUserSvc.users = function () {
		return $http({
			url: '/imbank/rest/user/gtUsers',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imUserSvc.getLoginTypes= function () {
		return $http({
			url:'/imbank/rest/user/getlogintypes',
			method: 'GET',
			headers:{'Content-Type':'application/json'}
		});
	};

	imUserSvc.GetClasses = function () {
		return $http({
			url: '/imbank/rest/user/gtClasses/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};


	imUserSvc.GetGroups = function ($scope) {
		return $http({
			url: '/imbank/rest/userGroups/gtGroupsActive/' + $scope.linkId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	imUserSvc.GetMemList = function (classId) {
		return $http({
			url: '/imbank/rest/user/gtMemList/'+classId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imUserSvc.UpdUser = function (user) {
		return $http({
			url: '/imbank/rest/user/updUser',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: user
		});
	};
	return imUserSvc;
}).factory('$userValid', function () {
	return function (valData) {
		if (angular.isUndefined(valData))
			return false;
		else {
			if (valData == null)
				return false;
			else {
				if (valData.toString().trim() == "")
					return false;
				else
					return true;
			}
		}
		return false;
	};
});


