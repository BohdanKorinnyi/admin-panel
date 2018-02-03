Application.controller('mainController', function ($scope, $http) {
    $scope.username = "";
    $scope.getUsername = function () {
        $scope.username = localStorage.getItem("login");
    };
    $http.get('info').then(function (value) {
        $scope.version = value.data.version;
    });

    $scope.setActive = function (id1, id2) {
        for (var i = 1; i <= 32; i++) {
            var element = $('#el' + i);
            if (id1 === i || id2 === i) {
                element.addClass('active');
            } else {
                element.removeClass('active');
            }
        }
    }
});