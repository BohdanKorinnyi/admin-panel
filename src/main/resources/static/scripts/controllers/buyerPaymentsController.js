Application.controller('buyerPaymentsController', function ($scope, $http) {
    $scope.payments = [];
    $scope.staffs = [];
    $scope.types = [];
    $scope.currencyOptions = [];
    $scope.currentStaffWallets = [];
    $scope.currentStaffPayrolls = [];
    $scope.isBuyer = false;
    $scope.showLoader = true;
    $scope.addedPayments = [];
    $scope.disableWalletPayrolSelects = true;

    $scope.checkRole = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);
        request.send(null);
        var principle = JSON.parse(request.response);
        $scope.role = principle.authorities[0].authority;
        if ($scope.role === 'BUYER') {
            $scope.isBuyer = true;
        }
        else{
            $scope.isBuyer = false;
        }
    };

    $http.get('/payment').then(function (value) {
        $scope.payments = value.data;
        $scope.showLoader = false;
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
        var params = {};
        params.payrollId = $scope.selectedPayroll;
        params.staffId = $scope.selectedStaff;
        params.date = formatDate($scope.selectedDate);
        params.datePayroll = formatDate($scope.selectedPayrollDate);
        params.sum = $scope.selectedSum;
        params.currencyId = $scope.selectedCode;
        params.typeId = $scope.selectedType;
        params.walletId = $scope.selectedWallet;

        $scope.addedPayments.push(params);

        $http.post(url, $scope.addedPayments).then(function success(response) {
            $scope.initPayments();
            $scope.currentStaffWallets = [];
            $scope.currentStaffPayrolls = [];
            $scope.addedPayments = [];
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during saving added payments', 'danger');
        });
    };

    $scope.getCurrency = function () {
        $http.get('/currency').then(function success(response) {
            $scope.currencyOptions = response.data;
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
                $scope.disableWalletPayrolSelects = false;
            }, function fail(response) {
                notify('ti-alert', 'Error occurred during loading payrolls', 'danger');
            });
        }
    };

    $scope.getStaffs = function () {
        $http.get('/staff').then(function success(response) {
            $scope.staffs = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading staffs', 'danger');
        });
    };

    $scope.getTypes = function () {
        $http.get('/payroll/types').then(function success(response) {
            $scope.types = response.data;
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
                $scope.disableWalletPayrolSelects = false;
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