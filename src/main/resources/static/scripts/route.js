Application.config(function($routeProvider) {
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
            templateUrl : "views/content.html",
            controller  : "contentController"
        })
        .when('/postback', {
            templateUrl : "views/postback.html",
            controller  : "postbackController"
        })
});
