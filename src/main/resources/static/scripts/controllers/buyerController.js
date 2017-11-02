"use strict";

Application.controller("buyerController", function ($scope, $http) {

    $scope.buyers = [];

    $scope.buyersAfids = [];

    $scope.name = "";
    $scope.planProfit = 0;
    $scope.planProfitOld = 0;
    $scope.planRev = 0;
    $scope.planRevOld = 0;
    $scope.type = "";
    $scope.comment = '';

    $scope.selectedBuyerIndex = -1;
    $scope.previousBuyerIndex = -1;

    // functions
    $scope.loadData = function () {
        $scope.initData();
    };

    $scope.initData = function () {
        var url = "/buyer";
        $http.get(url).then(function success(response) {
            $scope.buyers = response.data;
            console.log($scope.buyers);
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };

    $scope.buyersToSave = [];

    $scope.applyClick = function(){
        for(var i = 0; i < $scope.buyers.length; i++) {
            if ($scope.buyers[i].id == null) {
                $scope.buyersToSave.push($scope.buyers[i]);
            }
        }
        if($scope.buyersToSave.length !== 0){
            $http.post("/buyer/save", $scope.buyersToSave).then(function success(response) {
                $scope.initData();
                swal(
                    'Updated successfully',
                    'Buyers have updated!',
                    'success'
                );
                $scope.cancelBuyersData();
            }, function errorCallback() {
                swal(
                    'Update failed',
                    'Error occurred during saving data',
                    'error'
                );
                $scope.cancelBuyersData();
            });
        }
        else {
            $http.put("/buyer/update", $scope.buyers).then(function success(response){
                $scope.initData();
                swal(
                    'Updated successfully',
                    'Buyers have updated!',
                    'success'
                );
                $scope.cancelBuyersData();
            }, function errorCallback() {
                swal(
                    'Update failed',
                    'Error occurred during saving data',
                    'error'
                );
                $scope.cancelBuyersData();
            });
        }
    };

    $scope.onBuyerClick = function (buyer, index) {
        if ($scope.selectedBuyerIndex === -1) {
            $scope.selectedBuyerIndex = index;
            $scope.previousBuyerIndex = index;
        }
        $scope.previousBuyerIndex = $scope.selectedBuyerIndex;
        $scope.selectedBuyerIndex = index;

        //$scope.getBuyerAfidById(buyer.id, index);
        $scope.name = buyer.name;
        $scope.planProfit = buyer.planProfit;
        $scope.planProfitOld = buyer.planProfitOld;
        $scope.planRev = buyer.planRev;
        $scope.planRevOld = buyer.planRevOld;
        $scope.type = buyer.type;
        $scope.comment = buyer.comment;
    };

    $scope.updateBuyerName= function () {
        $scope.buyers[$scope.selectedBuyerIndex].name = $scope.name;
    };
    $scope.updateBuyerPlanProfit = function () {
        $scope.buyers[$scope.selectedBuyerIndex].planProfit = $scope.planProfit;
    };
    $scope.updateBuyerPlanProfitOld = function () {
        $scope.buyers[$scope.selectedBuyerIndex].planProfitOld = $scope.planProfitOld;
    };
    $scope.updateBuyerPlanRev = function () {
        $scope.buyers[$scope.selectedBuyerIndex].planRev = $scope.planRev;
    };
    $scope.updateBuyerPlanRevOld = function () {
        $scope.buyers[$scope.selectedBuyerIndex].planRevOld = $scope.planRevOld;
    };
    $scope.updateBuyerType = function () {
        $scope.buyers[$scope.selectedBuyerIndex].type = $scope.type;
    };
    $scope.updateBuyerComment = function () {
        $scope.buyers[$scope.selectedBuyerIndex].comment = $scope.comment;
    };

    $scope.addBuyer = function () {
        $scope.buyers.push({
            id: null,
            name: 'Default name',
            planProfit: 0,
            planProfitOld: 0,
            planRev: 0,
            planRevOld: 0,
            type: '',
            comment: ''
        });
        $scope.buyersAfids[$scope.buyers.length - 1] = [];
    };

    $scope.cancelBuyersData = function () {
        $scope.name = "";
        $scope.planProfit = 0;
        $scope.planProfitOld = 0;
        $scope.planRev = 0;
        $scope.planRevOld = 0;
        $scope.type = "";
        $scope.comment = '';
        $http.get("/buyer").then(function successCallback(response) {
            $scope.buyers = response.data;
        });
        $scope.buyerAfids = [];
        $scope.selectedBuyerIndex = -1;
        $scope.previousBuyerIndex = -1;
    };


});