/**
 * 
 */
angular.module('app.rptByBalance', []).controller("rptByBalanceCtrl", ["$scope", "$filter", "$rptBalanceValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","cardinventorySvc","bSvc", function ($scope,$filter, $rptBalanceValid, $rootScope, blockUI, logger, $location,$http,$window,cardinventorySvc,bSvc) {
    $scope.balance={};
    $scope.balanceSelectMode = false;
   $scope.loadCardInventoryData = function () {
        $scope.cardinventory= [];
        cardinventorySvc.getCardInventory().success(function (response) {
            $scope.cardinventorys = response;
            $scope.balanceSelectMode = true;
            
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
        });
    }
    $scope.loadCardInventoryData();
    $scope.loadBranchData = function () {
        $scope.branches = [];
        bSvc.getbranch().success(function (response) {
            $scope.branches = response;
            $scope.brSelectMode = true;
           // console.log("in get branches"+$scope.branches);

        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
        });
    }
    //$scope.loadBranchData();
    $scope.previewReport=function(balance)
    {
        /*if (!$rptBalanceValid(balance.branchSelect)) {
            logger.logWarning("Oops! You may have skipped specifying branch. Please try again.")
            return false;
        }*/
        
        /*var fromDt= $filter('date')(balance.fromDt,'yyyy-MM-dd');
        var toDt=$filter('date')(balance.toDt,'yyyy-MM-dd');*/
        var brId = $scope.branchSelect;
       
        //$scope.url = '/imbank/reports?type=B&branch=' +brId;
        $scope.url = '/imbank/reports?type=CB';
    }

}]).factory('$rptBalanceValid', function () {
    
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