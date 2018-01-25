Application.controller("advertiserBalanceController", function ($scope, $http, dateFactory, $location) {
    $scope.balance = [];
    $scope.addedBalance = [];

    $scope.accounts = [];
    $scope.advNames = [];
    $scope.selectedAdv = [];

    $scope.balanceToSave = [];

    $scope.dpFromDate = new Date(new Date().getFullYear(), 0, 1);
    $scope.dpToDate = new Date(new Date().getFullYear() + 1, 0, 1);

    $scope.dt = {
        startDate: $scope.dpFromDate,
        endDate: $scope.dpToDate
    };

    $scope.dpOptions = {
        locale: {
            applyClass: "btn-green",
            applyLabel: "Apply",
            fromLabel: "From",
            format: "DD-MM-YYYY",
            toLabel: "To",
            cancelLabel: 'Cancel',
            customRangeLabel: 'Custom range'
        },
        ranges: {
            "Today": [moment().subtract(1, "days"), moment()],
            "Yesterday": [moment().subtract(2, "days"), moment()],
            "Last 7 Days": [moment().subtract(6, "days"), moment()],
            "Last 30 Days": [moment().subtract(29, "days"), moment()]
        }
    };

    $scope.incomes = [];

    $scope.showAdvBalanceLoader = false;

    $scope.showCardDate = function (date) {
        return formatViewDate(date.startDate) + " - " + formatViewDate(date.endDate);
    };

    $scope.loadData = function () {
        $scope.incomes = [];
        $scope.showAdvBalanceLoader = true;
        var dateFrom = formatDate($scope.dpFromDate);
        var dateTo = formatDate($scope.dpToDate);

        var url = '/advertiser/report?advertiserIds=' + ($scope.selectedAdv.join()) + '&from=' + dateFrom + '&to=' + dateTo;
        $http.get(url).then(function successCallback(response) {
            $scope.totalRevenue = response.data.totalRevenue;
            $scope.totalIncome = response.data.totalIncome;
            $scope.totalLiability = $scope.totalRevenue - $scope.totalIncome;
            $scope.incomes = response.data.incomes;
            $scope.showAdvBalanceLoader = false;
        });
    };

    $scope.formatFromToDate = function () {
        $scope.dpFromDate = formatDate($scope.dt.startDate._d);
        $scope.dpToDate = formatDate($scope.dt.endDate._d);
    };

    $scope.onApplyBtnClick = function () {
        $scope.incomes = [];
        $scope.showAdvBalanceLoader = true;
        $scope.formatFromToDate();
        var url = '/advertiser/report?advertiserIds=' + ($scope.selectedAdv.join()) + '&from=' + $scope.dpFromDate + '&to=' + $scope.dpToDate;
        $http.get(url).then(function successCallback(response) {
            $scope.totalRevenue = response.data.totalRevenue;
            $scope.totalIncome = response.data.totalIncome;
            $scope.totalLiability = $scope.totalRevenue - $scope.totalIncome;
            $scope.incomes = response.data.incomes;
            $scope.showAdvBalanceLoader = false;
        });
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
        for (var i = 0; i < $scope.addedBalance.length; i++) {
            if ($scope.addedBalance[index].total !== null
                && $scope.addedBalance[index].commission !== null) {
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

function formatViewDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [day, month, year].join('-');
}