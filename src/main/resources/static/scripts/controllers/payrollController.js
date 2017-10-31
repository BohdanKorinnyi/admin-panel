'use strict';

Application.controller('payrollController', function ($scope, $http) {

    $scope.addedPayrolls = [];
    $scope.existedPayrolls = [];
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;

    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };

    $scope.selectedSize = 50;

    $scope.typeOptions = [];
    $scope.buyerOptions = [];
    $scope.currencyOptions = [];
    $scope.descriptionOptions = [];



    $scope.typeValues = {
        'Revenue': 0,
        'Cost': 1
    };


    $scope.sortType = '';
    $scope.sortReverse = '';

    $scope.addPayroll = function () {
        $scope.addedPayrolls.push({buyer: null, date: null, type: null,
            sum: null, currency: null, description: null});
    };

    $scope.applyPayroll = function () {
        $http.post("payroll/save", $scope.addedPayrolls).then(function successCallback(response) {
            //post for all payrolls
        }, function errorCallback(r) {
            notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
        });
    };


    $scope.getBuyers = function () {
        $http.get("/buyer").then(function success (r) {
            
        }), function fail(r) {

        };
    };

    $scope.getCurrency = function () {
        $http.get("/currency").then(function success (r) {

        }), function fail(r) {

        };
    };

    $scope.getDescription = function () {
        $http.get("payroll/description").then(function success (r) {

        }), function fail(r) {
            
        };
    };


    $scope.changeOrder = function () {
        if ($scope.sortReverse === '') {
            $scope.sortReverse = 'ASC';
        }
        else if ($scope.sortReverse === 'ASC') {
            $scope.sortReverse = 'DESC';
        }
        else if ($scope.sortReverse === 'DESC') {
            $scope.sortReverse = '';
        }
    };
});

function notify(icon, message, type) {
    $.notify({
        icon: icon,
        message: message
    }, {
        type: type,
        timer: 3000,
        placement: {
            from: 'top',
            align: 'right'
        }
    });
}