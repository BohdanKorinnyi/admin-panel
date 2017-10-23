"use strict";

Application.controller("postbackController", function ($scope, $http, dateFactory) {

    $scope.postbacks = [];
    $scope.sortingDetails = {};

    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;

    $scope.dpFromDate = "";
    $scope.dpToDate = "";

    $scope.sortType = "";
    $scope.sortReverse = "";

    $scope.selectedDuplicateValue = "";
    $scope.selectedStatusFromOfferNameValue = "";
    $scope.selectedClickIdValue = "";
    $scope.selectedOfferNameValue = "";
    $scope.selectedTimeZoneValue = "";
    $scope.selectedPrefixValue = "";
    $scope.selectedStatusValue = "";
    $scope.selectedAdvertiserValue = [];
    $scope.selectedAfIdValue = "";
    $scope.selectedBuyerValue = [];

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.advertiserNames = [];
    $scope.selectedAdvertiserNames = [];

    $scope.statusValues = [
        {
            id: 0,
            name: "Approve"
        },
        {
            id: 1,
            name: "Hold"
        },
        {
            id: 2,
            name: "Decline"
        }];
    $scope.selectedStatusValue = [];
    $scope.selectedStatusForPostValue = [];

    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;

    $scope.dateOptions = {
        "Today": "today",
        "Yesterday": "yesterday",
        "Last 7 days": "lastWeek",
        "This Month": "thisMonth",
        "Last Month": "lastMonth",
        "Custom Range": "custom"
    };
    $scope.selectedDate = "today";

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    $scope.showData = function () {
        console.log($scope.selectedBuyerNames);
        console.log($scope.selectedStatusValue);
        console.log($scope.selectedAdvertiserNames);
    };

    $scope.changeOrder = function () {
        if ($scope.sortReverse === "") {
            $scope.sortReverse = "ASC";
        }
        else if ($scope.sortReverse === "ASC") {
            $scope.sortReverse = "DESC";
        }
        else if ($scope.sortReverse === "DESC") {
            $scope.sortReverse = "";
        }
    };

    $scope.loadData = function () {
        $scope.initData();
    };

    $scope.initAdvNames = function () {
        var url = "/advertiser/names";
        $http.get(url).then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.advertiserNames.push({
                    id: i,
                    name: response.data[i]
                });
            }
        });
    };

    $scope.initBuyerNames = function () {
        var url = "/buyer/names";
        $http.get(url).then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.buyerNames.push({
                    id: i,
                    name: response.data[i]
                });
            }
        });
    };

    $scope.initData = function () {
        var url = "postback/get";
        var parameters = {};

        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};
        parameters['sortingDetails'] = {};

        var parameter = JSON.stringify(parameters);

        var queryHeaders = new Headers();
        queryHeaders.append('Content-Type', 'application/json');

        $http.post(url, parameter, {headers: queryHeaders}).then(function successCallback(r) {
            $scope.postbacks = r.data.postbacks;
            $scope.totalPagination = r.data.size;
            $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
        }, function errorCallback(resp) {
        });
    };

    $scope.getData = function () {
        var url = "postback/get";
        var parameters = {};

        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};

        if ($scope.selectedDate === "custom") {
            parameters.filter["from"] = $scope.dpFromDate;
            parameters.filter["to"] = $scope.dpToDate;
        }
        else {
            parameters.filter["from"] = formatDate(dateFactory.pickDate($scope.selectedDate));
            parameters.filter["to"] = formatDate(new Date());
        }

        //$scope.buyerNames[i].id
        for (var i = 0; i < $scope.selectedAdvertiserNames.length; i++) {
            for (var j = 0; j < $scope.advertiserNames.length; j++) {
                if ($scope.selectedAdvertiserNames[i] == $scope.advertiserNames[j].id) {
                    $scope.selectedAdvertiserValue.push($scope.advertiserNames[j].name);
                }
            }
        }

        for (var i = 0; i < $scope.selectedBuyerNames.length; i++) {
            for (var j = 0; j < $scope.buyerNames.length; j++) {
                if ($scope.selectedBuyerNames[i] == $scope.buyerNames[j].id) {
                    $scope.selectedBuyerValue.push($scope.buyerNames[j].name);
                }
            }
        }

        for (var i = 0; i < $scope.selectedStatusValue.length; i++) {
            for (var j = 0; j < $scope.statusValues.length; j++) {
                if ($scope.selectedStatusValue[i] == $scope.statusValues[j].id) {
                    $scope.selectedStatusForPostValue.push($scope.statusValues[j].name);
                }
            }
        }

        parameters.filter["buyer"] = $scope.selectedBuyerValue.join();
        parameters.filter["advertiser"] = $scope.selectedAdvertiserValue.join();
        parameters.filter["status"] = $scope.selectedStatusForPostValue.join();
        parameters.filter["afid"] = $scope.selectedAfIdValue;
        parameters.filter["prefix"] = $scope.selectedPrefixValue;
        parameters.filter["offerName"] = $scope.selectedOfferNameValue;
        parameters.filter["clickId"] = $scope.selectedClickIdValue;
        parameters.filter["statusFromOfferName"] = $scope.selectedStatusFromOfferNameValue;
        parameters.filter["duplicate"] = $scope.selectedDuplicateValue;
        parameters['sortingDetails'] = {};


        var parameter = JSON.stringify(parameters);
        $http.post(url, parameter).then(function successCallback(r) {
            $scope.totalItems = r.data.size;
            $scope.postbacks = r.data.conversions;
            $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
        }, function errorCallback(resp) {
            console.error("Error!" + resp);
        });
    };

    $scope.initDataOnPagination = function () {
        var url = "postback/get";
        var parameters = {};

        $scope.postbacks = [];
        $scope.postbacks.length = 0;
        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};
        parameters['sortingDetails'] = {};

        var parameter = JSON.stringify(parameters);
        $http.post(url, parameter).then(function successCallback(r) {
            $scope.postbacks = r.data.postbacks;
            $scope.totalPagination = r.data.size;
            $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
        }, function errorCallback(resp) {
            console.error("Error!" + resp);
        });
    };

    $scope.getSorted = function () {
        var url = "postback/get";
        var parameters = {};

        $scope.changeOrder();

        $scope.postbacks = [];
        $scope.postbacks.length = 0;
        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};
        parameters['sortingDetails'] = {};

        if ($scope.sortReverse !== "") {
            parameters.sortingDetails["column"] = $scope.sortType;
            parameters.sortingDetails["order"] = $scope.sortReverse;
        }

        var parameter = JSON.stringify(parameters);
        $http.post(url, parameter).then(function successCallback(r) {
            $scope.postbacks = r.data.postbacks;
            $scope.totalPagination = r.data.size;
            $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
        }, function errorCallback(resp) {
            console.error("Error!" + resp);
        });
    };


});

Application.directive('selectWatcher', function ($timeout) {
    return {
        link: function (scope, element, attr) {
            var last = attr.last;
            if (last === "true") {
                $timeout(function () {
                    $(element).parent().selectpicker('val', 'any');
                    $(element).parent().selectpicker('refresh');
                });
            }
        }
    };
});