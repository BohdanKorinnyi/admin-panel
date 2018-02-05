Application.controller('passwordController', function ($scope, $http) {
    $scope.error = false;
    $scope.changePassword = function () {
        if ($scope.newPassword !== $scope.confirmPassword) {
            $scope.error = true;
            return;
        }
        $http.get('/user/password' +
            '?existing=' + $scope.currentPassword +
            '&created=' + $scope.newPassword +
            '&confirmed=' + $scope.confirmPassword).then(function (value) {
            notify('ti-alert', 'The password was changed', 'success');
        },function (reason) {
            notify('ti-alert', 'Error occurred during changing password', 'danger');
        });
    }
});