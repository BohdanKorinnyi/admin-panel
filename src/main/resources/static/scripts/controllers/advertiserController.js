"use strict";

Application.controller("advertiserController", function ($scope, $http) {

    $scope.advertiserApiKey = "";
    $scope.advertiserUrl = "";
    $scope.advertiserName = "";
    $scope.advertiserRisk = "";
    $scope.advertiserShortName = "";
    $scope.advertiserSecretName = "";
    $scope.advertiserId = 0;
    $scope.currentStatusIndex = [];
    $scope.selectedAdvertiserIndex = -1;
    $scope.previousAdvertiserIndex = -1;

    $scope.advertisers = [];
    $scope.advertiserStatuses = [];

    $scope.advertiserRealStatuses = ["approved", "hold", "declined"];

    $scope.statusesForUser = [];
    $scope.statusesForUser['statuses'] = {};
    $scope.statusesForUser.statuses['status'] = [];
    $scope.statusesForUser.statuses['realStatus'] = [];
    $scope.statusesForUser.statuses['type'] = [];

    $scope.advertisersStatuses = [];
    $scope.role = "";
    $scope.disableButtons = false;


    // functions
    $scope.getRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);  // `false` makes the request synchronous
        request.send(null);

        if (request.status === 200) {
            var z = JSON.parse(request.response);
            $scope.role = z.authorities[0].authority;
            if($scope.role === "BUYER"){
                $scope.disableButtons = true;
            }
            else{
                $scope.disableButtons = false;
            }
        }
    };


    $scope.loadData = function () {
        $scope.initData();
    };

    $scope.initData = function () {
        var url = "advertiser/all";
        $http.get(url).then(function successCallback(response) {
            $scope.advertisers = response.data;
        });
    };

    $scope.onAdvertiserClick = function (advertiser, index) {
        if ($scope.selectedAdvertiserIndex === -1) {
            if($scope.role === "BUYER"){
                $scope.selectedAdvertiserIndex = -1;
            }
            else{
                $scope.selectedAdvertiserIndex = index;
                $scope.previousAdvertiserIndex = index;
            }
        }
        $scope.previousAdvertiserIndex = $scope.selectedAdvertiserIndex;
        $scope.selectedAdvertiserIndex = index;

        $scope.getAdvertiserStatusesById(advertiser.id, index);
        $scope.advertiserUrl = advertiser.url;
        $scope.advertiserName = advertiser.advname;
        $scope.advertiserRisk = advertiser.risk;
        $scope.advertiserShortName = advertiser.advshortname;
        $scope.advertiserSecretName = advertiser.secretKey;
        $scope.advertiserApiKey = advertiser.apiKey;
    };

    $scope.getAdvertiserStatusesById = function (advertiserId, advertiserIndex) {
        if (advertiserIndex in $scope.advertisersStatuses) {
            $scope.advertisersStatuses[$scope.previousAdvertiserIndex] = $scope.advertiserStatuses;
            $scope.advertiserStatuses = $scope.advertisersStatuses[advertiserIndex];
            return;
        }
        if (advertiserId === null) {
            $scope.advertiserStatuses = [];
            return;
        }
        $http.get("advertiser/status/get?advertiserId=" + advertiserId)
            .then(function successCallback(response) {
                $scope.advertiserStatuses = response.data;
                $scope.advertisersStatuses[advertiserIndex] = response.data;
            });
    };

    $scope.applyClick = function () {
        var parameters = [];
        for (var i = 0; i < $scope.advertisers.length; i++) {
            if (i in $scope.advertisersStatuses) {
                parameters.push($scope.advertisers[i]);
                parameters[parameters.length - 1].statuses = $scope.advertisersStatuses[i];
            }
        }
        $scope.advertisersStatuses = [];
        $http.put("advertiser/update", parameters).then(function successCallback() {
            notify('ti-alert', 'Updated successfully', 'success');
            $scope.cancelAdvertisersData();
        }, function errorCallback() {
            notify('ti-alert', 'Update failed', 'danger')
            $scope.cancelAdvertisersData();
        });
        $scope.advertiserStatuses = [];
        $scope.selectedAdvertiserIndex = -1;
        $scope.previousAdvertiserIndex = -1;
    };

    $scope.updateAdvertiserName = function () {
        $scope.advertisers[$scope.selectedAdvertiserIndex].advname = $scope.advertiserName;
    };
    $scope.updateAdvertiserSecretName = function () {
        $scope.advertisers[$scope.selectedAdvertiserIndex].secretKey = $scope.advertiserSecretName;
    };
    $scope.updateAdvertiserUrl = function () {
        $scope.advertisers[$scope.selectedAdvertiserIndex].url = $scope.advertiserUrl;
    };
    $scope.updateAdvertiserRisk = function () {
        $scope.advertisers[$scope.selectedAdvertiserIndex].risk = $scope.advertiserRisk;
    };
    $scope.updateAdvertiserShortName = function () {
        $scope.advertisers[$scope.selectedAdvertiserIndex].advshortname = $scope.advertiserShortName;
    };
    $scope.updateAdvertiserApiKey = function () {
        $scope.advertisers[$scope.selectedAdvertiserIndex].apiKey = $scope.advertiserApiKey;
    };



    $scope.addStatusClick = function () {
        $scope.advertiserStatuses.push({name: null, realStatus: null, type: null});
    };

    $scope.chckedIndexs = [];

    $scope.checkedIndex = function (student) {
        if ($scope.chckedIndexs.indexOf(student) === -1) {
            $scope.chckedIndexs.push(student);
        }
        else {
            $scope.chckedIndexs.splice($scope.chckedIndexs.indexOf(student), 1);
        }
    };

    $scope.removeStatus = function () {
        for (var i = 0; i < $scope.currentStatusIndex.length; i++) {
            $scope.advertiserStatuses.splice($scope.currentStatusIndex[i], 1);
        }
    };
    $scope.isSelected = function (index) {
        $scope.currentStatusIndex.push(index);

    };
    $scope.cancelAdvertisersData = function () {
        $scope.advertiserUrl = '';
        $scope.advertiserName = '';
        $scope.advertiserRisk = '';
        $scope.advertiserShortName = '';
        $scope.advertiserSecretName = '';
        $scope.advertiserApiKey = '';
        $http.get("advertiser/all").then(function successCallback(response) {
            $scope.advertisers = response.data;
        });
        $scope.advertiserStatuses = [];
        $scope.selectedAdvertiserIndex = -1;
        $scope.previousAdvertiserIndex = -1;
    };
    $scope.addAdvertiser = function () {
        $scope.advertisers.push({
            id: null,
            advname: 'Default name',
            advshortname: '',
            secretKey: '',
            risk: '',
            url: '',
            apiKey: ''
        });
        $scope.advertisersStatuses[$scope.advertisers.length - 1] = [];
    };
});