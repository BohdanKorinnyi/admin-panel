"use strict";

Application.controller("adminDashboardController", function ($scope, $http, dateFactory) {

    $scope.revenueToday = "";
    $scope.revenueYesterday = "";
    $scope.spentToday = "";
    $scope.spentYesterday = "";

    $scope.dateOptions = {
        'Select date': 'no-date',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth'
    };
    $scope.selectedDate = 'no-date';


    //$scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
    //$scope.to = formatDate(dateFactory.pickDateTo($scope.selectedDate));

    $scope.initData = function () {
        var url = "/dashboard/get";
        $http.get(url).then(function success(response) {

        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
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


