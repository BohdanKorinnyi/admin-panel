"use strict";

Application.controller("adminDashboardController", function ($scope, $http, dateFactory) {

    $scope.adminDashboardData = [];
    $scope.revenueToday = "";
    $scope.revenueYesterday = "";
    $scope.spentToday = "";
    $scope.spentYesterday = "";

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
            $scope.adminDashboardData = response.data.data;
            $scope.spentToday = response.data.today.spent;
            $scope.revenueToday = response.data.today.revenue;
            $scope.spentYesterday = response.data.yesterday.spent;
            $scope.revenueYesterday = response.data.yesterday.revenue;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading dashboard info', 'danger');
        });
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


