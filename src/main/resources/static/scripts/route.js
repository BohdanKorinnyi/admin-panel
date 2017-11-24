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
            controller  : "arbitratorController"
            // resolve: {
            //     permission: function(authorizationService, $route) {
            //         return authorizationService.permissionCheck([roles.BUYER, roles.ADMIN]);
            //     },
            // }
        })
        .when('/statistic', {
            templateUrl : "views/statistic.html",
            controller  : "statisticController"
        })
        .when('/sources', {
            templateUrl : "views/sources.html",
            controller  : "sourcesController"
        })
        .when('/plan', {
        templateUrl : "views/planByBuyer.html",
        controller  : "planByBuyerController"
        })
        .when('/', {
            templateUrl : "views/buyerDetails.html",
            controller  : "buyerDetailsController"
        });
});
