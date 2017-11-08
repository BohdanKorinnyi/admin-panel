Application.config(function($routeProvider, $locationProvider) {
    $routeProvider
        .when('/login', {
            templateUrl : "views/login.html",
            controller  : "loginController"
        })
        .when('/advertiser', {
            templateUrl : "views/advertiser.html",
            controller  : "advertiserController"
        })
        .when('/conversions', {
            templateUrl : "views/conversion.html",
            controller  : "conversionController"
        })
        .when('/postback', {
            templateUrl : "views/postback.html",
            controller  : "postbackController"
        })
        .when('/payroll', {
            templateUrl : "views/payroll.html",
            controller  : "payrollController"
        })
        .when('/buyers', {
            templateUrl : "views/buyers.html",
            controller  : "buyerController"
        })
        .when('/cbo', {
            templateUrl : "views/arbitratorExecutive.html",
            controller  : "arbitratorExecutiveController"
        })
        .when('/arbitrator', {
            templateUrl : "views/arbitratorHomeScreen.html",
            controller  : "arbitratorController",
            resolve: {
                permission: function(authorizationService, $route) {
                    return authorizationService.permissionCheck([roles.DIRECTOR]);
                },
            }
        })
        .when('/cost', {
            templateUrl : "views/costDataReport.html",
            controller  : "costDataReportController"
        });
});
