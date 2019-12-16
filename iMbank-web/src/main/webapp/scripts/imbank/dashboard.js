/**
 * Member Angular Module
 */
'use strict';
angular.module('app.dashboard', []).controller("dashBoardCtrl", ["$scope", "$filter", "dashBoardSvc", "$rootScope", "blockUI", "logger" ,"$location", function ($scope, $filter,dashBoardSvc, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.hide=false;
	$scope.tempCountList = [];
	$scope.donutChart2=[];
	$scope.countList=[];
	$scope.userCount=0;
	$scope.cardCount=0;
	$scope.cardbalanceCount=0;
	$scope.rejectedcardsCount=0;
	$scope.tempLineChartDetail=[];
	$scope.lineChartDetail=[];
	$scope.dashBoardViewMode=true;
	/**
	 * for displaying dash board details
	 */
	$scope.loadCountDetail=function(){
		dashBoardSvc.GetCountDetail().success(function (response) {
			$scope.tempCountList = response;
			//console.log("dashboard response"+response);
			if($scope.tempCountList.length>0){

				for (var i = 0; i <= $scope.tempCountList.length - 1; i++) {
					if($scope.tempCountList[i].detailDescription=="USERS"){
						$scope.userCount=$scope.tempCountList[i].detailCount;

					}else if($scope.tempCountList[i].detailDescription=="CARDS"){
						$scope.cardCount=$scope.tempCountList[i].detailCount;
					}
					/*else if($scope.tempCountList[i].detailDescription=="CARDBALANCE"){
        		 $scope.cardbalanceCount=$scope.tempCountList[i].detailCount;
        	 }
        	 else if($scope.tempCountList[i].detailDescription=="REJECTEDCARDS"){
        		 $scope.rejectedcardsCount=$scope.tempCountList[i].detailCount;
        	 }*/
					var obj =new Object();
					obj.data=$scope.tempCountList[i].detailCount;
					obj.label=$scope.tempCountList[i].detailDescription;
					$scope.countList.push( obj);
					$scope.isdata=true;
				}
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	$scope.loadTransChartDetail=function(){
		dashBoardSvc.GetTransChartDetail().success(function (response) {
			$scope.tempLineChartDetail = response;
			if($scope.tempLineChartDetail.length>0){
				for(var i=0;i <= $scope.tempLineChartDetail.length - 1; i++){
					var objd =new Object();
					objd.year=$scope.tempLineChartDetail[i].monthName;
					objd.a=$scope.tempLineChartDetail[i].authTransCount;
					objd.b=$scope.tempLineChartDetail[i].cancelTransCount;
					objd.c=$scope.tempLineChartDetail[i].totalTransCount;
					$scope.lineChartDetail.push(objd);
				}
			}

		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	angular.foreach=function(e){
		for (var i=0;$scope.relations.length-1;i++ ){
			$scope.relations[i].slice(i,1)
		}
	}
	if($rootScope.UsrRghts.userClassId == 4){
		$scope.dashBoardViewMode=false;
		$scope.loadCountDetail();
		$scope.loadTransChartDetail();
	}


	$scope.donutChart2.options = {
			series: {
				pie: {
					show: !0,
					innerRadius: .45
				}
			},
			legend: {
				show: !1
			},
			grid: {
				hoverable: !0,
				clickable: !0
			},
			colors: ["#176799", "#2F87B0", "#42A4BB", "#5BC0C4", "#78D6C7"],
			tooltip: !0,
			tooltipOpts: {
				content: "%p.0%, %s",
				defaultTheme: !1
			}
	}

}]).factory('dashBoardSvc', function ($http) {
	var imDashBoardSvc = {};
	imDashBoardSvc.GetCountDetail = function () {
		return $http({
			url: '/imbank/rest/dashBoard/gtCountDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};

	imDashBoardSvc.GetTransChartDetail = function () {
		return $http({
			url: '/imbank/rest/dashBoard/gtTransChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	return imDashBoardSvc;
})
.directive("flotChart", [function () {

	return {
		restrict: "A",
		scope: {
			data: "=",
			options: "="
		},

		link: function (scope, ele,attrs) {
			var data, options, plot;
			scope.$watch("$parent.countList",function (newValue, oldValue){
				return data = scope.$parent.countList, options = scope.options, plot = $.plot(ele[0], data, options)
			},true)

		}
	}

}]).directive("morrisChart", [function () {
	return {
		restrict: "A",
		scope: {
			data: "="
		},
		link: function (scope, ele, attrs) {
			var colors, data, func, options;

			switch (data = scope.data, attrs.type) {
			case "bar":
				scope.$watch("$parent.lineChartDetail",function (newValue, oldValue){ 
					if(newValue.length>0){
						data = scope.$parent.lineChartDetail;
						data = scope.$parent.lineChartDetail;
						return colors = void 0 === attrs.barColors || "" === attrs.barColors ? null : JSON.parse(attrs.barColors), options = {
								element: ele[0],
								data: data,
								xkey: attrs.xkey,
								ykeys: JSON.parse(attrs.ykeys),
								labels: JSON.parse(attrs.labels),
								barColors: colors || ["#0b62a4", "#7a92a3", "#4da74d", "#afd8f8", "#edc240", "#cb4b4b", "#9440ed"],
								stacked: attrs.stacked || null,
								resize: !0
						}, attrs.formatter && (func = new Function("y", "data", attrs.formatter), options.formatter = func),new Morris.Bar(options);}
				},true);


			}
		}

	}
}]).directive("lineChart", [function () {
	return {
		restrict: "A",
		scope: {
			data: "="
		},

		link: function (scope, ele,attrs) {
			var colors, data, func, options;
			scope.$watch("$parent.lineChartDetail",function (newValue, oldValue){ 
				if(newValue.length>0){
					data = scope.$parent.lineChartDetail;
					return colors = void 0 === attrs.lineColors || "" === attrs.lineColors ? null : JSON.parse(attrs.lineColors), options = {
							element: ele[0],
							data: data,
							xkey: attrs.xkey,
							ykeys: JSON.parse(attrs.ykeys),
							labels: JSON.parse(attrs.labels),
							lineWidth: attrs.lineWidth || 2,
							lineColors: colors || ["#0b62a4", "#7a92a3", "#4da74d", "#afd8f8", "#edc240", "#cb4b4b", "#9440ed"],
							resize: !0
					}, new Morris.Line(options);
				}},true);

		}
	}

}])