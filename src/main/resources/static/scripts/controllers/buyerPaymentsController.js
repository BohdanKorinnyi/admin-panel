Application.controller('buyerPaymentsController', function ($scope, $http) {
    $scope.payments = [];
    $http.get('/payment').then(function (value) {
        $scope.payments = value.data;
    });

    $scope.toUpperCase = function (value) {
        return value.toLocaleUpperCase();
    };

    $scope.formatDate = function (date) {
        return formatViewDate(date);
    };

    $scope.addPayment = function () {
        $scope.payments.unshift({
            buyer: null,
            staff: null,
            date: null,
            payroll: null,
            code: null,
            type: null,
            wallet: null
        });
    };
});