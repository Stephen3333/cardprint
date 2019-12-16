/**
 * Created by SJ on 7/29/2015.
 */


angular.module('app.rptByUser', []).controller("rptByUserCtrl", ["$scope", "$filter", "$rptTransValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","userSvc", function ($scope,$filter, $rptTransValid, $rootScope, blockUI, logger, $location,$http,$window,userSvc) {
	$scope.userrpt={};
	$scope.linkId = "-1";
	$scope.brSelectMode = false;
	/**
	 * loading users data
	 */
	$scope.loadUserList = function () {
		$scope.users = [];
		userSvc.users().success(function (response) {
			$scope.users = response;
			//console.log("in get users"+$scope.users);

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	$scope.loadUserList();
	/**
	 * preview report function
	 */

	$scope.exportPdfReport = function(userrpt) {

		blockUI.start();
		if (!$rptTransValid(userrpt.fromDt)) {
			logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$rptTransValid(userrpt.toDt)) {
			logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
			return false;
		}
		else  if (!$rptTransValid($scope.userSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the User. Please try again.")
			return false;
		}
		var fromDt= $filter('date')(userrpt.fromDt,'yyyy-MM-dd');
		var toDt=$filter('date')(userrpt.toDt,'yyyy-MM-dd');
		var userId=$scope.userSelect;
		//console.log("br select: "+$scope.userSelect);

		$scope.url = '/imbank/reports?type=U&eType=P&frDate=' +fromDt+'&toDate='+toDt +'&user='+userId;
		blockUI.stop();

	}
	$scope.exportExcelReport = function(userrpt) {

		blockUI.start();
		if (!$rptTransValid(userrpt.fromDt)) {
			logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$rptTransValid(userrpt.toDt)) {
			logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
			return false;
		}
		else  if (!$rptTransValid($scope.userSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the User. Please try again.")
			return false;
		}
		var fromDt= $filter('date')(userrpt.fromDt,'yyyy-MM-dd');
		var toDt=$filter('date')(userrpt.toDt,'yyyy-MM-dd');
		var userId=$scope.userSelect;
		//console.log("br select: "+$scope.userSelect);

		$scope.url = '/imbank/reports?type=U&eType=E&frDate=' +fromDt+'&toDate='+toDt +'&user='+userId;
		blockUI.stop();
	}
}]).factory('userService', function ($http) {
	var imUserSvc = {};
	imUserSvc.GetUsers = function () {
		return $http({
			url: '/imbank/rest/user/gtUsers/'+"-1",
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};



	return imUserSvc;
}).factory('$rptTransValid', function () {
	/* var imLoginSvc = {};
     imLoginSvc.GetBranches = function(userId) {
     return $http({
     url : '/iprint/rest/login/getUserBranches/' + userId,
     method : 'GET',
     headers : {
     'Content-Type' : 'application/json'
     }
     });
     };

     return imLoginSvc;*/
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

