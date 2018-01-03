'use strict';

Application.controller('balancePerMonthController', function ($scope, $http, $location) {
    var date = new Date();

    $scope.advNames = [];
    $scope.selectedAdv = [];
    $scope.addedBalance = [];

    $scope.showBalancePerMonthLoader = false;

    $scope.monthOptions = {
        "January": "01",
        "February": "02",
        "March": "03",
        "April": "04",
        "May": "05",
        "June": "06",
        "July": "07",
        "August": "08",
        "September": "09",
        "October": "10",
        "November": "11",
        "December": "12"
    };
    $scope.selectedMonth = "01";

    $scope.yearOptions = {
        "Current Year": "thisYear",
        "Next Year": "nextYear",
        "Previous Year": "prevYear"
    };
    $scope.selectedYear = "thisYear";


    $scope.setCurrentMonth = function (){
        var someDays = 10;
        var currentDate = new Date();
        currentDate.setDate(currentDate.getDate()+ someDays);
        var mm = currentDate.getMonth() + 1;
        if(mm < 10){
            mm = "0" + mm;
        }

        mm = mm.toString();
        $scope.bigChartSelectedMonth = mm;
    };


    $scope.initAdverts = function () {
        var url = "advertiser/all";
        $http.get(url).then(function successCallback(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.advNames.push({
                    id: response.data[i].id,
                    name: response.data[i].advshortname
                });
            }
            $scope.selectedAdv = $scope.advNames;
        });
    };


    $scope.addBalance = function () {
        $scope.addedBalance.unshift({
            advertiser: null, date: new Date(),
            total: null, comission: null,
            bank: null, account: null, cur: null
        });
    };


    $scope.go = function ( path ) {
        $location.path( path );
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
