/**
 * 
 */
/**
 * 
 */

'use strict';
angular.module('app.receivefromhq', []).controller("receivefromhqCtrl", ["$scope", "$filter", "cardinventorySvc","bSvc", "loginSvc","cardtypeSvc", "localStorageService", "$cardinventoryValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardinventorySvc,bSvc, loginSvc,cardtypeSvc, localStorageService, $cardinventoryValid, $rootScope, blockUI, logger, $location) {
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
   $scope.loadCardTypeData();
    $scope.editCardInventory = function (cardinventory) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify the cardinventory.");
            return false;
        }
        //console.log("###cardinventory.: "+cardinventory.status);
        $scope.cardinventoryEditMode = true;
        $scope.isExisting = true;
        $scope.id = cardinventory.id;
        
        $scope.batchSize = cardinventory.batchSize;
        $scope.batchNum = cardinventory.batchNum;
        $scope.branchSelect=cardinventory.branchId;
        $scope.cardtypeSelect=cardinventory.cardtypeId;
        $scope.status=cardinventory.status;
        $scope.receivedBy=cardinventory.receivedBy;
        $scope.comments = cardinventory.comments;
        
    };
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
              $scope.receivedBy="";
              $scope.comments="";
    };

    $scope.cancelCardInventory = function () {
        $scope.cardinventoryEditMode = false;
        $scope.isExisting = false;
       
    };

    $scope.saveCardInventory = function () {
        var cardinventory = {};

        if (!$cardinventoryValid($scope.batchSize)) {
            logger.logWarning("Oops! You may have skipped specifying the batch size. Please try again.")
            return false;
        }

        if (!$cardinventoryValid($scope.batchNum)) {
            logger.logWarning("Oops! You may have skipped specifying the Cardbalance. Please try again.")
            return false;
        }

        if (!$cardinventoryValid($scope.branchSelect)) {
            logger.logWarning("Oops! You may have skipped specifying the  Branch. Please try again.")
            return false;
        }
        if (!$cardinventoryValid($scope.cardtypeSelect)) {
            logger.logWarning("Oops! You may have skipped specifying the Cardtype. Please try again.")
            return false;
        }
        if (!$cardinventoryValid($scope.status)) {
            logger.logWarning("Oops! You may have skipped specifying the Status. Please try again.")
            return false;
        }
        if (!$cardinventoryValid($scope.receivedBy)) {
            logger.logWarning("Oops! You may have skipped specifying the ReceivedBy. Please try again.")
            return false;
        }
        if (!$cardinventoryValid($scope.comments)) {
            logger.logWarning("Oops! You may have skipped specifying the comments. Please try again.")
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
            cardinventory.status = $scope.status;
            cardinventory.receivedBy = $scope.receivedBy;
            cardinventory.comments = $scope.comments;
            cardinventory.createdBy = $rootScope.UsrRghts.sessionId;
            //console.log("branch select"+$scope.branchSelect);
            blockUI.start()
            cardinventorySvc.UpdCardInventoryReceive(cardinventory).success(function (response) {
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
                    $scope.receivedBy="";
                    $scope.comments="";

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