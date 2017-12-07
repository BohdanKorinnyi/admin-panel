Application.controller("buyerHomeController", function ($scope, $http) {
    $scope.init = function () {
        $http.get('postback/buyers/revenue').then(function (value) {
            $scope.confirmed = value.data;
        });
        $http.get('buyer/plan/revenue').then(function (value) {
            $scope.plan = value.data;
        });
        $http.get('buyer/marginality').then(function (value) {
            $scope.marginality = value.data;
        });
    };
});