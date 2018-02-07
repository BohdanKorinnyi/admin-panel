Application.controller('buyerPaymentsController', function ($scope, $http) {
    $scope.payments = [];
    $scope.staffs = [];
    $scope.types = [];
    $scope.currencyOptions = [];
    $scope.currentStaffWallets = [];
    $scope.currentStaffPayrolls = [];


    $http.get('/payment').then(function (value) {
        $scope.payments = value.data;
    });

    $scope.toUpperCase = function (value) {
        return value.toLocaleUpperCase();
    };

    $scope.formatDate = function (date) {
        return formatViewDate(date);
    };

    $scope.initPayments = function () {
        $scope.getStaffs();
        $scope.getTypes();
        $scope.getCurrency();
    };

    $scope.addPayment = function () {
        $scope.selectedPayroll = null;
        $scope.selectedStaff = null;
        $scope.selectedDate = formatDate(new Date());
        $scope.selectedPayrollDate = formatDate(new Date());
        $scope.selectedSum = null;
        $scope.selectedCode = null;
        $scope.selectedType = null;
        $scope.selectedWallet = null;
    };

    $scope.applyPayment = function () {
        var url = "/payment";
        var paymentForSave = {
            "payrollId": $scope.selectedPayroll,
            "staffId": $scope.selectedStaff,
            "date": formatDate($scope.selectedDate),
            "datePayroll": formatDate($scope.selectedPayrollDate),
            "sum": $scope.selectedSum,
            "currencyId": $scope.selectedCode,
            "typeId": $scope.selectedType,
            "walletId": $scope.selectedWallet
        };

        $http.post(url, paymentForSave).then(function success(response) {
            $scope.initPayments();
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during saving added payments', 'danger');
        });
    };

    $scope.getCurrency = function () {
        $http.get('/currency').then(function success(response) {
            $scope.currencyOptions = response.data;
            console.log("curr");
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading currency', 'danger');
        });
    };

    $scope.getPayrollsByStaffId = function () {
        if ($scope.selectedStaff !== null) {
            var staffId = parseInt($scope.selectedStaff);
            var url = "payroll/staff/" + staffId;
            $http.get(url).then(function success(response) {
                $scope.currentStaffPayrolls = response.data;
                console.log(response.data);
            }, function fail(response) {
                notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
            });
        }
    };

    $scope.getStaffs = function () {
        $http.get('/staff').then(function success(response) {
            $scope.staffs = response.data;
            console.log("staffs");
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading staffs', 'danger');
        });
    };

    $scope.getTypes = function () {
        $http.get('/payroll/types').then(function success(response) {
            $scope.types = response.data;
            console.log("types");
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading types', 'danger');
        });
    };

    $scope.getWalletsByStaffId = function () {
        if ($scope.selectedStaff !== null) {
            var staffId = parseInt($scope.selectedStaff);
            var url = "wallet/staff/" + staffId;
            $http.get(url).then(function success(response) {
                $scope.currentStaffWallets = response.data;
                console.log(response.data);
            }, function fail(response) {
                notify('ti-alert', 'Error occurred during loading wallets', 'danger');
            });
        }
    };

    $scope.getByStaffId = function () {
        $scope.getPayrollsByStaffId();
        $scope.getWalletsByStaffId();
    };

    $scope.displayStaffFullName = function (firstName, secondName) {
        return firstName + " " + secondName;
    };
})
;