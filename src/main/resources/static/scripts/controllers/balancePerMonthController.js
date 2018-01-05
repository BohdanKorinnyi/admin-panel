'use strict';

Application.controller('balancePerMonthController', function ($scope, $http, $location) {
    var date = new Date();

    $scope.accounts = [];
    $scope.advNames = [];
    $scope.selectedAdv = [];
    $scope.addedBalance = [];
    $scope.balanceToSave = [];


    $scope.showBalancePerMonthLoader = false;

    $scope.monthOptions = {
        "January": "01",
        "February": "02",
        "March": "03",
        "April": "04",
        "May": "05",
        "June": "06",
        "July": "07",
        "August": "08",
        "September": "09",
        "October": "10",
        "November": "11",
        "December": "12"
    };
    $scope.selectedMonth = "01";

    $scope.yearOptions = {
        "Current Year": "thisYear",
        "Next Year": "nextYear",
        "Previous Year": "prevYear"
    };
    $scope.selectedYear = "thisYear";


    $scope.setCurrentMonth = function () {
        var someDays = 10;
        var currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + someDays);
        var mm = currentDate.getMonth() + 1;
        if (mm < 10) {
            mm = "0" + mm;
        }

        mm = mm.toString();
        $scope.bigChartSelectedMonth = mm;
    };


    $scope.initAdverts = function () {
        var url = "advertiser/all";
        $http.get(url).then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.advNames.push({
                    id: response.data[i].id,
                    name: response.data[i].advshortname
                });
            }
            $scope.selectedAdv = $scope.advNames;
        });
    };


    $scope.addBalance = function () {
        $scope.addedBalance.push({
            advertiser: null, date: formatDate(new Date()),
            total: null, commission: null,
            bank: null, account: null, cur: null,
            currId: null
        });
    };


    $scope.go = function (path) {
        $location.path(path);
    };


    $scope.getAccounts = function () {
        var url = "/account/finance";

        $http.get(url).then(function successCallback(response) {
            $scope.accounts = response.data;
        });
    };

    $scope.getCurrencyForCurrentAccount = function (accountId, index) {
        var currency = "";
        var currencyId = "";
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
        var url = "/income";

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
        for(var i = 0; i < $scope.addedBalance.length; i++){
            if($scope.addedBalance[index].total !== null
                && $scope.addedBalance[index].commission !== null)
            {
                $scope.addedBalance[index].bank =
                    $scope.addedBalance[index].total - $scope.addedBalance[index].commission;
            }
        }
    };
});

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}
