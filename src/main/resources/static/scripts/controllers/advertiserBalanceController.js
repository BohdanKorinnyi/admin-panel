Application.controller("advertiserBalanceController", function ($scope, $http, dateFactory) {
    $scope.balance = [];

    $scope.advNames = [];
    $scope.selectedAdv = [];

    $scope.dateInterval = {
        'This Year': 'This Year',
        'Today': 'Today',
        'Yesterday': 'Yesterday',
        'Last 7 days': 'Last Week',
        'This Month': 'This Month',
        'Last Month': 'Last Month',
        'Custom Range': 'Custom'
    };

    $scope.selectedInterval = 'This Year';
    $scope.dpFromDate = "";
    $scope.dpToDate = "";

    $scope.incomes = [];

    $scope.showAdvBalanceLoader = false;


    $scope.loadData = function () {
        $scope.incomes = [];
        $scope.showAdvBalanceLoader = true;
        var dateFrom = "";
        var dateTo = "";
        if ($scope.selectedInterval !== 'Custom') {
            if ($scope.selectedInterval === 'This Year'){
                var yearFrom = new Date().getFullYear();
                var yearTo = new Date().getFullYear() + 1;

                var f = new Date(yearFrom, 0, 1);
                var t = new Date(yearTo, 0, 1);

                dateFrom = formatDate(f);
                dateTo = formatDate(t);
            }
            else {
                dateFrom = formatDate(dateFactory.pickDateFrom($scope.selectedInterval));
                dateTo = formatDate(dateFactory.pickDateTo($scope.selectedInterval));
            }
        }
        else {
            dateFrom = formatDate($scope.dpFromDate);
            dateTo = formatDate($scope.dpToDate);
        }
        var url = '/advertiser/report?advertiserIds=' + ($scope.selectedAdv.join()) + '&from=' + dateFrom + '&to=' + dateTo;
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