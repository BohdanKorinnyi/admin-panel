'use strict';

Application.controller('postbackController', function ($scope, $http, transferService) {
    $scope.postbacks = [];
    $scope.sortingDetails = {};
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;

    $scope.sortType = '';
    $scope.sortReverse = '';

    $scope.selectedDuplicateValue = '';
    $scope.selectedStatusFromOfferNameValue = '';
    $scope.selectedClickIdValue = '';
    $scope.selectedOfferNameValue = '';
    $scope.selectedTimeZoneValue = '';
    $scope.selectedPrefixValue = '';
    $scope.selectedStatusValue = '';
    $scope.selectedAdvertiserValue = [];
    $scope.selectedAfIdValue = '';
    $scope.selectedBuyerValue = [];

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.advertiserNames = [];
    $scope.selectedAdvertiserNames = [];

    $scope.statusValues = [
        {
            id: 0,
            name: 'Approved'
        },
        {
            id: 1,
            name: 'Hold'
        },
        {
            id: 2,
            name: 'Declined'
        }];
    $scope.selectedStatusValue = [];
    $scope.selectedStatusForPostValue = [];

    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;

    $scope.dpFromDate = new Date(new Date().getFullYear(), 0, 1);
    $scope.dpToDate = new Date(new Date().getFullYear() + 1, 0, 1);

    $scope.dt = {
        startDate: $scope.dpFromDate,
        endDate: $scope.dpToDate
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

    $scope.utcValues = {
        "UTC": "utc",
        "UTC+1": "utc+1", "UTC+2": "utc+2", "UTC+3": "utc+3",
        "UTC+4": "utc+4", "UTC+5": "utc+5", "UTC+6": "utc+6", "UTC+7": "utc+7",
        "UTC+8": "utc+8", "UTC+9": "utc+9",
        "UTC+10": "utc+10", "UTC+11": "utc+11", "UTC+12": "utc+12",
        "UTC-1": "utc-1", "UTC-2": "utc-2",
        "UTC-3": "utc-3", "UTC-4": "utc-4", "UTC-5": "utc-5",
        "UTC-6": "utc-6", "UTC-7": "utc-7", "UTC-8": "utc-8", "UTC-9": "utc-9",
        "UTC-10": "utc-10", "UTC-11": "utc-11", "UTC-12": "utc-12",
    };

    $scope.selectedUtc = "utc";

    $scope.hideBuyerSelect = false;

    $scope.export = function () {
        notify('ti-alert', 'Postback export in development', 'info');
    };

    $scope.updateFilterByCurrentMonth = function () {
        var currentDate = new Date();
        $scope.dpFromDate = formatDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
        $scope.dpToDate = formatDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 29));
        $scope.dt = {
            startDate: $scope.dpFromDate,
            endDate: $scope.dpToDate
        };
    };

    $scope.getPostbackId = function (postbackId) {
        $http.get('postback/fullurl?id=' + postbackId).then(function successCallback(response) {
            if (response.status === 204) {
                notify('ti-alert', 'Full Url doesn\'t found for this postback', 'into');
            } else {
                alertify.alert(response.data.fullurl);
            }
        }, function failCallback(response) {
            notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
        });
    };
    $scope.changeOrder = function () {
        if ($scope.sortReverse === '') {
            $scope.sortReverse = 'ASC';
        }
        else if ($scope.sortReverse === 'ASC') {
            $scope.sortReverse = 'DESC';
        }
        else if ($scope.sortReverse === 'DESC') {
            $scope.sortReverse = '';
        }
    };
    $scope.initAdvNames = function () {
        var url = '/advertiser/names';
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
        var url = '/buyer';
        $http.get(url).then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.buyerNames.push({
                    id: response.data[i].id,
                    name: response.data[i].name
                });
            }
        });
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

    $scope.loadPostbacks = function () {
        $scope.postbacks = [];
        $scope.showLoader = true;
        $scope.getRole();
        if ($scope.role === "BUYER") {
            $scope.hideBuyerSelect = true;
        }
        $http.post('grid/postback/get', $scope.getFilterParameters())
            .then(function successCallback(response) {
                $scope.postbacks = response.data.postbacks;
                $scope.showLoader = false;
                $scope.totalPagination = response.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(response) {
                $scope.showLoader = false;
                notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
            });
    };

    $scope.formatFromToDate = function () {
        $scope.dpFromDate = formatDate($scope.dt.startDate._d);
        $scope.dpToDate = formatDate($scope.dt.endDate._d);
    };

    $scope.apply = function () {
        $scope.postbacks = [];
        $scope.showLoader = true;

        var postParams = $scope.getFilterParameters();
        $scope.formatFromToDate();
        postParams.filter.from = $scope.dpFromDate;
        postParams.filter.to = $scope.dpToDate;

        $http.post('grid/postback/get', postParams)
            .then(function successCallback(response) {
                $scope.postbacks = response.data.postbacks;
                $scope.showLoader = false;
                $scope.totalPagination = response.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(response) {
                $scope.showLoader = false;
                notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
            });
    };


    $scope.getFilterParameters = function () {
        var parameters = {};
        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};

        if(transferService.getParam() === null){
            parameters.filter['from'] = formatDate($scope.dpFromDate);
            parameters.filter['to'] = formatDate($scope.dpToDate);
        }
        else{
            $scope.updateFilterByCurrentMonth();
            parameters.filter['from'] = formatDate($scope.dpFromDate);
            parameters.filter['to'] = formatDate($scope.dpToDate);
            transferService.setParam(null);
        }

        $scope.selectedAdvertiserValue = getSelectedValues($scope.selectedAdvertiserNames, $scope.advertiserNames);
        $scope.selectedBuyerValue = getSelectedValues($scope.selectedBuyerNames, $scope.buyerNames);
        $scope.selectedStatusForPostValue = getSelectedValues($scope.selectedStatusValue, $scope.statusValues);
        if ($scope.selectedBuyerValue.join() !== '') {
            parameters.filter['buyer'] = $scope.selectedBuyerValue.join();
        }
        if ($scope.selectedAdvertiserValue.join() !== '') {
            parameters.filter['advertiser'] = $scope.selectedAdvertiserValue.join();
        }
        if ($scope.selectedStatusForPostValue.join() !== '') {
            parameters.filter['status'] = $scope.selectedStatusForPostValue.join();
        }
        if ($scope.selectedUtc !== 'utc') {
            parameters.filter['timezone'] = $scope.selectedUtc;
        }
        if ($scope.selectedAfIdValue !== '') {
            parameters.filter['afid'] = $scope.selectedAfIdValue;
        }
        if ($scope.selectedAfIdValue !== '') {
            parameters.filter['afid'] = $scope.selectedAfIdValue;
        }
        if ($scope.selectedPrefixValue !== '') {
            parameters.filter['prefix'] = $scope.selectedPrefixValue;
        }
        if ($scope.selectedOfferNameValue !== '') {
            parameters.filter['offerName'] = $scope.selectedOfferNameValue;
        }
        if ($scope.selectedClickIdValue !== '') {
            parameters.filter['clickId'] = $scope.selectedClickIdValue;
        }
        if ($scope.selectedStatusFromOfferNameValue !== '') {
            parameters.filter['statusFromOfferName'] = $scope.selectedStatusFromOfferNameValue;
        }
        if ($scope.selectedDuplicateValue !== '') {
            parameters.filter['duplicate'] = $scope.selectedDuplicateValue;
        }
        if ($scope.sortReverse !== '') {
            parameters['sortingDetails'] = {};
            parameters.sortingDetails['column'] = $scope.sortType;
            parameters.sortingDetails['order'] = $scope.sortReverse;
        }
        return parameters;
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

Application.directive('selectWatcher', function ($timeout) {
    return {
        link: function (scope, element, attr) {
            var last = attr.last;
            if (last === 'true') {
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

function notify(icon, message, type) {
    $.notify({
        icon: icon,
        message: message
    }, {
        type: type,
        timer: 3000,
        placement: {
            from: 'top',
            align: 'right'
        }
    });
}