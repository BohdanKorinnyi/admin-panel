"use strict";

Application.controller("conversionController", function ($scope, $http, dateFactory) {

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

        $scope.filterOptions = {
            "Select Filter": "selectedFilter",
            "Buyer": "arbitratorName",
            "AfId": "arbitratorId",
            "Aff.network": "advertiserName",
            "Status": "status",
            "Prefix": "prefix",
            "Offer Name": "offerName"
        };

        $scope.selectedFilter = "selectedFilter";
        $scope.selectedBuyerValue = "";
        $scope.selectedAfIdValue = "";
        $scope.selectedAffNetworkValue = "";
        $scope.selectedStatusValue = "";
        $scope.selectedPrefixValue = "";
        $scope.selectedOfferNameValue = "";

        $scope.selectedPage = 1;
        $scope.totalPagination = 1;
        $scope.noOfPages = 1;


        $scope.dpFromDate = "";
        $scope.dpToDate = "";
        $scope.conversions = {};
        $scope.sortingDetails = {};

        $scope.sortType = "";
        $scope.sortReverse = "";

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

        $scope.cities = [];

        $scope.loadData = function () {
            $scope.initData();
        };


        $scope.getData = function () {
            var url = "grid/conversions/get";
            var parameters = {};

            parameters.page = $scope.selectedPage;
            parameters.size = $scope.selectedSize;
            parameters['filter'] = {};

            if($scope.selectedDate === "custom"){
                parameters.filter["from"] = $scope.dpFromDate;
                parameters.filter["to"] = $scope.dpToDate;
            }
            else {
                parameters.filter["from"] = formatDate(dateFactory.pickDate($scope.selectedDate));
                parameters.filter["to"] = formatDate(new Date());
            }

            // if($scope.selectedFilter !== "selectedFilter"){
            //     parameters.filter[$scope.selectedFilter] = $scope.selectedFilterValue;
            // }

            parameters.filter["arbitratorName"] = $scope.selectedBuyerValue;
            parameters.filter["advertiserName"] = $scope.selectedAffNetworkValue;
            parameters.filter["arbitratorId"] = $scope.selectedAfIdValue;
            parameters.filter["status"] = $scope.selectedStatusValue;
            parameters.filter["prefix"] = $scope.selectedPrefixValue;
            parameters.filter["offerName"] = $scope.selectedOfferNameValue;
            parameters['sortingDetails'] = {};

            var parameter = JSON.stringify(parameters);
            $http.post(url, parameter).then(function successCallback(r) {
                $scope.totalItems = r.data.size;
                $scope.cities = r.data.conversions;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(resp) {
                console.error("Error!" + resp);
            });
        };

        $scope.initData = function () {
            var url = "grid/conversions/get";
            var parameters = {};

            parameters.page = $scope.selectedPage;
            parameters.size = $scope.selectedSize;
            parameters['filter'] = {};
            parameters['sortingDetails'] = {};

            var parameter = JSON.stringify(parameters);
            $http.post(url, parameter).then(function successCallback(r) {
                $scope.cities = r.data.conversions;
                $scope.totalPagination = r.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(resp) {
                console.error("Error!" + resp);
            });
        };

        $scope.initDataOnPagination = function () {
            var url = "grid/conversions/get";
            var parameters = {};

            $scope.cities = [];
            $scope.cities.length = 0;
            parameters.page = $scope.selectedPage;
            parameters.size = $scope.selectedSize;
            parameters['filter'] = {};
            parameters['sortingDetails'] = {};

            var parameter = JSON.stringify(parameters);
            $http.post(url, parameter).then(function successCallback(r) {
                $scope.cities = r.data.conversions;
                $scope.totalPagination = r.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(resp) {
                console.error("Error!" + resp);
            });
        };

        $scope.getSorted = function () {
            var url = "grid/conversions/get";
            var parameters = {};

            $scope.changeOrder();

            $scope.cities = [];
            $scope.cities.length = 0;
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
                $scope.cities = r.data.conversions;
                $scope.totalPagination = r.data.size;
                $scope.noOfPages = Math.ceil($scope.totalPagination / $scope.selectedSize);
            }, function errorCallback(resp) {
                console.error("Error!" + resp);
            });
        };
    }
);