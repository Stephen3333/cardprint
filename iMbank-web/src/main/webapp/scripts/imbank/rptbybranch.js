/**
 * Created by Elly on 2/2/2015.
 */
angular.module('app.rptByBranch', []).controller("rptByBranchCtrl", ["$scope", "$filter", "$rptTransValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","bSvc", function ($scope,$filter, $rptTransValid, $rootScope, blockUI, logger, $location,$http,$window,bSvc) {
	$scope.transbr={};
	$scope.brSelectMode = false;
	/**
	 * loading branches data
	 */
	$scope.loadBranchData = function () {
		$scope.branches = [];
		bSvc.getbranch().success(function (response) {
			$scope.branches = response;
			$scope.brSelectMode = true;
			//console.log("in get branches"+$scope.branches);

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
		});
	}
	$scope.loadBranchData();

	
	$scope.exportPdfReport = function(transbr) {
		
		if (!$rptTransValid($scope.branchSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the Branch. Please try again.")
			return false;
		}
		if (!$rptTransValid(transbr.fromDt)) {
			logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$rptTransValid(transbr.toDt)) {
			logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
			return false;
		}
		
		var fromDt= $filter('date')(transbr.fromDt,'yyyy-MM-dd');
		var toDt=$filter('date')(transbr.toDt,'yyyy-MM-dd');
		var brId=$scope.branchSelect;
		//console.log("br select: "+$scope.branchSelect);

		$scope.url = '/imbank/reports?type=B&eType=P&frDate=' +fromDt+'&toDate='+toDt +'&branch='+brId;
 
}
$scope.exportExcelReport = function(transbr) {

	if (!$rptTransValid($scope.branchSelect)) {
		logger.logWarning("Oops! You may have skipped specifying the Branch. Please try again.")
		return false;
	}
	if (!$rptTransValid(transbr.fromDt)) {
		logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
		return false;
	}
	else  if (!$rptTransValid(transbr.toDt)) {
		logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
		return false;
	}
	
	var fromDt= $filter('date')(transbr.fromDt,'yyyy-MM-dd');
	var toDt=$filter('date')(transbr.toDt,'yyyy-MM-dd');
	var brId=$scope.branchSelect;
	//console.log("br select: "+$scope.branchSelect);

	$scope.url = '/imbank/reports?type=B&eType=E&frDate=' +fromDt+'&toDate='+toDt +'&branch='+brId;

}

}]).factory('$rptTransValid', function () {
	/* var imLoginSvc = {};
     imLoginSvc.GetBranches = function(userId) {
     return $http({
     url : '/iprint/rest/login/getUserBranches/' + userId,
     method : 'GET',
     headers : {
     'Content-Type' : 'application/json'
     }
     });
     };

     return imLoginSvc;*/
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
