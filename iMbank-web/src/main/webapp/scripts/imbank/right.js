
/**
 * branch Angular Module
 */
'use strict';
angular.module('app.right', []).controller("rightsCtrl", ["$scope", "$filter", "rightSvc", "loginSvc","groupSvc", "localStorageService", "$rightValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, rightSvc, loginSvc,groupSvc, localStorageService, $rightValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.classes = [];
	$scope.isExisting = false;
	$scope.rightEditMode = false;
	$scope.rightViewMode = false;

	/*
	 * for displaying all rights data
	 */
	$scope.loadRightData = function () {
		$scope.rights = [], $scope.searchKeywords = "", $scope.filteredRights = [], $scope.row = "",$scope.branchEditMode = false,$scope.branchViewMode = false;
		rightSvc.GetAllRights($scope.classSelect).success(function (response) {
			
			return $scope.rights = response, $scope.searchKeywords = "", $scope.filteredRights = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageRights = $scope.filteredRights.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredRights = $filter("filter")($scope.rights, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredRights = $filter("orderBy")($scope.rights, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageRights = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};

	$scope.loadRightData();
	$scope.loadHeaderData = function () {
		$scope.headers = [];
		rightSvc.GetHeaders().success(function (response) {
			$scope.headers = response;
			//console.log("res region"+response);

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadHeaderData();
	/*
	 * for edit branch data
	 */
	$scope.editRight = function (right) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the right.");
			return false;
		}
		$scope.rightEditMode = true;
		$scope.isExisting = true;

		$scope.rightId = right.rightId;
		$scope.rightName = right.rightName;
		$scope.rightViewName = right.rightViewName;
		$scope.headerSelect=right.headerId;
		$scope.rightDisplayName =right.rightDisplayName;
		
		//console.log("####branchid: "+branch.id);
		//console.log("branchAddress"+branch.branchAddress);

	};
	/*
	 * for creating new branch
	 */
	$scope.addRight = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create rights.");
			return false;
		}

		$scope.rightEditMode = true;
		$scope.isExisting = false;
		
		$scope.rightId =0;
		$scope.rightName = "";
		$scope.rightViewName ="";
		$scope.headerId="";
		$scope.rightDisplayName ="";
		//$scope.id = "";
		$scope.isDisabled = true;
	};
	/**
	 * cancel branch
	 */
	$scope.canceRight = function () {
		$scope.rightEditMode = false;
		$scope.isExisting = false;
		/*$scope.branchAddress="";
        $scope.branchName = "";
        $scope.branchId = "";
        $scope.id = "";
        $scope.status=0;*/

	};
	/**
	 * for saving branch data in db
	 */

	$scope.saveRight = function () {
		var right = {};

		if (!$rightValid($scope.rightName)) {
			logger.logWarning("Oops! You may have skipped specifying the rights's name. Please try again.")
			return false;
		}


		if (!$rightValid($scope.rightViewName)) {
			logger.logWarning("Oops! You may have skipped specifying the right's view Name. Please try again.")
			return false;
		}
		if (!$rightValid($scope.headerSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the right's menu header. Please try again.")
			return false;
		}
		if (!$rightValid($scope.rightDisplayName)) {
			logger.logWarning("Oops! You may have skipped specifying the right's display name. Please try again.")
			return false;
		}

		if (!$rightValid($scope.rightId))
			right.rightId = 0;
		//console.log("####branchid.is zero: "+ branch.id );
		else
			right.branchId = $scope.rightId;
		// console.log("####branchid.not zero: "+ branch.id );

		right.rightName = $scope.rightName;
		right.createdBy = $rootScope.UsrRghts.sessionId;
		right.rightViewName = $scope.rightViewName;
		right.headerId=$scope.headerSelect;
		right.rightDisplayName=   $scope.rightDisplayName;
		
		//console.log("checking region");
		//console.log($scope.regionSelect);

		blockUI.start()
		rightSvc.UpdRight(right).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Right information was saved successfully")
				
				$scope.rightId =0;
				$scope.rightViewName = "";
				$scope.rightName= "";
				$scope.rightDisplayName="";
				$scope.headerId="";
				
				$scope.rightEditMode = false;
				$scope.rightViewMode = false;

				$scope.loadRightData();
				
			} else {
				logger.logError(response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};

} ]).factory('rightSvc', function ($http) {
	var imbSvc = {};
	imbSvc.UpdRight = function (branch) {
		return $http({
			url: '/imbank/rest/userGroups/updRights',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: branch
		});
	};
	imbSvc.GetAllRights = function () {
		return $http({
			url: '/imbank/rest/userGroups/gtAllRights',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};


	imbSvc.getbranch = function () {
		return $http({
			url: '/imbank/rest/branch/gtbranch',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imbSvc.GetHeaders = function () {
		return $http({
			url: '/imbank/rest/userGroups/gtHeaders',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};


	return imbSvc;
}).factory('$rightValid', function () {
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