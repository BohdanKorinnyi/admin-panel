"use strict";

Application.controller("statisticController", function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.types = [];
    $scope.selectedTypes = [];

    $scope.costs = [];
    $scope.showCostsLoader = true;

    $scope.buyerDetails = false;

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

    $scope.loadCosts = function () {
        var url = "/statistic/all";
        $scope.costs = [];
        $scope.showCostsLoader = true;
        $scope.getRole();
        if($scope.role === "BUYER"){
            $scope.hideBuyerSelect = true;
        }
        $http.post(url, $scope.getGridDetails()).then(function (response) {
            $scope.costs = response.data;
            $scope.showCostsLoader = false;
        }, function () {
            $scope.showCostsLoader = false;
            notify('ti-alert', 'Error occurred during loading sources', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        var fromDate = "";
        var toDate = "";
        if($scope.dt.startDate !== null
            && $scope.dt.endDate !== null){
            fromDate = formatDate($scope.dt.startDate._d);
            toDate = formatDate($scope.dt.endDate._d);
        }

        return {
            "buyers": $scope.selectedBuyerNames,
            "types": $scope.selectedTypes,
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


    $scope.getTypes = function () {
        var url = "/account/types";
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

    $scope.toFixedValue = function (value) {
        return value.toFixed(2);
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