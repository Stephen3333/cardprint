/**
 * 
 */
angular.module('app.rptReject', []).controller("rptRejectCtrl", ["$scope", "$filter", "$rptRejectValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","bSvc", function ($scope,$filter, $rptRejectValid, $rootScope, blockUI, logger, $location,$http,$window,bSvc) {
    $scope.reject={};
      $scope.brSelectMode = false;
     $scope.loadBranchList = function () {
                        $scope.branches = [];
                        bSvc.getbranch().success(function (response) {
                        $scope.branches = response;
                        $scope.brSelectMode = true;

                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                        });
                    }
     $scope.loadBranchList();
    $scope.previewReport=function(reject)
    {
    	//console.log(reject);
        if (!$rptRejectValid(reject.fromDt)) {
            logger.logWarning("Oops! You may have skipped specifying the From Date. Please try again.")
            return false;
        }
        else  if (!$rptRejectValid(reject.toDt)) {
            logger.logWarning("Oops! You may have skipped specifying the To Date. Please try again.")
            return false;
        }
        else  if (!$rptRejectValid($scope.branchSelect)) {
            logger.logWarning("Oops! You may have skipped specifying the Branch. Please try again.")
            return false;
        }
        var fromDt= $filter('date')(reject.fromDt,'yyyy-MM-dd');
        var toDt=$filter('date')(reject.toDt,'yyyy-MM-dd');
        //console.log(fromDt);
        var brId=$scope.branchSelect;
        $scope.url = '/imbank/reports?type=CR&frDate=' +fromDt+'&toDate='+toDt+'&branch='+brId;
    }

}]).factory('$rptRejectValid', function () {

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