"use strict";

Application.controller("buyerStatisticController", function ($scope, $http, dateFactory) {

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.types = [];
    $scope.selectedTypes = [];

    $scope.buyerCosts = [];
    $scope.showBuyerCostsLoader = true;

    $scope.dateDetails = false;
    $scope.sourcesDetails = false;
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
        request.open('GET', '/user/me', false);
        request.send(null);

        if (request.status === 200) {
            var z = JSON.parse(request.response);
            $scope.role = z.authorities[0].authority;
        }
    };

    $scope.loadBuyerCosts = function () {
        var url = "/buyer/spent/report?from=&to=";
        $scope.buyerCosts = [];
        $scope.showBuyerCostsLoader = true;
        $scope.getRole();
        if($scope.role === "BUYER"){
            $scope.hideBuyerSelect = true;
        }
        $http.get(url).then(function (response) {
            $scope.buyerCosts = $scope.formatSources(response.data);
            $scope.showBuyerCostsLoader = false;
        }, function () {
            $scope.showBuyerCostsLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer costs', 'danger');
        });
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


    $scope.currentDateDetails = "";

    $scope.showDateDetailsColumn = function (date) {
        if ($scope.currentDateDetails === date) {
            $scope.dateDetails = false;
            $scope.currentDateDetails = "";
        }
        else {
            $scope.dateDetails = true;
            $scope.currentDateDetails = date;
        }

        $scope.currentBuyerUUID = "";
        $scope.currentSourceUuid = "";
    };

    $scope.currentSourceUuid = "";

    $scope.showSourcesDetailsColumn = function (uuid) {
        if ($scope.currentSourceUuid === uuid) {
            $scope.sourcesDetails = false;
            $scope.currentSourceUuid = "";
        }
        else {
            $scope.sourcesDetails = true;
            $scope.currentSourceUuid = uuid;
        }

        $scope.currentBuyerUUID = "";
    };

    $scope.currentBuyerUUID = "";

    $scope.showBuyerDetailsColumn = function (uuid) {
        if ($scope.currentBuyerUUID === uuid) {
            $scope.buyerDetails = false;
            $scope.currentBuyerUUID = "";
        }
        else {
            $scope.buyerDetails = true;
            $scope.currentBuyerUUID = uuid;
        }
    };

    $scope.formatSources = function (data) {
        for(var i = 0; i<data.length; i++){
            for(var j = 0; j<data[i].sources.length; j++){
                data[i].sources[j]['sourceId'] = createUUID();
                for(var f = 0; f<data[i].sources[j].costs.length; f++){
                    data[i].sources[j].costs[f]['buyerUUID'] = createUUID();
                }
            }
        }
        return data;
    };

    function createUUID() {
        var s = [];
        var hexDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (var i = 0; i < 32; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[12] = "4";
        s[16] = hexDigits.substr((s[16] & 0x3) | 0x8, 1);

        var uuid = s.join("");
        return uuid;
    }
});