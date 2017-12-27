Application.controller('mainController', function ($scope, $http) {
    $scope.username = "";
    $scope.getUsername = function () {
        $scope.username = localStorage.getItem("login");
    };
    $http.get('info').then(function (value) {
        $scope.version = value.data.version;
    });
});