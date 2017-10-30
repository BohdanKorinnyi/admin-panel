'use strict';
Application.controller('conversionController', function ($scope, $http, dateFactory) {
    $scope.conversions = [];
    $scope.selectedSize = 50;
    $scope.sizeOptions = {50: 50, 100: 100, 500: 500};
    $scope.selectedDate = 'no-date';
    $scope.dateOptions = {
        'Select date': 'no-date',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Range': 'custom'
    };

    $scope.filterOptions = {
        'Select Filter': 'selectedFilter',
        'Buyer': 'arbitratorName',
        'AfId': 'arbitratorId',
        'Aff.network': 'advertiserName',
        'Status': 'status',
        'Prefix': 'prefix',
        'Offer Name': 'offerName'
    };

    $scope.selectedFilter = 'selectedFilter';
    $scope.selectedBuyerValue = '';
    $scope.selectedAfIdValue = '';
    $scope.selectedAffNetworkValue = '';
    $scope.selectedStatusValue = '';
    $scope.selectedPrefixValue = '';
    $scope.selectedOfferNameValue = '';
    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.dpFromDate = '';
    $scope.dpToDate = '';
    $scope.conversions = {};
    $scope.sortingDetails = {};

    $scope.sortType = '';
    $scope.sortReverse = '';

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

    $scope.loadConversions = function () {
        $scope.conversions = [];
        $scope.showConversionLoader = true;
        $http.post('grid/conversions/get', $scope.getConversionFilter()).then(function successCallback(response) {
            $scope.conversions = response.data.conversions;
            $scope.showConversionLoader = false;
            $scope.totalPagination = response.data.size;
            $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
        }, function errorCallback() {
            $scope.showConversionLoader = false;
            notify('ti-alert', 'Error occurred during loading conversions', 'danger');
        });
    };

    $scope.getConversionFilter = function () {
        var parameters = {};
        parameters.page = $scope.selectedPage;
        parameters.size = $scope.selectedSize;
        parameters['filter'] = {};
        if ($scope.selectedDate !== 'no-date') {
            if ($scope.selectedDate === 'custom') {
                parameters.filter['from'] = $scope.dpFromDate;
                parameters.filter['to'] = $scope.dpToDate;
            } else {
                parameters.filter['from'] = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                parameters.filter['to'] = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }
        if ($scope.selectedBuyerValue !== '') {
            parameters.filter['arbitratorName'] = $scope.selectedBuyerValue;
        }
        if ($scope.selectedAffNetworkValue !== '') {
            parameters.filter['advertiserName'] = $scope.selectedAffNetworkValue;
        }
        if ($scope.selectedAfIdValue !== '') {
            parameters.filter['arbitratorId'] = $scope.selectedAfIdValue;
        }
        if ($scope.selectedStatusValue !== '') {
            parameters.filter['status'] = $scope.selectedStatusValue;
        }
        if ($scope.selectedPrefixValue !== '') {
            parameters.filter['prefix'] = $scope.selectedPrefixValue;
        }
        if ($scope.selectedOfferNameValue !== '') {
            parameters.filter['offerName'] = $scope.selectedOfferNameValue;
        }
        if ($scope.sortReverse !== '') {
            parameters['sortingDetails'] = {};
            parameters.sortingDetails['column'] = $scope.sortType;
            parameters.sortingDetails['order'] = $scope.sortReverse;
        }
        return parameters;
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