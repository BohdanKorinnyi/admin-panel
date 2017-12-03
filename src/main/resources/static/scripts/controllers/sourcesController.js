"use strict";

Application.controller("sourcesController", function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];


    $scope.sources = [];
    $scope.expenses = [];
    $scope.postbacks = [];
    $scope.showsourcesLoader = true;
    $scope.hideBuyerSelect = false;
    $scope.role = "";

    $scope.buyerDetails = false;
    $scope.dateDetails = true;
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
    };

    $scope.getRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);  // `false` makes the request synchronous
        request.send(null);

        if (request.status === 200) {
            var z = JSON.parse(request.response);
            $scope.role = z.authorities[0].authority;
            //localStorage.setItem('role', $scope.role);
        }
    };

    $scope.initsources = function () {
        var url = "/statistic/buyers";
        $scope.getRole();
        $scope.sources = [];
        $scope.showsourcesLoader = true;
        if ($scope.role === "BUYER") {
            $scope.hideBuyerSelect = true;
        }
        $http.get(url).then(function (response) {
            $scope.sources = response.data;
            $scope.showsourcesLoader = false;
        }, function () {
            $scope.showsourcesLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer sources', 'danger');
        });
    };

    $scope.applySources = function () {
        var url = "/statistic/buyers?buyerIds="+ $scope.getGridDetails();
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
        var url = "/statistic/date?buyerId=" + buyerId + "&date=" + date;
        var request = new XMLHttpRequest();
        request.open('GET', url, false);
        request.send(null);
        if (request.status === 200) {
            var z = JSON.parse(request.response);
            for (var i = 0; i < $scope.sources.length; i++) {
                if($scope.sources[i].buyerId === buyerId){
                    for(var j = 0; j<$scope.sources[i].data.length; j++){
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
        var fromDate = "";
        var toDate = "";
        if ($scope.selectedDate !== 'no-date') {
            if ($scope.selectedDate === 'custom') {
                fromDate = formatDate($scope.dpFromDate);
                toDate = formatDate($scope.dpToDate);
            }
            else {
                fromDate = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                toDate = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }

        return $scope.selectedBuyerNames+"&from="+fromDate+"&to="+toDate;
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
            $scope.getDataDetails(id, date);
            $scope.dateDetails = true;
            $scope.buyerDate = date;
        }
    };
});




