Application.controller('totalController', function ($scope) {
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
});