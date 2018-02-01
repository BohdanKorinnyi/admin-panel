Application.controller('dashboardController', function ($scope, $http, $q) {
    $scope.selectedYear = new Date().getFullYear();
    $scope.chartData = [];
    $scope.chartDateData = [];
    $scope.chartRevData = [];
    $scope.chartSpentData = [];
    $scope.chartProfitData = [];
    $scope.chartMonthData = [];

    $scope.payroll = [];
    $scope.revenue = '';
    $scope.profitPlan = '';
    $scope.totalPaid = 0.00;
    $scope.bonus = '';
    $scope.revenuePlan = '';
    $scope.buyerName = '';
    $scope.profit = '';
    $scope.revenueCompleted = '';
    $scope.profitCompleted = '';
    $scope.showLoader = false;

    $scope.fillTable = function () {
        var revenue = $http.get('/postback/year/' + $scope.selectedYear);
        var spent = $http.get('/spent/year/' + $scope.selectedYear);
        var payment = $http.get('/payment/year/' + $scope.selectedYear);
        var bonus = $http.get('/payroll/bonus/year/' + $scope.selectedYear);

        $scope.result = [];
        $q.all([revenue, spent, payment, bonus]).then(function (values) {
            getMonths().map(function (month) {
                $scope.result.push(tableRow(month, values));
            });
            $scope.total = {
                liability: 0,
                bonus: 0,
                paidTotal: 0
            };
            $scope.result.map(function (value) {
                $scope.total.liability += parseFloat(value.liability);
                $scope.total.bonus += parseFloat(value.bonus);
                $scope.total.paidTotal += parseFloat(value.paidTotal);
            });
        });

        $scope.showLoader = true;
    };

    $scope.initData = function () {
        $http.get('/dashboard').then(function success(response) {
            $scope.payroll = response.data.payroll;
            $scope.revenue = response.data.revenue.toFixed(2);
            $scope.profitPlan = response.data.profitPlan.toFixed(2);
            $scope.totalPaid = response.data.totalPaid.toFixed(2);
            $scope.bonus = response.data.bonus.toFixed(2);
            $scope.revenuePlan = response.data.revenuePlan.toFixed(2);
            $scope.buyerName = response.data.buyerName;
            $scope.profit = response.data.profit.toFixed(2);
            $scope.revenueCompleted = (($scope.revenue / $scope.revenuePlan !== 0 ? $scope.revenuePlan : 1) * 100).toFixed(2);
            $scope.profitCompleted = (($scope.profit / $scope.profitPlan !== 0 ? $scope.profitPlan : 1) * 100).toFixed(2);

            $scope.profitToday = (response.data.revenueToday - response.data.spentToday).toFixed(2);
            $scope.profitYesterday = (response.data.revenueYesterday - response.data.spentYesterday).toFixed(2);
            $scope.roiToday = response.data.spentToday === null ? 0.00 : (($scope.profitToday / response.data.spentToday) * 100).toFixed(2);
            $scope.roiYesterday = response.data.spentYesterday === null ? 0.00 : (($scope.profitYesterday / response.data.spentYesterday) * 100).toFixed(2);

            $scope.addDollarSign();
        }, function fail() {
            notify('ti-alert', 'Error occurred during loading buyer\'s statistic', 'danger');
        });
    };

    $scope.addDollarSign = function () {
        $scope.revenue = "$" + $scope.revenue;
        $scope.revenuePlan = "$" + $scope.revenuePlan;
        $scope.bonus = "$" + $scope.bonus;
        $scope.revenueCompleted = $scope.revenueCompleted + "%";
        $scope.roiToday = $scope.roiToday + "%";
        $scope.roiYesterday = $scope.roiYesterday + "%";
    };

    $scope.getDashboardChartData = function () {
        $scope.chartData = [];
        $scope.chartDateData = [];
        $scope.chartRevData = [];
        $scope.chartSpentData = [];
        $scope.chartProfitData = [];
        $scope.chartMonthData = [];
        $http.get("/dashboard/charts?from=2018-01-01&to=2018-01-31&filter=thisMonth").then(function success(response) {
            $scope.chartData = response.data.data;
            for (var i = 0; i < $scope.chartData.length; i++) {
                $scope.chartDateData.push($scope.chartData[i].fullDate);
                $scope.chartRevData.push($scope.chartData[i].revenue);
                $scope.chartSpentData.push($scope.chartData[i].spent);
                $scope.chartProfitData.push($scope.chartData[i].profit);
            }

            $scope.chartDateData.sort(function (a, b) {
                return a - b
            });
            for (var j = 0; j < $scope.chartDateData.length; j++) {
                $scope.chartMonthData.push($scope.chartDateData[j]);
            }
            new Chartist.Line('#revenueChart', {
                labels: $scope.chartMonthData,
                series: [$scope.chartRevData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
            new Chartist.Line('#spentChart', {
                labels: $scope.chartMonthData,
                series: [$scope.chartSpentData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
            new Chartist.Line('#profitChart', {
                labels: $scope.chartMonthData,
                series: [$scope.chartProfitData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
        }, function fail() {
            notify('ti-alert', 'Error occurred during loading dashboard chart info', 'danger');
        });
    };

    $scope.formatViewDate = function (date) {
        return formatViewDate(date);
    };
});



