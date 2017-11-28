Application.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/login', {
            templateUrl: "views/login.html",
            controller: "loginController"
        })
        .when('/advertiser', {
            templateUrl: "views/advertiser.html",
            controller: "advertiserController"
        })
        .when('/conversions', {
            templateUrl: "views/conversion.html",
            controller: "conversionController"
        })
        .when('/postback', {
            templateUrl: "views/postback.html",
            controller: "postbackController"
        })
        .when('/payroll', {
            templateUrl: "views/payroll.html",
            controller: "payrollController"
        })
        .when('/buyers', {
            templateUrl: "views/buyers.html",
            controller: "buyerController"
        })
        .when('/cbo', {
            templateUrl: "views/arbitratorExecutive.html",
            controller: "arbitratorExecutiveController"
        })
        .when('/arbitrator', {
            templateUrl: "views/arbitratorHomeScreen.html",
            controller: "arbitratorController"
        })
        .when('/statistic', {
            templateUrl: "views/statistic.html",
            controller: "statisticController"
        })
        .when('/sources', {
            templateUrl: "views/sources.html",
            controller: "sourcesController"
        })
        .when('/plan', {
            templateUrl: "views/planByBuyer.html",
            controller: "planByBuyerController"
        })
        .when('/buyer/dashboard', {
            templateUrl: "views/dashboard.html",
            controller: "dashboardController",
            resolve: {
                factory: checkRouting
            }
        })
        .when('/admin/dashboard', {
            templateUrl: "views/adminDashboard.html",
            controller: "adminDashboardController"

        });
}]);


var checkRouting = function ($q, $rootScope, $location, $http) {
    var request = new XMLHttpRequest();
    request.open('GET', '/user/me', false);  // `false` makes the request synchronous
    request.send(null);

    if (request.status === 200) {
        var z = JSON.parse(request.response);
        var role = z.authorities[0].authority;
        localStorage.setItem('role', role);
        if(role === "ADMIN"){
            $location.path('/admin/dashboard');
        }
        else if(role === "BUYER"){
            $location.path('/buyer/dashboard');
        }
        else{
            $location.path('/');
        }
    }
};