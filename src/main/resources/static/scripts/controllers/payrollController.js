'use strict';

Application.controller('payrollController', function ($scope, $http) {
    var baseUrl = 'payroll';
    $scope.showPayrollsLoader = true;
    $scope.addedPayrolls = [];
    $scope.existedPayrolls = [];
    $scope.selectedSize = 50;
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.typeValues = {
        'Revenue': 0,
        'Cost': 1
    };
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


    $scope.sortColumn = '';
    $scope.sortReverse = '';

    $scope.addPayroll = function () {
        $scope.addedPayrolls.push({
            buyer: null, date: null, type: null,
            sum: null, currency: null, description: null
        });
    };

    $scope.applyPayroll = function () {
        $http.post("payroll/save", $scope.addedPayrolls).then(function successCallback(response) {
            $scope.loadPayrolls();
        }, function errorCallback(response) {
            notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
        });
    };


    $scope.getBuyers = function () {
        $http.get("/buyer").then(function success(response) {
            $scope.buyerOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
        });
    };

    $scope.getCurrency = function () {
        $http.get("/currency").then(function success(response) {
            $scope.currencyOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
        });
    };

    $scope.getDescription = function () {
        $http.get("payroll/description").then(function success(response) {
            $scope.descriptionOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
        });
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

    $scope.clickRow = function (payroll) {
        console.log(payroll);
    };

    $scope.loadPayrolls = function () {
        $scope.payrolls = [];
        $scope.showPayrollsLoader = true;
        $http.post(baseUrl, $scope.getGridDetails()).then(function (response) {
            $scope.payrolls = $scope.updatePayrolls(response.data.data);
            $scope.showPayrollsLoader = false;
        }, function () {
            $scope.showPayrollsLoader = false;
            notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        return {
            'size': $scope.selectedSize,
            'number': $scope.selectedPage,
            'columnOrder': {'column': $scope.sortColumn, 'order': $scope.sortReverse}
        };
    };

    $scope.updatePayrolls = function (payrolls) {
        for (var i = 0; i < payrolls.length; i++) {
            payrolls[i]['bayerName'] = findBuyerName(payrolls[i].buyerId, $scope.buyerOptions);
            payrolls[i]['code'] = findCurrencyCode(payrolls[i].currencyId, $scope.currencyOptions);
            payrolls[i]['typeName'] = payrolls[i].type === 0 ? 'Revenue' : 'Cost';
        }
        console.log(payrolls);
        return payrolls;
    };
});

function findCurrencyCode(id, currency) {
    for (var i = 0; i < currency.length; i++) {
        if (currency[i].id === id) {
            return currency[i].code;
        }
    }
}


function findBuyerName(id, buyers) {
    for (var i = 0; i < buyers.length; i++) {
        if (buyers[i].id === id) {
            return buyers[i].name;
        }
    }
}

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