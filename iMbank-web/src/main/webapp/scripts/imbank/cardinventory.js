/**
 * 
 */

'use strict';
angular.module('app.cardinventory', []).controller("cardinventoryCtrl", ["$scope", "$filter", "cardinventorySvc","bSvc", "loginSvc","cardtypeSvc", "localStorageService", "$cardinventoryValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardinventorySvc,bSvc, loginSvc,cardtypeSvc, localStorageService, $cardinventoryValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.branches = [];
	$scope.cardinventory =[];
	$scope.isExisting = false;
	$scope.cardinventoryEditMode = false;
	$scope.cardinventoryViewMode = false;
	$scope.status = "";
	/**
	 * for loading card inventory data
	 */
	$scope.loadCardInventoryData = function () {
		$scope.cardinventory = [], $scope.searchKeywords = "", $scope.filteredCardInventory = [], $scope.row = "",$scope.cardinventoryEditMode = false,$scope.cardinventoryViewMode = false;
		cardinventorySvc.getCardInventory().success(function (response) {
			//console.log("cardinventory"+response);
			return $scope.cardinventory = response, $scope.searchKeywords = "", $scope.filteredCardInventory = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageCardInventory = $scope.filteredCardInventory.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredCardInventory = $filter("filter")($scope.cardinventory, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredCardInventory = $filter("orderBy")($scope.cardinventory, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageCardInventory = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});

	};

	$scope.loadCardInventoryData();
	/* $scope.$watch("status",function(newValue, oldValue){
        console.log($scope.cardinventory.batchSize);
        if($scope.status=="false"){
        	$scope.cardinventory.status="pending";
        }
        else{
        	$scope.cardinventory.status="received";
        }
    });*/
	/**
	 * for loading branch data
	 */
	$scope.loadBranchData = function () {
		$scope.branches = [];

		bSvc.getbranch($scope).success(function (response) {
			$scope.branches = response;

		})
	};
	$scope.loadBranchData();
	/**
	 * for loading card type
	 */
	$scope.loadCardTypeData = function () {
		$scope.cardtypes = [];
		cardtypeSvc.getCardType().success(function (response) {
			//console.log("cardtype"+response);
			$scope.cardtypes = response;
		})
	};
	$scope.loadCardTypeData();

	/**
	 * for editing card inventory
	 */
	$scope.editCardInventory = function (cardinventory) {

		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the cardinventory.");
			return false;
		}
		if(cardinventory.status == "pending"){

			//var ele = document.getElementById("editCard");

			document.getElementById("editCard").disabled = false;
			//console.log("###cardinventory.: "+cardinventory.status);
			$scope.cardinventoryEditMode = true;
			$scope.isExisting = true;
			$scope.id = cardinventory.id;

			$scope.batchSize = cardinventory.batchSize;
			$scope.batchNum = cardinventory.batchNum;
			$scope.branchSelect = cardinventory.branchId;
			$scope.cardtypeSelect = cardinventory.cardtypeId;
			$scope.status = cardinventory.status;
			$scope.createdBy = $rootScope.UsrRghts.sessionId;

		}
		else{ if(cardinventory.status=="received")
			//$scope.cardinventoryEditMode = false;
			//logger.log("you are not allowed to edit batch size where status is received");

			document.getElementById("editCard").disabled = true;
		document.getElementById("editCard").className = 'btn btn-warning';
		}
	};
	/**
	 * for creating card inventory
	 */
	$scope.addCardInventory = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create cardinventory.");
			return false;
		}

		$scope.cardinventoryEditMode = true;
		$scope.isExisting = false;

		$scope.id=0

		$scope.batchSize = "";
		$scope.batchNum = "";
		$scope.branchSelect="";
		$scope.cardtypeSelect="";
		$scope.status="";
		$scope.createdBy="";

	};
	/*
	 * cancel card inventory
	 */
	$scope.cancelCardInventory = function () {
		$scope.cardinventoryEditMode = false;
		$scope.isExisting = false;

	};
	/**
	 * for saving card inventory
	 */

	$scope.saveCardInventory = function () {
		var cardinventory = {};

		if (!$cardinventoryValid($scope.batchSize)) {
			logger.logWarning("Oops! You may have skipped specifying the current stock. please try again.")
			return false;
		}

		if (!$cardinventoryValid($scope.batchNum)) {
			logger.logWarning("Oops! You may have skipped specifying the Card Balance. Please try again.")
			return false;
		}

		if (!$cardinventoryValid($scope.branchSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the Cardinventory's Branch. Please try again.")
			return false;
		}
		if (!$cardinventoryValid($scope.cardtypeSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the Cardtype. Please try again.")
			return false;
		}

		if (!$cardinventoryValid($scope.id))
			cardinventory.id = 0;
		else
			cardinventory.id = $scope.id;
		cardinventory.batchSize = $scope.batchSize;
		cardinventory.batchNum = $scope.batchNum;
		cardinventory.branchId = $scope.branchSelect;
		cardinventory.cardtypeId = $scope.cardtypeSelect;
		cardinventory.createdBy = $rootScope.UsrRghts.sessionId;

		//console.log("branch select"+$scope.branchSelect);
		blockUI.start()
		cardinventorySvc.UpdCardInventory(cardinventory).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The cardinventory information was saved successfully")
				if (parseFloat($scope.cardinventoryId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}
				$scope.id=0

				$scope.batchSize = "";
				$scope.batchNum = "";
				$scope.branchSelect="";
				$scope.cardtypeSelect="";
				$scope.status="";

				$scope.cardinventoryEditMode = false;
				$scope.cardinventoryViewMode = false;

				$scope.loadCardInventoryData();
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

} ]).factory('cardinventorySvc', function ($http) {
	var imcardinventorySvc = {};

	imcardinventorySvc.getCardInventory = function () {
		return $http({
			url: '/imbank/rest/cardinventory/gtCardInventory',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	imcardinventorySvc.UpdCardInventory = function (cardinventory) {
		return $http({
			url: '/imbank/rest/cardinventory/createUpdateCardInventory',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: cardinventory
		});
	};
	imcardinventorySvc.UpdCardInventoryReceive = function (cardinventory) {
		return $http({
			url: '/imbank/rest/cardinventory/updateCardInventoryReceive',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: cardinventory
		});
	};
	imcardinventorySvc.getCardInventoryReject = function () {
		return $http({
			url: '/imbank/rest/cardinventory/gtCardReject',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	imcardinventorySvc.UpdCardInventoryReject = function (cardinventory) {
		return $http({
			url: '/imbank/rest/cardinventory/updateCardInventoryReject',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: cardinventory
		});
	};
	imcardinventorySvc.UpdCardStock = function (cardinventory) {
		return $http({
			url: '/imbank/rest/cardinventory/updateCardStock',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: cardinventory
		});
	};
	return imcardinventorySvc;
}).factory('$cardinventoryValid', function () {
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
})
.factory("inputDisabled", function(){
	return function(scope, element, attrs){
		scope.$watch(attrs.inputDisabled, function(val){
			if(val)
				element.removeAttr("disabled");
			else
				element.attr("disabled", "disabled");
		});
	}
});