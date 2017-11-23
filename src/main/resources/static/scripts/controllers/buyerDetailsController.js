"use strict";

Application.controller("buyerDetailsController", function ($scope, $http) {

    $scope.buyers = [];

    $scope.initData = function () {
        var url = "/payroll/buyers";
        $http.get(url).then(function success(response) {
            $scope.buyers = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };
});



