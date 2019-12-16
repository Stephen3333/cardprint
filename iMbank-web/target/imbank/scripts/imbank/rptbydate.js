/**
 * Created by Elly on 2/2/2015.
 */
angular.module('app.rptDate', []).controller("rptDateCtrl", ["$scope", "$filter", "$rptDateValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","bSvc", function ($scope,$filter, $rptDateValid, $rootScope, blockUI, logger, $location,$http,$window,bSvc) {
	$scope.trans={};
	/* $scope.brSelectMode = false;
     $scope.loadBranchList = function () {
                        $scope.branches = [];
                      loginSvc.GetBranches($scope.userId).success(function (response) {
                        $scope.branches = response;
                        $scope.brSelectMode = true;

                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                        });
                    }*/
	/**
	 * preview report function
	 */
	$scope.previewReport=function(trans)
	{
		blockUI.start();
		if (!$rptDateValid(trans.fromDt)) {
			logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		else  if (!$rptDateValid(trans.toDt)) {
			logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
			return false;
		}
		var fromDt= $filter('date')(trans.fromDt,'yyyy-MM-dd');
		var toDt=$filter('date')(trans.toDt,'yyyy-MM-dd');

		$scope.url = '/imbank/reports?type=D&frDate=' +fromDt+'&toDate='+toDt;
		// console.log(trans.fromDt);
		//console.log(trans.toDt);
		blockUI.stop();
	}
	
	$scope.exportPdfReport = function(trans) {
	
			
				if (!$rptDateValid(trans.fromDt)) {
					logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
					return false;
				}
				else  if (!$rptDateValid(trans.toDt)) {
					logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
					return false;
				}
				var fromDt= $filter('date')(trans.fromDt,'yyyy-MM-dd');
				var toDt=$filter('date')(trans.toDt,'yyyy-MM-dd');

				$scope.url = '/imbank/reports?type=D&eType=P&frDate=' +fromDt+'&toDate='+toDt;
		 
	}
	$scope.exportExcelReport = function(trans) {
		
				if (!$rptDateValid(trans.fromDt)) {
					logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
					return false;
				}
				else  if (!$rptDateValid(trans.toDt)) {
					logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
					return false;
				}
				var fromDt= $filter('date')(trans.fromDt,'yyyy-MM-dd');
				var toDt=$filter('date')(trans.toDt,'yyyy-MM-dd');

				$scope.url = '/imbank/reports?type=D&eType=E&frDate=' +fromDt+'&toDate='+toDt;
	}
	
}]).factory('$rptDateValid', function () {

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
