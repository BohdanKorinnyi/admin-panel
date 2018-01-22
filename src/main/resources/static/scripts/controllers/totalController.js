Application.controller('totalController', function ($scope, $http) {
    var date = new Date();
    $scope.selectedMonth = 'January';
    $scope.data = [];
    $scope.showLoader = true;
    $scope.buyerAndTotalData = [];
    $scope.selectedYear = date.getFullYear();

    $scope.dateOptions = {
        'January': 'January',
        'February': 'February',
        'March': 'March',
        'April': 'April',
        'May': 'May',
        'June': 'June',
        'July': 'July',
        'August': 'August',
        'September': 'September',
        'October': 'October',
        'November': 'November',
        'December': 'December'
    };

    $scope.init = function () {
        $scope.showLoader = true;
        $scope.headers = generateTableHeaders($scope.selectedMonth);
        $scope.result = [];
        $scope.buyerAndTotalData = [];
        var month = getMonthNumber($scope.selectedMonth);
        $scope.revenue = 0;
        $scope.spent = 0;
        $scope.profit = 0;
        $http.get("/finance/total?from=" + $scope.selectedYear + "-" + month + "-01&to=" + $scope.selectedYear + "-" + $scope.selectedYear + "-31")
            .then(function success(response) {
                $scope.showLoader = false;
                var data = response.data;

                Object.keys(data).map(function (value) {
                    for (var i = 0; i < data[value].length; i++) {
                        var daily = data[value][i];
                        $scope.revenue += daily.revenue;
                        $scope.spent += daily.spent;
                        $scope.profit += daily.profit;
                    }
                });
                $scope.revenue = getZeroInsteadOfUndefined($scope.revenue);
                $scope.spent = getZeroInsteadOfUndefined($scope.spent);
                $scope.profit = getZeroInsteadOfUndefined($scope.profit);

                $scope.values = [];
                Object.keys(data).map(function (value) {
                    var buyerData = [value, 'no-total'];
                    var profitTotal = 0;
                    for (var i = 2; i < $scope.headers.length; i++) {
                        var searchDateProfit = searchByDate($scope.headers[i], data[value], 'profit');
                        profitTotal = profitTotal + getZeroInsteadOfUndefined(searchDateProfit);
                        buyerData.push(searchDateProfit);
                    }
                    buyerData[1] = profitTotal.toFixed(2);
                    $scope.result.push(buyerData);

                    $scope.buyerAndTotalData.push({
                        buyer: buyerData[0], total: buyerData[1]
                    });
                });
                var totalSum = getTotalSum($scope.buyerAndTotalData);
                var buyerTotalByDate = ['Total', totalSum];
                for (var i = 2; i < $scope.headers.length; i++) {
                    var total = 0;
                    for (var j = 0; j < $scope.result.length; j++) {
                        total = total + getZeroInsteadOfUndefined($scope.result[j][i]);
                    }
                    buyerTotalByDate.push(getZeroInsteadOfUndefined(total.toFixed(2)));
                }
                $scope.result.push(buyerTotalByDate);
                replaceUndefinedValues($scope.result);
            }, function error() {
                $scope.showLoader = false;
                notify('ti-alert', 'Error occurred during loading data', 'danger');
            });
    };

    function searchByDate(date, buyerStats, value) {
        for (var i = 0; i < buyerStats.length; i++) {
            var gridDate = convertResponseToDate(buyerStats[i].date);
            if (gridDate === date) {
                return buyerStats[i][value];
            }
        }
    }

    function generateTableHeaders(monthName) {
        var monthNumber = getMonthNumber(monthName);
        var numberOfDays = getNumberOfDays(monthNumber);
        var headers = ['Buyer', 'Total'];
        for (var i = 1; i <= numberOfDays; i++) {
            headers.push(i + '-' + monthNumber + '-' + $scope.selectedYear);
        }
        return headers;
    }

    function convertResponseToDate(date) {
        return date.dayOfMonth + '-' + date.monthValue + '-' + date.year;
    }

    function getMonthNumber(monthName) {
        return new Date(Date.parse(monthName + ' 1, ' + $scope.selectedYear)).getMonth() + 1;
    }

    function getNumberOfDays(monthNumber) {
        return new Date($scope.selectedYear, monthNumber, 0).getDate();
    }

    function replaceUndefinedValues(resultArrays) {
        for (var i = 0; i < resultArrays.length; i++) {
            for (var j = 0; j < resultArrays[i].length; j++) {
                if (resultArrays[i][j] === undefined) {
                    resultArrays[i][j] = 0;
                }
            }
        }
    }

    function getTotalSum(buyerAndTotalArr) {
        var sum = 0.00;
        for (var i = 0; i < buyerAndTotalArr.length; i++) {
            sum = sum + parseFloat(buyerAndTotalArr[i].total);
        }
        return sum.toFixed(2);
    }

    function getZeroInsteadOfUndefined(value) {
        return value === undefined ? 0 : value;
    }
});