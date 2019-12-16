/**
 * User Angular Module
 */
'use strict';
angular.module('app.profile', []).controller("profileCtrl", ["$scope", "$filter", "profileSvc", "userSvc", "loginSvc", "localStorageService", "$profileValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, profileSvc, userSvc, loginSvc, localStorageService, $profileValid, $rootScope, blockUI, logger, $location) {
	$scope.groups = [];
	$scope.questions = [];
	/**
	 * for loading groups in profile
	 */
	$scope.loadGroupList = function () {
		$scope.groups = [];
		$scope.classSelect = $rootScope.UsrRghts.userClassId;
		$scope.linkId = $rootScope.UsrRghts.linkId;
		userSvc.GetGroups($scope).success(function (response) {
			blockUI.stop();
			$scope.groups = response;
		}).error(function (data, status, headers, config) {
			blockUI.stop();
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
		});
	};
	/**
	 * displaying profile data
	 */
	$scope.loadProfileDetail = function () {
		$scope.profileSave = [];
		profileSvc.GetUserById($rootScope.UsrRghts.sessionId).success(function (response) {
			blockUI.stop();
			$scope.profileSave.userId = response.userId;
			$scope.profileSave.firstName = response.firstName;
			$scope.profileSave.middleName = response.middleName;
			$scope.profileSave.lastName = response.lastName;
			$scope.profileSave.userName = response.userName;
			$scope.profileSave.userFullName = response.userFullName;
			$scope.profileSave.userPwd = response.userPwd;
			$scope.profileSave.groupSelect = response.userGroupId;
			$scope.profileSave.groupName = response.userGroupName;
			$scope.profileSave.userEmail = response.userEmail;
			$scope.profileSave.userPhone = response.userPhone;
			$scope.profileSave.linkId = response.userLinkedID;
			$scope.profileSave.active = response.active;
			//$scope.profileSave.userBioLogin= response.userBioLogin;
			//added
			$scope.profileSave.branchId = response.branchId;
			// console.log("!!!"+response.branchId);
			$scope.idDisabled = true;
		}).error(function (data, status, headers, config) {
			blockUI.stop();
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
		});
	};

	$scope.loadGroupList();

	$scope.loadProfileDetail();
	/*
	 * cancel function
	 */
	$scope.cancelProfile = function () {
		$scope.isDisabled = false;
		$scope.active = false;
		$location.path('/dashboard');
	};
	/*
	 * update user
	 */
	$scope.updUser = function () {
		var profile = {};
		if (!$profileValid($scope.profileSave.userName)) {
			logger.logWarning("Oops! You may have skipped specifying the User's Name. Please try again.");
			return false;
		}
		//if (!$profileValid($scope.profileSave.userEmail)) {
		//    logger.logWarning("Opss! You may have skipped specifying the User's Email. Please try again.");
		//    return false;
		//}
		//if (!$profileValid($scope.profileSave.userPhone)) {
		//    logger.logWarning("Opss! You may have skipped specifying the User's Phone. Please try again.");
		//    return false;
		//}

		if (!$profileValid($scope.profileSave.userId))
			user.userId = 0;
		else
			profile.userId = $scope.profileSave.userId;
		profile.firstName = $scope.profileSave.firstName;
		profile.middleName = $scope.profileSave.middleName;
		profile.lastName = $scope.profileSave.lastName;
		profile.userName = $scope.profileSave.userName;
		profile.userFullName = $scope.profileSave.userFullName;
		profile.userPwd = $scope.profileSave.userPwd;
		profile.userGroupId = $scope.profileSave.groupSelect;
		profile.userEmail = $scope.profileSave.userEmail;
		profile.userPhone = $scope.profileSave.userPhone;
		profile.userLinkedID = $scope.profileSave.linkId;
		profile.active = $scope.profileSave.active;
		//profile.userBioLogin= $scope.profileSave.userBioLogin;
		profile.branchId = $scope.profileSave.branchId;
		profile.createdBy = $rootScope.UsrRghts.sessionId;
//		console.log(profile);
		blockUI.start();
		userSvc.UpdUser(profile).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The profile information was saved Successfully");
				if (parseFloat($scope.profileSave.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
						$location.path('/dashboard');
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
					});
				}
				$scope.userId = 0;
				$scope.firstName="";
				$scope.middleName="";
				$scope.lastName="";
				$scope.userName = "";
				$scope.userFullName = "";
				$scope.userPwd = "";
				$scope.groupSelect = "";
				$scope.userEmail = "";
				$scope.userPhone = "";
				//$scope.userBioLogin = "";
				$scope.branchId="";
				$scope.active = false;

				$scope.$apply(function () {
					$scope.loadProfileDetail();
				});


				/* $scope.profileSave.userId="";
                $scope.profileSave.userName ="";
                $scope.profileSave.userFullName= "";
                $scope.profileSave.userPwd ="";
                $scope.profileSave.groupSelect ="";
                $scope.profileSave.userEmail ="";
                $scope.profileSave.userPhone ="";
                $scope.profileSave.linkId ="";
                $scope.profileSave.active="";
                $scope.profileSave.userBioLogin="";
				 */



				$location.path('/dashboard');
			}
			else if( response.respCode == 201)
			{
				logger.logWarning("Password Policy Validation failed,make sure you have not used this password before and it contains alpha,numeric ,special characters & it has minimum length of 8 characters");
			}
			else {
				logger.logWarning("Opss! Something went wrong while updating the profile. Please try again after sometime");
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
			blockUI.stop();
		});
	};
} ]).factory('profileSvc', function ($http) {
	var imProfileSvc = {};
	imProfileSvc.GetUserById = function (userId) {
		return $http({
			url: '/imbank/rest/user/gtUserById/' + userId,
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		});
	};
	return imProfileSvc;
}).factory('$profileValid', function () {
	return function (valData) {
		if (angular.isUndefined(valData))
			return false;
		else {
			if (valData == null)
				return false;
			else {
				return !(valData.toString().trim() == "");
			}
		}
	};
}).filter('getGroupName', function () {
	return function (input, id) {
		var i = 0, len = input.length;
		for (; i < len; i++) {
			//console.log("Checking: " + input[i].groupId + " against: " + id);
			if (input[i].groupId == id) {
				return input[i];
			}
		}
		return null;
	}
});