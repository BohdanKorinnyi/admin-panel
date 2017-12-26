Application.controller("advertiserBalanceController", function ($scope, $http) {
    $scope.balance = [];

    $scope.advNames = [];
    $scope.selectedAdvNames = [];

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


    $scope.initData = function () {
    };

    $scope.initAdverts = function () {
        var url = "advertiser/all";
        $http.get(url).then(function successCallback(response) {
            for(var i = 0; i < response.data.length; i++){
                $scope.advNames.push({
                    id: response.data[i].id,
                    name: response.data[i].advname
                });
            }
        });
    };
});