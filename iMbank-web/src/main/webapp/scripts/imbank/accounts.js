
'use strict';
angular.module('app.accountInq', []).controller("accountCtrl", ["$scope", "$filter", "accountInquirySvc", "$accountValid", "$rootScope", "blockUI", "logger" ,"$location","loginSvc", function ($scope, $filter, accountInquirySvc, $accountValid, $rootScope, blockUI, logger, $location,loginSvc) {
    var init;
    $scope.supplierListMode = true;
    $scope.supplierViewMode= false;
    $scope.supplierEditMode=false;
    $scope.supplierEnrollmentMode=false;
    $scope.userClassId = 1;
    /**
     * for displaying all account data
     */
    $scope.loadAccountData = function () {

        $scope.accounts = [], $scope.searchKeywords = "", $scope.filteredAccounts = [], $scope.row = "", $scope.accountInquiryEditMode = false;
        accountInquirySvc.GetAccounts().success(function (response) {
            return $scope.accounts = response, $scope.searchKeywords = "", $scope.filteredAccounts = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageAccounts = $scope.filteredAccounts.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredAccounts = $filter("filter")($scope.accounts, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredAccounts = $filter("orderBy")($scope.accounts, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageAccounts = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
        });

    }

  $scope.loadSingleAccount = function () {
	  $scope.userClassId=classId;
	  $scope.accounts = "";
      accountInquirySvc.GetSingleAccount($rootScope.UsrRghts.linkExtInfo).success(function (response)
    		  {
    	  $scope.accounts = response;
    	  }).error(function (data, status, headers, config) {
          logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
      });
  }


    $scope.saveSupplier = function () {
        var supplier = [];
        if (!$accountValid($scope.customerId)) {
            logger.logWarning("Oops! You may have skipped specifying the ID/Passport No. Please try again.")
            return false;
        }
        if (!$accountValid($scope.firstName)) {
            logger.logWarning("Oops! You may have skipped specifying the Supplier's Full Name. Please try again.")
            return false;
        }
        //if (!$accountValid($scope.address)) {
        //    logger.logWarning("Oops! You may have skipped specifying the Address. Please try again.")
        //    return false;
        //}
        //if (!$accountValid($scope.postalCode)) {
        //    logger.logWarning("Oops! You may have skipped specifying the Postal Code. Please try again.")
        //    return false;
        //}
        //if (!$accountValid($scope.mobile)) {
        //    logger.logWarning("Oops! You may have skipped specifying the Mobile. Please try again.")
        //    return false;
        //}
        if (!$accountValid($scope.supplierCode)) {
            logger.logWarning("Oops! You may have skipped specifying the  Code. Please try again.")
            return false;
        }

        if (!$accountValid($scope.id))
            supplier.id = 0;
        else
            supplier.id = $scope.id;
            supplier.customerId = $scope.customerId;
            supplier.firstName = $scope.firstName;
            supplier.middleName = $scope.middleName;
            supplier.lastName = $scope.lastName;
            supplier.address = $scope.address;
            supplier.postalCode = $scope.postalCode;
            supplier.mobile = $scope.mobile;
            supplier.email = $scope.email;
            supplier.supplierCode = $scope.supplierCode;
            supplier.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        accountInquirySvc.createUpdateAccount(supplier).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Customer information was saved successfully")
                if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                    localStorageService.clearAll();
                    loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
                        localStorageService.set('rxr', rightLst);
                        $rootScope.UsrRghts = rightLst;
                    }).error(function (data, status, headers, config) {
                        logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                    });
                }

                if(supplier.id === 0){
                    $scope.id= "";
                    $scope.customerId= "";
                    $scope.firstName= "";
                    $scope.middleName= "";
                    $scope.lastName= "";
                    $scope.address= "";
                    $scope.postalCode= "";
                    $scope.email= "";
                    $scope.mobile= "";
                    $scope.supplierCode="";
                    $scope.bioId="";

                    $scope.supplierEditMode = false;
                    $scope.supplierEnrollmentMode=true;
                    $scope.supplierListMode=false;

                    $scope.supplierCode=response.respMessage;
                    //console.log("SupplierCode>>"+$scope.supplierCode);
                    //$scope.loadAccountData();
                } else{
                    $scope.supplierEditMode = false;
                    $scope.supplierEnrollmentMode=false;
                    $scope.supplierListMode=true;

                }


                $scope.loadAccountData();

            }
            else if (response.respCode == 202) {
                logger.logWarning(response.respMessage);
            }
            else {
                logger.logWarning("Oops! Something went wrong while updating the user. Please try again after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };

  
    $scope.loadAccountData();


        ///create new supplier
    $scope.addSupplier = function () {
        //console.log("Supplier creation");
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create Suppliers.");
            return false;
        }
        $scope.supplierListMode = false;
        $scope.supplierEditMode=true;
        $scope.isEnrolled = true;
        $scope.bioId=100;
        $scope.isExisting = false;

    }
 
 $scope.viewAccount = function (account) {
        $scope.supplierViewMode = true;
        $scope.supplierListMode=false;
        $scope.supplierEditMode= false;
       	$scope.id= account.id;
    	$scope.customerId= account.customerId;
    	$scope.firstName= account.firstName;
    	$scope.middleName= account.middleName;
    	$scope.lastName= account.lastName;
    	$scope.address= account.address;
    	$scope.postalCode= account.postalCode;
    	$scope.email= account.email;
    	$scope.mobile= account.mobile;
        $scope.supplierCode=account.supplierCode;
        $scope.bioId=account.bioId;

    };

    $scope.editSupplier = function (account) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to Edit Suppliers.");
            return false;
        }

        $scope.supplierViewMode = false;
        $scope.supplierListMode=false;
        $scope.supplierEditMode= true;
        $scope.id= account.id;
        $scope.customerId= account.customerId;
        $scope.firstName= account.firstName;
        $scope.middleName= account.middleName;
        $scope.lastName= account.lastName;
        $scope.address= account.address;
        $scope.postalCode= account.postalCode;
        $scope.email= account.email;
        $scope.mobile= parseFloat(account.mobile);
        $scope.supplierCode=account.supplierCode;
        $scope.bioId=account.bioId;
        $scope.isExisting = true;
    };
    
    $scope.cancelAccount=function(){
        $scope.supplierListMode = true;
        $scope.supplierViewMode= false;
        $scope.supplierEnrollmentMode=false;
        $scope.supplierEditMode=false;

        $scope.userClassId=1;

        $scope.id= "";
        $scope.customerId= "";
        $scope.firstName= "";
        $scope.middleName= "";
        $scope.lastName= "";
        $scope.address= "";
        $scope.postalCode= "";
        $scope.email= "";
        $scope.mobile= "";
        $scope.supplierCode="";
        $scope.bioId="";
   }

    $scope.enrollSupplier = function () {
        $scope.supplierListMode = false;
        $scope.supplierEditMode=false;
        $scope.isDisabled = true;
        $scope.supplierEnrollmentMode=true;
        $scope.active = false;
    }


    /*
    exports the table
     */
    //$scope.exportAction = function(export_action) {
    //    logger.info("Exporting"+export_action);
    //    switch (export_action) {
    //        case 'pdf':
    //            $scope.$broadcast('export-pdf',{})
    //            ;
    //            break;
    //        case 'excel':
    //            $scope.$broadcast('export-excel',{});
    //            break;
    //        case 'doc':
    //            $scope.$broadcast('export-doc',{});
    //            break;
    //        default:
    //            console.log('no  event caught');
    //    }
    //
    //}

}]).factory('accountInquirySvc', function ($http) {
    var imAccountInquirySvc = {};
    imAccountInquirySvc.GetAccounts = function (brokerId) {
    	
        return $http({
            url: '/imbank/rest/member/gtAccounts',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };
    imAccountInquirySvc.GetSingleAccount = function (accountNo) {
    	   return $http({
        	url: '/imbank/rest/member/gtsingleAccount?uniqueId=' + encodeURIComponent(accountNo),
            method: 'GET'
            
        });
    };

    imAccountInquirySvc.createUpdateAccount = function (account) {
        return $http({
            url: '/imbank/rest/member/updImportAccount',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: {
                'id':account.id,
                'customerId': account.customerId,
                'firstName': account.firstName,
                'middleName': account.middleName,
                'lastName': account.lastName,
                'address': account.address,
                'postalCode': account.postalCode,
                'mobile': account.mobile,
                'email': account.email,
                'supplierCode': account.supplierCode,
                'createdBy':account.createdBy
            }
        });
    };

    return imAccountInquirySvc;
}).factory('$accountValid', function () {
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