"use strict";

Application.controller("costManagementController", function ($scope, $http, dateFactory) {

    $scope.editedRows = [];
    $scope.addedRows = [];
    $scope.selectedRowId = '';
    $scope.deletedRows = [];
    $scope.costs = [];

    $scope.buyerNames = [];
    $scope.selectedBuyerNames = [];
    $scope.selectedBuyerName = "";

    $scope.types = [];
    $scope.selectedTypes = [];

    $scope.selectedSize = 50;

    $scope.dateInterval = {
        'All time': 'allTime',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth',
        'Custom Range': 'custom'
    };
    $scope.selectedInterval = 'allTime';
    $scope.dpFromDate = "";
    $scope.dpToDate = "";

    $scope.hideBuyerSelect = false;
    $scope.showCostManagementLoader = false;
    $scope.searchFilter = "";

    $scope.tableSpent = "";

    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;

    //functions
    $scope.getRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);  // `false` makes the request synchronous
        request.send(null);

        if (request.status === 200) {
            var z = JSON.parse(request.response);
            $scope.role = z.authorities[0].authority;
        }
    };


    $scope.loadCosts = function () {
        $scope.costs = [];
        $scope.showCostManagementLoader = true;
        $scope.getRole();
        if($scope.role === "BUYER"){
            $scope.hideBuyerSelect = true;
        }

        var url = "/expenses?buyerIds="+ $scope.getFilterDetails();

        $http.post(url, $scope.getSizeAndNumberFilter())
            .then(function successCallback(response) {
                $scope.costs = response.data.data;
                for(var i = 0; i<$scope.costs.length; i++) {
                    for (var j = 0; j < $scope.buyerNames.length; j++) {
                        if ($scope.costs[i].buyerId === $scope.buyerNames[j].id) {
                            $scope.costs[i].buyer = $scope.buyerNames[j].name;
                        }
                    }
                }
                $scope.showCostManagementLoader = false;
                $scope.totalPagination = response.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(response) {
                $scope.showCostManagementLoader = false;
                notify('ti-alert', 'Error occurred during loading postbacks', 'danger');
            });
    };


    $scope.onApplyClick = function () {
        $scope.findAddedRows();
        $scope.findNotAddedRows();

        if($scope.deletedRows.length !== 0){
            var deleteUrl = "/expenses?expensesIds=" + $scope.deletedRows.join();
            $http.delete(deleteUrl).then(function success() {
                $scope.loadCosts();
                notify('ti-alert', 'Deleted successful', 'success');
            }, function errorCallback(response) {
                $scope.showCostManagementLoader = false;
                notify('ti-alert', 'Error occurred during deleting costs', 'danger');
            });
        }

        var putUrl = "/expenses";
        $http.put(putUrl, $scope.editedRows).then(function success() {
            $scope.loadCosts();
        }, function errorCallback(response) {
            $scope.showCostManagementLoader = false;
            notify('ti-alert', 'Error occurred during editing costs', 'danger');
        });

        if($scope.addedRows.length !== 0){
            var saveUrl = "/expenses/save";
            $http.post(saveUrl, $scope.addedRows).then(function success() {
                $scope.loadCosts();
                notify('ti-alert', 'Saving successful', 'success');
            }, function errorCallback(response) {
                $scope.showCostManagementLoader = false;
                notify('ti-alert', 'Error occurred during saving costs', 'danger');
            });
        }

    };

    $scope.addCost = function () {
        $scope.costs.unshift({
            buyer: null, buyerId: null, create: null,
            date: formatDate(new Date()), description: null,
            id: null, name: null, sum: null, typeId: null, update: null
        });
    };


    $scope.getFilterDetails = function () {
        var fromDate = "";
        var toDate = "";
        if ($scope.selectedDate !== 'allTime') {
            if ($scope.selectedDate === 'custom') {
                fromDate = $scope.dpFromDate;
                toDate = $scope.dpToDate;
            }
            else {
                fromDate = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
                toDate = formatDate(dateFactory.pickDateTo($scope.selectedDate));
            }
        }

        return $scope.selectedBuyerNames+"&expensesType="+$scope.selectedTypes+"&from="+fromDate+"&to="+toDate;
    };

    $scope.getSizeAndNumberFilter = function () {
        var parameters = {};
        parameters.size = $scope.selectedSize;
        parameters.number = $scope.selectedPage;

        return parameters;
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
        var url = "expenses/type/all";
        $http.get(url).then(function success(response) {
            $scope.types = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading types', 'danger');
        });
    };

    $scope.findAddedRows = function () {
        for(var i=0; i<$scope.costs.length; i++){
            if($scope.costs[i].id === null){
                for(var j=0; j<$scope.buyerNames.length; j++){
                    if($scope.buyerNames[j].name === $scope.costs[i].buyer){
                        $scope.costs[i].buyerId = $scope.buyerNames[j].id;
                    }
                }

                for(var f=0; f<$scope.types.length; f++){
                    if($scope.types[f].name === $scope.costs[i].name){
                        $scope.costs[i].typeId = $scope.types[f].id;
                    }
                }
                $scope.addedRows.push($scope.costs[i]);
            }
        }
    };

    $scope.findNotAddedRows = function () {
        for(var i=0; i<$scope.costs.length; i++){
            if($scope.costs[i].id !== null){
                $scope.editedRows.push($scope.costs[i]);
            }
        }
    };


    $scope.selectRow = function (id) {
       $scope.selectedRowId = id;
    };

    $scope.deleteRow = function(){
        $scope.deletedRows.push($scope.selectedRowId);
        for(var i=0; i<$scope.costs.length; i++){
            if($scope.selectedRowId === $scope.costs[i].id){
                $scope.costs.splice($scope.costs[i], 1);
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


