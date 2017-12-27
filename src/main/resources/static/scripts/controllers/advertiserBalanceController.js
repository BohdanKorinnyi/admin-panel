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

        if ($scope.selectedInterval !== 'custom') {
            $scope.dpFromDate = dateFactory.pickDateFrom($scope.selectedInterval);
            $scope.dpToDate = dateFactory.pickDateTo($scope.selectedInterval);
        }
        var url = '/advertiser/report?advertiserIds=' + ($scope.selectedAdv.join()) + '&from=' + $scope.dpFromDate + '&to=' + $scope.dpToDate;
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