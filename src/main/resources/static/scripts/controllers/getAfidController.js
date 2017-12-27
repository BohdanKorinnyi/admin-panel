Application.controller("getAfidController", function ($scope, $http) {

    $scope.buyersAfidsString = "";
    $scope.currentBuyerId = "";
    $scope.addedBuyersAfidCount = "";


    $scope.getBuyerId = function () {
        var request = new XMLHttpRequest();
        request.open('GET', '/user/me', false);
        request.send(null);

        if (request.status === 200) {
            var z = JSON.parse(request.response);
            $scope.getBuyersAfidById(z.buyerId);
            $scope.currentBuyerId = z.buyerId;
        }
    };

    $scope.getBuyersAfidById = function (id) {
        $scope.buyersAfidsString = '';
        if (id !== null) {
            $http.get(" /affiliates?buyer_id=" + id).then(function success(response) {
                $scope.buyersAfids = response.data;
                $scope.buyersAfidsString = response.data.join();
            }, function errorCallback() {
                notify('ti-alert', 'Error occurred during loading afids', 'danger');
            });
        }
    };


    $scope.addAfids = function () {
        if($scope.addedBuyersAfidCount > 10){
            notify('ti-alert', 'The number of added afIds can not be > 10', 'danger');
        }
        else{
            var url = "/affiliates?quantity="+$scope.addedBuyersAfidCount+"&buyer_id="+$scope.currentBuyerId;
            $http.post(url, $scope.addedBuyersAfidCount).then(function success(response){
                alertify.alert(response.data.join());
                $scope.getBuyersAfidById($scope.currentBuyerId);
            }, function errorCallback() {
                notify('ti-alert', 'Error occurred during adding afids', 'danger');
            });
        }

    };
});