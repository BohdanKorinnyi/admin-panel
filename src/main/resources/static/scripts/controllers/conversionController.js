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

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];

    $scope.utcValues = ["UTC","UTC+1","UTC+2","UTC+3","UTC+5","UTC+6","UTC+7","UTC+8","UTC+9",
        "UTC+10","UTC+11","UTC-3","UTC-4","UTC-5","UTC-6","UTC-7","UTC-8","UTC-9",
        "UTC-10","UTC-11"];
    $scope.selectedUtcValues =[];
    //TODO: move utc to filter

    $scope.buyerAfids = [];
    $scope.selectedBuyerAfids = [];

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

    $scope.loadAfids = function () {
      var url = "/affiliates/get";
      $http.get(url).then(function success(response) {
          $scope.buyerAfids = response.data;
      }, function errorCallback() {
          notify('ti-alert', 'Error occurred during loading buyers afids', 'danger');
      });
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
                parameters.filter['from'] = formatDate($scope.dpFromDate);
                parameters.filter['to'] = formatDate($scope.dpToDate);
            } else {
                parameters.filter['from'] = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                parameters.filter['to'] = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }
        if ($scope.selectedBuyerNames !== []) {
            parameters.filter['arbitratorName'] = $scope.selectedBuyerNames.join();
        }
        if ($scope.selectedAffNetworkValue !== '') {
            parameters.filter['advertiserName'] = $scope.selectedAffNetworkValue;
        }
        if ($scope.selectedBuyerAfids !== '') {
            parameters.filter['afids'] = $scope.selectedBuyerAfids.join();
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

    $scope.initBuyerNames = function () {
        var url = '/buyer/names';
        $http.get(url).then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.buyerNames.push({
                    id: i,
                    name: response.data[i]
                });
            }
        });
    };

    $scope.rowInfo = "";

    $scope.getRowInfo = function(id){
        var url = "/postback?conversionId=" + id;
        $http.get(url).then(function successCallback(response) {
            $scope.rowInfo = response.data;
        });
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