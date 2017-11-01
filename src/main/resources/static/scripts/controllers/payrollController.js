'use strict';

Application.controller('payrollController', function ($scope, $http) {
    var baseUrl = 'payroll';
    $scope.addedPayrolls = [];
    $scope.existedPayrolls = [];
    $scope.selectedSize = 50;
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.typeValues = [
        {'name': 'Revenue', 'value': '0'},
        {'name': 'Cost', 'value': 1}
    ];
    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };

    $scope.selectedSize = 50;
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


    $scope.selectedPayrollItem = {};
    $scope.selectedBuyerName = "";
    $scope.selectedTypeValue = "";
    $scope.selectedCurrencyCode = "";
    $scope.selectedDescriptionValue = "";

    $scope.clickRow = function (payroll) {
        $scope.selectedPayrollItem = payroll;
        if(payroll.type == 0){
            $scope.selectedTypeValue = "Revenue";
        }
        else{
            $scope.selectedTypeValue = "Cost";
        }

        for(var i = 0; i<$scope.buyerOptions.length; i++){
            if(payroll.id == $scope.buyerOptions[i].id){
                $scope.selectedBuyerName = $scope.buyerOptions[i].name;
            }
        }

        for(var i = 0; i<$scope.currencyOptions.length; i++){
            if(payroll.currencyId == $scope.currencyOptions[i].id){
                $scope.selectedCurrencyCode = $scope.currencyOptions[i].code;
            }
        }

        for(var i = 0; i<$scope.descriptionOptions.length; i++){
            if(payroll.description == $scope.descriptionOptions[i]){
                $scope.selectedDescriptionValue = $scope.descriptionOptions[i];
            }
        }
    };

    $scope.loadPayrolls = function () {
        $http.post(baseUrl, $scope.getGridDetails()).then(function (response) {
            $scope.payrolls = $scope.updatePayrolls(response.data.data);
            console.log($scope.payrolls);
        }, function () {
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