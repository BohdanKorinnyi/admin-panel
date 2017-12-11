Application.controller('totalController', function ($scope, $http) {
    $scope.selectedMonth = 'December';
    $scope.selectedSize = 50;
    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };

    $scope.dateOptions = {
        'January': 'January',
        'February': 'February',
        'March': 'March',
        'April': 'April',
        'May': 'May',
        'June': 'June',
        'July': 'July',
        'August': 'August',
        'September': 'September',
        'October': 'October',
        'November': 'November',
        'December': 'December'
    };
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