'use strict';

Application.controller('balancePerMonthController', function ($scope, $http, $location) {
    var date = new Date();

    $scope.accounts = [];
    $scope.balance = [];
    $scope.advNames = [];
    $scope.selectedAdv = [];
    $scope.addedBalance = [];
    $scope.balanceToSave = [];

    $scope.showBalancePerMonthLoader = true;

    $scope.monthOptions = {
        'January': '1',
        'February': '2',
        'March': '3',
        'April': '4',
        'May': '5',
        'June': '6',
        'July': '7',
        'August': '8',
        'September': '9',
        'October': '10',
        'November': '11',
        'December': '12'
    };
    $scope.selectedMonth = date.getMonth() + 1;

    $scope.yearOptions = {
        'Current Year': 'thisYear',
        'Next Year': 'nextYear',
        'Previous Year': 'prevYear'
    };
    $scope.selectedYear = 'thisYear';

    $scope.initAdverts = function () {
        var url = 'advertiser/all';
        $http.get(url).then(function successCallback(response) {
            response.data.map(function (value) {
                $scope.advNames.push({id: value.id, name: value.advshortname});
            });
            $scope.selectedAdv = $scope.advNames;
        });
    };

    $scope.addBalance = function () {
        $scope.addedBalance.push({
            advertiser: null, date: formatViewDate(new Date()),
            total: null, commission: null,
            bank: null, account: null, cur: null,
            currId: null
        });
    };

    $scope.go = function (path) {
        $location.path(path);
    };

    $scope.getAccounts = function () {
        var url = '/account/finance';
        $http.get(url).then(function successCallback(response) {
            $scope.accounts = response.data;
        });
    };

    $scope.getCurrencyForCurrentAccount = function (accountId, index) {
        var currency = '';
        var currencyId = '';
        for (var i = 0; i < $scope.accounts.length; i++) {
            if ($scope.accounts[i].id === parseInt(accountId)) {
                currency = $scope.accounts[i].code;
                currencyId = $scope.accounts[i].currencyId;
            }
        }

        for (var j = 0; j < $scope.addedBalance.length; j++) {
            $scope.addedBalance[index].cur = currency;
            $scope.addedBalance[index].currId = currencyId;
        }
    };
    $scope.saveAddedBalance = function () {
        var url = '/income';
        for (var i = 0; i < $scope.addedBalance.length; i++) {
            $scope.balanceToSave.push({
                date: formatDate($scope.addedBalance[i].date),
                total: $scope.addedBalance[i].total,
                commission: $scope.addedBalance[i].commission,
                bank: $scope.addedBalance[i].bank,
                accountId: $scope.addedBalance[i].account,
                advertiserId: $scope.addedBalance[i].advertiser,
                currencyId: $scope.addedBalance[i].currId
            });
        }
        $http.post(url, $scope.balanceToSave).then(function successCallback(response) {
            $scope.addedBalance = [];
        });
    };

    $scope.calculateSumBank = function (index) {
        for (var i = 0; i < $scope.addedBalance.length; i++) {
            if ($scope.addedBalance[index].total !== null && $scope.addedBalance[index].commission !== null) {
                $scope.addedBalance[index].bank =
                    $scope.addedBalance[index].total - $scope.addedBalance[index].commission;
            }
        }
    };

    $scope.getBalance = function () {
        $scope.showBalancePerMonthLoader = true;
        $http.get('balance/' + getYear($scope.selectedYear) + '/' + $scope.selectedMonth).then(function (value) {
            $scope.showBalancePerMonthLoader = false;
            $scope.balance = value.data;
        }, function (reason) {
            $scope.showBalancePerMonthLoader = false;
        });
    }
});

function getYear(selectedYear) {
    var currentYear = new Date().getFullYear();
    if (selectedYear === 'prevYear') {
        return currentYear - 1;
    } else if (selectedYear === 'nextYear') {
        return currentYear + 1;
    }
    return currentYear;
}