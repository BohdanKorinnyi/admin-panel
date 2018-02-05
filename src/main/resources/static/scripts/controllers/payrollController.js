'use strict';

Application.controller('payrollController', function ($scope, $http, $q) {
    $scope.showPayrollsLoader = true;
    $scope.addedPayrolls = [];
    $scope.existedPayrolls = [];
    $scope.selectedSize = 50;
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.types = [];

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
        $http.get('/user/me').then(function (value) {
            $scope.disableButtons = value.data.authorities[0].authority === 'BUYER';
        });
    };

    $scope.cancelClick = function () {
        $scope.addedPayrolls = [];
    };

    $scope.addPayroll = function () {
        $scope.addedPayrolls.push({
            buyerId: null, date: moment(), periond: moment(), typeId: null,
            sum: null, currencyId: null, description: null
        });
    };

    $scope.applyPayroll = function () {
        for (var i = 0; i < $scope.addedPayrolls.length; i++) {
            $scope.addedPayrolls[i].date = $scope.addedPayrolls[i].date.format('YYYY-MM-DD');
            $scope.addedPayrolls[i].periond = $scope.addedPayrolls[i].periond.format('YYYY-MM-DD');
        }
        $scope.payrolls = [];
        $scope.showPayrollsLoader = true;
        $http.post('payroll/save', $scope.addedPayrolls).then(function successCallback() {
            $scope.cancelClick();
            $scope.loadPayrolls();
        }, function errorCallback() {
            $scope.cancelClick();
            $scope.loadPayrolls();
            notify('ti-alert', 'Error occurred during saving payrolls', 'danger');
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
    $scope.descriptionValue = '';
    $scope.selectedDate = '';
    $scope.selectedPeriod = '';
    $scope.selectedSum = '';

    $scope.clickRow = function (payroll) {
        $scope.selectedPayrollItem = payroll;
        $scope.selectedDate = formatViewDate(payroll.date);
        $scope.selectedPeriod = formatViewDate(payroll.date);
        $scope.description = payroll.description;
        $scope.selectedSum = payroll.sum;
        $scope.selectedTypeValue = getTypeName(payroll.typeId);
        $scope.selectedTypeId = payroll.typeId;

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
    };

    $scope.saveExistingPayroll = function () {
        $scope.payrolls = [];
        $scope.showPayrollsLoader = true;
        var params = {};
        params.id = $scope.selectedPayrollItem.id;
        params.description = $scope.descriptionValue;
        params.date = moment($scope.selectedDate).format('YYYY-MM-DD');
        params.periond = moment($scope.selectedPeriod).format('YYYY-MM-DD');
        params.sum = $scope.selectedSum;
        params.typeId = getTypeId($scope.selectedTypeValue);

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

        $http.put('payroll', params).then(function success() {
            $scope.loadPayrolls();
            notify('ti-alert', 'Payroll updated successfully', 'success');
        }, function fail() {
            $scope.loadPayrolls();
            notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
        });
    };

    $scope.loadPayrolls = function () {
        $scope.payrolls = [];
        $scope.showPayrollsLoader = true;
        var payrollTypes = $http.get('/payroll/types');
        var payrolls = $http.post('payroll', $scope.getGridDetails());

        $q.all([payrollTypes, payrolls]).then(function (value) {
            if (Array.isArray(value[0].data)) {
                $scope.types = [];
                value[0].data.map(function (value2) {
                    $scope.types.push({
                        name: value2.type,
                        value: value2.id
                    });
                });
            }
            $scope.payrolls = $scope.updatePayrolls(value[1].data.data);
            $scope.showPayrollsLoader = false;
        }, function (reason) {
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
            payrolls[i]['typeName'] = getTypeName(payrolls[i].typeId);
        }
        return payrolls;
    };

    function getTypeName(typeId) {
        var typeName = typeId;
        $scope.types.map(function (value) {
            if (value.value === typeId) {
                typeName = value.name;
            }
        });
        return typeName;
    }

    function getTypeId(typeName) {
        var typeId = -1;
        $scope.types.map(function (value) {
            if (value.name === typeName) {
                typeId = value.value;
            }
        });
        return typeId;
    }

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