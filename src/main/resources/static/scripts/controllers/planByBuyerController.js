"use strict";

Application.controller("planByBuyerController", function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.plans = [];
    $scope.showPlanByBuyerLoader = true;

    $scope.selectedSize = 50;

    $scope.dateOptions = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December'
    ];
    $scope.selectedMonth = [];

    $scope.loadplans = function () {
        var url = "/buyer/plan";
        $scope.plans = [];
        $scope.showPlanByBuyerLoader = true;
        $http.get(url).then(function (response) {
            $scope.plans = response.data;
            $scope.showPlanByBuyerLoader = false;
        }, function () {
            $scope.showPlanByBuyerLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer plans', 'danger');
        });
    };

    $scope.filterPlans = function () {
        $scope.showPlanByBuyerLoader = true;
        $scope.plans = [];
        var joinedBuyerNames = $scope.selectedBuyerNames.join(",");
        var joinedMonth = $scope.selectedMonth.join(",");
        var url = "/buyer/plan?buyers=" + joinedBuyerNames + "&month=" + joinedMonth;
        $http.get(url).then(function (response) {
            $scope.plans = response.data;
            $scope.showPlanByBuyerLoader = false;
        }, function () {
            $scope.showPlanByBuyerLoader = false;
            notify('ti-alert', 'Error occurred during filtering buyer plans', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        return {
            "buyers": $scope.selectedBuyerNames,
            "month": $scope.selectedMonth
        };
    };

    $scope.writePerformance = function (performance) {
        return performance.toFixed(2);
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



