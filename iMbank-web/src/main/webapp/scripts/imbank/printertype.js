/**
 * 
 */

'use strict';
angular.module('app.printertype', []).controller("printertypeCtrl", ["$scope", "$filter", "cardtypeSvc","bSvc","productSvc", "loginSvc", "localStorageService", "$printertypeValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardtypeSvc,bSvc, productSvc,loginSvc, localStorageService, $printertypeValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.isExisting = false;
	$scope.printertypeEditMode = false;
	$scope.printertypeViewMode = false;

	/**
	 * for displaying printer types data(either usb or network)
	 */
	$scope.loadPrinterTypeData = function () {
		$scope.printertype = [], $scope.searchKeywords = "", $scope.filteredprintertype = [], $scope.row = "",$scope.printertypeEditMode = false,$scope.printertypeViewMode = false;
		cardtypeSvc.getPrinterType().success(function (response) {
			//console.log("printertype"+response);
			return $scope.printertype = response, $scope.searchKeywords = "", $scope.filteredprintertype = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagePrinterType = $scope.filteredprintertype.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredprintertype = $filter("filter")($scope.printertype, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredprintertype = $filter("orderBy")($scope.printertype, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPagePrinterType = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};

	$scope.loadPrinterTypeData();
	/*
	 * edit printer type
	 */
	$scope.editPrinterType = function (printertype) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the printer type.");
			return false;
		}
		$scope.printertypeEditMode = true;
		$scope.isExisting = true;
		$scope.id = printertype.id;
		$scope.printerName = printertype.printerName;
		$scope.description = printertype.description;

	};
	/**
	 * for creating new printer type
	 */
	$scope.addPrinterType = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create printertype.");
			return false;
		}

		$scope.printertypeEditMode = true;
		$scope.isExisting = false;

		$scope.id=0
		$scope.printerName = "";
		$scope.description="";


	};
	/*
	 * cancel function
	 */
	$scope.cancelPrinterType = function () {
		$scope.printertypeEditMode = false;
		$scope.isExisting = false;

	};
	/**
	 * saving printer type details in db
	 */
	$scope.savePrinterType = function () {
		var printertype = {};


		if (!$printertypeValid($scope.printerName)) {
			logger.logWarning("Oops! You may have skipped specifying the Name. Please try again.")
			return false;
		}

		if (!$printertypeValid($scope.description)) {
			logger.logWarning("Oops! You may have skipped specifying the  Description. Please try again.")
			return false;
		}


		if (!$printertypeValid($scope.id))
			printertype.id = 0;
		else
			printertype.id = $scope.id;
		printertype.printerName = $scope.printerName;
		printertype.description = $scope.description;


		blockUI.start()
		cardtypeSvc.UpdPrinterType(printertype).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The printertype information was saved successfully")
				if (parseFloat($scope.id) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}
				$scope.id=0

				$scope.printerName = "";
				$scope.description="";

				$scope.printertypeEditMode = false;
				$scope.printertypeViewMode = false;

				$scope.loadPrinterTypeData();


			} else {
				logger.logWarning(response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};

} ]).factory('$printertypeValid', function () {
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