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

    $scope.data = [];
    
    $scope.init = function () {
        var url = "/finance/total?from=2017-09-01&to=2017-12-31";
        $http.get(url).then(function success(response) {
            $scope.data = response.data;
        }, function error() {
            notify('ti-alert', 'Error occurred during loading data', 'danger');
        });
    };
});