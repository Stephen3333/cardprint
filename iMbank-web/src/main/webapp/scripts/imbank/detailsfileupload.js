
'use strict';
angular.module('app.fileupload', []).controller("fileuploadCtrl", ["$scope", "$filter", "fileuploadSvc", "loginSvc", "localStorageService",  "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, fileuploadSvc, loginSvc, localStorageService, $rootScope, blockUI, logger, $location) {
    //var init;

    $scope.uploadSupplierDetails = function () {
        logger.logSuccess("Uploading file in progress");
        var file = $scope.myFile;
        //console.log('file is## ' + JSON.stringify(file));
        blockUI.start()
        fileuploadSvc.uploadSupplier(file,$rootScope.UsrRghts.sessionId).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Supplier Information Uploaded successfully")

                $location.path('/dashboard');

            } else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };




    $scope.uploadTransactionDetails = function () {
        logger.logSuccess("Uploading file in progress!!");
        var file = $scope.myFile;
        //console.log('file is## ' + JSON.stringify(file));
        blockUI.start()
        fileuploadSvc.uploadTransaction(file,$rootScope.UsrRghts.sessionId).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Transactions have Uploaded successfully")

                $location.path('/dashboard');

            } else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };

} ]).factory('fileuploadSvc', function ($http) {
    var uploadSvc = {};

    //imProductSvc.getUoms = function () {
    //    return $http({
    //        url: '/everinic/rest/transaction/gtUom/',
    //        method: 'GET',
    //        headers: { 'Content-Type': 'application/json' }
    //    });
    //};


    uploadSvc.uploadSupplier = function (file,createdBy) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('createdBy',createdBy);
        return $http({
            url: '/imbank/rest/file/uploadsupplier',
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };


    uploadSvc.uploadTransaction = function (file,createdBy) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('createdBy',createdBy);
        return $http({
            url: '/imbank/rest/file/uploadtrans',
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };

    return uploadSvc;
}).directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);