'use strict';

Application.controller('buyerStatisticController', function ($scope, $http, dateFactory) {
    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];
    $scope.types = [];
    $scope.selectedTypes = [];
    $scope.buyerCosts = [];
    $scope.showBuyerCostsLoader = true;

    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;
    $scope.dateOptions = {
        'All time': 'allTime',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Range': 'custom'
    };
    $scope.selectedDate = 'thisMonth';
    $scope.dpFromDate = '';
    $scope.dpToDate = '';
    $scope.hideBuyerSelect = false;


    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    $scope.checkRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);
        request.send(null);
        var principle = JSON.parse(request.response);
        $scope.role = principle.authorities[0].authority;
        if ($scope.role === 'BUYER') {
            $scope.hideBuyerSelect = true;
        }
    };

    $scope.loadBuyerCosts = function () {
        $scope.buyerCosts = [];
        $scope.showBuyerCostsLoader = true;
        if ($scope.dpToDate !== '' && $scope.dpFromDate !== '') {
            $scope.from = formatDate($scope.dpFromDate);
            $scope.to = formatDate($scope.dpToDate);
        }
        else if ($scope.selectedDate === 'allTime') {
            var yearFrom = 2017;
            var yearTo = 2020;
            var f = new Date(yearFrom, 0, 1);
            var t = new Date(yearTo, 0, 1);
            $scope.from = formatDate(f);
            $scope.to = formatDate(t);
        }
        else {
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateTo($scope.selectedDate));
        }

        var url = '/buyer/spent/report?from=' + $scope.from + '&to=' + $scope.to
            + "&sources=" + $scope.selectedTypes + "&buyerIds=" + $scope.selectedBuyerNames;
        console.log(url);
        $http.get(url).then(function (response) {
            $scope.buyerCosts = response.data;
            $scope.showBuyerCostsLoader = false;
        }, function () {
            $scope.showBuyerCostsLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer spent', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        var yearFrom = 2017;
        var yearTo = 2020;

        var f = new Date(yearFrom, 0, 1);
        var t = new Date(yearTo, 0, 1);

        var fromDate = formatDate(f);
        var toDate = formatDate(t);

        if ($scope.selectedDate !== 'allTime') {
            if ($scope.selectedDate === 'custom') {
                fromDate = formatDate($scope.dpFromDate);
                toDate = formatDate($scope.dpToDate);
            }
            else {
                fromDate = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                toDate = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }

        return {
            'buyers': $scope.selectedBuyerNames,
            'types': $scope.selectedTypes,
            'from': fromDate,
            'to': toDate
        };
    };


    $scope.getBuyers = function () {
        var url = '/buyer';
        $http.get(url).then(function success(response) {
            $scope.buyerNames = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };


    $scope.getTypes = function () {
        var url = '/account/types';
        $http.get(url).then(function success(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.types.push({
                    id: i,
                    name: response.data[i]
                });
            }
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading types', 'danger');
        });
    };

    $scope.export = function () {
        var args = $scope.getGridDetails();
        window.location.href = 'report/stats?buyers='
            + args.buyers.join(',')
            + '&types=' + args.types.join(',')
            + '&from=' + args.from
            + '&to=' + args.to;
    };
});