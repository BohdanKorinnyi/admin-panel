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

        $scope.spentToday = 0;
        $scope.revenueToday = 0;
        $scope.spentYesterday = 0;
        $scope.revenueYesterday = 0;

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
            $scope.profitToday = ($scope.revenueToday - $scope.spentToday).toFixed(2);
            $scope.profitYesterday = ($scope.revenueYesterday - $scope.spentYesterday).toFixed(2);
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
                if($scope.selectedDate === "thisMonth" || $scope.selectedDate === "lastMonth"){
                    $scope.chartDateData.push(formatDateWithoutYear(getDateOfWeek($scope.chartData[i].date, 2017)));//todo: get current year
                }
                else{
                    $scope.chartDateData.push($scope.chartData[i].date);
                }
                $scope.chartRevData.push($scope.chartData[i].revenue);
                $scope.chartSpentData.push($scope.chartData[i].spent);
                $scope.chartProfitData.push($scope.chartData[i].profit);
            }

            $scope.chartDateData.sort(function(a, b){return a-b});
            new Chartist.Line('#revenueChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartRevData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
            new Chartist.Line('#spentChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartSpentData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
            new Chartist.Line('#profitChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartProfitData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
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

function getDateOfWeek(w, y) {
    var d = (1 + (w - 1) * 7); // 1st of January + 7 days for each week

    return new Date(y, 0, d);
}

function formatDateWithoutYear(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [month, day].join('-');
}


