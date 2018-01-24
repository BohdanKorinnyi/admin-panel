'use strict';

Application.controller('sourcesController', function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];


    $scope.sources = [];
    $scope.expenses = [];
    $scope.postbacks = [];
    $scope.showsourcesLoader = true;
    $scope.hideBuyerSelect = false;
    $scope.role = '';

    $scope.buyerDetails = false;
    $scope.dateDetails = true;
    $scope.buyerDetailsByDate = [];

    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;

    $scope.dt = {
        startDate: null,
        endDate: null
    };

    $scope.dpOptions = {
        locale: {
            applyClass: "btn-green",
            applyLabel: "Apply",
            fromLabel: "From",
            format: "DD-MM-YYYY",
            toLabel: "To",
            cancelLabel: 'Cancel',
            customRangeLabel: 'Custom range'
        },
        ranges: {
            "Today": [moment().subtract(1, "days"), moment()],
            "Yesterday": [moment().subtract(2, "days"), moment()],
            "Last 7 Days": [moment().subtract(6, "days"), moment()],
            "Last 30 Days": [moment().subtract(29, "days"), moment()]
        }
    };

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    $scope.getRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);
        request.send(null);
        if (request.status === 200) {
            var z = JSON.parse(request.response);
            $scope.role = z.authorities[0].authority;
        }
    };

    $scope.initsources = function () {
        var url = '/statistic/buyers';
        $scope.getRole();
        $scope.sources = [];
        $scope.showsourcesLoader = true;
        if ($scope.role === 'BUYER') {
            $scope.hideBuyerSelect = true;
        }
        $http.get(url).then(function (response) {
            $scope.sources = response.data;
            $scope.calculate();
            $scope.showsourcesLoader = false;
        }, function () {
            $scope.showsourcesLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer sources', 'danger');
        });
    };

    $scope.applySources = function () {
        var url = '/statistic/buyers?buyerIds=' + $scope.getGridDetails();
        $scope.sources = [];
        $scope.showsourcesLoader = true;
        $http.get(url).then(function (response) {
            $scope.sources = response.data;
            $scope.calculate();
            $scope.showsourcesLoader = false;
        }, function () {
            $scope.showsourcesLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer sources', 'danger');
        });
    };


    $scope.getDataDetails = function (buyerId, date) {
        var url = '/statistic/date?buyerId=' + buyerId + '&date=' + date;
        var request = new XMLHttpRequest();
        request.open('GET', url, false);
        request.send(null);
        if (request.status === 200) {
            var z = JSON.parse(request.response);
            for (var i = 0; i < $scope.sources.length; i++) {
                if ($scope.sources[i].buyerId === buyerId) {
                    for (var j = 0; j < $scope.sources[i].data.length; j++) {
                        if ($scope.sources[i].data[j].date === date) {
                            $scope.sources[i].data[j].dateDetails = z;
                            break;
                        }
                    }
                }
            }
        }
    };

    $scope.getGridDetails = function () {
        var fromDate = formatDate($scope.dt.startDate._d);
        var toDate = formatDate($scope.dt.endDate._d);
        return $scope.selectedBuyerNames + '&from=' + fromDate + '&to=' + toDate;
    };

    $scope.getBuyers = function () {
        var url = '/buyer';
        $http.get(url).then(function success(response) {
            $scope.buyerNames = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };

    $scope.id = -1;

    $scope.showBuyerDetailsColumn = function (id) {
        if ($scope.id === id) {
            $scope.buyerDetails = false;
            $scope.id = -1;
        } else {
            $scope.buyerDetails = true;
            $scope.id = id;
        }
    };

    $scope.buyerDate = '';

    $scope.showBuyerByDateDetailsColumn = function (id, date) {
        if ($scope.buyerDate === date) {
            $scope.dateDetails = false;
            $scope.buyerDate = '';
        } else {
            $scope.getDataDetails(id, date);
            $scope.dateDetails = true;
            $scope.buyerDate = date;
        }
    };

    $scope.calculate = function () {
        for (var i = 0; i < $scope.sources.length; i++) {
            var revenue = 0;
            var spent = 0;
            for (var j = 0; j < $scope.sources[i].data.length; j++) {
                spent += $scope.sources[i].data[j].spent;
                revenue += $scope.sources[i].data[j].revenue;
            }
            $scope.sources[i].revenue = revenue.toFixed(2);
            $scope.sources[i].spent = spent.toFixed(2);
        }
    };


    $scope.formatViewDate = function (date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [day, month, year].join('-');
    };
});