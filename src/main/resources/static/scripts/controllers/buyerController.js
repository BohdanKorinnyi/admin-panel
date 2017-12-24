"use strict";

Application.controller("buyerController", function ($scope, $http) {

    $scope.buyers = [];

    $scope.buyersAfids = [];
    $scope.buyersAfidsString = "";

    $scope.kpiNames = [];
    $scope.addedKpi = [];
    $scope.buyerKpi = [];

    $scope.addedBuyersAfidCount = "";
    $scope.name = "";
    $scope.planProfit = 0;
    $scope.planProfitOld = 0;
    $scope.planRev = 0;
    $scope.planRevOld = 0;
    $scope.type = "";
    $scope.comment = '';

    $scope.selectedBuyerIndex = -1;
    $scope.previousBuyerIndex = -1;


    $scope.dateOptions = {
        'January': '-01-01',
        'February': '-02-01',
        'March': '-03-01',
        'April': '-04-01',
        'May': '-05-01',
        'June': '-06-01',
        'July': '-07-01',
        'August': '-08-01',
        'September': '-09-01',
        'October': '-10-01',
        'November': '-11-01',
        'December': '-12-01'
    };
    $scope.selectedDate = '';
    $scope.year = "";

    // functions
    $scope.loadData = function () {
        $scope.initData();
        $scope.getKpiNames();
    };

    $scope.addKpi = function () {
        $scope.addedKpi.push({
            date: null, kpiName: null, kpiValue: null, year: null
        });
    };

    $scope.initData = function () {
        var url = "/buyer";
        $http.get(url).then(function success(response) {
            $scope.buyers = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };



    $scope.applyClick = function () {
        var toSave = [];
        var toUpdate = [];
        for (var i = 0; i < $scope.buyers.length; i++) {
            if ($scope.buyers[i].id === null) {
                toSave.push($scope.buyers[i]);
            } else {
                toUpdate.push($scope.buyers[i]);
            }
        }
        $scope.buyersAfidsString = '';
        if (toSave.length !== 0) {
            $http.post("/buyer/save", toSave).then(function success(response) {
                notify('ti-thumb-up', 'Buyers saved successfully', 'success');
                $scope.initData();
                $scope.cancelBuyersData();
            }, function errorCallback() {
                $scope.cancelBuyersData();
                notify('ti-alert', 'Error occurred during saving buyers', 'danger');
            });
        }
        if (toUpdate.length !== 0) {
            $http.put("/buyer/update", toUpdate).then(function success(response) {
                notify('ti-thumb-up', 'Buyers updated successfully', 'success');
                $scope.initData();
                $scope.cancelBuyersData();
            }, function errorCallback() {
                $scope.cancelBuyersData();
                notify('ti-alert', 'Error occurred during updating buyers', 'danger');
            });
        }
    };

    $scope.selectedBuyerId = 0;

    $scope.onBuyerClick = function (buyer, index) {
        if ($scope.selectedBuyerIndex === -1) {
            $scope.selectedBuyerIndex = index;
            $scope.previousBuyerIndex = index;
        }
        $scope.previousBuyerIndex = $scope.selectedBuyerIndex;
        $scope.selectedBuyerIndex = index;

        $scope.selectedBuyerId = buyer.id;
        $scope.getBuyerAfidById(buyer.id);
        $scope.getBuyerKpiById(buyer.id);
        $scope.name = buyer.name;
        $scope.planProfit = buyer.planProfit;
        $scope.planProfitOld = buyer.planProfitOld;
        $scope.planRev = buyer.planRev;
        $scope.planRevOld = buyer.planRevOld;
        $scope.type = buyer.type;
        $scope.comment = buyer.comment;
    };

    $scope.getBuyerAfidById = function (id) {
        $scope.buyersAfidsString = '';
        if (id !== null) {
            $http.get(" /affiliates?buyer_id=" + id).then(function success(response) {
                $scope.buyersAfids = response.data;
                $scope.buyersAfidsString = response.data.join();
            }, function errorCallback() {
                notify('ti-alert', 'Error occurred during loading afids', 'danger');
            });
        }
    };

    $scope.getBuyerKpiById = function (id) {
        if (id !== null) {
            $http.get("/buyer/kpi?buyerId=" + id).then(function success(response) {
                for(var i = 0; i < response.data.length; i++){
                    response.data[i].date = $scope.formatViewDate(response.data[i].date);
                }
                $scope.buyerKpi = response.data;
            }, function errorCallback() {
                notify('ti-alert', 'Error occurred during loading KPI', 'danger');
            });
        }
    };

    $scope.postBuyerKpiById = function () {
        if ($scope.selectedBuyerId !== -1) {
            for(var i = 0; i<$scope.addedKpi.length; i++){
                var kpi = $scope.addedKpi[i];
                kpi.date = kpi.year + kpi.date;
            }
            $http.post("/buyer/kpi?buyerId=" + $scope.selectedBuyerId, $scope.addedKpi).then(function success(response) {
                notify('ti-alert', 'Well done! KPI saved.', 'success');
                $scope.getBuyerKpiById($scope.selectedBuyerId);
            }, function errorCallback() {
                notify('ti-alert', 'Error occurred during saving KPI', 'danger');
            });
        }
    };

    $scope.getKpiNames = function(){
        var url = "/catalogs/kpi";
        $http.get(url).then(function success(response) {
            $scope.kpiNames = response.data;
        }, function errorCallback() {
            notify('ti-alert', 'Error occurred during loading KPI names', 'danger');
        });
    };

    $scope.updateBuyerName = function () {
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


    $scope.loader = false;

    $scope.addAfids = function () {
        $scope.loader = true;
        var url = "/affiliates?quantity="+$scope.addedBuyersAfidCount+"&buyer_id="+$scope.selectedBuyerId;
        $http.post(url, $scope.addedBuyersAfidCount).then(function success(response){
            $scope.getBuyerAfidById($scope.selectedBuyerId);
            $scope.cancelAfids();
            $scope.loader = false;
        }, function errorCallback(response) {
            notify('ti-alert', 'Error occurred during adding afids', 'danger');
        });
    };

    $scope.cancelAfids = function () {
        $scope.addedBuyersAfidCount = "";
    };

    $scope.removeRow = function (item) {
        $scope.addedKpi.splice(item, 1);
    };

    $scope.cancelKpi = function(){
        $scope.addedKpi = [];
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