/**
 * 
 */

'use strict';
angular.module('app.currentbalance', []).controller("currentbalanceCtrl", ["$scope", "$filter", "currentbalanceSvc","bSvc", "loginSvc", "localStorageService", "$currentbalanceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, currentbalanceSvc,bSvc, loginSvc, localStorageService, $currentbalanceValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.branches = [];
	$scope.isExisting = false;
	$scope.currentbalanceEditMode = false;
	$scope.currentbalanceViewMode = false;
	/*
	 * for displaying current balance data
	 */

	$scope.loadCurrentBalanceData = function () {
		$scope.currentbalance = [], $scope.searchKeywords = "", $scope.filteredCurrentBalanceData = [], $scope.row = "",$scope.currentbalanceEditMode = false,$scope.currentbalanceViewMode = false;
		currentbalanceSvc.getCurrentBalance().success(function (response) {
			//console.log("currentbalance"+response);
			return $scope.currentbalance = response, $scope.searchKeywords = "", $scope.filteredCurrentBalanceData= [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageCurrentBalanceData = $scope.filteredCurrentBalanceData.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredCurrentBalanceData = $filter("filter")($scope.currentbalance, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredCurrentBalanceData = $filter("orderBy")($scope.currentbalance, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPagecurrentbalance = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};

	$scope.loadCurrentBalanceDataData();
	/*
	 * loading branch data
	 */
	$scope.loadBranchData = function () {
		$scope.branches = [];

		bSvc.getbranch($scope).success(function (response) {
			$scope.branches = response;

		})
	}
	$scope.loadBranchData();
	/**
	 * for editing current balance
	 */
	$scope.editCurrentBalance = function (currentbalance) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the currentbalance.");
			return false;
		}

		$scope.currentbalanceEditMode = true;
		$scope.isExisting = true;
		$scope.id = currentbalance.id;
		$scope.branchId = currentbalance.branchId;
		$scope.currentBalance = currentbalance.currentBalance;
		//$scope.lastUpdate = currentbalance.lastUpdate;


	};
	/**
	 * for creating current balance
	 */
	$scope.addCurrentBalance = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create currentbalance.");
			return false;
		}

		$scope.currentbalanceEditMode = true;
		$scope.isExisting = false;

		$scope.id=0

		$scope.branchId="";
		$scope.currentBlance="";
		//$scope.lastUpdate="";
	};
	/**
	 * cancel function
	 */
	$scope.cancelCurrentBalance = function () {
		$scope.currentbalanceEditMode = false;
		$scope.isExisting = false;

	};
	/**
	 * for saving details in db
	 */
	$scope.saveCurrentBalanceData = function () {
		var currentbalance = {};


		if (!$currentbalanceValid($scope.branchId)) {
			logger.logWarning("Oops! You may have skipped specifying the currentbalance's Branch. Please try again.")
			return false;
		}
		if (!$currentbalanceValid($scope.currentBalance)) {
			logger.logWarning("Oops! You may have skipped specifying the Current balance. Please try again.")
			return false;
		}

		if (!$currentbalanceValid($scope.id))
			currentbalance.id = 55;
		else
			currentbalance.id = $scope.id;
		currentbalance.branchId = $scope.branchId;
		currentbalance.currentBalance = $scope.currentBalance;

		//console.log("branch id"+$scope.branchId);
		blockUI.start()
		currentbalanceSvc.UpdCurrentBalance(currentbalance).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The currentbalance information was saved successfully")
				if (parseFloat($scope.currentbalanceId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}
				$scope.id=0


				$scope.branchId="";
				$scope.currentBalance="";

				$scope.currentbalanceEditMode = false;
				$scope.currentbalanceViewMode = false;

				$scope.loadCurrentBalanceData();
				$scope.loadBranchData();

			} else {
				logger.logWarning(response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};

} ]).factory('currentbalanceSvc', function ($http) {
	var imcurrentbalanceSvc = {};

	imcurrentbalanceSvc.getCurrentBalance = function () {
		return $http({
			url: '/imbank/rest/currentbalance/gtCurrentBalance',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imcurrentbalanceSvc.UpdCurrentBalance = function (currentbalance) {
		return $http({
			url: '/imbank/rest/currentbalance/updateCurrentBalance',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: currentbalance
		});
	};

	return imcurrentbalanceSvc;
}).factory('$currentbalanceValid', function () {
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