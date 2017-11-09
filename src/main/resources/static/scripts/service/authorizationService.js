"use strict";

Application.factory('authorizationService', function ($resource, $q, $rootScope, $location) {
    return {

        permissionModel: {
            permission: {},
            isPermissionLoaded: false
        },

        permissionCheck: function (roleCollection) {

            var deferred = $q.defer();

            var parentPointer = this;

            if (this.permissionModel.isPermissionLoaded) {
                this.getPermission(this.permissionModel, roleCollection, deferred);
            } else {
                $resource('/user/me').get().then(function (response) {
                    parentPointer.permissionModel.permission = response;
                    parentPointer.permissionModel.isPermissionLoaded = true;
                    parentPointer.getPermission(parentPointer.permissionModel, roleCollection, deferred);
                });
            }
            return deferred.promise;
        },


        getPermission: function (permissionModel, roleCollection, deferred) {
            var ifPermissionPassed = false;

            angular.forEach(roleCollection, function (role) {
                switch (role) {
                    case roles.BUYER:
                        if (permissionModel.permission.isBuyer) {
                            ifPermissionPassed = true;
                        }
                        break;
                    case roles.TEAM_LEADER:
                        if (permissionModel.permission.authority == "TEAM_LEADER") {
                            ifPermissionPassed = true;
                        }
                        break;
                    case roles.CBO:
                        if (permissionModel.permission.authority == "CBO") {
                            ifPermissionPassed = true;
                        }
                        break;
                    case roles.MENTOR:
                        if (permissionModel.permission.authority == "MENTOR") {
                            ifPermissionPassed = true;
                        }
                        break;
                    case roles.CFO:
                        if (permissionModel.permission.authority == "CFO") {
                            ifPermissionPassed = true;
                        }
                        break;
                    case roles.DIRECTOR:
                        if (permissionModel.permission.authority == "DIRECTOR") {
                            ifPermissionPassed = true;
                        }
                        break;
                    case roles.ADMIN:
                        if (permissionModel.permission.isAdmin) {
                            ifPermissionPassed = true;
                        }
                        break;
                    default:
                        ifPermissionPassed = false;
                }
            });
            if (!ifPermissionPassed) {
                $location.path(routeForUnauthorizedAccess);
                $rootScope.$on('$locationChangeSuccess', function (next, current) {
                    deferred.resolve();
                });
            } else {
                deferred.resolve();
            }
        }
    };
});
