'use strict';

Application.controller('buyerStatisticController', function ($scope, $http, transferService) {
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

    $scope.updateDateFilterByCurrentMonth = function () {
        var currentDate = new Date();
        var from = formatDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
        var to = formatDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 29));

        $scope.dt = {
            startDate: from,
            endDate: to
        };
    };

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

        if(transferService.getParam() === null){
            var validDate = $scope.validateDate();
            var from = validDate.from;
            var to = validDate.to;
        }
        else{
            $scope.updateDateFilterByCurrentMonth();
            transferService.setParam(null);
        }

        var url = '/buyer/spent/report?from=' + from + '&to=' + to
            + "&sources=" + $scope.selectedTypes + "&buyerIds=" + $scope.selectedBuyerNames;
        $http.get(url).then(function (response) {
            $scope.buyerCosts = response.data;
            $scope.showBuyerCostsLoader = false;
        }, function () {
            $scope.showBuyerCostsLoader = false;
            notify('ti-alert', 'Error occurred during loading buyer spent', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        var validDate = $scope.validateDate();
        var from = validDate.from;
        var to = validDate.to;

        return {
            'buyers': $scope.selectedBuyerNames,
            'types': $scope.selectedTypes,
            'from': from,
            'to': to
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

    $scope.validateDate = function () {
        var yearFrom = 2017;
        var yearTo = 2020;
        var f = new Date(yearFrom, 0, 1);
        var t = new Date(yearTo, 0, 1);

        var fromDate = f;
        var toDate = t;
        if($scope.dt.startDate !== null
            && $scope.dt.endDate !== null){
            fromDate = formatDate($scope.dt.startDate._d);
            toDate = formatDate($scope.dt.endDate._d);
        }

        return {
            from: formatDate(fromDate),
            to: formatDate(toDate)
        };
    };
});