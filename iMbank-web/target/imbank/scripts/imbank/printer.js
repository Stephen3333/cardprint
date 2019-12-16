
'use strict';
angular.module('app.printer', []).controller("productCtrl", ["$scope", "$filter", "productSvc","bSvc", "loginSvc", "localStorageService", "$productValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, productSvc,bSvc, loginSvc, localStorageService, $productValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.branches = [];
	$scope.isExisting = false;
	$scope.productEditMode = false;
	$scope.productViewMode = false;
	/*
	 * for displaying printer deatils data
	 */
	$scope.loadProductData = function () {
		$scope.products = [], $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = "",$scope.productEditMode = false,$scope.productViewMode = false;
		bSvc.getProducts().success(function (response) {
			//console.log("printer"+response);
			return $scope.products = response, $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageProducts = $scope.filteredProducts.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredProducts = $filter("filter")($scope.products, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredProducts = $filter("orderBy")($scope.products, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageProducts = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	};

	$scope.loadProductData();

	/*
	 * loading branch data
	 */
	$scope.loadBranchData = function () {
		$scope.branches = [];

		bSvc.getbranch($scope).success(function (response) {
			$scope.branches = response;
			//console.log("printer branch "+response);
		})
	}
	$scope.loadBranchData();
	/*
	 * edit printer
	 */
	$scope.editProduct = function (product) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the product.");
			return false;
		}
		//console.log("###product.: "+product.status);
		$scope.productEditMode = true;
		$scope.isExisting = true;
		$scope.id = product.id;

		$scope.serialNumber = product.serialNumber;
		$scope.ipAddress = product.ipAddress;
		$scope.branchSelect=product.branchId;
		//$scope.branchId=product.branchId;
		$scope.status=product.status;
		//console.log("edit branchid"+product.branchName);

	};
	/*
	 * for creating new printer
	 */
	$scope.addProduct = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create products.");
			return false;
		}

		$scope.productEditMode = true;
		$scope.isExisting = false;

		$scope.id=0

		$scope.serialNumber = "";
		$scope.ipAddress = "";
		$scope.branchSelect="";
		//$scope.branchId="";
		$scope.status="";
	};
	/*
	 * cancel function
	 */
	$scope.cancelProduct = function () {
		$scope.productEditMode = false;
		$scope.isExisting = false;
		/*$scope.id=0
        $scope.status="";
        $scope.serialNumber = "";
        $scope.ipAddress = "";
        $scope.branchSelect="";
        $scope.branchId="";*/

	};

	/**
	 * saving details in db
	 */

	$scope.saveProduct = function () {
		var product = {};

		if (!$productValid($scope.serialNumber)) {
			logger.logWarning("Oops! You may have skipped specifying the Printer Serial Number. Please try again.")
			return false;
		}

		if (!$productValid($scope.ipAddress)) {
			logger.logWarning("Oops! You may have skipped specifying the Printer Ip Address. Please try again.")
			return false;
		}

		if (!$productValid($scope.branchSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the Printer's Branch. Please try again.")
			return false;
		}

		/*if (!$productValid($scope.status)) {
            logger.logWarning("Oops! You may have skipped specifying the Printer Status. Please try again.")
            return false;
        }*/

		if (!$productValid($scope.id))
			product.id = 0;
		else
			product.id = $scope.id;
		product.serialNumber = $scope.serialNumber;
		product.ipAddress = $scope.ipAddress;
		product.branchId = $scope.branchSelect;
		product.status = $scope.status;
		product.createdBy = $rootScope.UsrRghts.sessionId;
		//console.log("branch select"+$scope.branchSelect);
		blockUI.start()
		bSvc.UpdProduct(product).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The product information was saved successfully")
				if (parseFloat($scope.productId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}
				$scope.id=0

				$scope.serialNumber = "";
				$scope.ipAddress = "";
				$scope.branchSelect="";
				//$scope.branchId="";
				$scope.status="";

				$scope.productEditMode = false;
				$scope.productViewMode = false;

				$scope.loadProductData();
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

} ]).factory('productSvc', function ($http) {
	var imProductSvc = {};



	return imProductSvc;
}).factory('$productValid', function () {
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