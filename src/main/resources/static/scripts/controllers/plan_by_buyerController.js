"use strict";

Application.controller("plan_by_buyerController", function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.plans = [];
    $scope.showPlanByBuyerLoader = true;

    $scope.selectedSize = 50;

    $scope.dateOptions = {
        'Select Month': 'no-date',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Month': 'custom'
    };
    $scope.selectedDate = 'no-date';
    $scope.dpFromDate = "";
    $scope.dpToDate = "";

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    $scope.loadplans = function () {
        var url = "";
        $scope.plans = [];
        $scope.showPlanByBuyerLoader = true;
        $http.post(url, $scope.getGridDetails()).then(function (response) {
            $scope.sources = response.data;
            $scope.showPlanByBuyerLoader = false;
        }, function () {
            $scope.showPlanByBuyerLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer plans', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        var fromDate = "";
        var toDate = "";
        if ($scope.selectedDate !== 'no-date') {
            if ($scope.selectedDate === 'custom') {
                fromDate = $scope.dpFromDate;
                toDate = $scope.dpToDate;
            }
            else {
                fromDate = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                toDate = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }

        return {
            "buyers": $scope.selectedBuyerNames,
            "from": fromDate,
            "to": toDate
        };
    };


    $scope.getBuyers = function () {
        var url = "/buyer";
        $http.get(url).then(function success(response) {
            $scope.buyerNames = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };
});



