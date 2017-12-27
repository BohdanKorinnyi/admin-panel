Application.controller("advertiserBalanceController", function ($scope, $http, dateFactory) {
    $scope.balance = [];

    $scope.advNames = [];
    $scope.selectedAdv = [];

    $scope.dateInterval = {
        'This Year': 'allTime',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Range': 'custom'
    };

    $scope.selectedInterval = 'allTime';
    $scope.dpFromDate = "";
    $scope.dpToDate = "";

    $scope.showAdvBalanceLoader = false;


    $scope.loadData = function () {
        var dateFrom = "";
        var dateTo = "";
        if ($scope.selectedInterval !== 'custom') {
            dateFrom = formatDate(dateFactory.pickDateFrom($scope.selectedInterval));
            dateTo = formatDate(dateFactory.pickDateTo($scope.selectedInterval));
        }
        else {
            dateFrom = formatDate($scope.dpFromDate);
            dateTo = formatDate($scope.dpToDate);
        }
        var url = '/advertiser/report?advertiserIds=' + ($scope.selectedAdv.join()) + '&from=' + dateFrom + '&to=' + dateTo;
        console.log(url);
        $http.get(url).then(function successCallback(response) {
            $scope.totalRevenue = response.data.totalRevenue;
            $scope.totalIncome = response.data.totalIncome;
            $scope.totalLiability = $scope.totalRevenue - $scope.totalIncome;
            $scope.incomes = response.data.incomes;
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