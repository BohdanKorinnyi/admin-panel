'use strict';

Application.controller('payrollController', function ($scope) {

    $scope.payrolls = [];
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.sortingDetails = {};

    $scope.sortType = '';
    $scope.sortReverse = '';

    $scope.changeOrder = function () {
        if ($scope.sortReverse === '') {
            $scope.sortReverse = 'ASC';
        }
        else if ($scope.sortReverse === 'ASC') {
            $scope.sortReverse = 'DESC';
        }
        else if ($scope.sortReverse === 'DESC') {
            $scope.sortReverse = '';
        }
    };

function notify(icon, message, type) {
    $.notify({
        icon: icon,
        message: message
    }, {
        type: type,
        timer: 3000,
        placement: {
            from: 'top',
            align: 'right'
        }
    });
}