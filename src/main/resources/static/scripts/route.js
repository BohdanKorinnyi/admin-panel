Application.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/login', {
            templateUrl: 'views/login.html',
            controller: 'loginController'
        })
        .when('/advertiser', {
            templateUrl: 'views/advertiser.html',
            controller: 'advertiserController'
        })
        .when('/conversions', {
            templateUrl: 'views/conversion.html',
            controller: 'conversionController'
        })
        .when('/postback', {
            templateUrl: 'views/postback.html',
            controller: 'postbackController'
        })
        .when('/payroll', {
            templateUrl: 'views/payroll.html',
            controller: 'payrollController'
        })
        .when('/buyers', {
            templateUrl: 'views/buyers.html',
            controller: 'buyerController',
            resolve:{
                factory: checkRoutingForBuyersAndAfid
            }
        })
        .when('/home', {
            templateUrl: 'views/arbitratorExecutive.html',
            controller: 'arbitratorExecutiveController',
            resolve: {
                factory: checkRoutingForHomePage
            }
        })
        .when('/home/buyer', {
            templateUrl: 'views/buyerHomeScreen.html',
            controller: 'buyerHomeController'
        })
        .when('/statistic', {
            templateUrl: 'views/statistic.html',
            controller: 'statisticController'
        })
        .when('/buyerStatistic', {
            templateUrl: 'views/buyerStatistic.html',
            controller: 'buyerStatisticController'
        })
        .when('/sources', {
            templateUrl: 'views/sources.html',
            controller: 'sourcesController'
        })
        .when('/plan', {
            templateUrl: 'views/planByBuyer.html',
            controller: 'planByBuyerController'
        })
        .when('/dashboard',{
            resolve: {
                factory: checkRouting
            }
        })
        .when('/buyer/dashboard', {
            templateUrl: 'views/dashboard.html',
            controller: 'dashboardController'
        })
        .when('/admin/dashboard', {
            templateUrl: 'views/adminDashboard.html',
            controller: 'adminDashboardController'
        })
        .when('/advertiserBalance', {
            templateUrl: 'views/advertiserBalance.html',
            controller: 'advertiserBalanceController'
        })
        .when('/cost/management', {
            templateUrl: 'views/costManagement.html',
            controller: 'costManagementController'
        })
        .when('/finance/total', {
            templateUrl: 'views/total.html',
            controller: 'totalController',
            resolve: {
                factory: checkRoutingForTotalPage
            }
        })
        .when('/', {
            resolve: {
                factory: checkRouting
            }
        });
}]);

var checkRoutingForTotalPage = function ($q, $rootScope, $location) {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/me', false);
    request.send(null);
    if (request.status === 200) {
        var z = JSON.parse(request.response);
        var role = z.authorities[0].authority;
        localStorage.setItem('role', role);
        if (role === 'ADMIN' || role === 'ROLE_ADMIN'
            || role === 'DIRECTOR' || role === 'CFO' || role === 'CBO') {
            $location.path('/finance/total');
        }
        else {
            $location.path('/');
        }
    }
};


var checkRouting = function ($q, $rootScope, $location) {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/me', false);
    request.send(null);
    if (request.status === 200) {
        var z = JSON.parse(request.response);
        var role = z.authorities[0].authority;
        localStorage.setItem('role', role);
        if (role === 'ADMIN' || role === 'ROLE_ADMIN'
            || role === 'DIRECTOR' || role === 'CFO' || role === 'CBO') {
            $location.path('/admin/dashboard');
        }
        else if (role === 'BUYER') {
            $location.path('/buyer/dashboard');
        }
        else {
            $location.path('/');
        }
    }
};

var checkRoutingForHomePage = function ($q, $rootScope, $location) {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/me', false);
    request.send(null);
    if (request.status === 200) {
        var z = JSON.parse(request.response);
        var role = z.authorities[0].authority;
        localStorage.setItem('role', role);
        if (role === 'ADMIN' || role === 'ROLE_ADMIN'
            || role === 'DIRECTOR' || role === 'CFO' || role === 'CBO') {
            $location.path('/home');
        }
        else if (role === 'BUYER') {
            $location.path('/home/buyer');
        }
        else {
            $location.path('/');
        }
    }
};

var checkRoutingForBuyersAndAfid = function ($q, $rootScope, $location) {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/me', false);
    request.send(null);
    if (request.status === 200) {
        var z = JSON.parse(request.response);
        var role = z.authorities[0].authority;
        localStorage.setItem('role', role);
        if (role === 'ADMIN' || role === 'ROLE_ADMIN'
            || role === 'DIRECTOR' || role === 'CFO' || role === 'CBO') {
            $location.path('/buyers');
        }
        else if (role === 'BUYER') {
            $location.path('/home/buyer');
        }
        else {
            $location.path('/');
        }
    }
};