/**
 * 
 */
/**
 * Transaction Angular Module
 */
'use strict';
angular.module('app.cardprint', []).controller("cardprintCtrl", ["$scope", "$filter", "cardprintSvc","cardtypeSvc","currentbalanceSvc","loginSvc","bSvc", "userSvc","$cardprintValid", "$rootScope", "blockUI", "logger" ,"$location","localStorageService", function ($scope, $filter, cardprintSvc,cardtypeSvc,currentbalanceSvc,loginSvc,bSvc,userSvc, $cardprintValid, $rootScope, blockUI, logger, $location,localStorageService) {
	var init;

	$scope.selectedToPrint={};

	//var classId=$rootScope.UsrRghts.userClassId;
	var classId=1;
	$scope.isExisting = false;
	$scope.cardVerify = false;
	$scope.userId=$rootScope.UsrRghts.sessionId;

	//$scope.showName = true;
	$scope.loadBranchData = function () {
		$scope.branches=[];
		bSvc.getbranch($scope).success(function (response) {
			$scope.branches = response;
		})
	};
	//$scope.loadBranchData();
	/**
	 * for displaying card types in card print module
	 */
	$scope.loadCardTypeData = function () {
		$scope.cardtypes = [];
		cardtypeSvc.getCardType().success(function (response) {
			$scope.cardtypes= response;
		})
	};
	$scope.loadCardTypeData();

	$scope.loadPrinterTypeData = function () {
		$scope.printertypes = [];
		cardtypeSvc.getPrinterType().success(function (response) {
			$scope.printertypes= response;
		})
	};
	$scope.loadPrinterTypeData();

	$scope.loadUserData = function () {
		$scope.users = [];
		userSvc.users($scope).success(function (response) {
			$scope.users = response;

		})
	}
	//$scope.loadUserData();
	$scope.cancelJob = function(){

		//$("#printId")[0].cancelMagRead("XPS Card Printer");
		//logger.logError("Print Job Has Been Cancelled");
		
		$location.path('/dashboard');

	};

	$scope.testPrinter = function(){
		logger.logSuccess($("#printId")[0].printerConnected("XPS Card Printer"));
	};
	/**
	 * watch function for card type select
	 */
	$scope.$watch("cardtypeSelect", function (newValue, oldValue) {
		if ($cardprintValid(newValue)) {
			if (newValue != oldValue) {
				//console.log("new"+newValue);
				$scope.showCardSelectdetails(newValue);
			}
		}
	});

	$scope.showCardSelectdetails = function(cardtype_id){
		var found = $filter('getById')($scope.cardtypes, cardtype_id);
		$scope.selectedToPrint = found;
	}
	$scope.$watch("printertypeSelect", function (newValue, oldValue) {
		if ($cardprintValid(newValue)) {
			if (newValue != oldValue) {

				$scope.showCardPrinterdetails(newValue);
			}
		}
	});

	$scope.showCardPrinterdetails = function(printertype_id){
		var found = $filter('getById')($scope.printertypes, printertype_id);
		//console.log(found);
		//$scope.selectedToPrint = JSON.stringify(found);
		$scope.selectedPrinter = found;

	}
	/**
	 * function for entering account name and cartype,printer type in card print sub module
	 * under card issurance
	 */
	$scope.printCard = function(){
		if (!$cardprintValid($scope.accountName)) {
			logger.logWarning("Oops! You may have skipped specifying the account. Please try again.")
			return false;
		}
		//added
		if (!$cardprintValid($scope.accountNumber)) {
			logger.logWarning("Oops! You may have skipped specifying the account number. Please try again.")
			return false;
		}

		if (!$cardprintValid($scope.cardtypeSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the cardtype. Please try again.")
			return false;
		}
		if (!$cardprintValid($scope.printertypeSelect)) {
			logger.logWarning("Oops! You may have skipped specifying the printer. Please try again.")
			return false;
		}
		//$scope.showName = false;
		
	var appletRes = $("#printId")[0].cardPrint(
			$scope.selectedPrinter.printerName,
				$scope.accountName,
				$scope.selectedToPrint.xCoordinates, 
				$scope.selectedToPrint.yCoordinates,
			    $scope.selectedToPrint.fontname,
				$scope.selectedToPrint.fontsize,
				$scope.selectedToPrint.fontcolor);
	
		//var appletRes = $("#printId")[0].cardPrint( "XPS Card Printer212",$scope.accountName,45,540,"OCR A EXTENDED",12,400);
		//console.log("Update the Logs here##"+appletRes);
		//console.log("Print Res##"+appletRes);
	//blockUI.start();
	if(appletRes.toString()=='1'){
		$scope.savecard();
		//blockUI.stop();
		
	} else{
		logger.logError("Print Job Failed , Please try again!!");
		//blockUI.stop();
		
	}
	
		//$scope.savecard();

		/*var printrequest="0?"+$scope.selectedPrinter.printerName+','+$scope.accountName+','+$scope.selectedToPrint.xCoordinates+','+$scope.selectedToPrint.yCoordinates+','+$scope.selectedToPrint.fontname+","+$scope.selectedToPrint.fontsize+","+$scope.selectedToPrint.fontcolor
		if ("WebSocket" in window) {
			console.log("WebSocket is supported by your Browser!");

			// Let us open a web socket
			//var ws = new WebSocket("ws://localhost:9998/echo");
			var ws = new WebSocket("ws://localhost:8200/");
			ws.binaryType = "arraybuffer";
			//ws.bufferedAmount(enrollRequest.length);
			ws.onopen = function() {
				// Web Socket is connected, send data using send()
				console.log("Message to send");
			
			    
				ws.send(printrequest);
				console.log("Message is sent...");
			};
			var res = {};
			ws.onmessage = function(evt) {
				console.log(evt.data);
				//blockUI.start();
				res = JSON.parse(evt.data);
				//res=response;
				console.log("Response##"+res);
				if(res.toString()=='1'){
					$scope.savecard();
				} else{
					logger.logError("Print Job Failed , Please try again!!");
				}
				console.log("Message is received...");
				//console.log(received_msg);
			};

			ws.onclose = function() {
				// websocket is closed.
				
				console.log("Connection is closed...");				  
			};
			
		} else {
			// The browser doesn't support WebSocket
			console.log("WebSocket NOT supported by your Browser!");
		}*/
	};
	
	/**
	 * saving details in db
	 */
	$scope.savecard = function () {
		var cardPrint = {};


		/*if (!$cardprintValid($scope.branchSelect)) {
            logger.logWarning("Oops! You may have skipped specifying the branch's Name. Please try again.")
            return false;
        }*/

		if (!$cardprintValid($scope.id))
			cardPrint.id = 0;
		else
			cardPrint.id = 0;
        //cardPrint.accountNumber = $scope.accountNumber;
		cardPrint.accountName= $scope.accountName;
		cardPrint.printedBy = $rootScope.UsrRghts.sessionId;
		//Added
		cardPrint.accountNumber=$scope.accountNumber;
		//cardPrint.branchPrinted = $scope.branchSelect;
		cardPrint.cardFormatId = $scope.cardtypeSelect;
		cardPrint.printerId	 = $scope.printertypeSelect;

		blockUI.start()
		cardprintSvc.saveCard(cardPrint).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The CardPrint information was saved successfully");
				$scope.accountName="";
				//Added
				$scope.accountNumber="";
				$scope.printedBy = "";
				$scope.branchSelect="";
				$scope.cardtypeSelect="";
				$scope.printertypeSelect="";

				//$scope.cardVerify = false;
			} else {
				logger.logError(response.respMessage);
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			blockUI.stop();
		});
	};
	/*$scope.$watch('accountName', function(val) {
        $scope.accountName = $filter('uppercase')(val);
      }, true);*/

}]).factory('cardprintSvc', function ($http) {
	var imCardprintSvc = {};
	//var cardSvc = {};

	imCardprintSvc.GetUserById = function (userId) {
		return $http({
			url: '/imbank/rest/user/gtUserById/' + userId,
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		});
	};
	imCardprintSvc.saveCard = function (cardPrint) {
		return $http({
			url: '/imbank/rest/cardprint/createUpdateCard',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: cardPrint
		});
	};



	return imCardprintSvc;
}).factory('$cardprintValid', function () {
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
	}
}).filter('getById', function() {
	return function(input, id) {
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (+input[i].id == +id) {
				return input[i];
			}
		}
		return null;
	}
});
/*.directive("inputDisabled", function(){
  return function(scope, element, attrs){
    scope.$watch(attrs.inputDisabled, function(val){
      if(val)
        element.removeAttr("disabled");
      else
        element.attr("disabled", "disabled");
    });
  }
});*/