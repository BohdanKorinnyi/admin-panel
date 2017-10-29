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
        "Select Date": "no-date",
        "Today": "today",
        "Yesterday": "yesterday",
        "Last 7 days": "lastWeek",
        "This Month": "thisMonth",
        "Last Month": "lastMonth",
        "Custom Range": "custom"
    };
    $scope.selectedDate = "no-date";

    $scope.getPostbackId = function (id) {
        console.log(id);
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

    $scope.loadPostbacks = function () {
        $scope.postbacks = [];
        $scope.showLoader = true;
        $http.post('grid/postback/get', $scope.getFilterParameters())
            .then(function successCallback(response) {
                $scope.postbacks = response.data.postbacks;
                $scope.showLoader = false;
                $scope.totalPagination = response.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(response) {
                $scope.showLoader = false;
                console.log('error', response);
            });
    };

    $scope.getFilterParameters = function () {
        var parameters = {};
        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};

        if ($scope.selectedDate !== 'no-date') {
            if ($scope.selectedDate === "custom") {
                parameters.filter["from"] = $scope.dpFromDate;
                parameters.filter["to"] = $scope.dpToDate;
            }
            else {
                parameters.filter["from"] = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                parameters.filter["to"] = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }
        $scope.selectedAdvertiserValue = getSelectedValues($scope.selectedAdvertiserNames, $scope.advertiserNames);
        $scope.selectedBuyerValue = getSelectedValues($scope.selectedBuyerNames, $scope.buyerNames);
        $scope.selectedStatusForPostValue = getSelectedValues($scope.selectedStatusValue, $scope.statusValues);
        if ($scope.selectedBuyerValue.join() !== '') {
            parameters.filter["buyer"] = $scope.selectedBuyerValue.join();
        }
        if ($scope.selectedAdvertiserValue.join() !== '') {
            parameters.filter["advertiser"] = $scope.selectedAdvertiserValue.join();
        }
        if ($scope.selectedStatusForPostValue.join() !== '') {
            parameters.filter["status"] = $scope.selectedStatusForPostValue.join();
        }
        if ($scope.selectedAfIdValue !== '') {
            parameters.filter["afid"] = $scope.selectedAfIdValue;
        }
        if ($scope.selectedAfIdValue !== '') {
            parameters.filter["afid"] = $scope.selectedAfIdValue;
        }
        if ($scope.selectedPrefixValue !== '') {
            parameters.filter["prefix"] = $scope.selectedPrefixValue;
        }
        if ($scope.selectedOfferNameValue !== '') {
            parameters.filter["offerName"] = $scope.selectedOfferNameValue;
        }
        if ($scope.selectedClickIdValue !== '') {
            parameters.filter["clickId"] = $scope.selectedClickIdValue;
        }
        if ($scope.selectedStatusFromOfferNameValue !== '') {
            parameters.filter["statusFromOfferName"] = $scope.selectedStatusFromOfferNameValue;
        }
        if ($scope.selectedDuplicateValue !== '') {
            parameters.filter["duplicate"] = $scope.selectedDuplicateValue;
        }
        if ($scope.sortReverse !== '') {
            parameters['sortingDetails'] = {};
            parameters.sortingDetails["column"] = $scope.sortType;
            parameters.sortingDetails["order"] = $scope.sortReverse;
        }
        return parameters;
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

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

function getSelectedValues(selected, allValues) {
    var returnType = [];
    for (var i = 0; i < selected.length; i++) {
        for (var j = 0; j < allValues.length; j++) {
            if (selected[i] == allValues[j].id) {
                returnType.push(allValues[j].name);
            }
        }
    }
    return returnType;
}