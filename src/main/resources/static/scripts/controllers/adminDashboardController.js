'use strict';

Application.controller('adminDashboardController', function ($scope, $http, dateFactory) {
    var date = new Date();
    $scope.adminDashboardData = [];
    $scope.revenueToday = '';
    $scope.revenueYesterday = '';
    $scope.spentToday = '';
    $scope.spentYesterday = '';
    $scope.revTotal = '';
    $scope.spentTotal = '';
    $scope.profitTotal = '';


    $scope.monthShortNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    $scope.dateOptions = {
        'This year': 'allTime',
        'Today': 'today',
        'Yesterday': 'yesterday',
        'Last 7 days': 'lastWeek',
        'This Month': 'thisMonth',
        'Last Month': 'lastMonth'
    };

    $scope.bigChartMonthOptions = {
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
    var currentMonth = getCurrentMonth();

    $scope.bigChartSelectedMonth = currentMonth >= 10 ? currentMonth : '0' + currentMonth;

    $scope.bigChartYearOptions = {
        "Current Year": "thisYear",
        "Next Year": "nextYear",
        "Previous Year": "prevYear"
    };
    $scope.bigChartSelectedYear = "thisYear";

    $scope.selectedDate = 'thisMonth';
    $scope.from = '';
    $scope.to = '';


    $scope.clearSelects = function () {
        $scope.bigChartSelectedMonth = currentMonth >= 10 ? currentMonth : '0' + currentMonth;;
        $scope.bigChartSelectedYear = "thisYear";
    };

    $scope.setCurrentMonth = function () {
        var someDays = 1;
        var currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + someDays);
        var mm = currentDate.getMonth() + 1;
        if (mm < 10) {
            mm = "0" + mm;
        }

        mm = mm.toString();
        $scope.bigChartSelectedMonth = mm;
    };

    $scope.toFixedView = function (value) {
        return value.toFixed(2);
    };

    $scope.initAdminDashboardData = function () {
        $scope.revTotal = 0;
        $scope.spentTotal = 0;
        $scope.profitTotal = 0;
        $scope.roiTotal = 0;

        $scope.spentToday = 0;
        $scope.spentYesterday = 0;

        $scope.revenueToday = "";
        $scope.revenueYesterday = "";
        $scope.profitToday = "";
        $scope.profitYesterday = "";
        $scope.roiToday = "";
        $scope.roiYesterday = "";

        if ($scope.selectedDate === 'allTime') {
            var yearFrom = new Date().getFullYear();
            var yearTo = new Date().getFullYear() + 1;

            var f = new Date(yearFrom, 0, 1);
            var t = new Date(yearTo, 0, 1);

            $scope.from = formatDate(f);
            $scope.to = formatDate(t);
        }
        else if ($scope.selectedDate === 'yesterday') {
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
        }
        else {
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateTo($scope.selectedDate));
        }

        var url = '/admin/dashboard?from=' + $scope.from + '&to=' + $scope.to;
        $scope.adminDashboardData = [];
        $http.get(url).then(function success(response) {
            $scope.findTotals(response.data.data);
            $scope.adminDashboardData = response.data.data;
            $scope.spentToday = response.data.today.spent;
            $scope.revenueToday = response.data.today.revenue;
            $scope.spentYesterday = response.data.yesterday.spent;
            $scope.revenueYesterday = response.data.yesterday.revenue;
            $scope.profitToday = (parseFloat($scope.revenueToday) - parseFloat($scope.spentToday)).toFixed(2);
            $scope.profitYesterday = (parseFloat($scope.revenueYesterday) - parseFloat($scope.spentYesterday)).toFixed(2);
            $scope.roiToday = ((parseFloat($scope.spentToday) === 0 ? 0 : parseFloat($scope.profitToday) / parseFloat($scope.spentToday)) * 100).toFixed(2);
            $scope.roiYesterday = ((parseFloat($scope.spentYesterday) === 0 ? 0 : parseFloat($scope.profitYesterday) / parseFloat($scope.spentYesterday)) * 100).toFixed(2);

            $scope.addDollarSign();
        }, function fail() {
            notify('ti-alert', 'Error occurred during loading admin dashboard info', 'danger');
        });
    };

    $scope.addDollarSign = function () {
        $scope.revenueToday = "$" + $scope.revenueToday;
        $scope.revenueYesterday = "$" + $scope.revenueYesterday;
        $scope.profitToday = "$" + $scope.profitToday;
        $scope.profitYesterday = "$" + $scope.profitYesterday;
        $scope.roiToday = $scope.roiToday + "%";
        $scope.roiYesterday = $scope.roiYesterday + "%";
    };

    $scope.getAdminDashboardChartData = function () {
        $scope.chartData = [];
        $scope.chartDateData = [];
        $scope.chartRevData = [];
        $scope.chartSpentData = [];
        $scope.chartProfitData = [];
        $scope.chartMonthData = [];

        if ($scope.selectedDate === 'allTime') {
            $scope.from = '01-01-2018';
            $scope.to = '31-01-2018';
        }
        else {
            $scope.from = formatDate(dateFactory.pickDateFrom($scope.selectedDate));
            $scope.to = formatDate(dateFactory.pickDateTo($scope.selectedDate));
        }
        $http.get('/admin/dashboard/charts?from=' + $scope.from + '&to=' + $scope.to + '&filter=' + $scope.selectedDate).then(function success(response) {
            $scope.chartData = response.data.data;
            for (var i = 0; i < $scope.chartData.length; i++) {
                var currentDate = formatDateWithoutYear(getDateOfWeek($scope.chartData[i].date, date.getFullYear()));
                var currentNotFormattedDate = getDateOfWeek($scope.chartData[i].date, date.getFullYear());

                if ($scope.selectedDate === 'thisMonth') {
                    var currentMonth = date.getMonth();
                    if (currentNotFormattedDate.getMonth() !== currentMonth) {
                        currentNotFormattedDate.setDate(1);
                        currentNotFormattedDate.setMonth(currentMonth);
                        $scope.chartDateData.push(formatDateWithoutYear(currentNotFormattedDate));
                    }
                    else {
                        $scope.chartDateData.push(currentDate);
                    }
                }
                else if ($scope.selectedDate === 'lastMonth') {
                    var lastMonth = date.getMonth() - 1;
                    if (currentNotFormattedDate.getMonth() !== lastMonth) {
                        currentNotFormattedDate.setDate(1);
                        currentNotFormattedDate.setMonth(lastMonth);
                        $scope.chartDateData.push(formatDateWithoutYear(currentNotFormattedDate));
                    }
                    else {
                        $scope.chartDateData.push(currentDate);
                    }
                }
                else {
                    $scope.chartDateData.push($scope.chartData[i].date);
                }

                $scope.chartRevData.push($scope.chartData[i].revenue);
                $scope.chartSpentData.push($scope.chartData[i].spent);
                $scope.chartProfitData.push($scope.chartData[i].profit);
            }

            $scope.chartDateData.sort(function (a, b) {
                return a - b
            });
            if ($scope.selectedDate === 'allTime') {
                for (var j = 0; j < $scope.chartDateData.length; j++) {
                    $scope.chartMonthData.push($scope.monthShortNames[$scope.chartDateData[j] - 1]);
                }
                $scope.chartDateData = $scope.chartMonthData;
            }
            new Chartist.Line('#revenueChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartRevData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
            new Chartist.Line('#spentChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartSpentData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
            new Chartist.Line('#profitChart', {
                labels: $scope.chartDateData,
                series: [$scope.chartProfitData]
            }, {
                showArea: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ]
            });
        }, function fail() {
            notify('ti-alert', 'Error occurred during loading admin dashboard chart info', 'danger');
        });
    };


    $scope.getBigChartData = function (key) {
        $scope.bigChartData = [];
        $scope.bigChartDateData = [];
        $scope.bigChartRevData = [];
        $scope.bigChartSpentData = [];
        $scope.bigChartProfitData = [];
        var year = "";

        if ($scope.bigChartSelectedYear === 'thisYear') {
            year = new Date().getFullYear();
        }
        else if ($scope.bigChartSelectedYear === "nextYear") {
            year = new Date().getFullYear() + 1;
        } else {
            year = new Date().getFullYear() - 1;
        }

        $scope.from = year + "-" + $scope.bigChartSelectedMonth + "-01";
        $scope.to = year + "-" + $scope.bigChartSelectedMonth + "-31";

        var url = '/admin/dashboard/charts?from=' + $scope.from + '&to=' + $scope.to + '&filter=day';

        $http.get(url).then(function success(response) {
            $scope.bigChartData = response.data.data;

            if (key === 0) {
                for (var s = 0; s < $scope.bigChartData.length; s++) {
                    $scope.bigChartSpentData.push($scope.bigChartData[s].spent);
                    $scope.bigChartDateData.push($scope.formatViewDate($scope.bigChartData[s].date));
                }

                new Chartist.Line('#bigSpentChart', {
                    labels: $scope.bigChartDateData,
                    series: [$scope.bigChartSpentData]
                }, {
                    showArea: true,
                    plugins: [
                        Chartist.plugins.tooltip()
                    ]
                });
            }

            else if (key === 1) {
                for (var r = 0; r < $scope.bigChartData.length; r++) {
                    $scope.bigChartRevData.push($scope.bigChartData[r].revenue);
                    $scope.bigChartDateData.push($scope.formatViewDate($scope.bigChartData[r].date))
                }

                new Chartist.Line('#bigRevenueChart', {
                    labels: $scope.bigChartDateData,
                    series: [$scope.bigChartRevData]
                }, {
                    showArea: true,
                    plugins: [
                        Chartist.plugins.tooltip()
                    ]
                });
            }

            else if (key === 2) {
                for (var p = 0; p < $scope.bigChartData.length; p++) {
                    $scope.bigChartProfitData.push($scope.bigChartData[p].profit);
                    $scope.bigChartDateData.push($scope.formatViewDate($scope.bigChartData[p].date))
                }

                new Chartist.Line('#bigProfitChart', {
                    labels: $scope.bigChartDateData,
                    series: [$scope.bigChartProfitData]
                }, {
                    showArea: true,
                    plugins: [
                        Chartist.plugins.tooltip()
                    ]
                });
            }
        }, function fail() {
            notify('ti-alert', 'Error occurred during loading admin dashboard chart info', 'danger');
        });
    };

    $scope.calculateRoi = function (profit, spent) {
        spent = parseFloat(spent);
        if (spent === 0) {
            return 0.00;
        }
        return (parseFloat(profit) / spent) * 100;
    };

    $scope.findTotals = function (data) {
        for (var i = 0; i < data.length; i++) {
            $scope.revTotal = $scope.revTotal + data[i].revenue;
            $scope.spentTotal = $scope.spentTotal + data[i].spent;
            $scope.profitTotal = $scope.profitTotal + data[i].profit;
        }
        $scope.revTotal = $scope.revTotal.toFixed(2);
        $scope.spentTotal = $scope.spentTotal.toFixed(2);
        $scope.profitTotal = $scope.profitTotal.toFixed(2);
        $scope.roiTotal = parseFloat($scope.spentTotal) === 0 ? 0.00 : ((parseFloat($scope.profitTotal) / parseFloat($scope.spentTotal)) * 100).toFixed(2);
    };


    $scope.formatViewDate = function (date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [day];
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

function getDateOfWeek(w, y) {
    var d = (1 + (w - 1) * 7); // 1st of January + 7 days for each week
    return new Date(y, 0, d);
}

function formatDateWithoutYear(date) {
    var d = new Date(date),
        day = '' + d.getDate();
    if (day.length < 2) {
        day = '0' + day
    }
    return [day];
}

function daysInMonth(month, year) {
    return new Date(year, month, 0).getDate();
}

