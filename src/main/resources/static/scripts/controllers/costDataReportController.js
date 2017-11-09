"use strict";

Application.controller("costDataReportController", function ($scope, $http) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.types = [];
    $scope.selectedTypes = [];

    $scope.costs = [];
    $scope.showCostsLoader = false;


    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;

    $scope.dateOptions = {
        'Select Date': 'no-date',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Range': 'custom'
    };
    $scope.selectedDate = 'no-date';

    $scope.loadCosts = function () {
        var url = "/statistic/all";
        $scope.costs = [];
        $scope.showPayrollsLoader = true;
        $http.post(url, $scope.getGridDetails()).then(function (response) {
            $scope.costs = response.data;
            $scope.showCostsLoader = false;
        }, function () {
            $scope.showCostsLoader = false;
            notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        return {
            "buyers": $scope.selectedBuyerNames,
            "types": $scope.selectedTypes
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

    $scope.getTypes = function () {
        var url = "/account/types";
        $http.get(url).then(function success(response) {
            $scope.types = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading types', 'danger');
        });
    };
});