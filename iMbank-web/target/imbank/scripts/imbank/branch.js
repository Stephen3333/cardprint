
/**
 * branch Angular Module
 */
'use strict';
angular.module('app.branch', []).controller("branchCtrl", ["$scope", "$filter", "bSvc", "loginSvc", "localStorageService", "$branchValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, bSvc, loginSvc, localStorageService, $branchValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.classes = [];
	$scope.isExisting = false;
	$scope.branchEditMode = false;
	$scope.branchViewMode = false;

	/*
	 * for displaying all branches data
	 */
	$scope.loadbranchData = function () {
		$scope.branches = [], $scope.searchKeywords = "", $scope.filteredBranches = [], $scope.row = "",$scope.branchEditMode = false,$scope.branchViewMode = false;
		bSvc.getbranch().success(function (response) {
			return $scope.branches = response, $scope.searchKeywords = "", $scope.filteredBranches = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageBranches = $scope.filteredBranches.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredBranches = $filter("filter")($scope.branches, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredBranches = $filter("orderBy")($scope.branches, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageBranches = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};

	$scope.loadbranchData();
	/**
	 * displaying for all regions data
	 */
	$scope.loadRegionData = function () {
		$scope.regions = [];
		bSvc.LoadRegions().success(function (response) {
			$scope.regions = response;
			//console.log("res region"+response);

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadRegionData();

	/*
	 * for edit branch data
	 */
	$scope.editbranch = function (branch) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the branch.");
			return false;
		}
		$scope.branchEditMode = true;
		$scope.isExisting = true;

		$scope.branchId = branch.branchId;
		$scope.branchName = branch.branchName;
		$scope.branchCode = branch.branchCode;
		$scope.branchAddress=branch.branchAddress;
		$scope.regionSelect =branch.regionId;
		$scope.status=branch.status;
		//console.log("####branchid: "+branch.id);
		//console.log("branchAddress"+branch.branchAddress);

	};
	/*
	 * for creating new branch
	 */
	$scope.addbranch = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create branches.");
			return false;
		}

		$scope.branchEditMode = true;
		$scope.isExisting = false;

		$scope.branchId =0;
		$scope.branchName = "";
		$scope.branchCode = "";
		$scope.branchAddress="";
		$scope.regionSelect="";
		$scope.status=0;
		//$scope.id = "";
		$scope.isDisabled = true;
	};
	/**
	 * cancel branch
	 */
	$scope.cancelbranch = function () {
		$scope.branchEditMode = false;
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

	$scope.savebranch = function () {
		var branch = {};

		if (!$branchValid($scope.branchCode)) {
			logger.logWarning("Oops! You may have skipped specifying the branch's Code. Please try again.")
			return false;
		}


		if (!$branchValid($scope.branchName)) {
			logger.logWarning("Oops! You may have skipped specifying the branch's Name. Please try again.")
			return false;
		}
		if (!$branchValid($scope.branchAddress)) {
			logger.logWarning("Oops! You may have skipped specifying the branch's Address. Please try again.")
			return false;
		}
		if (!$branchValid($scope.regionSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the Region. Please try again.")
			return false;
		}

		if (!$branchValid($scope.branchId))
			branch.branchId = 0;
		//console.log("####branchid.is zero: "+ branch.id );
		else
			branch.branchId = $scope.branchId;
		// console.log("####branchid.not zero: "+ branch.id );

		branch.branchName = $scope.branchName;
		branch.createdBy = $rootScope.UsrRghts.sessionId;
		branch.branchCode = $scope.branchCode;
		branch.branchAddress=$scope.branchAddress;
		branch.regionId=   $scope.regionSelect;
		branch.status=$scope.status;

		//console.log("checking region");
		//console.log($scope.regionSelect);

		blockUI.start()
		bSvc.Updbranch(branch).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The branch information was saved successfully")
				//  $scope.branchId =0;
				/* $scope.branchName = "";
                    $scope.branchId = "";
                    $scope.branchAddress="";
                    $scope.branchAddress="";*/
				if (parseFloat($scope.branchId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
							{
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
							}).error(function (data, status, headers, config) {
								logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
							});
				}
				$scope.branchId =0;
				$scope.branchName = "";
				$scope.branchCode= "";
				$scope.branchAddress="";
				$scope.regionSelect="";
				$scope.status=0;
				//$scope.id = "";
				$scope.branchEditMode = false;
				$scope.branchViewMode = false;

				$scope.loadbranchData();
				$scope.loadRegionData();
			} else {
				logger.logError(response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};

} ]).factory('bSvc', function ($http) {
	var imbSvc = {};
	imbSvc.Updbranch = function (branch) {
		return $http({
			url: '/imbank/rest/branch/createUpdatebranch',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: branch
		});
	};


	imbSvc.getbranch = function () {
		return $http({
			url: '/imbank/rest/branch/gtbranch',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imbSvc.LoadRegions = function () {
		return $http({
			url: '/imbank/rest/branch/gtregions',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imbSvc.getProducts = function () {
		return $http({
			url: '/imbank/rest/branch/gtprinters',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	imbSvc.UpdProduct = function (product) {
		return $http({
			url: '/imbank/rest/branch/createUpdateProduct',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: product
		});
	};


	return imbSvc;
}).factory('$branchValid', function () {
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