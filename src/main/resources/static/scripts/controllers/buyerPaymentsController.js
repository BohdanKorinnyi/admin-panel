Application.controller('buyerPaymentsController', function ($scope, $http) {
    $http.get('/payment').then(function (value) {
        $scope.payments = value.data;
    });

    $scope.toUpperCase = function (value) {
        return value.toLocaleUpperCase();
    };

    $scope.formatDate = function (date) {
        return formatViewDate(date);
    }
});