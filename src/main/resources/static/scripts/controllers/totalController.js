Application.controller('totalController', function ($scope, $http) {
    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };

    $scope.selectedSize = 50;

    $scope.dateOptions = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December'
    ];
    $scope.selectedMonth = [];

    $scope.buyersTotals = {};
    $scope.data = [];

    $scope.init = function () {
        var url = "/finance/total?from=2017-10-01&to=2017-10-31";
        $http.get(url).then(function success(response) {
            $scope.buyersTotals = response.data;
            Object.keys($scope.buyersTotals).map(function (p1) {
                $scope.data.push($scope.buyersTotals[p1]);
            });
            console.log($scope.data);
            for(var i = 0; i<2; i++){
                console.log($scope.data[i]);
                for(var j=0; j<2; j++){
                    console.log($scope.data[i][j]);
                }
            }
        }, function error() {
            notify('ti-alert', 'Error occurred during loading data', 'danger');
        });
    };
});