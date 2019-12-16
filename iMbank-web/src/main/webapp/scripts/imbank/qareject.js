/**
 *
 */
/**
 *
 */

'use strict';
angular.module('app.qareject', []).controller("qarejectCtrl", ["$scope", "$filter", "cardinventorySvc", "bSvc", "loginSvc", "cardtypeSvc", "localStorageService", "$cardrejectValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardinventorySvc, bSvc, loginSvc, cardtypeSvc, localStorageService, $cardrejectValid, $rootScope, blockUI, logger, $location) {
    var init;
    $scope.branches = [];
    $scope.isExisting = false;
    $scope.cardrejectEditMode = false;
    $scope.cardrejectViewMode = false;


    $scope.loadCardRejectData = function () {
        $scope.cardreject = [], $scope.searchKeywords = "", $scope.filteredCardReject = [], $scope.row = "", $scope.cardrejectEditMode = false, $scope.cardrejectViewMode = false;
        cardinventorySvc.getCardInventoryReject().success(function (response) {
            //console.log("cardreject" + response);
            return $scope.cardreject = response, $scope.searchKeywords = "", $scope.filteredCardReject = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageCardReject = $scope.filteredCardReject.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredCardReject = $filter("filter")($scope.cardreject, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredCardReject = $filter("orderBy")($scope.cardreject, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageCardReject = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
        });
    };

    $scope.loadCardRejectData();

    $scope.loadBranchData = function () {
        $scope.branches = [];

        bSvc.getbranch($scope).success(function (response) {
            $scope.branches = response;

        })
    }
    $scope.loadBranchData();

    $scope.editCardReject = function (cardreject) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify the cardreject.");
            return false;
        }

        $scope.cardrejectEditMode = true;
        $scope.isExisting = true;
        $scope.id = cardreject.id;
        $scope.cardNum = cardreject.cardNum;
        $scope.rejectedBy = cardreject.rejectedBy;
        $scope.branchSelect = cardreject.branchId;
        $scope.reason = cardreject.reason;
        //$scope.rejectedOn = cardreject.rejectedOn;
        //console.log("###cardreject.: "+cardreject.rejectedBy);

    };
    $scope.addCardReject = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create cardreject.");
            return false;
        }

        $scope.cardrejectEditMode = true;
        $scope.isExisting = false;

        $scope.id = 0
        $scope.cardNum = "";
        $scope.rejectedBy = "";
        $scope.branchSelect = "";
        $scope.reason = "";
        //$scope.rejectedOn="";

    };

    $scope.cancelCardReject = function () {
        $scope.cardrejectEditMode = false;
        $scope.isExisting = false;

    };

    $scope.saveCardReject = function () {
        var cardreject = {};
        if (!$cardrejectValid($scope.cardNum)) {
            logger.logWarning("Oops! You may have skipped specifying the Card Number. Please try again.")
            return false;
        }

        if (!$cardrejectValid($scope.rejectedBy)) {
            logger.logWarning("Oops! You may have skipped specifying the RejectedBy. Please try again.")
            return false;
        }

        if (!$cardrejectValid($scope.branchSelect)) {
            logger.logWarning("Oops! You may have skipped specifying the  Branch. Please try again.")
            return false;
        }
        if (!$cardrejectValid($scope.reason)) {
            logger.logWarning("Oops! You may have skipped specifying the Reason. Please try again.")
            return false;
        }
        /*if (!$cardrejectValid($scope.rejectedOn)) {
         logger.logWarning("Oops! You may have skipped specifying the RejectedOn. Please try again.")
         return false;
         }*/

        if (!$cardrejectValid($scope.id))
            cardreject.id = 0;
        else
            cardreject.id = $scope.id;
        cardreject.cardNum = $scope.cardNum;
        cardreject.rejectedBy = $scope.rejectedBy;
        cardreject.branchId = $scope.branchSelect;
        cardreject.reason = $scope.reason;
        //cardreject.rejectedOn = $scope.rejectedOn;
        cardreject.rejectedBy = $rootScope.UsrRghts.sessionId;
        //console.log("rejected date" + $scope.rejectedOn);
        blockUI.start()
        cardinventorySvc.UpdCardInventoryReject(cardreject).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The cardreject information was saved successfully")
                if (parseFloat($scope.cardrejectId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                    localStorageService.clearAll();
                    loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
                        localStorageService.set('rxr', rightLst);
                        $rootScope.UsrRghts = rightLst;
                    }).error(function (data, status, headers, config) {
                        logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                    });
                }
                $scope.id = 0
                $scope.cardNum = "";
                $scope.rejectedBy = "";
                $scope.branchSelect = "";
                $scope.reason = "";
                //$scope.rejectedOn="";

                $scope.cardrejectEditMode = false;
                $scope.cardrejectViewMode = false;

                $scope.loadCardRejectData();
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

}]).factory('$cardrejectValid', function () {
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