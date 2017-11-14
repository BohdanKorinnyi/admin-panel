"use strict";

Application.controller("buyerSourcesController", function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.sources = [];
    $scope.expenses = [];
    $scope.postbacks = [];
    $scope.showsourcesLoader = true;

    $scope.buyerDetails = false;

    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;

    $scope.dateOptions = {
        'Select Date': 'no-date',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Range': 'custom'
    };
    $scope.selectedDate = 'no-date';
    $scope.dpFromDate = "";
    $scope.dpToDate = "";

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    $scope.loadsources = function () {
        var url = "/statistic";
        $scope.sources = [];
        $scope.showsourcesLoader = true;
        $http.post(url, $scope.getGridDetails()).then(function (response) {
            $scope.sources = response.data;
            $scope.showsourcesLoader = false;
        }, function () {
            $scope.showsourcesLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer sources', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        var fromDate = "";
        var toDate = "";
        if ($scope.selectedDate !== 'no-date') {
            if ($scope.selectedDate === 'custom') {
                fromDate = $scope.dpFromDate;
                toDate = $scope.dpToDate;
            }
            else {
                fromDate = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                toDate = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }

        return {
            "buyers": $scope.selectedBuyerNames,
            "from": fromDate,
            "to": toDate
        };
    };


    $scope.getBuyers = function () {
        var url = "/buyer";
        $http.get(url).then(function success(response) {
            $scope.buyerNames = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
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

    $scope.currentCostId = [];
    $scope.id = -1;

    $scope.showBuyerDetailsColumn = function (id) {
        if ($scope.id === id) {
            $scope.buyerDetails = false;
            $scope.id = -1;
        }
        else {
            $scope.buyerDetails = true;
            $scope.id = id;
        }
    };
});



