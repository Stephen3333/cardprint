/**
 * 
 */

'use strict';
angular.module('app.lockusers', []).controller("lockusersCtrl", ["$scope", "$filter","lockuserSvc",  "loginSvc", "localStorageService", "$lockuserValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, lockuserSvc,loginSvc, localStorageService, $lockuserValid, $rootScope, blockUI, logger, $location) {
    var init;
    $scope.classes = [];
    $scope.isExisting = false;
    $scope.lockuserEditMode = false;
    $scope.lockuserViewMode = false;

    $scope.loadlockUserData = function () {
        $scope.lockUsers = [], $scope.searchKeywords = "", $scope.filteredlockUsers = [], $scope.row = "",$scope.lockuserEditMode = false,$scope.lockuserViewMode = false;
        lockuserSvc.GetAttempts().success(function (response) {
        	//console.log("lockeduser"+response);
            return $scope.lockUsers = response, $scope.searchKeywords = "", $scope.filteredlockUsers = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagelockUsers = $scope.filteredlockUsers.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredlockUsers = $filter("filter")($scope.lockUsers, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredlockUsers = $filter("orderBy")($scope.lockUsers, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPagelockUsers = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
        });
       
    };

    $scope.loadlockUserData();

    $scope.editlockUser = function (user) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify the lockuser.");
            return false;
        }
        $scope.lockuserEditMode = true;
        $scope.isExisting = true;
        $scope.userHeader = "Edit lockuser";
        $scope.userId = user.userId;
        $scope.userName = user.userName;
        $scope.isLocked = user.isLocked;

    };
    $scope.addlockUser = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create companies.");
            return false;
        }

        $scope.lockuserEditMode = true;
        $scope.isExisting = false;
        $scope.userHeader = "lockuser Creation";
        $scope.userId =0;
        $scope.userName = "";
        $scope.isLocked = false;
    };

    $scope.cancellockUser = function () {
        $scope.lockuserEditMode = false;
        $scope.isExisting = false;

        $scope.userId =0;
        $scope.userName = "";
        $scope.isLocked = "";

    };
   

    $scope.savelockUser = function () {
        var user = {};

        
        /*if (!$lockuserValid($scope.userName)) {
            logger.logWarning("Oops! You may have skipped specifying the lockuser's Name. Please try again.")
            return false;
        }*/
        if (!$lockuserValid($scope.isLocked)) {
            logger.logWarning("Oops! You may have skipped specifying the lockuser's status. Please try again.")
            return false;
        }


        if (!$lockuserValid($scope.userId))
            user.userId = 0;
	     else
	        user.userId = $scope.userId;
	        user.userName = $scope.userName;
	        user.isLocked = $scope.isLocked;
            user.createdBy = $rootScope.UsrRghts.sessionId;
            user.pwdAttempts = 0;
            blockUI.start()
            lockuserSvc.resetAttempts(user).success(function (response) {
                if (response.respCode == 200) {
                    logger.logSuccess("Great! The lockuser information was saved successfully");
                    if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                        localStorageService.clearAll();
                        loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
                            localStorageService.set('rxr', rightLst);
                            $rootScope.UsrRghts = rightLst;
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                        });
                    }
                    $scope.userId =0;
                    $scope.userName = "";
                    $scope.pwdAttempts = "";
                    $scope.isLocked = false;
                    $scope.lockuserEditMode = false;
                    $scope.lockuserViewMode = false;

                    $scope.loadlockuserData();
                } else {
                    logger.logError(response.respMessage);
                }
                blockUI.stop();
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                blockUI.stop();
            });
        };

} ]).factory('lockuserSvc', function ($http) {
    var lockuserSvc = {};
    lockuserSvc.GetAttempts = function() {
        return $http({
            url : '/imbank/rest/user/getAttempts',
            method : 'GET',
            headers : {
                'Content-Type' : 'application/json'
            }
        });
    };
    lockuserSvc.resetAttempts = function(user) {
        return $http({
            url : '/imbank/rest/user/updUserAttempts',
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json'
            },
            data : user

        });
    };
return lockuserSvc;

}).factory('$lockuserValid', function () {
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