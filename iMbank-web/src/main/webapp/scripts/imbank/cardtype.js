/**
 * 
 */

'use strict';
angular.module('app.cardtype', []).controller("cardtypeCtrl", ["$scope", "$filter", "cardtypeSvc","bSvc","productSvc", "loginSvc", "localStorageService", "$cardtypeValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardtypeSvc,bSvc, productSvc,loginSvc, localStorageService, $cardtypeValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.branches = [];
	$scope.isExisting = false;
	$scope.cardtypeEditMode = false;
	$scope.cardtypeViewMode = false;

	/**
	 * for displaying card type details(under masters sub module is cardtype)
	 */
	$scope.loadCardTypeData = function () {
		$scope.cardtype = [], $scope.searchKeywords = "", $scope.filteredCardType = [], $scope.row = "",$scope.cardtypeEditMode = false,$scope.cardtypeViewMode = false;
		cardtypeSvc.getCardType().success(function (response) {
			// console.log("cardtype"+response);
			return $scope.cardtype = response, $scope.searchKeywords = "", $scope.filteredCardType = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageCardType = $scope.filteredCardType.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredCardType = $filter("filter")($scope.cardtype, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredCardType = $filter("orderBy")($scope.cardtype, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageCardType = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};

	$scope.loadCardTypeData();

	/**
	 * loading branch data
	 */

	$scope.loadBranchData = function () {
		$scope.branches = [];

		bSvc.getbranch($scope).success(function (response) {
			$scope.branches = response;

		})
	}
	$scope.loadBranchData();
	/*
	 * loading printers data
	 */
	$scope.loadProductData = function () {
		$scope.products = [];
		bSvc.getProducts().success(function (response) {
			$scope.products = response;
			// console.log("product res"+response);
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};
	$scope.loadProductData();
	/**
	 * edit card type
	 */
	$scope.editCardType = function (cardtype) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the cardtype.");
			return false;
		}

		$scope.cardtypeEditMode = true;
		$scope.isExisting = true;
		$scope.id = cardtype.id;
		$scope.code = cardtype.code;
		$scope.name = cardtype.name;
		$scope.description=cardtype.description;
		$scope.xCoordinates = cardtype.xCoordinates;
		$scope.yCoordinates = cardtype.yCoordinates;
		$scope.fontname = cardtype.fontname;
		$scope.fontsize= cardtype.fontsize;
		$scope.fontcolor = cardtype.fontcolor;

		// console.log("cardtype name"+cardtype.name);


	};
	/*
	 * for creating new card type
	 */

	$scope.addCardType = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create cardtype.");
			return false;
		}

		$scope.cardtypeEditMode = true;
		$scope.isExisting = false;

		$scope.id=0

		$scope.code = "";
		$scope.name = "";
		$scope.description="";
		$scope.xCoordinates = "";
		$scope.yCoordinates = "";
		$scope.fontname = "";
		$scope.fontsize= "";
		$scope.fontcolor = ""; 

	};
	/*
	 * cancel card type
	 */
	$scope.cancelCardType = function () {
		$scope.cardtypeEditMode = false;
		$scope.isExisting = false;

	};
	/*
	 * for save card type details in db
	 */
	$scope.saveCardType = function () {
		var cardtype = {};


		if (!$cardtypeValid($scope.code)) {
			logger.logWarning("Oops! You may have skipped specifying the Code. Please try again.")
			return false;
		}
		if (!$cardtypeValid($scope.name)) {
			logger.logWarning("Oops! You may have skipped specifying the Name. Please try again.")
			return false;
		}

		if (!$cardtypeValid($scope.description)) {
			logger.logWarning("Oops! You may have skipped specifying the  Description. Please try again.")
			return false;
		}
		if (!$cardtypeValid($scope.xCoordinates)) {
			logger.logWarning("Oops! You may have skipped specifying the  XCoordinates. Please try again.")
			return false;
		}
		if (!$cardtypeValid($scope.yCoordinates)) {
			logger.logWarning("Oops! You may have skipped specifying the  YCoordinates. Please try again.")
			return false;
		}
		if (!$cardtypeValid($scope.fontname)) {
			logger.logWarning("Oops! You may have skipped specifying the  FontName. Please try again.")
			return false;
		}
		if (!$cardtypeValid($scope.fontsize)) {
			logger.logWarning("Oops! You may have skipped specifying the  FontSize. Please try again.")
			return false;
		}
		if (!$cardtypeValid($scope.fontcolor)) {
			logger.logWarning("Oops! You may have skipped specifying the  FontColor. Please try again.")
			return false;
		}

		if (!$cardtypeValid($scope.id))
			cardtype.id = 0;
		else
			cardtype.id = $scope.id;
		cardtype.code = $scope.code;
		cardtype.name = $scope.name;
		cardtype.description = $scope.description;
		cardtype.xCoordinates = $scope.xCoordinates;
		cardtype.yCoordinates = $scope.yCoordinates;
		cardtype.fontname = $scope.fontname; 
		cardtype.fontsize = $scope.fontsize;
		cardtype.fontcolor = $scope.fontcolor;
		

		blockUI.start()
		cardtypeSvc.UpdCardType(cardtype).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The cardtype information was saved successfully")
				if (parseFloat($scope.cardtypeId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}
				$scope.id=0

				$scope.code = "";
				$scope.name = "";
				$scope.description="";

				$scope.cardtypeEditMode = false;
				$scope.cardtypeViewMode = false;

				$scope.loadCardTypeData();
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

} ]).factory('cardtypeSvc', function ($http) {
	var imCardTypeSvc = {};

	imCardTypeSvc.getCardType = function () {
		return $http({
			url: '/imbank/rest/cardtype/gtcardtype',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imCardTypeSvc.UpdCardType = function (cardtype) {
		return $http({
			url: '/imbank/rest/cardtype/createUpdateCardType',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: cardtype
		});
	};
	imCardTypeSvc.getPrinterType = function () {
		return $http({
			url: '/imbank/rest/cardtype/gtprintertype',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	imCardTypeSvc.UpdPrinterType = function (printertype) {
		return $http({
			url: '/imbank/rest/cardtype/createUpdatePrinterType',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: printertype
		});
	};
	return imCardTypeSvc;

}).factory('$cardtypeValid', function () {
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