"use strict";

Application.controller("adminDashboardController", function ($scope, $http, dateFactory) {

    $scope.chartData = [];
    $scope.chartDateData =[];
    $scope.chartRevData = [];
    $scope.chartSpentData = [];
    $scope.chartProfitData = [];
    $scope.adminDashboardData = [];
    $scope.revenueToday = "";
    $scope.revenueYesterday = "";
    $scope.spentToday = "";
    $scope.spentYesterday = "";
    $scope.revTotal = 0;
    $scope.spentTotal = 0;
    $scope.profitTotal = 0;

    $scope.dateOptions = {
        'All time': 'allTime',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth'
    };
    $scope.selectedDate = 'allTime';
    $scope.from = "";
    $scope.to = "";

    $scope.initData = function () {
        $scope.revTotal = 0;
        $scope.spentTotal = 0;
        $scope.profitTotal = 0;

        if($scope.selectedDate === "allTime"){
            $scope.from = "";
            $scope.to = "";
        }
        else if($scope.selectedDate === "yesterday"){
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
        }
        else{
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateTo($scope.selectedDate));
        }
        var url = "/admin/dashboard?from="+$scope.from+"&to="+$scope.to;
        $http.get(url).then(function success(response) {
            $scope.findTotals(response.data.data);
            $scope.adminDashboardData = response.data.data;
            $scope.spentToday = response.data.today.spent;
            $scope.revenueToday = response.data.today.revenue;
            $scope.spentYesterday = response.data.yesterday.spent;
            $scope.revenueYesterday = response.data.yesterday.revenue;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading dashboard info', 'danger');
        });
    };

    $scope.getChartData = function () {
        $scope.chartData = [];
        $scope.chartDateData =[];
        $scope.chartRevData = [];
        $scope.chartSpentData = [];
        $scope.chartProfitData = [];

        if($scope.selectedDate === "allTime"){
            $scope.from = "";
            $scope.to = "";
        }
        else{
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateTo($scope.selectedDate));
        }

        var url = "/admin/dashboard/charts?from="+$scope.from+"&to="+$scope.to+"&filter="+$scope.selectedDate;
        $http.get(url).then(function success(response) {
            $scope.chartData = response.data.data;
            for(var i=0; i<$scope.chartData.length; i++){
                $scope.chartDateData.push($scope.chartData[i].date);
                $scope.chartRevData.push($scope.chartData[i].revenue);
                $scope.chartSpentData.push($scope.chartData[i].spent);
                $scope.chartProfitData.push($scope.chartData[i].profit);
            }

            $scope.chartDateData.sort(function(a, b){return a-b});
            new Chartist.Line('#revenueChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartRevData]
            }, {
                showArea: true
            });
            new Chartist.Line('#spentChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartSpentData]
            }, {
                showArea: true
            });
            new Chartist.Line('#profitChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartProfitData]
            }, {
                showArea: true
            });
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading chart info', 'danger');
        });
    };


    $scope.findTotals = function (data) {
        for(var i = 0; i<data.length; i++){
            $scope.revTotal = $scope.revTotal + data[i].revenue;
            $scope.spentTotal = $scope.spentTotal + data[i].spent;
            $scope.profitTotal = $scope.profitTotal + data[i].profit;
        }
        $scope.revTotal = $scope.revTotal.toFixed(2);
        $scope.spentTotal = $scope.spentTotal.toFixed(2);
        $scope.profitTotal = $scope.profitTotal.toFixed(2);
    };

});

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}


