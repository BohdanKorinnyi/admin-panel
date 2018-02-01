Application.controller("buyerHomeController", function ($scope, $http, $location, transferService) {

    $scope.typeValues = [
        {'name': 'Accrual', 'value': 0},
        {'name': 'Write-off', 'value': 1}
    ];

    $scope.selectedPage = 1;
    $scope.totalPagination = 1;
    $scope.noOfPages = 1;
    $scope.sizeOptions = {
        50: 50,
        100: 100,
        500: 500
    };
    $scope.selectedSize = 50;
    $scope.sortColumn = '';
    $scope.sortReverse = '';
    $scope.showPayrollsLoader = true;
    $scope.buyerOptions = [];
    $scope.currencyOptions = [];

    $scope.toSpentByBuyer = function () {
        transferService.setParam(1);
        $location.path("/buyerStatistic");
    };

    $scope.toPostbackReport = function () {
        transferService.setParam(1);
        $location.path("/postback");
    };

    $scope.toRevSpent = function () {
        $location.path("/sources");
    };

    $scope.changeOrder = function () {
        if ($scope.sortReverse === '') {
            $scope.sortReverse = 'ASC';
        }
        else if ($scope.sortReverse === 'ASC') {
            $scope.sortReverse = 'DESC';
        }
        else if ($scope.sortReverse === 'DESC') {
            $scope.sortReverse = '';
        }
    };


    $scope.init = function () {
        $scope.confirmed = "";
        $scope.spent = "";
        $scope.profit = "";
        $scope.spent = "";
        $scope.bonus = "";
        $scope.verified = "";
        var confirmed = "";
        var spent = "";
        var profit = "";

        $http.get('postback/buyers/revenue').then(function (value) {
            confirmed = value.data;
            $http.get('/buyer/home/spent').then(function (value) {
                if(value.data === ""){
                    spent = 0;
                }
                else{
                    spent = value.data;
                }
                profit = (confirmed - spent).toFixed(2);

                $scope.confirmed = "$" + confirmed;
                $scope.spent = "$" + spent;
                $scope.profit = "$" + profit;
                if (profit > 0) {
                    $scope.bonus = "$" + (profit * 0.2).toFixed(2);
                }
            });
        });
        $http.get('buyer/plan/revenue').then(function (value) {
            $scope.plan = "$" + value.data;
        });
    };

    $scope.addDollarSign = function () {
        $scope.plan = "$" + $scope.plan;
        $scope.confirmed = "$" + $scope.confirmed;
        $scope.spent = "$" + $scope.spent;
        $scope.profit = "$" + $scope.profit;
        $scope.bonus = "$" + $scope.bonus;
    };


    $scope.selectedPayrollItem = {};
    $scope.selectedBuyerName = '';
    $scope.selectedBuyerId = 0;
    $scope.selectedTypeValue = '';
    $scope.selectedTypeId = 0;
    $scope.selectedCurrencyCode = '';
    $scope.selectedCurrencyId = 0;
    $scope.selectedDescriptionValue = '';
    $scope.selectedDate = '';
    $scope.selectedSum = '';

    $scope.clickRow = function (payroll) {
        $scope.selectedPayrollItem = payroll;
        $scope.selectedDate = formatDate(payroll.date);
        $scope.selectedSum = payroll.sum;
        if (payroll.type === 0) {
            $scope.selectedTypeValue = 'Accrual';
            $scope.selectedTypeId = 0;
        }
        else {
            $scope.selectedTypeValue = 'Write-off';
            $scope.selectedTypeId = 1;
        }
        for (var i = 0; i < $scope.buyerOptions.length; i++) {
            if (payroll.buyerId === $scope.buyerOptions[i].id) {
                $scope.selectedBuyerName = $scope.buyerOptions[i].name;
                $scope.selectedBuyerId = $scope.buyerOptions[i].id;
            }
        }
        for (var i = 0; i < $scope.currencyOptions.length; i++) {
            if (payroll.currencyId === $scope.currencyOptions[i].id) {
                $scope.selectedCurrencyCode = $scope.currencyOptions[i].code;
                $scope.selectedCurrencyId = $scope.currencyOptions[i].id;
            }
        }
        for (var i = 0; i < $scope.descriptionOptions.length; i++) {
            if (payroll.description === $scope.descriptionOptions[i]) {
                $scope.selectedDescriptionValue = $scope.descriptionOptions[i];
            }
        }
    };

    $scope.loadPayrolls = function () {
        $scope.payrolls = [];
        $scope.showPayrollsLoader = true;
        $http.post("/payroll", $scope.getGridDetails()).then(function (response) {
            $scope.payrolls = $scope.updatePayrolls(response.data.data);
            $scope.showPayrollsLoader = false;
        }, function () {
            $scope.showPayrollsLoader = false;
            notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
        });
    };

    $scope.getGridDetails = function () {
        return {
            'size': $scope.selectedSize,
            'number': $scope.selectedPage,
            'columnOrder': {'column': $scope.sortColumn, 'order': $scope.sortReverse}
        };
    };


    $scope.updatePayrolls = function (payrolls) {
        for (var i = 0; i < payrolls.length; i++) {
            payrolls[i]['bayerName'] = findBuyerName(payrolls[i].buyerId, $scope.buyerOptions);
            payrolls[i]['code'] = findCurrencyCode(payrolls[i].currencyId, $scope.currencyOptions);
            payrolls[i]['typeName'] = payrolls[i].type === 0 ? 'Accrual' : 'Write-off';
        }
        return payrolls;
    };

    $scope.getBuyers = function () {
        $http.get('/buyer').then(function success(response) {
            $scope.buyerOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
    };

    $scope.getCurrency = function () {
        $http.get('/currency').then(function success(response) {
            $scope.currencyOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading currency', 'danger');
        });
    };
});


function findCurrencyCode(id, currency) {
    for (var i = 0; i < currency.length; i++) {
        if (currency[i].id === id) {
            return currency[i].code;
        }
    }
}

function findBuyerName(id, buyers) {
    for (var i = 0; i < buyers.length; i++) {
        if (buyers[i].id === id) {
            return buyers[i].name;
        }
    }
}