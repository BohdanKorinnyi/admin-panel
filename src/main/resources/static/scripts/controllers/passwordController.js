Application.controller('passwordController', function ($scope, $http) {
    $scope.error = false;
    $scope.changePassword = function () {
        if ($scope.newPassword !== $scope.confirmPassword) {
            $scope.error = true;
            return;
        }
        $http.post('/user/password').then(function (value) {

        });
    }
});