Application.controller('buyerPaymentsController', function ($scope, $http) {
    $scope.payments = [];
    $scope.addedPayments = [];
    $scope.buyers = [];
    $scope.staffs = [];
    $scope.staffOptions = [];
    $scope.types = [];
    $scope.wallets = [];
    $scope.walletOptions = [];
    $scope.currencyOptions = [];
    $scope.disableStaffWallet = true;
    $scope.addedPaymentsToSave = [];

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
        $scope.getBuyers();
        $scope.getStaffs();
        $scope.getTypes();
        $scope.getCurrency();
        $scope.getWallets();
    };

    $scope.addPayment = function () {
        $scope.disableStaffWallet = true;
        $scope.staffOptions = [];
        $scope.walletOptions = [];
        $scope.addedPayments.push({
            buyer: null,
            staff: null,
            date: formatDate(new Date()),
            payroll: formatDate(new Date()),
            sum: null,
            code: null,
            type: null,
            wallet: null
        });
    };

    $scope.selectBuyer = function (id) {
        $scope.disableStaffWallet = false;
        for(var i = 0; i< $scope.staffs.length; i++){
            if(id === $scope.staffs[i].buyerId.toString()){
                $scope.staffOptions.push($scope.staffs[i]);
            }
        }
        for(var j = 0; j< $scope.wallets.length; j++){
            if(id === $scope.wallets[j].buyerId.toString()){
                $scope.walletOptions.push($scope.wallets[j]);
            }
        }
    };

    $scope.applyPayment = function () {
        $scope.formatAddedPaymentsDate();
        console.log($scope.addedPayments);
        console.log($scope.buyers);
        console.log($scope.staffs);
        console.log($scope.types);
        console.log($scope.currencyOptions);
        console.log($scope.wallets);

        var url = "/payment";
        var paymentsForSave = [];
        for(var i = 0; i<$scope.addedPayments.length; i++){
            paymentsForSave.push({
                "buyerId": $scope.addedPayments[i].buyer,
                "staffId": $scope.addedPayments[i].staff,
                "date": $scope.addedPayments[i].date,
                "datePayroll": $scope.addedPayments[i].payroll,
                "sum": $scope.addedPayments[i].sum,
                "currencyId": $scope.addedPayments[i].code,
                "typeId": $scope.addedPayments[i].type,
                "walletId": $scope.addedPayments[i].wallet
            });
        }

        $http.post(url, paymentsForSave).then(function success(response) {
            $scope.initPayments();
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during saving added payments', 'danger');
        });
    };

    $scope.cancelClick = function () {
        $scope.addedPayments = [];
    };

    $scope.formatAddedPaymentsDate = function () {
        for (var i = 0; i < $scope.addedPayments.length; i++) {
            $scope.addedPayments[i].date = formatDate($scope.addedPayments[i].date);
            $scope.addedPayments[i].payroll = formatDate($scope.addedPayments[i].payroll);
        }
    };

    $scope.removeRow = function (index) {
        $scope.addedPayments.splice(index, 1);
    };

    $scope.getCurrency = function () {
        $http.get('/currency').then(function success(response) {
            $scope.currencyOptions = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading currency', 'danger');
        });
    };

    $scope.getBuyers = function () {
        $http.get('/buyer').then(function success(response) {
            $scope.buyers = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading buyers', 'danger');
        });
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

    $scope.getWallets = function () {
        $http.get('/wallet').then(function success(response) {
            $scope.wallets = response.data;
        }, function fail(response) {
            notify('ti-alert', 'Error occurred during loading wallets', 'danger');
        });
    };

    $scope.displayStaffFullName = function (firstName, secondName) {
         return firstName + " " + secondName;
    };
});