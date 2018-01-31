'use strict';

Application.controller('buyerPaymentsController', function ($scope, $http) {
    var baseUrl = 'payroll';
    $scope.showPayrollsLoader = true;
    $scope.addedPayrolls = [];
    $scope.existedPayrolls = [];
    $scope.selectedSize = 50;
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.typeValues = [
        {'name': 'Accrual', 'value': 0},
        {'name': 'Write-off', 'value': 1}
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
    $scope.datePicker = '';


    $scope.sortColumn = '';
    $scope.sortReverse = '';

    $scope.role = "";
    $scope.disableButtons = false;

    $scope.getRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);
        request.send(null);
        $scope.role = JSON.parse(request.response).authorities[0].authority;
        $scope.disableButtons = $scope.role === "BUYER";
    };


    $scope.cancelClick = function () {
        $scope.addedPayrolls = [];
    };

    $scope.addPayroll = function () {
        $scope.addedPayrolls.push({
            buyerId: null, date: moment(), periond: moment(), type: null,
            sum: null, currencyId: null, description: null
        });
    };

    $scope.formatAddedPayrollsDate = function () {
        for (var i = 0; i < $scope.addedPayrolls.length; i++) {
            $scope.addedPayrolls[i].date = formatViewDate($scope.addedPayrolls[i].date._d);
        }
    };

    $scope.applyPayroll = function () {
        $scope.formatAddedPayrollsDate();
        $http.post('payroll/save', $scope.addedPayrolls).then(function successCallback() {
            $scope.cancelClick();
            $scope.loadPayrolls();
        }, function errorCallback() {
            notify('ti-alert', 'Error occurred during saving payroll', 'danger');
        });
    };


    $scope.getBuyers = function () {
        $http.get('/buyer').then(function success(response) {
            $scope.buyerOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };

    $scope.getCurrency = function () {
        $http.get('/currency').then(function success(response) {
            $scope.currencyOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading currency', 'danger');
        });
    };

    $scope.getDescription = function () {
        $http.get('payroll/description').then(function success(response) {
            $scope.descriptionOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading description', 'danger');
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
    $scope.selectedBuyerName = '';
    $scope.selectedBuyerId = 0;
    $scope.selectedTypeValue = '';
    $scope.selectedTypeId = 0;
    $scope.selectedCurrencyCode = '';
    $scope.selectedCurrencyId = 0;
    $scope.selectedDescriptionValue = '';
    $scope.selectedDate = '';
    $scope.selectedSum = '';

    $scope.clickRow = function (payroll) {
        $scope.selectedPayrollItem = payroll;
        $scope.selectedDate = formatViewDate(payroll.date);
        $scope.selectedSum = payroll.sum;
        if (payroll.type === 0) {
            $scope.selectedTypeValue = 'Accrual';
            $scope.selectedTypeId = 0;
        }
        else {
            $scope.selectedTypeValue = 'Write-off';
            $scope.selectedTypeId = 1;
        }
        for (var i = 0; i < $scope.buyerOptions.length; i++) {
            if (payroll.buyerId === $scope.buyerOptions[i].id) {
                $scope.selectedBuyerName = $scope.buyerOptions[i].name;
                $scope.selectedBuyerId = $scope.buyerOptions[i].id;
            }
        }
        for (var i = 0; i < $scope.currencyOptions.length; i++) {
            if (payroll.currencyId === $scope.currencyOptions[i].id) {
                $scope.selectedCurrencyCode = $scope.currencyOptions[i].code;
                $scope.selectedCurrencyId = $scope.currencyOptions[i].id;
            }
        }
        for (var i = 0; i < $scope.descriptionOptions.length; i++) {
            if (payroll.description === $scope.descriptionOptions[i]) {
                $scope.selectedDescriptionValue = $scope.descriptionOptions[i];
            }
        }
    };

    $scope.saveExistingPayroll = function () {
        var params = {};
        params.id = $scope.selectedPayrollItem.id;
        params.description = $scope.selectedDescriptionValue;

        params.date = formatViewDate($scope.selectedDate);
        params.sum = $scope.selectedSum;

        if ($scope.selectedTypeValue === 'Accrual') {
            $scope.selectedTypeId = 0;
        }
        else {
            $scope.selectedTypeId = 1;
        }
        params.type = $scope.selectedTypeId;

        for (var i = 0; i < $scope.currencyOptions.length; i++) {
            if ($scope.selectedCurrencyCode === $scope.currencyOptions[i].code) {
                $scope.selectedCurrencyId = $scope.currencyOptions[i].id;
            }
        }
        for (var i = 0; i < $scope.buyerOptions.length; i++) {
            if ($scope.selectedBuyerName === $scope.buyerOptions[i].name) {
                $scope.selectedBuyerId = $scope.buyerOptions[i].id;
            }
        }
        params.buyerId = $scope.selectedBuyerId;
        params.currencyId = $scope.selectedCurrencyId;

        $http.put(baseUrl, params).then(function success(response) {
            $scope.loadPayrolls();
            notify('ti-alert', 'Payroll updated successfully', 'success');
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
        });
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
            payrolls[i]['typeName'] = payrolls[i].type === 0 ? 'Accrual' : 'Write-off';
        }
        return payrolls;
    };

    $scope.removeRow = function (item) {
        $scope.addedPayrolls.splice(item, 1);
    };

    $scope.formatViewDate = function (date) {
        return formatViewDate(date);
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