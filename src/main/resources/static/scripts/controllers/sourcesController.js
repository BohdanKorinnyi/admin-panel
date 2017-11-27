"use strict";

Application.controller("sourcesController", function ($scope, $http, dateFactory, sourcesDatesService) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.sources = [];
    $scope.expenses = [];
    $scope.postbacks = [];
    $scope.showsourcesLoader = true;

    $scope.buyerDetails = false;
    $scope.dateDetails = false;
    $scope.buyerDetailsByDate = [];

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

    // $scope.loadsources = function () {
    //     var url = "/statistic";
    //     $scope.sources = [];
    //     $scope.showsourcesLoader = true;
    //     $http.post(url, $scope.getGridDetails()).then(function (response) {
    //         $scope.sources = response.data;
    //         $scope.showsourcesLoader = false;
    //     }, function () {
    //         $scope.showsourcesLoader = false;
    //         notify('ti-alert', 'Error occurred during loading buyer sources', 'danger');
    //     });
    // };

    $scope.initsources = function () {
        var url = "/statistic/buyers";
        $scope.sources = [];
        $scope.showsourcesLoader = true;
        $http.get(url).then(function (response) {
            $scope.sources = response.data;
            $scope.showsourcesLoader = false;
        }, function () {
            $scope.showsourcesLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer sources', 'danger');
        });
    };

    $scope.getDataDetails = function (buyerId, date) {
        var dateDetailsDataPromise = sourcesDatesService.getData(buyerId, date);
        dateDetailsDataPromise.then(function(result) {
            // this is only run after getData() resolves
            $scope.buyerDetailsByDate = result;
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

    $scope.buyerDate = "";

    $scope.showBuyerByDateDetailsColumn = function (id, date) {
        if ($scope.buyerDate === date) {
            $scope.dateDetails = false;
            $scope.buyerDate = "";
        }
        else {
            $scope.dateDetails = true;
            $scope.buyerDate = date;
        }

        $scope.getDataDetails(id, date);
    };
});


Application.factory('sourcesDatesService', function($http) {

    var getData = function(id, date) {

        // Angular $http() and then() both return promises themselves
        return $http({method:"GET", url:"/statistic/date?buyerId="+id+"&date="+date}).then(function(result){

            // What we return here is the data that will be accessible
            // to us after the promise resolves
            return result.data;
        });
    };


    return { getData: getData };
});




