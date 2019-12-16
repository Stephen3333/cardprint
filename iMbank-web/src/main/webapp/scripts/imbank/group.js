/**
 * Groups Angular Module
 */
'use strict';
angular.module('app.group.rights', []).controller("groupsCtrl", ["$scope", "$filter", "userSvc", "groupSvc", "$groupValid", "$rootScope", "blockUI", "logger", "$location", "localStorageService", "loginSvc", function ($scope, $filter, userSvc, groupSvc, $groupValid, $rootScope, blockUI, logger, $location, localStorageService, loginSvc) {
	var init;
	$scope.classes = [];
	$scope.groups = [];
	/**
	 * for displaying branches
	 */
	$scope.loadClassesList = function () {
		$scope.classes = [];
		$scope.groupEditMode = false;
		userSvc.GetClasses().success(function (response) {
			$scope.classes = response;
			angular.forEach($scope.classes, function (item, index) {
				if (item.classId == 4) {
					$scope.classes.splice(index, 1);
				}
			});
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	/**
	 * for loading groups list
	 */
	$scope.loadGroupList = function () {
		$scope.groups = [];
		groupSvc.GetGroups($scope).success(function (response) {
			$scope.groups = response;
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	/**
	 * for displaying rights list
	 */
	$scope.loadRightList = function () {
		$scope.groupSave.rights = [];
		groupSvc.GetRights($scope.classSelect).success(function (response) {
			$scope.groupSave.rights = response;
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	/* $scope.$watch("classSelect", function (newValue, oldValue) {
        if ($groupValid(newValue)) {
            //console.log("New Value>>"+newValue);
            if (newValue != oldValue) {
                if (newValue == 4) {
                    $scope.linkId = "-1";
                    $scope.loadGroupData();
                } else{
                    $scope.linkId = "-1";
                    $scope.loadGroupData();
                }
            }
        }
    });*/

	/**
	 * for loading group data
	 */
	$scope.loadGroupData = function () {
		$scope.groups = [], $scope.searchKeywords = "", $scope.filteredGroups = [], $scope.row = "", $scope.groupEditMode = false;
		groupSvc.GetGroups($scope).success(function (response) {
			return $scope.groups = response, $scope.searchKeywords = "", $scope.filteredGroups = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageGroups = $scope.filteredGroups.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredGroups = $filter("filter")($scope.groups, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredGroups = $filter("orderBy")($scope.groups, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageGroups = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	$scope.linkId = $rootScope.UsrRghts.linkId;
	$scope.loadGroupData();
	/* if ($rootScope.UsrRghts.userClassId == 4) {
        $scope.classSelectMode = false;
        $scope.loadClassesList();
    }
    else {
        $scope.classSelectMode = true;
        $scope.classSelect = $rootScope.UsrRghts.userClassId;

        $scope.loadGroupData();
    }*/

	/**
	 * edit group
	 */
	$scope.editGroup = function (group) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the user group and rights.");
			return false;
		}
		$scope.groupEditMode = true;
		$scope.groupHeader = "Edit Group";
		$scope.groupSave = [];
		$scope.groupSave.groupId = group.groupId;
		$scope.groupSave.groupCode = group.groupCode;
		$scope.groupSave.groupName = group.groupName;
		$scope.linkId = group.groupLinkId;
		$scope.groupSave.rights = group.rights;
		$scope.groupSave.active = group.active;
		$scope.isDisabled = true;
	};
	/**
	 * for creating new group
	 */
	$scope.addGroup = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new user groups and assign rights.");
			return false;
		}
		$scope.groupHeader = "Group Creation";
		$scope.groupSave = [];
		$scope.groupSave.groupId = 0;
		$scope.groupSave.groupCode = "";
		$scope.groupSave.groupName = "";
		$scope.groupEditMode = true;
		$scope.groupSave.active = false;
		$scope.loadRightList();
		$scope.isDisabled = true ;

	};

	$scope.cancelGroup = function () {
		$scope.groupEditMode = false;
		$scope.isDisabled = false;
		$scope.groupSave.groupId = 0;
		$scope.groupSave.groupCode = "";
		$scope.groupSave.groupName = "";
		$scope.groupSave.active= false;

		$scope.loadGroupData();
	}
	/**
	 * update group
	 */

	$scope.updGroup = function () {
		var group = [];
		if (!$groupValid($scope.groupSave.groupCode)) {
			logger.logWarning("Oops! You may have skipped specifying the Group's Code. Please try again.")
			return false;
		}
		if (!$groupValid($scope.groupSave.groupName)) {
			logger.logWarning("Oops! You may have skipped specifying the Group's Name. Please try again.")
			return false;
		}
		if (!$groupValid($scope.groupSave.groupId))
			$scope.groupSave.groupId = 0;
		else
			group.groupId = $scope.groupSave.groupId;
		group.groupCode = $scope.groupSave.groupCode;
		group.groupName = $scope.groupSave.groupName;
		group.groupLinkId = $scope.linkId;
		group.rights = $scope.groupSave.rights;
		group.active = $scope.groupSave.active;
		group.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		groupSvc.UpdGroup(group).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The group information was saved successfully")
				if (parseFloat(group.groupId) == parseFloat($rootScope.UsrRghts.userGroupId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
					});
				}
				$scope.groupSave.groupId = 0;
				$scope.groupSave.groupName = "";
				$scope.groupSave.groupCode = "";
				$scope.groupEditMode = false;
				$scope.groupSave.rights = [];

				$scope.isDisabled = false;
				$scope.loadGroupData();
			} else {
				logger.logError(response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};

}]).factory('groupSvc', function ($http) {
	var imGroupSvc = {};
	imGroupSvc.GetGroups = function ($scope) {
		return $http({
			url: '/imbank/rest/userGroups/gtGroups/'  + $scope.linkId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	imGroupSvc.UpdGroup = function (group) {
		return $http({
			url: '/imbank/rest/userGroups/updGroup',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data:         {
				'groupId': group.groupId,
				'groupCode': group.groupCode,
				'groupName': group.groupName,
				'groupLinkId': group.groupLinkId,
				'active': group.active,
				'rights': group.rights,
				'createdBy': group.createdBy
			}
		});
	};
	imGroupSvc.GetRights = function () {
		return $http({
			url: '/imbank/rest/userGroups/gtRights/',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	return imGroupSvc;
}).factory('$groupValid', function () {
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