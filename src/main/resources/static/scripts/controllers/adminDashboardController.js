"use strict";

Application.controller("adminDashboardController", function ($scope, $http, dateFactory) {

    $scope.adminDashboardData = [];
    $scope.revenueToday = "";
    $scope.revenueYesterday = "";
    $scope.spentToday = "";
    $scope.spentYesterday = "";
    $scope.revTotal = 0;
    $scope.spentTotal = 0;
    $scope.profitTotal = 0;

    $scope.dateOptions = {
        'All time': 'all_time',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth'
    };
    $scope.selectedDate = 'all_time';
    $scope.from = "";
    $scope.to = "";

    $scope.initData = function () {
        $scope.revTotal = 0;
        $scope.spentTotal = 0;
        $scope.profitTotal = 0;

        if($scope.selectedDate === "all_time"){
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


    new Chartist.Line('#revenueChart', {
        labels: [1, 2, 3, 4],
        series: [[100, 120, 180, 200]]
    }, {
        low: 0,
        showArea: true
    });


    new Chartist.Line('#spentChart', {
        labels: [1, 2, 3, 4],
        series: [[100, 120, 180, 200]]
    }, {
        low: 0,
        showArea: true
    });


    new Chartist.Line('#profitChart', {
        labels: [1, 2, 3, 4],
        series: [[100, 120, 180, 200]]
    }, {
        low: 0,
        showArea: true
    });
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


