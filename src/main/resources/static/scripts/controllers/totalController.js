Application.controller('totalController', function ($scope, $http) {
    var date = new Date();
    $scope.selectedMonth = 'December';
    $scope.selectedSize = 50;
    $scope.data = [];
    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };

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
        $scope.headers = generateTableHeaders($scope.selectedMonth);
        $scope.result = [];
        var month = getMonthNumber($scope.selectedMonth);
        var year = date.getFullYear();
        $http.get("/finance/total?from=" + year + "-" + month + "-01&to=" + year + "-" + month + "-31")
            .then(function success(response) {
                var data = response.data;

                $scope.revenue = 0;
                $scope.spent = 0;
                $scope.profit = 0;
                Object.keys(data).map(function (value) {
                    for (var i = 0; i < data[value].length; i++) {
                        var daily = data[value][i];
                        $scope.revenue += daily.revenue;
                        $scope.spent += daily.spent;
                        $scope.profit += daily.profit;
                    }
                });
                $scope.revenue = $scope.revenue.toFixed(2);
                $scope.spent = $scope.spent.toFixed(2);
                $scope.profit = $scope.profit.toFixed(2);

                $scope.values = [];
                Object.keys(data).map(function (value) {
                    var buyerData = [value, 'no-total'];
                    var profitTotal = 0;
                    for (var i = 2; i < $scope.headers.length; i++) {
                        var searchDateProfit = searchByDate($scope.headers[i], data[value], 'profit');
                        profitTotal = profitTotal + (searchDateProfit === undefined ? 0 : searchDateProfit);
                        buyerData.push(searchDateProfit);
                    }
                    buyerData[1] = profitTotal.toFixed(2);
                    $scope.result.push(buyerData);
                });
                var buyerTotalByDate = ['Total', undefined];
                for (var i = 2; i < $scope.headers.length; i++) {
                    var total = 0;
                    for (var j = 0; j < $scope.result.length; j++) {
                        total = total + ($scope.result[j][i] === undefined ? 0 : $scope.result[j][i]);
                    }
                    buyerTotalByDate.push(total === undefined ? undefined : total.toFixed(2));
                }
                $scope.result.push(buyerTotalByDate);
            }, function error() {
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
        var year = date.getFullYear();
        var headers = ['Buyer', 'Total'];
        for (var i = 1; i <= numberOfDays; i++) {
            headers.push(i + '-' + monthNumber + '-' + year);
        }
        console.log(headers);
        return headers;
    }

    function convertResponseToDate(date) {
        return date.dayOfMonth + '-' + date.monthValue + '-' + date.year;
    }

    function getMonthNumber(monthName) {
        return new Date(Date.parse(monthName + " 1, " + date.getFullYear())).getMonth() + 1;
    }

    function getNumberOfDays(monthNumber) {
        return new Date(date.getFullYear(), monthNumber, 0).getDate();
    }
});