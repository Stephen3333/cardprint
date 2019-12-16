/**
 * Login Angular Module
 */


var afisIp = '10.1.111.169';
var afisPort = '8192';



'use strict';
angular.module('app.login', []).controller('loginCtrl', function($scope, loginSvc, $location, $rootScope, blockUI, logger, localStorageService, $loginValidation,$timeout) {

	$scope.UserName = "";
	$scope.Password = "";
	$scope.UserId = 0;
//	$scope.isValidUser= false;
	$scope.tgBio = false;
	$scope.resetPass = false;
	$scope.brSelectMode = false;
	$scope.showForgotpasswordLink = false;
	$scope.showLoginLink=true;
	$rootScope.UsrRghts = [];

	$scope.loadBranchList = function () {
		$scope.branches = [];
		loginSvc.GetBranches($scope.userId).success(function (response) {
			$scope.branches = response;
			$scope.brSelectMode = true;

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	/*
	 * for login with user name and password
	 */
	$scope.Login = function() {
		if (!$loginValidation($scope.UserName)) {
			logger.logWarning("Oops! You may have skipped entering your user name. Please try again.")
			return false;
		}
		if (!$loginValidation($scope.Password)) {
			logger.logWarning("Oops! You may have skipped entering your password. Please try again.")
			return false;
		}
		blockUI.start();
		localStorageService.clearAll();
		var loginUser = new Object();
		loginUser.userName = $scope.UserName;
		loginUser.userPwd = $scope.Password;
		//$scope.userName = UserName;
		//$scope.password = Password;
		loginSvc.AuthManual(loginUser).success(function(response) {
			if (response.respCode == 203) {
				logger.logSuccess("Info: "+"Oops! Please reset your password");
				$scope.resetPass = true;
				//$scope.tgBio = false;
				$scope.userId = response.respMessage;

			} else if(response.respCode == 200){
				loginSvc.GetRights(response.userId).success(function(rightLst) {
					localStorageService.set('rxr', rightLst);
					$rootScope.UsrRghts = rightLst;
					$scope.userId = response.userId;
					localStorageService.set('brId', $scope.branchId);
					//  $scope.loadBranchList();
					$location.path('/dashboard');
				}).error(function(data, status, headers, config) {
					logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
				});
			} else if(response.respCode == 204){
				loginSvc.GetRights(response.userId).success(function(rightLst) {
					localStorageService.set('rxr', rightLst);
					$rootScope.UsrRghts = rightLst;
					$scope.userId = response.userId;
					localStorageService.set('brId', $scope.branchId);
					//  $scope.loadBranchList();
					$location.path('/dashboard');
				    logger.logSuccess("Info: "+"Your password is going to be expired please change your password");
				}).error(function(data, status, headers, config) {
					logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
				});
			}
			else{
				logger.logError("Error: "+response.respCode+" "+response.respMessage);
			}
			blockUI.stop();
		}).error(function(data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};
	$scope.setUserBranch=function()
	{
		$location.path('/dashboard');
	}
	//006195
	$scope.showForgotpassword=function(){
		$scope.UserName="";
		$scope.showForgotpasswordLink = true;
		$scope.tgBio = false;
		$scope.showLoginLink=false;
	}
	$scope.showLogin=function(){
		$scope.UserName="";
		$scope.showForgotpasswordLink = false;
		$scope.tgBio = false;
		$scope.showLoginLink=true;
	}
	
	/**
	 * forgot password function
	 */
	$scope.forgotPassword=function(UserName)
	{
		$scope.UserName="";
		if(!$loginValidation(UserName))
		{
			logger.logError("You may have skipped To Specify the User Name");
			return false;
		}
		blockUI.start();

		loginSvc.ForgotPassword(UserName).success(function(response) {
			if(response.respCode == 200){

				logger.logSuccess("Check Your mail For the Password!!")
				$scope.tgBio = false
				$scope.showForgotpasswordLink = false;
				$scope.showLoginLink=true;
				blockUI.stop();
			} else{
				logger.logError("Error: "+response.respMessage);
				blockUI.stop();
			}

		}).error(function(data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	}

	/**
	 * reset password function
	 */
	$scope.resetPassword = function () {
		//logger.logWarning("Reset Password :)");
		if (!$loginValidation($scope.password)) {
			logger.logError("Oops! You may have skipped specifying the Password. Please try again.")
			return false;
		}

		if (!$loginValidation($scope.cpassword)) {
			logger.logError("Oops! You may have skipped confirming the Password. Please try again.")
			return false;
		}

		if($scope.password != $scope.cpassword){
			logger.logError("Oops! Passwords should match, Please try again.");
			return false;
		}


		if (!$loginValidation($scope.userId)) {
			logger.logError("UserID Error! Please try again");
			return false;
		}

		var usr = new Object();
		usr.userId = $scope.userId;
		usr.userPwd = $scope.password;

		blockUI.start();
		loginSvc.resetPassword(usr).success(function(response) {
			if(response.respCode == 200){

				logger.logSuccess("Password reset Successfully!!")
				$scope.userId = "";
				$scope.resetPass = false;
			} else{
				logger.logError("Error: "+response.respMessage);
			}
			blockUI.stop();
		}).error(function(data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});

	};



}).factory('$loginValidation', function() {
	return function(valData) {
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
}).factory('loginSvc', function($http) {
	var imLoginSvc = {};

	imLoginSvc.AuthManual = function(loginUser) {
		//console.log(loginUser);
		return $http({
			url : '/imbank/rest/login/manualAuth',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : loginUser

		});
	};
	imLoginSvc.ForgotPassword = function(loginUser) {
		//console.log(loginUser);
		return $http({
			url : '/imbank/rest/login/forgotpwd',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : loginUser

		});
	};


	imLoginSvc.resetPassword = function(usr) {
		return $http({
			url : '/imbank/rest/login/resetPassword',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : usr

		});
	};


	imLoginSvc.GetRights = function(userId) {
		return $http({
			url : '/imbank/rest/login/getUserRights/' + userId,
			method : 'GET',
			headers : {
				'Content-Type' : 'application/json'
			}
		});
	};


	imLoginSvc.GetBranches = function(userId) {
		return $http({
			url : '/imbank/rest/login/getUserBranches/' + userId,
			method : 'GET',
			headers : {
				'Content-Type' : 'application/json'
			}
		});
	};

	return imLoginSvc;
});
