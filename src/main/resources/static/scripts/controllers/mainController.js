Application.controller('mainController', function ($scope) {
    $scope.username = "";
    $scope.getUsername = function () {
        $scope.username = localStorage.getItem("login");
    };
});