"use strict";

Application.controller("dashboardController", function ($scope, $http) {

    $scope.payroll = [];
    $scope.revenue = "";
    $scope.profitPlan = "";
    $scope.totalPaid = "";
    $scope.bonus = "";
    $scope.revenuePlan = "";
    $scope.buyerName = "";
    $scope.profit = "";

    $scope.revenueCompleted = "";
    $scope.profitCompleted = "";

    $scope.initData = function () {
        var url = "/dashboard/get";
        $http.get(url).then(function success(response) {
            $scope.payroll = response.data.payroll;
            $scope.revenue = response.data.revenue.toFixed(2);
            $scope.profitPlan = response.data.profitPlan.toFixed(2);
            $scope.totalPaid = response.data.totalPaid.toFixed(2);
            $scope.bonus = response.data.bonus.toFixed(2);
            $scope.revenuePlan = response.data.revenuePlan.toFixed(2);
            $scope.buyerName = response.data.buyerName;
            $scope.profit = response.data.profit.toFixed(2);

            $scope.revenueCompleted = (($scope.revenue/$scope.revenuePlan)*100).toFixed(2);
            $scope.profitCompleted = (($scope.profit/$scope.profitPlan)*100).toFixed(2);

        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyer\'s statistic', 'danger');
        });
    };
});



