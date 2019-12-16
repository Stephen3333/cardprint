/**
 * 
 */
/**
 * 
 */

'use strict';
angular.module('app.cardbalance', []).controller("cardbalanceCtrl", ["$scope", "$filter", "cardinventorySvc","bSvc", "loginSvc","cardtypeSvc", "localStorageService", "$cardinventoryValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardinventorySvc,bSvc, loginSvc,cardtypeSvc, localStorageService, $cardinventoryValid, $rootScope, blockUI, logger, $location) {
    var init;
    $scope.branches = [];
    $scope.isExisting = false;
    $scope.cardinventoryEditMode = false;
    $scope.cardinventoryViewMode = false;
    

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
    
    $scope.loadBranchData = function () {
        $scope.branches = [];

        bSvc.getbranch($scope).success(function (response) {
            $scope.branches = response;
           
            })
    }
    $scope.loadBranchData();
    
    $scope.loadCardTypeData = function () {
        $scope.cardtypes = [];
        cardtypeSvc.getCardType().success(function (response) {
        	//console.log("cardtype"+response);
        	$scope.cardtypes = response;
        })
    }

    $scope.editCardInventory = function (cardinventory) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify the cardinventory.");
            return false;
        }
       // console.log("###cardinventory.: "+cardinventory.status);
        $scope.cardinventoryEditMode = true;
        $scope.isExisting = true;
        $scope.id = cardinventory.id;
        
        $scope.batchNum = cardinventory.batchNum;
        $scope.branchSelect=cardinventory.branchId;
        $scope.status=cardinventory.status;
        
    };
   /* $scope.addCardInventory = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create cardbalance.");
            return false;
        }

        $scope.cardinventoryEditMode = true;
        $scope.isExisting = false;

             $scope.id=0
              
              $scope.cardBalance = "";
              $scope.branchSelect="";
              $scope.status="";
    };*/

    $scope.cancelCardInventory = function () {
        $scope.cardinventoryEditMode = false;
        $scope.isExisting = false;
       
    };

    $scope.saveCardInventory = function () {
        var cardinventory = {};

        
        if (!$cardinventoryValid($scope.batchNum)) {
            logger.logWarning("Oops! You may have skipped specifying the Card Balance. Please try again.")
            return false;
        }

        if (!$cardinventoryValid($scope.branchSelect)) {
            logger.logWarning("Oops! You may have skipped specifying the Branch. Please try again.")
            return false;
        }
        
        if (!$cardinventoryValid($scope.status)) {
            logger.logWarning("Oops! You may have skipped specifying the Status. Please try again.")
            return false;
        }

        if (!$cardinventoryValid($scope.id))
            cardinventory.id = 0;
        else
        	cardinventory.id = $scope.id;
            cardinventory.batchNum = $scope.batchNum;
            cardinventory.branchId = $scope.branchSelect;
            cardinventory.status = $scope.status;
            cardinventory.createdBy = $rootScope.UsrRghts.sessionId;
            //console.log("branch select"+$scope.branchSelect);
            blockUI.start()
            cardinventorySvc.UpdCardInventory(cardinventory).success(function (response) {
                if (response.respCode == 200) {
                    logger.logSuccess("Great! The cardbalance information was saved successfully")
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
                    
                    $scope.batchNum = "";
                    $scope.branchSelect="";
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

} ]).factory('$cardinventoryValid', function () {
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